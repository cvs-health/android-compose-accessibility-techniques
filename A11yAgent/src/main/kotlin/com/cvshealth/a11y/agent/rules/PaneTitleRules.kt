package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import com.cvshealth.a11y.agent.scanner.ParsedKotlinFile

class MissingPaneTitleRule : A11yRule {
    override val id = "missing-pane-title"
    override val name = "Missing Pane Title"
    override val severity = A11ySeverity.WARNING
    override val impact = A11yImpact.SERIOUS
    override val wcagCriteria = listOf("2.4.2")
    override val description = "Scaffold should have semantics { paneTitle = ... } for screen change announcements"

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for (call in file.allCalls.filter { it.name == "Scaffold" }) {
            // Check modifier chain for paneTitle semantics
            if (call.hasSemanticsProperty("paneTitle")) continue
            if (call.modifierChain.contains("paneTitle")) continue

            // Check if inside a known scaffold wrapper
            if (call.enclosingCallName in setOf("GenericScaffold")) continue

            diagnostics.add(
                makeDiagnostic(
                    message = "Scaffold is missing paneTitle semantics. TalkBack won't announce screen changes.",
                    line = call.line,
                    column = call.column,
                    context = context,
                    suggestion = "Add modifier = Modifier.semantics { paneTitle = screenTitle } to the Scaffold"
                )
            )
        }
        return diagnostics
    }
}

class TabMissingLabelRule : A11yRule {
    override val id = "tab-missing-label"
    override val name = "Tab Missing Label"
    override val severity = A11ySeverity.WARNING
    override val impact = A11yImpact.SERIOUS
    override val wcagCriteria = listOf("2.4.2", "4.1.2")
    override val description = "Tab items should have accessible labels describing their purpose"

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for (call in file.allCalls.filter { it.name in setOf("Tab", "LeadingIconTab") }) {
            // Check if tab has text content
            val text = call.getArgument("text")
            if (text != null && text.trim() != "null" && text.trim().isNotEmpty()) continue

            // Check for contentDescription in semantics
            if (call.hasSemanticsProperty("contentDescription")) continue

            // Check for a Text composable in the body
            if (call.rawArgumentText.contains("Text(")) continue

            diagnostics.add(
                makeDiagnostic(
                    message = "Tab has no accessible label. TalkBack users won't know what this tab represents.",
                    line = call.line,
                    column = call.column,
                    context = context,
                    suggestion = "Add text = { Text(\"Tab name\") } or semantics { contentDescription = \"...\" }"
                )
            )
        }
        return diagnostics
    }
}
