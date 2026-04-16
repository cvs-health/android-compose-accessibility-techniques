package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import com.cvshealth.a11y.agent.scanner.ParsedKotlinFile

class ClickableMissingRoleRule : A11yRule {
    override val id = "clickable-missing-role"
    override val name = "Clickable Missing Role or Click Label"
    override val severity = A11ySeverity.WARNING
    override val impact = A11yImpact.SERIOUS
    override val wcagCriteria = listOf("4.1.2")
    override val description = "Modifier.clickable() should include onClickLabel for accessible action description"

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()
        val clickablePattern = Regex("""\.\s*clickable\s*[({]""")

        for ((index, line) in context.sourceLines.withIndex()) {
            if (!clickablePattern.containsMatchIn(line)) continue

            // Get surrounding context to check for onClickLabel
            val start = maxOf(0, index)
            val end = minOf(context.sourceLines.size - 1, index + 5)
            val contextText = context.sourceLines.subList(start, end + 1).joinToString("\n")

            if (contextText.contains("onClickLabel")) continue
            if (contextText.contains("role =")) continue

            // Skip if inside a known interactive composable
            val lineText = line.trim()
            if (lineText.startsWith("Button") || lineText.startsWith("IconButton") ||
                lineText.startsWith("TextButton")) continue

            val fix = computeClickableFix(index + 1, line, context)

            diagnostics.add(
                makeDiagnostic(
                    message = "Modifier.clickable() without onClickLabel. TalkBack users won't know what the action does.",
                    line = index + 1,
                    column = (clickablePattern.find(line)?.range?.first ?: 0) + 1,
                    context = context,
                    fix = fix,
                    suggestion = "Add onClickLabel = \"action description\" to Modifier.clickable()"
                )
            )
        }
        return diagnostics
    }

    private fun computeClickableFix(lineNumber: Int, line: String, context: RuleContext): A11yFix? {
        val lineOffset = lineColumnToOffset(context.sourceText, lineNumber)
        val searchArea = context.sourceText.substring(
            lineOffset,
            minOf(lineOffset + line.length + 100, context.sourceText.length)
        )
        val match = Regex("""\.\s*clickable\s*\(""").find(searchArea) ?: return null
        val insertOffset = lineOffset + match.range.last + 1
        return A11yFix(
            description = "Add onClickLabel to clickable",
            replacementText = "onClickLabel = \"TODO: describe action\", ",
            startOffset = insertOffset,
            endOffset = insertOffset
        )
    }
}
