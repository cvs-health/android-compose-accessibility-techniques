package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import com.cvshealth.a11y.agent.scanner.ParsedKotlinFile

class LabelContainsRoleButtonRule : A11yRule {
    override val id = "label-contains-role-button"
    override val name = "Button Label Contains Role Word"
    override val severity = A11ySeverity.WARNING
    override val impact = A11yImpact.MINOR
    override val wcagCriteria = listOf("4.1.2")
    override val description = "Button labels should not contain the word 'button' — TalkBack announces it"

    private val buttonCalls = setOf("Button", "TextButton", "OutlinedButton",
        "FilledTonalButton", "ElevatedButton", "IconButton")

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for (call in file.allCalls.filter { it.name in buttonCalls }) {
            // Check text content in the button body
            val bodyText = call.rawArgumentText.lowercase()
            if (bodyText.contains("\"") && Regex("""\bbutton\b""").containsMatchIn(
                    bodyText.replace(Regex("""onClick\s*=\s*\{[^}]*\}"""), ""))) {
                diagnostics.add(
                    makeDiagnostic(
                        message = "Button label contains the word \"button\". TalkBack already announces the Button role.",
                        line = call.line,
                        column = call.column,
                        context = context,
                        suggestion = "Remove \"button\" from the label text"
                    )
                )
            }
        }
        return diagnostics
    }
}

class IconButtonMissingLabelRule : A11yRule {
    override val id = "icon-button-missing-label"
    override val name = "IconButton Missing Accessible Label"
    override val severity = A11ySeverity.ERROR
    override val impact = A11yImpact.CRITICAL
    override val wcagCriteria = listOf("4.1.2")
    override val description = "IconButton must have an accessible label via its Icon's contentDescription or semantics"

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for (call in file.allCalls.filter { it.name == "IconButton" }) {
            // Check if the IconButton has semantics with contentDescription
            if (call.hasSemanticsProperty("contentDescription")) continue

            // Check if there's an Icon inside with a non-null contentDescription
            val bodyText = call.rawArgumentText
            val iconPattern = Regex("""Icon\s*\([^)]*contentDescription\s*=\s*(?!null)""")
            if (iconPattern.containsMatchIn(bodyText)) continue

            // Check for clearAndSetSemantics on the button itself
            if (call.modifierChain.contains("clearAndSetSemantics")) continue

            val fix = computeIconButtonFix(call, context)

            diagnostics.add(
                makeDiagnostic(
                    message = "IconButton has no accessible label. Set contentDescription on the Icon or add semantics { contentDescription = \"...\" } to the IconButton.",
                    line = call.line,
                    column = call.column,
                    context = context,
                    fix = fix,
                    suggestion = "Add contentDescription = stringResource(R.string.button_description) to the Icon inside the IconButton"
                )
            )
        }
        return diagnostics
    }

    private fun computeIconButtonFix(call: com.cvshealth.a11y.agent.scanner.DetectedCall, context: RuleContext): A11yFix? {
        // Find the Icon( call inside the IconButton body and insert contentDescription
        val lineOffset = lineColumnToOffset(context.sourceText, call.line)
        val bodyText = call.rawArgumentText
        val iconIdx = bodyText.indexOf("Icon(")
        if (iconIdx < 0) return null

        // Find the Icon call in the source text starting from the IconButton line
        val searchArea = context.sourceText.substring(
            lineOffset,
            minOf(lineOffset + bodyText.length + 200, context.sourceText.length)
        )
        val iconInSource = searchArea.indexOf("Icon(")
        if (iconInSource < 0) return null

        // Find the closing paren of Icon() by counting depth
        val absIconStart = lineOffset + iconInSource + 5 // past "Icon("
        var depth = 1
        var pos = absIconStart
        while (pos < context.sourceText.length && depth > 0) {
            when (context.sourceText[pos]) {
                '(' -> depth++
                ')' -> depth--
            }
            if (depth > 0) pos++
        }
        // pos is now at the closing paren
        val insertOffset = pos
        return A11yFix(
            description = "Add contentDescription to Icon in IconButton",
            replacementText = ",\n                contentDescription = stringResource(R.string.button_description)",
            startOffset = insertOffset,
            endOffset = insertOffset
        )
    }
}

class VisuallyDisabledNotSemanticallyRule : A11yRule {
    override val id = "visually-disabled-not-semantically"
    override val name = "Visually Disabled Not Semantically Disabled"
    override val severity = A11ySeverity.WARNING
    override val impact = A11yImpact.SERIOUS
    override val wcagCriteria = listOf("4.1.2")
    override val description = "Elements using alpha to appear disabled must also be semantically disabled"

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()
        val alphaPattern = Regex("""\.\s*alpha\s*\(\s*(0\.\d+f?)\s*\)""")

        for ((index, line) in context.sourceLines.withIndex()) {
            val match = alphaPattern.find(line) ?: continue
            val alphaValue = match.groupValues[1].removeSuffix("f").toDoubleOrNull() ?: continue
            if (alphaValue >= 0.5) continue

            // Check surrounding context for enabled = false or disabled semantics
            val start = maxOf(0, index - 5)
            val end = minOf(context.sourceLines.size - 1, index + 5)
            val surroundingText = context.sourceLines.subList(start, end + 1).joinToString("\n")

            if (surroundingText.contains("enabled = false") ||
                surroundingText.contains("enabled(false)") ||
                surroundingText.contains("disabled = true") ||
                surroundingText.contains(".disabled(")) continue

            diagnostics.add(
                makeDiagnostic(
                    message = "Element uses alpha($alphaValue) to appear disabled but may not be semantically disabled for TalkBack.",
                    line = index + 1,
                    column = match.range.first + 1,
                    context = context,
                    suggestion = "Add Modifier.semantics { disabled() } or use enabled = false on the component"
                )
            )
        }
        return diagnostics
    }
}
