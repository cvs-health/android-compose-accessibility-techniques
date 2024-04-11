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

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dropdown_menus.DropdownMenusScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dropdown_menus.dropdownMenusExample1ButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dropdown_menus.dropdownMenusExample1DropdownMenuTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dropdown_menus.dropdownMenusExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dropdown_menus.dropdownMenusExample1MenuItem1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dropdown_menus.dropdownMenusExample1MenuItem2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dropdown_menus.dropdownMenusExample1RowTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dropdown_menus.dropdownMenusExample2ButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dropdown_menus.dropdownMenusExample2CloseMenuItemTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dropdown_menus.dropdownMenusExample2DropdownMenuTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dropdown_menus.dropdownMenusExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dropdown_menus.dropdownMenusExample2MenuItem1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dropdown_menus.dropdownMenusExample2MenuItem2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dropdown_menus.dropdownMenusExample2RowTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dropdown_menus.dropdownMenusHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DropdownMenusTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme {
                DropdownMenusScreen { }
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
            .onNode(hasTestTag(dropdownMenusHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(dropdownMenusExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(dropdownMenusExample2HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExampleListItemsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(dropdownMenusExample1RowTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(dropdownMenusExample2RowTestTag)
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
        // The DropdownMenu composable passes its Modifier down inside a pop-up, so its testTag will
        // only be composed when the pop-up is expanded. Thus, these assertions verify that the
        // dropdown menu is closed.
        composeTestRule.onAllNodes(isRoot()).assertCountEquals(1)
        composeTestRule
            .onNodeWithTag(dropdownMenusExample1DropdownMenuTestTag)
            .assertDoesNotExist()
    }

    @Test
    fun verifyExample2ExposedDropdownMenuHasExpectedInitialState() {
        // The DropdownMenu composable passes its Modifier down inside a pop-up, so its testTag will
        // only be composed when the pop-up is expanded. Thus, these assertions verify that the
        // dropdown menu is closed.
        composeTestRule.onAllNodes(isRoot()).assertCountEquals(1)
        composeTestRule
            .onNodeWithTag(dropdownMenusExample2DropdownMenuTestTag)
            .assertDoesNotExist()
    }

    @Test
    fun verifyExample1DropdownMenuHasExpectedMenuActions() {
        // Verify that the dropdown menu is closed.
        composeTestRule.onAllNodes(isRoot()).assertCountEquals(1)

        // Open the dropdown menu.
        composeTestRule
            .onNodeWithTag(dropdownMenusExample1ButtonTestTag)
            .performScrollTo()
            .performClick()

        // Verify that the dropdown menu is expanded. A pop-up creates a second root node and the
        // DropdownMenu test tag within the pop-up will be composed.
        composeTestRule.onAllNodes(isRoot()).assertCountEquals(2)
        composeTestRule
            .onNodeWithTag(dropdownMenusExample1DropdownMenuTestTag)
            .assertExists()

        // Verify that the expected menu items are displayed.
        composeTestRule
            .onNodeWithTag(dropdownMenusExample1MenuItem1TestTag)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag(dropdownMenusExample1MenuItem2TestTag)
            .assertIsDisplayed()

        // Verify that the first menu item is in a pop-up and click it.
        composeTestRule
            .onNodeWithTag(dropdownMenusExample1MenuItem1TestTag)
            .assert(
                hasAnyAncestor(SemanticsMatcher.keyIsDefined(SemanticsProperties.IsPopup))
            )
            .performClick()

        // Note: Can't verify that the corresponding pop-up message is displayed.
        // Verify that the dropdown menu is closed.
        composeTestRule.onAllNodes(isRoot()).assertCountEquals(1)
        composeTestRule
            .onNodeWithTag(dropdownMenusExample1DropdownMenuTestTag)
            .assertDoesNotExist()
    }

    @Test
    fun verifyExample2DropdownMenuHasExpectedMenuActions() {
        // Verify that the dropdown menu is closed.
        composeTestRule.onAllNodes(isRoot()).assertCountEquals(1)

        // Open the dropdown menu.
        composeTestRule
            .onNodeWithTag(dropdownMenusExample2ButtonTestTag)
            .performScrollTo()
            .performClick()

        // Verify that the dropdown menu is expanded. A pop-up creates a second root node and the
        // DropdownMenu test tag within the pop-up will be composed.
        composeTestRule.onAllNodes(isRoot()).assertCountEquals(2)
        composeTestRule
            .onNodeWithTag(dropdownMenusExample2DropdownMenuTestTag)
            .assertExists()

        // Verify that the expected menu items are displayed.
        composeTestRule
            .onNodeWithTag(dropdownMenusExample2MenuItem1TestTag)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag(dropdownMenusExample2MenuItem2TestTag)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag(dropdownMenusExample2CloseMenuItemTestTag)
            .assertIsDisplayed()

        // Verify that the close menu item is in a pop-up and click it.
        composeTestRule
            .onNodeWithTag(dropdownMenusExample2CloseMenuItemTestTag)
            .assert(
                hasAnyAncestor(SemanticsMatcher.keyIsDefined(SemanticsProperties.IsPopup))
            )
            .performClick()

        // Note: Can't verify that the corresponding pop-up message is displayed.
        // Verify that the dropdown menu is closed.
        composeTestRule.onAllNodes(isRoot()).assertCountEquals(1)
        composeTestRule
            .onNodeWithTag(dropdownMenusExample2DropdownMenuTestTag)
            .assertDoesNotExist()
    }

}