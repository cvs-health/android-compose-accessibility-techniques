package com.cvshealth.a11y.agent.formatters

import com.cvshealth.a11y.agent.core.*
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object HtmlFormatter {

    fun format(
        diagnostics: List<A11yDiagnostic>,
        score: A11yScore,
        trendEntries: List<TrendTracker.Entry> = emptyList(),
        allRules: List<A11yRule> = emptyList()
    ): String {
        val byFile = diagnostics.groupBy { it.filePath }
        val byRule = diagnostics.groupBy { it.ruleID }
        val timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")
            .withZone(ZoneId.systemDefault())
            .format(Instant.now())

        return buildString {
            appendLine("<!DOCTYPE html>")
            appendLine("<html lang=\"en\">")
            appendLine("<head>")
            appendLine("<meta charset=\"UTF-8\">")
            appendLine("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
            appendLine("<title>Compose Accessibility Report</title>")
            appendLine("<style>")
            appendLine(CSS)
            appendLine("</style>")
            appendLine("</head>")
            appendLine("<body>")

            // Page header
            appendLine("<h1>Compose Accessibility Report</h1>")
            appendLine("<p class=\"timestamp\">Generated: $timestamp</p>")

            // Summary stat cards
            appendLine("<div class=\"summary\">")
            appendLine("<div class=\"stat error\"><div class=\"number\">${score.totalErrors}</div><div class=\"label\">Errors</div></div>")
            appendLine("<div class=\"stat warning\"><div class=\"number\">${score.totalWarnings}</div><div class=\"label\">Warnings</div></div>")
            appendLine("<div class=\"stat info\"><div class=\"number\">${score.totalInfo}</div><div class=\"label\">Info</div></div>")
            appendLine("<div class=\"stat\"><div class=\"number\">${score.filesAnalyzed}</div><div class=\"label\">Files</div></div>")
            appendLine("</div>")

            // Score section
            val gradeClass = when (score.grade.first()) {
                'A' -> "grade-a"; 'B' -> "grade-b"; 'C' -> "grade-c"; 'D' -> "grade-d"; else -> "grade-f"
            }
            appendLine("<div class=\"score-section\">")
            appendLine("<div class=\"score-header-row\">")
            appendLine("<div class=\"score-big\">${"%.1f".format(score.score)}</div>")
            appendLine("<div>")
            appendLine("<div class=\"score-grade $gradeClass\">${score.grade}</div>")
            appendLine("<div class=\"score-subtitle\">WCAG 2.2 Accessibility Score</div>")
            appendLine("</div>")
            appendLine("</div>")
            appendLine("<div class=\"score-stats\">")
            appendLine("<span>${score.criteriaPassed} criteria passed</span>")
            appendLine("<span>${score.criteriaFailed} failed</span>")
            appendLine("<span>${score.filesAnalyzed} files analyzed</span>")
            appendLine("</div>")

            // Failed criteria list
            val failedCriteria = score.criteriaScores.filter { it.status == CriterionStatus.FAIL }
            if (failedCriteria.isNotEmpty()) {
                appendLine("<div class=\"failed-criteria\">")
                appendLine("<h3><span class=\"badge badge-fail\">Failed WCAG Criteria</span></h3>")
                appendLine("<ul>")
                for (c in failedCriteria) {
                    val anchor = wcagAnchor(c.criterion)
                    appendLine("<li><span class=\"badge badge-fail\">\u2717</span> <a href=\"https://www.w3.org/WAI/WCAG22/Understanding/$anchor\"><span class=\"criterion-id\">${c.criterion}</span></a> ${c.name} <span class=\"criterion-counts\">${c.errorCount} errors, ${c.warningCount} warnings</span></li>")
                }
                appendLine("</ul>")
                appendLine("</div>")
            }

            val reviewCriteria = score.criteriaScores.filter { it.status == CriterionStatus.REVIEW }
            if (reviewCriteria.isNotEmpty()) {
                appendLine("<div class=\"failed-criteria\">")
                appendLine("<h3><span class=\"badge badge-warning\">Needs Review</span></h3>")
                appendLine("<ul>")
                for (c in reviewCriteria) {
                    val anchor = wcagAnchor(c.criterion)
                    appendLine("<li><span class=\"badge badge-warning\">\u26A0</span> <a href=\"https://www.w3.org/WAI/WCAG22/Understanding/$anchor\"><span class=\"criterion-id\">${c.criterion}</span></a> ${c.name} <span class=\"criterion-counts\">${c.warningCount} warnings, ${c.infoCount} info</span></li>")
                }
                appendLine("</ul>")
                appendLine("</div>")
            }
            appendLine("</div>")

            // Trend section
            if (trendEntries.isNotEmpty()) {
                appendLine("<div class=\"trend-section\">")
                appendLine("<h2>Score Trend</h2>")

                // Delta from last run
                val lastEntry = trendEntries.last()
                val delta = score.score - lastEntry.score
                val deltaClass = when { delta > 0 -> "positive"; delta < 0 -> "negative"; else -> "neutral" }
                val deltaStr = when { delta > 0 -> "+${"%.1f".format(delta)}"; delta < 0 -> "${"%.1f".format(delta)}"; else -> "\u00b10.0" }
                appendLine("<div class=\"trend-delta $deltaClass\">Change from last run: $deltaStr</div>")

                // SVG trend chart
                val allScores = trendEntries.map { it.score } + score.score
                val chartW = 680; val chartH = 200
                val padL = 40; val padR = 20; val padT = 20; val padB = 40
                val plotW = chartW - padL - padR
                val plotH = chartH - padT - padB
                val minY = (allScores.minOrNull() ?: 0.0).coerceAtMost(50.0)
                val maxY = (allScores.maxOrNull() ?: 100.0).coerceAtLeast(100.0)
                val rangeY = maxY - minY

                appendLine("<div class=\"trend-chart\">")
                appendLine("<svg viewBox=\"0 0 $chartW $chartH\" xmlns=\"http://www.w3.org/2000/svg\">")

                // Grid lines
                for (v in listOf(0, 25, 50, 75, 100).filter { it >= minY && it <= maxY }) {
                    val y = padT + plotH - ((v - minY) / rangeY * plotH)
                    appendLine("<line x1=\"$padL\" y1=\"${"%.1f".format(y)}\" x2=\"${chartW - padR}\" y2=\"${"%.1f".format(y)}\" class=\"chart-grid\"/>")
                    appendLine("<text x=\"${padL - 5}\" y=\"${"%.1f".format(y + 4)}\" class=\"chart-label\" text-anchor=\"end\">$v</text>")
                }

                // Points
                val points = allScores.mapIndexed { i, s ->
                    val x = if (allScores.size == 1) padL + plotW / 2.0 else padL + i.toDouble() / (allScores.size - 1) * plotW
                    val y = padT + plotH - ((s - minY) / rangeY * plotH)
                    Pair(x, y)
                }

                // Area fill
                val areaPath = points.joinToString(" ") { "%.1f,%.1f".format(it.first, it.second) }
                val firstX = "%.1f".format(points.first().first)
                val lastX = "%.1f".format(points.last().first)
                val bottomY = "%.1f".format(padT + plotH.toDouble())
                appendLine("<polygon points=\"$firstX,$bottomY $areaPath $lastX,$bottomY\" class=\"chart-area\"/>")

                // Line
                val linePath = points.joinToString(" ") { "%.1f,%.1f".format(it.first, it.second) }
                appendLine("<polyline points=\"$linePath\" class=\"chart-line\"/>")

                // Dots and labels
                val dates = trendEntries.map { it.date.take(10) } + "Now"
                points.forEachIndexed { i, (x, y) ->
                    val dotClass = if (i == points.size - 1) "chart-dot-current" else "chart-dot"
                    val r = if (i == points.size - 1) 5 else 4
                    appendLine("<circle cx=\"${"%.1f".format(x)}\" cy=\"${"%.1f".format(y)}\" r=\"$r\" class=\"$dotClass\"/>")
                    appendLine("<text x=\"${"%.1f".format(x)}\" y=\"${"%.1f".format(y - 10)}\" class=\"chart-label\" text-anchor=\"middle\">${"%.0f".format(allScores[i])}</text>")
                    appendLine("<text x=\"${"%.1f".format(x)}\" y=\"${chartH - 5}\" class=\"chart-label\" text-anchor=\"middle\">${dates[i]}</text>")
                }

                appendLine("</svg>")
                appendLine("</div>")

                // History table
                appendLine("<table class=\"trend-table\">")
                appendLine("<thead><tr><th>Date</th><th>Score</th><th>Grade</th><th>Errors</th><th>Change</th></tr></thead>")
                appendLine("<tbody>")
                var prevScore: Double? = null
                for (entry in trendEntries) {
                    val d = if (prevScore != null) entry.score - prevScore!! else 0.0
                    val changeHtml = when {
                        prevScore == null -> "<span style=\"color:#595f64\">\u2014</span>"
                        d > 0 -> "<span style=\"color:var(--pass)\">+${"%.1f".format(d)}</span>"
                        d < 0 -> "<span style=\"color:var(--error)\">${"%.1f".format(d)}</span>"
                        else -> "<span style=\"color:#595f64\">0.0</span>"
                    }
                    appendLine("<tr><td>${entry.date.take(10)}</td><td>${"%.1f".format(entry.score)}</td><td>${entry.grade}</td><td>${entry.errors}</td><td>$changeHtml</td></tr>")
                    prevScore = entry.score
                }
                // Current row
                val currentDelta = if (prevScore != null) score.score - prevScore!! else 0.0
                val currentChangeHtml = when {
                    prevScore == null -> "<span style=\"color:#595f64\">\u2014</span>"
                    currentDelta > 0 -> "<span style=\"color:var(--pass)\">+${"%.1f".format(currentDelta)}</span>"
                    currentDelta < 0 -> "<span style=\"color:var(--error)\">${"%.1f".format(currentDelta)}</span>"
                    else -> "<span style=\"color:#595f64\">0.0</span>"
                }
                appendLine("<tr style=\"font-weight:700\"><td>Now</td><td>${"%.1f".format(score.score)}</td><td>${score.grade}</td><td>${score.totalErrors}</td><td>$currentChangeHtml</td></tr>")
                appendLine("</tbody></table>")
                appendLine("</div>")
            }

            // WCAG criteria table
            appendLine("<h2>WCAG 2.2 Conformance</h2>")
            appendLine("<table class=\"criteria-table\">")
            appendLine("<thead><tr><th>Criterion</th><th>Name</th><th>Level</th><th>Status</th><th>Issues</th></tr></thead>")
            appendLine("<tbody>")
            for (c in score.criteriaScores.filter { it.status != CriterionStatus.NOT_CHECKED }) {
                val statusBadge = when (c.status) {
                    CriterionStatus.PASS -> "<span class=\"badge badge-pass\">\u2713 Pass</span>"
                    CriterionStatus.FAIL -> "<span class=\"badge badge-fail\">\u2717 Fail</span>"
                    CriterionStatus.REVIEW -> "<span class=\"badge badge-warning\">\u26A0 Review</span>"
                    CriterionStatus.NOT_CHECKED -> "<span class=\"badge\">\u00b7 N/A</span>"
                }
                val issues = mutableListOf<String>()
                if (c.errorCount > 0) issues.add("${c.errorCount}E")
                if (c.warningCount > 0) issues.add("${c.warningCount}W")
                if (c.infoCount > 0) issues.add("${c.infoCount}I")
                val issueStr = issues.joinToString(" ").ifEmpty { "\u2014" }
                val anchor = wcagAnchor(c.criterion)
                appendLine("<tr>")
                appendLine("<td><a href=\"https://www.w3.org/WAI/WCAG22/Understanding/$anchor\">${c.criterion}</a></td>")
                appendLine("<td>${c.name}</td><td>${c.level.label}</td>")
                appendLine("<td class=\"status-cell\">$statusBadge</td>")
                appendLine("<td>$issueStr</td>")
                appendLine("</tr>")
            }
            appendLine("</tbody></table>")

            // Build rule name lookup
            val ruleNames = allRules.associate { it.id to it.name }

            // Issues by File (expand/collapse)
            appendLine("<h2>Issues by File</h2>")
            for ((filePath, fileDiags) in byFile.entries.sortedBy { it.key }) {
                val shortPath = filePath.substringAfterLast("/").ifEmpty { filePath }
                val errorCount = fileDiags.count { it.severity == A11ySeverity.ERROR }
                val warningCount = fileDiags.count { it.severity == A11ySeverity.WARNING }

                appendLine("<details>")
                append("<summary>$shortPath \u2014 ${fileDiags.size} issue(s)")
                if (errorCount > 0) append(" <span class=\"badge badge-error\">$errorCount errors</span>")
                if (warningCount > 0) append(" <span class=\"badge badge-warning\">$warningCount warnings</span>")
                appendLine("</summary>")
                appendLine("<div class=\"diag-list\">")

                for (diag in fileDiags.sortedBy { it.line }) {
                    val ruleName = ruleNames[diag.ruleID] ?: diag.ruleID
                    appendLine("<div class=\"diag\">")
                    appendLine("<div class=\"diag-title\">${escapeHtml(ruleName)}</div>")
                    appendLine("<span class=\"badge badge-${diag.severity.label}\">${diag.severity.label}</span>")
                    appendLine("<span class=\"badge badge-${diag.impact.label.lowercase()}\">${diag.impact.label}</span>")
                    appendLine("<span class=\"diag-loc\">${diag.line}:${diag.column}</span>")
                    appendLine(escapeHtml(diag.message))
                    appendLine("<span class=\"diag-rule\">${diag.ruleID}</span>")
                    if (diag.wcagCriteria.isNotEmpty()) {
                        val links = diag.wcagCriteria.joinToString(", ") { c ->
                            "<a href=\"https://www.w3.org/WAI/WCAG22/Understanding/${wcagAnchor(c)}\">${c}</a>"
                        }
                        appendLine("<span class=\"diag-wcag\">WCAG $links</span>")
                    }

                    // Bad code block
                    diag.sourceSnippet?.let { snippet ->
                        appendLine("<div class=\"code-label bad-label\">Current code</div>")
                        appendLine("<div class=\"code-block\">")
                        for (line in snippet.lines()) {
                            val escaped = escapeHtml(line)
                            if (line.startsWith(">")) {
                                appendLine("<span class=\"line-bad\">$escaped</span>")
                            } else {
                                appendLine(escaped)
                            }
                        }
                        appendLine("</div>")
                    }

                    // Fix suggestion
                    diag.suggestion?.let {
                        appendLine("<div class=\"suggestion\">${escapeHtml(it)}</div>")
                    }

                    // Good code block
                    if (diag.sourceSnippet != null && diag.suggestion != null) {
                        val corrected = generateCorrectedSnippet(diag.sourceSnippet!!, diag.suggestion!!)
                        if (corrected != null) {
                            appendLine("<div class=\"code-label good-label\">Fixed code</div>")
                            appendLine("<div class=\"fix-block\">")
                            for (line in corrected.lines()) {
                                val escaped = escapeHtml(line)
                                if (line.startsWith(">")) {
                                    appendLine("<span class=\"line-good\">$escaped</span>")
                                } else {
                                    appendLine(escaped)
                                }
                            }
                            appendLine("</div>")
                        }
                    }
                    appendLine("</div>")
                }
                appendLine("</div>")
                appendLine("</details>")
            }

            // Issues by Rule summary table
            if (allRules.isNotEmpty()) {
                appendLine("<h2>Issues by Rule</h2>")
                appendLine("<table class=\"criteria-table\">")
                appendLine("<thead><tr><th>Rule</th><th>Severity</th><th>Impact</th><th>Count</th><th>WCAG</th></tr></thead>")
                appendLine("<tbody>")
                for (rule in allRules.sortedBy { it.id }) {
                    val count = byRule[rule.id]?.size ?: 0
                    appendLine("<tr><td>${rule.id}</td><td><span class=\"badge badge-${rule.severity.label}\">${rule.severity.label}</span></td><td>${rule.impact.label}</td><td>$count</td><td>${rule.wcagCriteria.joinToString(", ")}</td></tr>")
                }
                appendLine("</tbody></table>")
            }

            appendLine("<footer>Generated by a11y-check-android v0.1.0</footer>")
            appendLine("</body></html>")
        }
    }

    /**
     * Heuristically generates a corrected version of the source snippet by applying the suggestion.
     */
    private fun generateCorrectedSnippet(snippet: String, suggestion: String): String? {
        val lines = snippet.lines().toMutableList()
        val flaggedIdx = lines.indexOfFirst { it.startsWith(">") }
        if (flaggedIdx == -1) return null

        val flaggedLine = lines[flaggedIdx]
        val prefixMatch = Regex("""^>\s*(\d+)\s*\|\s?(.*)$""").find(flaggedLine) ?: return null
        val lineNum = prefixMatch.groupValues[1]
        val originalCode = prefixMatch.groupValues[2]
        val indent = originalCode.takeWhile { it == ' ' }

        val modifierPattern = Regex("""(Modifier\.\w+\s*(?:\([^)]*\)|\{[^}]*\}))""")
        val modifierMatch = modifierPattern.find(suggestion)

        val paramPattern = Regex("""(contentDescription\s*=\s*\S+(?:\([^)]*(?:\([^)]*\)[^)]*)*\))?|label\s*=\s*\{[^}]*\}|label\s*=\s*\{?\s*Text\([^)]*\)\s*\}?|enabled\s*=\s*\w+|text\s*=\s*\{[^}]*\})""")
        val paramMatch = paramPattern.find(suggestion)

        val correctedLines = lines.toMutableList()

        if (suggestion.contains("contentDescription = null") && suggestion.contains("contentDescription =")) {
            val replacement = paramMatch?.groupValues?.get(1) ?: "contentDescription = stringResource(R.string.description)"
            val fixed = originalCode.replace(Regex("""contentDescription\s*=\s*null"""), replacement)
            correctedLines[flaggedIdx] = "> $lineNum | $fixed"
            return correctedLines.joinToString("\n")
        }

        if (suggestion.startsWith("Add ") || suggestion.startsWith("Use ")) {
            if (modifierMatch != null) {
                val modifierCode = modifierMatch.groupValues[1]
                correctedLines[flaggedIdx] = "> $lineNum | $originalCode"
                correctedLines.add(flaggedIdx + 1, "> $lineNum |     $indent.$modifierCode")
                return correctedLines.joinToString("\n")
            }
            if (paramMatch != null) {
                val param = paramMatch.groupValues[1]
                if (originalCode.contains("(")) {
                    val fixed = if (originalCode.trimEnd().endsWith("(")) {
                        "$originalCode\n> $lineNum |     $indent$param,"
                    } else {
                        originalCode.replace(")", ", $param)")
                    }
                    correctedLines[flaggedIdx] = "> $lineNum | $fixed"
                    return correctedLines.joinToString("\n")
                } else {
                    correctedLines.add(flaggedIdx + 1, "> $lineNum |     $indent$param")
                    return correctedLines.joinToString("\n")
                }
            }
        }

        if (suggestion.contains("Change ") && suggestion.contains(" to ")) {
            val changePattern = Regex("""Change\s+(\S+)\s+to\s+(\S+)""")
            val changeMatch = changePattern.find(suggestion)
            if (changeMatch != null) {
                val from = changeMatch.groupValues[1]
                val to = changeMatch.groupValues[2]
                val fixed = originalCode.replace(from, to)
                if (fixed != originalCode) {
                    correctedLines[flaggedIdx] = "> $lineNum | $fixed"
                    return correctedLines.joinToString("\n")
                }
            }
        }

        if (suggestion.contains("Remove ")) {
            val removePattern = Regex("""Remove\s+"([^"]+)"""")
            val removeMatch = removePattern.find(suggestion)
            if (removeMatch != null) {
                val toRemove = removeMatch.groupValues[1]
                val fixed = originalCode.replace(toRemove, "").replace("  ", " ")
                correctedLines[flaggedIdx] = "> $lineNum | $fixed"
                return correctedLines.joinToString("\n")
            }
        }

        // Fallback: try any code-like pattern
        if (modifierMatch != null || paramMatch != null) {
            val fixCode = modifierMatch?.groupValues?.get(1) ?: paramMatch?.groupValues?.get(1) ?: return null
            correctedLines[flaggedIdx] = "> $lineNum | $originalCode"
            correctedLines.add(flaggedIdx + 1, "> $lineNum |     $indent$fixCode")
            return correctedLines.joinToString("\n")
        }

        val genericCodePattern = Regex("""(\w+\s*=\s*\w+\([^)]*(?:\([^)]*\)[^)]*)*\)|\.\w+\s*\([^)]*\)|\.\w+\s*\{[^}]*\}|semantics\s*\{[^}]*\})""")
        val genericMatch = genericCodePattern.find(suggestion)
        if (genericMatch != null) {
            val fixCode = genericMatch.groupValues[1]
            correctedLines[flaggedIdx] = "> $lineNum | $originalCode"
            correctedLines.add(flaggedIdx + 1, "> $lineNum |     $indent$fixCode")
            return correctedLines.joinToString("\n")
        }

        return null
    }

    private fun escapeHtml(text: String): String =
        text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")

    private fun wcagAnchor(criterion: String): String {
        val slugs = mapOf(
            "1.1.1" to "non-text-content", "1.2.1" to "audio-only-and-video-only-prerecorded",
            "1.3.1" to "info-and-relationships", "1.3.2" to "meaningful-sequence",
            "1.3.3" to "sensory-characteristics", "1.3.4" to "orientation", "1.3.5" to "identify-input-purpose",
            "1.4.1" to "use-of-color", "1.4.2" to "audio-control", "1.4.3" to "contrast-minimum",
            "1.4.4" to "resize-text", "1.4.5" to "images-of-text",
            "1.4.10" to "reflow", "1.4.11" to "non-text-contrast", "1.4.12" to "text-spacing",
            "1.4.13" to "content-on-hover-or-focus",
            "2.1.1" to "keyboard", "2.1.2" to "no-keyboard-trap", "2.1.4" to "character-key-shortcuts",
            "2.2.1" to "timing-adjustable", "2.2.2" to "pause-stop-hide",
            "2.3.1" to "three-flashes-or-below-threshold",
            "2.4.1" to "bypass-blocks", "2.4.2" to "page-titled", "2.4.3" to "focus-order",
            "2.4.4" to "link-purpose-in-context", "2.4.5" to "multiple-ways",
            "2.4.6" to "headings-and-labels", "2.4.7" to "focus-visible", "2.4.11" to "focus-not-obscured-minimum",
            "2.5.1" to "pointer-gestures", "2.5.2" to "pointer-cancellation",
            "2.5.3" to "label-in-name", "2.5.4" to "motion-actuation",
            "2.5.7" to "dragging-movements", "2.5.8" to "target-size-minimum",
            "3.1.1" to "language-of-page", "3.1.2" to "language-of-parts",
            "3.2.1" to "on-focus", "3.2.2" to "on-input", "3.2.6" to "consistent-help",
            "3.3.1" to "error-identification", "3.3.2" to "labels-or-instructions",
            "3.3.3" to "error-suggestion", "3.3.4" to "error-prevention-legal-financial-data",
            "3.3.7" to "redundant-entry", "3.3.8" to "accessible-authentication-minimum",
            "4.1.2" to "name-role-value", "4.1.3" to "status-messages"
        )
        return slugs[criterion] ?: criterion.replace(".", "-")
    }

    private val CSS = """
        :root { --bg: #f8f9fa; --card-bg: #fff; --text: #1a1a2e; --border: #dee2e6; --error: #dc3545; --error-bg: #f8d7da; --warning: #856404; --warning-bg: #fff3cd; --info: #0c5460; --info-bg: #d1ecf1; --pass: #155724; --pass-bg: #d4edda; }
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: var(--bg); color: var(--text); line-height: 1.6; padding: 2rem; max-width: 1200px; margin: 0 auto; }
        h1 { font-size: 1.75rem; margin-bottom: 0.5rem; }
        h2 { font-size: 1.25rem; margin: 2rem 0 1rem; border-bottom: 2px solid var(--border); padding-bottom: 0.5rem; }
        a { text-decoration: underline; color: #0b5ed7; }
        .timestamp { color: #595f64; font-size: 0.875rem; margin-bottom: 1.5rem; }
        .summary { display: flex; gap: 1rem; flex-wrap: wrap; margin-bottom: 2rem; }
        .stat { background: var(--card-bg); border: 1px solid var(--border); border-radius: 8px; padding: 1rem 1.5rem; min-width: 120px; text-align: center; }
        .stat .number { font-size: 2rem; font-weight: 700; }
        .stat .label { font-size: 0.875rem; color: #595f64; }
        .stat.error .number { color: var(--error); }
        .stat.warning .number { color: var(--warning); }
        .stat.info .number { color: var(--info); }
        .score-section { background: var(--card-bg); border: 1px solid var(--border); border-radius: 8px; padding: 1.5rem; margin-bottom: 2rem; }
        .score-header-row { display: flex; align-items: center; gap: 1.5rem; margin-bottom: 1rem; flex-wrap: wrap; }
        .score-big { font-size: 3rem; font-weight: 800; line-height: 1; }
        .score-grade { font-size: 2rem; font-weight: 700; padding: 0.25rem 0.75rem; border-radius: 8px; display: inline-block; }
        .grade-a { background: var(--pass-bg); color: var(--pass); }
        .grade-b { background: #d4edda; color: #155724; }
        .grade-c { background: var(--warning-bg); color: var(--warning); }
        .grade-d { background: #ffe0b2; color: #e65100; }
        .grade-f { background: var(--error-bg); color: var(--error); }
        .score-subtitle { color: #595f64; font-size: 0.875rem; }
        .score-stats { display: flex; gap: 1.5rem; flex-wrap: wrap; font-size: 0.875rem; color: #595f64; margin-bottom: 1rem; }
        .score-stats span { white-space: nowrap; }
        .failed-criteria { margin-top: 0.75rem; }
        .failed-criteria h3 { font-size: 0.9375rem; margin-bottom: 0.5rem; }
        .failed-criteria ul { list-style: none; padding: 0; }
        .failed-criteria li { padding: 0.25rem 0; font-size: 0.875rem; }
        .failed-criteria .criterion-id { font-weight: 600; }
        .failed-criteria .criterion-counts { color: #595f64; font-size: 0.75rem; }
        .criteria-table { width: 100%; border-collapse: collapse; background: var(--card-bg); border-radius: 8px; overflow: hidden; border: 1px solid var(--border); margin-bottom: 1.5rem; }
        .criteria-table th { background: #f1f3f5; font-weight: 600; padding: 0.625rem 1rem; text-align: left; border-bottom: 1px solid var(--border); font-size: 0.875rem; }
        .criteria-table td { padding: 0.5rem 1rem; border-bottom: 1px solid var(--border); font-size: 0.875rem; }
        .criteria-table td.status-cell { text-align: center; }
        .badge { display: inline-block; padding: 0.125rem 0.5rem; border-radius: 4px; font-size: 0.75rem; font-weight: 600; }
        .badge-pass { background: var(--pass-bg); color: var(--pass); }
        .badge-fail { background: var(--error-bg); color: var(--error); }
        .badge-error { background: var(--error-bg); color: var(--error); }
        .badge-warning { background: var(--warning-bg); color: var(--warning); }
        .badge-info { background: var(--info-bg); color: var(--info); }
        .badge-critical { background: #f8d7da; color: #721c24; }
        .badge-serious { background: #ffe0b2; color: #e65100; }
        .badge-moderate { background: #fff3cd; color: #856404; }
        .badge-minor { background: #e2e3e5; color: #383d41; }
        details { background: var(--card-bg); border: 1px solid var(--border); border-radius: 8px; margin-bottom: 0.75rem; }
        summary { padding: 0.75rem 1rem; cursor: pointer; font-weight: 500; font-size: 0.875rem; }
        summary:hover { background: #f1f3f5; }
        .diag-list { padding: 0.5rem 1rem 1rem; }
        .diag { padding: 0.75rem 0; border-bottom: 1px solid #e9ecef; font-size: 0.8125rem; }
        .diag-title { font-size: 0.9375rem; font-weight: 700; margin: 0 0 0.375rem 0; color: #212529; }
        .diag:last-child { border-bottom: none; }
        .diag-loc { color: #595f64; font-family: monospace; }
        .diag-rule { color: #595f64; font-style: italic; }
        .diag-wcag { font-size: 0.75rem; font-weight: 600; color: #495057; }
        .diag-wcag a { color: #0b5ed7; }
        .code-label { font-size: 0.6875rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.5px; margin-top: 0.5rem; padding: 0.15rem 0.5rem; border-radius: 3px 3px 0 0; display: block; width: fit-content; }
        .bad-label { background: var(--error); color: white; }
        .good-label { background: #28a745; color: white; }
        .code-block { background: #1e1e2e; color: #cdd6f4; border-radius: 0 6px 6px 6px; padding: 0.625rem 0.75rem; margin: 0 0 0.375rem 0; font-family: 'SF Mono', Menlo, Consolas, monospace; font-size: 0.75rem; overflow-x: auto; line-height: 1.5; white-space: pre; }
        .code-block .line-bad { color: #f38ba8; }
        .suggestion { background: var(--pass-bg); color: var(--pass); border-radius: 4px; padding: 0.375rem 0.625rem; margin: 0.375rem 0; font-size: 0.75rem; display: inline-block; }
        .suggestion::before { content: "Fix: "; font-weight: 600; }
        .fix-block { background: #1e3a1e; color: #a6e3a1; border-radius: 0 6px 6px 6px; padding: 0.625rem 0.75rem; margin: 0 0 0.375rem 0; font-family: 'SF Mono', Menlo, Consolas, monospace; font-size: 0.75rem; overflow-x: auto; line-height: 1.5; white-space: pre; }
        .fix-block .line-good { color: #40e040; font-weight: 600; }
        .trend-section { background: var(--card-bg); border: 1px solid var(--border); border-radius: 8px; padding: 1.5rem; margin-bottom: 2rem; }
        .trend-section h2 { margin-top: 0; border-bottom: none; padding-bottom: 0; }
        .trend-chart { margin: 1rem 0; }
        .trend-chart svg { width: 100%; max-width: 700px; }
        .trend-chart .chart-line { fill: none; stroke: #0d6efd; stroke-width: 2.5; stroke-linecap: round; stroke-linejoin: round; }
        .trend-chart .chart-area { fill: rgba(13, 110, 253, 0.1); }
        .trend-chart .chart-dot { fill: #0d6efd; }
        .trend-chart .chart-dot-current { fill: #198754; stroke: #fff; stroke-width: 2; }
        .trend-chart .chart-grid { stroke: #e9ecef; stroke-width: 1; }
        .trend-chart .chart-label { fill: #595f64; font-size: 11px; font-family: -apple-system, sans-serif; }
        .trend-delta { font-size: 1.25rem; font-weight: 700; margin-bottom: 0.5rem; }
        .trend-delta.positive { color: var(--pass); }
        .trend-delta.negative { color: var(--error); }
        .trend-delta.neutral { color: #595f64; }
        .trend-table { width: 100%; max-width: 700px; }
        .trend-table th { background: #f1f3f5; }
        footer { margin-top: 3rem; text-align: center; color: #6c757d; font-size: 0.85rem; }
    """.trimIndent()
}
