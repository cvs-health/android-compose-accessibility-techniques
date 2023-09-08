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
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.switch_controls.SwitchControlsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.switch_controls.switchControlsExample1CheckboxTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.switch_controls.switchControlsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.switch_controls.switchControlsExample2CheckboxTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.switch_controls.switchControlsExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.switch_controls.switchControlsHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SwitchControlsTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme() {
                SwitchControlsScreen { }
            }
        }
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(switchControlsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(switchControlsExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(switchControlsExample2HeadingTestTag) and isHeading())
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
                hasTestTag(switchControlsExample1CheckboxTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(switchControlsExample2CheckboxTestTag)
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
            .onNode(hasTestTag(switchControlsExample1CheckboxTestTag) and !hasText())
            .assertExists()
    }

    @Test
    fun verifyGoodExample2HasText() {
        composeTestRule
            .onNode(hasTestTag(switchControlsExample2CheckboxTestTag) and hasText())
            .assertExists()
    }

    @Test
    fun verifyThatBadExample1CheckboxRowIsNotToggleable() {
        composeTestRule
            .onNode(hasTestTag(switchControlsExample1CheckboxTestTag) and !isToggleable())
            .assertExists()
    }

    @Test
    fun verifyThatGoodExample2CheckboxRowIsToggleable() {
        composeTestRule
            .onNodeWithTag(switchControlsExample2CheckboxTestTag)
            .assertIsToggleable()
    }

    @Test
    fun verifyThatCheckboxRowToggles() {
        composeTestRule
            .onNodeWithTag(switchControlsExample2CheckboxTestTag)
            .assertIsOff()
        composeTestRule
            .onNodeWithTag(switchControlsExample2CheckboxTestTag)
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNodeWithTag(switchControlsExample2CheckboxTestTag)
            .assertIsOn()
        composeTestRule
            .onNodeWithTag(switchControlsExample2CheckboxTestTag)
            .performClick()
        composeTestRule
            .onNodeWithTag(switchControlsExample2CheckboxTestTag)
            .assertIsOff()
    }

    @Test
    fun verifyThatFauxSwitchRowIsNotToggleable() {
        composeTestRule
            .onNode(hasTestTag(switchControlsExample1CheckboxTestTag) and !isToggleable())
            .assertExists()
    }
}