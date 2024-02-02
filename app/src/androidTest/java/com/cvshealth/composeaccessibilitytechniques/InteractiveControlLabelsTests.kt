/*
   Copyright 2023-2024 CVS Health and/or one of its affiliates

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.cvshealth.composeaccessibilitytechniques

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.NativeKeyEvent
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasContentDescriptionExactly
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyPress
import androidx.compose.ui.test.performScrollTo
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.InteractiveControlLabelsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample10ControlTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample10HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample11ControlTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample11HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample12ControlTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample12HeadingTestTag
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
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample7ControlTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample7HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.interactiveControlLabelsExample8ControlTestTag
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
    fun verifyScreenHasPaneTitle() {
        composeTestRule.onNode(hasPaneTitle()).assertExists()
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
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample10HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample11HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample12HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyControlsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(interactiveControlLabelsExample1ControlTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(interactiveControlLabelsExample2ControlTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(interactiveControlLabelsExample3ControlTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(interactiveControlLabelsExample4ControlTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(interactiveControlLabelsExample5ControlTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(interactiveControlLabelsExample6ControlTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(interactiveControlLabelsExample7ControlTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(interactiveControlLabelsExample8ControlTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(interactiveControlLabelsExample9ControlTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(interactiveControlLabelsExample10ControlTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(interactiveControlLabelsExample11ControlTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(interactiveControlLabelsExample12ControlTestTag)
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

    /**
     * verifyTextFieldFocusability - verifies that pressing a TextField focuses on it and that Tab
     * and Shift-Tab work appropriately.
     *
     * While this functionality isn't the point of the screen, it's a good place to demonstrate how
     * to test this. Because at the time of writing, isFocused() only appears to work on TextFields.
     */
    @Test
    fun verifyTextFieldFocusability() {
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample2ControlTestTag))
            .assert(!isFocused())
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample2ControlTestTag))
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample2ControlTestTag))
            .assert(isFocused())

        // Back-tab to Example 1 TextField
        composeTestRule
            .onNodeWithTag(interactiveControlLabelsExample2ControlTestTag)
            .performKeyPress(
                KeyEvent(
                    NativeKeyEvent(
                        0,
                        0,
                        NativeKeyEvent.ACTION_DOWN,
                        NativeKeyEvent.KEYCODE_TAB,
                        0,
                        NativeKeyEvent.META_SHIFT_ON
                    )
                )
            )
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample1ControlTestTag))
            .assert(isFocused())

        // Forward-tab to Example 2 TextField
        composeTestRule
            .onNodeWithTag(interactiveControlLabelsExample1ControlTestTag)
            .performScrollTo()
            .performKeyPress(
                KeyEvent(NativeKeyEvent(NativeKeyEvent.ACTION_UP, NativeKeyEvent.KEYCODE_TAB))
            )
        composeTestRule
            .onNode(hasTestTag(interactiveControlLabelsExample2ControlTestTag))
            .assert(isFocused())
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
        composeTestRule
            .onNode(
                hasTestTag(interactiveControlLabelsExample11ControlTestTag)
                        and
                        hasContentDescription()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(interactiveControlLabelsExample12ControlTestTag)
                        and
                        hasContentDescription()
            )
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
        composeTestRule
            .onNode(
                hasTestTag(interactiveControlLabelsExample10ControlTestTag)
                        and
                        !hasText()
                        and
                        !hasContentDescription()
            )
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

    @Test
    fun verifyThatSlidersHaveExpectedRanges() {
        composeTestRule
            .onNode(
                hasTestTag(interactiveControlLabelsExample10ControlTestTag)
                        and
                        hasProgressBarRangeInfo(
                            ProgressBarRangeInfo(0f,0f..10f, 9)
                        )
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(interactiveControlLabelsExample11ControlTestTag)
                        and
                        hasProgressBarRangeInfo(
                            ProgressBarRangeInfo(0f,0f..10f, 9)
                        )
            )
            .assertExists()
    }

    @Test
    fun verifyThatRangeSliderHasExpectedRanges() {
        // Test Example 12, Range start thumb value
        composeTestRule
            .onNode(
                hasParent(hasTestTag(interactiveControlLabelsExample12ControlTestTag))
                        and
                        hasContentDescriptionExactly("Range start")
                        and
                        hasProgressBarRangeInfo(
                            ProgressBarRangeInfo(0f,0f..10f, 9)
                        )
            )
            .assertExists()

        // Test Example 12, Range end thumb value
        composeTestRule
            .onNode(
                hasParent(hasTestTag(interactiveControlLabelsExample12ControlTestTag))
                        and
                        hasContentDescriptionExactly("Range end")
                        and
                        hasProgressBarRangeInfo(
                            ProgressBarRangeInfo(10f,0f..10f, 9)
                        )
            )
            .assertExists()
    }
}