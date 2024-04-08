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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BadExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.OkExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.VisibleFocusBorderButton
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.VisibleFocusBorderTextButton
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.visibleFocusBorder
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val popupMessagesHeadingTestTag = "popupMessagesHeading"
const val popupMessagesExample1HeadingTestTag = "popupMessagesExample1Heading"
const val popupMessagesExample1ButtonTestTag = "popupMessagesExample1Button"
const val popupMessagesExample2HeadingTestTag = "popupMessagesExample2Heading"
const val popupMessagesExample2ButtonTestTag = "popupMessagesExample2Button"
const val popupMessagesExample3HeadingTestTag = "popupMessagesExample3Heading"
const val popupMessagesExample3ButtonTestTag = "popupMessagesExample3Button"
const val popupMessagesExample4HeadingTestTag = "popupMessagesExample4Heading"
const val popupMessagesExample4ButtonTestTag = "popupMessagesExample4Button"
const val popupMessagesExample5HeadingTestTag = "popupMessagesExample5Heading"
const val popupMessagesExample5ButtonTestTag = "popupMessagesExample5Button"
const val popupMessagesExample6HeadingTestTag = "popupMessagesExample6Heading"
const val popupMessagesExample6ButtonTestTag = "popupMessagesExample6Button"
const val popupMessagesExample6AlertDialogTestTag = "popupMessagesExample6AlertDialog"
const val popupMessagesExample6AlertDialogActionButtonTestTag = "popupMessagesExample6AlertDialogActionButton"
const val popupMessagesExample6AlertDialogDismissButtonTestTag = "popupMessagesExample6AlertDialogDismissButton"
const val popupMessagesExample6ShowMoreAlertDialogTestTag = "popupMessagesExample6ShowMoreAlertDialog"
const val popupMessagesExample6ShowMoreAlertDialogDismissButtonTestTag = "popupMessagesExample6ShowMoreAlertDialogDismissButton"
const val popupMessagesExample7HeadingTestTag = "popupMessagesExample7Heading"
const val popupMessagesExample7ButtonTestTag = "popupMessagesExample7Button"
const val popupMessagesExample7MessageTextTestTag = "popupMessagesExample7MessageText"
const val popupMessagesExample7MessageTextButtonTestTag = "popupMessagesExample7MessageTextButton"

