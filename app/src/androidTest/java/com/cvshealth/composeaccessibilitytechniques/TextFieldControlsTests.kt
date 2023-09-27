package com.cvshealth.composeaccessibilitytechniques

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.NativeKeyEvent
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performKeyPress
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.ImeAction
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.textfield_controls.TextFieldControlsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.textfield_controls.textFieldControlsExample1ButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.textfield_controls.textFieldControlsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.textfield_controls.textFieldControlsExample1TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.textfield_controls.textFieldControlsHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TextFieldControlsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme() {
                TextFieldControlsScreen { }
            }
        }
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(textFieldControlsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(textFieldControlsExample1HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExampleTextsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(textFieldControlsExample1TextFieldTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textFieldControlsExample1ButtonTestTag)
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
    fun verifyExample1TextFieldHasExpectedInitialState() {
        composeTestRule
            .onNode(
                hasTestTag(textFieldControlsExample1TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Done)
                        and
                        !isError()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textFieldControlsExample1ButtonTestTag)
                        and
                        hasClickAction()
            )
            .assertExists()
    }

    @Test
    fun verifySubmittingWithNoNameSetsErrorAndAddingTextClearsError() {
        composeTestRule
            .onNodeWithTag(textFieldControlsExample1ButtonTestTag)
            .performClick()
        composeTestRule
            .onNode(
                hasTestTag(textFieldControlsExample1TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Done)
                        and
                        isError()
            )
            .assertExists()

        composeTestRule
            .onNodeWithTag(textFieldControlsExample1TextFieldTestTag)
            .performTextInput("Test Name")
        composeTestRule
            .onNode(
                hasTestTag(textFieldControlsExample1TextFieldTestTag)
                        and
                        !isError()
            )
            .assertExists()
    }

    @Test
    fun verifyImeActionDoneWithNoNameSetsErrorAndAddingTextClearsError() {
        composeTestRule
            .onNodeWithTag(textFieldControlsExample1TextFieldTestTag)
            .performImeAction()
        composeTestRule
            .onNode(
                hasTestTag(textFieldControlsExample1TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Done)
                        and
                        isError()
            )
            .assertExists()

        composeTestRule
            .onNodeWithTag(textFieldControlsExample1TextFieldTestTag)
            .performTextInput("Test Name")
        composeTestRule
            .onNode(
                hasTestTag(textFieldControlsExample1TextFieldTestTag)
                        and
                        !isError()
            )
            .assertExists()
    }

    @Test
    fun verifyPressingEnterWithNoNameSetsErrorAndAddingTextClearsError() {
        composeTestRule
            .onNodeWithTag(textFieldControlsExample1TextFieldTestTag)
            .performClick()
            .performKeyPress(KeyEvent(NativeKeyEvent(android.view.KeyEvent.ACTION_UP, android.view.KeyEvent.KEYCODE_ENTER)))
        composeTestRule
            .onNode(
                hasTestTag(textFieldControlsExample1TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Done)
                        and
                        isError()
            )
            .assertExists()

        composeTestRule
            .onNodeWithTag(textFieldControlsExample1TextFieldTestTag)
            .performTextInput("Test Name")
        composeTestRule
            .onNode(
                hasTestTag(textFieldControlsExample1TextFieldTestTag)
                        and
                        !isError()
            )
            .assertExists()
    }

    @Test
    fun verifySubmittingWithNameDoesNotSetError() {
        composeTestRule
            .onNodeWithTag(textFieldControlsExample1TextFieldTestTag)
            .performTextInput("Test Name")
        composeTestRule
            .onNodeWithTag(textFieldControlsExample1ButtonTestTag)
            .performClick()
        composeTestRule
            .onNode(
                hasTestTag(textFieldControlsExample1TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Done)
                        and
                        !isError()
            )
            .assertExists()
    }
}