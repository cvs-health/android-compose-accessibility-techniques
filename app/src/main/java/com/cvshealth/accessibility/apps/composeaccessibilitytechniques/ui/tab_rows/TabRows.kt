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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.tab_rows

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BadExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.FixedPagedTabGroup
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.OkExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.StatefulFixedTabGroup
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.StatelessScrollableTabGroup
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.visibleFocusBorder
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val tabRowsHeadingTestTag = "tabRowsHeading"
const val tabRowsExample1HeadingTestTag = "tabRowsExample1Heading"
const val tabRowsExample1TabRowTestTag = "tabRowsExample1TabRow"
const val tabRowsExample1TabContentTestTagBase = "tabRowsExample1TabContent"
const val tabRowsExample2HeadingTestTag = "tabRowsExample2Heading"
const val tabRowsExample2TabRowTestTag = "tabRowsExample2TabRow"
const val tabRowsExample2TabContentTestTagBase = "tabRowsExample2TabContent"
const val tabRowsExample3HeadingTestTag = "tabRowsExample3Heading"
const val tabRowsExample3TabRowTestTag = "tabRowsExample3TabRow"
const val tabRowsExample3TabContentTestTagBase = "tabRowsExample3TabContent"
const val tabRowsExample4HeadingTestTag = "tabRowsExample4Heading"
const val tabRowsExample4TabRowTestTag = "tabRowsExample4TabRow"
const val tabRowsExample4TabContentTestTagBase = "tabRowsExample4TabContent"
const val tabRowsExample4PagerTestTag = "tabRowsExample4Pager"
const val tabRowsEndSpacerTestTag = "tabRowsEndSpacer"