/**
 * Demonstrate accessibility techniques for pop-up message controls, such as [Toast], Snackbar, and
 * [BasicAlertDialog].
 *
 * Applies [GenericScaffold] to wrap the screen content. Hosts Snackbars.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@Composable
fun PopupMessagesScreen(
    onBackPressed: () -> Unit
) {
    // Key technique 1a: For Snackbars, remember SnackbarHostState and CoroutineScope.
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    
    GenericScaffold(
        title = stringResource(id = R.string.popup_messages_title),
        onBackPressed = onBackPressed,
        // Key technique 1b: For Snackbars, pass a SnackbarHost to the Scaffold.
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.popup_messages_heading),
                modifier = Modifier.testTag(popupMessagesHeadingTestTag)
            )
            BodyText(textId = R.string.popup_messages_description_1)
            BodyText(textId = R.string.popup_messages_description_2)
            BodyText(textId = R.string.popup_messages_description_3)

            // Toast example
            OkExample1()

            // Snackbar examples
            OkExample2(coroutineScope, snackbarHostState)
            OkExample3(coroutineScope, snackbarHostState)
            BadExample4(coroutineScope, snackbarHostState)
            BadExample5(coroutineScope, snackbarHostState)

            // AlertDialog example
            GoodExample6()

            // Text message example
            GoodExample7()

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        PopupMessagesScreen {}
    }
}

@Composable
private fun OkExample1() {
    // OK example 1: Toast messages
    OkExampleHeading(
        text = stringResource(id = R.string.popup_messages_example_1_header),
        modifier = Modifier.testTag(popupMessagesExample1HeadingTestTag)
    )

    BodyText(textId = R.string.popup_messages_example_1_description_1)
    BodyText(textId = R.string.popup_messages_example_1_description_2)

    // Key technique 2a: Toasts require context.
    val context = LocalContext.current
    val toastMessage = stringResource(id = R.string.popup_messages_example_1_message)
    VisibleFocusBorderButton(
        onClick = {
            // Key technique 2b: Use Toast.makeText(...).show().
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
        },
        modifier = Modifier
            .testTag(popupMessagesExample1ButtonTestTag)
            .padding(top = 8.dp),
    ) {
        Text(stringResource(id = R.string.popup_messages_example_1_button_label))
    }
}

@Preview(showBackground = true)
@Composable
private fun OkExample1Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            OkExample1()
        }
    }
}

@Composable
private fun OkExample2(
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    // OK example 2: Fixed-duration Snackbar with no action button
    OkExampleHeading(
        text = stringResource(id = R.string.popup_messages_example_2_header),
        modifier = Modifier.testTag(popupMessagesExample2HeadingTestTag)
    )
    BodyText(textId = R.string.popup_messages_example_2_description_1)
    BodyText(textId = R.string.popup_messages_example_2_description_2)

    val snackbarMessage = stringResource(id = R.string.popup_messages_example_2_message)
    VisibleFocusBorderButton(
        onClick = {
            // Key technique 1c: Show Snackbars in a coroutine scope.
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = snackbarMessage,
                    withDismissAction = false,
                    duration = SnackbarDuration.Long
                )
            }
        },
        modifier = Modifier
            .testTag(popupMessagesExample2ButtonTestTag)
            .padding(top = 8.dp),
    ) {
        Text(stringResource(id = R.string.popup_messages_example_2_button_label))
    }
}

@Preview(showBackground = true)
@Composable
private fun OkExample2Preview() {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    ComposeAccessibilityTechniquesTheme {
        GenericScaffold(
            title = "OkExample2Preview",
            onBackPressed = {},
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { modifier: Modifier ->
            Column(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                OkExample2(coroutineScope, snackbarHostState)
            }
        }
    }
}

@Composable
private fun OkExample3(
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    // OK example 3: Fixed-duration Snackbar with dismiss button
    OkExampleHeading(
        text = stringResource(id = R.string.popup_messages_example_3_header),
        modifier = Modifier.testTag(popupMessagesExample3HeadingTestTag)
    )
    BodyText(textId = R.string.popup_messages_example_3_description_1)

    val snackbarMessage = stringResource(id = R.string.popup_messages_example_3_message)
    VisibleFocusBorderButton(
        onClick = {
            // Key technique 1c: Show Snackbars in a coroutine scope.
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = snackbarMessage,
                    withDismissAction = true,
                    duration = SnackbarDuration.Long
                )
            }
        },
        modifier = Modifier
            .testTag(popupMessagesExample3ButtonTestTag)
            .padding(top = 8.dp),
    ) {
        Text(stringResource(id = R.string.popup_messages_example_3_button_label))
    }
}

@Preview(showBackground = true)
@Composable
private fun OkExample3Preview() {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    ComposeAccessibilityTechniquesTheme {
        GenericScaffold(
            title = "OkExample3Preview",
            onBackPressed = {},
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { modifier: Modifier ->
            Column(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                OkExample3(coroutineScope, snackbarHostState)
            }
        }
    }
}

@Composable
private fun BadExample4(
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    // Bad example 4: Fixed-duration Snackbar with a contextual action button
    BadExampleHeading(
        text = stringResource(id = R.string.popup_messages_example_4_header),
        modifier = Modifier.testTag(popupMessagesExample4HeadingTestTag)
    )
    BodyText(textId = R.string.popup_messages_example_4_description)

    val snackbarMessage = stringResource(id = R.string.popup_messages_example_4_message)
    val snackbarActionLabel = stringResource(id = R.string.popup_messages_example_4_message_action_label)
    val snackbarActionMessage = stringResource(id = R.string.popup_messages_example_4_action_message)
    VisibleFocusBorderButton(
        onClick = {
            // Key technique 1c: Show Snackbars in a coroutine scope.
            coroutineScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = snackbarMessage,
                    actionLabel = snackbarActionLabel,
                    withDismissAction = true,
                    duration = SnackbarDuration.Long
                )
                if (result == SnackbarResult.ActionPerformed) {
                    snackbarHostState.showSnackbar(
                        message = snackbarActionMessage,
                        withDismissAction = true,
                        duration = SnackbarDuration.Long
                    )
                }
            }
        },
        modifier = Modifier
            .testTag(popupMessagesExample4ButtonTestTag)
            .padding(top = 8.dp),
    ) {
        Text(stringResource(id = R.string.popup_messages_example_4_button_label))
    }
}

@Preview(showBackground = true)
@Composable
private fun BadExample4Preview() {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    ComposeAccessibilityTechniquesTheme {
        GenericScaffold(
            title = "BadExample4Preview",
            onBackPressed = {},
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { modifier: Modifier ->
            Column(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                BadExample4(coroutineScope, snackbarHostState)
            }
        }
    }
}

@Composable
private fun BadExample5(
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    // Bad example 5: Infinite-duration Snackbar with dismiss action
    BadExampleHeading(
        text = stringResource(id = R.string.popup_messages_example_5_header),
        modifier = Modifier.testTag(popupMessagesExample5HeadingTestTag)
    )
    BodyText(textId = R.string.popup_messages_example_5_description_1)
    BodyText(textId = R.string.popup_messages_example_5_description_2)

    val snackbarMessage = stringResource(id = R.string.popup_messages_example_5_message)
    VisibleFocusBorderButton(
        onClick = {
            // Key technique 1c: Show Snackbars in a coroutine scope.
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = snackbarMessage,
                    withDismissAction = true,
                    duration = SnackbarDuration.Indefinite
                )
            }
        },
        modifier = Modifier
            .testTag(popupMessagesExample5ButtonTestTag)
            .padding(top = 8.dp),
    ) {
        Text(stringResource(id = R.string.popup_messages_example_5_button_label))
    }
}

@Preview(showBackground = true)
@Composable
private fun BadExample5Preview() {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    ComposeAccessibilityTechniquesTheme {
        GenericScaffold(
            title = "BadExample5Preview",
            onBackPressed = {},
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { modifier: Modifier ->
            Column(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                BadExample5(coroutineScope, snackbarHostState)
            }
        }
    }
}

@Composable
private fun GoodExample6() {
    // Good example 6: AlertDialog with action buttons
    val (isAlertDialogOpen, setIsAlertDialogOpen) = remember { mutableStateOf(false) }
    val (isShowMoreDialogOpen, setIsShowMoreDialogOpen) = remember { mutableStateOf(false) }

    GoodExampleHeading(
        text = stringResource(id = R.string.popup_messages_example_6_header),
        modifier = Modifier.testTag(popupMessagesExample6HeadingTestTag),
    )
    BodyText(textId = R.string.popup_messages_example_6_description)

    VisibleFocusBorderButton(
        onClick = {
            setIsAlertDialogOpen(true)
        },
        modifier = Modifier
            .testTag(popupMessagesExample6ButtonTestTag)
            .padding(top = 8.dp),
    ) {
        Text(stringResource(id = R.string.popup_messages_example_6_button_label))
    }

    if (isAlertDialogOpen) {
        Example6AlertDialog(setIsAlertDialogOpen, setIsShowMoreDialogOpen)
    }
    if (isShowMoreDialogOpen) {
        Example6ShowMoreDialog(setIsShowMoreDialogOpen)
    }
}

@Composable
private fun Example6AlertDialog(
    setIsAlertDialogOpen: (Boolean) -> Unit,
    setIsShowMoreDialogOpen: (Boolean) -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            setIsAlertDialogOpen(false)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    setIsAlertDialogOpen(false)
                    setIsShowMoreDialogOpen(true)
                },
                modifier = Modifier
                    .testTag(popupMessagesExample6AlertDialogActionButtonTestTag)
                    .visibleFocusBorder()
            ) {
                Text(stringResource(id = R.string.popup_messages_example_6_show_more_label))
            }
        },
        modifier = Modifier.testTag(popupMessagesExample6AlertDialogTestTag),
        dismissButton = {
            TextButton(
                onClick = {
                    setIsAlertDialogOpen(false)
                },
                modifier = Modifier
                    .testTag(popupMessagesExample6AlertDialogDismissButtonTestTag)
                    .visibleFocusBorder()
            ) {
                Text(stringResource(id = R.string.popup_messages_example_6_dismiss_label))
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.popup_messages_example_6_alert_title),
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.popup_messages_example_6_message),
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Example6ShowMoreDialog(
    setIsShowMoreDialogOpen: (Boolean) -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            setIsShowMoreDialogOpen(false)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    setIsShowMoreDialogOpen(false)
                },
                modifier = Modifier
                    .testTag(popupMessagesExample6ShowMoreAlertDialogDismissButtonTestTag)
                    .visibleFocusBorder()
            ) {
                Text(
                    stringResource(id = R.string.popup_messages_example_6_action_popup_dismiss_label)
                )
            }
        },
        modifier = Modifier.testTag(popupMessagesExample6ShowMoreAlertDialogTestTag),
        title = {
            Text(
                text = stringResource(id = R.string.popup_messages_example_6_action_popup_title),
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.popup_messages_example_6_action_popup_message),
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExample6Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample6()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GoodExample7() {
    // Good example 7: On-screen text messages and buttons
    var counterValue by remember { mutableIntStateOf(0) }

    GoodExampleHeading(
        text = stringResource(id = R.string.popup_messages_example_7_header),
        modifier = Modifier.testTag(popupMessagesExample7HeadingTestTag),
    )
    BodyText(textId = R.string.popup_messages_example_7_description)

    // Keyboard focus technique: Remember a focus requester to handle moving keyboard focus.
    val focusRequester = remember { FocusRequester() }

    // Scrolling technique: Remember a BringIntoViewRequester.
    val scope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    VisibleFocusBorderButton(
        onClick = {
            counterValue++
            // Scrolling technique: Launch bringIntoViewRequester to scroll to a composable/region.
            scope.launch {
                bringIntoViewRequester.bringIntoView()
            }
        },
        modifier = Modifier
            .testTag(popupMessagesExample7ButtonTestTag)
            .padding(top = 8.dp)
            // Keyboard focus technique: Declare where the focusRequester will place keyboard focus.
            .focusRequester(focusRequester),
    ) {
        Text(stringResource(id = R.string.popup_messages_example_7_button_label))
    }

    val counterStatusMessage = stringResource(id = R.string.popup_messages_example_7_message, counterValue)
    Row(
        // Scrolling technique: Define the composable to be scrolled to.
        modifier = Modifier.bringIntoViewRequester(bringIntoViewRequester),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = counterStatusMessage,
            modifier = Modifier
                .testTag(popupMessagesExample7MessageTextTestTag)
                .weight(1f)
                .semantics {
                    liveRegion = LiveRegionMode.Polite
                    contentDescription = counterStatusMessage
                }
        )
        if (counterValue > 0) {
            val clearContentDescription = stringResource(id = R.string.popup_messages_example_7_clear_content_description)
            VisibleFocusBorderTextButton(
                onClick = {
                    counterValue = 0
                    // Keyboard focus technique: Move keyboard focus back to the increment button.
                    focusRequester.requestFocus()
                },
                modifier = Modifier.testTag(popupMessagesExample7MessageTextButtonTestTag)
            ) {
                Text(
                    text = stringResource(id = R.string.popup_messages_example_7_clear_label),
                    modifier = Modifier.semantics { 
                        contentDescription = clearContentDescription
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GoodExample7Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample7()
        }
    }
}
