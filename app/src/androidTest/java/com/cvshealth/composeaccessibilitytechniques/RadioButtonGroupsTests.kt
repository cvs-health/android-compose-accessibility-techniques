package com.cvshealth.composeaccessibilitytechniques

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher.Companion.expectValue
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelectable
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.isNotSelected
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.radio_button_groups.RadioButtonGroupsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.radio_button_groups.radioButtonGroupsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.radio_button_groups.radioButtonGroupsExample1RadioButtonGroupTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.radio_button_groups.radioButtonGroupsExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.radio_button_groups.radioButtonGroupsExample2RadioButtonGroupTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.radio_button_groups.radioButtonGroupsHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RadioButtonGroupsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme() {
                RadioButtonGroupsScreen { }
            }
        }
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(radioButtonGroupsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(radioButtonGroupsExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(radioButtonGroupsExample2HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExampleRadioButtonGroupsHaveNoHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(radioButtonGroupsExample1RadioButtonGroupTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(radioButtonGroupsExample2RadioButtonGroupTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
    }

    @Test
    fun verifyEveryHeadingsHasATestTag() {
        composeTestRule.onNode(hasNoTestTag() and isHeading()).assertDoesNotExist()
    }

    @Test
    fun verifyExample1HasExpectedProperties() {
        // Text labels are not associated with the RadioButton role.
        composeTestRule
            .onNode(
                hasAnyAncestor(hasTestTag(radioButtonGroupsExample1RadioButtonGroupTestTag))
                        and
                        hasText("Banana")
                        and
                        !expectValue(SemanticsProperties.Role, Role.RadioButton)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasAnyAncestor(hasTestTag(radioButtonGroupsExample1RadioButtonGroupTestTag))
                        and
                        hasText("Grape")
                        and
                        !expectValue(SemanticsProperties.Role, Role.RadioButton)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasAnyAncestor(hasTestTag(radioButtonGroupsExample1RadioButtonGroupTestTag))
                        and
                        hasText("Orange")
                        and
                        !expectValue(SemanticsProperties.Role, Role.RadioButton)
            )
            .assertExists()

        // Only one RadioButton node is selected
        composeTestRule
            .onNode(
                hasAnyAncestor(hasTestTag(radioButtonGroupsExample1RadioButtonGroupTestTag))
                        and
                        expectValue(SemanticsProperties.Role, Role.RadioButton)
                        and
                        isSelected()
            )
            .assertExists()
    }

    @Test
    fun verifyExample2HasExpectedProperties() {
        composeTestRule
            .onNode(
                hasAnyAncestor(hasTestTag(radioButtonGroupsExample2RadioButtonGroupTestTag))
                        and
                        hasText("Banana")
                        and
                        expectValue(SemanticsProperties.Role, Role.RadioButton)
            )
            .assertIsSelected()
        composeTestRule
            .onNode(
                hasAnyAncestor(hasTestTag(radioButtonGroupsExample2RadioButtonGroupTestTag))
                        and
                        hasText("Grape")
                        and
                        expectValue(SemanticsProperties.Role, Role.RadioButton)
            )
            .assertIsSelectable()
            .assertIsNotSelected()
        composeTestRule
            .onNode(
                hasAnyAncestor(hasTestTag(radioButtonGroupsExample2RadioButtonGroupTestTag))
                        and
                        hasText("Orange")
                        and
                        isNotSelected()
                        and
                        isSelectable()
                        and
                        hasClickAction()
                        and
                        expectValue(SemanticsProperties.Role, Role.RadioButton)
            )
            .assertExists()

        // Click Orange RadioButton and verify that it is selected and Banana is not selected.
        composeTestRule
            .onNode(
                hasAnyAncestor(hasTestTag(radioButtonGroupsExample2RadioButtonGroupTestTag))
                        and
                        hasText("Orange")
                        and
                        isSelectable()
                        and
                        hasClickAction()
                        and
                        expectValue(SemanticsProperties.Role, Role.RadioButton)
            )
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNode(
                hasAnyAncestor(hasTestTag(radioButtonGroupsExample2RadioButtonGroupTestTag))
                        and
                        hasText("Banana")
                        and
                        isNotSelected()
                        and
                        isSelectable()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasAnyAncestor(hasTestTag(radioButtonGroupsExample2RadioButtonGroupTestTag))
                        and
                        hasText("Orange")
            )
            .assertIsSelected()
    }
}