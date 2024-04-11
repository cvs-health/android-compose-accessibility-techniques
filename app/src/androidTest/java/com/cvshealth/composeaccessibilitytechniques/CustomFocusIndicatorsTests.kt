/*
   © Copyright 2024 CVS Health and/or one of its affiliates. All rights reserved.

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
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_focus_indicators.CustomFocusIndicatorsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_focus_indicators.customFocusIndicatorsExample1ButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_focus_indicators.customFocusIndicatorsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_focus_indicators.customFocusIndicatorsExample2ButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_focus_indicators.customFocusIndicatorsExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_focus_indicators.customFocusIndicatorsExample3ButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_focus_indicators.customFocusIndicatorsExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_focus_indicators.customFocusIndicatorsExample4ButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_focus_indicators.customFocusIndicatorsExample4HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_focus_indicators.customFocusIndicatorsExample5CardTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_focus_indicators.customFocusIndicatorsExample5HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_focus_indicators.customFocusIndicatorsExample6CardTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_focus_indicators.customFocusIndicatorsExample6HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_focus_indicators.customFocusIndicatorsExample7CardTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_focus_indicators.customFocusIndicatorsExample7HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_focus_indicators.customFocusIndicatorsHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 *  CustomFocusIndicatorTests - test custom keyboard focus indicator screen semantics to the extent
 *  possible. Since focus indicators are a visual effect, not a semantic effect, and (at the time of
 *  writing) even the semantic state of isFocused() is untestable for most composables, the scope of
 *  these tests is limited to other properties, such as headings.
 */
class CustomFocusIndicatorsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme {
                CustomFocusIndicatorsScreen { }
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
            .onNode(hasTestTag(customFocusIndicatorsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(customFocusIndicatorsExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(customFocusIndicatorsExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(customFocusIndicatorsExample3HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(customFocusIndicatorsExample4HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(customFocusIndicatorsExample5HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(customFocusIndicatorsExample6HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(customFocusIndicatorsExample7HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExample1ControlsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(customFocusIndicatorsExample1ButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(customFocusIndicatorsExample2ButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(customFocusIndicatorsExample3ButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(customFocusIndicatorsExample4ButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(customFocusIndicatorsExample5CardTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(customFocusIndicatorsExample6CardTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(customFocusIndicatorsExample7CardTestTag)
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
    fun verifyAllExamplesAreClickable() {
        composeTestRule
            .onNode(
                hasTestTag(customFocusIndicatorsExample1ButtonTestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(customFocusIndicatorsExample2ButtonTestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(customFocusIndicatorsExample3ButtonTestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(customFocusIndicatorsExample4ButtonTestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(customFocusIndicatorsExample5CardTestTag)
                        and
                        !hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(customFocusIndicatorsExample6CardTestTag)
                        and
                        !hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(customFocusIndicatorsExample7CardTestTag)
                        and
                        !hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()
    }
}