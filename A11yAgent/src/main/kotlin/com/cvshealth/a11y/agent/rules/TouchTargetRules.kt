package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import com.cvshealth.a11y.agent.scanner.ParsedKotlinFile

class SmallTouchTargetRule : A11yRule {
    override val id = "small-touch-target"
    override val name = "Small Touch Target"
    override val severity = A11ySeverity.ERROR
    override val impact = A11yImpact.SERIOUS
    override val wcagCriteria = listOf("2.5.8")
    override val description = "Interactive elements must meet minimum touch target size (48dp)"

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()
        val minTarget = context.configOptions.minTouchTarget
        val sizePattern = Regex("""\.\s*size\s*\(\s*(\d+)\.dp\s*\)""")
        val widthPattern = Regex("""\.\s*width\s*\(\s*(\d+)\.dp\s*\)""")
        val heightPattern = Regex("""\.\s*height\s*\(\s*(\d+)\.dp\s*\)""")

        val interactiveCalls = setOf("IconButton", "Button", "TextButton", "OutlinedButton",
            "FloatingActionButton", "ExtendedFloatingActionButton")

        for (call in file.allCalls.filter { it.name in interactiveCalls }) {
            val chain = call.modifierChain

            // Skip if minimumInteractiveComponentSize is used
            if (chain.contains("minimumInteractiveComponentSize")) continue

            // Check for explicit small sizes
            val sizeMatch = sizePattern.find(chain)
            val widthMatch = widthPattern.find(chain)
            val heightMatch = heightPattern.find(chain)

            val explicitSize = sizeMatch?.groupValues?.get(1)?.toIntOrNull()
            val explicitWidth = widthMatch?.groupValues?.get(1)?.toIntOrNull()
            val explicitHeight = heightMatch?.groupValues?.get(1)?.toIntOrNull()

            val tooSmall = (explicitSize != null && explicitSize < minTarget) ||
                (explicitWidth != null && explicitWidth < minTarget) ||
                (explicitHeight != null && explicitHeight < minTarget)

            if (tooSmall) {
                val dimension = explicitSize ?: minOf(explicitWidth ?: minTarget, explicitHeight ?: minTarget)
                diagnostics.add(
                    makeDiagnostic(
                        message = "${call.name} has explicit size ${dimension}dp which is below the ${minTarget}dp minimum touch target.",
                        line = call.line,
                        column = call.column,
                        context = context,
                        suggestion = "Add Modifier.minimumInteractiveComponentSize() or increase size to at least ${minTarget}dp"
                    )
                )
            }
        }
        return diagnostics
    }
}
