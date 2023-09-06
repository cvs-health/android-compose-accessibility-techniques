package com.cvshealth.composeaccessibilitytechniques

import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
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
}