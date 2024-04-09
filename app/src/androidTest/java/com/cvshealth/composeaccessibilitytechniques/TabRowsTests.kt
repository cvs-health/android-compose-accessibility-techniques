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
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasNoScrollAction
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasScrollToIndexAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.isNotSelected
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.tab_rows.TabRowsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.tab_rows.tabRowsEndSpacerTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.tab_rows.tabRowsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.tab_rows.tabRowsExample1TabContentTestTagBase
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.tab_rows.tabRowsExample1TabRowTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.tab_rows.tabRowsExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.tab_rows.tabRowsExample2TabContentTestTagBase
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.tab_rows.tabRowsExample2TabRowTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.tab_rows.tabRowsExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.tab_rows.tabRowsExample3TabContentTestTagBase
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.tab_rows.tabRowsExample3TabRowTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.tab_rows.tabRowsExample4HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.tab_rows.tabRowsExample4PagerTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.tab_rows.tabRowsExample4TabContentTestTagBase
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.tab_rows.tabRowsExample4TabRowTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.tab_rows.tabRowsHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TabRowsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme {
                TabRowsScreen { }
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
            .onNode(hasTestTag(tabRowsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(tabRowsExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(tabRowsExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(tabRowsExample3HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(tabRowsExample4HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExampleListItemsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(tabRowsExample1TabRowTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(tabRowsExample2TabRowTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(tabRowsExample3TabRowTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(tabRowsExample4TabRowTestTag)
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
    fun verifyFixedTabs1InitialState() {
        // find tab row and verify it is not horizontally scrollable
        composeTestRule.onNode(
            hasTestTag(tabRowsExample1TabRowTestTag) and hasNoScrollAction()
        )

        // find tabs in fixed tab row
        val tabs = composeTestRule.onAllNodes(
            hasParent(hasTestTag(tabRowsExample1TabRowTestTag))
        )

        // verify 5 tabs
        tabs.assertCountEquals(5)
        tabs.assertAll(hasRole(Role.Tab))

        // verify all tabs are displayed
        (0..4).forEach { tabIndex ->
            tabs[tabIndex].assertIsDisplayed()
        }

        // verify 1st tab selected and no other tab is selected
        tabs[0].assert(isSelected())
        (1..4).forEach { tabIndex ->
            tabs[tabIndex].assert(isNotSelected())
        }

        // verify 1st tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample1TabContentTestTagBase}0")
            .assertExists()
    }

    @Test
    fun verifyFixedTabs1ExpectedBehavior() {
        // find tabs in fixed tab row
        val tabs = composeTestRule.onAllNodes(hasParent(hasTestTag(tabRowsExample1TabRowTestTag)))

        // verify 1st tab selected and 2nd tab is not selected
        tabs[0].assert(isSelected())
        tabs[1].assert(isNotSelected())

        // verify 1st tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample1TabContentTestTagBase}0")
            .assertExists()

        // press 2nd tab
        tabs[1].performScrollTo().performClick()

        // verify 2nd tab selected and 1st tab is not selected
        tabs[1].assert(isSelected())
        tabs[0].assert(isNotSelected())

        // verify 2nd tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample1TabContentTestTagBase}1")
            .assertExists()

        // verify 5th tab not selected
        tabs[4].assert(isNotSelected())

        // press 5th tab
        tabs[4].performScrollTo().performClick()

        // verify 5th tab selected and 2nd tab is not selected
        tabs[4].assert(isSelected())
        tabs[1].assert(isNotSelected())

        // verify 5th tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample1TabContentTestTagBase}4")
            .assertExists()

        // press 1st tab
        tabs[0].performScrollTo().performClick()

        // verify 1st tab selected and 5th tab is not selected
        tabs[0].assert(isSelected())
        tabs[4].assert(isNotSelected())

        // verify 1st tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample1TabContentTestTagBase}0")
            .assertExists()
    }

    @Test
    fun verifyFixedTabs2InitialState() {
        // find tab row and verify it is not horizontally scrollable
        composeTestRule.onNode(
            hasTestTag(tabRowsExample2TabRowTestTag) and hasNoScrollAction()
        )

        // find tabs in fixed tab row
        val tabs = composeTestRule.onAllNodes(
            hasParent(hasTestTag(tabRowsExample2TabRowTestTag))
        )

        // verify 5 tabs
        tabs.assertCountEquals(5)
        tabs.assertAll(hasRole(Role.Tab))

        // scroll to content
        composeTestRule.onNodeWithTag("${tabRowsExample2TabContentTestTagBase}0").performScrollTo()

        // verify all tabs are displayed
        (0..4).forEach { tabIndex ->
            tabs[tabIndex].assertIsDisplayed()
        }

        // verify 1st tab selected and no other tab is selected
        tabs[0].assert(isSelected())
        (1..4).forEach { tabIndex ->
            tabs[tabIndex].assert(isNotSelected())
        }

        // verify 1st tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample2TabContentTestTagBase}0")
            .assertExists()
    }

    @Test
    fun verifyFixedTabs2ExpectedBehavior() {
        // find tabs in fixed tab row
        val tabs = composeTestRule.onAllNodes(hasParent(hasTestTag(tabRowsExample2TabRowTestTag)))

        // verify 1st tab selected and 2nd tab is not selected
        tabs[0].assert(isSelected())
        tabs[1].assert(isNotSelected())

        // verify 1st tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample2TabContentTestTagBase}0")
            .assertExists()

        // scroll to content
        composeTestRule.onNodeWithTag("${tabRowsExample2TabContentTestTagBase}0").performScrollTo()

        // press 2nd tab
        tabs[1].performScrollTo().performClick()

        // verify 2nd tab selected and 1st tab is not selected
        tabs[1].assert(isSelected())
        tabs[0].assert(isNotSelected())

        // verify 2nd tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample2TabContentTestTagBase}1")
            .assertExists()

        // verify 5th tab not selected
        tabs[4].assert(isNotSelected())

        // press 5th tab
        tabs[4].performScrollTo().performClick()

        // verify 5th tab selected and 2nd tab is not selected
        tabs[4].assert(isSelected())
        tabs[1].assert(isNotSelected())

        // verify 5th tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample2TabContentTestTagBase}4")
            .assertExists()

        // press 1st tab
        tabs[0].performScrollTo().performClick()

        // verify 1st tab selected and 5th tab is not selected
        tabs[0].assert(isSelected())
        tabs[4].assert(isNotSelected())

        // verify 1st tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample2TabContentTestTagBase}0")
            .assertExists()
    }

    @Test
    fun verifyScrollingTabsInitialState() {
        // find tab row and verify it is horizontally scrollable
        composeTestRule.onNode(
            hasTestTag(tabRowsExample3TabRowTestTag) and hasScrollAction()
        )

        // find tabs in scrollable tab row
        val tabs = composeTestRule.onAllNodes(
            hasAnyAncestor(hasTestTag(tabRowsExample3TabRowTestTag)) and hasRole(Role.Tab)
        )

        // verify 5 tabs
        tabs.assertCountEquals(5)

        // scroll to content
        composeTestRule.onNodeWithTag("${tabRowsExample3TabContentTestTagBase}0").performScrollTo()

        // verify that not all tabs are displayed
        tabs[0].performScrollTo().assertIsDisplayed()
        // Note: This test will fail if run in Landscape mode or on a tablet with enough room to
        // display the entire ScrollableTabRow.
        tabs[4].assertIsNotDisplayed()

        // verify 1st tab selected and no other tab is selected
        tabs[0].assert(isSelected())
        (1..4).forEach { tabIndex ->
            tabs[tabIndex].assert(isNotSelected())
        }

        // verify 1st tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample2TabContentTestTagBase}0")
            .assertExists()
    }

    @Test
    fun verifyScrollingTabsExpectedBehavior() {
        // find tabs in scrollable tab row
        val tabs = composeTestRule.onAllNodes(
            hasAnyAncestor(hasTestTag(tabRowsExample3TabRowTestTag)) and hasRole(Role.Tab)
        )

        // verify 1st tab selected and 2nd tab is not selected
        tabs[0].assert(isSelected())
        tabs[1].assert(isNotSelected())

        // verify 1st tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample3TabContentTestTagBase}0")
            .assertExists()

        // scroll to content
        composeTestRule.onNodeWithTag("${tabRowsExample3TabContentTestTagBase}0").performScrollTo()

        // press 2nd tab
        tabs[1].performScrollTo().performClick()

        // verify 2nd tab selected and 1st tab is not selected
        tabs[1].assert(isSelected())
        tabs[0].assert(isNotSelected())

        // verify 2nd tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample3TabContentTestTagBase}1")
            .assertExists()

        // verify 5th tab not selected
        tabs[4].assert(isNotSelected())

        // press 5th tab
        tabs[4].performScrollTo().performClick()

        // verify 5th tab selected and 2nd tab is not selected
        tabs[4].assert(isSelected())
        tabs[1].assert(isNotSelected())

        // verify 5th tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample3TabContentTestTagBase}4")
            .assertExists()

        // press 1st tab
        tabs[0].performScrollTo().performClick()

        // verify 1st tab selected and 5th tab is not selected
        tabs[0].assert(isSelected())
        tabs[4].assert(isNotSelected())

        // verify 1st tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample3TabContentTestTagBase}0")
            .assertExists()
    }

    @Test
    fun verifyPagedTabsInitialState() {
        // find tab row and verify it is not horizontally scrollable
        composeTestRule.onNode(
            hasTestTag(tabRowsExample4TabRowTestTag) and hasNoScrollAction()
        )

        // scroll to content
        composeTestRule.onNodeWithTag(tabRowsExample4TabRowTestTag).performScrollTo()

        // find tabs in fixed paged tab row
        val tabs = composeTestRule.onAllNodes(
            hasParent(hasTestTag(tabRowsExample4TabRowTestTag))
        )

        // verify 5 tabs
        tabs.assertCountEquals(5)
        tabs.assertAll(hasRole(Role.Tab))

        // verify first and last tabs are displayed
        tabs[0].assertIsDisplayed()
        tabs[4].assertIsDisplayed()

        // verify 1st tab selected and no other tab is selected
        tabs[0].assert(isSelected())
        (1..4).forEach { tabIndex ->
            tabs[tabIndex].assert(isNotSelected())
        }

        // find content pager and verify it is horizontally scrollable
        composeTestRule.onNode(
            hasTestTag(tabRowsExample4PagerTestTag) and hasScrollAction() and hasScrollToIndexAction()
        )

        // verify 1st tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample4TabContentTestTagBase}0")
            .assertExists()
    }

    @Test
    fun verifyPagedTabsExpectedBehavior() {
        // scroll past content
        composeTestRule.onNodeWithTag(tabRowsEndSpacerTestTag).performScrollTo()

        // find tabs in fixed paged tab row
        val tabs = composeTestRule.onAllNodes(hasParent(hasTestTag(tabRowsExample4TabRowTestTag)))

        // verify 1st tab selected and 2nd tab is not selected
        tabs[0].assert(isSelected())
        tabs[1].assert(isNotSelected())

        // verify 1st tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample4TabContentTestTagBase}0")
            .assertExists()

        // press 2nd tab
        tabs[1].performScrollTo().performClick()

        // verify 2nd tab selected and 1st tab is not selected
        tabs[1].assert(isSelected())
        tabs[0].assert(isNotSelected())

        // verify 2nd tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample4TabContentTestTagBase}1")
            .assertExists()

        // verify 5th tab not selected
        tabs[4].assert(isNotSelected())

        // press 5th tab
        tabs[4].performScrollTo().performClick()

        // verify 5th tab selected and 2nd tab is not selected
        tabs[4].assert(isSelected())
        tabs[1].assert(isNotSelected())

        // verify 5th tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample4TabContentTestTagBase}4")
            .assertExists()

        // press 1st tab
        tabs[0].performScrollTo().performClick()

        // verify 1st tab selected and 5th tab is not selected
        tabs[0].assert(isSelected())
        tabs[4].assert(isNotSelected())

        // verify 1st tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample4TabContentTestTagBase}0")
            .assertExists()

        // find content pager and verify it has 5 pages
        composeTestRule.onNode(
            hasTestTag(tabRowsExample4PagerTestTag)
        ).performScrollTo()

        // scroll content pager forward
        composeTestRule.onNode(
            hasTestTag(tabRowsExample4PagerTestTag)
        ).performScrollToIndex(1)

        // verify 2nd tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample4TabContentTestTagBase}1")
            .assertExists()

        // scroll content pager backwards
        composeTestRule.onNode(
            hasTestTag(tabRowsExample4PagerTestTag)
        ).performScrollToIndex(0)

        // verify 1st tab content shown
        composeTestRule
            .onNodeWithTag("${tabRowsExample4TabContentTestTagBase}0")
            .assertExists()
    }
}