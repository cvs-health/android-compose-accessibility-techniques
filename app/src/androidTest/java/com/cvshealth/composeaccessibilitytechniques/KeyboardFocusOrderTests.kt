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

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.genericScaffoldTitleTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_focus_order.KeyboardFocusOrderScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_focus_order.keyboardFocusOrderExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_focus_order.keyboardFocusOrderExample1LayoutTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_focus_order.keyboardFocusOrderExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_focus_order.keyboardFocusOrderExample2LayoutTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_focus_order.keyboardFocusOrderExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_focus_order.keyboardFocusOrderExample3LayoutTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_focus_order.keyboardFocusOrderExample4Button1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_focus_order.keyboardFocusOrderExample4Button2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_focus_order.keyboardFocusOrderExample4HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_focus_order.keyboardFocusOrderHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 *  KeyboardFocusOrderTests - test keyboard focus order screen semantics to the extent possible.
 *
 *  Key technique: At the time of writing, the only composable which allow isFocused() to be set and
 *  tested is TextField. As these demonstrations use Checkbox and Button, focus order and focus
 *  traps cannot be checked here. (When that changes, apply the techniques from
 *  InteractiveControlLabelsTests.verifyTextFieldFocusability() to test focus change on tabbing.)
 */
class KeyboardFocusOrderTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme {
                KeyboardFocusOrderScreen { }
            }
        }
    }

    @Test
    fun verifyScreenHasPaneTitle() {
        composeTestRule.onNode(hasPaneTitle()).assertExists()
    }

    @Test
    fun verifyHeadingsCount() {
        composeTestRule.onAllNodes(isHeading()).assertCountEquals(6)
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(genericScaffoldTitleTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardFocusOrderHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardFocusOrderExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardFocusOrderExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardFocusOrderExample3HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardFocusOrderExample4HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExample1ControlsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(keyboardFocusOrderExample1LayoutTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardFocusOrderExample2LayoutTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardFocusOrderExample3LayoutTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardFocusOrderExample4Button1TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardFocusOrderExample4Button2TestTag)
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

}