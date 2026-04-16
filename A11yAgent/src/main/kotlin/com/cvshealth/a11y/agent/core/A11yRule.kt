package com.cvshealth.a11y.agent.core

import com.cvshealth.a11y.agent.scanner.ParsedKotlinFile

data class RuleContext(
    val filePath: String,
    val sourceText: String,
    val sourceLines: List<String>,
    val disabledRules: Set<String> = emptySet(),
    val severityOverrides: Map<String, A11ySeverity> = emptyMap(),
    val configOptions: ConfigOptions = ConfigOptions()
)

data class ConfigOptions(
    val minTouchTarget: Int = 48,
    val contrastRatio: Double = 4.5
)

/**
 * Compute a character offset from 1-based line and column numbers.
 */
fun lineColumnToOffset(sourceText: String, line: Int, column: Int = 1): Int {
    var offset = 0
    for (i in 1 until line) {
        val nl = sourceText.indexOf('\n', offset)
        if (nl < 0) break
        offset = nl + 1
    }
    return offset + column - 1
}

interface A11yRule {
    val id: String
    val name: String
    val severity: A11ySeverity
    val impact: A11yImpact
    val wcagCriteria: List<String>
    val description: String

    fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic>

    fun makeDiagnostic(
        message: String,
        line: Int,
        column: Int = 1,
        context: RuleContext,
        severityOverride: A11ySeverity? = null,
        wcagCriteriaOverride: List<String>? = null,
        fix: A11yFix? = null,
        suggestion: String? = null
    ): A11yDiagnostic {
        val effectiveSeverity = severityOverride
            ?: context.severityOverrides[id]
            ?: severity
        return A11yDiagnostic(
            ruleID = id,
            severity = effectiveSeverity,
            impact = impact,
            message = message,
            filePath = context.filePath,
            line = line,
            column = column,
            wcagCriteria = wcagCriteriaOverride ?: wcagCriteria,
            fix = fix,
            suggestion = suggestion
        )
    }
}
