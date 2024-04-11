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

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.MainActivity
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.screen_and_pane_titles.screenAndPaneTitlesHeadingTestTag
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 *  ScreenAndPaneTitlesTests - test screen and pane titles screen semantics to the extent possible.
 */
class ScreenAndPaneTitlesTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        // Navigate from HomeScreen to AccordionScreen.
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.home_informative_content))
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNodeWithText(
                composeTestRule.activity.getString(R.string.screen_and_pane_titles_title)
            )
            .performScrollTo()
            .performClick()
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(screenAndPaneTitlesHeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyEveryHeadingsHasATestTag() {
        composeTestRule.onNode(hasNoTestTag() and isHeading()).assertDoesNotExist()
    }

    @Test
    fun verifyScreenPaneTitleSemantics() {
        composeTestRule.onNode(
            hasPaneTitle(composeTestRule.activity.getString(R.string.screen_and_pane_titles_title))
        )
    }
}