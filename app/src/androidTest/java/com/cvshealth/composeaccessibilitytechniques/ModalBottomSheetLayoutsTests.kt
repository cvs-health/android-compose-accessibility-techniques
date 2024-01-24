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

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.modalbottomsheet_layouts.ModalBottomSheetLayoutsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.modalbottomsheet_layouts.modalBottomSheetLayoutsExample1Button1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.modalbottomsheet_layouts.modalBottomSheetLayoutsExample1Button2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.modalbottomsheet_layouts.modalBottomSheetLayoutsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.modalbottomsheet_layouts.modalBottomSheetLayoutsExample1SelectedItemTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.modalbottomsheet_layouts.modalBottomSheetLayoutsExampleSheetTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.modalbottomsheet_layouts.modalBottomSheetLayoutsHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ModalBottomSheetLayoutsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme() {
                ModalBottomSheetLayoutsScreen { }
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
            .onNode(hasTestTag(modalBottomSheetLayoutsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(modalBottomSheetLayoutsExample1HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExample1ControlsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(modalBottomSheetLayoutsExample1Button1TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(modalBottomSheetLayoutsExample1Button2TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(modalBottomSheetLayoutsExample1SelectedItemTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        // Can't check modalBottomSheetLayoutsExampleSheetTestTag for a lack of headings here,
        // because it is not composed yet.
    }

    @Test
    fun verifyEveryHeadingsHasATestTag() {
        composeTestRule.onNode(hasNoTestTag() and isHeading()).assertDoesNotExist()
    }

    @Test
    fun verifyExample1Button1ProducesExpectedBehavior() {
        // Verify that button 1 exists and is not in a modal pop-up.
        composeTestRule
            .onNode(
                hasTestTag(modalBottomSheetLayoutsExample1Button1TestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
                        and
                        !hasAnyAncestor(SemanticsMatcher.keyIsDefined(SemanticsProperties.IsPopup))
            )
            .assertExists()

        // Press button 1 to display bottom sheet.
        composeTestRule
            .onNode(
                hasTestTag(modalBottomSheetLayoutsExample1Button1TestTag)
            )
            .performScrollTo()
            .performClick()

        // Verify bottom sheet displayed.
        composeTestRule
            .onNodeWithTag(modalBottomSheetLayoutsExampleSheetTestTag)
            .assertIsDisplayed()

        // Verify bottom sheet is modal.
        composeTestRule
            .onNodeWithTag(modalBottomSheetLayoutsExampleSheetTestTag)
            .assert(
                hasAnyAncestor(SemanticsMatcher.keyIsDefined(SemanticsProperties.IsPopup))
            )

        // Verify bottom sheet contains no heading(s).
        composeTestRule
            .onNode(
                hasTestTag(modalBottomSheetLayoutsExampleSheetTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Verify first bottom sheet item is displayed.
        // Note: Don't seem to be able to test that it has keyboard focus, probably due to timing...
        composeTestRule
            .onNodeWithText("Apple")
            .assertIsDisplayed()

        // Verify a slightly later bottom sheet item is displayed.
        composeTestRule
            .onNodeWithText("Banana")
            .assertIsDisplayed()

        // Activate the later bottom sheet item.
        composeTestRule
            .onNodeWithText("Banana")
            .performScrollTo()
            .performClick()

        // Bottom sheet should have been dismissed, so it should no longer be composed.
        composeTestRule
            .onNodeWithTag(modalBottomSheetLayoutsExampleSheetTestTag)
            .assertDoesNotExist()

        // Selected item text should contain the text of the selected bottom sheet item.
        composeTestRule
            .onNode(
                hasTestTag(modalBottomSheetLayoutsExample1SelectedItemTestTag)
                        and
                        hasText(text = "Banana", substring = true)
            )
            .assertIsDisplayed()
    }

    @Test
    fun verifyExample1Button2ProducesExpectedBehavior() {
        // Verify that button 2 exists and is not in a modal pop-up.
        composeTestRule
            .onNode(
                hasTestTag(modalBottomSheetLayoutsExample1Button2TestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
                        and
                        !hasAnyAncestor(SemanticsMatcher.keyIsDefined(SemanticsProperties.IsPopup))
            )
            .assertExists()

        // Press button 2 to display bottom sheet.
        composeTestRule
            .onNode(
                hasTestTag(modalBottomSheetLayoutsExample1Button2TestTag)
            )
            .performScrollTo()
            .performClick()

        // Verify bottom sheet displayed.
        composeTestRule
            .onNodeWithTag(modalBottomSheetLayoutsExampleSheetTestTag)
            .assertIsDisplayed()

        // Verify bottom sheet is modal.
        composeTestRule
            .onNodeWithTag(modalBottomSheetLayoutsExampleSheetTestTag)
            .assert(
                hasAnyAncestor(SemanticsMatcher.keyIsDefined(SemanticsProperties.IsPopup))
            )

        // Verify bottom sheet contains no heading(s).
        composeTestRule
            .onNode(
                hasTestTag(modalBottomSheetLayoutsExampleSheetTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Verify first bottom sheet item is displayed.
        // Note: Don't seem to be able to test that it has keyboard focus, probably due to timing...
        composeTestRule
            .onNodeWithText("Apple")
            .assertIsDisplayed()

        // Verify a slightly later bottom sheet item is displayed.
        composeTestRule
            .onNodeWithText("Grape")
            .assertIsDisplayed()

        // Activate the later bottom sheet item.
        composeTestRule
            .onNodeWithText("Grape")
            .performScrollTo()
            .performClick()

        // Bottom sheet should have been dismissed, so it should no longer be composed.
        composeTestRule
            .onNodeWithTag(modalBottomSheetLayoutsExampleSheetTestTag)
            .assertDoesNotExist()

        // Selected item text should contain the text of the selected bottom sheet item.
        composeTestRule
            .onNode(
                hasTestTag(modalBottomSheetLayoutsExample1SelectedItemTestTag)
                        and
                        hasText(text = "Grape", substring = true)
            )
            .assertIsDisplayed()
    }
}