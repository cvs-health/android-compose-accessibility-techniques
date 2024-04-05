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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BadExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyTextNoPadding
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BulletListItemRow
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericListColumn
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.TextListItemRow
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val listSemanticsHeadingTestTag = "listSemanticsHeading"
const val listSemanticsExample1HeadingTestTag = "listSemanticsExample1Heading"
const val listSemanticsExample1ColumnTestTag = "listSemanticsExample1Column"
const val listSemanticsExample2HeadingTestTag = "listSemanticsExample2Heading"
const val listSemanticsExample2ColumnTestTag = "listSemanticsExample2Column"
const val listSemanticsExample2Row1TestTag = "listSemanticsExample2Row1"
const val listSemanticsExample2Row2TestTag = "listSemanticsExample2Row2"
const val listSemanticsExample2Row3TestTag = "listSemanticsExample2Row3"
const val listSemanticsExample3HeadingTestTag = "listSemanticsExample3Heading"
const val listSemanticsExample3ColumnTestTag = "listSemanticsExample3Column"
const val listSemanticsExample3Row1TestTag = "listSemanticsExample3Row1"
const val listSemanticsExample3Row2TestTag = "listSemanticsExample3Row2"
const val listSemanticsExample3Row3TestTag = "listSemanticsExample3Row3"
const val listSemanticsExample4HeadingTestTag = "listSemanticsExample4Heading"
const val listSemanticsExample4LazyRowTestTag = "listSemanticsExample4LazyRow"
const val listSemanticsExample4Column1TestTag = "listSemanticsExample4Column1"
const val listSemanticsExample4Column2TestTag = "listSemanticsExample4Column2"
const val listSemanticsExample4Column3TestTag = "listSemanticsExample4Column3"
const val listSemanticsExample4Column4TestTag = "listSemanticsExample4Column4"
const val listSemanticsExample4Column5TestTag = "listSemanticsExample4Column5"
const val listSemanticsExample4Column6TestTag = "listSemanticsExample4Column6"