/**
 * Demonstrate accessibility techniques for [TabRow] and [Tab] controls.
 *
 * Applies [GenericScaffold] to wrap the screen content.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@Composable
fun TabRowsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.tab_rows_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()

        val titles = listOf(
            stringResource(id = R.string.tab_rows_tab_1),
            stringResource(id = R.string.tab_rows_tab_2),
            stringResource(id = R.string.tab_rows_tab_3),
            stringResource(id = R.string.tab_rows_tab_4),
            stringResource(id = R.string.tab_rows_tab_5)
        )
        val contents = listOf(
            stringResource(id = R.string.tab_rows_tab_1_content),
            stringResource(id = R.string.tab_rows_tab_2_content),
            stringResource(id = R.string.tab_rows_tab_3_content),
            stringResource(id = R.string.tab_rows_tab_4_content),
            stringResource(id = R.string.tab_rows_tab_5_content)
        )

        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.tab_rows_heading),
                modifier = Modifier.testTag(tabRowsHeadingTestTag)
            )
            BodyText(textId = R.string.tab_rows_description_1)
            BodyText(textId = R.string.tab_rows_description_2)
            BodyText(textId = R.string.tab_rows_description_3)

            BadExample1(titles, contents)
            GoodExample2(titles, contents)
            OkExample3(titles, contents)
            OkExample4(titles, contents)

            Spacer(
                modifier = Modifier
                    .testTag(tabRowsEndSpacerTestTag)
                    .height(32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        TabRowsScreen {}
    }
}

@Composable
private fun BadExample1(
    titles: List<String>,
    contents: List<String>
) {
    // Bad example 1: Fixed TabRow with limited text
    BadExampleHeading(
        text = stringResource(id = R.string.tab_rows_example_1_header),
        modifier = Modifier.testTag(tabRowsExample1HeadingTestTag)
    )

    val (tabIndex, setTabIndex) = rememberSaveable { mutableIntStateOf(0) }
    Column {
        TabRow(
            selectedTabIndex = tabIndex,
            modifier = Modifier.testTag(tabRowsExample1TabRowTestTag)
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { setTabIndex(index) },
                    modifier = Modifier.visibleFocusBorder(),
                    text = {
                        Text(
                            text = title,
                            // Key error: Limiting tab height with maxLines = 1 (or setting Modifier.height())
                            // prevents text from reflowing onto multiple lines properly.
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                )
            }
        }
        Text(
            text = contents[tabIndex],
            modifier = Modifier
                .testTag("${tabRowsExample1TabContentTestTagBase}${tabIndex}")
                .padding(vertical = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider()
    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
private fun BadExample1Preview() {
    val titles = listOf(
        stringResource(id = R.string.tab_rows_tab_1),
        stringResource(id = R.string.tab_rows_tab_2),
        stringResource(id = R.string.tab_rows_tab_3),
    )
    val contents = listOf(
        stringResource(id = R.string.tab_rows_tab_1_content),
        stringResource(id = R.string.tab_rows_tab_2_content),
        stringResource(id = R.string.tab_rows_tab_3_content),
    )
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            BadExample1(titles, contents)
        }
    }
}

@Composable
private fun GoodExample2(
    titles: List<String>,
    contents: List<String>
) {
    // Good example 2: Fixed TabRow with text reflow
    GoodExampleHeading(
        text = stringResource(id = R.string.tab_rows_example_2_header),
        modifier = Modifier.testTag(tabRowsExample2HeadingTestTag)
    )

    // Key technique: Since this tab's state is pure UI and this composable does not need
    // to be aware when it changes, wrapping the TabRow and its Tabs in a reusable control
    // that manages its own state is possible and simpler than state hoisting.
    StatefulFixedTabGroup(
        tabTitles = titles,
        modifier = Modifier.testTag(tabRowsExample2TabRowTestTag)
    ) { tabIndex ->
        Text(
            text = contents[tabIndex],
            modifier = Modifier
                .testTag("${tabRowsExample2TabContentTestTagBase}${tabIndex}")
                .padding(vertical = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider()
    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
private fun GoodExample2Preview() {
    val titles = listOf(
        stringResource(id = R.string.tab_rows_tab_1),
        stringResource(id = R.string.tab_rows_tab_2),
        stringResource(id = R.string.tab_rows_tab_3),
    )
    val contents = listOf(
        stringResource(id = R.string.tab_rows_tab_1_content),
        stringResource(id = R.string.tab_rows_tab_2_content),
        stringResource(id = R.string.tab_rows_tab_3_content),
    )
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample2(titles, contents)
        }
    }
}

@Composable
private fun OkExample3(
    titles: List<String>,
    contents: List<String>
) {
    // OK example 3: ScrollableTabRow
    OkExampleHeading(
        text = stringResource(id = R.string.tab_rows_example_3_header),
        modifier = Modifier.testTag(tabRowsExample3HeadingTestTag)
    )
    BodyText(textId = R.string.tab_rows_example_3_description)

    // Note: Tab selection state is hoisted to this composable for example purposes only.
    // This example could use StatefulScrollableTabGroup instead.
    val (example2TabIndex, setExample2TabIndex) = rememberSaveable { mutableIntStateOf(0) }
    StatelessScrollableTabGroup(
        tabIndex = example2TabIndex,
        setTabIndex = setExample2TabIndex,
        tabTitles = titles,
        modifier = Modifier.testTag(tabRowsExample3TabRowTestTag)
    ) { tabIndex ->
        Text(
            text = contents[tabIndex],
            modifier = Modifier
                .testTag("${tabRowsExample3TabContentTestTagBase}${tabIndex}")
                .padding(vertical = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider()
    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
private fun OkExample3Preview() {
    val titles = listOf(
        stringResource(id = R.string.tab_rows_tab_1),
        stringResource(id = R.string.tab_rows_tab_2),
        stringResource(id = R.string.tab_rows_tab_3),
        stringResource(id = R.string.tab_rows_tab_4),
    )
    val contents = listOf(
        stringResource(id = R.string.tab_rows_tab_1_content),
        stringResource(id = R.string.tab_rows_tab_2_content),
        stringResource(id = R.string.tab_rows_tab_3_content),
        stringResource(id = R.string.tab_rows_tab_4_content),
    )
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            OkExample3(titles, contents)
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun OkExample4(
    titles: List<String>,
    contents: List<String>
) {
    // OK example 4: TabRow with Pager
    OkExampleHeading(
        text = stringResource(id = R.string.tab_rows_example_4_header),
        modifier = Modifier.testTag(tabRowsExample4HeadingTestTag)
    )
    BodyText(textId = R.string.tab_rows_example_4_description)


    // Key technique: Since this tab panel's state is pure UI and this composable does not need
    // to be aware when it changes, wrapping the TabRow and its HorizontalPager of Tabs in a
    // reusable control that manages its own state is possible and simpler than state hoisting.
    FixedPagedTabGroup(
        tabTitles = titles,
        modifier = Modifier.testTag(tabRowsExample4TabRowTestTag),
        pagerModifier = Modifier.testTag(tabRowsExample4PagerTestTag),
    ) { pageIndex ->
        Text(
            text = contents[pageIndex],
            modifier = Modifier
                .testTag("${tabRowsExample4TabContentTestTagBase}${pageIndex}")
                .padding(vertical = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider()
}

@Preview(showBackground = true)
@Composable
private fun OkExample4Preview() {
    val titles = listOf(
        stringResource(id = R.string.tab_rows_tab_1),
        stringResource(id = R.string.tab_rows_tab_2),
    )
    val contents = listOf(
        stringResource(id = R.string.tab_rows_tab_1_content),
        stringResource(id = R.string.tab_rows_tab_2_content),
    )
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            OkExample4(titles, contents)
        }
    }
}

