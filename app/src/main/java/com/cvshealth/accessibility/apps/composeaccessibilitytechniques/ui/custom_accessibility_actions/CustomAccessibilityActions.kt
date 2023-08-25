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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_accessibility_actions

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BadExampleTitle
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleTitle
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.CvsRed

const val customAccessibilityActionsHeadingTestTag = "customAccessibilityActionsHeading"
const val customAccessibilityActionsExample1CardTestTag = "customAccessibilityActionsExample1Card"
const val customAccessibilityActionsExample1LikeButtonTestTag = "customAccessibilityActionsExample1LikeButton"
const val customAccessibilityActionsExample1ShareButtonTestTag = "customAccessibilityActionsExample1ShareButton"
const val customAccessibilityActionsExample1ReportButtonTestTag = "customAccessibilityActionsExample1ReportButton"
const val customAccessibilityActionsExample2CardTestTag = "customAccessibilityActionsExample2Card"
const val customAccessibilityActionsExample3CardTestTag = "customAccessibilityActionsExample3Card"

@Composable
fun CustomAccessibilityActionsScreen(
    onBackPressed: () -> Unit
) {
    val viewModel = viewModel<CustomAccessibilityActionsViewModel>()
    val viewState: CustomActionScreenState by viewModel.customActionScreenState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Launch Toast(s) for activated buttons. Normally would also change the display state of those
    // buttons in the composables below and/or navigate to prompts for additional information.
    // Instead state is cleared to catch next button activation.
    //
    // Note: Toasts are used instead of Snackbars to keep the code simpler and due to accessibility
    // considerations. While Toasts are worse for accessibility in many ways, currently Compose
    // Snackbars have a very bad TalkBack experience with regard to focus order, modal state, etc.
    if (viewState.cardStates.any { (_, cardState) -> cardState.actionsActivated.isNotEmpty() }) {
        LaunchedEffect(viewState) {
            viewState.cardStates.forEach { cardId, cardState ->
                cardState.actionsActivated.forEach { actionType ->
                    val messageEvent = CustomActionMessageEvent(actionType, cardId)
                    displayMessageEvent(context, messageEvent)
                }
            }
            viewModel.clearMessageEvents()
        }
    }

    GenericScaffold(
        title = stringResource(id = R.string.custom_accessibility_actions_title),
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
                text = stringResource(id = R.string.custom_accessibility_actions_heading),
                modifier = Modifier.testTag(customAccessibilityActionsHeadingTestTag)
            )
            BodyText(textId = R.string.custom_accessibility_actions_description_1)
            BodyText(textId = R.string.custom_accessibility_actions_description_2)

            val handleMessageEvent = { messageEvent: CustomActionMessageEvent ->
                viewModel.handleMessageEvent(messageEvent)
            }

            BadExample1(handleMessageEvent)
            GoodExample2(handleMessageEvent)
            GoodExample3(handleMessageEvent)

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme() {
        CustomAccessibilityActionsScreen {}
    }
}

