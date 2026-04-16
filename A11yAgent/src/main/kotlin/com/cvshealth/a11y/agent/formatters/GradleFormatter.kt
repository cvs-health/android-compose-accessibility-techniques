package com.cvshealth.a11y.agent.formatters

import com.cvshealth.a11y.agent.core.*

/**
 * Outputs diagnostics in the file:line:column: severity: message format
 * that Android Studio / IntelliJ IDEA parses from Gradle task output
 * to produce clickable inline annotations in the editor.
 *
 * Equivalent to the iOS a11y-check --format xcode output.
 */
object GradleFormatter {

    fun format(diagnostics: List<A11yDiagnostic>, score: A11yScore, compact: Boolean = false): String {
        val sb = StringBuilder()

        for (diag in diagnostics.sortedWith(compareBy({ it.filePath }, { it.line }))) {
            val severity = when (diag.severity) {
                A11ySeverity.ERROR -> "error"
                A11ySeverity.WARNING -> "warning"
                A11ySeverity.INFO -> "info"
            }
            val wcag = if (diag.wcagCriteria.isNotEmpty()) {
                " [WCAG ${diag.wcagCriteria.joinToString(", ")}]"
            } else ""

            val path = if (compact) diag.filePath.substringAfterLast("/") else diag.filePath
            sb.appendLine("$path:${diag.line}:${diag.column}: $severity: [${diag.ruleID}] [${diag.impact.label}] ${diag.message}$wcag")
        }

        // Score summary as a project-level diagnostic
        val scoreSeverity = when {
            score.criteriaFailed > 0 -> "warning"
            else -> "info"
        }
        val failedCriteria = score.criteriaScores
            .filter { it.status == CriterionStatus.FAIL }
            .joinToString(", ") { "${it.criterion} ${it.name}" }
        val failedStr = if (failedCriteria.isNotEmpty()) " — Failed: $failedCriteria" else ""
        sb.appendLine("w: [a11y-score] WCAG Score: ${"%.1f".format(score.score)}/100 (${score.grade}) — ${score.totalErrors} errors, ${score.totalWarnings} warnings$failedStr")

        return sb.toString()
    }
}
