/*
   © Copyright 2023-2024 CVS Health and/or one of its affiliates. All rights reserved.

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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleTitle
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GroupedBadExampleTitle
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GroupedGoodExampleTitle
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SnackbarLauncher
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.visibleCardFocusBorder
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme


const val contentGroupingHeadingTestTag = "contentGroupingHeading"
const val contentGroupingTableExamplesHeadingTestTag = "contentGroupingTableExamplesHeading"
const val contentGroupingTableExample1TitleTestTag = "contentGroupingTableExample1Title"
const val contentGroupingTableExample1ColumnTestTag = "contentGroupingTableExample1Column"
const val contentGroupingTableExample1Row1TestTag = "contentGroupingTableExample1Row1"
const val contentGroupingTableExample1Row1Cell1TestTag = "contentGroupingTableExample1Row1Cell1"
const val contentGroupingTableExample1Row1Cell2TestTag = "contentGroupingTableExample1Row1Cell2"
const val contentGroupingTableExample1Row1Cell3TestTag = "contentGroupingTableExample1Row1Cell3"
const val contentGroupingTableExample1Row2TestTag = "contentGroupingTableExample1Row2"
const val contentGroupingTableExample1Row2Cell1TestTag = "contentGroupingTableExample1Row2Cell1"
const val contentGroupingTableExample1Row2Cell2TestTag = "contentGroupingTableExample1Row2Cell2"
const val contentGroupingTableExample1Row2Cell3TestTag = "contentGroupingTableExample1Row2Cell3"
const val contentGroupingTableExample2TitleTestTag = "contentGroupingTableExample2Title"
const val contentGroupingTableExample2ColumnTestTag = "contentGroupingTableExample2Column"
const val contentGroupingTableExample2Row1TestTag = "contentGroupingTableExample2Row1"
const val contentGroupingTableExample2Row2TestTag = "contentGroupingTableExample2Row2"
const val contentGroupingTableExample3TitleTestTag = "contentGroupingTableExample3Title"
const val contentGroupingTableExample3RowTestTag = "contentGroupingTableExample3Row"
const val contentGroupingTableExample3Column1TestTag = "contentGroupingTableExample3Column1"
const val contentGroupingTableExample3Column2TestTag = "contentGroupingTableExample3Column2"
const val contentGroupingTableExample3Column3TestTag = "contentGroupingTableExample3Column3"
const val contentGroupingCardExamplesHeadingTestTag = "contentGroupingCardExamplesHeading"
const val contentGroupingCardExample4CardTestTag = "contentGroupingCardExample4Card"
const val contentGroupingCardExample5CardTestTag = "contentGroupingCardExample5Card"
const val contentGroupingCardExample6CardTestTag = "contentGroupingCardExample6Card"

/**
 * Demonstrate accessibility techniques regarding content grouping.
 *
 * Applies [GenericScaffold] to wrap the screen content. Hosts Snackbars.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@Composable
fun ContentGroupingScreen(
    onBackPressed: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarLauncher = SnackbarLauncher(rememberCoroutineScope(), snackbarHostState)

    GenericScaffold(
        title = stringResource(id = R.string.content_grouping_title),
        onBackPressed = onBackPressed,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()
        Column(
            modifier = modifier
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.content_grouping_heading),
                modifier = Modifier.testTag(contentGroupingHeadingTestTag)
            )
            BodyText(textId = R.string.content_grouping_description)
            BodyText(textId = R.string.content_grouping_description_2)

            // Simple Table Examples
            SimpleHeading(
                text = stringResource(id = R.string.content_grouping_table_examples),
                modifier = Modifier
                    .testTag(contentGroupingTableExamplesHeadingTestTag)
                    .padding(top = 8.dp)
            )
            BadExample1()
            BadExample2()
            GoodExample3()

            // Card Examples
            SimpleHeading(
                text = stringResource(id = R.string.content_grouping_card_examples),
                modifier = Modifier
                    .testTag(contentGroupingCardExamplesHeadingTestTag)
                    .padding(top = 8.dp)
            )
            BadExample4()
            GoodExample5()
            GoodExample6(snackbarLauncher)

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        ContentGroupingScreen {}
    }
}

@Composable
private fun BadExample1() {
    // Bad example 1: Ungrouped table
    // Does not organize texts that belongs together into their own layout composables
    // and does not apply Modifier.semantics(mergeDescendants = true).
    GroupedBadExampleTitle(
        textId = R.string.content_grouping_table_example_1,
        modifier = Modifier.testTag(contentGroupingTableExample1TitleTestTag)
    )
    Column(
        modifier = Modifier.testTag(contentGroupingTableExample1ColumnTestTag)
    ) {
        Row(
            modifier = Modifier.testTag(contentGroupingTableExample1Row1TestTag)
        ) {
            BodyText(
                textId = R.string.content_grouping_table_example_header_1,
                modifier = Modifier
                    .testTag(contentGroupingTableExample1Row1Cell1TestTag)
                    .weight(1.0f)
            )
            BodyText(
                textId = R.string.content_grouping_table_example_header_2,
                modifier = Modifier
                    .testTag(contentGroupingTableExample1Row1Cell2TestTag)
                    .weight(1.0f)
            )
            BodyText(
                textId = R.string.content_grouping_table_example_header_3,
                modifier = Modifier
                    .testTag(contentGroupingTableExample1Row1Cell3TestTag)
                    .weight(1.0f)
            )
        }
        Row(
            modifier = Modifier.testTag(contentGroupingTableExample1Row2TestTag)
        ) {
            BodyText(
                textId = R.string.content_grouping_table_example_value_1,
                modifier = Modifier
                    .testTag(contentGroupingTableExample1Row2Cell1TestTag)
                    .weight(1.0f)
            )
            BodyText(
                textId = R.string.content_grouping_table_example_value_2,
                modifier = Modifier
                    .testTag(contentGroupingTableExample1Row2Cell2TestTag)
                    .weight(1.0f)
            )
            BodyText(
                textId = R.string.content_grouping_table_example_value_3,
                modifier = Modifier
                    .testTag(contentGroupingTableExample1Row2Cell3TestTag)
                    .weight(1.0f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewBadExample1() {
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
private fun BadExample2() {
    // Bad example 2: Misgrouped table
    // Applies Modifier.semantics(mergeDescendants = true), but to improperly grouped texts,
    // so all the headers are read together, and then all the data values are read together.
    // That doesn't associate each header with its data value.
    GroupedBadExampleTitle(
        textId = R.string.content_grouping_table_example_2,
        modifier = Modifier.testTag(contentGroupingTableExample2TitleTestTag)
    )
    Column(
        modifier = Modifier.testTag(contentGroupingTableExample2ColumnTestTag)
    ) {
        Row(
            modifier = Modifier
                .testTag(contentGroupingTableExample2Row1TestTag)
                .semantics(mergeDescendants = true) {}
        ) {
            BodyText(
                textId = R.string.content_grouping_table_example_header_1,
                modifier = Modifier.weight(1.0f)
            )
            BodyText(
                textId = R.string.content_grouping_table_example_header_2,
                modifier = Modifier.weight(1.0f)
            )
            BodyText(
                textId = R.string.content_grouping_table_example_header_3,
                modifier = Modifier.weight(1.0f)
            )
        }
        Row(
            modifier = Modifier
                .testTag(contentGroupingTableExample2Row2TestTag)
                .semantics(mergeDescendants = true) {}
        ) {
            BodyText(
                textId = R.string.content_grouping_table_example_value_1,
                modifier = Modifier.weight(1.0f)
            )
            BodyText(
                textId = R.string.content_grouping_table_example_value_2,
                modifier = Modifier.weight(1.0f)
            )
            BodyText(
                textId = R.string.content_grouping_table_example_value_3,
                modifier = Modifier.weight(1.0f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewBadExample2() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            BadExample2()
        }
    }
}

@Composable
private fun GoodExample3() {
    // Good example 3: Properly grouped table

    // Key techniques:
    // 1. Groups headers and data values that belong together into their own layout composables.
    // 2. Applies Modifier.semantics(mergeDescendants = true) to those tight header-value groups.
    GroupedGoodExampleTitle(
        textId = R.string.content_grouping_table_example_3,
        modifier = Modifier.testTag(contentGroupingTableExample3TitleTestTag)
    )
    Row(
        modifier = Modifier.testTag(contentGroupingTableExample3RowTestTag)
    ) {
        Column(
            modifier = Modifier
                .testTag(contentGroupingTableExample3Column1TestTag)
                .semantics(mergeDescendants = true) {}
                .weight(1.0f)
        ) {
            BodyText(
                textId = R.string.content_grouping_table_example_header_1
            )
            BodyText(
                textId = R.string.content_grouping_table_example_value_1
            )
        }
        Column(
            modifier = Modifier
                .testTag(contentGroupingTableExample3Column2TestTag)
                .semantics(mergeDescendants = true) {}
                .weight(1.0f)
        ) {
            BodyText(
                textId = R.string.content_grouping_table_example_header_2
            )
            BodyText(
                textId = R.string.content_grouping_table_example_value_2
            )
        }
        Column(
            modifier = Modifier
                .testTag(contentGroupingTableExample3Column3TestTag)
                .semantics(mergeDescendants = true) {}
                .weight(1.0f)
        ) {
            BodyText(
                textId = R.string.content_grouping_table_example_header_3
            )
            BodyText(
                textId = R.string.content_grouping_table_example_value_3
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewGoodExample3() {
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
private fun BadExample4() {
    // Bad example 4: Card without grouped content
    OutlinedCard(
        modifier = Modifier
            .testTag(contentGroupingCardExample4CardTestTag)
            .padding(top = 8.dp)
    ) {
        GroupedBadExampleTitle(
            textId = R.string.content_grouping_card1_title,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BodyText(
                textId = R.string.content_grouping_card1_author,
                modifier = Modifier.weight(1f)
            )
            BodyText(
                textId = R.string.content_grouping_card1_date,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
        BodyText(
            textId = R.string.content_grouping_card1_description,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewBadExample4() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            BadExample4()
        }
    }
}

@Composable
private fun GoodExample5() {
    // Good example 5: Card with grouped content

    // Key technique: Group all composable content using Modifier.semantics(mergeDescendants = true).
    OutlinedCard(
        modifier = Modifier
            .testTag(contentGroupingCardExample5CardTestTag)
            .padding(top = 8.dp)
            .semantics(mergeDescendants = true) { }
    ) {
        // Note: Do not apply mergeDescendants semantics within the Card; it will create additional
        // areas of accessibility focus within this larger group.
        //
        // Also, do not put a testTag() on this composable, because that would introduce an extra
        // semantics node, making Compose jUnit testing of merged content impossible.
        // Doing so would not affect TalkBack's behavior for users, only automated testing.
        GoodExampleTitle(
            textId = R.string.content_grouping_card2_title,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BodyText(
                textId = R.string.content_grouping_card2_author,
                modifier = Modifier.weight(1f)
            )
            BodyText(
                textId = R.string.content_grouping_card2_date,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
        BodyText(
            textId = R.string.content_grouping_card2_description,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewGoodExample5() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample5()
        }
    }
}

@Composable
private fun GoodExample6(
    snackbarLauncher: SnackbarLauncher?
) {
    // Good example 6: Another card with grouped content
    val popupMessage = stringResource(
        id = R.string.content_grouping_card3_message
    )

    // Key technique: Making a composable clickable automatically applies mergeDescendants
    // semantics to the composable's child content.
    //
    // Note that nested actionable composables, such as a nested Button, will not be merged,
    // but remain separate accessibility nodes, as do nested composables that also apply
    // mergeDescendants=true semantics.
    OutlinedCard(
        onClick = {
            snackbarLauncher?.show(popupMessage)
        },
        modifier = Modifier
            .testTag(contentGroupingCardExample6CardTestTag)
            .padding(top = 8.dp)
            .visibleCardFocusBorder()
    ) {
        // Note: GoodExampleTitle used here, because it does not set mergeDescendants=true.
        GoodExampleTitle(
            textId = R.string.content_grouping_card3_title,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BodyText(
                textId = R.string.content_grouping_card3_author,
                modifier = Modifier.weight(1f)
            )
            BodyText(
                textId = R.string.content_grouping_card3_date,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
        BodyText(
            textId = R.string.content_grouping_card3_description,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewGoodExample6() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample6(snackbarLauncher = null)
        }
    }
}