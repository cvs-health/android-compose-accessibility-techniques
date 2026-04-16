package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import com.cvshealth.a11y.agent.scanner.ParsedKotlinFile

class AccessibilityGroupingRule : A11yRule {
    override val id = "accessibility-grouping"
    override val name = "Accessibility Grouping Missing"
    override val severity = A11ySeverity.INFO
    override val impact = A11yImpact.MODERATE
    override val wcagCriteria = listOf("1.3.1")
    override val description = "Row/Column containing Icon + Text should use semantics(mergeDescendants = true) for grouping"

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for (call in file.allCalls.filter { it.name in setOf("Row", "Column") }) {
            val body = call.rawArgumentText

            // Check if it contains both an Icon and Text
            val hasIcon = body.contains("Icon(") || body.contains("Image(")
            val hasText = body.contains("Text(")
            if (!hasIcon || !hasText) continue

            // Skip if already has mergeDescendants or clearAndSetSemantics
            if (call.modifierChain.contains("mergeDescendants") ||
                call.modifierChain.contains("clearAndSetSemantics") ||
                body.contains("mergeDescendants")) continue

            // Skip if the Row/Column is inside a Button or clickable
            if (call.enclosingCallName in setOf("Button", "TextButton", "IconButton",
                    "OutlinedButton", "Card", "ElevatedCard")) continue
            if (call.enclosingScopeText.contains(".clickable(")) continue
            if (call.enclosingScopeText.contains(".toggleable(")) continue

            diagnostics.add(
                makeDiagnostic(
                    message = "${call.name} contains Icon and Text but lacks mergeDescendants. TalkBack will announce them as separate elements.",
                    line = call.line,
                    column = call.column,
                    context = context,
                    suggestion = "Add Modifier.semantics(mergeDescendants = true) { } to the ${call.name}"
                )
            )
        }
        return diagnostics
    }
}

class HiddenWithInteractiveChildrenRule : A11yRule {
    override val id = "hidden-with-interactive-children"
    override val name = "Hidden Semantics with Interactive Children"
    override val severity = A11ySeverity.ERROR
    override val impact = A11yImpact.CRITICAL
    override val wcagCriteria = listOf("4.1.2")
    override val description = "clearAndSetSemantics hides all child semantics including interactive controls"

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()
        val interactiveChildren = setOf("Button", "TextButton", "IconButton", "OutlinedButton",
            "Checkbox", "Switch", "RadioButton", "Slider", "TextField", "OutlinedTextField")

        for (call in file.allCalls) {
            if (!call.modifierChain.contains("clearAndSetSemantics")) continue

            val body = call.rawArgumentText
            val hasInteractive = interactiveChildren.any { body.contains("$it(") }
            if (!hasInteractive) continue

            diagnostics.add(
                makeDiagnostic(
                    message = "clearAndSetSemantics on ${call.name} hides interactive child controls from TalkBack.",
                    line = call.line,
                    column = call.column,
                    context = context,
                    suggestion = "Use semantics(mergeDescendants = true) instead, or ensure the replacement contentDescription covers all child functionality"
                )
            )
        }
        return diagnostics
    }
}

class BoxChildOrderRule : A11yRule {
    override val id = "box-child-order"
    override val name = "Box Child Reading Order"
    override val severity = A11ySeverity.INFO
    override val impact = A11yImpact.MODERATE
    override val wcagCriteria = listOf("1.3.2")
    override val description = "Box children are read by TalkBack in declaration order — ensure visual and reading order match"

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for (call in file.allCalls.filter { it.name == "Box" }) {
            val body = call.rawArgumentText
            // Detect when both align and zIndex are used, suggesting overlapping content
            if (body.contains(".align(") && body.contains(".zIndex(")) {
                // Check for traversalIndex to override reading order
                if (body.contains("traversalIndex") || body.contains("isTraversalGroup")) continue

                diagnostics.add(
                    makeDiagnostic(
                        message = "Box uses both align and zIndex with overlapping children. TalkBack reading order may not match visual order.",
                        line = call.line,
                        column = call.column,
                        context = context,
                        suggestion = "Use semantics { isTraversalGroup = true } and traversalIndex to control TalkBack reading order"
                    )
                )
            }
        }
        return diagnostics
    }
}

class RadioGroupMissingRule : A11yRule {
    override val id = "radio-group-missing"
    override val name = "RadioButton Not in SelectableGroup"
    override val severity = A11ySeverity.ERROR
    override val impact = A11yImpact.SERIOUS
    override val wcagCriteria = listOf("1.3.1", "4.1.2")
    override val description = "RadioButton groups should use Modifier.selectableGroup() on the parent Column/Row"

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for (call in file.allCalls.filter { it.name == "RadioButton" }) {
            // Check if there's a selectableGroup in the enclosing scope
            if (call.enclosingScopeText.contains("selectableGroup")) continue

            // Check if inside a RadioButtonGroup component
            if (call.enclosingCallName == "RadioButtonGroup") continue

            // Check if the RadioButton is properly wrapped with selectable
            if (call.enclosingScopeText.contains(".selectable(")) continue

            diagnostics.add(
                makeDiagnostic(
                    message = "RadioButton is not inside a selectableGroup(). TalkBack won't announce the group relationship.",
                    line = call.line,
                    column = call.column,
                    context = context,
                    suggestion = "Wrap RadioButtons in Column(Modifier.selectableGroup()) and use Row(Modifier.selectable(role = Role.RadioButton))"
                )
            )
        }
        return diagnostics
    }
}
