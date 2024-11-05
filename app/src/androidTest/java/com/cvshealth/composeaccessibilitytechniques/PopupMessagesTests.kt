/*
   Copyright 2024 CVS Health and/or one of its affiliates

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

import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescriptionExactly
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.genericScaffoldTitleTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.PopupMessagesScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample1ButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample2ButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample3ButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample4ButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample4HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample5ButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample5HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample6AlertDialogActionButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample6AlertDialogDismissButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample6AlertDialogTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample6ButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample6HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample6ShowMoreAlertDialogDismissButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample6ShowMoreAlertDialogTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample7ButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample7HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample7MessageTextButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesExample7MessageTextTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.popupMessagesHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * PopupMessagesTests - test PopupMessagesScreen examples, to the extent possible.
 *
 * Notes:
 *     - Toasts are not visible to the semantic tests.
 *     - Snackbars and their contents do not have testTags.
 */
class PopupMessagesTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme {
                PopupMessagesScreen { }
            }
        }
    }

    @Test
    fun verifyScreenHasPaneTitle() {
        composeTestRule.onNode(hasPaneTitle()).assertExists()
    }

    @Test
    fun verifyHeadingsCount() {
        composeTestRule.onAllNodes(isHeading()).assertCountEquals(9)
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(genericScaffoldTitleTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(popupMessagesHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(popupMessagesExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(popupMessagesExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(popupMessagesExample3HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(popupMessagesExample4HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(popupMessagesExample5HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(popupMessagesExample6HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(popupMessagesExample7HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExampleContentsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample1ButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample2ButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample3ButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample4ButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample5ButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample6ButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample7ButtonTestTag)
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
    fun verifyExample1HasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample1ButtonTestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()

        // Note: Toasts do not appear in the semantics tree, so testing can't detect any result of
        // pressing Example 1's button.
    }

    @Test
    fun verifyExample2HasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample2ButtonTestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()

        // Verify that Snackbar appears when example button is pressed.
        // Note: Snackbars DO appear in the semantics tree (at the end).
        composeTestRule
            .onNodeWithTag(popupMessagesExample2ButtonTestTag)
            .performScrollTo()
            .performClick()

        // Verify Snackbar is present.
        // Note: In the absence of a custom Snackbar layout, no testTag is available, so use content
        // text or (as here) pure semantic attributes. However, pure semantics tests might apply to
        // other controls, so use them with care.
        composeTestRule
            .onNode(
        hasLiveRegionMode(LiveRegionMode.Polite)
                and
                hasDismissAction()
                and
                hasAnyChild(hasIsTraversalGroup())
            )
            .assertExists()

        // Verify that Snackbar has no button(s).
        composeTestRule
            .onNode(
                hasLiveRegionMode(LiveRegionMode.Polite)
                        and
                        hasDismissAction()
                        and
                        !hasAnyDescendant(
                            hasRole(Role.Button)
                                    and
                                    hasClickAction()
                        )

            )
            .assertExists()

        // Move time forward and verify that Snackbar has dismissed itself.
        composeTestRule.mainClock.advanceTimeBy(20000L)
        composeTestRule
            .onNode(
                hasLiveRegionMode(LiveRegionMode.Polite)
                        and
                        hasDismissAction()
                        and
                        hasAnyChild(hasIsTraversalGroup())
            )
            .assertDoesNotExist()
    }

    @Test
    fun verifyExample3HasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample3ButtonTestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()

        // Verify that Snackbar appears when example button is pressed.
        // Note: Snackbars DO appear in the semantics tree (at the end).
        composeTestRule
            .onNodeWithTag(popupMessagesExample3ButtonTestTag)
            .performScrollTo()
            .performClick()

        // Verify Snackbar is present.
        // Note: In the absence of a custom Snackbar layout, no testTag is available, so use content
        // text or (as here) pure semantic attributes. However, pure semantics tests might apply to
        // other controls, so use them with care.
        composeTestRule
            .onNode(
        hasLiveRegionMode(LiveRegionMode.Polite)
                and
                hasDismissAction()
                and
                hasAnyChild(hasIsTraversalGroup())
            )
            .assertExists()

        // Verify that Snackbar has a Dismiss icon button.
        // contentDescription of the Snackbar Dismiss button is fixed (per language) by the system.
        composeTestRule
            .onNode(
                hasContentDescriptionExactly("Dismiss")
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()

        // Verify that Snackbar has no button other than the "Dismiss" button.
        composeTestRule
            .onNode(
                hasLiveRegionMode(LiveRegionMode.Polite)
                        and
                        hasDismissAction()
                        and
                        !hasAnyDescendant(
                            hasRole(Role.Button)
                                    and
                                    hasClickAction()
                                    and
                                    !hasContentDescriptionExactly("Dismiss")
                        )

            )
            .assertExists()

        // Move time forward and verify that Snackbar has dismissed itself.
        composeTestRule.mainClock.advanceTimeBy(20000L)
        composeTestRule
            .onNode(
                hasLiveRegionMode(LiveRegionMode.Polite)
                        and
                        hasDismissAction()
                        and
                        hasAnyChild(hasIsTraversalGroup())
            )
            .assertDoesNotExist()
    }

    @Test
    fun verifyExample3Dismisses() {
        // Press the example button.
        composeTestRule
            .onNodeWithTag(popupMessagesExample3ButtonTestTag)
            .performScrollTo()
            .performClick()

        // Press the Snackbar dismiss button.
        // Note: Can't performScrollTo() on this button -- the Snackbar is not a scrollable region.
        composeTestRule
            .onNodeWithContentDescription("Dismiss")
            .performClick()

        // Verify that Snackbar has been dismissed.
        composeTestRule
            .onNode(
                hasLiveRegionMode(LiveRegionMode.Polite)
                        and
                        hasDismissAction()
                        and
                        hasAnyChild(hasIsTraversalGroup())
            )
            .assertDoesNotExist()
    }

    @Test
    fun verifyExample4HasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample4ButtonTestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()

        // Verify that Snackbar appears when example button is pressed.
        // Note: Snackbars DO appear in the semantics tree (at the end).
        composeTestRule
            .onNodeWithTag(popupMessagesExample4ButtonTestTag)
            .performScrollTo()
            .performClick()

        // Verify Snackbar is present.
        // Note: In the absence of a custom Snackbar layout, no testTag is available, so use content
        // text or (as here) pure semantic attributes. However, pure semantics tests might apply to
        // other controls, so use them with care.
        composeTestRule
            .onNode(
        hasLiveRegionMode(LiveRegionMode.Polite)
                and
                hasDismissAction()
                and
                hasAnyChild(hasIsTraversalGroup())
            )
            .assertExists()

        // Verify that Snackbar has a "Show more" action button by checking that the Snackbar has a
        // button other than the "Dismiss" button.
        composeTestRule
            .onNode(
                hasLiveRegionMode(LiveRegionMode.Polite)
                        and
                        hasDismissAction()
                        and
                        hasAnyDescendant(
                            hasRole(Role.Button)
                                    and
                                    hasClickAction()
                                    and
                                    !hasContentDescriptionExactly("Dismiss")
                        )

            )
            .assertExists()

        // Verify that Snackbar has a Dismiss icon button.
        // contentDescription of the Snackbar Dismiss button is fixed (per language) by the system.
        composeTestRule
            .onNode(
                hasContentDescriptionExactly("Dismiss")
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()

        // Move time forward and verify that Snackbar has dismissed itself.
        composeTestRule.mainClock.advanceTimeBy(20000L)
        composeTestRule
            .onNode(
                hasLiveRegionMode(LiveRegionMode.Polite)
                        and
                        hasDismissAction()
                        and
                        hasAnyChild(hasIsTraversalGroup())
            )
            .assertDoesNotExist()
    }

    @Test
    fun verifyExample4Dismisses() {
        // Press the example button.
        composeTestRule
            .onNodeWithTag(popupMessagesExample4ButtonTestTag)
            .performScrollTo()
            .performClick()

        // Press the Snackbar dismiss button.
        // Note: Can't performScrollTo() on this button -- the Snackbar is not a scrollable region.
        composeTestRule
            .onNodeWithContentDescription("Dismiss")
            .performClick()

        // Verify that Snackbar has been dismissed.
        composeTestRule
            .onNode(
                hasLiveRegionMode(LiveRegionMode.Polite)
                        and
                        hasDismissAction()
                        and
                        hasAnyChild(hasIsTraversalGroup())
            )
            .assertDoesNotExist()
    }

    @Test
    fun verifyExample4ActionShowsSnackbar() {
        // Press the example button.
        composeTestRule
            .onNodeWithTag(popupMessagesExample4ButtonTestTag)
            .performScrollTo()
            .performClick()

        // Press the Snackbar "Show more" button, by pressing the Snackbar button that isn't the
        // "Dismiss" button.
        composeTestRule
            .onNode(

                hasRole(Role.Button)
                        and
                        hasClickAction()
                        and
                        !hasContentDescriptionExactly("Dismiss")
                        and
                        hasAnyAncestor(
                            hasLiveRegionMode(LiveRegionMode.Polite)
                                    and
                                    hasDismissAction()
                                    and
                                    hasAnyChild(hasIsTraversalGroup())
                        )
            )
            .performClick()

        // Verify that a Snackbar exists with only Dismiss buttton.
        composeTestRule
            .onNode(
                hasLiveRegionMode(LiveRegionMode.Polite)
                        and
                        hasDismissAction()
                        and
                        hasAnyChild(hasIsTraversalGroup())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasContentDescriptionExactly("Dismiss")
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasRole(Role.Button)
                        and
                        hasClickAction()
                        and
                        !hasContentDescriptionExactly("Dismiss")
                        and
                        hasAnyAncestor(
                            hasLiveRegionMode(LiveRegionMode.Polite)
                                    and
                                    hasDismissAction()
                                    and
                                    hasAnyChild(hasIsTraversalGroup())
                        )
            )
            .assertDoesNotExist()

        // Press the Snackbar dismiss button.
        // Note: Can't performScrollTo() on this button -- the Snackbar is not a scrollable region.
        composeTestRule
            .onNodeWithContentDescription("Dismiss")
            .performClick()

        // Verify that Snackbar has been dismissed.
        composeTestRule
            .onNode(
                hasLiveRegionMode(LiveRegionMode.Polite)
                        and
                        hasDismissAction()
                        and
                        hasAnyChild(hasIsTraversalGroup())
            )
            .assertDoesNotExist()
    }

    @Test
    fun verifyExample5HasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample5ButtonTestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()

        // Verify that Snackbar appears when example button is pressed.
        // Note: Snackbars DO appear in the semantics tree (at the end).
        composeTestRule
            .onNodeWithTag(popupMessagesExample5ButtonTestTag)
            .performScrollTo()
            .performClick()

        // Verify Snackbar is present.
        // Note: In the absence of a custom Snackbar layout, no testTag is available, so use content
        // text or (as here) pure semantic attributes. However, pure semantics tests might apply to
        // other controls, so use them with care.
        composeTestRule
            .onNode(
                hasLiveRegionMode(LiveRegionMode.Polite)
                        and
                        hasDismissAction()
                        and
                        hasAnyChild(hasIsTraversalGroup())
            )
            .assertExists()

        // Verify that Snackbar has a Dismiss icon button.
        // contentDescription of the Snackbar Dismiss button is fixed (per language) by the system.
        composeTestRule
            .onNode(
                hasContentDescriptionExactly("Dismiss")
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()

        // Verify that Snackbar has no button other than the "Dismiss" button.
        composeTestRule
            .onNode(
                hasLiveRegionMode(LiveRegionMode.Polite)
                        and
                        hasDismissAction()
                        and
                        !hasAnyDescendant(
                            hasRole(Role.Button)
                                    and
                                    hasClickAction()
                                    and
                                    !hasContentDescriptionExactly("Dismiss")
                        )

            )
            .assertExists()

        // Move time forward and verify that Snackbar has NOT dismissed itself.
        composeTestRule.mainClock.advanceTimeBy(20000L)
        composeTestRule
            .onNode(
                hasLiveRegionMode(LiveRegionMode.Polite)
                        and
                        hasDismissAction()
                        and
                        hasAnyChild(hasIsTraversalGroup())
            )
            .assertExists()
    }

    @Test
    fun verifyExample5Dismisses() {
        // Press the example button.
        composeTestRule
            .onNodeWithTag(popupMessagesExample5ButtonTestTag)
            .performScrollTo()
            .performClick()

        // Press the Snackbar dismiss button.
        // Note: Can't performScrollTo() on this button -- the Snackbar is not a scrollable region.
        composeTestRule
            .onNodeWithContentDescription("Dismiss")
            .performClick()

        // Verify that Snackbar has been dismissed.
        composeTestRule
            .onNode(
                hasLiveRegionMode(LiveRegionMode.Polite)
                        and
                        hasDismissAction()
                        and
                        hasAnyChild(hasIsTraversalGroup())
            )
            .assertDoesNotExist()
    }

    @Test
    fun verifyExample6HasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample6ButtonTestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()

        // Verify that AlertDialog appears when example button is pressed.
        composeTestRule
            .onNodeWithTag(popupMessagesExample6ButtonTestTag)
            .performScrollTo()
            .performClick()

        // Verify AlertDialog exists.
        composeTestRule
            .onNodeWithTag(popupMessagesExample6AlertDialogTestTag)
            .assertExists()

        // Verify "Show more" AlertDialog does not exist.
        composeTestRule
            .onNodeWithTag(popupMessagesExample6ShowMoreAlertDialogTestTag)
            .assertDoesNotExist()

        // Verify action button exists.
        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample6AlertDialogActionButtonTestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()

        // Verify dismiss button exists.
        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample6AlertDialogDismissButtonTestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()
    }

    @Test
    fun verifyExample6ActionButtonShowsDialog() {
        // Press the example button.
        composeTestRule
            .onNodeWithTag(popupMessagesExample6ButtonTestTag)
            .performScrollTo()
            .performClick()

        // Verify AlertDialog does exist.
        composeTestRule
            .onNodeWithTag(popupMessagesExample6AlertDialogTestTag)
            .assertExists()

        // Press the AlertDialog action button.
        composeTestRule
            .onNodeWithTag(popupMessagesExample6AlertDialogActionButtonTestTag)
            .performClick()

        // Verify AlertDialog does not exist.
        composeTestRule
            .onNodeWithTag(popupMessagesExample6AlertDialogTestTag)
            .assertDoesNotExist()

        // Verify "Show more" AlertDialog does exist.
        composeTestRule
            .onNodeWithTag(popupMessagesExample6ShowMoreAlertDialogTestTag)
            .assertExists()

        // Verify dismiss button exists.
        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample6ShowMoreAlertDialogDismissButtonTestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()

        // Press the "Show more" AlertDialog dismiss button.
        composeTestRule
            .onNodeWithTag(popupMessagesExample6ShowMoreAlertDialogDismissButtonTestTag)
            .performClick()

        // Verify AlertDialog does not exist.
        composeTestRule
            .onNodeWithTag(popupMessagesExample6AlertDialogTestTag)
            .assertDoesNotExist()

        // Verify "Show more" AlertDialog does exist.
        composeTestRule
            .onNodeWithTag(popupMessagesExample6ShowMoreAlertDialogTestTag)
            .assertDoesNotExist()
    }

    @Test
    fun verifyExample6Dismisses() {
        // Press the example button.
        composeTestRule
            .onNodeWithTag(popupMessagesExample6ButtonTestTag)
            .performScrollTo()
            .performClick()

        // Verify AlertDialog does exist.
        composeTestRule
            .onNodeWithTag(popupMessagesExample6AlertDialogTestTag)
            .assertExists()

        // Press the AlertDialog dismiss button.
        composeTestRule
            .onNodeWithTag(popupMessagesExample6AlertDialogDismissButtonTestTag)
            .performClick()

        // Verify AlertDialog does not exist.
        composeTestRule
            .onNodeWithTag(popupMessagesExample6AlertDialogTestTag)
            .assertDoesNotExist()

        // Verify "Show more" AlertDialog does not exist.
        composeTestRule
            .onNodeWithTag(popupMessagesExample6ShowMoreAlertDialogTestTag)
            .assertDoesNotExist()
    }

    @Test
    fun verifyExample7HasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample7ButtonTestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()

        // Verify that message text changes exists with expected initial value.
        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample7MessageTextTestTag)
                        and
                        hasTextExactly("Counter: 0")
            )
            .assertExists()

        // Verify that message Clear button does not exist.
        composeTestRule
            .onNodeWithTag(popupMessagesExample7MessageTextButtonTestTag)
            .assertDoesNotExist()

        // Verify that message text changes when example button is pressed.
        composeTestRule
            .onNodeWithTag(popupMessagesExample7ButtonTestTag)
            .performScrollTo()
            .performClick()

        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample7MessageTextTestTag)
                        and
                        hasTextExactly("Counter: 1")
            )
            .assertExists()

        // Verify that message Clear button does exist.
        composeTestRule
            .onNodeWithTag(popupMessagesExample7MessageTextButtonTestTag)
            .assertExists()

        // Verify that message text changes when Clear button is pressed.
        composeTestRule
            .onNodeWithTag(popupMessagesExample7MessageTextButtonTestTag)
            .performScrollTo()
            .performClick()

        composeTestRule
            .onNode(
                hasTestTag(popupMessagesExample7MessageTextTestTag)
                        and
                        hasTextExactly("Counter: 0")
            )
            .assertExists()
    }
}