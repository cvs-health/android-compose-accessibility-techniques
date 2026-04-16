package com.cvshealth.a11y.agent.core

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.Instant

class TrendTracker(directory: String, fileName: String = ".a11y-scores.json") {

    @Serializable
    data class Entry(
        val date: String,
        val score: Double,
        val grade: String,
        val errors: Int,
        val warnings: Int,
        val criteriaPassed: Int,
        val criteriaFailed: Int,
        val filesAnalyzed: Int,
        val gitCommit: String? = null
    )

    @Serializable
    data class History(
        val entries: MutableList<Entry> = mutableListOf()
    )

    private val filePath = File(directory, fileName).absolutePath
    private val json = Json { prettyPrint = true; encodeDefaults = true }

    fun load(): History {
        val file = File(filePath)
        if (!file.exists()) return History()
        return try {
            json.decodeFromString<History>(file.readText())
        } catch (_: Exception) {
            History()
        }
    }

    fun record(score: A11yScore) {
        val history = load()
        val entry = Entry(
            date = Instant.now().toString(),
            score = score.score,
            grade = score.grade,
            errors = score.totalErrors,
            warnings = score.totalWarnings,
            criteriaPassed = score.criteriaPassed,
            criteriaFailed = score.criteriaFailed,
            filesAnalyzed = score.filesAnalyzed,
            gitCommit = currentGitCommit()
        )
        history.entries.add(entry)
        File(filePath).writeText(json.encodeToString(history))
    }

    fun formatTrend(currentScore: A11yScore, lastN: Int = 10): String {
        val history = load()
        val reset = "\u001B[0m"
        val bold = "\u001B[1m"
        val dim = "\u001B[2m"
        val green = "\u001B[32m"
        val red = "\u001B[31m"
        val yellow = "\u001B[33m"

        val sb = StringBuilder()
        sb.appendLine("\n${bold}Score Trend:$reset")

        if (history.entries.isEmpty()) {
            sb.appendLine("  ${dim}No previous scores recorded. Run with --trend again to start tracking.$reset")
            return sb.toString()
        }

        val recent = history.entries.takeLast(lastN)

        // Delta from last
        recent.lastOrNull()?.let { last ->
            val delta = currentScore.score - last.score
            val deltaStr = when {
                delta > 0 -> "$green+${"%.1f".format(delta)}$reset"
                delta < 0 -> "$red${"%.1f".format(delta)}$reset"
                else -> "${dim}\u00b10.0$reset"
            }
            sb.appendLine("  Change from last run: $deltaStr")
        }

        // Sparkline
        val allScores = recent.map { it.score } + currentScore.score
        sb.appendLine("  ${bold}History:$reset ${formatSparkline(allScores)}")

        // Table
        sb.appendLine("\n  ${dim}Date                          Score  Grade  Errors  \u0394$reset")
        var prevScore: Double? = null
        for (entry in recent) {
            val delta = formatDelta(entry.score, prevScore, green, red, dim, reset)
            val gradeColor = gradeColor(entry.grade, green, yellow, red)
            val dateShort = entry.date.take(25).padEnd(30)
            sb.append("  $dateShort")
            sb.append("%5.1f  ".format(entry.score))
            sb.append("$gradeColor${entry.grade.padEnd(5)}$reset  ")
            sb.append("${if (entry.errors > 0) red else dim}${entry.errors.toString().padEnd(6)}$reset  ")
            sb.appendLine(delta)
            prevScore = entry.score
        }

        // Current
        val currentDelta = formatDelta(currentScore.score, prevScore, green, red, dim, reset)
        val gradeColor = gradeColor(currentScore.grade, green, yellow, red)
        sb.append("  ${bold}\u2192 now".padEnd(36))
        sb.append("%5.1f  ".format(currentScore.score))
        sb.append("$gradeColor${currentScore.grade.padEnd(5)}$reset  ")
        sb.append("${if (currentScore.totalErrors > 0) red else dim}${currentScore.totalErrors.toString().padEnd(6)}$reset  ")
        sb.appendLine("$currentDelta$reset")

        return sb.toString()
    }

    private fun formatDelta(score: Double, prevScore: Double?, green: String, red: String, dim: String, reset: String): String {
        if (prevScore == null) return "$dim  \u2014$reset"
        val d = score - prevScore
        return when {
            d > 0 -> "$green+${"%.1f".format(d)}$reset"
            d < 0 -> "$red${"%.1f".format(d)}$reset"
            else -> "$dim 0.0$reset"
        }
    }

    private fun gradeColor(grade: String, green: String, yellow: String, red: String): String {
        return when (grade.first()) {
            'A', 'B' -> green
            'C', 'D' -> yellow
            else -> red
        }
    }

    private fun formatSparkline(values: List<Double>): String {
        val blocks = listOf("\u2581", "\u2582", "\u2583", "\u2584", "\u2585", "\u2586", "\u2587", "\u2588")
        val min = values.minOrNull() ?: return ""
        val max = values.maxOrNull() ?: return ""
        val range = max - min
        return values.joinToString("") { value ->
            if (range == 0.0) blocks[4]
            else {
                val idx = ((value - min) / range * (blocks.size - 1)).toInt().coerceIn(0, blocks.size - 1)
                blocks[idx]
            }
        }
    }

    private fun currentGitCommit(): String? {
        return try {
            val process = ProcessBuilder("git", "rev-parse", "--short", "HEAD")
                .redirectErrorStream(true)
                .start()
            val output = process.inputStream.bufferedReader().readText().trim()
            process.waitFor()
            if (process.exitValue() == 0) output else null
        } catch (_: Exception) {
            null
        }
    }
}
