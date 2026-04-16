package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import com.cvshealth.a11y.agent.scanner.ParsedKotlinFile

class GenericLinkTextRule : A11yRule {
    override val id = "generic-link-text"
    override val name = "Generic Link Text"
    override val severity = A11ySeverity.WARNING
    override val impact = A11yImpact.MODERATE
    override val wcagCriteria = listOf("2.4.4")
    override val description = "Clickable elements should have descriptive labels, not generic text"

    private val genericTexts = setOf(
        "click here", "here", "learn more", "read more", "more",
        "tap here", "press here", "go", "link", "see more",
        "more info", "details", "continue"
    )

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for (call in file.allCalls) {
            if (!call.modifierChain.contains(".clickable(") &&
                call.name !in setOf("ClickableText", "TextButton")) continue

            // Extract text content
            val textContent = call.getArgument("__pos_0")
                ?: call.getArgument("text")
                ?: call.rawArgumentText

            for (generic in genericTexts) {
                val pattern = Regex(""""${Regex.escape(generic)}"""", RegexOption.IGNORE_CASE)
                if (pattern.containsMatchIn(textContent)) {
                    diagnostics.add(
                        makeDiagnostic(
                            message = "Clickable element uses generic text \"$generic\". Provide descriptive text that explains the destination or action.",
                            line = call.line,
                            column = call.column,
                            context = context,
                            suggestion = "Replace generic text with a description of where the link goes or what it does"
                        )
                    )
                    break
                }
            }
        }
        return diagnostics
    }
}

class ButtonUsedAsLinkRule : A11yRule {
    override val id = "button-used-as-link"
    override val name = "Button Used as Link"
    override val severity = A11ySeverity.INFO
    override val impact = A11yImpact.MINOR
    override val wcagCriteria = listOf("2.4.4")
    override val description = "Buttons that navigate to URLs should use link semantics"

    private val urlPatterns = listOf(
        Regex("""Uri\.parse"""),
        Regex("""Intent\.ACTION_VIEW"""),
        Regex("""CustomTabsIntent"""),
        Regex("""openUri"""),
        Regex("""https?://"""),
        Regex("""uriHandler\.openUri""")
    )

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        val buttonCalls = setOf("Button", "TextButton", "OutlinedButton",
            "FilledTonalButton", "ElevatedButton")

        for (call in file.allCalls.filter { it.name in buttonCalls }) {
            val body = call.rawArgumentText + call.enclosingScopeText
            val hasUrl = urlPatterns.any { it.containsMatchIn(body) }
            if (!hasUrl) continue

            diagnostics.add(
                makeDiagnostic(
                    message = "Button appears to open a URL. Consider adding link role semantics for TalkBack.",
                    line = call.line,
                    column = call.column,
                    context = context,
                    suggestion = "Add Modifier.semantics { role = Role.Link } or use clickable(onClickLabel = \"Opens in browser\")"
                )
            )
        }
        return diagnostics
    }
}
