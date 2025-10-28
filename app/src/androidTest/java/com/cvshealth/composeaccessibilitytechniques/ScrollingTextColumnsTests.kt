/*
   Copyright 2025 CVS Health and/or one of its affiliates

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

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.NativeKeyEvent
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performKeyPress
import androidx.compose.ui.test.requestFocus
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.genericScaffoldTitleTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.scrolling_columns.ScrollingTextColumnsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.scrolling_columns.scrollingTextColumnExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.scrolling_columns.scrollingTextColumnScreenHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.scrolling_columns.scrollingTextColumnTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 *  ScrollingTextColumnsTests - test scrolling text column screen semantics.
 */
class ScrollingTextColumnsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme {
                ScrollingTextColumnsScreen {}
            }
        }
    }

    @Test
    fun verifyScreenHasPaneTitle() {
        composeTestRule.onNode(hasPaneTitle()).assertExists()
    }

    @Test
    fun verifyHeadingsCount() {
        composeTestRule.onAllNodes(isHeading()).assertCountEquals(3)
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(genericScaffoldTitleTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(scrollingTextColumnScreenHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(scrollingTextColumnExample1HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyEveryHeadingsHasATestTag() {
        composeTestRule.onNode(hasNoTestTag() and isHeading()).assertDoesNotExist()
    }

    @Test
    fun verifyInitialScrollState() {
        composeTestRule.onNode(
            hasTestTag(scrollingTextColumnTestTag)
            and
            isNotVerticallyScrolled()
        ).assertExists()
    }

    @Test
    fun verifyPageDownKeyScrollsDown() {
        composeTestRule.onNode(
            hasTestTag(scrollingTextColumnTestTag)
            and
            isNotVerticallyScrolled()
        ).assertExists()

        // Focus on Column
        composeTestRule
            .onNodeWithTag(scrollingTextColumnTestTag)
            .requestFocus()

        // Page Down to scroll Column
        composeTestRule
            .onNodeWithTag(scrollingTextColumnTestTag)
            .performKeyPress(
                KeyEvent(
                    NativeKeyEvent(
                        0,
                        0,
                        NativeKeyEvent.ACTION_DOWN,
                        NativeKeyEvent.KEYCODE_PAGE_DOWN,
                        0,
                    )
                )
            )
        composeTestRule
            .onNodeWithTag(scrollingTextColumnTestTag)
            .performKeyPress(
                KeyEvent(
                    NativeKeyEvent(
                        0,
                        0,
                        NativeKeyEvent.ACTION_UP,
                        NativeKeyEvent.KEYCODE_PAGE_DOWN,
                        0,
                    )
                )
            )

        // Verify Column has scrolled down
        composeTestRule.onNode(
            hasTestTag(scrollingTextColumnTestTag)
            and
            isVerticallyScrolled()
        ).assertExists()
    }

    @Test
    fun verifyPageUpKeyScrollsUp() {
        composeTestRule.onNode(
            hasTestTag(scrollingTextColumnTestTag)
            and
            isNotVerticallyScrolled()
        ).assertExists()

        // Focus on Column
        composeTestRule
            .onNodeWithTag(scrollingTextColumnTestTag)
            .requestFocus()

        // Page Down to scroll Column
        composeTestRule
            .onNodeWithTag(scrollingTextColumnTestTag)
            .performKeyPress(
                KeyEvent(
                    NativeKeyEvent(
                        0,
                        0,
                        NativeKeyEvent.ACTION_DOWN,
                        NativeKeyEvent.KEYCODE_PAGE_DOWN,
                        0,
                    )
                )
            )
        composeTestRule
            .onNodeWithTag(scrollingTextColumnTestTag)
            .performKeyPress(
                KeyEvent(
                    NativeKeyEvent(
                        0,
                        0,
                        NativeKeyEvent.ACTION_UP,
                        NativeKeyEvent.KEYCODE_PAGE_DOWN,
                        0,
                    )
                )
            )

        // Verify Column has scrolled down
        composeTestRule.onNode(
            hasTestTag(scrollingTextColumnTestTag)
            and
            isVerticallyScrolled()
        ).assertExists()

        // Page Down to scroll Column
        composeTestRule
            .onNodeWithTag(scrollingTextColumnTestTag)
            .performKeyPress(
                KeyEvent(
                    NativeKeyEvent(
                        0,
                        0,
                        NativeKeyEvent.ACTION_DOWN,
                        NativeKeyEvent.KEYCODE_PAGE_UP,
                        0,
                    )
                )
            )
        composeTestRule
            .onNodeWithTag(scrollingTextColumnTestTag)
            .performKeyPress(
                KeyEvent(
                    NativeKeyEvent(
                        0,
                        0,
                        NativeKeyEvent.ACTION_UP,
                        NativeKeyEvent.KEYCODE_PAGE_UP,
                        0,
                    )
                )
            )

        // Verify Column has scrolled up to top
        composeTestRule.onNode(
            hasTestTag(scrollingTextColumnTestTag)
            and
            isNotVerticallyScrolled()
        ).assertExists()
    }

}