@Composable
private fun BadExample1(
    handleMessageEvent: (CustomActionMessageEvent) -> Unit
) {
    // Bad example 1: Card without custom accessibility actions
    val cardId = 1
    val showDetailsCLickLabel = stringResource(
        id = R.string.custom_accessibility_actions_see_details, cardId
    )
    OutlinedCard(
        modifier = Modifier
            .testTag(customAccessibilityActionsExample1CardTestTag)
            .padding(top = 8.dp)
            .clickable(
                onClickLabel = showDetailsCLickLabel
            ) {
                handleMessageEvent(CustomActionMessageEvent(CustomActionType.ShowDetails, cardId))
            }
    ) {
        // Note: Do not put a testTag() on this composable, because that would introduce an extra
        // semantics node, making Compose jUnit testing of merged content impossible. While that
        // would not affect TalkBack's behavior for users, it does affect automated testing.
        BadExampleTitle(
            textId = R.string.custom_accessibility_actions_example_1_card_heading,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        BodyText(
            textId = R.string.custom_accessibility_actions_example_1_card_description,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            LikeButton(
                cardId,
                modifier = Modifier.testTag(customAccessibilityActionsExample1LikeButtonTestTag),
                handleMessageEvent
            )
            ShareButton(
                cardId,
                modifier = Modifier.testTag(customAccessibilityActionsExample1ShareButtonTestTag),
                handleMessageEvent
            )
            ReportButton(
                cardId,
                modifier = Modifier.testTag(customAccessibilityActionsExample1ReportButtonTestTag),
                handleMessageEvent
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewBadExample1() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            BadExample1(handleMessageEvent = {})
        }
    }
}

@Composable
private fun GoodExample2(
    handleMessageEvent: (CustomActionMessageEvent) -> Unit
) {
    // Good example 2: Card with custom accessibility actions
    val cardId = 2
    // Tip: Retrieve custom accessibility action labels in the outer Composable context, not inside
    // Modifier.semantics { } where stringResource() is unavailable.
    val showDetailsCLickLabel = stringResource(
        id = R.string.custom_accessibility_actions_see_details, cardId
    )
    val likeActionLabel = stringResource(
        id = R.string.custom_accessibility_actions_example_like_button
    )
    val shareActionLabel = stringResource(
        id = R.string.custom_accessibility_actions_example_share_button, cardId
    )
    val reportActionLabel = stringResource(
        id = R.string.custom_accessibility_actions_example_report_button, cardId
    )
    OutlinedCard(
        modifier = Modifier
            .testTag(customAccessibilityActionsExample2CardTestTag)
            .padding(top = 8.dp)
            // Tip: If the enclosing layout is clickable, use Modifier.clickable, not the
            // Modifier.semantics onClick() property; otherwise, the layout composable will not be
            // touch-clickable.
            .clickable(
                onClickLabel = showDetailsCLickLabel
            ) {
                handleMessageEvent(CustomActionMessageEvent(CustomActionType.ShowDetails, cardId))
            }
            .semantics(mergeDescendants = true) {
                // Key technique 1: Declare customActions in Modifier.semantics{}.
                customActions = listOf(
                    CustomAccessibilityAction(showDetailsCLickLabel) {
                        handleMessageEvent(
                            CustomActionMessageEvent(CustomActionType.ShowDetails, cardId)
                        )
                        true
                    },
                    CustomAccessibilityAction(likeActionLabel) {
                        handleMessageEvent(CustomActionMessageEvent(CustomActionType.Like, cardId))
                        true
                    },
                    CustomAccessibilityAction(shareActionLabel) {
                        handleMessageEvent(CustomActionMessageEvent(CustomActionType.Share, cardId))
                        true
                    },
                    CustomAccessibilityAction(reportActionLabel) {
                        handleMessageEvent(
                            CustomActionMessageEvent(CustomActionType.Report, cardId)
                        )
                        true
                    }
                )
            }
    ) {
        // Note: Do not put a testTag() on this composable, because that would introduce an extra
        // semantics node, making Compose jUnit testing of merged content impossible. While that
        // would not affect TalkBack's behavior for users, it does affect automated testing.
        GoodExampleTitle(
            textId = R.string.custom_accessibility_actions_example_2_card_heading,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        BodyText(
            textId = R.string.custom_accessibility_actions_example_2_card_description,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        // Key Technique 2a: Remove the buttons from the accessibility tree using
        // Modifier.clearAndSetSemantics() on the enclosing Row. The buttons will remain touch-
        // clickable and keyboard-activateable. Alternatively, apply
        // Modifier.semantics { invisibleToUser() } to each button (see Example 3).
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .clearAndSetSemantics { },
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            LikeButton(cardId = cardId, handleMessageEvent = handleMessageEvent)
            ShareButton(cardId = cardId, handleMessageEvent = handleMessageEvent)
            ReportButton(cardId = cardId, handleMessageEvent = handleMessageEvent)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewGoodExample2() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample2(handleMessageEvent = {})
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun GoodExample3(
    handleMessageEvent: (CustomActionMessageEvent) -> Unit
) {
    // Good example 3: Card with custom accessibility actions
    val cardId = 3
    val showDetailsCLickLabel = stringResource(
        id = R.string.custom_accessibility_actions_see_details, cardId
    )
    val likeActionLabel = stringResource(
        id = R.string.custom_accessibility_actions_example_like_button
    )
    val shareActionLabel = stringResource(
        id = R.string.custom_accessibility_actions_example_share_button, cardId
    )
    val reportActionLabel = stringResource(
        id = R.string.custom_accessibility_actions_example_report_button, cardId
    )
    OutlinedCard(
        modifier = Modifier
            .testTag(customAccessibilityActionsExample3CardTestTag)
            .padding(top = 8.dp)
            .clickable(
                onClickLabel = showDetailsCLickLabel
            ) {
                handleMessageEvent(CustomActionMessageEvent(CustomActionType.ShowDetails, cardId))
            }
            .semantics(mergeDescendants = true) {
                // Key technique 1: Declare customActions in Modifier.semantics{}.
                customActions = listOf(
                    CustomAccessibilityAction(showDetailsCLickLabel) {
                        handleMessageEvent(
                            CustomActionMessageEvent(CustomActionType.ShowDetails, cardId)
                        )
                        true
                    },
                    CustomAccessibilityAction(likeActionLabel) {
                        handleMessageEvent(CustomActionMessageEvent(CustomActionType.Like, cardId))
                        true
                    },
                    CustomAccessibilityAction(shareActionLabel) {
                        handleMessageEvent(CustomActionMessageEvent(CustomActionType.Share, cardId))
                        true
                    },
                    CustomAccessibilityAction(reportActionLabel) {
                        handleMessageEvent(
                            CustomActionMessageEvent(CustomActionType.Report, cardId)
                        )
                        true
                    }
                )
            }
    ) {
        // Note: Do not put a testTag() on this composable, because that would introduce an extra
        // semantics node, making Compose jUnit testing of merged content impossible. While that
        // would not affect TalkBack's behavior for users, it does affect automated testing.
        GoodExampleTitle(
            textId = R.string.custom_accessibility_actions_example_3_card_heading,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        BodyText(
            textId = R.string.custom_accessibility_actions_example_3_card_description,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        // Key Technique 2b: Remove each button from the accessibility tree using
        // Modifier.semantics { invisibleToUser() }. The buttons will remain touch-clickable and
        // keyboard-activatable. Alternatively, use Modifier.clearAndSetSemantics {} on the
        // surrounding Row (see Example 2).
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .clearAndSetSemantics { },
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            LikeButton(cardId, Modifier.semantics { invisibleToUser() }, handleMessageEvent)
            ShareButton(cardId, Modifier.semantics { invisibleToUser() }, handleMessageEvent)
            ReportButton(cardId, Modifier.semantics { invisibleToUser() }, handleMessageEvent)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewGoodExample3() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample3(handleMessageEvent = {})
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun LikeButton(
    cardId: Int,
    modifier: Modifier = Modifier,
    handleMessageEvent: (CustomActionMessageEvent) -> Unit
) {
    IconButton(
        onClick = {
            handleMessageEvent(CustomActionMessageEvent(CustomActionType.Like, cardId))
        },
        modifier = modifier.minimumInteractiveComponentSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_plus_fill),
            contentDescription = stringResource(
                id = R.string.custom_accessibility_actions_example_like_button
            ),
            tint = CvsRed
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ShareButton(
    cardId: Int,
    modifier: Modifier = Modifier,
    handleMessageEvent: (CustomActionMessageEvent) -> Unit
) {
    IconButton(
        onClick = {
            handleMessageEvent(CustomActionMessageEvent(CustomActionType.Share, cardId))
        },
        modifier = modifier.minimumInteractiveComponentSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_share_fill),
            contentDescription = stringResource(
                id = R.string.custom_accessibility_actions_example_share_button
            ),
            tint = CvsRed
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ReportButton(
    cardId: Int,
    modifier: Modifier = Modifier,
    handleMessageEvent: (CustomActionMessageEvent) -> Unit
) {
    IconButton(
        onClick = {
            handleMessageEvent(CustomActionMessageEvent(CustomActionType.Report, cardId))
        },
        modifier = modifier.minimumInteractiveComponentSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_minus_fill),
            contentDescription = stringResource(
                id = R.string.custom_accessibility_actions_example_report_button
            ),
            tint = CvsRed
        )
    }
}

private fun displayMessageEvent(
    context: Context,
    messageEvent: CustomActionMessageEvent
) {
    val messageId = when (messageEvent.actionType) {
        CustomActionType.ShowDetails -> R.string.custom_accessibility_actions_show_details_event
        CustomActionType.Like -> R.string.custom_accessibility_actions_like_event
        CustomActionType.Share -> R.string.custom_accessibility_actions_share_event
        CustomActionType.Report -> R.string.custom_accessibility_actions_report_event
    }
    val message = context.getString(messageId, messageEvent.cardId)
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}