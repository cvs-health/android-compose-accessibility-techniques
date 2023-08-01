package com.cvshealth.composeaccessibilitytechniques

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertRangeInfoEquals
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
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_group_replacement.contentGroupReplacementExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_group_replacement.contentGroupReplacementExample1ProgressBarTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_group_replacement.contentGroupReplacementExample1RatingLabelTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_group_replacement.contentGroupReplacementExample1RatingTextTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_group_replacement.contentGroupReplacementExample1ReviewTextTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_group_replacement.contentGroupReplacementExample1RowTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_group_replacement.contentGroupReplacementExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_group_replacement.contentGroupReplacementExample2RowTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_group_replacement.contentGroupReplacementExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_group_replacement.contentGroupReplacementExample3RowTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_group_replacement.contentGroupReplacementExample4HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_group_replacement.contentGroupReplacementExample4RowTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_group_replacement.contentGroupReplacementExamplesMaxRating
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_group_replacement.contentGroupReplacementExamplesRating
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_group_replacement.contentGroupReplacementExamplesReviews
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_group_replacement.contentGroupReplacementHeadingTestTag
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ContentGroupReplacementTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        // Navigate from HomeScreen to TextAlternativesScreen.
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.content_group_replacement_title))
            .performScrollTo()
            .performClick()
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(contentGroupReplacementHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(contentGroupReplacementExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(contentGroupReplacementExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(contentGroupReplacementExample3HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(contentGroupReplacementExample4HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyNonHeadingsAreNotHeadings() {
        // Introduction section
        composeTestRule
            .onNode(
                hasTextExactly(
                    composeTestRule.activity.getString(
                        R.string.content_group_replacement_description
                    )
                ) and !isHeading()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTextExactly(
                    composeTestRule.activity.getString(
                        R.string.content_group_replacement_description_2
                    )
                ) and !isHeading()
            )
            .assertExists()

        // Example 1
        composeTestRule
            .onNode(
                hasTestTag(contentGroupReplacementExample1RowTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Example 2
        composeTestRule
            .onNode(
                hasTestTag(contentGroupReplacementExample2RowTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Example 3
        composeTestRule
            .onNode(
                hasTestTag(contentGroupReplacementExample3RowTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Example 4
        composeTestRule
            .onNode(
                hasTestTag(contentGroupReplacementExample4RowTestTag)
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
    fun verifyExample1UngroupedRowHasExpectedTextAndRangeInfo() {
        // Row has no contentDescription
        composeTestRule
            .onNode(
                hasTestTag(contentGroupReplacementExample1RowTestTag)
                        and
                        hasContentDescriptionExactly()
            )
            .assertExists()

        // Rating label has expected text
        composeTestRule
            .onNode(
                hasTestTag(contentGroupReplacementExample1RatingLabelTestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.content_group_replacement_rating_label
                            )
                        )
            )
            .assertExists()

        // LinearProgressIndicator has expected range information (can't test TalkBack text directly)
        composeTestRule
            .onNode(
                hasTestTag(contentGroupReplacementExample1ProgressBarTestTag)
            )
            .assertRangeInfoEquals(
                ProgressBarRangeInfo(
                    current = contentGroupReplacementExamplesRating / contentGroupReplacementExamplesMaxRating,
                    range = 0.0f..1.0f,
                    steps = 0
                )
            )

        // Rating text has expected text
        composeTestRule
            .onNode(
                hasTestTag(contentGroupReplacementExample1RatingTextTestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.content_group_replacement_rating_text,
                                contentGroupReplacementExamplesRating,
                                contentGroupReplacementExamplesMaxRating
                            )
                        )
            )
            .assertExists()

        // Review text has expected text
        composeTestRule
            .onNode(
                hasTestTag(contentGroupReplacementExample1ReviewTextTestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.content_group_replacement_reviews,
                                contentGroupReplacementExamplesReviews
                            )
                        )
            )
            .assertExists()
    }

    @Test
    fun verifyExample2GroupedRowHasExpectedText() {
        // Row has expected texts
        composeTestRule
            .onNode(
                hasTestTag(contentGroupReplacementExample2RowTestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.content_group_replacement_rating_label
                            ),
                            composeTestRule.activity.getString(
                                R.string.content_group_replacement_rating_text,
                                contentGroupReplacementExamplesRating,
                                contentGroupReplacementExamplesMaxRating
                            ),
                            composeTestRule.activity.getString(
                                R.string.content_group_replacement_reviews,
                                contentGroupReplacementExamplesReviews
                            )
                        )
            )
            .assertExists()
    }

    @Test
    fun verifyExample3ReplacedRowHasExpectedContentDescriptionAndInvisibleTexts() {
        val groupContentDescription = composeTestRule.activity.resources.getQuantityString(
            R.plurals.content_group_replacement_rating_group_content_description,
            contentGroupReplacementExamplesReviews,
            contentGroupReplacementExamplesRating,
            contentGroupReplacementExamplesMaxRating,
            contentGroupReplacementExamplesReviews
        )

        // Row has expected contentDescription and no text(s)
        composeTestRule
            .onNode(
                hasTestTag(contentGroupReplacementExample3RowTestTag)
                        and
                        hasContentDescriptionExactly(groupContentDescription)
                        and
                        // invisibleToUser() text nodes are still present in the semantics tree
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.content_group_replacement_rating_label
                            ),
                            composeTestRule.activity.getString(
                                R.string.content_group_replacement_rating_text,
                                contentGroupReplacementExamplesRating,
                                contentGroupReplacementExamplesMaxRating
                            ),
                            composeTestRule.activity.getString(
                                R.string.content_group_replacement_reviews,
                                contentGroupReplacementExamplesReviews
                            )
                        )
            )
            .assertExists()
    }

    @Test
    fun verifyExample4ReplacedRowHasExpectedContentDescriptionAndNoTexts() {
        val groupContentDescription = composeTestRule.activity.resources.getQuantityString(
            R.plurals.content_group_replacement_rating_group_content_description,
            contentGroupReplacementExamplesReviews,
            contentGroupReplacementExamplesRating,
            contentGroupReplacementExamplesMaxRating,
            contentGroupReplacementExamplesReviews
        )

        // Row has expected contentDescription and no text(s)
        composeTestRule
            .onNode(
                hasTestTag(contentGroupReplacementExample4RowTestTag)
                        and
                        hasContentDescriptionExactly(groupContentDescription)
                        and
                        hasTextExactly()
            )
            .assertExists()
    }

}