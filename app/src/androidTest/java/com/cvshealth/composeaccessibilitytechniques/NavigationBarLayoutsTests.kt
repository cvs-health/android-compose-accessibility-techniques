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
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.isNotSelected
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.navigationbar_layouts.NavigationBarLayoutsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.navigationbar_layouts.navigationBarLayoutsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.navigationbar_layouts.navigationBarLayoutsExample1NavigationBarTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.navigationbar_layouts.navigationBarLayoutsExample1NavigationHostTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.navigationbar_layouts.navigationBarLayoutsExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.navigationbar_layouts.navigationBarLayoutsExample2NavigationBarTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.navigationbar_layouts.navigationBarLayoutsExample2NavigationHostTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.navigationbar_layouts.navigationBarLayoutsExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.navigationbar_layouts.navigationBarLayoutsExample3NavigationBarTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.navigationbar_layouts.navigationBarLayoutsExample3NavigationHostTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.navigationbar_layouts.navigationBarLayoutsHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationBarLayoutsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme {
                NavigationBarLayoutsScreen { }
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
            .onNode(hasTestTag(navigationBarLayoutsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(navigationBarLayoutsExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(navigationBarLayoutsExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(navigationBarLayoutsExample3HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExampleListItemsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample1NavigationHostTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample1NavigationBarTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample2NavigationHostTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample2NavigationBarTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample3NavigationHostTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample3NavigationBarTestTag)
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
    fun verifyExample1InitialState() {
        // find NavigationBarItems, which are grandchildren of the NavigationBar
        val navBarItems = composeTestRule.onAllNodes(
            hasParent(hasParent(hasTestTag(navigationBarLayoutsExample1NavigationBarTestTag)))
        )

        // verify 4 NavigationBarItems
        navBarItems.assertCountEquals(4)
        navBarItems.assertAll(hasRole(Role.Tab))

        // verify all NavigationBarItems are displayed
        (0..3).forEach { index ->
            navBarItems[index].assertIsDisplayed()
        }

        // verify 1st NavigationBarItems selected and no other NavigationBarItems is selected
        navBarItems[0].assert(isSelected())
        (1..3).forEach { tabIndex ->
            navBarItems[tabIndex].assert(isNotSelected())
        }

        // Verify NavigationBarItems have correct labels
        // Note: Cannot verify that 1st NavigationBarItem text is ellipsized; that is a non-semantic
        // display issue.
        navBarItems[0].assert(hasTextExactly("Home: Long label example"))
        navBarItems[1].assert(hasTextExactly("Favorites"))
        navBarItems[2].assert(hasTextExactly("Notifications"))
        navBarItems[3].assert(hasTextExactly("Settings"))

        // verify 1st NavigationBarItem content shown
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample1NavigationHostTestTag)
                        and
                        hasAnyChild(hasTextExactly("Home: Long label example"))
                        and
                        hasAnyChild(
                            hasTextExactly(
                                "Home screen placeholder. Note how the NavigationBar item text is truncated (or not)."
                            )
                        )
            )
            .assertIsDisplayed()
    }

    @Test
    fun verifyExample1ExpectedBehavior() {
        // find NavigationBarItems, which are grandchildren of the NavigationBar
        val navBarItems = composeTestRule.onAllNodes(
            hasParent(hasParent(hasTestTag(navigationBarLayoutsExample1NavigationBarTestTag)))
        )

        // verify 1st NavigationBarItem selected and 2nd NavigationBarItem is not selected
        navBarItems[0].assert(isSelected())
        navBarItems[1].assert(isNotSelected())

        // Scroll to Example 1 NavigationBar
        navBarItems[0].performScrollTo()

        // verify 1st NavigationBarItem content shown
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample1NavigationHostTestTag)
                        and
                        hasAnyChild(hasTextExactly("Home: Long label example"))
                        and
                        hasAnyChild(
                            hasTextExactly(
                                "Home screen placeholder. Note how the NavigationBar item text is truncated (or not)."
                            )
                        )
            )
            .assertIsDisplayed()

        // press 2nd NavigationBarItem
        navBarItems[1].performScrollTo().performClick()

        // verify 2nd NavigationBarItem selected and 1st tab is not selected
        navBarItems[1].assert(isSelected())
        navBarItems[0].assert(isNotSelected())

        // verify 2nd NavigationBarItem content shown
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample1NavigationHostTestTag)
                        and
                        hasAnyChild(hasTextExactly("Favorites"))
                        and
                        hasAnyChild(hasTextExactly("Favorites screen placeholder."))
            )
            .assertIsDisplayed()

        // verify 4th NavigationBarItem not selected
        navBarItems[3].assert(isNotSelected())

        // press 4th NavigationBarItem
        navBarItems[3].performScrollTo().performClick()

        // verify 4th NavigationBarItem selected and 2nd NavigationBarItem is not selected
        navBarItems[3].assert(isSelected())
        navBarItems[1].assert(isNotSelected())

        // verify 4th NavigationBarItem content shown
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample1NavigationHostTestTag)
                        and
                        hasAnyChild(hasTextExactly("Settings"))
                        and
                        hasAnyChild(hasTextExactly("Settings screen placeholder."))
            )
            .assertIsDisplayed()

        // press 1st NavigationBarItem
        navBarItems[0].performScrollTo().performClick()

        // verify 1st NavigationBarItem selected and 4th NavigationBarItem is not selected
        navBarItems[0].assert(isSelected())
        navBarItems[3].assert(isNotSelected())

        // verify 1st NavigationBarItem content shown
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample1NavigationHostTestTag)
                        and
                        hasAnyChild(hasTextExactly("Home: Long label example"))
                        and
                        hasAnyChild(
                            hasTextExactly(
                                "Home screen placeholder. Note how the NavigationBar item text is truncated (or not)."
                            )
                        )
            )
            .assertIsDisplayed()
    }

    @Test
    fun verifyExample2InitialState() {
        // find NavigationBarItems, which are grandchildren of the NavigationBar
        val navBarItems = composeTestRule.onAllNodes(
            hasParent(hasParent(hasTestTag(navigationBarLayoutsExample2NavigationBarTestTag)))
        )

        // verify 4 NavigationBarItems
        navBarItems.assertCountEquals(4)
        navBarItems.assertAll(hasRole(Role.Tab))

        // Scroll to Example 2 NavigationBar
        navBarItems[0].performScrollTo()

        // verify all NavigationBarItems are displayed
        (0..3).forEach { index ->
            navBarItems[index].assertIsDisplayed()
        }

        // verify 1st NavigationBarItems selected and no other NavigationBarItems is selected
        navBarItems[0].assert(isSelected())
        (1..3).forEach { tabIndex ->
            navBarItems[tabIndex].assert(isNotSelected())
        }

        // Verify NavigationBarItems have correct labels
        // Note: Cannot verify that 1st NavigationBarItem text is not ellipsized; that is a
        // non-semantic display issue.
        navBarItems[0].assert(hasTextExactly("Home: Long label example"))
        navBarItems[1].assert(hasTextExactly("Favorites"))
        navBarItems[2].assert(hasTextExactly("Notifications"))
        navBarItems[3].assert(hasTextExactly("Settings"))

        // verify 1st NavigationBarItem content shown
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample2NavigationHostTestTag)
                        and
                        hasAnyChild(hasTextExactly("Home: Long label example"))
                        and
                        hasAnyChild(
                            hasTextExactly(
                                "Home screen placeholder. Note how the NavigationBar item text is truncated (or not)."
                            )
                        )
            )
            .assertIsDisplayed()
    }

    @Test
    fun verifyExample2ExpectedBehavior() {
        // find NavigationBarItems, which are grandchildren of the NavigationBar
        val navBarItems = composeTestRule.onAllNodes(
            hasParent(hasParent(hasTestTag(navigationBarLayoutsExample2NavigationBarTestTag)))
        )

        // verify 1st NavigationBarItem selected and 2nd NavigationBarItem is not selected
        navBarItems[0].assert(isSelected())
        navBarItems[1].assert(isNotSelected())

        // Scroll to Example 2 NavigationBar
        navBarItems[0].performScrollTo()

        // verify 1st NavigationBarItem content shown
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample2NavigationHostTestTag)
                        and
                        hasAnyChild(hasTextExactly("Home: Long label example"))
                        and
                        hasAnyChild(
                            hasTextExactly(
                                "Home screen placeholder. Note how the NavigationBar item text is truncated (or not)."
                            )
                        )
            )
            .assertIsDisplayed()

        // press 2nd NavigationBarItem
        navBarItems[1].performScrollTo().performClick()

        // verify 2nd NavigationBarItem selected and 1st tab is not selected
        navBarItems[1].assert(isSelected())
        navBarItems[0].assert(isNotSelected())

        // verify 2nd NavigationBarItem content shown
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample2NavigationHostTestTag)
                        and
                        hasAnyChild(hasTextExactly("Favorites"))
                        and
                        hasAnyChild(hasTextExactly("Favorites screen placeholder."))
            )
            .assertIsDisplayed()

        // verify 4th NavigationBarItem not selected
        navBarItems[3].assert(isNotSelected())

        // press 4th NavigationBarItem
        navBarItems[3].performScrollTo().performClick()

        // verify 4th NavigationBarItem selected and 2nd NavigationBarItem is not selected
        navBarItems[3].assert(isSelected())
        navBarItems[1].assert(isNotSelected())


        // verify 4th NavigationBarItem content shown
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample2NavigationHostTestTag)
                        and
                        hasAnyChild(hasTextExactly("Settings"))
                        and
                        hasAnyChild(hasTextExactly("Settings screen placeholder."))
            )
            .assertIsDisplayed()

        // press 1st NavigationBarItem
        navBarItems[0].performScrollTo().performClick()

        // verify 1st NavigationBarItem selected and 4th NavigationBarItem is not selected
        navBarItems[0].assert(isSelected())
        navBarItems[3].assert(isNotSelected())

        // verify 1st NavigationBarItem content shown
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample2NavigationHostTestTag)
                        and
                        hasAnyChild(hasTextExactly("Home: Long label example"))
                        and
                        hasAnyChild(
                            hasTextExactly(
                                "Home screen placeholder. Note how the NavigationBar item text is truncated (or not)."
                            )
                        )
            )
            .assertIsDisplayed()
    }

    @Test
    fun verifyExample3InitialState() {
        // find NavigationBarItems, which are grandchildren of the NavigationBar
        val navBarItems = composeTestRule.onAllNodes(
            hasParent(hasParent(hasTestTag(navigationBarLayoutsExample3NavigationBarTestTag)))
        )

        // verify 4 NavigationBarItems
        navBarItems.assertCountEquals(4)
        navBarItems.assertAll(hasRole(Role.Tab))

        // Scroll to Example 2 NavigationBar
        navBarItems[0].performScrollTo()

        // verify all NavigationBarItems are displayed
        (0..3).forEach { index ->
            navBarItems[index].assertIsDisplayed()
        }

        // verify 1st NavigationBarItems selected and no other NavigationBarItems is selected
        navBarItems[0].assert(isSelected())
        (1..3).forEach { tabIndex ->
            navBarItems[tabIndex].assert(isNotSelected())
        }

        // Verify NavigationBarItems have correct labels
        // Note: Cannot verify that 2nd-4th NavigationBarItem labels are not displayed; that is a
        // non-semantic display consideration.
        navBarItems[0].assert(hasTextExactly("Home: Long label example"))
        navBarItems[1].assert(hasTextExactly("Favorites"))
        navBarItems[2].assert(hasTextExactly("Notifications"))
        navBarItems[3].assert(hasTextExactly("Settings"))

        // verify 1st NavigationBarItem content shown
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample3NavigationHostTestTag)
                        and
                        hasAnyChild(hasTextExactly("Home: Long label example"))
                        and
                        hasAnyChild(
                            hasTextExactly(
                                "Home screen placeholder. Note how the NavigationBar item text is truncated (or not)."
                            )
                        )
            )
            .assertIsDisplayed()
    }

    @Test
    fun verifyExample3ExpectedBehavior() {
        // find NavigationBarItems, which are grandchildren of the NavigationBar
        val navBarItems = composeTestRule.onAllNodes(
            hasParent(hasParent(hasTestTag(navigationBarLayoutsExample3NavigationBarTestTag)))
        )

        // verify 1st NavigationBarItem selected and 2nd NavigationBarItem is not selected
        navBarItems[0].assert(isSelected())
        navBarItems[1].assert(isNotSelected())

        // Scroll to Example 2 NavigationBar
        navBarItems[0].performScrollTo()

        // verify 1st NavigationBarItem content shown
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample3NavigationHostTestTag)
                        and
                        hasAnyChild(hasTextExactly("Home: Long label example"))
                        and
                        hasAnyChild(
                            hasTextExactly(
                                "Home screen placeholder. Note how the NavigationBar item text is truncated (or not)."
                            )
                        )
            )
            .assertIsDisplayed()

        // press 2nd NavigationBarItem
        navBarItems[1].performScrollTo().performClick()

        // verify 2nd NavigationBarItem selected and 1st tab is not selected
        navBarItems[1].assert(isSelected())
        navBarItems[0].assert(isNotSelected())

        // verify 2nd NavigationBarItem content shown
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample3NavigationHostTestTag)
                        and
                        hasAnyChild(hasTextExactly("Favorites"))
                        and
                        hasAnyChild(hasTextExactly("Favorites screen placeholder."))
            )
            .assertIsDisplayed()

        // verify 4th NavigationBarItem not selected
        navBarItems[3].assert(isNotSelected())

        // press 4th NavigationBarItem
        navBarItems[3].performScrollTo().performClick()

        // verify 4th NavigationBarItem selected and 2nd NavigationBarItem is not selected
        navBarItems[3].assert(isSelected())
        navBarItems[1].assert(isNotSelected())


        // verify 4th NavigationBarItem content shown
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample3NavigationHostTestTag)
                        and
                        hasAnyChild(hasTextExactly("Settings"))
                        and
                        hasAnyChild(hasTextExactly("Settings screen placeholder."))
            )
            .assertIsDisplayed()

        // press 1st NavigationBarItem
        navBarItems[0].performScrollTo().performClick()

        // verify 1st NavigationBarItem selected and 4th NavigationBarItem is not selected
        navBarItems[0].assert(isSelected())
        navBarItems[3].assert(isNotSelected())

        // verify 1st NavigationBarItem content shown
        composeTestRule
            .onNode(
                hasTestTag(navigationBarLayoutsExample3NavigationHostTestTag)
                        and
                        hasAnyChild(hasTextExactly("Home: Long label example"))
                        and
                        hasAnyChild(
                            hasTextExactly(
                                "Home screen placeholder. Note how the NavigationBar item text is truncated (or not)."
                            )
                        )
            )
            .assertIsDisplayed()
    }

}