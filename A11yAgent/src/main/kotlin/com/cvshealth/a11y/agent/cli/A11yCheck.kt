package com.cvshealth.a11y.agent.cli

import com.cvshealth.a11y.agent.core.*
import com.cvshealth.a11y.agent.formatters.*
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.double
import com.github.ajalt.clikt.parameters.types.enum
import kotlinx.serialization.json.*
import java.io.File
import kotlin.system.exitProcess

enum class OutputFormat { TERMINAL, JSON, SARIF, HTML, GRADLE }

class A11yCheck : CliktCommand(
    name = "a11y-check-android"
) {
    val paths by argument(help = "Files or directories to analyze")
        .multiple(default = listOf("."))

    val format by option("--format", help = "Output format")
        .enum<OutputFormat>(ignoreCase = true)
        .default(OutputFormat.TERMINAL)

    val disable by option("--disable", help = "Comma-separated rule IDs to disable")

    val only by option("--only", help = "Minimum severity to show (info, warning, error)")
        .enum<A11ySeverity>(ignoreCase = true)

    val listRules by option("--list-rules", help = "List all available rules and exit")
        .flag()

    val config by option("--config", help = "Path to .a11ycheck.yml config file")

    val diff by option("--diff", help = "Only check lines changed in git diff")
        .flag()

    val diffBase by option("--diff-base", help = "Git ref to diff against")

    val minScore by option("--min-score", help = "Exit 1 if score below threshold")
        .double()

    val trend by option("--trend", help = "Track score history")
        .flag("--no-trend", default = true)

    val baselineSave by option("--baseline-save", help = "Save current issues as baseline")
        .flag()

    val baseline by option("--baseline", help = "Filter known baseline issues")
        .flag()

    val badge by option("--badge", help = "Output SVG score badge")
        .flag()

    val generateDocs by option("--generate-docs", help = "Generate rule documentation")
        .flag()

    val fix by option("--fix", help = "Automatically apply available fixes")
        .flag()

    val dryRun by option("--dry-run", help = "Preview fixes without applying")
        .flag()

    val perComposable by option("--per-composable", help = "Score each @Composable function separately")
        .flag()

    val watch by option("--watch", help = "Watch for file changes and re-run")
        .flag()

    val lines by option("--lines", help = "Only check lines in a range, e.g. 50-120 or 10-30,80-100")

    val diffReport by option("--diff-report", help = "Compare against a previous JSON report")

    val perFile by option("--per-file", help = "Show per-file accessibility scores")
        .flag()

    val compact by option("--compact", help = "Suppress file path in output")
        .flag()

    override fun run() {
        val registry = RuleRegistry()

        // Load config
        val loadedConfig = if (config != null) {
            ConfigLoader.loadAt(config!!)
        } else {
            val firstPath = paths.firstOrNull() ?: "."
            ConfigLoader.load(firstPath)
        }
        registry.applyConfig(loadedConfig)

        // Apply --disable
        disable?.split(",")?.map { it.trim() }?.forEach {
            registry.disabledRuleIDs.add(it)
        }

        // --list-rules
        if (listRules) {
            printRuleList(registry)
            return
        }

        // --generate-docs
        if (generateDocs) {
            printRuleDocs(registry)
            return
        }

        // Collect diagnostics
        registry.clearComposables()
        var diagnostics = mutableListOf<A11yDiagnostic>()
        val allFilePaths = mutableListOf<String>()

        for (path in paths) {
            val file = File(path)
            if (file.isFile) {
                diagnostics.addAll(registry.analyzeFile(file.absolutePath))
                allFilePaths.add(file.absolutePath)
            } else if (file.isDirectory) {
                diagnostics.addAll(registry.analyzeDirectory(file.absolutePath))
                allFilePaths.addAll(registry.allFilePaths(file.absolutePath))
            }
        }

        // Apply filters
        if (diff) {
            val workDir = File(paths.first()).let { if (it.isFile) it.parentFile else it }.absolutePath
            val changedLines = DiffFilter.changedLines(workDir, diffBase)
            diagnostics = DiffFilter.filter(diagnostics, changedLines).toMutableList()
        }

        // Lines range filter
        if (lines != null) {
            val ranges = parseLineRanges(lines!!)
            if (ranges.isNotEmpty()) {
                diagnostics = diagnostics.filter { diag ->
                    ranges.any { diag.line in it }
                }.toMutableList()
            }
        }

        // Severity filter
        if (only != null) {
            diagnostics = diagnostics.filter { it.severity >= only!! }.toMutableList()
        }

        // Auto-fix
        if (fix || dryRun) {
            val fixer = AutoFixer()
            val result = fixer.applyFixes(diagnostics, dryRun)
            echo(fixer.formatResult(result, dryRun))

            if (fix && !dryRun && result.totalFixed > 0) {
                // Re-analyze after applying fixes
                registry.clearComposables()
                diagnostics = mutableListOf()
                for (path in paths) {
                    val file = File(path)
                    if (file.isFile) {
                        diagnostics.addAll(registry.analyzeFile(file.absolutePath))
                    } else if (file.isDirectory) {
                        diagnostics.addAll(registry.analyzeDirectory(file.absolutePath))
                    }
                }
                // Re-apply severity filter
                if (only != null) {
                    diagnostics = diagnostics.filter { it.severity >= only!! }.toMutableList()
                }
            }
        }

        // Calculate score
        val score = ScoreCalculator().calculate(diagnostics, registry.enabledRules, allFilePaths)

        // --baseline-save
        if (baselineSave) {
            val baselineObj = Baseline.from(diagnostics, score.score)
            val dir = File(paths.first()).let { if (it.isFile) it.parentFile else it }.absolutePath
            baselineObj.save(dir)
            echo("Baseline saved with ${diagnostics.size} issues (score: ${"%.1f".format(score.score)})")
            return
        }

        // --baseline
        if (baseline) {
            val dir = File(paths.first()).let { if (it.isFile) it.parentFile else it }.absolutePath
            val baselineObj = Baseline.loadFrom(dir)
            if (baselineObj != null) {
                diagnostics = baselineObj.filterNew(diagnostics).toMutableList()
            }
        }

        // --diff-report
        if (diffReport != null) {
            val reportFile = File(diffReport!!)
            if (reportFile.exists()) {
                val json = Json.parseToJsonElement(reportFile.readText()).jsonObject
                val oldDiags = json["diagnostics"]?.jsonArray
                if (oldDiags != null) {
                    val oldFingerprints = oldDiags.mapNotNull { d ->
                        val obj = d.jsonObject
                        val ruleID = obj["ruleID"]?.jsonPrimitive?.contentOrNull
                        val filePath = obj["filePath"]?.jsonPrimitive?.contentOrNull
                        val message = obj["message"]?.jsonPrimitive?.contentOrNull
                        if (ruleID != null && filePath != null && message != null)
                            "$ruleID|$filePath|$message"
                        else null
                    }.toSet()

                    val before = diagnostics.size
                    diagnostics = diagnostics.filter { diag ->
                        val fp = "${diag.ruleID}|${diag.filePath}|${diag.message}"
                        fp !in oldFingerprints
                    }.toMutableList()
                    System.err.println("Diff report: ${before - diagnostics.size} existing issues filtered, ${diagnostics.size} new issues")
                }
            }
        }

        // Trend tracking
        val tracker = if (trend) {
            val dir = File(paths.first()).let { if (it.isFile) it.parentFile else it }.absolutePath
            TrendTracker(dir)
        } else null

        val trendHistory = tracker?.load()

        // --badge
        if (badge) {
            echo(generateBadgeSvg(score))
            return
        }

        // Format output
        val output = when (format) {
            OutputFormat.TERMINAL -> {
                val main = TerminalFormatter.format(diagnostics, score, compact)
                val trendOutput = tracker?.formatTrend(score) ?: ""
                main + trendOutput
            }
            OutputFormat.JSON -> JsonFormatter.format(diagnostics, score, trendHistory?.entries)
            OutputFormat.SARIF -> SarifFormatter.format(diagnostics, registry.enabledRules)
            OutputFormat.HTML -> HtmlFormatter.format(diagnostics, score, trendHistory?.entries ?: emptyList(), registry.enabledRules)
            OutputFormat.GRADLE -> GradleFormatter.format(diagnostics, score, compact)
        }

        echo(output)

        // Per-composable scoring
        if (perComposable) {
            val scorer = ComposableScorer()
            val scores = scorer.scoreComposables(registry.composables, diagnostics, registry.enabledRules)
            echo(scorer.formatScores(scores))
        }

        // Per-file scoring
        if (perFile && score.fileScores.isNotEmpty()) {
            echo(formatFileScores(score.fileScores))
        }

        // Record trend
        tracker?.record(score)

        // Exit code
        val hasErrors = diagnostics.any { it.severity == A11ySeverity.ERROR }
        val belowMinScore = minScore != null && score.score < minScore!!

        if (hasErrors || belowMinScore) {
            if (!watch) exitProcess(1)
        }

        // Watch mode
        if (watch) {
            System.err.println("Watching for changes... (press Ctrl+C to stop)")
            var lastModified = latestModificationTime(allFilePaths)
            while (true) {
                Thread.sleep(1000)
                val current = latestModificationTime(allFilePaths)
                if (current > lastModified) {
                    lastModified = current
                    System.err.println("\n--- File change detected, re-running... ---\n")
                    val javaHome = System.getProperty("java.home")
                    val javaBin = "$javaHome/bin/java"
                    val classpath = System.getProperty("java.class.path")
                    val mainClass = "com.cvshealth.a11y.agent.cli.A11yCheckKt"
                    val args = currentContext.originalArgv.filter { it != "--watch" }
                    val pb = ProcessBuilder(listOf(javaBin, "-cp", classpath, mainClass) + args)
                    pb.inheritIO()
                    pb.start().waitFor()
                }
            }
        }
    }

    private fun parseLineRanges(spec: String): List<IntRange> {
        return spec.split(",").mapNotNull { part ->
            val trimmed = part.trim()
            val components = trimmed.split("-", limit = 2)
            when (components.size) {
                2 -> {
                    val start = components[0].toIntOrNull()
                    val end = components[1].toIntOrNull()
                    if (start != null && end != null && start <= end) start..end else null
                }
                1 -> components[0].toIntOrNull()?.let { it..it }
                else -> null
            }
        }
    }

    private fun latestModificationTime(filePaths: List<String>): Long {
        return filePaths.maxOfOrNull { File(it).lastModified() } ?: 0L
    }

    private fun printRuleList(registry: RuleRegistry) {
        echo("Available rules (${registry.rules.size}):\n")
        val maxIdLen = registry.rules.maxOf { it.id.length }
        for (rule in registry.rules.sortedBy { it.id }) {
            val enabled = if (rule.id in registry.disabledRuleIDs) " [disabled]" else ""
            val wcag = rule.wcagCriteria.joinToString(", ")
            echo("  ${rule.id.padEnd(maxIdLen + 2)} ${rule.severity.label.padEnd(7)} [${rule.impact.label.padEnd(8)}] WCAG $wcag$enabled")
            echo("    ${rule.description}")
        }
    }

    private fun printRuleDocs(registry: RuleRegistry) {
        echo(RuleDocsGenerator().generate(registry.rules))
    }

    private fun formatFileScores(fileScores: List<FileScore>): String {
        val sb = StringBuilder()
        sb.appendLine("\nPer-File Scores:")
        val sorted = fileScores.sortedBy { it.score }
        for (fs in sorted) {
            val filled = (fs.score / 100.0 * 20).toInt().coerceIn(0, 20)
            val bar = "\u2588".repeat(filled) + "\u2591".repeat(20 - filled)
            val grade = A11yScore.letterGrade(fs.score)
            val shortPath = fs.filePath.substringAfterLast("/")
            val issues = mutableListOf<String>()
            if (fs.errorCount > 0) issues.add("${fs.errorCount} error${if (fs.errorCount != 1) "s" else ""}")
            if (fs.warningCount > 0) issues.add("${fs.warningCount} warning${if (fs.warningCount != 1) "s" else ""}")
            val issueStr = if (issues.isNotEmpty()) "  (${issues.joinToString(", ")})" else ""
            sb.appendLine("  [$bar]  ${"%.1f".format(fs.score)} ($grade)  $shortPath$issueStr")
        }
        return sb.toString()
    }

    private fun generateBadgeSvg(score: A11yScore): String {
        val color = when {
            score.score >= 90 -> "#4c1"
            score.score >= 80 -> "#97CA00"
            score.score >= 70 -> "#dfb317"
            score.score >= 60 -> "#fe7d37"
            else -> "#e05d44"
        }
        val scoreText = "${"%.0f".format(score.score)}%"
        val label = "a11y score"
        return """<svg xmlns="http://www.w3.org/2000/svg" width="140" height="20" role="img" aria-label="$label: $scoreText">
  <title>$label: $scoreText</title>
  <linearGradient id="b" x2="0" y2="100%"><stop offset="0" stop-color="#bbb" stop-opacity=".1"/><stop offset="1" stop-opacity=".1"/></linearGradient>
  <mask id="a"><rect width="140" height="20" rx="3" fill="#fff"/></mask>
  <g mask="url(#a)"><rect width="80" height="20" fill="#555"/><rect x="80" width="60" height="20" fill="$color"/><rect width="140" height="20" fill="url(#b)"/></g>
  <g fill="#fff" text-anchor="middle" font-family="DejaVu Sans,sans-serif" font-size="11">
    <text aria-hidden="true" x="40" y="15" fill="#010101" fill-opacity=".3">$label</text>
    <text x="40" y="14">$label</text>
    <text aria-hidden="true" x="110" y="15" fill="#010101" fill-opacity=".3">$scoreText</text>
    <text x="110" y="14">$scoreText</text>
  </g>
</svg>"""
    }
}

fun main(args: Array<String>) = A11yCheck().main(args)
