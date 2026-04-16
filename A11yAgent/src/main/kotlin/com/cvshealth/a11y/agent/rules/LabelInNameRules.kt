/*
 * Copyright 2026 CVS Health and/or one of its affiliates
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import com.cvshealth.a11y.agent.scanner.DetectedCall
import com.cvshealth.a11y.agent.scanner.ParsedKotlinFile

/**
 * WCAG 2.5.3 Label in Name (Level A)
 *
 * Flags composables where the accessible name (contentDescription) does not
 * contain the visible text label. Voice Control / Voice Access users speak
 * the visible text to activate controls — if the accessible name diverges,
 * they cannot interact with the element.
 *
 * ERROR  — visible text is not contained in the accessible name at all
 * WARNING — visible text is present but does not appear at the start
 */
class LabelInNameRule : A11yRule {
    override val id = "label-in-name"
    override val name = "Label in Name"
    override val severity = A11ySeverity.ERROR
    override val impact = A11yImpact.SERIOUS
    override val wcagCriteria = listOf("2.5.3")
    override val description =
        "Visible text label must be contained in the accessible name (contentDescription). " +
        "Voice Control users speak the visible text to activate controls."

    private val targetCalls = setOf(
        "Button", "TextButton", "OutlinedButton",
        "FilledTonalButton", "ElevatedButton", "IconButton",
        "Tab", "LeadingIconTab"
    )

    private val textCallPattern = Regex("""Text\s*\(\s*"((?:[^"\\]|\\.)*)"""")
    private val contentDescPattern = Regex("""contentDescription\s*=\s*"((?:[^"\\]|\\.)*)"""")
    private val stringLiteralPattern = Regex("""^"((?:[^"\\]|\\.)*)"$""")

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for (call in file.allCalls.filter { it.name in targetCalls }) {
            // Skip hidden elements
            if (call.hasSemanticsProperty("invisibleToUser") ||
                call.modifierChain.contains("clearAndSetSemantics")) {
                continue
            }

            val visibleText = extractVisibleText(call) ?: continue
            val accessibleName = extractAccessibleName(call) ?: continue

            val visibleLower = visibleText.lowercase().trim()
            val accessibleLower = accessibleName.lowercase().trim()

            if (visibleLower.isBlank() || accessibleLower.isBlank()) continue

            if (!accessibleLower.contains(visibleLower)) {
                diagnostics.add(
                    makeDiagnostic(
                        message = "${call.name} visible text \"$visibleText\" is not contained in " +
                            "contentDescription \"$accessibleName\". Voice Control users speak " +
                            "the visible text to activate controls.",
                        line = call.line,
                        column = call.column,
                        context = context,
                        severityOverride = A11ySeverity.ERROR,
                        suggestion = "Change contentDescription to include \"$visibleText\" " +
                            "or start with it, e.g. contentDescription = \"$visibleText\""
                    )
                )
            } else if (!accessibleLower.startsWith(visibleLower)) {
                diagnostics.add(
                    makeDiagnostic(
                        message = "${call.name} visible text \"$visibleText\" appears in " +
                            "contentDescription \"$accessibleName\" but not at the start. " +
                            "WCAG 2.5.3 recommends the accessible name begins with the visible text.",
                        line = call.line,
                        column = call.column,
                        context = context,
                        severityOverride = A11ySeverity.WARNING,
                        suggestion = "Reorder contentDescription to start with \"$visibleText\""
                    )
                )
            }
        }
        return diagnostics
    }

    /**
     * Extract visible text from the composable call.
     * Looks for Text("...") calls in the raw argument text, enclosing scope,
     * or the text= argument lambda.
     */
    private fun extractVisibleText(call: DetectedCall): String? {
        // For Tab: check the text argument which is a lambda like { Text("Home") }
        val textArg = call.getArgument("text")
        if (textArg != null) {
            val match = textCallPattern.find(textArg)
            if (match != null) return match.groupValues[1]
        }

        // Check raw argument text first (inline content)
        val rawMatch = textCallPattern.find(call.rawArgumentText)
        if (rawMatch != null) return rawMatch.groupValues[1]

        // For Button variants: trailing lambda content is in enclosingScopeText
        // but not in rawArgumentText. Search the scope text for Text("...")
        // appearing after this call's line.
        val scopeMatch = textCallPattern.find(call.enclosingScopeText)
        if (scopeMatch != null) return scopeMatch.groupValues[1]

        return null
    }

    /**
     * Extract the accessible name from contentDescription argument or semantics block.
     * Returns null if the value is non-literal (resource ref, variable) or absent.
     */
    private fun extractAccessibleName(call: DetectedCall): String? {
        // Check direct contentDescription argument
        val directArg = call.getArgument("contentDescription")
        if (directArg != null && directArg.trim() != "null") {
            val literal = stringLiteralPattern.find(directArg.trim())
            if (literal != null) return literal.groupValues[1]
            // Non-literal (e.g., stringResource(...)) — skip
            return null
        }

        // Check semantics block in modifier chain
        val semanticsBlock = call.getModifierBlock("semantics") ?: return null
        val match = contentDescPattern.find(semanticsBlock) ?: return null
        return match.groupValues[1]

        // Also check enclosing scope for contentDescription set externally
    }
}
