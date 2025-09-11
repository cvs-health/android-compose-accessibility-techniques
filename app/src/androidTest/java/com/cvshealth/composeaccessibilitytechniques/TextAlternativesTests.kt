/*
   Copyright 2023-2025 CVS Health and/or one of its affiliates

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

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasContentDescriptionExactly
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.MainActivity
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.genericScaffoldTitleTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample10HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample10Icon1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample10Icon2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample10TextTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample11HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample11Icon1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample11Icon2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample11TextTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample12GroupTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample12HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample13GroupTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample13HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample14GroupTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample14HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample1UngroupedTextAndIcon1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample1UngroupedTextAndIcon2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample2UngroupedTextAndIcon1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample2UngroupedTextAndIcon2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample3GroupedTextAndIcon1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample3GroupedTextAndIcon2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample4GroupedTextAndIcon1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample4GroupedTextAndIcon2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample4HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample5GroupedTextAndIcon1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample5GroupedTextAndIcon2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample5HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample6HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample6IconButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample7HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample7IconButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample8HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample8IconButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample9HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample9Icon1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample9Icon2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesExample9TextTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.textAlternativesHeadingTestTag
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TextAlternativesTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        // Navigate from HomeScreen to TextAlternativesScreen.
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.home_informative_content))
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.text_alternatives_title))
            .performScrollTo()
            .performClick()
    }

    @Test
    fun verifyScreenHasPaneTitle() {
        composeTestRule.onNode(
            hasPaneTitle(composeTestRule.activity.getString(R.string.text_alternatives_title))
        ).assertExists()
    }

    @Test
    fun verifyHeadingsCount() {
        composeTestRule.onAllNodes(isHeading()).assertCountEquals(16)
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(genericScaffoldTitleTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(textAlternativesHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(textAlternativesExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(textAlternativesExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(textAlternativesExample3HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(textAlternativesExample4HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(textAlternativesExample5HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(textAlternativesExample6HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(textAlternativesExample7HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(textAlternativesExample8HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(textAlternativesExample9HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(textAlternativesExample10HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(textAlternativesExample11HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(textAlternativesExample12HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(textAlternativesExample13HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(textAlternativesExample14HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyNonHeadingsAreNotHeadings() {
        // Introduction section
        composeTestRule
            .onNode(
                hasTextExactly(
                    composeTestRule.activity.getString(
                        R.string.text_alternatives_description_1
                    )
                ) and !isHeading()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTextExactly(
                    composeTestRule.activity.getString(
                        R.string.text_alternatives_description_2
                    )
                ) and !isHeading()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTextExactly(
                    composeTestRule.activity.getString(
                        R.string.text_alternatives_description_3
                    )
                ) and !isHeading()
            )
            .assertExists()

        // Example 1
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample1UngroupedTextAndIcon1TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample1UngroupedTextAndIcon2TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Example 2
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample2UngroupedTextAndIcon1TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample2UngroupedTextAndIcon2TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Example 3
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample3GroupedTextAndIcon1TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample3GroupedTextAndIcon2TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Example 4
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample4GroupedTextAndIcon1TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample4GroupedTextAndIcon2TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTextExactly(
                    composeTestRule.activity.getString(
                        R.string.text_alternatives_example_4_note
                    )
                ) and !isHeading()
            )
            .assertExists()

        // Example 5
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample5GroupedTextAndIcon1TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample5GroupedTextAndIcon2TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Example 6
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample6IconButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTextExactly(
                    composeTestRule.activity.getString(
                        R.string.text_alternatives_example_6_note
                    )
                ) and !isHeading()
            )
            .assertExists()

        // Example 7
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample7IconButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Example 8
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample8IconButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Example 9
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample9Icon1TestTag)
                        and
                        !isHeading()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample9TextTestTag)
                        and
                        !isHeading()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample9Icon2TestTag)
                        and
                        !isHeading()
            )
            .assertExists()

        // Example 10
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample10Icon1TestTag)
                        and
                        !isHeading()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample10TextTestTag)
                        and
                        !isHeading()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample10Icon2TestTag)
                        and
                        !isHeading()
            )
            .assertExists()

        // Example 11
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample11Icon1TestTag)
                        and
                        !isHeading()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample11TextTestTag)
                        and
                        !isHeading()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample11Icon2TestTag)
                        and
                        !isHeading()
            )
            .assertExists()

        // Example 12
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample12GroupTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Example 13
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample13GroupTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Example 14
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample14GroupTestTag)
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
    fun verifyExample1UngroupedTextAndIconsHaveTextAndEmptyContentDescription() {
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample1UngroupedTextAndIcon1TestTag)
                        and
                        hasAnyChild(
                            hasTextExactly(
                                composeTestRule.activity.getString(
                                    R.string.text_alternatives_example_sunrise_time
                                )
                            )
                        )
                        and
                        hasAnyChild(hasContentDescriptionExactly(""))
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample1UngroupedTextAndIcon2TestTag)
                        and
                        hasAnyChild(
                            hasTextExactly(
                                composeTestRule.activity.getString(
                                    R.string.text_alternatives_example_sunset_time
                                )
                            )
                        )
                        and
                        hasAnyChild(hasContentDescriptionExactly(""))
            )
            .assertExists()
    }

    @Test
    fun verifyExample2UngroupedTextAndIconsHaveExpectedTextAndContentDescription() {
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample2UngroupedTextAndIcon1TestTag)
                        and
                        hasAnyChild(
                            hasTextExactly(
                                composeTestRule.activity.getString(
                                    R.string.text_alternatives_example_sunrise_time
                                )
                            )
                        )
                        and
                        hasAnyChild(
                            hasContentDescriptionExactly(
                                composeTestRule.activity.getString(
                                    R.string.text_alternatives_example_sunrise_description
                                )
                            )
                        )
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample2UngroupedTextAndIcon2TestTag)
                        and
                        hasAnyChild(
                            hasTextExactly(
                                composeTestRule.activity.getString(
                                    R.string.text_alternatives_example_sunset_time
                                )
                            )
                        )
                        and
                        hasAnyChild(
                            hasContentDescriptionExactly(
                                composeTestRule.activity.getString(
                                    R.string.text_alternatives_example_sunset_description
                                )
                            )
                        )
            )
            .assertExists()
    }

    @Test
    fun verifyExample3GroupedTextAndIconsHaveExpectedTextAndContentDescription() {
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample3GroupedTextAndIcon1TestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.text_alternatives_example_sunrise_time
                            ))
                        and
                        hasContentDescriptionExactly(
                            composeTestRule.activity.getString(
                                R.string.text_alternatives_example_sunrise_description
                            )
                        )
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample3GroupedTextAndIcon2TestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.text_alternatives_example_sunset_time
                            )
                        )
                        and
                        hasContentDescriptionExactly(
                            composeTestRule.activity.getString(
                                R.string.text_alternatives_example_sunset_description
                            )
                        )
            )
            .assertExists()
    }

    @Test
    fun verifyExample4GroupedTextAndIconAndTextsHaveExpectedTextAndContentDescription() {
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample4GroupedTextAndIcon1TestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.text_alternatives_example_sunrise_time
                            ),
                            composeTestRule.activity.getString(
                                R.string.text_alternatives_example_sunrise_description
                            )
                        )
                        and
                        hasContentDescriptionExactly()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample4GroupedTextAndIcon2TestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.text_alternatives_example_sunset_time
                            ),
                            composeTestRule.activity.getString(
                                R.string.text_alternatives_example_sunset_description
                            )
                        )
                        and
                        hasContentDescriptionExactly()
            )
            .assertExists()
    }

    @Test
    fun verifyExample5GroupedTextAndIconsHaveExpectedContentDescription() {
        // It is hard to write automated tests that distinguish between texts suppressed with
        // hideFromAccessibility() and not those which are not: the merged semantics nodes do have
        // text, so calling hasTextExactly() with no arguments on the merged semantics tree will fail.
        // TalkBack just doesn't announce those texts, because that are marked hideFromAccessibility().
        // The current test on the unmerged tree works around that problem by looking for a node
        // with the expected text and verifying that it is hideFromAccessibility.
        //
        // Testing would be simpler for the clearAndSetSemantics{} technique: just use
        // hasTextExactly() with no argument on the merged semantics tree.
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample5GroupedTextAndIcon1TestTag)
                        and
                        hasAnyDescendant(
                            hasTextExactly(
                                composeTestRule.activity.getString(
                                    R.string.text_alternatives_example_sunrise_time
                                )
                            ) and isHiddenFromAccessibility()
                        )
                        and
                        hasContentDescriptionExactly(
                            composeTestRule.activity.getString(
                                R.string.text_alternatives_example_5_grouped_sunrise_text
                            )
                        ),
                useUnmergedTree = true
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample5GroupedTextAndIcon2TestTag)
                        and
                        hasAnyDescendant(
                            hasTextExactly(
                                composeTestRule.activity.getString(
                                    R.string.text_alternatives_example_sunset_time
                                )
                            ) and isHiddenFromAccessibility()
                        )
                        and
                        hasContentDescriptionExactly(
                            composeTestRule.activity.getString(
                                R.string.text_alternatives_example_5_grouped_sunset_text
                            )
                        ),
                useUnmergedTree = true
            )
            .assertExists()
    }

    @Test
    fun verifyExample6IconButtonHasEmptyContentDescription() {
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample6IconButtonTestTag)
                        and
                        hasContentDescriptionExactly("")
            )
            .assertExists()
    }

    @Test
    fun verifyExample7IconButtonHasNullContentDescription() {
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample7IconButtonTestTag)
                        and
                        hasContentDescriptionExactly()
            )
            .assertExists()
    }

    @Test
    fun verifyExample8IconButtonHasExpectedContentDescription() {
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample8IconButtonTestTag)
                        and
                        hasContentDescriptionExactly(
                            composeTestRule.activity.getString(
                                R.string.text_alternatives_example_8_content_description
                            )
                        )
            )
            .assertExists()
    }

    @Test
    fun verifyExample9UngroupedTextAndDecorativeIconsHaveTextAndEmptyContentDescription() {
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample9Icon1TestTag)
                        and
                        hasTextExactly()
                        and
                        hasContentDescriptionExactly("")
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample9TextTestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.text_alternatives_example_9_decorated_text
                            )
                        )
                        and
                        hasContentDescriptionExactly()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample9Icon2TestTag)
                        and
                        hasTextExactly()
                        and
                        hasContentDescriptionExactly("")
            )
            .assertExists()
    }

    @Test
    fun verifyExample10UngroupedTextAndDecorativeIconsHaveTextAndNonEmptyContentDescription() {
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample10Icon1TestTag)
                        and
                        hasTextExactly()
                        and
                        hasContentDescriptionExactly(
                            composeTestRule.activity.getString(
                                R.string.text_alternatives_example_10_content_description
                            )
                        )
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample10TextTestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.text_alternatives_example_10_decorated_text
                            )
                        )
                        and
                        hasContentDescriptionExactly()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample10Icon2TestTag)
                        and
                        hasTextExactly()
                        and
                        hasContentDescriptionExactly(
                            composeTestRule.activity.getString(
                                R.string.text_alternatives_example_10_content_description
                            )
                        )
            )
            .assertExists()
    }

    @Test
    fun verifyExample11UngroupedTextAndDecorativeIconsHaveTextAndNullContentDescription() {
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample11Icon1TestTag)
                        and
                        hasTextExactly()
                        and
                        hasContentDescriptionExactly()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample11TextTestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.text_alternatives_example_11_decorated_text
                            )
                        )
                        and
                        hasContentDescriptionExactly()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample11Icon2TestTag)
                        and
                        hasTextExactly()
                        and
                        hasContentDescriptionExactly()
            )
            .assertExists()
    }

    @Test
    fun verifyExample12GroupedTextAndIconsHaveExpectedTextAndEmptyContentDescriptions() {
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample12GroupTestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.text_alternatives_example_12_decorated_text
                            )
                        )
                        and
                        hasContentDescriptionExactly("", "")
            )
            .assertExists()
    }

    @Test
    fun verifyExample13GroupedTextAndIconsHaveExpectedTextAndHiddenContentDescriptions() {
        // It is hard to write automated tests that distinguish between texts suppressed with
        // hideFromAccessibility() and not those which are not. The current test on the unmerged
        // tree works around that problem by looking for a node with the expected contentDescription
        // and verifying that it has hideFromAccessibility semantics.
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample13GroupTestTag)
                        and
                        hasAnyDescendant(
                            hasContentDescriptionExactly(
                                composeTestRule.activity.getString(
                                    R.string.text_alternatives_example_13_content_description
                                )
                            ) and isHiddenFromAccessibility()
                        )
                        and
                        hasAnyDescendant(
                            hasTextExactly(
                                composeTestRule.activity.getString(
                                    R.string.text_alternatives_example_13_decorated_text
                                )
                            )
                        ),
                useUnmergedTree = true
            )
            .assertExists()
    }

    @Test
    fun verifyExample14GroupedTextAndIconsHaveExpectedTextAndNullContentDescriptions() {
        composeTestRule
            .onNode(
                hasTestTag(textAlternativesExample14GroupTestTag)
                        and
                        hasContentDescriptionExactly()
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.text_alternatives_example_14_decorated_text
                            )
                        )
            )
            .assertExists()
    }
}