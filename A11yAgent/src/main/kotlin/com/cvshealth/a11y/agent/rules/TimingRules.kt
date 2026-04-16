package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import com.cvshealth.a11y.agent.scanner.ParsedKotlinFile

class TimingAdjustableRule : A11yRule {
    override val id = "timing-adjustable"
    override val name = "Timing Not Adjustable"
    override val severity = A11ySeverity.WARNING
    override val impact = A11yImpact.SERIOUS
    override val wcagCriteria = listOf("2.2.1")
    override val description = "Timed content should be adjustable, extendable, or removable by the user"

    private val timingPatterns = listOf(
        Regex("""delay\s*\(\s*\d+"""),
        Regex("""withTimeout\s*\(\s*\d+"""),
        Regex("""LaunchedEffect.*delay"""),
        Regex("""postDelayed"""),
        Regex("""CountDownTimer"""),
        Regex("""Timer\s*\(""")
    )

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for ((index, line) in context.sourceLines.withIndex()) {
            for (pattern in timingPatterns) {
                if (!pattern.containsMatchIn(line)) continue

                val start = maxOf(0, index - 10)
                val end = minOf(context.sourceLines.size - 1, index + 10)
                val scope = context.sourceLines.subList(start, end + 1).joinToString("\n")

                // Skip if there's user control over the timing
                if (scope.contains("dismiss") || scope.contains("cancel") ||
                    scope.contains("extend") || scope.contains("pause") ||
                    scope.contains("userAction") || scope.contains("Snackbar")) continue

                diagnostics.add(
                    makeDiagnostic(
                        message = "Timed operation detected. Users must be able to extend, adjust, or disable time limits.",
                        line = index + 1,
                        column = (pattern.find(line)?.range?.first ?: 0) + 1,
                        context = context,
                        suggestion = "Ensure users can extend or disable the time limit (WCAG 2.2.1)"
                    )
                )
                break
            }
        }
        return diagnostics
    }
}

class DialogFocusManagementRule : A11yRule {
    override val id = "dialog-focus-management"
    override val name = "Dialog Focus Management"
    override val severity = A11ySeverity.INFO
    override val impact = A11yImpact.MODERATE
    override val wcagCriteria = listOf("2.4.3")
    override val description = "Dialogs and bottom sheets should manage focus for keyboard and TalkBack users"

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()
        val dialogCalls = setOf("AlertDialog", "Dialog", "ModalBottomSheet", "BottomSheetScaffold")

        for (call in file.allCalls.filter { it.name in dialogCalls }) {
            // Check for focus management
            val scope = call.rawArgumentText + call.enclosingScopeText
            if (scope.contains("FocusRequester") ||
                scope.contains("requestFocus") ||
                scope.contains("focusRequester") ||
                scope.contains("onDismissRequest")) continue

            diagnostics.add(
                makeDiagnostic(
                    message = "${call.name} may need explicit focus management for TalkBack and keyboard users.",
                    line = call.line,
                    column = call.column,
                    context = context,
                    suggestion = "Use FocusRequester to move focus into the dialog when it opens and return focus when it closes"
                )
            )
        }
        return diagnostics
    }
}

class GestureMissingAlternativeRule : A11yRule {
    override val id = "gesture-missing-alternative"
    override val name = "Custom Gesture Missing Alternative"
    override val severity = A11ySeverity.WARNING
    override val impact = A11yImpact.SERIOUS
    override val wcagCriteria = listOf("2.1.1", "2.5.1")
    override val description = "Custom gestures must have keyboard/TalkBack-accessible alternatives"

    private val gesturePatterns = listOf(
        Regex("""pointerInput\s*\("""),
        Regex("""detectDragGestures"""),
        Regex("""detectTapGestures"""),
        Regex("""detectTransformGestures"""),
        Regex("""onLongPress"""),
        Regex("""swipeable"""),
        Regex("""anchoredDraggable""")
    )

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for ((index, line) in context.sourceLines.withIndex()) {
            for (pattern in gesturePatterns) {
                if (!pattern.containsMatchIn(line)) continue

                val start = maxOf(0, index - 15)
                val end = minOf(context.sourceLines.size - 1, index + 15)
                val scope = context.sourceLines.subList(start, end + 1).joinToString("\n")

                // Check for alternatives
                if (scope.contains("customActions") ||
                    scope.contains("accessibilityAction") ||
                    scope.contains("onClickLabel") ||
                    scope.contains("clickable") ||
                    scope.contains("KeyEvent")) continue

                diagnostics.add(
                    makeDiagnostic(
                        message = "Custom gesture detected without an accessible alternative.",
                        line = index + 1,
                        column = (pattern.find(line)?.range?.first ?: 0) + 1,
                        context = context,
                        suggestion = "Add accessibility custom actions or clickable alternatives for keyboard/TalkBack users"
                    )
                )
                break
            }
        }
        return diagnostics
    }
}

class InputPurposeRule : A11yRule {
    override val id = "input-purpose"
    override val name = "Input Purpose Not Identified"
    override val severity = A11ySeverity.INFO
    override val impact = A11yImpact.MODERATE
    override val wcagCriteria = listOf("1.3.5")
    override val description = "TextFields should use semantics { contentType = ... } for autofill support"

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()
        val textFieldCalls = setOf("TextField", "OutlinedTextField", "BasicTextField")

        for (call in file.allCalls.filter { it.name in textFieldCalls }) {
            // Check for contentType in semantics
            if (call.hasSemanticsProperty("contentType")) continue
            if (call.modifierChain.contains("contentType")) continue

            // Check for keyboardOptions hint that this is a specific input type
            val keyboardType = call.getArgument("keyboardOptions")
            val hintInput = keyboardType != null && (
                keyboardType.contains("Email") || keyboardType.contains("Phone") ||
                keyboardType.contains("Password") || keyboardType.contains("Uri") ||
                keyboardType.contains("Number"))

            if (hintInput && !call.hasSemanticsProperty("contentType")) {
                diagnostics.add(
                    makeDiagnostic(
                        message = "TextField has a specific keyboard type but no contentType semantics for autofill.",
                        line = call.line,
                        column = call.column,
                        context = context,
                        suggestion = "Add Modifier.semantics { contentType = ContentType.EmailAddress } (or appropriate type)"
                    )
                )
            }
        }
        return diagnostics
    }
}
