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

import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.isEnabled
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.isNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.UxChangeAnnouncementsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample1IncrementCounterTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample1LiveRegionTextTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample1ResetCounterTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample2ButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample2LiveRegionTextTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample3ButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample3ProgressIndicatorTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample4AlertTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample4ButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample4HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsHeadingTestTag
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * UxChangeAnnouncementsTests -- test UX change announcement semantics, to the extent possible.
 *
 * Note: Use of View.announceForAccessibility() cannot be verified in Compose jUnit UI tests, as it
 * is not a semantic state. Only semantic properties such as being a liveRegion or component
 * composition state, visibility, and value are verifiable.
 */
class UxChangeAnnouncementsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme {
                UxChangeAnnouncementsScreen { }
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
            .onNode(hasTestTag(uxChangeAnnouncementsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(uxChangeAnnouncementsExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(uxChangeAnnouncementsExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(uxChangeAnnouncementsExample3HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(uxChangeAnnouncementsExample4HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExampleTextFieldsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample1LiveRegionTextTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample1IncrementCounterTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample1ResetCounterTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample2ButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Example 2 button is not a heading, and LiveRegion text is not composed
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample2ButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNodeWithTag(uxChangeAnnouncementsExample2LiveRegionTextTestTag)
            .assertDoesNotExist()

        // Example 3 button is not a heading, and ProgressIndicator is not composed
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample3ButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNodeWithTag(uxChangeAnnouncementsExample3ProgressIndicatorTestTag)
            .assertDoesNotExist()

        // Example 4 button is not a heading, and AlertDialog is not composed
        composeTestRule
            .onNode(hasTestTag(uxChangeAnnouncementsExample4ButtonTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNodeWithTag(uxChangeAnnouncementsExample4AlertTestTag)
            .assertDoesNotExist()
    }

    @Test
    fun verifyEveryHeadingsHasATestTag() {
        composeTestRule.onNode(hasNoTestTag() and isHeading()).assertDoesNotExist()
    }

    @Test
    fun verifyExample1LiveRegionIsAPoliteLiveRegion() {
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample1LiveRegionTextTestTag)
                        and
                        hasLiveRegionMode(LiveRegionMode.Polite)
            )
            .assertExists()
    }

    @Test
    fun verifyExample1ButtonFunctionality() {
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample1LiveRegionTextTestTag)
                        and
                        hasTextExactly("Counter: 0")
            )
            .assertExists()
        composeTestRule.onNodeWithTag(uxChangeAnnouncementsExample1IncrementCounterTestTag)
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample1LiveRegionTextTestTag)
                        and
                        hasTextExactly("Counter: 1")
            )
            .assertExists()
        composeTestRule.onNodeWithTag(uxChangeAnnouncementsExample1ResetCounterTestTag)
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample1LiveRegionTextTestTag)
                        and
                        hasTextExactly("Counter: 0")
            )
            .assertExists()
    }

    @Test
    fun verifyExample2TextTogglesVisibility() {
        // Display Example 2 text
        composeTestRule
            .onNodeWithTag(uxChangeAnnouncementsExample2ButtonTestTag)
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNodeWithTag(uxChangeAnnouncementsExample2LiveRegionTextTestTag)
            .assertExists()
        composeTestRule
            .onNodeWithTag(uxChangeAnnouncementsExample2LiveRegionTextTestTag)
            .performScrollTo()
            .assertIsDisplayed()
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample2LiveRegionTextTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Hide text
        composeTestRule
            .onNodeWithTag(uxChangeAnnouncementsExample2ButtonTestTag)
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNodeWithTag(uxChangeAnnouncementsExample2LiveRegionTextTestTag)
            .assertDoesNotExist()
    }

    @Test
    fun verifyExample3DisplaysProgressIndicatorAndDisablesButtons() {
        // Verify starting condition that ProgressIndicator is not composed
        composeTestRule
            .onNodeWithTag(uxChangeAnnouncementsExample3ProgressIndicatorTestTag)
            .assertDoesNotExist()

        // Verify that all buttons are enabled
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample1IncrementCounterTestTag)
                        and
                        isEnabled()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample1ResetCounterTestTag)
                        and
                        isEnabled()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample2ButtonTestTag)
                        and
                        isEnabled()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample3ButtonTestTag)
                        and
                        isEnabled()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample4ButtonTestTag)
                        and
                        isEnabled()
            )
            .assertExists()

        // Display the progress indicator
        composeTestRule
            .onNodeWithTag(uxChangeAnnouncementsExample3ButtonTestTag)
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNodeWithTag(uxChangeAnnouncementsExample3ProgressIndicatorTestTag)
            .assertExists()

        // Verify that all buttons are disabled
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample1IncrementCounterTestTag)
                        and
                        isNotEnabled()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample1ResetCounterTestTag)
                        and
                        isNotEnabled()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample2ButtonTestTag)
                        and
                        isNotEnabled()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample3ButtonTestTag)
                        and
                        isNotEnabled()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample4ButtonTestTag)
                        and
                        isNotEnabled()
            )
            .assertExists()
    }

    @Test
    fun verifyExample4DisplaysAlertDialog() {
        // Verify starting condition that AlertDialog is not composed
        composeTestRule
            .onNodeWithTag(uxChangeAnnouncementsExample4AlertTestTag)
            .assertDoesNotExist()

        // Display the AlertDialog
        composeTestRule
            .onNodeWithTag(uxChangeAnnouncementsExample4ButtonTestTag)
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNodeWithTag(uxChangeAnnouncementsExample4AlertTestTag)
            .assertExists()
    }
}