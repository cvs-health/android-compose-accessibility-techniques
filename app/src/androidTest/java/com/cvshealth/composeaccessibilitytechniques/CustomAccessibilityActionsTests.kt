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

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_accessibility_actions.CustomAccessibilityActionsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_accessibility_actions.customAccessibilityActionsExample1CardTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_accessibility_actions.customAccessibilityActionsExample1LikeButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_accessibility_actions.customAccessibilityActionsExample1ReportButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_accessibility_actions.customAccessibilityActionsExample1ShareButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_accessibility_actions.customAccessibilityActionsExample2CardTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_accessibility_actions.customAccessibilityActionsExample3CardTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_accessibility_actions.customAccessibilityActionsHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CustomAccessibilityActionsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme {
                CustomAccessibilityActionsScreen { }
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
            .onNode(hasTestTag(customAccessibilityActionsHeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyNonHeadingsAreNotHeadings() {
        composeTestRule
            .onAllNodes(!hasTestTag(customAccessibilityActionsHeadingTestTag))
            .assertAll(!isHeading())
    }

    @Test
    fun verifyExample1HasExpectedClickActions() {
        // On the first example card, clickable() is applied to Column within the Card to avoid
        // TalkBack reading order issues. The test much be modified accordingly.
        composeTestRule.onNode(
            hasTestTag(customAccessibilityActionsExample1CardTestTag)
                    and
                    hasAnyChild(hasClickAction())
        ).assertExists()
        composeTestRule.onNode(
            hasTestTag(customAccessibilityActionsExample1LikeButtonTestTag)
                    and
                    hasClickAction()
        ).assertExists()
        composeTestRule.onNode(
            hasTestTag(customAccessibilityActionsExample1ShareButtonTestTag)
                    and
                    hasClickAction()
        ).assertExists()
        composeTestRule.onNode(
            hasTestTag(customAccessibilityActionsExample1ReportButtonTestTag)
                    and
                    hasClickAction()
        ).assertExists()
    }

    @Test
    fun verifyExample1HasNoCustomActions() {
        composeTestRule.onNode(
            hasTestTag(customAccessibilityActionsExample1CardTestTag)
                    and
                    hasNoCustomActions()
        ).assertExists()
    }

    @Test
    fun verifyExample2HasExpectedClickActions() {
        composeTestRule.onNode(
            hasTestTag(customAccessibilityActionsExample2CardTestTag)
                    and
                    hasClickAction()
        ).assertExists()
        composeTestRule.onNode(
            hasTestTag(customAccessibilityActionsExample2CardTestTag)
                    and
                    hasAnyDescendant(hasClickAction())
        ).assertDoesNotExist()
    }

    @Test
    fun verifyExample2HasExpectedCustomActions() {
        composeTestRule.onNode(
            hasTestTag(customAccessibilityActionsExample2CardTestTag)
                    and
                    hasCustomActions()
                    and
                    hasCustomActionLabelled("See details")
                    and
                    hasCustomActionLabelled("Like this post")
                    and
                    hasCustomActionLabelled("Share this post")
                    and
                    hasCustomActionLabelled("Report this post as inappropriate")
        ).assertExists()
    }

    @Test
    fun verifyExample3HasExpectedClickActions() {
        composeTestRule.onNode(
            hasTestTag(customAccessibilityActionsExample3CardTestTag)
                    and
                    hasClickAction()
        ).assertExists()
        composeTestRule.onNode(
            hasTestTag(customAccessibilityActionsExample3CardTestTag)
                    and
                    hasAnyDescendant(hasClickAction())
        ).assertDoesNotExist()
    }

    @Test
    fun verifyExample3HasExpectedCustomActions() {
        composeTestRule.onNode(
            hasTestTag(customAccessibilityActionsExample3CardTestTag)
                    and
                    hasCustomActions()
                    and
                    hasCustomActionLabelled("See details")
                    and
                    hasCustomActionLabelled("Like this post")
                    and
                    hasCustomActionLabelled("Share this post")
                    and
                    hasCustomActionLabelled("Report this post as inappropriate")
        ).assertExists()
    }
}