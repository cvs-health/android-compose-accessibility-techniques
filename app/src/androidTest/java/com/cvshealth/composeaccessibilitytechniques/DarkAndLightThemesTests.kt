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

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dark_and_light_themes.DarkAndLightThemesScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dark_and_light_themes.darkAndLightThemesHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * DarkAndLightThemesTests verifies the semantic properties of the DarkAndLightThemesScreen
 * composable to the extent possible. Theme colors and theme setting support are not semantic
 * properties, so only the screen heading and paneTitle existence can be verified.
 */
class DarkAndLightThemesTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme {
                DarkAndLightThemesScreen { }
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
            .onNode(hasTestTag(darkAndLightThemesHeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyOnlyTheOneHeadingIsAHeading() {
        composeTestRule
            .onNode(!hasTestTag(darkAndLightThemesHeadingTestTag) and isHeading())
            .assertDoesNotExist()
    }

    @Test
    fun verifyEveryHeadingsHasATestTag() {
        composeTestRule.onNode(hasNoTestTag() and isHeading()).assertDoesNotExist()
    }
}