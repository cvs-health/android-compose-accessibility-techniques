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

package com.cvshealth.accessibility.lint

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Location
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.client.api.UElementHandler
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UFile

class ComposeTextScanDetector : Detector(), SourceCodeScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>> =
        listOf(UFile::class.java)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {
        override fun visitFile(node: UFile) {
            if (!context.file.name.endsWith(".kt")) return

            val fileName = context.file.name
            if (fileName in THEME_FILES) return

            val source = context.getContents()?.toString() ?: return

            checkFixedFontSize(context, source)
            checkMaxLinesOne(context, source)
            checkHeadingSemanticsMissing(context, source)
            checkFakeHeadingInLabel(context, source)
            checkClickableMissingRole(context, source)
            checkHardcodedColor(context, source, fileName)
            checkVisuallyDisabledNotSemantically(context, source)
            checkGenericLinkText(context, source)
            checkReduceMotion(context, source)
            checkGestureMissingAlternative(context, source)
            checkTimingAdjustable(context, source)
            checkDialogFocusManagement(context, source)
        }
    }

    private fun isComment(source: String, matchStart: Int): Boolean {
        val lineStart = source.lastIndexOf('\n', matchStart) + 1
        val lineEnd = source.indexOf('\n', matchStart).let { if (it == -1) source.length else it }
        val lineText = source.substring(lineStart, lineEnd).trimStart()
        return lineText.startsWith("//") || lineText.startsWith("*")
    }

    private fun getContext(source: String, matchStart: Int, linesBefore: Int = 5, linesAfter: Int = 5): String {
        var start = matchStart
        for (i in 0 until linesBefore) {
            val prev = source.lastIndexOf('\n', start - 1)
            if (prev == -1) { start = 0; break }
            start = prev
        }
        var end = matchStart
        for (i in 0 until linesAfter) {
            val next = source.indexOf('\n', end + 1)
            if (next == -1) { end = source.length; break }
            end = next
        }
        return source.substring(start, end)
    }

    // --- Existing rules ---

    private fun checkFixedFontSize(context: JavaContext, source: String) {
        val pattern = Regex("""\b(\d+)\.sp\b""")
        for (match in pattern.findAll(source)) {
            if (isComment(source, match.range.first)) continue
            context.report(
                FIXED_FONT_SIZE,
                Location.create(context.file, source, match.range.first, match.range.last + 1),
                "Hardcoded font size `${match.value}`. Use `MaterialTheme.typography` to support user text size preferences."
            )
        }
    }

    private fun checkMaxLinesOne(context: JavaContext, source: String) {
        val pattern = Regex("""maxLines\s*=\s*1\b""")
        for (match in pattern.findAll(source)) {
            if (isComment(source, match.range.first)) continue
            context.report(
                MAX_LINES_ONE,
                Location.create(context.file, source, match.range.first, match.range.last + 1),
                "`maxLines = 1` truncates text, which may hide content from users who need larger text."
            )
        }
    }

    // --- New rules ---

    private fun checkHeadingSemanticsMissing(context: JavaContext, source: String) {
        val headingStyles = listOf(
            "headlineLarge", "headlineMedium", "headlineSmall",
            "titleLarge", "titleMedium", "titleSmall",
            "displayLarge", "displayMedium", "displaySmall"
        )
        val pattern = Regex("""Text\s*\(""")
        for (match in pattern.findAll(source)) {
            if (isComment(source, match.range.first)) continue

            // Get the call context (next ~500 chars or until matching paren)
            val callEnd = minOf(match.range.first + 500, source.length)
            val callText = source.substring(match.range.first, callEnd)

            val hasHeadingStyle = headingStyles.any { callText.contains(it) }
            if (!hasHeadingStyle) continue

            // Check surrounding context for heading()
            val ctx = getContext(source, match.range.first, 10, 10)
            if (ctx.contains("heading()") || ctx.contains("SimpleHeading") ||
                ctx.contains("GoodExampleHeading") || ctx.contains("BadExampleHeading")) continue

            context.report(
                HEADING_SEMANTICS_MISSING,
                Location.create(context.file, source, match.range.first, match.range.last + 1),
                "Text uses heading typography but lacks `semantics { heading() }`. Screen readers need heading semantics for navigation."
            )
        }
    }

    private fun checkFakeHeadingInLabel(context: JavaContext, source: String) {
        val pattern = Regex("""contentDescription\s*=\s*"[^"]*\bheading\b[^"]*"""", RegexOption.IGNORE_CASE)
        for (match in pattern.findAll(source)) {
            if (isComment(source, match.range.first)) continue
            context.report(
                FAKE_HEADING_IN_LABEL,
                Location.create(context.file, source, match.range.first, match.range.last + 1),
                "Content description contains 'heading'. Use `semantics { heading() }` instead of role words in labels."
            )
        }
    }

    private fun checkClickableMissingRole(context: JavaContext, source: String) {
        val pattern = Regex("""\.clickable\s*\(""")
        for (match in pattern.findAll(source)) {
            if (isComment(source, match.range.first)) continue

            val callEnd = minOf(match.range.first + 300, source.length)
            val callText = source.substring(match.range.first, callEnd)
            if (callText.contains("role =") || callText.contains("Role.")) continue

            context.report(
                CLICKABLE_MISSING_ROLE,
                Location.create(context.file, source, match.range.first, match.range.last + 1),
                "`.clickable()` without a `role` parameter. Specify `role = Role.Button` (or appropriate role) for screen readers."
            )
        }
    }

    private fun checkHardcodedColor(context: JavaContext, source: String, fileName: String) {
        if (fileName in setOf("Color.kt", "Theme.kt", "Colors.kt")) return

        val namedColors = listOf(
            "Color.Black", "Color.White", "Color.Red", "Color.Green", "Color.Blue",
            "Color.Yellow", "Color.Cyan", "Color.Magenta", "Color.Gray",
            "Color.LightGray", "Color.DarkGray"
        )
        val hexPattern = Regex("""Color\s*\(\s*0x[0-9A-Fa-f]+\s*\)""")

        for (color in namedColors) {
            var idx = source.indexOf(color)
            while (idx >= 0) {
                if (!isComment(source, idx)) {
                    // Skip val declarations
                    val lineStart = source.lastIndexOf('\n', idx) + 1
                    val lineText = source.substring(lineStart, source.indexOf('\n', idx).let { if (it == -1) source.length else it })
                    if (!lineText.contains("val ") && !lineText.contains("private ")) {
                        context.report(
                            HARDCODED_COLOR,
                            Location.create(context.file, source, idx, idx + color.length),
                            "Hardcoded color `$color` won't adapt to dark mode or high-contrast themes. Use `MaterialTheme.colorScheme`."
                        )
                    }
                }
                idx = source.indexOf(color, idx + 1)
            }
        }

        for (match in hexPattern.findAll(source)) {
            if (isComment(source, match.range.first)) continue
            val lineStart = source.lastIndexOf('\n', match.range.first) + 1
            val lineText = source.substring(lineStart, source.indexOf('\n', match.range.first).let { if (it == -1) source.length else it })
            if (lineText.contains("val ") || lineText.contains("private ")) continue
            context.report(
                HARDCODED_COLOR,
                Location.create(context.file, source, match.range.first, match.range.last + 1),
                "Hardcoded hex color won't adapt to dark mode or high-contrast themes. Use `MaterialTheme.colorScheme`."
            )
        }
    }

    private fun checkVisuallyDisabledNotSemantically(context: JavaContext, source: String) {
        val pattern = Regex("""\.alpha\s*\(\s*(0\.\d+|0)\s*\)""")
        for (match in pattern.findAll(source)) {
            if (isComment(source, match.range.first)) continue

            val valueStr = match.groupValues.getOrNull(1) ?: continue
            val value = valueStr.toDoubleOrNull() ?: continue
            if (value >= 0.5) continue

            val ctx = getContext(source, match.range.first)
            if (ctx.contains("enabled = false") || ctx.contains("disabled = true")) continue

            context.report(
                VISUALLY_DISABLED_NOT_SEMANTICALLY,
                Location.create(context.file, source, match.range.first, match.range.last + 1),
                "Element is visually dimmed with `alpha($valueStr)` but may still be interactive. Add `enabled = false` or disable semantically."
            )
        }
    }

    private fun checkGenericLinkText(context: JavaContext, source: String) {
        val genericPhrases = listOf("click here", "learn more", "read more", "tap here", "here", "more info")
        val pattern = Regex(""""(${genericPhrases.joinToString("|") { Regex.escape(it) }})"""", RegexOption.IGNORE_CASE)
        for (match in pattern.findAll(source)) {
            if (isComment(source, match.range.first)) continue
            val ctx = getContext(source, match.range.first, 3, 3)
            if (!ctx.contains("clickable") && !ctx.contains("ClickableText") &&
                !ctx.contains("LinkAnnotation") && !ctx.contains("pushStringAnnotation")) continue

            context.report(
                GENERIC_LINK_TEXT,
                Location.create(context.file, source, match.range.first, match.range.last + 1),
                "Generic link text \"${match.groupValues[1]}\". Use descriptive text that makes sense out of context."
            )
        }
    }

    private fun checkReduceMotion(context: JavaContext, source: String) {
        val animationCalls = listOf("AnimatedVisibility", "animateFloatAsState", "animateDpAsState",
            "animateColorAsState", "InfiniteTransition", "rememberInfiniteTransition",
            "updateTransition", "AnimatedContent")
        for (call in animationCalls) {
            var idx = source.indexOf(call)
            while (idx >= 0) {
                if (!isComment(source, idx)) {
                    val ctx = getContext(source, idx, 10, 10)
                    if (!ctx.contains("reduceMotion") && !ctx.contains("isReduceMotionEnabled") &&
                        !ctx.contains("motionScheme")) {
                        context.report(
                            REDUCE_MOTION,
                            Location.create(context.file, source, idx, idx + call.length),
                            "`$call` used without checking reduced motion preference. Consider honoring `AccessibilityManager.isReduceMotionEnabled`."
                        )
                    }
                }
                idx = source.indexOf(call, idx + 1)
            }
        }
    }

    private fun checkGestureMissingAlternative(context: JavaContext, source: String) {
        val gestureCalls = listOf("pointerInput", "detectDragGestures", "detectTapGestures",
            "onLongPress", "detectHorizontalDragGestures", "detectVerticalDragGestures")
        for (call in gestureCalls) {
            val pattern = Regex("""\b${Regex.escape(call)}\s*[\({]""")
            for (match in pattern.findAll(source)) {
                if (isComment(source, match.range.first)) continue
                val ctx = getContext(source, match.range.first, 10, 10)
                if (ctx.contains("customActions") || ctx.contains("clickable") ||
                    ctx.contains("onClickLabel") || ctx.contains("Button(")) continue

                context.report(
                    GESTURE_MISSING_ALTERNATIVE,
                    Location.create(context.file, source, match.range.first, match.range.last + 1),
                    "`$call` gesture with no alternative input method. Provide a button or accessibility action as a fallback."
                )
            }
        }
    }

    private fun checkTimingAdjustable(context: JavaContext, source: String) {
        val pattern = Regex("""\b(delay|withTimeout|withTimeoutOrNull)\s*\(\s*(\d{4,})\s*\)""")
        for (match in pattern.findAll(source)) {
            if (isComment(source, match.range.first)) continue
            val ctx = getContext(source, match.range.first, 5, 5)
            if (ctx.contains("adjustable") || ctx.contains("userTimeout") || ctx.contains("extend")) continue

            val duration = match.groupValues[2].toLongOrNull() ?: continue
            context.report(
                TIMING_ADJUSTABLE,
                Location.create(context.file, source, match.range.first, match.range.last + 1),
                "Hardcoded timing of ${duration}ms. Users with disabilities may need more time. Consider making the duration adjustable."
            )
        }
    }

    private fun checkDialogFocusManagement(context: JavaContext, source: String) {
        val dialogCalls = listOf("AlertDialog", "Dialog", "ModalBottomSheet")
        for (call in dialogCalls) {
            val pattern = Regex("""\b${Regex.escape(call)}\s*\(""")
            for (match in pattern.findAll(source)) {
                if (isComment(source, match.range.first)) continue

                val callEnd = minOf(match.range.first + 1000, source.length)
                val callText = source.substring(match.range.first, callEnd)
                // Find matching close brace/paren (approximate)
                if (callText.contains("FocusRequester") || callText.contains("requestFocus") ||
                    callText.contains("focusRequester")) continue

                context.report(
                    DIALOG_FOCUS_MANAGEMENT,
                    Location.create(context.file, source, match.range.first, match.range.last + 1),
                    "`$call` without explicit focus management. Use `FocusRequester` to direct focus for keyboard and screen reader users."
                )
            }
        }
    }

    companion object {
        private val THEME_FILES = setOf("Type.kt", "Theme.kt", "Color.kt", "Typography.kt")

        val FIXED_FONT_SIZE = Issue.create(
            id = "A11yFixedFontSize",
            briefDescription = "Hardcoded font size",
            explanation = """
                Hardcoded `.sp` font sizes don't follow the user's text size preferences \
                set in system settings. Use `MaterialTheme.typography` styles instead to \
                ensure text scales properly.

                WCAG 1.4.4 Resize Text (Level AA)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeTextScanDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val MAX_LINES_ONE = Issue.create(
            id = "A11yMaxLinesOne",
            briefDescription = "Text limited to single line",
            explanation = """
                `maxLines = 1` causes text to be truncated with ellipsis. Users who need \
                larger text sizes may lose content. Consider allowing text to wrap or \
                providing the full text through other means.

                WCAG 1.4.4 Resize Text (Level AA)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 5,
            severity = Severity.INFORMATIONAL,
            implementation = Implementation(
                ComposeTextScanDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val HEADING_SEMANTICS_MISSING = Issue.create(
            id = "A11yHeadingSemanticsMissing",
            briefDescription = "Heading typography without heading semantics",
            explanation = """
                Text using heading typography (headline*, title*, display*) should have \
                `Modifier.semantics { heading() }` so screen readers can navigate by headings.

                WCAG 2.4.6 Headings and Labels (Level AA) / WCAG 1.3.1 Info and Relationships (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 7,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeTextScanDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val FAKE_HEADING_IN_LABEL = Issue.create(
            id = "A11yFakeHeadingInLabel",
            briefDescription = "Content description contains 'heading'",
            explanation = """
                Content descriptions should not contain the word "heading". Use \
                `semantics { heading() }` to mark headings programmatically instead.

                WCAG 1.3.1 Info and Relationships (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeTextScanDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val CLICKABLE_MISSING_ROLE = Issue.create(
            id = "A11yClickableMissingRole",
            briefDescription = "Clickable modifier without role",
            explanation = """
                `.clickable()` without a `role` parameter doesn't convey the element's \
                purpose to screen readers. Specify `role = Role.Button` or the appropriate role.

                WCAG 4.1.2 Name, Role, Value (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 7,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeTextScanDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val HARDCODED_COLOR = Issue.create(
            id = "A11yHardcodedColor",
            briefDescription = "Hardcoded color value",
            explanation = """
                Hardcoded colors (Color.Black, Color(0xFF...) etc.) won't adapt to dark \
                mode or high-contrast themes. Use `MaterialTheme.colorScheme` tokens instead.

                WCAG 1.4.3 Contrast (Minimum) (Level AA)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeTextScanDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val VISUALLY_DISABLED_NOT_SEMANTICALLY = Issue.create(
            id = "A11yVisuallyDisabledNotSemantically",
            briefDescription = "Visually disabled but not semantically",
            explanation = """
                Element is visually dimmed with `.alpha()` but may still be interactive. \
                Add `enabled = false` or use semantic properties to mark it as disabled.

                WCAG 4.1.2 Name, Role, Value (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeTextScanDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val GENERIC_LINK_TEXT = Issue.create(
            id = "A11yGenericLinkText",
            briefDescription = "Generic link text",
            explanation = """
                Link text like "click here" or "learn more" is not meaningful out of context. \
                Screen reader users often navigate by links — use descriptive text.

                WCAG 2.4.4 Link Purpose (In Context) (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeTextScanDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val REDUCE_MOTION = Issue.create(
            id = "A11yReduceMotion",
            briefDescription = "Animation without reduced motion check",
            explanation = """
                Animations should respect the user's reduced motion preference. Check \
                `AccessibilityManager.isReduceMotionEnabled` and provide alternatives.

                WCAG 2.3.1 Three Flashes or Below Threshold (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 5,
            severity = Severity.INFORMATIONAL,
            implementation = Implementation(
                ComposeTextScanDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val GESTURE_MISSING_ALTERNATIVE = Issue.create(
            id = "A11yGestureMissingAlternative",
            briefDescription = "Gesture without alternative input",
            explanation = """
                Custom gestures (drag, long press, swipe) need an alternative input method \
                for users who cannot perform them. Provide buttons or accessibility actions.

                WCAG 2.1.1 Keyboard (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeTextScanDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val TIMING_ADJUSTABLE = Issue.create(
            id = "A11yTimingAdjustable",
            briefDescription = "Hardcoded timing duration",
            explanation = """
                Hardcoded timing (delay, timeout) may not give users with disabilities \
                enough time. Consider making durations adjustable or extending on interaction.

                WCAG 2.2.1 Timing Adjustable (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 5,
            severity = Severity.INFORMATIONAL,
            implementation = Implementation(
                ComposeTextScanDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val DIALOG_FOCUS_MANAGEMENT = Issue.create(
            id = "A11yDialogFocusManagement",
            briefDescription = "Dialog without focus management",
            explanation = """
                Dialogs and bottom sheets should manage focus explicitly using \
                `FocusRequester` to ensure keyboard and screen reader users land on \
                the correct element when the dialog opens.

                WCAG 2.4.3 Focus Order (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeTextScanDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}
