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
import org.jetbrains.uast.ULiteralExpression

class ComposeA11yDetector : Detector(), SourceCodeScanner {

    override fun getApplicableMethodNames() = listOf(
        "Icon", "Image",
        "TextField", "OutlinedTextField",
        "IconButton",
        "Slider", "RangeSlider",
        "ExposedDropdownMenuBox",
        "Tab", "LeadingIconTab",
        "Scaffold",
        "Checkbox", "TriStateCheckbox", "Switch",
        "Button", "TextButton", "OutlinedButton", "FilledTonalButton", "ElevatedButton",
    )

    override fun visitMethodCall(
        context: JavaContext,
        node: UCallExpression,
        method: PsiMethod
    ) {
        when (node.methodName) {
            "Icon", "Image" -> {
                checkContentDescription(context, node, method)
                checkLabelContainsRoleImage(context, node, method)
            }
            "TextField", "OutlinedTextField" -> checkTextFieldLabel(context, node, method)
            "IconButton" -> {
                checkIconButtonLabel(context, node, method)
                checkLabelInName(context, node)
            }
            "Slider", "RangeSlider" -> checkSliderLabel(context, node, method)
            "ExposedDropdownMenuBox" -> checkDropdownLabel(context, node)
            "Tab", "LeadingIconTab" -> {
                checkTabLabel(context, node, method)
                checkLabelInName(context, node)
            }
            "Scaffold" -> checkPaneTitle(context, node)
            "Checkbox", "TriStateCheckbox", "Switch" -> checkToggleLabel(context, node)
            "Button", "TextButton", "OutlinedButton", "FilledTonalButton", "ElevatedButton" -> {
                checkLabelContainsRoleButton(context, node)
                checkLabelInName(context, node)
            }
        }
    }

    // --- Existing rules ---

    private fun checkContentDescription(
        context: JavaContext,
        node: UCallExpression,
        method: PsiMethod
    ) {
        val params = method.parameterList.parameters
        val cdIndex = params.indexOfFirst { it.name == "contentDescription" }
        if (cdIndex == -1) return

        val arg = node.getArgumentForParameter(cdIndex) ?: return

        if (arg is ULiteralExpression && arg.isNull) {
            var parent = node.uastParent
            while (parent != null) {
                if (parent is UCallExpression && parent.methodName == "IconButton") {
                    return
                }
                parent = parent.uastParent
            }

            context.report(
                ICON_MISSING_LABEL,
                node,
                context.getNameLocation(node),
                "${node.methodName} has `contentDescription = null`. " +
                    "Provide a descriptive label for screen reader users."
            )
        }

        if (arg is ULiteralExpression && arg.value is String && (arg.value as String).isEmpty()) {
            context.report(
                EMPTY_CONTENT_DESCRIPTION,
                node,
                context.getNameLocation(node),
                "Empty `contentDescription` makes the element invisible to screen readers. " +
                    "Use `null` for decorative elements or provide a meaningful description."
            )
        }
    }

    private fun checkTextFieldLabel(
        context: JavaContext,
        node: UCallExpression,
        method: PsiMethod
    ) {
        val params = method.parameterList.parameters
        val labelIndex = params.indexOfFirst { it.name == "label" }
        if (labelIndex == -1) return

        val arg = node.getArgumentForParameter(labelIndex)
        if (arg == null || (arg is ULiteralExpression && arg.isNull)) {
            context.report(
                TEXTFIELD_MISSING_LABEL,
                node,
                context.getNameLocation(node),
                "${node.methodName} is missing a `label` parameter. " +
                    "Screen reader users need labels to identify input fields."
            )
        }
    }

    // --- New rules ---

    private fun checkIconButtonLabel(
        context: JavaContext,
        node: UCallExpression,
        method: PsiMethod
    ) {
        val params = method.parameterList.parameters
        val cdIndex = params.indexOfFirst { it.name == "contentDescription" }
        // IconButton doesn't have contentDescription param — check onClick lambda body
        val sourceText = node.asSourceString()

        // Check modifier chain for semantics contentDescription
        if (sourceText.contains("contentDescription") || sourceText.contains("clearAndSetSemantics")) {
            return
        }

        // Check if child Icon has a non-null contentDescription
        if (sourceText.contains("contentDescription") && !sourceText.contains("contentDescription = null")) {
            return
        }

        // If the only Icon inside has null contentDescription or no contentDescription
        if (cdIndex >= 0) return // Has its own CD param (unlikely for IconButton, but safe check)

        // Check if the body source has an Icon with a real contentDescription
        val bodySource = sourceText
        val hasLabeledIcon = bodySource.contains("contentDescription =") &&
            !bodySource.contains("contentDescription = null")
        if (hasLabeledIcon) return

        // Check for onClick label or semantics label
        if (bodySource.contains("onClickLabel") || bodySource.contains("semantics")) return

        context.report(
            ICON_BUTTON_MISSING_LABEL,
            node,
            context.getNameLocation(node),
            "IconButton has no accessible label. Add `contentDescription` to the inner Icon or add semantics to the IconButton."
        )
    }

