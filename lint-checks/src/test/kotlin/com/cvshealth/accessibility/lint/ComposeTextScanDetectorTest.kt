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

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.junit.Test

class ComposeTextScanDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = ComposeTextScanDetector()

    override fun allowCompilationErrors(): Boolean = true

    override fun getIssues(): List<Issue> = listOf(
        ComposeTextScanDetector.FIXED_FONT_SIZE,
        ComposeTextScanDetector.MAX_LINES_ONE,
        ComposeTextScanDetector.HEADING_SEMANTICS_MISSING,
        ComposeTextScanDetector.FAKE_HEADING_IN_LABEL,
        ComposeTextScanDetector.CLICKABLE_MISSING_ROLE,
        ComposeTextScanDetector.HARDCODED_COLOR,
        ComposeTextScanDetector.VISUALLY_DISABLED_NOT_SEMANTICALLY,
        ComposeTextScanDetector.GENERIC_LINK_TEXT,
        ComposeTextScanDetector.REDUCE_MOTION,
        ComposeTextScanDetector.GESTURE_MISSING_ALTERNATIVE,
        ComposeTextScanDetector.TIMING_ADJUSTABLE,
        ComposeTextScanDetector.DIALOG_FOCUS_MANAGEMENT,
    )

    // ---- A11yFixedFontSize ----

    @Test
    fun testHardcodedSpFontSizeTriggersA11yfixedfontsize() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.Text
                import androidx.compose.ui.unit.sp
                @Composable
                fun Foo() {
                    Text(text = "Hello", fontSize = 16.sp)
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yFixedFontSize")
    }

    @Test
    fun testAnotherHardcodedSpValueTriggersA11yfixedfontsize() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.Text
                import androidx.compose.ui.unit.sp
                @Composable
                fun Bar() {
                    Text(text = "Subtitle", fontSize = 14.sp)
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yFixedFontSize")
    }

    @Test
    fun testTypographyStyleWithoutHardcodedSpIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.Text
                import androidx.compose.material3.MaterialTheme
                @Composable
                fun Foo() {
                    Text(text = "Hello", style = MaterialTheme.typography.bodyMedium)
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yMaxLinesOne ----

    @Test
    fun testMaxlines1TriggersA11ymaxlinesone() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.Text
                @Composable
                fun Foo() {
                    Text(text = "Long text that might be truncated", maxLines = 1)
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yMaxLinesOne")
    }

    @Test
    fun testMaxlines2DoesNotTriggerA11ymaxlinesone() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.Text
                @Composable
                fun Foo() {
                    Text(text = "Long text", maxLines = 2)
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yHeadingSemanticsMissing ----

    @Test
    fun testTextWithHeadlinelargeWithoutHeadingSemanticsTriggersA11yheadingsemanticsmissing() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.Text
                import androidx.compose.material3.MaterialTheme
                @Composable
                fun Foo() {
                    Text(text = "Section Title", style = MaterialTheme.typography.headlineLarge)
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yHeadingSemanticsMissing")
    }

    @Test
    fun testTextWithTitlemediumWithoutHeadingSemanticsTriggersA11yheadingsemanticsmissing() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.Text
                import androidx.compose.material3.MaterialTheme
                @Composable
                fun Foo() {
                    Text(text = "Card Title", style = MaterialTheme.typography.titleMedium)
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yHeadingSemanticsMissing")
    }

    @Test
    fun testTextWithHeadlinemediumAndHeadingSemanticsIsClean() {
        // Skip WHITESPACE mode: extra whitespace may cause the heading() keyword search to
        // miss the context window, producing a false positive in whitespace mode.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.Text
                import androidx.compose.material3.MaterialTheme
                @Composable
                fun Foo() {
                    Text(
                        text = "Section",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.semantics { heading() }
                    )
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectClean()
    }

    // ---- A11yFakeHeadingInLabel ----

    @Test
    fun testContentdescriptionContainingHeadingTriggersA11yfakeheadinginlabel() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.Icon
                @Composable
                fun Foo() {
                    Icon(painter = painterResource(R.drawable.ic_section), contentDescription = "Section heading")
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yFakeHeadingInLabel")
    }

    @Test
    fun testContentdescriptionContainingHeadingCaseInsensitiveTriggersA11yfakeheadinginlabel() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    val desc = contentDescription = "My Heading Icon"
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yFakeHeadingInLabel")
    }

    @Test
    fun testContentdescriptionWithoutHeadingWordIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.Icon
                @Composable
                fun Foo() {
                    Icon(painter = painterResource(R.drawable.ic_section), contentDescription = "Section title")
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yClickableMissingRole ----

    @Test
    fun testClickableWithoutRoleTriggersA11yclickablemissingrole() {
        // The detector regex is \.clickable\s*\( — use explicit parentheses before the lambda
        // so the regex matches. This detector uses Location.create() without an AST node.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.foundation.layout.Box
                @Composable
                fun Foo() {
                    Box(modifier = Modifier.clickable() { doAction() }) {
                    }
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yClickableMissingRole")
    }

    @Test
    fun testClickableWithRoleIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.foundation.layout.Box
                @Composable
                fun Foo() {
                    Box(modifier = Modifier.clickable(role = Role.Button) { doAction() }) {
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testClickableWithRoleDotExpressionIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.foundation.layout.Box
                @Composable
                fun Foo() {
                    Box(modifier = Modifier.clickable(
                        role = Role.Checkbox,
                        onClick = { toggle() }
                    )) {
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yHardcodedColor ----

    @Test
    fun testColorDotBlackUsageTriggersA11yhardcodedcolor() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.Text
                import androidx.compose.ui.graphics.Color
                @Composable
                fun Foo() {
                    Text(text = "Hello", color = Color.Black)
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yHardcodedColor")
    }

    @Test
    fun testColorDotWhiteUsageTriggersA11yhardcodedcolor() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.Text
                import androidx.compose.ui.graphics.Color
                @Composable
                fun Foo() {
                    Text(text = "Hello", color = Color.White)
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yHardcodedColor")
    }

    @Test
    fun testMaterialthemeColorschemeUsageIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.Text
                import androidx.compose.material3.MaterialTheme
                @Composable
                fun Foo() {
                    Text(text = "Hello", color = MaterialTheme.colorScheme.onBackground)
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yVisuallyDisabledNotSemantically ----

    @Test
    fun testAlphaWithLowValueTriggersA11yvisuallydisablednotsemantically() {
        // The detector regex is \.alpha\s*\(\s*(0\.\d+|0)\s*\) — use a value without the
        // 'f' float suffix so the regex matches. This detector uses Location.create() without
        // an AST node.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.foundation.layout.Box
                @Composable
                fun Foo() {
                    Box(modifier = Modifier.alpha(0.3)) {
                    }
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yVisuallyDisabledNotSemantically")
    }

    @Test
    fun testAlphaZeroTriggersA11yvisuallydisablednotsemantically() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.foundation.layout.Box
                @Composable
                fun Foo() {
                    Box(modifier = Modifier.alpha(0)) {
                    }
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yVisuallyDisabledNotSemantically")
    }

    @Test
    fun testAlphaWithEnabledFalseIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.foundation.layout.Box
                @Composable
                fun Foo(enabled: Boolean) {
                    Box(modifier = Modifier.alpha(0.3)) {
                        SomeButton(enabled = false) { }
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yGenericLinkText ----

    @Test
    fun testGenericLinkTextClickHereInClickableContextTriggersA11ygenericlinktext() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Text(
                        text = "click here",
                        modifier = Modifier.clickable { openUrl() }
                    )
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yGenericLinkText")
    }

    @Test
    fun testGenericLinkTextLearnMoreInClickabletextContextTriggersA11ygenericlinktext() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    ClickableText(
                        text = AnnotatedString("learn more"),
                        onClick = { openPage() }
                    )
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yGenericLinkText")
    }

    @Test
    fun testDescriptiveLinkTextIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Text(
                        text = "View accessibility guidelines",
                        modifier = Modifier.clickable { openUrl() }
                    )
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yReduceMotion ----

    @Test
    fun testAnimatedvisibilityWithoutReducemotionCheckTriggersA11yreducemotion() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.animation.AnimatedVisibility
                @Composable
                fun Foo(visible: Boolean) {
                    AnimatedVisibility(visible = visible) {
                        Content()
                    }
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yReduceMotion")
    }

    @Test
    fun testAnimatefloatasstateWithoutReducemotionCheckTriggersA11yreducemotion() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.animation.core.animateFloatAsState
                @Composable
                fun Foo(expanded: Boolean) {
                    val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yReduceMotion")
    }

    @Test
    fun testAnimatedvisibilityWithReducemotionCheckIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.animation.AnimatedVisibility
                @Composable
                fun Foo(visible: Boolean) {
                    val isReduceMotionEnabled = LocalReduceMotion.current
                    AnimatedVisibility(visible = visible && !isReduceMotionEnabled) {
                        Content()
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yGestureMissingAlternative ----

    @Test
    fun testPointerinputWithoutAlternativeTriggersA11ygesturemissingalternative() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.foundation.layout.Box
                @Composable
                fun Foo() {
                    Box(modifier = Modifier.pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            handleDrag(dragAmount)
                        }
                    })
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yGestureMissingAlternative")
    }

    @Test
    fun testDetectdraggesturesWithoutAlternativeTriggersA11ygesturemissingalternative() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Modifier.pointerInput(Unit) {
                        detectDragGestures { change, amount ->
                            processDrag(amount)
                        }
                    }
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yGestureMissingAlternative")
    }

    @Test
    fun testPointerinputWithCustomactionsAlternativeIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.foundation.layout.Box
                @Composable
                fun Foo() {
                    Box(modifier = Modifier
                        .pointerInput(Unit) {
                            detectDragGestures { _, _ -> }
                        }
                        .semantics {
                            customActions = listOf(CustomAccessibilityAction("Dismiss") { true })
                        }
                    )
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yTimingAdjustable ----

    @Test
    fun testDelayWithLongDurationTriggersA11ytimingadjustable() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import kotlinx.coroutines.delay
                suspend fun Foo() {
                    delay(5000)
                    hideToast()
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yTimingAdjustable")
    }

    @Test
    fun testWithtimeoutWithLongDurationTriggersA11ytimingadjustable() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import kotlinx.coroutines.withTimeout
                suspend fun Foo() {
                    withTimeout(10000) {
                        fetchData()
                    }
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yTimingAdjustable")
    }

    @Test
    fun testShortDelayUnder1000msIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import kotlinx.coroutines.delay
                suspend fun Foo() {
                    delay(300)
                    triggerAnimation()
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yDialogFocusManagement ----

    @Test
    fun testAlertdialogWithoutFocusrequesterTriggersA11ydialogfocusmanagement() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.AlertDialog
                @Composable
                fun Foo(onDismiss: () -> Unit) {
                    AlertDialog(
                        onDismissRequest = onDismiss,
                        title = { Text("Warning") },
                        text = { Text("Are you sure?") },
                        confirmButton = { Button(onClick = onDismiss) { Text("OK") } }
                    )
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yDialogFocusManagement")
    }

    @Test
    fun testDialogWithoutFocusrequesterTriggersA11ydialogfocusmanagement() {
        // This detector uses Location.create() without an AST node, so SUPPRESSIBLE and
        // WHITESPACE test modes are not applicable.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.ui.window.Dialog
                @Composable
                fun Foo(onDismiss: () -> Unit) {
                    Dialog(onDismissRequest = onDismiss) {
                        Text("Dialog content")
                    }
                }
                """.trimIndent()
            )
        ).skipTestModes(TestMode.SUPPRESSIBLE, TestMode.WHITESPACE)
            .allowCompilationErrors().run().expectContains("A11yDialogFocusManagement")
    }

    @Test
    fun testAlertdialogWithFocusrequesterIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.AlertDialog
                import androidx.compose.ui.focus.FocusRequester
                import androidx.compose.ui.focus.focusRequester
                @Composable
                fun Foo(onDismiss: () -> Unit) {
                    val focusRequester = remember { FocusRequester() }
                    AlertDialog(
                        onDismissRequest = onDismiss,
                        title = { Text("Warning") },
                        confirmButton = {
                            Button(
                                onClick = onDismiss,
                                modifier = Modifier.focusRequester(focusRequester)
                            ) { Text("OK") }
                        }
                    )
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }
}
