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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.modalbottomsheet_layouts

import android.view.KeyEvent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.VisibleFocusBorderButton
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.visibleFocusBorder
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


const val modalBottomSheetLayoutsHeadingTestTag = "modalBottomSheetLayoutsHeading"
const val modalBottomSheetLayoutsExample1HeadingTestTag = "modalBottomSheetLayoutsExample1Heading"
const val modalBottomSheetLayoutsExample1Button1TestTag = "modalBottomSheetLayoutsExample1Button1"
const val modalBottomSheetLayoutsExample1Button2TestTag = "modalBottomSheetLayoutsExample1Button2"
const val modalBottomSheetLayoutsExample1SelectedItemTestTag = "modalBottomSheetLayoutsExample1SelectedItem"
const val modalBottomSheetLayoutsExampleSheetTestTag = "modalBottomSheetLayoutsExampleSheet"

/**
 * Demonstrate accessibility techniques for [ModalBottomSheet] layouts.
 *
 * Applies [GenericScaffold] to wrap the screen content.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetLayoutsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.modalbottomsheet_layouts_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.modalbottomsheet_layouts_heading),
                modifier = Modifier.testTag(modalBottomSheetLayoutsHeadingTestTag)
            )
            BodyText(textId = R.string.modalbottomsheet_layouts_description_1)
            BodyText(textId = R.string.modalbottomsheet_layouts_description_2)
            BodyText(textId = R.string.modalbottomsheet_layouts_description_3)

            var openBottomSheet by rememberSaveable { mutableStateOf(false) }
            val (selectedSheetItem, setSelectedSheetItem) = rememberSaveable { mutableStateOf("") }
            var skipPartiallyExpanded by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()
            val sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = skipPartiallyExpanded
            )
            val focusRequester = remember { FocusRequester() }
            val itemNames = listOf(
                stringResource(id = R.string.modalbottomsheet_layouts_item_1),
                stringResource(id = R.string.modalbottomsheet_layouts_item_2),
                stringResource(id = R.string.modalbottomsheet_layouts_item_3),
                stringResource(id = R.string.modalbottomsheet_layouts_item_4),
                stringResource(id = R.string.modalbottomsheet_layouts_item_5),
                stringResource(id = R.string.modalbottomsheet_layouts_item_6),
                stringResource(id = R.string.modalbottomsheet_layouts_item_7),
                stringResource(id = R.string.modalbottomsheet_layouts_item_8),
                stringResource(id = R.string.modalbottomsheet_layouts_item_9),
                stringResource(id = R.string.modalbottomsheet_layouts_item_10),
                stringResource(id = R.string.modalbottomsheet_layouts_item_11),
                stringResource(id = R.string.modalbottomsheet_layouts_item_12),
                stringResource(id = R.string.modalbottomsheet_layouts_item_13),
                stringResource(id = R.string.modalbottomsheet_layouts_item_14),
                stringResource(id = R.string.modalbottomsheet_layouts_item_15),
                stringResource(id = R.string.modalbottomsheet_layouts_item_16),
                stringResource(id = R.string.modalbottomsheet_layouts_item_17),
                stringResource(id = R.string.modalbottomsheet_layouts_item_18),
                stringResource(id = R.string.modalbottomsheet_layouts_item_19),
                stringResource(id = R.string.modalbottomsheet_layouts_item_20),
                stringResource(id = R.string.modalbottomsheet_layouts_item_21),
                stringResource(id = R.string.modalbottomsheet_layouts_item_22),
                stringResource(id = R.string.modalbottomsheet_layouts_item_23),
                stringResource(id = R.string.modalbottomsheet_layouts_item_24),
                stringResource(id = R.string.modalbottomsheet_layouts_item_25),
                stringResource(id = R.string.modalbottomsheet_layouts_item_26),
                stringResource(id = R.string.modalbottomsheet_layouts_item_27),
                stringResource(id = R.string.modalbottomsheet_layouts_item_28),
            )

            // Good example 1: Half-open ModalBottomSheet layout
            GoodExampleHeading(
                text = stringResource(id = R.string.modalbottomsheet_layouts_example_1_header),
                modifier = Modifier.testTag(modalBottomSheetLayoutsExample1HeadingTestTag)
            )

            VisibleFocusBorderButton(
                onClick = {
                    skipPartiallyExpanded = false
                    openBottomSheet = true
                },
                modifier = Modifier
                    .testTag(modalBottomSheetLayoutsExample1Button1TestTag)
                    .padding(top = 8.dp)
            ) {
                Text(text = stringResource(id = R.string.modalbottomsheet_layouts_example_1_button_1))
            }

            VisibleFocusBorderButton(
                onClick = {
                    skipPartiallyExpanded = true
                    openBottomSheet = true
                },
                modifier = Modifier
                    .testTag(modalBottomSheetLayoutsExample1Button2TestTag)
                    .padding(top = 8.dp)
            ) {
                Text(text = stringResource(id = R.string.modalbottomsheet_layouts_example_1_button_2))
            }

            val selectedItemText = stringResource(
                id = R.string.modalbottomsheet_layouts_example_1_selected_item,
                selectedSheetItem
            )
            Text(
                text = selectedItemText,
                modifier = Modifier
                    .testTag(modalBottomSheetLayoutsExample1SelectedItemTestTag)
                    .padding(top = 8.dp)
                    .semantics {
                        liveRegion = LiveRegionMode.Polite
                        // contentDescription use is necessary, due to
                        // https://issuetracker.google.com/issues/225780131.
                        contentDescription = selectedItemText
                    },
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (openBottomSheet) {
                val bottomSheetTitle = stringResource(id = R.string.modalbottomsheet_layouts_sheet_title)
                val dragHandleDescription = stringResource(id = R.string.modalbottomsheet_layouts_sheet_drag_handle_description)
                ModalBottomSheet(
                    onDismissRequest = { openBottomSheet = false },
                    modifier = Modifier
                        .testTag(modalBottomSheetLayoutsExampleSheetTestTag)
                        // Key technique: Allow the Esc key to dismiss the bottom sheet.
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ESCAPE) {
                                scope.launch {
                                    sheetState.hide()
                                }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        openBottomSheet = false
                                    }
                                }
                                true
                            } else {
                                false
                            }
                        }
                        // Key technique: If only a half-expanded sheet is shown, limit the size of
                        // the sheet; otherwise, it is possible to tab to and select content that is
                        // off-screen with the keyboard.
                        .run {
                            if (!skipPartiallyExpanded) {
                                fillMaxHeight(fraction = 0.5f)
                            } else {
                                this
                            }
                        }
                        .semantics {
                            paneTitle = bottomSheetTitle
                        },
                    sheetState = sheetState,
                    // Key technique: Override the default drag handle contentDescription to provide
                    // a meaningful bottom sheet title.
                    dragHandle = {
                        BottomSheetDefaults.DragHandle(
                            modifier = Modifier.semantics {
                                contentDescription = dragHandleDescription
                            }
                        )
                    },
                    // Key technique: Set the windowInsets so bottom sheet padding can be measured.
                    windowInsets = WindowInsets.safeDrawing
                ) {
                    // Key technique: Pad the bottom of the bottom sheet contents to avoid the
                    // them sliding under the bottom system navigation bar and the last item
                    // becoming unfocusable with assistive technologies. (The bottom sheet drag
                    // handle provides sufficient top padding to protect the sheet contents.)
                    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                    LazyColumn(
                        modifier = Modifier
                            .padding(bottom = bottomPadding)
                            .focusRequester(focusRequester)
                    ) {
                        // Key technique: Provide a visual bottom sheet title.
                        item {
                            Text(
                                text = bottomSheetTitle,
                                modifier = Modifier.padding(horizontal = 8.dp),
                                style = typography.headlineSmall
                            )
                            HorizontalDivider()
                        }

                        items(itemNames) { itemName ->
                            ListItem(
                                headlineContent = {
                                    Text(itemName)
                                },
                                modifier = Modifier
                                    .visibleFocusBorder()
                                    .clickable(role = Role.Button) {
                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                openBottomSheet = false
                                                // Key technique for liveRegions: update state that
                                                // affects a liveRegion only after the bottom sheet has
                                                // been dismissed.
                                                setSelectedSheetItem(itemName)
                                            }
                                        }
                                    }
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }

            // Key technique: Set keyboard focus onto a newly-opened bottom sheet. (Otherwise, Esc
            // key handling to close the bottom sheet will fail.)
            LaunchedEffect(openBottomSheet) {
                if (openBottomSheet) {
                    delay(500)
                    focusRequester.requestFocus()
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        ModalBottomSheetLayoutsScreen {}
    }
}
