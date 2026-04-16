package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import com.cvshealth.a11y.agent.scanner.ParsedKotlinFile

class TextFieldMissingLabelRule : A11yRule {
    override val id = "textfield-missing-label"
    override val name = "TextField Missing Label"
    override val severity = A11ySeverity.ERROR
    override val impact = A11yImpact.CRITICAL
    override val wcagCriteria = listOf("4.1.2")
    override val description = "TextField and OutlinedTextField must have a label parameter for accessibility"

    private val textFieldCalls = setOf("TextField", "OutlinedTextField")

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for (call in file.allCalls.filter { it.name in textFieldCalls }) {
            val label = call.getArgument("label")

            if (label == null || label.isBlank() || label.trim() == "{}") {
                // Check for alternative labeling
                if (call.hasSemanticsProperty("contentDescription")) continue
                if (call.enclosingScopeText.contains("LabeledContent")) continue
                if (call.modifierChain.contains("clearAndSetSemantics")) continue

                val fix = computeTextFieldFix(call, context)

                diagnostics.add(
                    makeDiagnostic(
                        message = "${call.name} is missing a label parameter. TalkBack users won't know what input is expected.",
                        line = call.line,
                        column = call.column,
                        context = context,
                        fix = fix,
                        suggestion = "Add label = { Text(\"Field label\") } to the ${call.name}"
                    )
                )
            }
        }
        return diagnostics
    }

    private fun computeTextFieldFix(call: com.cvshealth.a11y.agent.scanner.DetectedCall, context: RuleContext): A11yFix? {
        val lineOffset = lineColumnToOffset(context.sourceText, call.line)
        val callStart = context.sourceText.indexOf(call.name + "(", lineOffset)
        if (callStart < 0) return null

        // Find the opening paren
        val openParen = callStart + call.name.length
        if (openParen >= context.sourceText.length || context.sourceText[openParen] != '(') return null

        // Insert label as first named argument after the opening paren
        val insertOffset = openParen + 1
        return A11yFix(
            description = "Add label parameter to ${call.name}",
            replacementText = "\n        label = { Text(\"TODO: add label\") },",
            startOffset = insertOffset,
            endOffset = insertOffset
        )
    }
}

class SliderMissingLabelRule : A11yRule {
    override val id = "slider-missing-label"
    override val name = "Slider Missing Label"
    override val severity = A11ySeverity.ERROR
    override val impact = A11yImpact.SERIOUS
    override val wcagCriteria = listOf("4.1.2")
    override val description = "Slider must have an associated label via semantics or wrapping composable"

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for (call in file.allCalls.filter { it.name in setOf("Slider", "RangeSlider") }) {
            if (call.hasSemanticsProperty("contentDescription")) continue
            if (call.modifierChain.contains("clearAndSetSemantics")) continue
            if (call.enclosingScopeText.contains("LabeledContent")) continue

            // Check if there's nearby text that could be a label via mergeDescendants
            if (call.enclosingScopeText.contains("mergeDescendants")) continue

            diagnostics.add(
                makeDiagnostic(
                    message = "${call.name} has no accessible label. TalkBack users won't know what the slider controls.",
                    line = call.line,
                    column = call.column,
                    context = context,
                    suggestion = "Wrap with a visible Text label (e.g., Column with Text + Slider, or LabeledContent). Fallback: add Modifier.semantics { contentDescription = \"...\" }"
                )
            )
        }
        return diagnostics
    }
}

class DropdownMissingLabelRule : A11yRule {
    override val id = "dropdown-missing-label"
    override val name = "Dropdown Menu Missing Label"
    override val severity = A11ySeverity.ERROR
    override val impact = A11yImpact.SERIOUS
    override val wcagCriteria = listOf("4.1.2")
    override val description = "ExposedDropdownMenuBox must have an associated label"

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for (call in file.allCalls.filter { it.name == "ExposedDropdownMenuBox" }) {
            // Check if the body contains a TextField with a label
            val bodyText = call.rawArgumentText
            if (bodyText.contains("label =") && bodyText.contains("TextField")) continue
            if (call.hasSemanticsProperty("contentDescription")) continue

            diagnostics.add(
                makeDiagnostic(
                    message = "ExposedDropdownMenuBox has no accessible label. Include a labeled TextField inside it.",
                    line = call.line,
                    column = call.column,
                    context = context,
                    suggestion = "Add a TextField with label = { Text(\"Selection\") } inside the dropdown"
                )
            )
        }
        return diagnostics
    }
}
