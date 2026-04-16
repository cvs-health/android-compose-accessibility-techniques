package com.cvshealth.a11y.agent.core

object InlineSuppression {

    data class Suppression(
        val line: Int,
        val ruleIDs: Set<String>?
    )

    fun filter(diagnostics: List<A11yDiagnostic>, sourceText: String): List<A11yDiagnostic> {
        val suppressions = parseSuppressionsFromSource(sourceText)
        if (suppressions.isEmpty()) return diagnostics

        return diagnostics.filter { diag ->
            suppressions.none { sup ->
                sup.line == diag.line && (sup.ruleIDs == null || sup.ruleIDs.contains(diag.ruleID))
            }
        }
    }

    internal fun parseSuppressionsFromSource(source: String): List<Suppression> {
        val suppressions = mutableListOf<Suppression>()
        val lines = source.lines()

        for ((index, line) in lines.withIndex()) {
            val lineNumber = index + 1
            val trimmed = line.trim()

            // Check for disable-next-line (must come before disable check)
            val nextLineIdx = trimmed.indexOf("// a11y-check:disable-next-line")
            if (nextLineIdx >= 0) {
                val afterDirective = trimmed.substring(nextLineIdx + "// a11y-check:disable-next-line".length).trim()
                val ruleIDs = parseRuleIDs(afterDirective)
                suppressions.add(Suppression(line = lineNumber + 1, ruleIDs = ruleIDs))
                continue
            }

            // Check for same-line disable
            val disableIdx = trimmed.indexOf("// a11y-check:disable")
            if (disableIdx >= 0) {
                val afterDirective = trimmed.substring(disableIdx + "// a11y-check:disable".length).trim()
                if (afterDirective.startsWith("-next-line")) continue
                val ruleIDs = parseRuleIDs(afterDirective)
                suppressions.add(Suppression(line = lineNumber, ruleIDs = ruleIDs))
            }
        }

        return suppressions
    }

    private fun parseRuleIDs(text: String): Set<String>? {
        val ids = text.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
        return if (ids.isEmpty()) null else ids.toSet()
    }
}
