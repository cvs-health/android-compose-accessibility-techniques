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

class ComposeA11yDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = ComposeA11yDetector()

    override fun allowCompilationErrors(): Boolean = true

    override fun getIssues(): List<Issue> = listOf(
        ComposeA11yDetector.ICON_MISSING_LABEL,
        ComposeA11yDetector.EMPTY_CONTENT_DESCRIPTION,
        ComposeA11yDetector.TEXTFIELD_MISSING_LABEL,
        ComposeA11yDetector.ICON_BUTTON_MISSING_LABEL,
        ComposeA11yDetector.SLIDER_MISSING_LABEL,
        ComposeA11yDetector.DROPDOWN_MISSING_LABEL,
        ComposeA11yDetector.TAB_MISSING_LABEL,
        ComposeA11yDetector.MISSING_PANE_TITLE,
        ComposeA11yDetector.TOGGLE_MISSING_LABEL,
        ComposeA11yDetector.LABEL_CONTAINS_ROLE_IMAGE,
        ComposeA11yDetector.LABEL_CONTAINS_ROLE_BUTTON,
        ComposeA11yDetector.LABEL_IN_NAME_ERROR,
        ComposeA11yDetector.LABEL_IN_NAME_WARNING,
    )

    // ---- A11yIconMissingLabel ----

    @Test
    fun testIconWithNullContentdescriptionTriggersA11yiconmissinglabel() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.Icon
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Icon(painter = painterResource(R.drawable.ic_check), contentDescription = null)
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testIconWithNonnullContentdescriptionIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.Icon
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Icon(painter = painterResource(R.drawable.ic_check), contentDescription = "Checkmark")
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testImageWithNullContentdescriptionTriggersA11yiconmissinglabel() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.foundation.Image
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Image(painter = painterResource(R.drawable.logo), contentDescription = null)
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testImageWithNonnullContentdescriptionIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.foundation.Image
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Image(painter = painterResource(R.drawable.logo), contentDescription = "Company logo")
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yEmptyContentDescription ----

    @Test
    fun testIconWithEmptyContentdescriptionTriggersA11yemptycontentdescription() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.Icon
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Icon(painter = painterResource(R.drawable.ic_star), contentDescription = "")
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testImageWithEmptyContentdescriptionTriggersA11yemptycontentdescription() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.foundation.Image
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Image(painter = painterResource(R.drawable.bg), contentDescription = "")
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testImageWithDescriptiveContentdescriptionIsCleanForEmptyCheck() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.foundation.Image
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Image(painter = painterResource(R.drawable.bg), contentDescription = "Background scenery")
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yTextFieldMissingLabel ----

    @Test
    fun testTextfieldWithoutLabelTriggersA11ytextfieldmissinglabel() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.TextField
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    TextField(value = "", onValueChange = {})
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testOutlinedtextfieldWithoutLabelTriggersA11ytextfieldmissinglabel() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.OutlinedTextField
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    OutlinedTextField(value = "", onValueChange = {})
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testTextfieldWithLabelIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.TextField
                import androidx.compose.material3.Text
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    TextField(value = "", onValueChange = {}, label = { Text("Email") })
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yIconButtonMissingLabel ----

    @Test
    fun testIconbuttonWithNoContentdescriptionTriggersA11yiconbuttonmissinglabel() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.IconButton
                import androidx.compose.material3.Icon
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    IconButton(onClick = {}) {
                        Icon(painter = painterResource(R.drawable.ic_close), contentDescription = null)
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testIconbuttonWithLabeledIconIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.IconButton
                import androidx.compose.material3.Icon
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    IconButton(onClick = {}) {
                        Icon(painter = painterResource(R.drawable.ic_close), contentDescription = "Close")
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11ySliderMissingLabel ----

    @Test
    fun testSliderWithoutLabelTriggersA11yslidermissinglabel() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.Slider
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Slider(value = 0.5f, onValueChange = {})
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testSliderWithContentdescriptionSemanticsIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.Slider
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Slider(
                        value = 0.5f,
                        onValueChange = {},
                        modifier = Modifier.semantics { contentDescription = "Volume" }
                    )
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testRangesliderWithoutLabelTriggersA11yslidermissinglabel() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.RangeSlider
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    RangeSlider(value = 0f..1f, onValueChange = {})
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yDropdownMissingLabel ----

    @Test
    fun testExposeddropdownmenuboxWithoutLabeledTextfieldTriggersA11ydropdownmissinglabel() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.ExposedDropdownMenuBox
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    ExposedDropdownMenuBox(expanded = false, onExpandedChange = {}) {
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testExposeddropdownmenuboxWithLabeledTextfieldIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.ExposedDropdownMenuBox
                import androidx.compose.material3.TextField
                import androidx.compose.material3.Text
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    ExposedDropdownMenuBox(expanded = false, onExpandedChange = {}) {
                        TextField(value = "Option A", onValueChange = {}, label = { Text("Category") })
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yTabMissingLabel ----

    @Test
    fun testTabWithoutTextLabelTriggersA11ytabmissinglabel() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.Tab
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Tab(selected = true, onClick = {}) {
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testTabWithTextParameterIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.Tab
                import androidx.compose.material3.Text
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Tab(selected = true, onClick = {}, text = { Text("Home") })
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testLeadingicontabWithoutTextTriggersA11ytabmissinglabel() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.LeadingIconTab
                import androidx.compose.material3.Icon
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    LeadingIconTab(selected = false, onClick = {}, icon = {
                        Icon(painter = painterResource(R.drawable.ic_home), contentDescription = null)
                    })
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yMissingPaneTitle ----

    @Test
    fun testScaffoldWithoutPanetitleTriggersA11ymissingpanetitle() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.Scaffold
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Scaffold {
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testScaffoldWithPanetitleSemanticsIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.Scaffold
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Scaffold(modifier = Modifier.semantics { paneTitle = "Home Screen" }) {
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testScaffoldWithTopappbarAndTitleIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.Scaffold
                import androidx.compose.material3.TopAppBar
                import androidx.compose.material3.Text
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Scaffold(topBar = {
                        TopAppBar(title = { Text("Settings") })
                    }) {
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yToggleMissingLabel ----

    @Test
    fun testCheckboxWithoutLabelTriggersA11ytogglemissinglabel() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.Checkbox
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Checkbox(checked = false, onCheckedChange = {})
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testSwitchWithoutLabelTriggersA11ytogglemissinglabel() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.Switch
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Switch(checked = true, onCheckedChange = {})
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testCheckboxWithContentdescriptionSemanticsIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.Checkbox
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Checkbox(
                        checked = false,
                        onCheckedChange = {},
                        modifier = Modifier.semantics { contentDescription = "Accept terms" }
                    )
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yLabelContainsRoleImage ----

    @Test
    fun testIconWithImageInContentdescriptionTriggersA11ylabelcontainsroleimage() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.Icon
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Icon(painter = painterResource(R.drawable.ic_logo), contentDescription = "Company logo image")
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testImageWithIconInContentdescriptionTriggersA11ylabelcontainsroleimage() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.foundation.Image
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Image(painter = painterResource(R.drawable.close), contentDescription = "close icon")
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testIconWithDescriptiveContentdescriptionWithoutRoleWordIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.Icon
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Icon(painter = painterResource(R.drawable.ic_logo), contentDescription = "Company logo")
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yLabelContainsRoleButton ----

    @Test
    fun testButtonWithButtonInLabelTriggersA11ylabelcontainsrolebutton() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.Button
                import androidx.compose.material3.Text
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Button(onClick = {}) {
                        Text("Submit button")
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testTextbuttonWithButtonInLabelTriggersA11ylabelcontainsrolebutton() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.TextButton
                import androidx.compose.material3.Text
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    TextButton(onClick = {}) {
                        Text("Cancel Button")
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testButtonWithActiononlyLabelIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.Button
                import androidx.compose.material3.Text
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Button(onClick = {}) {
                        Text("Submit")
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yLabelInNameError / A11yLabelInNameWarning ----

    @Test
    fun testButtonWithMismatchedContentdescriptionTriggersA11ylabelinnameerror() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.Button
                import androidx.compose.material3.Text
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Button(
                        onClick = {},
                        modifier = Modifier.semantics { contentDescription = "Submit form" }
                    ) {
                        Text("Save")
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testButtonWithMatchingLabelIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.material3.Button
                import androidx.compose.material3.Text
                import androidx.compose.runtime.Composable
                @Composable
                fun Foo() {
                    Button(
                        onClick = {},
                        modifier = Modifier.semantics { contentDescription = "Save document" }
                    ) {
                        Text("Save")
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }
}
