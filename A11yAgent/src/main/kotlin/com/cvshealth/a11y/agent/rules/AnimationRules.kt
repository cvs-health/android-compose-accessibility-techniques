package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import com.cvshealth.a11y.agent.scanner.ParsedKotlinFile

class ReduceMotionRule : A11yRule {
    override val id = "reduce-motion"
    override val name = "Reduce Motion Not Checked"
    override val severity = A11ySeverity.WARNING
    override val impact = A11yImpact.MODERATE
    override val wcagCriteria = listOf("2.3.1")
    override val description = "Animations should respect the user's reduced motion accessibility setting"

    private val animationCalls = setOf(
        "AnimatedVisibility", "AnimatedContent", "Crossfade",
        "animateFloatAsState", "animateColorAsState", "animateDpAsState",
        "animateIntAsState", "animateContentSize"
    )
    private val animationPatterns = listOf(
        Regex("""animate\w+AsState"""),
        Regex("""Animatable\s*\("""),
        Regex("""infiniteTransition"""),
        Regex("""rememberInfiniteTransition""")
    )

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        // Check detected calls
        for (call in file.allCalls.filter { it.name in animationCalls }) {
            val scope = call.enclosingScopeText
            if (scope.contains("reduceMotion") ||
                scope.contains("isReduceMotionEnabled") ||
                scope.contains("LocalReduceMotion") ||
                scope.contains("prefersReducedMotion") ||
                scope.contains("ANIMATOR_DURATION_SCALE")) continue

            diagnostics.add(
                makeDiagnostic(
                    message = "${call.name} does not check for reduced motion preference.",
                    line = call.line,
                    column = call.column,
                    context = context,
                    suggestion = "Check accessibility settings for reduced motion and provide a non-animated alternative"
                )
            )
        }

        // Check line-level animation patterns
        for ((index, line) in context.sourceLines.withIndex()) {
            for (pattern in animationPatterns) {
                if (!pattern.containsMatchIn(line)) continue

                // Avoid duplicate reports from detected calls
                val alreadyReported = diagnostics.any { it.line == index + 1 }
                if (alreadyReported) continue

                val start = maxOf(0, index - 10)
                val end = minOf(context.sourceLines.size - 1, index + 10)
                val scopeText = context.sourceLines.subList(start, end + 1).joinToString("\n")

                if (scopeText.contains("reduceMotion") ||
                    scopeText.contains("isReduceMotionEnabled") ||
                    scopeText.contains("ANIMATOR_DURATION_SCALE")) continue

                diagnostics.add(
                    makeDiagnostic(
                        message = "Animation pattern without reduced motion check.",
                        line = index + 1,
                        column = (pattern.find(line)?.range?.first ?: 0) + 1,
                        context = context,
                        suggestion = "Check accessibility settings for reduced motion and provide a non-animated alternative"
                    )
                )
            }
        }
        return diagnostics
    }
}
