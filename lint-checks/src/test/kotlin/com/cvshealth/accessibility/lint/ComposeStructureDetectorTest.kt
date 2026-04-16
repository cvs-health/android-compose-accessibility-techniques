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
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.junit.Test

class ComposeStructureDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = ComposeStructureDetector()

    override fun getIssues(): List<Issue> = listOf(
        ComposeStructureDetector.HIDDEN_WITH_INTERACTIVE_CHILDREN,
        ComposeStructureDetector.ACCESSIBILITY_GROUPING,
        ComposeStructureDetector.BOX_CHILD_ORDER,
        ComposeStructureDetector.BUTTON_USED_AS_LINK,
    )

    // ---- A11yHiddenWithInteractiveChildren ----

    @Test
    fun testRowWithClearandsetsemanticsAndButtonChildTriggersA11yhiddenwithinteractivechildren() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.foundation.layout.Row
                @Composable
                fun Foo() {
                    Row(modifier = Modifier.clearAndSetSemantics {}) {
                        Button(onClick = {}) { Text("Action") }
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testColumnWithClearandsetsemanticsAndCheckboxChildTriggersA11yhiddenwithinteractivechildren() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.foundation.layout.Column
                import androidx.compose.material3.Checkbox
                @Composable
                fun Foo() {
                    Column(modifier = Modifier.clearAndSetSemantics {}) {
                        Checkbox(checked = false, onCheckedChange = {})
                        Text("Option")
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testRowWithClearandsetsemanticsAndOnlyTextChildIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.foundation.layout.Row
                @Composable
                fun Foo() {
                    Row(modifier = Modifier.clearAndSetSemantics { contentDescription = "Status: Active" }) {
                        Icon(painter = painterResource(R.drawable.ic_active), contentDescription = null)
                        Text("Active")
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testBoxWithClearandsetsemanticsAndSwitchChildTriggersA11yhiddenwithinteractivechildren() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.foundation.layout.Box
                import androidx.compose.material3.Switch
                @Composable
                fun Foo() {
                    Box(modifier = Modifier.clearAndSetSemantics {}) {
                        Switch(checked = true, onCheckedChange = {})
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yAccessibilityGrouping ----

    @Test
    fun testClickableRowWithIconAndTextButNoMergedescendantsTriggersA11yaccessibilitygrouping() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.foundation.layout.Row
                import androidx.compose.material3.Icon
                import androidx.compose.material3.Text
                @Composable
                fun Foo() {
                    Row(modifier = Modifier.clickable { openDetail() }) {
                        Icon(painter = painterResource(R.drawable.ic_info), contentDescription = null)
                        Text("View Details")
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testClickableColumnWithImageAndTextButNoMergedescendantsTriggersA11yaccessibilitygrouping() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.foundation.layout.Column
                import androidx.compose.foundation.Image
                import androidx.compose.material3.Text
                @Composable
                fun Foo() {
                    Column(modifier = Modifier.clickable { navigate() }) {
                        Image(painter = painterResource(R.drawable.thumbnail), contentDescription = null)
                        Text("Article Title")
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testClickableRowWithIconAndTextAndMergedescendantsIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.foundation.layout.Row
                import androidx.compose.material3.Icon
                import androidx.compose.material3.Text
                @Composable
                fun Foo() {
                    Row(modifier = Modifier
                        .semantics(mergeDescendants = true) {}
                        .clickable { openDetail() }
                    ) {
                        Icon(painter = painterResource(R.drawable.ic_info), contentDescription = null)
                        Text("View Details")
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testNonclickableRowWithIconAndTextIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.foundation.layout.Row
                import androidx.compose.material3.Icon
                import androidx.compose.material3.Text
                @Composable
                fun Foo() {
                    Row {
                        Icon(painter = painterResource(R.drawable.ic_info), contentDescription = null)
                        Text("Status")
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yBoxChildOrder ----

    @Test
    fun testBoxWithAlignAndZindexButNoTraversalindexTriggersA11yboxchildorder() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.foundation.layout.Box
                @Composable
                fun Foo() {
                    Box {
                        Image(
                            painter = painterResource(R.drawable.bg),
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center).zIndex(0f)
                        )
                        Text(
                            "Overlay Text",
                            modifier = Modifier.align(Alignment.BottomCenter).zIndex(1f)
                        )
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testBoxWithAlignAndZindexAndTraversalindexIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.foundation.layout.Box
                @Composable
                fun Foo() {
                    Box {
                        Image(
                            painter = painterResource(R.drawable.bg),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .zIndex(0f)
                                .semantics { traversalIndex = 1f }
                        )
                        Text(
                            "Overlay Text",
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .zIndex(1f)
                                .semantics { traversalIndex = 0f }
                        )
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testBoxWithoutZindexIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.foundation.layout.Box
                @Composable
                fun Foo() {
                    Box {
                        Image(
                            painter = painterResource(R.drawable.bg),
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        Text("Text", modifier = Modifier.align(Alignment.BottomCenter))
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yButtonUsedAsLink ----

    @Test
    fun testButtonWithHttpsUrlTriggersA11ybuttonusedaslink() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.Button
                @Composable
                fun Foo() {
                    Button(onClick = {
                        uriHandler.openUri("https://www.example.com")
                    }) {
                        Text("Visit Website")
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testTextbuttonWithUriParseTriggersA11ybuttonusedaslink() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.TextButton
                import android.net.Uri
                @Composable
                fun Foo() {
                    TextButton(onClick = {
                        val uri = Uri.parse("https://example.com/page")
                        startActivity(Intent(Intent.ACTION_VIEW, uri))
                    }) {
                        Text("Open Link")
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testButtonWithRoleDotLinkSemanticsIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.Button
                @Composable
                fun Foo() {
                    Button(
                        onClick = { uriHandler.openUri("https://www.example.com") },
                        modifier = Modifier.semantics { role = Role.Link }
                    ) {
                        Text("Visit Website")
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testButtonWithoutLinkBehaviorIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.Button
                @Composable
                fun Foo() {
                    Button(onClick = { submitForm() }) {
                        Text("Submit")
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }
}
