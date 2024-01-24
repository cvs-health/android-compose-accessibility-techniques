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

import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_state_descriptions.CustomStateDescriptionsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_state_descriptions.customStateDescriptionsExample1CheckboxTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_state_descriptions.customStateDescriptionsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_state_descriptions.customStateDescriptionsExample2CheckboxTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_state_descriptions.customStateDescriptionsExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_state_descriptions.customStateDescriptionsExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_state_descriptions.customStateDescriptionsExample3SwitchTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_state_descriptions.customStateDescriptionsExample4HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_state_descriptions.customStateDescriptionsExample4SwitchTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_state_descriptions.customStateDescriptionsHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CustomStateDescriptionsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme() {
                CustomStateDescriptionsScreen { }
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
            .onNode(hasTestTag(customStateDescriptionsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(customStateDescriptionsExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(customStateDescriptionsExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(customStateDescriptionsExample3HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(customStateDescriptionsExample4HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExampleTextFieldsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(customStateDescriptionsExample1CheckboxTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(customStateDescriptionsExample2CheckboxTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(customStateDescriptionsExample3SwitchTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(customStateDescriptionsExample4SwitchTestTag)
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
    fun verifyExample1CheckboxHasDefaultStateDescription() {
        composeTestRule
            .onNode(
                hasTestTag(customStateDescriptionsExample1CheckboxTestTag)
                        and
                        hasNoStateDescription()
            )
            .assertExists()
    }

    @Test
    fun verifyExample2CheckboxHasCustomStateDescriptions() {
        composeTestRule
            .onNode(
                hasTestTag(customStateDescriptionsExample2CheckboxTestTag)
                        and
                        hasStateDescription("Deactivated")
            )
            .assertExists()
        composeTestRule
            .onNodeWithTag(customStateDescriptionsExample2CheckboxTestTag)
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNode(
                hasTestTag(customStateDescriptionsExample2CheckboxTestTag)
                        and
                        hasStateDescription("Activated")
            )
            .assertExists()
        composeTestRule
            .onNodeWithTag(customStateDescriptionsExample2CheckboxTestTag)
            .performClick()
        composeTestRule
            .onNode(
                hasTestTag(customStateDescriptionsExample2CheckboxTestTag)
                        and
                        hasStateDescription("Deactivated")
            )
            .assertExists()
    }

    @Test
    fun verifyExample3SwitchHasDefaultStateDescription() {
        composeTestRule
            .onNode(
                hasTestTag(customStateDescriptionsExample3SwitchTestTag)
                        and
                        hasNoStateDescription()
            )
            .assertExists()
    }

    @Test
    fun verifyExample4SwitchHasCustomStateDescriptions() {
        composeTestRule
            .onNode(
                hasTestTag(customStateDescriptionsExample4SwitchTestTag)
                        and
                        hasStateDescription("Lowered")
            )
            .assertExists()
        composeTestRule
            .onNodeWithTag(customStateDescriptionsExample4SwitchTestTag)
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNode(
                hasTestTag(customStateDescriptionsExample4SwitchTestTag)
                        and
                        hasStateDescription("Raised")
            )
            .assertExists()
        composeTestRule
            .onNodeWithTag(customStateDescriptionsExample4SwitchTestTag)
            .performClick()
        composeTestRule
            .onNode(
                hasTestTag(customStateDescriptionsExample4SwitchTestTag)
                        and
                        hasStateDescription("Lowered")
            )
            .assertExists()
    }

}