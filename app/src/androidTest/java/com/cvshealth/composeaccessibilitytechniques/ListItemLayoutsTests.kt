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
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescriptionExactly
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.ListItemLayoutsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample1ListItemTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample2ListItemTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample3ListItem1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample3ListItem2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample4HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample4ListItemTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ListItemLayoutsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme {
                ListItemLayoutsScreen { }
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
            .onNode(hasTestTag(listItemLayoutsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(listItemLayoutsExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(listItemLayoutsExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(listItemLayoutsExample3HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(listItemLayoutsExample4HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExampleListItemsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample1ListItemTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample2ListItemTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample3ListItem1TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample3ListItem2TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample4ListItemTestTag)
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
    fun verifyExample1ListItemHasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample1ListItemTestTag)
                        and
                        hasNoClickAction()
                        and
                        isTraversalGroup()
                        and
                        hasTextExactly("Accessible inactive ListItem", "Announces as a single text.")
                        and
                        hasContentDescriptionExactly()
            )
            .assertExists()
    }

    @Test
    fun verifyExample2ListItemHasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample2ListItemTestTag)
                        and
                        hasClickAction()
                        and
                        isTraversalGroup()
                        and
                        hasRole(Role.Button)
                        and
                        hasTextExactly("Accessible clickable ListItem", "Announces as a single labeled, clickable Button.")
                        and
                        hasContentDescriptionExactly()
            )
            .assertExists()
    }

    @Test
    fun verifyExample3ListItemsHaveExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample3ListItem1TestTag)
                        and
                        isSelectable()
                        and
                        isSelected()
                        and
                        isTraversalGroup()
                        and
                        hasRole(Role.RadioButton)
                        and
                        hasTextExactly("Accessible selectable ListItem 1", "Announces as the first of two labeled, selectable radio buttons.")
                        and
                        hasContentDescriptionExactly()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample3ListItem2TestTag)
                        and
                        isSelectable()
                        and
                        !isSelected()
                        and
                        isTraversalGroup()
                        and
                        hasRole(Role.RadioButton)
                        and
                        hasTextExactly("Accessible selectable ListItem 2", "Announces as the second of two labeled, selectable radio buttons.")
                        and
                        hasContentDescriptionExactly()
            )
            .assertExists()

        // Click on unselected ListItem changes isSelected state of both ListItems
        composeTestRule
            .onNodeWithTag(listItemLayoutsExample3ListItem2TestTag)
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample3ListItem1TestTag)
                        and
                        !isSelected()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample3ListItem2TestTag)
                        and
                        isSelected()
            )
            .assertExists()
    }

    @Test
    fun verifyExample4ListItemHasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample4ListItemTestTag)
                        and
                        isToggleable()
                        and
                        isTraversalGroup()
                        and
                        hasRole(Role.Switch)
                        and
                        hasTextExactly("Accessible toggleable ListItem", "Announces as a single labeled, toggleable Switch.")
                        and
                        hasContentDescriptionExactly()
            )
            .assertExists()
    }

}