    private fun checkSliderLabel(
        context: JavaContext,
        node: UCallExpression,
        method: PsiMethod
    ) {
        val sourceText = node.asSourceString()
        if (sourceText.contains("contentDescription") ||
            sourceText.contains("clearAndSetSemantics") ||
            sourceText.contains("mergeDescendants") ||
            sourceText.contains("LabeledContent")) {
            return
        }

        // Walk parent to check for a labeling container
        var parent = node.uastParent
        var depth = 0
        while (parent != null && depth < 5) {
            if (parent is UCallExpression) {
                val parentSource = parent.asSourceString()
                if (parentSource.contains("mergeDescendants") ||
                    parentSource.contains("contentDescription") ||
                    parentSource.contains("LabeledContent")) {
                    return
                }
            }
            parent = parent.uastParent
            depth++
        }

        context.report(
            SLIDER_MISSING_LABEL,
            node,
            context.getNameLocation(node),
            "${node.methodName} has no accessible label. Add semantics contentDescription or wrap with a labeled container."
        )
    }

    private fun checkDropdownLabel(
        context: JavaContext,
        node: UCallExpression,
    ) {
        val sourceText = node.asSourceString()
        if (sourceText.contains("label =") && sourceText.contains("TextField")) return
        if (sourceText.contains("contentDescription")) return

        context.report(
            DROPDOWN_MISSING_LABEL,
            node,
            context.getNameLocation(node),
            "ExposedDropdownMenuBox has no accessible label. Include a labeled TextField inside it."
        )
    }

    private fun checkTabLabel(
        context: JavaContext,
        node: UCallExpression,
        method: PsiMethod
    ) {
        val params = method.parameterList.parameters
        val textIndex = params.indexOfFirst { it.name == "text" }
        if (textIndex >= 0) {
            val arg = node.getArgumentForParameter(textIndex)
            if (arg != null && !(arg is ULiteralExpression && arg.isNull)) return
        }

        val sourceText = node.asSourceString()
        if (sourceText.contains("contentDescription") ||
            sourceText.contains("Text(") ||
            sourceText.contains("text =")) {
            return
        }

        context.report(
            TAB_MISSING_LABEL,
            node,
            context.getNameLocation(node),
            "${node.methodName} has no text label. Screen reader users need a label to identify each tab."
        )
    }

    private fun checkPaneTitle(
        context: JavaContext,
        node: UCallExpression,
    ) {
        val sourceText = node.asSourceString()
        if (sourceText.contains("paneTitle") || sourceText.contains("testTag")) return

        // Check if topBar has a title
        if (sourceText.contains("TopAppBar") && sourceText.contains("title =")) return
        if (sourceText.contains("CenterAlignedTopAppBar") && sourceText.contains("title =")) return

        context.report(
            MISSING_PANE_TITLE,
            node,
            context.getNameLocation(node),
            "Scaffold has no pane title. Add `Modifier.semantics { paneTitle = \"...\" }` or use a TopAppBar with title."
        )
    }

    private fun checkToggleLabel(
        context: JavaContext,
        node: UCallExpression,
    ) {
        val sourceText = node.asSourceString()
        if (sourceText.contains("contentDescription") ||
            sourceText.contains("clearAndSetSemantics") ||
            sourceText.contains("toggleable") ||
            sourceText.contains("mergeDescendants")) {
            return
        }

        // Check parent for labeling Row/Column
        var parent = node.uastParent
        var depth = 0
        while (parent != null && depth < 5) {
            if (parent is UCallExpression) {
                val parentSource = parent.asSourceString()
                if (parentSource.contains("mergeDescendants") ||
                    parentSource.contains("toggleable") ||
                    parentSource.contains("contentDescription") ||
                    parentSource.contains("clearAndSetSemantics")) {
                    return
                }
                if (parent.methodName in listOf("Row", "Column") && parentSource.contains("Text(")) {
                    if (parentSource.contains("mergeDescendants")) return
                }
            }
            parent = parent.uastParent
            depth++
        }

        context.report(
            TOGGLE_MISSING_LABEL,
            node,
            context.getNameLocation(node),
            "${node.methodName} has no accessible label. Wrap in a Row with mergeDescendants and a Text, or add semantics contentDescription."
        )
    }

