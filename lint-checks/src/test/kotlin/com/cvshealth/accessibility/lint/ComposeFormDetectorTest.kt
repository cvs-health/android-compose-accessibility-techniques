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
import com.android.tools.lint.checks.infrastructure.TestMode
import org.junit.Test

/**
 * These tests deliberately do not call `allowCompilationErrors()`. This detector declares
 * [getApplicableMethodNames] and overrides `visitMethodCall(context, node, method: PsiMethod)`,
 * which lint only dispatches once the call resolves to a method. Sample code that does not
 * compile never resolves, so the detector is never invoked and every assertion would pass
 * vacuously. [ComposeStubs] supplies the API surface that makes resolution possible.
 */
class ComposeFormDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = ComposeFormDetector()

    override fun getIssues(): List<Issue> = listOf(
        ComposeFormDetector.INPUT_PURPOSE,
        ComposeFormDetector.RADIO_GROUP_MISSING,
    )

    // ---- A11yInputPurpose ----

    @Test
    fun testTextfieldWithEmailKeyboardTypeAndNoAutofillTriggersA11yinputpurpose() {
        lint().files(
            *ComposeStubs.all,
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.TextField
                import androidx.compose.foundation.text.KeyboardOptions
                import androidx.compose.ui.text.input.KeyboardType
                @Composable
                fun Foo() {
                    TextField(
                        value = "",
                        onValueChange = {},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).run().expectContains("A11yInputPurpose")
    }

    @Test
    fun testTextfieldWithPhoneKeyboardTypeAndNoAutofillTriggersA11yinputpurpose() {
        lint().files(
            *ComposeStubs.all,
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.TextField
                import androidx.compose.foundation.text.KeyboardOptions
                import androidx.compose.ui.text.input.KeyboardType
                @Composable
                fun Foo() {
                    TextField(
                        value = "",
                        onValueChange = {},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).run().expectContains("A11yInputPurpose")
    }

    @Test
    fun testOutlinedtextfieldWithPasswordKeyboardTypeAndNoAutofillTriggersA11yinputpurpose() {
        lint().files(
            *ComposeStubs.all,
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.OutlinedTextField
                import androidx.compose.foundation.text.KeyboardOptions
                import androidx.compose.ui.text.input.KeyboardType
                @Composable
                fun Foo() {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).run().expectContains("A11yInputPurpose")
    }

    @Test
    fun testTextfieldWithEmailKeyboardTypeAndAutofillHintIsClean() {
        lint().files(
            *ComposeStubs.all,
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.TextField
                import androidx.compose.foundation.text.KeyboardOptions
                import androidx.compose.ui.text.input.KeyboardType
                import androidx.compose.ui.Modifier
                import androidx.compose.ui.semantics.ContentType
                import androidx.compose.ui.semantics.semantics
                @Composable
                fun Foo() {
                    TextField(
                        value = "",
                        onValueChange = {},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.semantics {
                            contentType = ContentType.EmailAddress
                        }
                    )
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).run().expectClean()
    }

    @Test
    fun testTextfieldWithTextKeyboardTypeNoSpecificPurposeIsClean() {
        lint().files(
            *ComposeStubs.all,
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.Text
                import androidx.compose.material3.TextField
                @Composable
                fun Foo() {
                    TextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Name") }
                    )
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).run().expectClean()
    }

    @Test
    fun testTextfieldWithNumberKeyboardTypeAndAutofillIsClean() {
        lint().files(
            *ComposeStubs.all,
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.TextField
                import androidx.compose.foundation.text.KeyboardOptions
                import androidx.compose.ui.text.input.KeyboardType
                import androidx.compose.ui.Modifier
                import androidx.compose.ui.semantics.semantics
                @Composable
                fun Foo() {
                    TextField(
                        value = "",
                        onValueChange = {},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.semantics { autofill = true }
                    )
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).run().expectClean()
    }

    // ---- A11yRadioGroupMissing ----

    @Test
    fun testRadiobuttonOutsideSelectablegroupColumnTriggersA11yradiogroupmissing() {
        lint().files(
            *ComposeStubs.all,
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.RadioButton
                import androidx.compose.foundation.layout.Column
                @Composable
                fun Foo() {
                    Column {
                        RadioButton(selected = true, onClick = {})
                        RadioButton(selected = false, onClick = {})
                    }
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).run().expectContains("A11yRadioGroupMissing")
    }

    @Test
    fun testRadiobuttonWithoutAnyWrappingGroupTriggersA11yradiogroupmissing() {
        lint().files(
            *ComposeStubs.all,
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.RadioButton
                @Composable
                fun Foo() {
                    RadioButton(selected = false, onClick = {})
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).run().expectContains("A11yRadioGroupMissing")
    }

    @Test
    fun testRadiobuttonInColumnWithSelectablegroupIsClean() {
        lint().files(
            *ComposeStubs.all,
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.RadioButton
                import androidx.compose.foundation.layout.Column
                import androidx.compose.foundation.selection.selectableGroup
                import androidx.compose.ui.Modifier
                @Composable
                fun Foo() {
                    Column(modifier = Modifier.selectableGroup()) {
                        RadioButton(selected = true, onClick = {})
                        RadioButton(selected = false, onClick = {})
                        RadioButton(selected = false, onClick = {})
                    }
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).run().expectClean()
    }

    @Test
    fun testRadiobuttonInRowWithSelectablegroupIsClean() {
        lint().files(
            *ComposeStubs.all,
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.RadioButton
                import androidx.compose.foundation.layout.Row
                import androidx.compose.foundation.selection.selectableGroup
                import androidx.compose.ui.Modifier
                @Composable
                fun Foo() {
                    Row(modifier = Modifier.selectableGroup()) {
                        RadioButton(selected = true, onClick = {})
                        RadioButton(selected = false, onClick = {})
                    }
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).run().expectClean()
    }

    /**
     * Regression test for the container-type gate. `selectableGroup()` on a `LazyRow` is a real
     * and correct way to build a radio group, and it is the shape used by the two byte-identical
     * radio groups in the CVS flagship Android app (`ShopRadioButtonGroup.kt:52` and
     * `SubmitReviewRadioButtonGroup.kt:77`). An earlier version of this rule only honoured
     * `selectableGroup()` when it sat on a `Column` or `Row`, so both of those were reported.
     */
    @Test
    fun testRadiobuttonInLazyRowWithSelectablegroupIsClean() {
        lint().files(
            *ComposeStubs.all,
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.RadioButton
                import androidx.compose.foundation.lazy.LazyRow
                import androidx.compose.foundation.selection.selectableGroup
                import androidx.compose.ui.Modifier
                @Composable
                fun Foo(options: List<String>) {
                    LazyRow(modifier = Modifier.selectableGroup()) {
                        items(options) { option ->
                            RadioButton(selected = false, onClick = {})
                        }
                    }
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).run().expectClean()
    }
}
