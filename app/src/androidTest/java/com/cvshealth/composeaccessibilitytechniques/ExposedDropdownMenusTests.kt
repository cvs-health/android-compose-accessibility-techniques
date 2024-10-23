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

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher.Companion.keyIsDefined
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.genericScaffoldTitleTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.exposed_dropdown_menus.ExposedDropdownMenusScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.exposed_dropdown_menus.exposedDropdownMenusExample1DropdownMenuBoxTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.exposed_dropdown_menus.exposedDropdownMenusExample1DropdownMenuTextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.exposed_dropdown_menus.exposedDropdownMenusExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.exposed_dropdown_menus.exposedDropdownMenusExample2AndroidViewTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.exposed_dropdown_menus.exposedDropdownMenusExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.exposed_dropdown_menus.exposedDropdownMenusHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// Note: Cannot test ExposedDropDownMenusScreen, Example 2, because AndroidView is opaque to Compose
// jUnit UI semantic testing. Only the AndroidView's existence and outward semantics can be verified.
class ExposedDropdownMenusTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme {
                ExposedDropdownMenusScreen { }
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
            .onNode(hasTestTag(exposedDropdownMenusHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(exposedDropdownMenusExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(exposedDropdownMenusExample2HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExampleListItemsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(exposedDropdownMenusExample1DropdownMenuBoxTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(exposedDropdownMenusExample2AndroidViewTestTag)
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
    fun verifyExample1ExposedDropdownMenuHasExpectedInitialState() {
        composeTestRule
            .onNodeWithTag(exposedDropdownMenusExample1DropdownMenuTextFieldTestTag)
            .assert(
                hasTextExactly("Payment type", "(No payment type selected.)")
            )
        composeTestRule
            .onNodeWithTag(exposedDropdownMenusExample1DropdownMenuTextFieldTestTag)
            .assert(
                hasClickAction()
            )
        composeTestRule
            .onNodeWithTag(exposedDropdownMenusExample1DropdownMenuTextFieldTestTag)
            .assert(
                hasRole(Role.DropdownList)
            )
    }

    @Test
    fun verifyExample1ExposedDropdownMenuHasExpectedMenuActions() {
        composeTestRule
            .onNodeWithTag(exposedDropdownMenusExample1DropdownMenuTextFieldTestTag)
            .performScrollTo()
            .performClick()

        composeTestRule
            .onNodeWithText("Check")
            .assert(
                hasAnyAncestor(keyIsDefined(SemanticsProperties.IsPopup))
            )
            .performClick()

        composeTestRule
            .onNodeWithTag(exposedDropdownMenusExample1DropdownMenuTextFieldTestTag)
            .assert(
                hasTextExactly("Payment type", "Check")
                and
                hasRole(Role.DropdownList)
            )
    }

}