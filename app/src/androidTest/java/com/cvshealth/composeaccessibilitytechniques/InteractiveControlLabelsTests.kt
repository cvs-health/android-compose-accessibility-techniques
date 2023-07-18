package com.cvshealth.composeaccessibilitytechniques

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.InteractiveControlLabelsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample1ControlTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample2ControlTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample3ControlTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample4ControlTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample4HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample5ControlTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample5HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample6ControlTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample6HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample7HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample8HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample9ControlTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample9HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InteractiveControlLabelsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme() {
                InteractiveControlLabelsScreen { /* NO-OP */ }
            }
        }
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample3HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample4HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample5HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample6HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample7HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample8HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample9HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyEveryHeadingsHasATestTag() {
        composeTestRule.onNode(hasNoTestTag() and isHeading()).assertDoesNotExist()
    }

    @Test
    fun verifyGoodExamplesHaveText() {
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample2ControlTestTag) and hasText())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample4ControlTestTag) and hasText())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample6ControlTestTag) and hasText())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample9ControlTestTag) and hasText())
            .assertExists()
    }

    @Test
    fun verifyBadExamplesDoNotHaveText() {
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample1ControlTestTag) and !hasText())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample3ControlTestTag) and !hasText())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample5ControlTestTag) and !hasText())
            .assertExists()
    }

    @Test
    fun verifyThatFauxCheckboxRowIsNotToggleable() {
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample3ControlTestTag) and !isToggleable())
            .assertExists()
    }

    @Test
    fun verifyThatCheckboxRowIsToggleable() {
        composeTestRule
            .onNodeWithTag(interactiveControlLabelsExample4ControlTestTag)
            .assertIsToggleable()
    }

    @Test
    fun verifyThatCheckboxRowToggles() {
        composeTestRule
            .onNodeWithTag(interactiveControlLabelsExample4ControlTestTag)
            .assertIsOff()
        composeTestRule
            .onNodeWithTag(interactiveControlLabelsExample4ControlTestTag)
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNodeWithTag(interactiveControlLabelsExample4ControlTestTag)
            .assertIsOn()
        composeTestRule
            .onNodeWithTag(interactiveControlLabelsExample4ControlTestTag)
            .performClick()
        composeTestRule
            .onNodeWithTag(interactiveControlLabelsExample4ControlTestTag)
            .assertIsOff()
    }

    @Test
    fun verifyThatFauxSwitchRowIsNotToggleable() {
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample5ControlTestTag) and !isToggleable())
            .assertExists()
    }

    @Test
    fun verifyThatSwitchRowIsToggleable() {
        composeTestRule
            .onNodeWithTag(interactiveControlLabelsExample6ControlTestTag)
            .assertIsToggleable()
    }

    @Test
    fun verifyThatSwitchRowToggles() {
        composeTestRule
            .onNodeWithTag(interactiveControlLabelsExample6ControlTestTag)
            .assertIsOff()
        composeTestRule
            .onNodeWithTag(interactiveControlLabelsExample6ControlTestTag)
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNodeWithTag(interactiveControlLabelsExample6ControlTestTag)
            .assertIsOn()
        composeTestRule
            .onNodeWithTag(interactiveControlLabelsExample6ControlTestTag)
            .performClick()
        composeTestRule
            .onNodeWithTag(interactiveControlLabelsExample6ControlTestTag)
            .assertIsOff()
    }

    @Test
    fun verifyThatButtonHasClickAction() {
        composeTestRule
            .onNodeWithTag(interactiveControlLabelsExample9ControlTestTag)
            .assertHasClickAction()
    }
}