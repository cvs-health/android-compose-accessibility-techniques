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
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

class ComposeFormDetector : Detector(), SourceCodeScanner {

    override fun getApplicableMethodNames() = listOf(
        "TextField", "OutlinedTextField", "BasicTextField",
        "RadioButton",
    )

    override fun visitMethodCall(
        context: JavaContext,
        node: UCallExpression,
        method: PsiMethod
    ) {
        when (node.methodName) {
            "TextField", "OutlinedTextField", "BasicTextField" -> checkInputPurpose(context, node)
            "RadioButton" -> checkRadioGroupMissing(context, node)
        }
    }

    private fun checkInputPurpose(context: JavaContext, node: UCallExpression) {
        val sourceText = node.asSourceString()

        // Check if keyboard type suggests a specific input purpose
        val hasTypedKeyboard = sourceText.contains("KeyboardType.Email") ||
            sourceText.contains("KeyboardType.Phone") ||
            sourceText.contains("KeyboardType.Password") ||
            sourceText.contains("KeyboardType.Number")
        if (!hasTypedKeyboard) return

        // Check if autofill or content type semantics are set
        if (sourceText.contains("autofill") || sourceText.contains("contentType") ||
            sourceText.contains("AutofillType") || sourceText.contains("semantics")) return

        context.report(
            INPUT_PURPOSE,
            node,
            context.getNameLocation(node),
            "${node.methodName} specifies a keyboard type but no autofill/content type. " +
                "Add autofill hints so the system can assist users with input."
        )
    }

    private fun checkRadioGroupMissing(context: JavaContext, node: UCallExpression) {
        // Walk parent chain looking for selectableGroup
        var parent = node.uastParent
        var depth = 0
        while (parent != null && depth < 8) {
            if (parent is UCallExpression) {
                val parentName = parent.methodName
                if (parentName in listOf("Column", "Row")) {
                    val parentSource = parent.asSourceString()
                    if (parentSource.contains("selectableGroup")) return
                }
            }
            parent = parent.uastParent
            depth++
        }

        context.report(
            RADIO_GROUP_MISSING,
            node,
            context.getNameLocation(node),
            "RadioButton is not inside a Column/Row with `selectableGroup()`. " +
                "Screen readers need group semantics to announce radio button position."
        )
    }

    companion object {
        val INPUT_PURPOSE = Issue.create(
            id = "A11yInputPurpose",
            briefDescription = "Input field missing autofill purpose",
            explanation = """
                TextField with a specific keyboard type (Email, Phone, Password) should \
                also declare autofill hints or content type semantics so the system can \
                assist users with input.

                WCAG 1.3.5 Identify Input Purpose (Level AA)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 5,
            severity = Severity.INFORMATIONAL,
            implementation = Implementation(
                ComposeFormDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val RADIO_GROUP_MISSING = Issue.create(
            id = "A11yRadioGroupMissing",
            briefDescription = "RadioButton not in selectable group",
            explanation = """
                RadioButton should be inside a Column or Row with \
                `Modifier.selectableGroup()` so screen readers can announce \
                the radio button's position (e.g., "1 of 3").

                WCAG 1.3.1 Info and Relationships (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeFormDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}
