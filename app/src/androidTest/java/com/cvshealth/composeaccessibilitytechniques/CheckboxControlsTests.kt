/*
   Copyright 2023 CVS Health and/or one of its affiliates

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

import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.checkbox_controls.CheckboxControlsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.checkbox_controls.checkboxControlsExample1CheckboxTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.checkbox_controls.checkboxControlsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.checkbox_controls.checkboxControlsExample2CheckboxTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.checkbox_controls.checkboxControlsExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.checkbox_controls.checkboxControlsHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CheckboxControlsTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme() {
                CheckboxControlsScreen { }
            }
        }
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(checkboxControlsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(checkboxControlsExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(checkboxControlsExample2HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyEveryHeadingsHasATestTag() {
        composeTestRule.onNode(hasNoTestTag() and isHeading()).assertDoesNotExist()
    }

    @Test
    fun verifyExampleCheckboxControlsHaveNoHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(checkboxControlsExample1CheckboxTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(checkboxControlsExample2CheckboxTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
    }

    @Test
    fun verifyBadExample1HasNoText() {
        composeTestRule
            .onNode(hasTestTag(checkboxControlsExample1CheckboxTestTag) and !hasText())
            .assertExists()
    }

    @Test
    fun verifyGoodExample2HasText() {
        composeTestRule
            .onNode(hasTestTag(checkboxControlsExample2CheckboxTestTag) and hasText())
            .assertExists()
    }

    @Test
    fun verifyThatBadExample1CheckboxRowIsNotToggleable() {
        composeTestRule
            .onNode(hasTestTag(checkboxControlsExample1CheckboxTestTag) and !isToggleable())
            .assertExists()
    }

    @Test
    fun verifyThatGoodExample2CheckboxRowIsToggleable() {
        composeTestRule
            .onNodeWithTag(checkboxControlsExample2CheckboxTestTag)
            .assertIsToggleable()
    }

    @Test
    fun verifyThatCheckboxRowToggles() {
        composeTestRule
            .onNodeWithTag(checkboxControlsExample2CheckboxTestTag)
            .assertIsOff()
        composeTestRule
            .onNodeWithTag(checkboxControlsExample2CheckboxTestTag)
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNodeWithTag(checkboxControlsExample2CheckboxTestTag)
            .assertIsOn()
        composeTestRule
            .onNodeWithTag(checkboxControlsExample2CheckboxTestTag)
            .performClick()
        composeTestRule
            .onNodeWithTag(checkboxControlsExample2CheckboxTestTag)
            .assertIsOff()
    }

    @Test
    fun verifyThatFauxSwitchRowIsNotToggleable() {
        composeTestRule
            .onNode(hasTestTag(checkboxControlsExample1CheckboxTestTag) and !isToggleable())
            .assertExists()
    }
}