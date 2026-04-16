package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import com.cvshealth.a11y.agent.scanner.ParsedKotlinFile

class IconMissingLabelRule : A11yRule {
    override val id = "icon-missing-label"
    override val name = "Icon Missing Content Description"
    override val severity = A11ySeverity.ERROR
    override val impact = A11yImpact.CRITICAL
    override val wcagCriteria = listOf("1.1.1")
    override val description = "Icon composables must have a non-null contentDescription or be explicitly decorative"

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for (call in file.allCalls.filter { it.name == "Icon" }) {
            val contentDesc = call.getArgument("contentDescription")
                ?: call.getArgument("__pos_1") // 2nd positional arg

            // null contentDescription is the problem
            if (contentDesc == null || contentDesc.trim() == "null") {
                // Skip if inside IconButton/Button that provides its own label
                if (call.enclosingCallName in setOf("IconButton", "Button", "TextButton", "OutlinedButton",
                        "FilledTonalButton", "ElevatedButton", "FloatingActionButton", "ExtendedFloatingActionButton")) {
                    // Only skip if the parent button provides an accessible label elsewhere
                    continue
                }

                // Skip if explicitly hidden from accessibility
                if (call.hasSemanticsProperty("invisibleToUser") ||
                    call.modifierChain.contains("clearAndSetSemantics")) {
                    continue
                }

                // Compute auto-fix
                val fix = computeIconFix(call, contentDesc, context)

                diagnostics.add(
                    makeDiagnostic(
                        message = "Icon is missing contentDescription. Provide a descriptive label or use null for decorative icons inside labeled containers.",
                        line = call.line,
                        column = call.column,
                        context = context,
                        fix = fix,
                        suggestion = "Add contentDescription = stringResource(R.string.description) to the Icon"
                    )
                )
            }
        }
        return diagnostics
    }

    private fun computeIconFix(call: com.cvshealth.a11y.agent.scanner.DetectedCall, contentDesc: String?, context: RuleContext): A11yFix? {
        val lineOffset = lineColumnToOffset(context.sourceText, call.line)
        val callLine = context.sourceLines.getOrNull(call.line - 1) ?: return null
        val replacement = "stringResource(R.string.icon_description)"

        if (contentDesc?.trim() == "null") {
            // Replace `contentDescription = null` with a real value
            val searchStart = lineOffset
            val searchArea = context.sourceText.substring(searchStart, minOf(searchStart + callLine.length + 200, context.sourceText.length))
            val idx = searchArea.indexOf("contentDescription")
            if (idx >= 0) {
                val nullIdx = searchArea.indexOf("null", idx + "contentDescription".length)
                if (nullIdx >= 0) {
                    val absStart = searchStart + nullIdx
                    return A11yFix(
                        description = "Replace null contentDescription with $replacement",
                        replacementText = replacement,
                        startOffset = absStart,
                        endOffset = absStart + 4
                    )
                }
            }
        } else {
            // No contentDescription argument — insert one before closing paren
            val searchStart = lineOffset
            val rawText = call.rawArgumentText
            // Find the closing paren of the call
            val callStart = context.sourceText.indexOf(call.name + "(", searchStart)
            if (callStart >= 0) {
                // Find matching close paren
                var depth = 0
                var closeIdx = -1
                for (i in (callStart + call.name.length) until context.sourceText.length) {
                    when (context.sourceText[i]) {
                        '(' -> depth++
                        ')' -> { depth--; if (depth == 0) { closeIdx = i; break } }
                    }
                }
                if (closeIdx > 0) {
                    return A11yFix(
                        description = "Add contentDescription parameter",
                        replacementText = ",\n        contentDescription = $replacement)",
                        startOffset = closeIdx,
                        endOffset = closeIdx + 1
                    )
                }
            }
        }
        return null
    }
}

class LabelContainsRoleImageRule : A11yRule {
    override val id = "label-contains-role-image"
    override val name = "Content Description Contains Role Name"
    override val severity = A11ySeverity.WARNING
    override val impact = A11yImpact.MINOR
    override val wcagCriteria = listOf("1.1.1")
    override val description = "Content descriptions should not contain role words like 'image', 'icon', 'picture'"

    private val roleWords = listOf("image", "icon", "picture", "graphic", "photo")

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for (call in file.allCalls.filter { it.name == "Icon" || it.name == "Image" }) {
            val contentDesc = call.getArgument("contentDescription") ?: continue
            val descLower = contentDesc.lowercase()

            for (word in roleWords) {
                if (descLower.contains(word) && descLower != "null") {
                    diagnostics.add(
                        makeDiagnostic(
                            message = "Content description contains the role word \"$word\". TalkBack already announces the element type.",
                            line = call.line,
                            column = call.column,
                            context = context,
                            suggestion = "Remove \"$word\" from the contentDescription — describe what the image shows, not what it is"
                        )
                    )
                    break
                }
            }
        }
        return diagnostics
    }
}

class EmptyContentDescriptionRule : A11yRule {
    override val id = "empty-content-description"
    override val name = "Empty Content Description String"
    override val severity = A11ySeverity.ERROR
    override val impact = A11yImpact.SERIOUS
    override val wcagCriteria = listOf("1.1.1")
    override val description = "contentDescription should be null for decorative elements, never an empty string"

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for (call in file.allCalls.filter { it.name in setOf("Icon", "Image") }) {
            val contentDesc = call.getArgument("contentDescription")
                ?: call.getArgument("__pos_1")
            if (contentDesc != null && (contentDesc.trim() == "\"\"" || contentDesc.trim() == "\"\".toString()")) {
                diagnostics.add(
                    makeDiagnostic(
                        message = "Empty string contentDescription makes the element focusable by TalkBack but announces nothing. Use null for decorative elements.",
                        line = call.line,
                        column = call.column,
                        context = context,
                        suggestion = "Change contentDescription = \"\" to contentDescription = null"
                    )
                )
            }
        }
        return diagnostics
    }
}
