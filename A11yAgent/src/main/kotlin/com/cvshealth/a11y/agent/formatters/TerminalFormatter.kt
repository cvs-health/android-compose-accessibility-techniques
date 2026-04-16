package com.cvshealth.a11y.agent.formatters

import com.cvshealth.a11y.agent.core.*

object TerminalFormatter {

    private const val RESET = "\u001B[0m"
    private const val BOLD = "\u001B[1m"
    private const val DIM = "\u001B[2m"
    private const val RED = "\u001B[31m"
    private const val GREEN = "\u001B[32m"
    private const val YELLOW = "\u001B[33m"
    private const val CYAN = "\u001B[36m"

    fun format(diagnostics: List<A11yDiagnostic>, score: A11yScore, compact: Boolean = false): String {
        val sb = StringBuilder()

        if (diagnostics.isEmpty()) {
            sb.appendLine("${GREEN}${BOLD}No accessibility issues found!$RESET")
        } else {
            // Group by file
            val byFile = diagnostics.groupBy { it.filePath }
            for ((filePath, fileDiags) in byFile.entries.sortedBy { it.key }) {
                if (!compact) {
                    val shortPath = filePath.substringAfterLast("/src/")
                    sb.appendLine("\n${BOLD}$shortPath$RESET")
                }

                for (diag in fileDiags.sortedBy { it.line }) {
                    val severityColor = when (diag.severity) {
                        A11ySeverity.ERROR -> RED
                        A11ySeverity.WARNING -> YELLOW
                        A11ySeverity.INFO -> CYAN
                    }
                    val marker = when (diag.severity) {
                        A11ySeverity.ERROR -> "\u2717"
                        A11ySeverity.WARNING -> "\u26A0"
                        A11ySeverity.INFO -> "\u2139"
                    }
                    val wcag = if (diag.wcagCriteria.isNotEmpty()) " [WCAG ${diag.wcagCriteria.joinToString(", ")}]" else ""
                    sb.appendLine("  $severityColor$marker ${diag.line}:${diag.column} ${diag.severity.label} [${diag.impact.label}]: ${diag.message}$wcag ($DIM${diag.ruleID}$RESET$severityColor)$RESET")

                    diag.suggestion?.let { suggestion ->
                        sb.appendLine("    ${DIM}Suggestion: $suggestion$RESET")
                    }
                }
            }
        }

        sb.appendLine(formatScoreSummary(score))
        return sb.toString()
    }

    private fun formatScoreSummary(score: A11yScore): String {
        val sb = StringBuilder()
        val gradeColor = when {
            score.score >= 80 -> GREEN
            score.score >= 60 -> YELLOW
            else -> RED
        }

        sb.appendLine("\n${BOLD}Accessibility Score:$RESET")

        // Progress bar
        val filled = (score.score / 5).toInt()
        val empty = 20 - filled
        val bar = "$gradeColor${"█".repeat(filled)}${DIM}${"░".repeat(empty)}$RESET"
        sb.appendLine("  $bar ${gradeColor}${BOLD}${"%.1f".format(score.score)}/100 (${score.grade})$RESET")

        sb.appendLine()
        sb.appendLine("  ${BOLD}Summary:$RESET")
        sb.appendLine("  ${if (score.totalErrors > 0) RED else DIM}Errors:   ${score.totalErrors}$RESET")
        sb.appendLine("  ${if (score.totalWarnings > 0) YELLOW else DIM}Warnings: ${score.totalWarnings}$RESET")
        sb.appendLine("  ${DIM}Info:     ${score.totalInfo}$RESET")
        sb.appendLine("  Files analyzed: ${score.filesAnalyzed}")
        sb.appendLine("  WCAG criteria: ${score.criteriaPassed} passed, ${score.criteriaFailed} failed, ${score.criteriaNotChecked} not checked")

        // Failed criteria
        val failedCriteria = score.criteriaScores.filter { it.status == CriterionStatus.FAIL }
        if (failedCriteria.isNotEmpty()) {
            sb.appendLine("\n  ${RED}${BOLD}Failed WCAG Criteria:$RESET")
            for (c in failedCriteria) {
                sb.appendLine("    ${RED}\u2717 ${c.criterion} ${c.name} (${c.errorCount} errors)$RESET")
            }
        }

        // Review criteria
        val reviewCriteria = score.criteriaScores.filter { it.status == CriterionStatus.REVIEW }
        if (reviewCriteria.isNotEmpty()) {
            sb.appendLine("\n  ${YELLOW}${BOLD}Needs Review:$RESET")
            for (c in reviewCriteria) {
                sb.appendLine("    ${YELLOW}\u26A0 ${c.criterion} ${c.name} (${c.warningCount} warnings)$RESET")
            }
        }

        return sb.toString()
    }
}
