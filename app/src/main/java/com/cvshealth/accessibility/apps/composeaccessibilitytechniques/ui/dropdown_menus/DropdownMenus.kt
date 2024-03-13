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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dropdown_menus

import android.view.KeyEvent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyTextNoPadding
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.ProblematicExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SnackbarLauncher
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.VisibleFocusBorderIconButton
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import kotlinx.coroutines.delay

const val dropdownMenusHeadingTestTag = "dropdownMenusHeading"
const val dropdownMenusExample1HeadingTestTag = "dropdownMenusExample1Heading"
const val dropdownMenusExample1RowTestTag = "dropdownMenusExample1Row"
const val dropdownMenusExample1ButtonTestTag = "dropdownMenusExample1Button"
const val dropdownMenusExample1DropdownMenuTestTag = "dropdownMenusExample1DropdownMenu"
const val dropdownMenusExample1MenuItem1TestTag = "dropdownMenusExample1MenuItem1"
const val dropdownMenusExample1MenuItem2TestTag = "dropdownMenusExample1MenuItem2"
const val dropdownMenusExample2HeadingTestTag = "dropdownMenusExample2Heading"
const val dropdownMenusExample2RowTestTag = "dropdownMenusExample2Row"
const val dropdownMenusExample2ButtonTestTag = "dropdownMenusExample2Button"
const val dropdownMenusExample2DropdownMenuTestTag = "dropdownMenusExample2DropdownMenu"
const val dropdownMenusExample2MenuItem1TestTag = "dropdownMenusExample2MenuItem1"
const val dropdownMenusExample2MenuItem2TestTag = "dropdownMenusExample2MenuItem2"
const val dropdownMenusExample2CloseMenuItemTestTag = "dropdownMenusExample2CloseMenuItem"

@Composable
fun DropdownMenusScreen(
    onBackPressed: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarLauncher = SnackbarLauncher(rememberCoroutineScope(), snackbarHostState)

    GenericScaffold(
        title = stringResource(id = R.string.dropdown_menus_title),
        onBackPressed = onBackPressed,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.dropdown_menus_heading),
                modifier = Modifier.testTag(dropdownMenusHeadingTestTag)
            )
            BodyText(textId = R.string.dropdown_menus_description_1)
            BodyText(textId = R.string.dropdown_menus_description_2)

            ProblematicExample1(snackbarLauncher)
            GoodExample2(snackbarLauncher)

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        DropdownMenusScreen {}
    }
}

@Composable
private fun ProblematicExample1(
    snackbarLauncher: SnackbarLauncher?
) {
    // Problematic example 1: ListItem with stock DropdownMenu
    // The challenge is closing the menu without making a selection.

    // Key technique 1: For DropdownMenus, remember their expansion state.
    var isMenuExpanded by remember { mutableStateOf(false) }
    ProblematicExampleHeading(
        text = stringResource(id = R.string.dropdown_menus_example_1_heading),
        modifier = Modifier.testTag(dropdownMenusExample1HeadingTestTag)
    )

    val menuItem1Message = stringResource(id = R.string.dropdown_menus_example_menu_item_1_message)
    val menuItem2Message = stringResource(id = R.string.dropdown_menus_example_menu_item_2_message)

    Spacer(Modifier.height(8.dp))
    ListItem(
        headlineContent = {
            Text(stringResource(id = R.string.dropdown_menus_example_1_listitem_heading))
        },
        modifier = Modifier
            .testTag(dropdownMenusExample1RowTestTag)
            .border(1.dp, MaterialTheme.colorScheme.onBackground),
        trailingContent = {
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopStart)
            ) {
                // Note: This button should also have a clearly visible focus indicator (not shown).
                IconButton(
                    onClick = { isMenuExpanded = true },
                    modifier = Modifier
                        .testTag(dropdownMenusExample1ButtonTestTag)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(id = R.string.dropdown_menus_example_menu_content_description)
                    )
                }

                // Key issue 1: Menu cannot be closed with the Esc key, as most keyboard users
                // would expect.
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                    modifier = Modifier.testTag(dropdownMenusExample1DropdownMenuTestTag)
                ) {
                    DropdownMenuItem(
                        text = {
                            BodyText(textId = R.string.dropdown_menus_example_menu_item_1)
                        },
                        onClick = {
                            isMenuExpanded = false
                            snackbarLauncher?.show(menuItem1Message)
                        },
                        modifier = Modifier.testTag(dropdownMenusExample1MenuItem1TestTag)
                    )
                    DropdownMenuItem(
                        text = {
                            BodyText(textId = R.string.dropdown_menus_example_menu_item_2)
                        },
                        onClick = {
                            isMenuExpanded = false
                            snackbarLauncher?.show(menuItem2Message)
                        },
                        modifier = Modifier.testTag(dropdownMenusExample1MenuItem2TestTag)
                    )

                    // Key issue 2: No menu close item on the menu itself. Although, TalkBack,
                    // Switch Access, and the keyboard have ways of closing the menu, they are not
                    // obvious to most users.
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ProblematicExample1Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            ProblematicExample1(snackbarLauncher = null)
        }
    }
}

