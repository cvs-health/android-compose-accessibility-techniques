package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import com.cvshealth.a11y.agent.scanner.ParsedKotlinFile

class HeadingSemanticsMissingRule : A11yRule {
    override val id = "heading-semantics-missing"
    override val name = "Heading Semantics Missing"
    override val severity = A11ySeverity.WARNING
    override val impact = A11yImpact.SERIOUS
    override val wcagCriteria = listOf("2.4.6", "1.3.1")
    override val description = "Text using heading typography should have semantics { heading() }"

    private val headingStyles = setOf(
        "headlineLarge", "headlineMedium", "headlineSmall",
        "titleLarge", "titleMedium", "titleSmall",
        "displayLarge", "displayMedium", "displaySmall"
    )

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for (call in file.allCalls.filter { it.name == "Text" }) {
            val styleArg = call.getArgument("style") ?: continue

            val hasHeadingStyle = headingStyles.any { styleArg.contains(it) }
            if (!hasHeadingStyle) continue

            // Skip if inside a known heading wrapper
            if (call.enclosingCallName in setOf("SimpleHeading", "GoodExampleHeading",
                    "BadExampleHeading", "OkExampleHeading", "ProblematicExampleHeading")) continue

            // Skip if inside Button/Link/Toggle
            if (call.enclosingCallName in setOf("Button", "TextButton", "IconButton",
                    "OutlinedButton", "Checkbox", "Switch", "RadioButton")) continue

            // Check for heading() in semantics on the modifier chain or enclosing scope
            if (call.hasSemanticsProperty("heading")) continue
            if (call.enclosingScopeText.contains("heading()")) continue

            val fix = computeHeadingFix(call, context)

            diagnostics.add(
                makeDiagnostic(
                    message = "Text uses heading typography (${styleArg.substringAfterLast(".")}) but lacks semantics { heading() }",
                    line = call.line,
                    column = call.column,
                    context = context,
                    fix = fix,
                    suggestion = "Add Modifier.semantics { heading() } to the Text or its container"
                )
            )
        }
        return diagnostics
    }

    private fun computeHeadingFix(call: com.cvshealth.a11y.agent.scanner.DetectedCall, context: RuleContext): A11yFix? {
        val lineOffset = lineColumnToOffset(context.sourceText, call.line)
        val callStart = context.sourceText.indexOf("Text(", lineOffset)
        if (callStart < 0) return null

        val modifierArg = call.getArgument("modifier")
        if (modifierArg != null) {
            // Append .semantics { heading() } to existing modifier chain
            val searchArea = context.sourceText.substring(callStart)
            val modIdx = searchArea.indexOf("modifier")
            if (modIdx >= 0) {
                // Find the end of the modifier expression (look for comma or next named arg)
                val absModStart = callStart + modIdx
                val modValueStart = context.sourceText.indexOf("=", absModStart)
                if (modValueStart >= 0) {
                    // Walk to find the "Modifier" token start and append after the chain
                    val afterEquals = modValueStart + 1
                    // Find the next comma or closing paren at the same depth
                    var depth = 0
                    var endOfModifier = afterEquals
                    for (i in afterEquals until context.sourceText.length) {
                        when (context.sourceText[i]) {
                            '(' -> depth++
                            ')' -> {
                                if (depth == 0) { endOfModifier = i; break }
                                depth--
                            }
                            '{' -> depth++
                            '}' -> {
                                if (depth == 0) { endOfModifier = i; break }
                                depth--
                            }
                            ',' -> if (depth == 0) { endOfModifier = i; break }
                        }
                    }
                    // Insert .semantics { heading() } before the comma/paren
                    val insertBefore = endOfModifier
                    // Skip trailing whitespace backward
                    var insertPoint = insertBefore
                    while (insertPoint > afterEquals && context.sourceText[insertPoint - 1].isWhitespace() && context.sourceText[insertPoint - 1] != '\n') {
                        insertPoint--
                    }
                    return A11yFix(
                        description = "Append .semantics { heading() } to modifier chain",
                        replacementText = ".semantics { heading() }",
                        startOffset = insertPoint,
                        endOffset = insertPoint
                    )
                }
            }
        } else {
            // No modifier argument — insert modifier = Modifier.semantics { heading() } as first arg
            val openParen = callStart + "Text".length
            if (openParen < context.sourceText.length && context.sourceText[openParen] == '(') {
                val insertOffset = openParen + 1
                return A11yFix(
                    description = "Add modifier with semantics { heading() }",
                    replacementText = "\n        modifier = Modifier.semantics { heading() },",
                    startOffset = insertOffset,
                    endOffset = insertOffset
                )
            }
        }
        return null
    }
}

class FakeHeadingInLabelRule : A11yRule {
    override val id = "fake-heading-in-label"
    override val name = "Fake Heading in Label"
    override val severity = A11ySeverity.WARNING
    override val impact = A11yImpact.MODERATE
    override val wcagCriteria = listOf("1.3.1")
    override val description = "Content descriptions should not contain the word 'heading'"

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()
        val headingPattern = Regex("""contentDescription\s*=\s*"[^"]*\bheading\b[^"]*"""", RegexOption.IGNORE_CASE)

        for (line in context.sourceLines.withIndex()) {
            val match = headingPattern.find(line.value)
            if (match != null) {
                diagnostics.add(
                    makeDiagnostic(
                        message = "Content description contains 'heading'. Use semantics { heading() } instead of including role in the label.",
                        line = line.index + 1,
                        column = match.range.first + 1,
                        context = context,
                        suggestion = "Remove 'heading' from contentDescription and add semantics { heading() }"
                    )
                )
            }
        }
        return diagnostics
    }
}