/**
 * Demonstrate accessibility techniques for list semantics in accordance with WCAG
 * [Success Criterion 1.3.1 Info and Relationships](https://www.w3.org/TR/WCAG22/#info-and-relationships) and [Success Criterion 4.1.2 Name, Role, Value](https://www.w3.org/TR/WCAG22/#name-role-value).
 *
 * Applies [GenericScaffold] to wrap the screen content.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@Composable
fun ListSemanticsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.list_semantics_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()
        Column(
            modifier = modifier
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.list_semantics_heading),
                modifier = Modifier.testTag(listSemanticsHeadingTestTag)
            )
            BodyText(textId = R.string.list_semantics_description_1)
            BodyText(textId = R.string.list_semantics_description_2)

            BadExample1()
            GoodExample2()
            GoodExample3()
            GoodExample4()

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        ListSemanticsScreen {}
    }
}

@Composable
private fun FauxBulletListRow(
    @StringRes textId: Int,
    modifier: Modifier = Modifier
) {
    val bulletPointAltText = stringResource(id = R.string.list_semantics_bullet_point_alt_text)
    Row(
        modifier = modifier.semantics(mergeDescendants = true) {},
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "\u2022",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .semantics {
                    contentDescription = bulletPointAltText
                }
                .width(24.dp)
        )
        BodyTextNoPadding(textId = textId,
            modifier = Modifier.weight(1f, fill = true),
        )
    }
}

@Composable
private fun BadExample1() {
    // Bad example 1: Visual list without list semantics
    BadExampleHeading(
        text = stringResource(id = R.string.list_semantics_example_1),
        modifier = Modifier.testTag(listSemanticsExample1HeadingTestTag)
    )
    Column(
        modifier = Modifier
            .testTag(listSemanticsExample1ColumnTestTag)
            .padding(top = 8.dp)
    ) {
        FauxBulletListRow(textId = R.string.list_semantics_bad_point_1)
        FauxBulletListRow(textId = R.string.list_semantics_bad_point_2)
        FauxBulletListRow(textId = R.string.list_semantics_bad_point_3)
    }
    BodyText(textId = R.string.list_semantics_after_bad_example)
}

@Preview(showBackground = true)
@Composable
private fun BadExample1Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            BadExample1()
        }
    }
}

@Composable
private fun GoodExample2() {
    // Good example 2: Bullet list with list semantics
    GoodExampleHeading(
        text = stringResource(id = R.string.list_semantics_example_2),
        modifier = Modifier.testTag(listSemanticsExample2HeadingTestTag)
    )
    GenericListColumn(
        rowCount = 3,
        modifier = Modifier
            .testTag(listSemanticsExample2ColumnTestTag)
            .padding(top = 8.dp)
    ) {
        BulletListItemRow(
            rowIndex = 0,
            textId = R.string.list_semantics_good_point_1,
            modifier = Modifier.testTag(listSemanticsExample2Row1TestTag)
        )
        BulletListItemRow(
            rowIndex = 1,
            textId = R.string.list_semantics_good_point_2,
            modifier = Modifier.testTag(listSemanticsExample2Row2TestTag)
        )
        BulletListItemRow(
            rowIndex = 2,
            textId = R.string.list_semantics_good_point_3,
            modifier = Modifier.testTag(listSemanticsExample2Row3TestTag)
        )
    }
    BodyText(textId = R.string.list_semantics_after_list)
}

@Preview(showBackground = true)
@Composable
private fun GoodExample2Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample2()
        }
    }
}

@Composable
private fun GoodExample3() {
    // Good example 3: Numbered list with list semantics
    GoodExampleHeading(
        text = stringResource(id = R.string.list_semantics_example_3),
        modifier = Modifier.testTag(listSemanticsExample3HeadingTestTag)
    )
    GenericListColumn(
        rowCount = 3,
        modifier = Modifier
            .testTag(listSemanticsExample3ColumnTestTag)
            .padding(top = 8.dp)
    ) {
        TextListItemRow(
            rowIndex = 0,
            textId = R.string.list_semantics_good_point_1_numbered,
            modifier = Modifier.testTag(listSemanticsExample3Row1TestTag)
        )
        TextListItemRow(
            rowIndex = 1,
            textId = R.string.list_semantics_good_point_2_numbered,
            modifier = Modifier.testTag(listSemanticsExample3Row2TestTag)
        )
        TextListItemRow(
            rowIndex = 2,
            textId = R.string.list_semantics_good_point_3_numbered,
            modifier = Modifier.testTag(listSemanticsExample3Row3TestTag)
        )
    }
    BodyText(textId = R.string.list_semantics_after_list_2)
}

@Preview(showBackground = true)
@Composable
private fun GoodExample3Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample3()
        }
    }
}

@Composable
private fun LazyRowItem(
    @StringRes textId: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(end = 8.dp)
            .semantics(mergeDescendants = true) {}
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
            .padding(all = 4.dp)
    ) {
        Text(text = stringResource(id = textId))
    }
}

@Composable
private fun GoodExample4() {
    // Good example 4: LazyColumn list with list semantics
    GoodExampleHeading(
        text = stringResource(id = R.string.list_semantics_example_4),
        modifier = Modifier.testTag(listSemanticsExample4HeadingTestTag)
    )

    // Key technique: LazyRow and LazyColumn automatically apply list semantics
    // (at least to the extent of announcing list entry and exit).
    LazyRow(
        modifier = Modifier
            .testTag(listSemanticsExample4LazyRowTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        item {
            LazyRowItem(
                textId = R.string.list_semantics_lazyrow_1,
                modifier = Modifier.testTag(listSemanticsExample4Column1TestTag)
            )
        }
        item {
            LazyRowItem(
                textId = R.string.list_semantics_lazyrow_2,
                modifier = Modifier.testTag(listSemanticsExample4Column2TestTag)
            )
        }
        item {
            LazyRowItem(
                textId = R.string.list_semantics_lazyrow_3,
                modifier = Modifier.testTag(listSemanticsExample4Column3TestTag)
            )
        }
        item {
            LazyRowItem(
                textId = R.string.list_semantics_lazyrow_4,
                modifier = Modifier.testTag(listSemanticsExample4Column4TestTag)
            )
        }
        item {
            LazyRowItem(
                textId = R.string.list_semantics_lazyrow_5,
                modifier = Modifier.testTag(listSemanticsExample4Column5TestTag)
            )
        }
        item {
            LazyRowItem(
                textId = R.string.list_semantics_lazyrow_6,
                modifier = Modifier.testTag(listSemanticsExample4Column6TestTag)
            )
        }
    }
    BodyText(textId = R.string.list_semantics_after_list_3)
}

@Preview(showBackground = true)
@Composable
private fun GoodExample4Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample4()
        }
    }
}