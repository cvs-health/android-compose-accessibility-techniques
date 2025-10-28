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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.scrolling_columns

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.CollectionItemInfo
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.collectionItemInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SnackbarLauncher
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.VisibleFocusBorderButton
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.visibleFocusBorder
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val scrollingInteractiveColumnTestTag = "scrollingInteractiveColumn"
const val scrollingInteractiveColumnScreenHeadingTestTag = "scrollingInteractiveColumnScreenHeading"
const val scrollingInteractiveColumnExample1HeadingTestTag = "scrollingInteractiveColumnExample1Heading"

/**
 * Demonstrate accessibility techniques for a scrollable [Column] list of sometimes-interactive
 * content. Shows how to separate focusability for the scroll region and the interactive content.
 *
 * Applies [GenericScaffold] to wrap the screen content.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScrollingInteractiveColumnsScreen(
    onBackPressed: () -> Unit
) {
    val itemNames = listOf(
        stringResource(id = R.string.scrolling_interactive_columns_item_1),
        stringResource(id = R.string.scrolling_interactive_columns_item_2),
        stringResource(id = R.string.scrolling_interactive_columns_item_3),
        stringResource(id = R.string.scrolling_interactive_columns_item_4),
        stringResource(id = R.string.scrolling_interactive_columns_item_5),
        stringResource(id = R.string.scrolling_interactive_columns_item_6),
        stringResource(id = R.string.scrolling_interactive_columns_item_7),
        stringResource(id = R.string.scrolling_interactive_columns_item_8),
        stringResource(id = R.string.scrolling_interactive_columns_item_9),
        stringResource(id = R.string.scrolling_interactive_columns_item_10),
        stringResource(id = R.string.scrolling_interactive_columns_item_11),
        stringResource(id = R.string.scrolling_interactive_columns_item_12),
        stringResource(id = R.string.scrolling_interactive_columns_item_13),
        stringResource(id = R.string.scrolling_interactive_columns_item_14),
        stringResource(id = R.string.scrolling_interactive_columns_item_15),
        stringResource(id = R.string.scrolling_interactive_columns_item_16),
        stringResource(id = R.string.scrolling_interactive_columns_item_17),
        stringResource(id = R.string.scrolling_interactive_columns_item_18),
        stringResource(id = R.string.scrolling_interactive_columns_item_19),
        stringResource(id = R.string.scrolling_interactive_columns_item_20),
        stringResource(id = R.string.scrolling_interactive_columns_item_21),
        stringResource(id = R.string.scrolling_interactive_columns_item_22),
        stringResource(id = R.string.scrolling_interactive_columns_item_23),
        stringResource(id = R.string.scrolling_interactive_columns_item_24),
        stringResource(id = R.string.scrolling_interactive_columns_item_25),
        stringResource(id = R.string.scrolling_interactive_columns_item_26),
        stringResource(id = R.string.scrolling_interactive_columns_item_27),
        stringResource(id = R.string.scrolling_interactive_columns_item_28),
    )
    val lastItemButtonMessage = stringResource(R.string.scrolling_interactive_columns_last_item_button_message)

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarLauncher = SnackbarLauncher(coroutineScope, snackbarHostState)

    // Key technique 1: Declare the ScrollState for later use.
    val scrollState = rememberScrollState()
    
    GenericScaffold(
        title = stringResource(id = R.string.scrolling_interactive_columns_title),
        onBackPressed = onBackPressed,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { modifier ->
        val itemCount = itemNames.size
        Column(
            modifier = modifier
                .testTag(scrollingInteractiveColumnTestTag)
                .fillMaxSize()
                // Key technique: Make the column scrollable and attach the scrollState.
                .verticalScroll(scrollState)
                // Key technique: Place a focus indicator on the scrollable column, similar to
                // ScrollView's.
                .visibleFocusBorder()
                // Note: Making the scrollable Column focusable() is not needed to make the Column
                // keyboard scrollable, as there is a focusable Button within its content. However,
                // in order to preserve meaningful focus order, the outer Column should have its own
                // separate focus stop before that of the nested Button.
                .focusable()
                // Technique: Padding is applied here to separate the focus indicator from the
                // Column's content.
                .padding(horizontal = 16.dp)
        ) {
                SimpleHeading(
                    text = stringResource(id = R.string.scrolling_interactive_columns_heading),
                    modifier = Modifier.testTag(scrollingInteractiveColumnScreenHeadingTestTag)
                )
                BodyText(textId = R.string.scrolling_interactive_columns_description_1)
                BodyText(textId = R.string.scrolling_interactive_columns_description_2)

                GoodExampleHeading(
                    text = stringResource(id = R.string.scrolling_interactive_columns_example_1_header),
                    modifier = Modifier.testTag(scrollingInteractiveColumnExample1HeadingTestTag)
                )
                Spacer(Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    // Additional Technique: Apply list semantics to the Column.
                    .semantics {
                        collectionInfo = CollectionInfo(rowCount = itemCount + 1, columnCount = 1)
                    }
            ) {
                itemNames.forEachIndexed { index, itemName ->
                    ListItem(
                        headlineContent = { Text(itemName) },
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = MaterialTheme.shapes.medium
                            )
                            .semantics {
                                collectionItemInfo = CollectionItemInfo(
                                    rowIndex = index,
                                    rowSpan = 1,
                                    columnIndex = 0,
                                    columnSpan = 1
                                )
                            },
                        // Technique: Enhances focus background fill.
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
                ListItem(
                    headlineContent = {
                        Text(
                            stringResource(R.string.scrolling_interactive_columns_last_item_label)
                        )
                    },
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = MaterialTheme.shapes.medium
                        )
                        .semantics {
                            collectionItemInfo = CollectionItemInfo(
                                rowIndex = itemCount,
                                rowSpan = 1,
                                columnIndex = 0,
                                columnSpan = 1
                            )
                        },
                    trailingContent = {
                        VisibleFocusBorderButton(
                            onClick = {
                                snackbarLauncher.show(lastItemButtonMessage)
                            }
                        ) {
                            Text(
                                stringResource(R.string.scrolling_interactive_columns_last_item_button_label)
                            )
                        }
                    },
                    // Technique: Enhances focus background fill.
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewScrollingListWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        ScrollingInteractiveColumnsScreen {}
    }
}