@Composable
private fun GoodExample2(
    snackbarLauncher: SnackbarLauncher?
) {
    // Good example 2: ListItem with accessible DropdownMenu
    // To close the menu, use the Close X menu item or the Escape key.

    // Key technique 1: For DropdownMenus, remember their expansion state.
    var isMenuExpanded by remember { mutableStateOf(false) }

    GoodExampleHeading(
        text = stringResource(id = R.string.dropdown_menus_example_2_heading),
        modifier = Modifier.testTag(dropdownMenusExample2HeadingTestTag)
    )
    BodyText(textId = R.string.dropdown_menus_example_2_description)

    val menuItem1Message = stringResource(id = R.string.dropdown_menus_example_menu_item_1_message)
    val menuItem2Message = stringResource(id = R.string.dropdown_menus_example_menu_item_2_message)
    val menuCloseMessage = stringResource(id = R.string.dropdown_menus_example_menu_close_message)

    Spacer(Modifier.height(8.dp))
    ListItem(
        headlineContent = {
            Text(stringResource(id = R.string.dropdown_menus_example_2_listitem_heading))
        },
        modifier = Modifier
            .testTag(dropdownMenusExample2RowTestTag)
            .border(1.dp, MaterialTheme.colorScheme.onBackground),
        trailingContent = {
            // Wrap the dropdown menu in a Box.
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopStart)
            ) {
                // Key technique 2a: Automatically move focus to the dropdown menu on open. First,
                // create a focus requester.
                val focusRequester = remember { FocusRequester() }

                // Present the menu launch button; the menu will appear above, below, or covering this icon,
                // depending on its screen position and the menu's size.

                // Key technique 3: Make sure dropdown menu buttons have visible focus indicators.
                VisibleFocusBorderIconButton(
                    onClick = { isMenuExpanded = true },
                    modifier = Modifier
                        .testTag(dropdownMenusExample2ButtonTestTag)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(id = R.string.dropdown_menus_example_menu_content_description)
                    )
                }

                // Compose the Dropdown menu, which only composes its content if expanded.
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                    modifier = Modifier
                        .testTag(dropdownMenusExample2DropdownMenuTestTag)
                        // Key technique 2b: Set the focus requester on the Dropdown menu.
                        .focusRequester(focusRequester)

                        // Key technique 4: Allow the Esc key to dismiss the dropdown menu. This
                        // code requires that the menu have focus, so it is partially predicated on
                        // Key Technique 2, but not entirely.
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ESCAPE) {
                                isMenuExpanded = false
                                snackbarLauncher?.show(
                                    message = menuCloseMessage,
                                    withDismissAction = false,
                                    duration = SnackbarDuration.Short
                                )
                                true
                            } else {
                                false
                            }
                        }
                ) {
                    // The dropdown menu's content...
                    DropdownMenuItem(
                        text = {
                            BodyText(textId = R.string.dropdown_menus_example_menu_item_1)
                        },
                        onClick = {
                            isMenuExpanded = false
                            snackbarLauncher?.show(menuItem1Message)
                        },
                        modifier = Modifier.testTag(dropdownMenusExample2MenuItem1TestTag)
                    )

                    DropdownMenuItem(
                        text = {
                            BodyText(textId = R.string.dropdown_menus_example_menu_item_2)
                        },
                        onClick = {
                            isMenuExpanded = false
                            snackbarLauncher?.show(menuItem2Message)
                        },
                        modifier = Modifier.testTag(dropdownMenusExample2MenuItem2TestTag)
                    )

                    // Key technique 5: Add a "Close" menu item.
                    // This menu item is separated by a HorizontalDivider and adds a trailing icon
                    // to distinguish it from the other menu items. Different presentations for the
                    // close control would work as well.
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = {
                            BodyTextNoPadding(textId = R.string.dropdown_menus_example_menu_close_item)
                        },
                        onClick = {
                            isMenuExpanded = false
                            snackbarLauncher?.show(
                                message = menuCloseMessage,
                                withDismissAction = false,
                                duration = SnackbarDuration.Short
                            )
                        },
                        modifier = Modifier.testTag(dropdownMenusExample2CloseMenuItemTestTag),
                        // Trailing icon added to emphasize its function of closing the menu.
                        trailingIcon = {
                            // Note: This Icon's information is redundant with the menu item's text,
                            // so its contentDescription is omitted.
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null
                            )
                        }
                    )
                }

                // Key technique 2c: When the menu opens, pause, then request focus on the menu.
                LaunchedEffect(isMenuExpanded) {
                    if (isMenuExpanded) {
                        delay(500)
                        focusRequester.requestFocus()
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GoodExample2Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
           GoodExample2(snackbarLauncher = null)
        }
    }
}