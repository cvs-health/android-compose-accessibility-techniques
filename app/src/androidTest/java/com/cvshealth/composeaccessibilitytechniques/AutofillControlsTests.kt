/*
   Copyright 2024-2025 CVS Health and/or one of its affiliates

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

import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.input.ImeAction
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.autofill_controls.AutofillControlsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.autofill_controls.autofillControlsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.autofill_controls.autofillControlsExample1TextField1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.autofill_controls.autofillControlsExample1TextField2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.autofill_controls.autofillControlsExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.autofill_controls.autofillControlsExample2TextField1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.autofill_controls.autofillControlsExample2TextField2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.autofill_controls.autofillControlsHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.genericScaffoldTitleTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * AutofillControlsTests verifies the semantic properties of the AutofillControlsScreen composable.
 */
class AutofillControlsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme {
                AutofillControlsScreen { }
            }
        }
    }

    @Test
    fun verifyScreenHasPaneTitle() {
        composeTestRule.onNode(hasPaneTitle()).assertExists()
    }

    @Test
    fun verifyHeadingsCount() {
        composeTestRule.onAllNodes(isHeading()).assertCountEquals(4)
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(genericScaffoldTitleTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(autofillControlsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(autofillControlsExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(autofillControlsExample2HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExampleTextsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(autofillControlsExample1TextField1TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(autofillControlsExample1TextField2TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(autofillControlsExample2TextField1TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(autofillControlsExample2TextField2TestTag)
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
    fun verifyExample1TextFieldsHaveExpectedInitialState() {
        composeTestRule
            .onNode(
                hasTestTag(autofillControlsExample1TextField1TestTag)
                        and
                        hasImeAction(ImeAction.Next)
                        and
                        !isError()
                        and
                        hasNoContentType()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(autofillControlsExample1TextField2TestTag)
                        and
                        hasImeAction(ImeAction.Next)
                        and
                        !isError()
                        and
                        hasNoContentType()
            )
            .assertExists()
    }

    @Test
    fun verifyExample2TextFieldsHaveExpectedInitialState() {
        composeTestRule
            .onNode(
                hasTestTag(autofillControlsExample2TextField1TestTag)
                        and
                        hasImeAction(ImeAction.Next)
                        and
                        !isError()
                        and
                        hasContentType(ContentType.PersonFullName)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(autofillControlsExample2TextField2TestTag)
                        and
                        hasImeAction(ImeAction.Next)
                        and
                        !isError()
                        and
                        hasContentType(ContentType.EmailAddress)
            )
            .assertExists()
    }

}