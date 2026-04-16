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

class ComposeStructureDetector : Detector(), SourceCodeScanner {

    override fun getApplicableMethodNames() = listOf(
        "Row", "Column", "Box",
        "Button", "TextButton", "OutlinedButton", "FilledTonalButton", "ElevatedButton",
    )

    override fun visitMethodCall(
        context: JavaContext,
        node: UCallExpression,
        method: PsiMethod
    ) {
        when (node.methodName) {
            "Row", "Column" -> {
                checkHiddenWithInteractiveChildren(context, node)
                checkAccessibilityGrouping(context, node)
            }
            "Box" -> {
                checkHiddenWithInteractiveChildren(context, node)
                checkBoxChildOrder(context, node)
            }
            "Button", "TextButton", "OutlinedButton", "FilledTonalButton", "ElevatedButton" ->
                checkButtonUsedAsLink(context, node)
        }
    }

    private fun checkHiddenWithInteractiveChildren(context: JavaContext, node: UCallExpression) {
        val sourceText = node.asSourceString()
        if (!sourceText.contains("clearAndSetSemantics")) return

        val interactiveChildren = listOf(
            "Button(", "TextButton(", "IconButton(", "OutlinedButton(",
            "Checkbox(", "Switch(", "RadioButton(", "Slider(",
            ".clickable(", ".toggleable("
        )
        val hasInteractive = interactiveChildren.any { sourceText.contains(it) }
        if (!hasInteractive) return

        context.report(
            HIDDEN_WITH_INTERACTIVE_CHILDREN,
            node,
            context.getNameLocation(node),
            "${node.methodName} uses `clearAndSetSemantics` but contains interactive children. " +
                "Interactive elements inside a cleared semantics scope are hidden from screen readers."
        )
    }

    private fun checkAccessibilityGrouping(context: JavaContext, node: UCallExpression) {
        val sourceText = node.asSourceString()

        // Check if Row/Column contains both Icon and Text (common pattern needing grouping)
        val hasIcon = sourceText.contains("Icon(") || sourceText.contains("Image(")
        val hasText = sourceText.contains("Text(")
        if (!hasIcon || !hasText) return

        // Check if clickable but not merged
        val isClickable = sourceText.contains(".clickable(") || sourceText.contains(".toggleable(")
        if (!isClickable) return

        if (sourceText.contains("mergeDescendants") || sourceText.contains("clearAndSetSemantics")) return

        context.report(
            ACCESSIBILITY_GROUPING,
            node,
            context.getNameLocation(node),
            "Clickable ${node.methodName} with Icon and Text should use `Modifier.semantics(mergeDescendants = true)` " +
                "to group content for screen readers."
        )
    }

    private fun checkBoxChildOrder(context: JavaContext, node: UCallExpression) {
        val sourceText = node.asSourceString()
        if (!sourceText.contains(".align(") || !sourceText.contains(".zIndex(")) return

        if (sourceText.contains("traversalIndex") || sourceText.contains("isTraversalGroup")) return

        context.report(
            BOX_CHILD_ORDER,
            node,
            context.getNameLocation(node),
            "Box with overlapping children (align + zIndex) may have confusing screen reader order. " +
                "Consider using `traversalIndex` or `isTraversalGroup` to control reading order."
        )
    }

    private fun checkButtonUsedAsLink(context: JavaContext, node: UCallExpression) {
        val sourceText = node.asSourceString()
        val linkIndicators = listOf("Uri.parse", "ACTION_VIEW", "https://", "http://", "openUri", "uriHandler")
        val hasLinkBehavior = linkIndicators.any { sourceText.contains(it) }
        if (!hasLinkBehavior) return

        // Check if role is already set to Link
        if (sourceText.contains("Role.Link") || sourceText.contains("UrlAnnotation") ||
            sourceText.contains("LinkAnnotation")) return

        context.report(
            BUTTON_USED_AS_LINK,
            node,
            context.getNameLocation(node),
            "${node.methodName} navigates to a URL but is announced as a button. " +
                "Use a link pattern or add `Role.Link` semantics."
        )
    }

    companion object {
        val HIDDEN_WITH_INTERACTIVE_CHILDREN = Issue.create(
            id = "A11yHiddenWithInteractiveChildren",
            briefDescription = "Interactive children hidden from screen readers",
            explanation = """
                Using `clearAndSetSemantics` on a container hides all children from \
                screen readers. If the container has interactive children (buttons, \
                checkboxes, etc.), they become inaccessible.

                WCAG 4.1.2 Name, Role, Value (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 8,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeStructureDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val ACCESSIBILITY_GROUPING = Issue.create(
            id = "A11yAccessibilityGrouping",
            briefDescription = "Clickable content not grouped for screen readers",
            explanation = """
                A clickable Row/Column containing both Icon and Text should use \
                `Modifier.semantics(mergeDescendants = true)` to group the content \
                into a single screen reader element.

                WCAG 1.3.1 Info and Relationships (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 5,
            severity = Severity.INFORMATIONAL,
            implementation = Implementation(
                ComposeStructureDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val BOX_CHILD_ORDER = Issue.create(
            id = "A11yBoxChildOrder",
            briefDescription = "Box children may have confusing screen reader order",
            explanation = """
                Box with overlapping children using `align` and `zIndex` may present \
                content in an unexpected order to screen readers. Use `traversalIndex` \
                or `isTraversalGroup` to control the reading order.

                WCAG 1.3.2 Meaningful Sequence (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 5,
            severity = Severity.INFORMATIONAL,
            implementation = Implementation(
                ComposeStructureDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val BUTTON_USED_AS_LINK = Issue.create(
            id = "A11yButtonUsedAsLink",
            briefDescription = "Button used for navigation (should be a link)",
            explanation = """
                A Button that navigates to a URL is semantically a link, not a button. \
                Screen reader users expect links and buttons to behave differently. Use \
                a link pattern or add `Role.Link` semantics.

                WCAG 2.4.4 Link Purpose (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeStructureDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}
