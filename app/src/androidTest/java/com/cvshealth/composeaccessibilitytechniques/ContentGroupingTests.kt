package com.cvshealth.composeaccessibilitytechniques

import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescriptionExactly
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.MainActivity
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingCardExample4CardTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingCardExample5CardTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingCardExample6CardTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingCardExamplesHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExample1ColumnTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExample1Row1Cell1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExample1Row1Cell2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExample1Row1Cell3TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExample1Row1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExample1Row2Cell1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExample1Row2Cell2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExample1Row2Cell3TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExample1Row2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExample1TitleTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExample2ColumnTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExample2Row1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExample2Row2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExample2TitleTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExample3Column1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExample3Column2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExample3Column3TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExample3RowTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExample3TitleTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.contentGroupingTableExamplesHeadingTestTag
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ContentGroupingTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        // Navigate from HomeScreen to ContentGroupingScreen.
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.content_grouping_title))
            .performScrollTo()
            .performClick()
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(contentGroupingHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(contentGroupingTableExamplesHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(contentGroupingCardExamplesHeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyNonHeadingsAreNotHeadings() {
        // Introduction section
        composeTestRule
            .onNode(
                hasTextExactly(
                    composeTestRule.activity.getString(
                        R.string.content_grouping_description
                    )
                ) and !isHeading()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTextExactly(
                    composeTestRule.activity.getString(
                        R.string.content_grouping_description_2
                    )
                ) and !isHeading()
            )
            .assertExists()

        // Example 1
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample1TitleTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample1ColumnTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Example 2
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample2TitleTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample2ColumnTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Example 3
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample3TitleTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample3RowTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Example 4
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingCardExample4CardTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Example 5
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingCardExample5CardTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()

        // Example 6
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingCardExample6CardTestTag)
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
    fun verifyExample1UngroupedTableHasNoGroupedTextsAndExpectedIndividualTexts() {
        // Verify that the table contains no contentDescriptions -- the test for this is convoluted.
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample1ColumnTestTag)
                        and
                        hasContentDescriptionExactly()
                        and
                        !hasAnyDescendant(!hasContentDescriptionExactly())
            )
            .assertExists()

        // Verify that the table contains no grouped text as a whole
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample1ColumnTestTag)
                        and
                        hasTextExactly()
            )
            .assertExists()

        // Verify that each table Row contains no grouped text
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample1Row1TestTag)
                        and
                        hasTextExactly()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample1Row2TestTag)
                        and
                        hasTextExactly()
            )
            .assertExists()

        // Verify that individual table Texts have the expected Text values
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample1Row1Cell1TestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.content_grouping_table_example_header_1
                            )
                        )
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample1Row1Cell2TestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.content_grouping_table_example_header_2
                            )
                        )
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample1Row1Cell3TestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.content_grouping_table_example_header_3
                            )
                        )
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample1Row2Cell1TestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.content_grouping_table_example_value_1
                            )
                        )
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample1Row2Cell2TestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.content_grouping_table_example_value_2
                            )
                        )
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample1Row2Cell3TestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.content_grouping_table_example_value_3
                            )
                        )
            )
            .assertExists()
    }

    @Test
    fun verifyExample2MisgroupedTableHasExpectedGroupedTexts() {
        // Verify that the table contains no contentDescriptions -- the test for this is convoluted.
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample2ColumnTestTag)
                        and
                        hasContentDescriptionExactly()
                        and
                        !hasAnyDescendant(!hasContentDescriptionExactly())
            )
            .assertExists()

        // Verify that the table contains no grouped text as a whole
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample2ColumnTestTag)
                        and
                        hasTextExactly()
            )
            .assertExists()

        // Verify that each table Row contains the expected grouped texts
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample2Row1TestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.content_grouping_table_example_header_1
                            ),
                            composeTestRule.activity.getString(
                                R.string.content_grouping_table_example_header_2
                            ),
                            composeTestRule.activity.getString(
                                R.string.content_grouping_table_example_header_3
                            )
                        )
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample2Row2TestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.content_grouping_table_example_value_1
                            ),
                            composeTestRule.activity.getString(
                                R.string.content_grouping_table_example_value_2
                            ),
                            composeTestRule.activity.getString(
                                R.string.content_grouping_table_example_value_3
                            )
                        )
            )
            .assertExists()
    }

    @Test
    fun verifyExample3CorrectlyGroupedTableHasExpectedGroupedTexts() {
        // Verify that the table contains no contentDescriptions -- the test for this is convoluted.
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample3RowTestTag)
                        and
                        hasContentDescriptionExactly()
                        and
                        !hasAnyDescendant(!hasContentDescriptionExactly())
            )
            .assertExists()

        // Verify that the table contains no grouped text as a whole
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample3RowTestTag)
                        and
                        hasTextExactly()
            )
            .assertExists()

        // Verify that each table Column contains the expected grouped texts
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample3Column1TestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.content_grouping_table_example_header_1
                            ),
                            composeTestRule.activity.getString(
                                R.string.content_grouping_table_example_value_1
                            )
                        )
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample3Column2TestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.content_grouping_table_example_header_2
                            ),
                            composeTestRule.activity.getString(
                                R.string.content_grouping_table_example_value_2
                            )
                        )
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingTableExample3Column3TestTag)
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.content_grouping_table_example_header_3
                            ),
                            composeTestRule.activity.getString(
                                R.string.content_grouping_table_example_value_3
                            )
                        )
            )
            .assertExists()
    }

    @Test
    fun verifyExample4UngroupedCardHasNoGroupedTexts() {
        // Verify that the Card contains no contentDescriptions -- the test for this is convoluted.
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingCardExample4CardTestTag)
                        and
                        hasContentDescriptionExactly()
                        and
                        !hasAnyDescendant(!hasContentDescriptionExactly())
            )
            .assertExists()

        // Verify that the Card contains no grouped text as a whole
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingCardExample4CardTestTag)
                        and
                        hasTextExactly()
            )
            .assertExists()
    }

    @Test
    fun verifyExample5GroupedCardHasExpectedGroupedTexts() {
        // Verify that the Card contains no contentDescriptions -- the test for this is convoluted.
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingCardExample5CardTestTag)
                        and
                        hasContentDescriptionExactly()
                        and
                        !hasAnyDescendant(!hasContentDescriptionExactly())
            )
            .assertExists()

        // Verify that the Card contains the expected grouped texts (and is not clickable)
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingCardExample5CardTestTag)
                        and
                        hasNoClickAction()
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.content_grouping_card2_title
                            ),
                            composeTestRule.activity.getString(
                                R.string.content_grouping_card2_author
                            ),
                            composeTestRule.activity.getString(
                                R.string.content_grouping_card2_date
                            ),
                            composeTestRule.activity.getString(
                                R.string.content_grouping_card2_description
                            ),
                        )
            )
            .assertExists()
    }

    @Test
    fun verifyExample6ClickGroupedCardHasExpectedGroupedTexts() {
        // Verify that the Card contains no contentDescriptions -- the test for this is convoluted.
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingCardExample6CardTestTag)
                        and
                        hasContentDescriptionExactly()
                        and
                        !hasAnyDescendant(!hasContentDescriptionExactly())
            )
            .assertExists()

        // Verify that the Card contains the expected grouped texts and is clickable
        composeTestRule
            .onNode(
                hasTestTag(contentGroupingCardExample6CardTestTag)
                        and
                        hasClickAction()
                        and
                        hasTextExactly(
                            composeTestRule.activity.getString(
                                R.string.content_grouping_card3_title
                            ),
                            composeTestRule.activity.getString(
                                R.string.content_grouping_card3_author
                            ),
                            composeTestRule.activity.getString(
                                R.string.content_grouping_card3_date
                            ),
                            composeTestRule.activity.getString(
                                R.string.content_grouping_card3_description
                            ),
                        )
            )
            .assertExists()
    }
}