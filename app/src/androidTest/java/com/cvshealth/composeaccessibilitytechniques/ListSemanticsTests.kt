/*
   Copyright 2023 CVS Health and/or one of its affiliates

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

import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.MainActivity
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample1ColumnTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample2ColumnTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample2Row1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample2Row2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample2Row3TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample3ColumnTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample3Row1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample3Row2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample3Row3TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample4Column1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample4Column2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample4Column3TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample4Column4TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample4Column5TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample4Column6TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample4HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsExample4LazyRowTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.listSemanticsHeadingTestTag
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ListSemanticsTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        // Navigate from HomeScreen to ListSemanticsScreen.
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.home_informative_content))
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.list_semantics_title))
            .performScrollTo()
            .performClick()
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(listSemanticsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(listSemanticsExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(listSemanticsExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(listSemanticsExample3HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(listSemanticsExample4HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyNonHeadingsAreNotHeadings() {
        // Introduction section
        composeTestRule
            .onNode(
                hasTextExactly(
                    composeTestRule.activity.getString(
                        R.string.list_semantics_description_1
                    )
                ) and !isHeading()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTextExactly(
                    composeTestRule.activity.getString(
                        R.string.list_semantics_description_2
                    )
                ) and !isHeading()
            )
            .assertExists()

        // Example 1
        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample1ColumnTestTag)
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
                        R.string.list_semantics_after_bad_example
                    )
                ) and !isHeading()
            )
            .assertExists()

        // Example 2
        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample2ColumnTestTag)
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
                        R.string.list_semantics_after_list
                    )
                ) and !isHeading()
            )
            .assertExists()

        // Example 3
        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample3ColumnTestTag)
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
                        R.string.list_semantics_after_list_2
                    )
                ) and !isHeading()
            )
            .assertExists()

        // Example 4
        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample4LazyRowTestTag)
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
                        R.string.list_semantics_after_list_3
                    )
                ) and !isHeading()
            )
            .assertExists()
    }

    @Test
    fun verifyEveryHeadingsHasATestTag() {
        composeTestRule.onNode(hasNoTestTag() and isHeading()).assertDoesNotExist()
    }

    @Test
    fun verifyExample1NonSemanticListHasNoListSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample1ColumnTestTag)
                        and
                        hasNoCollectionInfo()
                        and
                        !hasAnyDescendant(hasCollectionItemInfo())
            )
            .assertExists()
    }

    @Test
    fun verifyExample2SemanticBulletListHasExpectedListSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample2ColumnTestTag)
                        and
                        hasCollectionInfo()
                        and
                        hasCollectionRowCount(expectedRowCount = 3)
                        and
                        hasCollectionColumnCount(expectedColumnCount = 1)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample2Row1TestTag)
                        and
                        hasCollectionItemInfo()
                        and
                        hasCollectionItemColumnIndex(expectedColumnIndex = 0)
                        and
                        hasCollectionItemColumnSpan(expectedColumnSpan = 1)
                        and
                        hasCollectionItemRowIndex(expectedRowIndex = 0)
                        and
                        hasCollectionItemRowSpan(expectedRowSpan = 1)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample2Row2TestTag)
                        and
                        hasCollectionItemInfo()
                        and
                        hasCollectionItemColumnIndex(expectedColumnIndex = 0)
                        and
                        hasCollectionItemColumnSpan(expectedColumnSpan = 1)
                        and
                        hasCollectionItemRowIndex(expectedRowIndex = 1)
                        and
                        hasCollectionItemRowSpan(expectedRowSpan = 1)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample2Row3TestTag)
                        and
                        hasCollectionItemInfo()
                        and
                        hasCollectionItemColumnIndex(expectedColumnIndex = 0)
                        and
                        hasCollectionItemColumnSpan(expectedColumnSpan = 1)
                        and
                        hasCollectionItemRowIndex(expectedRowIndex = 2)
                        and
                        hasCollectionItemRowSpan(expectedRowSpan = 1)
            )
            .assertExists()
    }

    @Test
    fun verifyExample3SemanticNumberedListHasExpectedListSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample3ColumnTestTag)
                        and
                        hasCollectionInfo()
                        and
                        hasCollectionRowCount(expectedRowCount = 3)
                        and
                        hasCollectionColumnCount(expectedColumnCount = 1)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample3Row1TestTag)
                        and
                        hasCollectionItemInfo()
                        and
                        hasCollectionItemColumnIndex(expectedColumnIndex = 0)
                        and
                        hasCollectionItemColumnSpan(expectedColumnSpan = 1)
                        and
                        hasCollectionItemRowIndex(expectedRowIndex = 0)
                        and
                        hasCollectionItemRowSpan(expectedRowSpan = 1)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample3Row2TestTag)
                        and
                        hasCollectionItemInfo()
                        and
                        hasCollectionItemColumnIndex(expectedColumnIndex = 0)
                        and
                        hasCollectionItemColumnSpan(expectedColumnSpan = 1)
                        and
                        hasCollectionItemRowIndex(expectedRowIndex = 1)
                        and
                        hasCollectionItemRowSpan(expectedRowSpan = 1)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample3Row3TestTag)
                        and
                        hasCollectionItemInfo()
                        and
                        hasCollectionItemColumnIndex(expectedColumnIndex = 0)
                        and
                        hasCollectionItemColumnSpan(expectedColumnSpan = 1)
                        and
                        hasCollectionItemRowIndex(expectedRowIndex = 2)
                        and
                        hasCollectionItemRowSpan(expectedRowSpan = 1)
            )
            .assertExists()
    }

    @Test
    fun verifyExample4LazyListHasExpectedListSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample4LazyRowTestTag)
                        and
                        hasCollectionInfo()
                        and
                        hasCollectionRowCount(expectedRowCount = 1)
                        and
                        hasCollectionColumnCount(expectedColumnCount = -1)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample4Column1TestTag)
                        and
                        hasNoCollectionItemInfo()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample4Column2TestTag)
                        and
                        hasNoCollectionItemInfo()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample4Column3TestTag)
                        and
                        hasNoCollectionItemInfo()
            )
            .assertExists()

        // LazyRow items past 4 (depending on the device) don't exist in the list until scrolled to
        // so scroll early in case test is run on a narrower device.
        composeTestRule
            .onNode(hasTestTag(listSemanticsExample4LazyRowTestTag))
            .performScrollToIndex(index = 5)

        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample4Column4TestTag)
                        and
                        hasNoCollectionItemInfo()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample4Column5TestTag)
                        and
                        hasNoCollectionItemInfo()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listSemanticsExample4Column6TestTag)
                        and
                        hasNoCollectionItemInfo()
            )
            .assertExists()
    }
}