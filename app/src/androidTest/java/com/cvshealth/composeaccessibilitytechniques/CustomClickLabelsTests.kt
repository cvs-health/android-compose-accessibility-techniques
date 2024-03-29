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

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_click_labels.CustomClickLabelsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_click_labels.customClickLabelsExample1CardTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_click_labels.customClickLabelsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_click_labels.customClickLabelsExample2CardTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_click_labels.customClickLabelsExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_click_labels.customClickLabelsExample3ButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_click_labels.customClickLabelsExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_click_labels.customClickLabelsHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 *  CustomClickLabelsTests - test StandAloneLinksScreen semantics to the extent possible.
 *
 *  Key technique: Test for onClickLabel existence and value. See TestHelpers.kt for details.
 */
class CustomClickLabelsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme() {
                CustomClickLabelsScreen { }
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
            .onNode(hasTestTag(customClickLabelsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(customClickLabelsExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(customClickLabelsExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(customClickLabelsExample3HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExampleListItemsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(customClickLabelsExample1CardTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(customClickLabelsExample2CardTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(customClickLabelsExample3ButtonTestTag)
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
    fun verifyExample1LinkTextHasExpectedSemantics() {
        composeTestRule
            .onNodeWithTag(customClickLabelsExample1CardTestTag)
            .assertHasClickAction()
        composeTestRule
            .onNode(
                hasTestTag(customClickLabelsExample1CardTestTag) and hasClickAction() and !hasClickActionLabel()
            )
            .assertExists()
    }

    @Test
    fun verifyExample2LinkTextHasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(customClickLabelsExample2CardTestTag)
                        and
                        hasClickAction()
                        and
                        hasClickActionLabelled("show details")
            )
            .assertExists()
    }

    @Test
    fun verifyExample3ButtonHasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(customClickLabelsExample3ButtonTestTag)
                        and
                        hasClickAction()
                        and
                        hasRole(Role.Button)
                        and
                        hasClickActionLabelled("display a pop-up message")
            )
            .assertExists()
    }

}