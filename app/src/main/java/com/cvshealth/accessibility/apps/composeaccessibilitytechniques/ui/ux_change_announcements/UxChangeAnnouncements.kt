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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.VisibleFocusBorderButton
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import kotlinx.coroutines.delay

const val uxChangeAnnouncementsHeadingTestTag = "uxChangeAnnouncementsHeading"
const val uxChangeAnnouncementsExample1HeadingTestTag = "uxChangeAnnouncementsExample1Heading"
const val uxChangeAnnouncementsExample1LiveRegionTextTestTag = "uxChangeAnnouncementsExample1LiveRegionText"
const val uxChangeAnnouncementsExample1IncrementCounterTestTag = "uxChangeAnnouncementsExample1IncrementCounter"
const val uxChangeAnnouncementsExample1ResetCounterTestTag = "uxChangeAnnouncementsExample1ResetCounter"
const val uxChangeAnnouncementsExample2HeadingTestTag = "uxChangeAnnouncementsExample2Heading"
const val uxChangeAnnouncementsExample2ButtonTestTag = "uxChangeAnnouncementsExample2Button"
const val uxChangeAnnouncementsExample2LiveRegionTextTestTag = "uxChangeAnnouncementsExample2LiveRegionText"
const val uxChangeAnnouncementsExample3HeadingTestTag = "uxChangeAnnouncementsExample3Heading"
const val uxChangeAnnouncementsExample3ButtonTestTag = "uxChangeAnnouncementsExample3Button"
const val uxChangeAnnouncementsExample3ProgressIndicatorTestTag = "uxChangeAnnouncementsExample3LiveRegionText"
const val uxChangeAnnouncementsExample4HeadingTestTag = "uxChangeAnnouncementsExample4Heading"
const val uxChangeAnnouncementsExample4ButtonTestTag = "uxChangeAnnouncementsExample4Button"
const val uxChangeAnnouncementsExample4AlertTestTag = "uxChangeAnnouncementsExample4LiveRegionText"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UxChangeAnnouncementsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.ux_change_announcements_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()
        val (counterValue, setCounterValue) = rememberSaveable { mutableStateOf(0) }

        // Box to hold scrollable Column and centered ProgressIndicator.
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            var isWaitingIndicatorVisible by rememberSaveable {
                mutableStateOf(false)
            }
            val waitingMessage = stringResource(R.string.ux_change_announcements_waiting)

            // Scrolling content column.
            Column(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                SimpleHeading(
                    text = stringResource(id = R.string.ux_change_announcements_heading),
                    modifier = Modifier.testTag(uxChangeAnnouncementsHeadingTestTag)
                )
                BodyText(textId = R.string.ux_change_announcements_description)
                BodyText(textId = R.string.ux_change_announcements_description_2)

                // Good example 1: Counter with liveRegion semantics
                GoodExampleHeading(
                    text = stringResource(id = R.string.ux_change_announcements_example_1_header),
                    modifier = Modifier.testTag(uxChangeAnnouncementsExample1HeadingTestTag)
                )

                // Display the counter liveRegion.
                val counterText =
                    stringResource(id = R.string.ux_change_announcements_counter, counterValue)
                Text(
                    text = counterText,
                    modifier = Modifier
                        .testTag(uxChangeAnnouncementsExample1LiveRegionTextTestTag)
                        .padding(top = 8.dp)
                        .semantics {
                            // Key technique 1: Set liveRegion semantics on the Text that changes when
                            // the buttons below are pressed.
                            liveRegion = LiveRegionMode.Polite
                            // contentDescription use is necessary, due to
                            // https://issuetracker.google.com/issues/225780131.
                            contentDescription = counterText
                        },
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )

                // Display buttons to change the counter liveRegion value.
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Increment the counter liveRegion value.
                    VisibleFocusBorderButton(
                        onClick = { setCounterValue(counterValue + 1) },
                        modifier = Modifier.testTag(
                            uxChangeAnnouncementsExample1IncrementCounterTestTag
                        ),
                        enabled = !isWaitingIndicatorVisible // Lock out if Example 3 running.
                    ) {
                        Text(text = stringResource(id = R.string.ux_change_announcements_increment_counter))
                    }

                    // Reset the counter liveRegion value to zero.
                    VisibleFocusBorderButton(
                        onClick = { setCounterValue(0) },
                        modifier = Modifier.testTag(uxChangeAnnouncementsExample1ResetCounterTestTag),
                        enabled = !isWaitingIndicatorVisible // Lock out if Example 3 running.
                    ) {
                        Text(text = stringResource(id = R.string.ux_change_announcements_reset_counter))
                    }
                }

                // Good example 2: Announcing visibility with announceForAccessibility
                GoodExampleHeading(
                    text = stringResource(id = R.string.ux_change_announcements_example_2_header),
                    modifier = Modifier.testTag(uxChangeAnnouncementsExample2HeadingTestTag)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Button to toggle the conditional text's visibility.
                val (isTextVisible, setIsTextVisible) = rememberSaveable { mutableStateOf(false) }
                val v = LocalView.current
                val message = stringResource(
                    id = if (isTextVisible)
                        R.string.ux_change_announcements_example_2_text_hidden
                    else
                        R.string.ux_change_announcements_example_2_text
                )
                VisibleFocusBorderButton(
                    onClick = {
                        setIsTextVisible(!isTextVisible)

                        // Announce change of text visibility. Required because visibility change is
                        // not announced, even by liveRegions.
                        // Key Technique: Use View.announceForAccessibility() to make TalkBack
                        // announcements only when necessary.
                        v.announceForAccessibility(message)
                    },
                    modifier = Modifier.testTag(uxChangeAnnouncementsExample2ButtonTestTag),
                    enabled = !isWaitingIndicatorVisible // Lock out if Example 3 running.
                ) {
                    Text(
                        text = stringResource(
                            id = if (isTextVisible)
                                R.string.ux_change_announcements_example_2_hide_text
                            else
                                R.string.ux_change_announcements_example_2_show_text
                        )
                    )
                }

                // Display the conditional text. As liveRegions do not announce their visibility and
                // this text's value is not dynamically changed, there is no reason for it to have
                // liveRegion semantics.
                if (isTextVisible) {
                    Text(
                        text = stringResource(id = R.string.ux_change_announcements_example_2_text),
                        modifier = Modifier
                            .testTag(uxChangeAnnouncementsExample2LiveRegionTextTestTag)
                            .padding(top = 8.dp),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // Good example 3: Waiting indicator
                GoodExampleHeading(
                    text = stringResource(id = R.string.ux_change_announcements_example_3_header),
                    modifier = Modifier.testTag(uxChangeAnnouncementsExample3HeadingTestTag)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Button to display the waiting indicator. See the end of this composable for the
                // waiting indicator code.
                val waitingCompletedMessage =
                    stringResource(R.string.ux_change_announcements_waiting_completed)
                VisibleFocusBorderButton(
                    onClick = {
                        isWaitingIndicatorVisible = true

                        // Announce start of waiting indicator display.
                        // Key technique: Use View.announceForAccessibility() to make TalkBack
                        // announcements only when necessary.
                        v.announceForAccessibility(waitingMessage)
                    },
                    modifier = Modifier.testTag(uxChangeAnnouncementsExample3ButtonTestTag),
                    enabled = !isWaitingIndicatorVisible // Lock out if Example 3 running.
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.ux_change_announcements_example_3_show_waiting_indicator
                        )
                    )
                }

                // Begin a timer to close the waiting indicator.
                // Note: This effect would normally occur as part of a lower layer process, mediated
                // by a state holder such as a ViewModel.
                LaunchedEffect(isWaitingIndicatorVisible) {
                    if (isWaitingIndicatorVisible) {
                        delay(15000L)
                        isWaitingIndicatorVisible = false

                        // Announce end of waiting indicator display.
                        // Key technique: Use View.announceForAccessibility() to make TalkBack
                        // announcements only when necessary.
                        v.announceForAccessibility(waitingCompletedMessage)
                    }
                }

                // Good example 4: Waiting indicator in AlertDialog
                // Note: Using an AlertDialog automatically focuses TalkBack on the initial dialog
                // component, eliminating the need to use View.announceForAccessibility(). It also
                // disables all controls on the underlying screen and confines focus to its enclosed
                // content, in this case the ProgressIndicator.
                GoodExampleHeading(
                    text = stringResource(id = R.string.ux_change_announcements_example_4_header),
                    modifier = Modifier.testTag(uxChangeAnnouncementsExample4HeadingTestTag)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Button to launch the waiting indicator dialog
                var isWaitingDialogVisible by remember { mutableStateOf(false) }
                VisibleFocusBorderButton(
                    onClick = {
                        isWaitingDialogVisible = true
                    },
                    modifier = Modifier.testTag(uxChangeAnnouncementsExample4ButtonTestTag),
                    enabled = !isWaitingIndicatorVisible // Lock out if Example 3 running.
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.ux_change_announcements_example_4_show_waiting_indicator
                        )
                    )
                }

                // Display the waiting indicator dialog
                if (isWaitingDialogVisible) {
                    BasicAlertDialog(
                        onDismissRequest = {
                            isWaitingDialogVisible = false
                        },
                        modifier = Modifier.testTag(uxChangeAnnouncementsExample4AlertTestTag)
                    ) {
                        Surface(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight(),
                            shape = MaterialTheme.shapes.large,
                            tonalElevation = AlertDialogDefaults.TonalElevation
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .semantics {
                                        contentDescription = waitingMessage
                                    }
                                    .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                            )
                        }
                    }
                }

                // Begin a timer to close the waiting indicator dialog.
                // Note: This effect would normally occur as part of a lower layer process, mediated
                // by a state holder such as a ViewModel.
                LaunchedEffect(isWaitingDialogVisible) {
                    if (isWaitingDialogVisible) {
                        delay(15000L)
                        isWaitingDialogVisible = false

                        // Announce end of waiting dialog display.
                        // Key technique: Use View.announceForAccessibility() to make TalkBack
                        // announcements only when necessary.
                        v.announceForAccessibility(waitingCompletedMessage)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Example 3 ProgressIndicator is laid out here in order to center it on screen
            // independent of the scrolling Column.
            if (isWaitingIndicatorVisible) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .testTag(uxChangeAnnouncementsExample3ProgressIndicatorTestTag)
                        .semantics {
                            // Key Technique: Set the contentDescription to announce the control
                            // properly should the user focus on it in TalkBack. Note that the same
                            // waitingMessage is used both the announce its launch above and to
                            // describe the control here.
                            contentDescription = waitingMessage
                        }
                        .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        UxChangeAnnouncementsScreen {}
    }
}