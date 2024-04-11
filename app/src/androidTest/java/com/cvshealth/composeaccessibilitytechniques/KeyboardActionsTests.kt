/*
   © Copyright 2023-2024 CVS Health and/or one of its affiliates. All rights reserved.

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

import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.input.ImeAction
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_actions.KeyboardActionsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_actions.keyboardActionsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_actions.keyboardActionsExample1TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_actions.keyboardActionsExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_actions.keyboardActionsExample2TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_actions.keyboardActionsExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_actions.keyboardActionsExample3TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_actions.keyboardActionsExample4HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_actions.keyboardActionsExample4TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_actions.keyboardActionsExample5HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_actions.keyboardActionsExample5TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_actions.keyboardActionsExample6HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_actions.keyboardActionsExample6TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_actions.keyboardActionsHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class KeyboardActionsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme {
                KeyboardActionsScreen { }
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
            .onNode(hasTestTag(keyboardActionsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardActionsExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardActionsExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardActionsExample3HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardActionsExample4HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardActionsExample5HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardActionsExample6HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExampleTextFieldsAreNotHeadings() {
        composeTestRule
            .onNode(hasTestTag(keyboardActionsExample1TextFieldTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardActionsExample2TextFieldTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardActionsExample3TextFieldTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardActionsExample4TextFieldTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardActionsExample5TextFieldTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardActionsExample6TextFieldTestTag) and !isHeading())
            .assertExists()
    }

    @Test
    fun verifyEveryHeadingsHasATestTag() {
        composeTestRule.onNode(hasNoTestTag() and isHeading()).assertDoesNotExist()
    }

    @Test
    fun verifyExampleTextFieldsHaveExpectedImeActions() {
        composeTestRule
            .onNode(
                hasTestTag(keyboardActionsExample1TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.None)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardActionsExample2TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Next)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardActionsExample3TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Done)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardActionsExample4TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Send)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardActionsExample5TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Search)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardActionsExample6TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Go)
            )
            .assertExists()
    }

}