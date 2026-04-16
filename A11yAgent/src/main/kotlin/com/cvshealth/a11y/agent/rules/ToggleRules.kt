package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import com.cvshealth.a11y.agent.scanner.ParsedKotlinFile

class ToggleMissingLabelRule : A11yRule {
    override val id = "toggle-missing-label"
    override val name = "Toggle Control Missing Label Association"
    override val severity = A11ySeverity.ERROR
    override val impact = A11yImpact.CRITICAL
    override val wcagCriteria = listOf("4.1.2")
    override val description = "Checkbox/Switch must be inside a Row with Modifier.toggleable() for proper label association"

    private val toggleControls = setOf("Checkbox", "TriStateCheckbox", "Switch")

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for (call in file.allCalls.filter { it.name in toggleControls }) {
            // Check if onCheckedChange/onValueChange is null (properly delegated to parent)
            val onCheckedChange = call.getArgument("onCheckedChange")
            val onValueChange = call.getArgument("onValueChange")

            if (onCheckedChange?.trim() == "null" || onValueChange?.trim() == "null") {
                // This is the correct pattern — click handling is on parent Row.toggleable()
                continue
            }

            // Check if inside a toggleable or selectable Row
            if (call.enclosingScopeText.contains(".toggleable(") ||
                call.enclosingScopeText.contains(".selectable(")) continue

            // Check if inside a component that handles label association
            if (call.enclosingCallName in setOf("CheckboxRow", "SwitchRow", "ListItem")) continue

            // Check for semantics with contentDescription nearby
            if (call.hasSemanticsProperty("contentDescription") ||
                call.enclosingScopeText.contains("clearAndSetSemantics")) continue

            val fix = computeToggleFix(call, context)

            diagnostics.add(
                makeDiagnostic(
                    message = "${call.name} handles its own click but may not be properly labeled. Wrap in Row(Modifier.toggleable()) with the label Text.",
                    line = call.line,
                    column = call.column,
                    context = context,
                    fix = fix,
                    suggestion = "Use Row(Modifier.toggleable(value, role = Role.${if (call.name == "Switch") "Switch" else "Checkbox"}, onValueChange)) with ${call.name}(onCheckedChange = null)"
                )
            )
        }
        return diagnostics
    }

    private fun computeToggleFix(call: com.cvshealth.a11y.agent.scanner.DetectedCall, context: RuleContext): A11yFix? {
        // Replace onCheckedChange = { ... } with onCheckedChange = null
        val lineOffset = lineColumnToOffset(context.sourceText, call.line)
        val searchEnd = minOf(lineOffset + call.rawArgumentText.length + 200, context.sourceText.length)
        val searchArea = context.sourceText.substring(lineOffset, searchEnd)

        val argName = if (call.name == "Switch") "onCheckedChange" else "onCheckedChange"
        val argIdx = searchArea.indexOf("$argName =")
        if (argIdx < 0) return null

        val valueStart = searchArea.indexOf("=", argIdx + argName.length)
        if (valueStart < 0) return null

        // Find the value — it's either a lambda { ... } or a reference
        val afterEquals = searchArea.substring(valueStart + 1).trimStart()
        val absValueStart = lineOffset + valueStart + 1 + (searchArea.substring(valueStart + 1).length - afterEquals.length)

        if (afterEquals.startsWith("{")) {
            // Find matching closing brace
            var depth = 0
            var pos = absValueStart
            while (pos < context.sourceText.length) {
                when (context.sourceText[pos]) {
                    '{' -> depth++
                    '}' -> {
                        depth--
                        if (depth == 0) {
                            return A11yFix(
                                description = "Set onCheckedChange = null (move click handling to parent Row.toggleable())",
                                replacementText = "null",
                                startOffset = absValueStart,
                                endOffset = pos + 1
                            )
                        }
                    }
                }
                pos++
            }
        }
        return null
    }
}
