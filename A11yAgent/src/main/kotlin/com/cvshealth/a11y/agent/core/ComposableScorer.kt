package com.cvshealth.a11y.agent.core

data class ComposableScore(
    val name: String,
    val filePath: String,
    val startLine: Int,
    val endLine: Int,
    val score: Double,
    val grade: String,
    val errorCount: Int,
    val warningCount: Int
)

class ComposableScorer {

    fun scoreComposables(
        composables: List<RuleRegistry.LocatedComposable>,
        diagnostics: List<A11yDiagnostic>,
        rules: List<A11yRule>
    ): List<ComposableScore> {
        val calculator = ScoreCalculator()
        return composables.map { (filePath, comp) ->
            val compDiags = diagnostics.filter { d ->
                d.filePath == filePath && d.line in comp.startLine..comp.endLine
            }
            val score = calculator.calculate(compDiags, rules, listOf(filePath))
            ComposableScore(
                name = comp.name,
                filePath = filePath,
                startLine = comp.startLine,
                endLine = comp.endLine,
                score = score.score,
                grade = score.grade,
                errorCount = compDiags.count { it.severity == A11ySeverity.ERROR },
                warningCount = compDiags.count { it.severity == A11ySeverity.WARNING }
            )
        }.sortedBy { it.score }
    }

    fun formatScores(scores: List<ComposableScore>): String {
        if (scores.isEmpty()) return "No @Composable functions found."

        return buildString {
            appendLine()
            appendLine("Per-Composable Scores:")
            for (cs in scores) {
                val barLength = 20
                val filled = (cs.score / 100.0 * barLength).toInt().coerceIn(0, barLength)
                val bar = "\u2588".repeat(filled) + "\u2591".repeat(barLength - filled)
                val shortPath = cs.filePath.substringAfterLast("/")
                val issues = mutableListOf<String>()
                if (cs.errorCount > 0) issues.add("${cs.errorCount} error${if (cs.errorCount != 1) "s" else ""}")
                if (cs.warningCount > 0) issues.add("${cs.warningCount} warning${if (cs.warningCount != 1) "s" else ""}")
                val issueStr = if (issues.isNotEmpty()) "  (${issues.joinToString(", ")})" else ""
                appendLine("  [$bar]  ${"%.1f".format(cs.score)} (${cs.grade})  ${cs.name}  $shortPath:${cs.startLine}-${cs.endLine}$issueStr")
            }
        }
    }
}