    private fun checkLabelContainsRoleImage(
        context: JavaContext,
        node: UCallExpression,
        method: PsiMethod
    ) {
        val params = method.parameterList.parameters
        val cdIndex = params.indexOfFirst { it.name == "contentDescription" }
        if (cdIndex == -1) return

        val arg = node.getArgumentForParameter(cdIndex) ?: return
        if (arg is ULiteralExpression && arg.value is String) {
            val desc = (arg.value as String).lowercase()
            val roleWords = listOf("image", "icon", "picture", "graphic", "photo")
            for (word in roleWords) {
                if (desc.contains(word)) {
                    context.report(
                        LABEL_CONTAINS_ROLE_IMAGE,
                        arg,
                        context.getLocation(arg),
                        "Content description contains the role word \"$word\". TalkBack already announces the element type."
                    )
                    break
                }
            }
        }
    }

    private fun checkLabelInName(
        context: JavaContext,
        node: UCallExpression,
    ) {
        val sourceText = node.asSourceString()

        // Extract visible text from Text("...") call in the composable body
        val textPattern = Regex("""Text\s*\(\s*"((?:[^"\\]|\\.)*)"""")
        val visibleMatch = textPattern.find(sourceText) ?: return
        val visibleText = visibleMatch.groupValues[1].trim()
        if (visibleText.isBlank()) return

        // Extract accessible name from contentDescription = "..."
        val cdPattern = Regex("""contentDescription\s*=\s*"((?:[^"\\]|\\.)*)"""")
        val accessibleMatch = cdPattern.find(sourceText) ?: return
        val accessibleName = accessibleMatch.groupValues[1].trim()
        if (accessibleName.isBlank()) return

        val visibleLower = visibleText.lowercase()
        val accessibleLower = accessibleName.lowercase()

        when {
            !accessibleLower.contains(visibleLower) ->
                context.report(
                    LABEL_IN_NAME_ERROR,
                    node,
                    context.getNameLocation(node),
                    "Visible text \"$visibleText\" is not contained in " +
                        "contentDescription \"$accessibleName\". " +
                        "Voice Control users speak the visible text to activate controls."
                )
            !accessibleLower.startsWith(visibleLower) ->
                context.report(
                    LABEL_IN_NAME_WARNING,
                    node,
                    context.getNameLocation(node),
                    "Visible text \"$visibleText\" is not at the start of " +
                        "contentDescription \"$accessibleName\". " +
                        "WCAG 2.5.3 recommends the accessible name begins with the visible text."
                )
        }
    }

    private fun checkLabelContainsRoleButton(
        context: JavaContext,
        node: UCallExpression,
    ) {
        val sourceText = node.asSourceString()
        val buttonPattern = Regex(""""[^"]*\b[Bb]utton\b[^"]*"""")
        val match = buttonPattern.find(sourceText) ?: return

        context.report(
            LABEL_CONTAINS_ROLE_BUTTON,
            node,
            context.getNameLocation(node),
            "Button label contains the word \"button\". TalkBack already announces the element as a button."
        )
    }

    companion object {
        val ICON_MISSING_LABEL = Issue.create(
            id = "A11yIconMissingLabel",
            briefDescription = "Icon/Image missing content description",
            explanation = """
                Icons and images should have a meaningful `contentDescription` for screen \
                reader users. Set `contentDescription` to a descriptive string. Use `null` \
                only for purely decorative elements inside a labeled container (e.g. \
                IconButton with its own contentDescription).

                WCAG 1.1.1 Non-text Content (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 8,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeA11yDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val EMPTY_CONTENT_DESCRIPTION = Issue.create(
            id = "A11yEmptyContentDescription",
            briefDescription = "Empty content description",
            explanation = """
                An empty string for `contentDescription` makes the element discoverable \
                but unlabeled by screen readers. Use `null` if the element is decorative, \
                or provide a meaningful description.

                WCAG 1.1.1 Non-text Content (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 7,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeA11yDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val TEXTFIELD_MISSING_LABEL = Issue.create(
            id = "A11yTextFieldMissingLabel",
            briefDescription = "TextField missing label",
            explanation = """
                TextField and OutlinedTextField should have a `label` parameter so screen \
                reader users can identify the input field's purpose.

                WCAG 4.1.2 Name, Role, Value (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 8,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeA11yDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val ICON_BUTTON_MISSING_LABEL = Issue.create(
            id = "A11yIconButtonMissingLabel",
            briefDescription = "IconButton missing accessible label",
            explanation = """
                IconButton must have an accessible label. Either provide a `contentDescription` \
                on the inner Icon, or add semantics to the IconButton's modifier.

                WCAG 4.1.2 Name, Role, Value (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 8,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeA11yDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val SLIDER_MISSING_LABEL = Issue.create(
            id = "A11ySliderMissingLabel",
            briefDescription = "Slider missing accessible label",
            explanation = """
                Slider and RangeSlider must have an associated label via semantics \
                contentDescription or a wrapping composable with mergeDescendants.

                WCAG 4.1.2 Name, Role, Value (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 8,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeA11yDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val DROPDOWN_MISSING_LABEL = Issue.create(
            id = "A11yDropdownMissingLabel",
            briefDescription = "Dropdown menu missing label",
            explanation = """
                ExposedDropdownMenuBox must include a labeled TextField inside it so \
                screen reader users can identify the dropdown's purpose.

                WCAG 4.1.2 Name, Role, Value (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 8,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeA11yDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val TAB_MISSING_LABEL = Issue.create(
            id = "A11yTabMissingLabel",
            briefDescription = "Tab missing text label",
            explanation = """
                Tab and LeadingIconTab should have a text label so screen reader users \
                can identify each tab. Use the `text` parameter or include a Text composable.

                WCAG 2.4.2 Page Titled (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 7,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeA11yDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val MISSING_PANE_TITLE = Issue.create(
            id = "A11yMissingPaneTitle",
            briefDescription = "Scaffold missing pane title",
            explanation = """
                Scaffold screens should have a pane title for screen readers. Use \
                `Modifier.semantics { paneTitle = "..." }` or include a TopAppBar with title.

                WCAG 2.4.2 Page Titled (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 7,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeA11yDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val TOGGLE_MISSING_LABEL = Issue.create(
            id = "A11yToggleMissingLabel",
            briefDescription = "Toggle control missing label",
            explanation = """
                Checkbox, Switch, and TriStateCheckbox must have an associated text label. \
                Wrap in a Row with `Modifier.toggleable()` and include a Text composable, \
                or add semantics contentDescription.

                WCAG 4.1.2 Name, Role, Value (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 8,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeA11yDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val LABEL_CONTAINS_ROLE_IMAGE = Issue.create(
            id = "A11yLabelContainsRoleImage",
            briefDescription = "Content description contains role name",
            explanation = """
                Content descriptions should not contain role words like "image", "icon", \
                "picture". TalkBack already announces the element type. Describe what the \
                image shows, not what it is.

                WCAG 1.1.1 Non-text Content (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeA11yDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val LABEL_IN_NAME_ERROR = Issue.create(
            id = "A11yLabelInNameError",
            briefDescription = "Visible text not contained in accessible name",
            explanation = """
                The accessible name (contentDescription) must contain the visible text label. \
                Voice Control users speak the visible text to activate the control. \
                If the visible text is "Save" but contentDescription is "Submit Form", \
                the user cannot activate it by saying "tap Save".

                WCAG 2.5.3 Label in Name (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 9,
            severity = Severity.ERROR,
            implementation = Implementation(
                ComposeA11yDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val LABEL_IN_NAME_WARNING = Issue.create(
            id = "A11yLabelInNameWarning",
            briefDescription = "Visible text not at start of accessible name",
            explanation = """
                The accessible name should begin with the visible text label. \
                WCAG 2.5.3 recommends the visible text appears at the start of the \
                accessible name so Voice Control users can activate the control by \
                speaking the label.

                WCAG 2.5.3 Label in Name (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeA11yDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val LABEL_CONTAINS_ROLE_BUTTON = Issue.create(
            id = "A11yLabelContainsRoleButton",
            briefDescription = "Button label contains role name",
            explanation = """
                Button labels should not contain the word "button". TalkBack already \
                announces the element as a button. Describe the action instead.

                WCAG 4.1.2 Name, Role, Value (Level A)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeA11yDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}
