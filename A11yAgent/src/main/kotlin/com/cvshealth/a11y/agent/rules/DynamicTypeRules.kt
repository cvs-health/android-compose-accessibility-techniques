package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import com.cvshealth.a11y.agent.scanner.ParsedKotlinFile

class FixedFontSizeRule : A11yRule {
    override val id = "fixed-font-size"
    override val name = "Fixed Font Size"
    override val severity = A11ySeverity.WARNING
    override val impact = A11yImpact.SERIOUS
    override val wcagCriteria = listOf("1.4.4")
    override val description = "Avoid hardcoded .sp font sizes — use MaterialTheme.typography styles for dynamic type support"

    private val spPattern = Regex("""(\d+)\.sp\b""")
    private val excludePatterns = listOf("fontSize =", "TextStyle(", "typography")

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for ((index, line) in context.sourceLines.withIndex()) {
            val match = spPattern.find(line) ?: continue

            // Skip if this is in a Typography/TextStyle definition (theme setup is fine)
            val trimmed = line.trim()
            if (trimmed.startsWith("//") || trimmed.startsWith("*")) continue
            if (trimmed.contains("TextStyle(")) continue

            // Check if it's a font size parameter on a Text/composable
            if (trimmed.contains("fontSize") || trimmed.contains("size =")) {
                diagnostics.add(
                    makeDiagnostic(
                        message = "Hardcoded font size ${match.value} — won't scale with user's text size settings.",
                        line = index + 1,
                        column = match.range.first + 1,
                        context = context,
                        suggestion = "Use MaterialTheme.typography.bodyLarge (or appropriate style) instead of fixed sp values"
                    )
                )
            }
        }
        return diagnostics
    }
}

class MaxLinesOneRule : A11yRule {
    override val id = "max-lines-one"
    override val name = "maxLines = 1 Text Truncation"
    override val severity = A11ySeverity.INFO
    override val impact = A11yImpact.MODERATE
    override val wcagCriteria = listOf("1.4.4")
    override val description = "Text with maxLines = 1 may truncate content when text size is increased"

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for (call in file.allCalls.filter { it.name == "Text" }) {
            val maxLines = call.getArgument("maxLines") ?: continue
            if (maxLines.trim() == "1") {
                diagnostics.add(
                    makeDiagnostic(
                        message = "Text with maxLines = 1 may truncate content when the user increases text size.",
                        line = call.line,
                        column = call.column,
                        context = context,
                        suggestion = "Consider allowing text to wrap or using overflow = TextOverflow.Ellipsis with a tooltip"
                    )
                )
            }
        }
        return diagnostics
    }
}
