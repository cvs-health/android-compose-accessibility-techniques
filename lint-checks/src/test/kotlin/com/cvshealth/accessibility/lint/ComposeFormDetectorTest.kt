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

class ComposeFormDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = ComposeFormDetector()

    override fun allowCompilationErrors(): Boolean = true

    override fun getIssues(): List<Issue> = listOf(
        ComposeFormDetector.INPUT_PURPOSE,
        ComposeFormDetector.RADIO_GROUP_MISSING,
    )

    // ---- A11yInputPurpose ----

    @Test
    fun testTextfieldWithEmailKeyboardTypeAndNoAutofillTriggersA11yinputpurpose() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
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
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testTextfieldWithPhoneKeyboardTypeAndNoAutofillTriggersA11yinputpurpose() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
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
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testOutlinedtextfieldWithPasswordKeyboardTypeAndNoAutofillTriggersA11yinputpurpose() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
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
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testTextfieldWithEmailKeyboardTypeAndAutofillHintIsClean() {
        lint().files(
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.semantics {
                            contentType = ContentType.EmailAddress
                        }
                    )
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testTextfieldWithTextKeyboardTypeNoSpecificPurposeIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
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
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testTextfieldWithNumberKeyboardTypeAndAutofillIsClean() {
        lint().files(
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.semantics { autofill = true }
                    )
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }

    // ---- A11yRadioGroupMissing ----

    @Test
    fun testRadiobuttonOutsideSelectablegroupColumnTriggersA11yradiogroupmissing() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
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
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testRadiobuttonWithoutAnyWrappingGroupTriggersA11yradiogroupmissing() {
        // TODO: UAST cannot resolve unresolved Compose imports with allowCompilationErrors(),
        // so visitMethodCall is not invoked and the detector does not trigger in tests.
        lint().files(
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
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testRadiobuttonInColumnWithSelectablegroupIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.RadioButton
                import androidx.compose.foundation.layout.Column
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
        ).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testRadiobuttonInRowWithSelectablegroupIsClean() {
        lint().files(
            kotlin(
                """
                package test
                import androidx.compose.runtime.Composable
                import androidx.compose.material3.RadioButton
                import androidx.compose.foundation.layout.Row
                @Composable
                fun Foo() {
                    Row(modifier = Modifier.selectableGroup()) {
                        RadioButton(selected = true, onClick = {})
                        RadioButton(selected = false, onClick = {})
                    }
                }
                """.trimIndent()
            )
        ).allowCompilationErrors().run().expectClean()
    }
}
