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

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasContentDescriptionExactly
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.slider_controls.SliderControlsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.slider_controls.sliderControlsExample1ControlTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.slider_controls.sliderControlsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.slider_controls.sliderControlsExample2ControlTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.slider_controls.sliderControlsExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.slider_controls.sliderControlsExample3ControlTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.slider_controls.sliderControlsExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.slider_controls.sliderControlsExample4ControlTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.slider_controls.sliderControlsExample4HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.slider_controls.sliderControlsHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SliderControlsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme {
                SliderControlsScreen { /* NO-OP */ }
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
            .onNode(hasTestTag(sliderControlsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(sliderControlsExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(sliderControlsExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(sliderControlsExample3HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(sliderControlsExample4HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyControlsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(sliderControlsExample1ControlTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(sliderControlsExample2ControlTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(sliderControlsExample3ControlTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(sliderControlsExample4ControlTestTag)
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
    fun verifyThatSlidersHaveExpectedRanges() {
        composeTestRule
            .onNode(
                hasTestTag(sliderControlsExample1ControlTestTag)
                        and
                        hasProgressBarRangeInfo(
                            ProgressBarRangeInfo(0f,0f..10f, 9)
                        )
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(sliderControlsExample2ControlTestTag)
                        and
                        hasProgressBarRangeInfo(
                            ProgressBarRangeInfo(0f,0f..10f, 9)
                        )
            )
            .assertExists()
    }

    @Test
    fun verifyThatRangeSliderHasExpectedRanges() {
        // Test Example 3, Range start thumb value
        composeTestRule
            .onNode(
                hasParent(hasTestTag(sliderControlsExample3ControlTestTag))
                        and
                        hasContentDescriptionExactly("Range start")
                        and
                        hasProgressBarRangeInfo(
                            ProgressBarRangeInfo(0f,0f..10f, 9)
                        )
            )
            .assertExists()

        // Test Example 3, Range end thumb value
        composeTestRule
            .onNode(
                hasParent(hasTestTag(sliderControlsExample3ControlTestTag))
                        and
                        hasContentDescriptionExactly("Range end")
                        and
                        hasProgressBarRangeInfo(
                            ProgressBarRangeInfo(10f,0f..10f, 9)
                        )
            )
            .assertExists()

        // Cannot verify values of Example 4's View-based RangeSlider, because the AndroidView
        // wrapper is opaque to Compose jUnit UI testing.
    }
}