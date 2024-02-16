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

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.standalone_links.StandAloneLinksScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.standalone_links.standaloneLinksExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.standalone_links.standaloneLinksExample1LinkTextTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.standalone_links.standaloneLinksExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.standalone_links.standaloneLinksExample2LinkTextTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.standalone_links.standaloneLinksExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.standalone_links.standaloneLinksExample3LinkTextTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.standalone_links.standaloneLinksExample4HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.standalone_links.standaloneLinksExample4LinkButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.standalone_links.standaloneLinksHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 *  StandAloneLinksTests - test StandAloneLinksScreen semantics to the extent possible.
 *
 *  Key technique: Test for onClickLabel existence and value. See TestHelpers.kt for details.
 */
class StandAloneLinksTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme() {
                StandAloneLinksScreen { }
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
            .onNode(hasTestTag(standaloneLinksHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(standaloneLinksExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(standaloneLinksExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(standaloneLinksExample3HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(standaloneLinksExample4HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExampleListItemsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(standaloneLinksExample1LinkTextTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(standaloneLinksExample2LinkTextTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(standaloneLinksExample3LinkTextTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(standaloneLinksExample4LinkButtonTestTag)
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
            .onNodeWithTag(standaloneLinksExample1LinkTextTestTag)
            .assertHasClickAction()
        composeTestRule
            .onNode(
                hasTestTag(standaloneLinksExample1LinkTextTestTag) and hasClickAction() and !hasClickActionLabel()
            )
            .assertExists()
    }

    @Test
    fun verifyExample2LinkTextHasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(standaloneLinksExample2LinkTextTestTag)
                        and
                        hasClickAction()
                        and
                        hasClickActionLabelled("open in browser")
            )
            .assertExists()
    }

    @Test
    fun verifyExample3LinkTextHasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(standaloneLinksExample3LinkTextTestTag)
                        and
                        hasClickAction()
                        and
                        hasClickActionLabelled("open in browser")
            )
            .assertExists()
    }

    @Test
    fun verifyExample4LinkTextHasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(standaloneLinksExample4LinkButtonTestTag)
                        and
                        hasClickAction()
                        and
                        !hasClickActionLabel()
            )
            .assertExists()
    }
}