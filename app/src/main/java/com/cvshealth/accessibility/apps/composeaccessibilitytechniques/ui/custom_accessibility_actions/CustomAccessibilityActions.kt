/*
   Copyright 2023-2025 CVS Health and/or one of its affiliates

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
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.hideFromAccessibility
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
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SnackbarLauncher
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.VisibleFocusBorderIconButton
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.visibleCardFocusBorder
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val customAccessibilityActionsHeadingTestTag = "customAccessibilityActionsHeading"
const val customAccessibilityActionsExample1CardTestTag = "customAccessibilityActionsExample1Card"
const val customAccessibilityActionsExample1LikeButtonTestTag = "customAccessibilityActionsExample1LikeButton"
const val customAccessibilityActionsExample1ShareButtonTestTag = "customAccessibilityActionsExample1ShareButton"
const val customAccessibilityActionsExample1ReportButtonTestTag = "customAccessibilityActionsExample1ReportButton"
const val customAccessibilityActionsExample2CardTestTag = "customAccessibilityActionsExample2Card"
const val customAccessibilityActionsExample3CardTestTag = "customAccessibilityActionsExample3Card"

/**
 * Demonstrate techniques for adding custom accessibility actions.
 *
 * Applies [GenericScaffold] to wrap the screen content. Hosts Snackbars.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@Composable
fun CustomAccessibilityActionsScreen(
    onBackPressed: () -> Unit
) {
    val viewModel = viewModel<CustomAccessibilityActionsViewModel>()
    val viewState: CustomActionScreenState by viewModel.customActionScreenState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarLauncher = SnackbarLauncher(rememberCoroutineScope(), snackbarHostState)
    val context = LocalContext.current

    // Launch Snackbar(s) for activated buttons. Normally would also change the display state of those
    // buttons in the composables below and/or navigate to prompts for additional information.
    // Instead state is cleared to catch next button activation.
    if (viewState.cardStates.any { (_, cardState) -> cardState.actionsActivated.isNotEmpty() }) {
        LaunchedEffect(viewState) {
            viewState.cardStates.forEach { (cardId, cardState) ->
                cardState.actionsActivated.forEach { actionType ->
                    val messageEvent = CustomActionMessageEvent(actionType, cardId)
                    displayMessageEvent(context, snackbarLauncher, messageEvent)
                }
            }
            viewModel.clearMessageEvents()
        }
    }

    GenericScaffold(
        title = stringResource(id = R.string.custom_accessibility_actions_title),
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
    ComposeAccessibilityTechniquesTheme {
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
            .visibleCardFocusBorder()
    ) {
        // Moving Modifier.clickable from Card to an otherwise unnecessary inner Column to fix
        // TalkBack reading order issues.
        Column(
            modifier = Modifier.clickable(
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
            .visibleCardFocusBorder()
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
        // Modifier.semantics { hideFromAccessibility() } to each button (see Example 3).
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
    ComposeAccessibilityTechniquesTheme {
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
            .visibleCardFocusBorder()
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
        // Modifier.semantics { hideFromAccessibility() }. The buttons will remain touch-clickable
        // and keyboard-activatable. Alternatively, use Modifier.clearAndSetSemantics {} on the
        // surrounding Row (see Example 2).
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .clearAndSetSemantics { },
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            LikeButton(cardId, Modifier.semantics { hideFromAccessibility() }, handleMessageEvent)
            ShareButton(cardId, Modifier.semantics { hideFromAccessibility() }, handleMessageEvent)
            ReportButton(cardId, Modifier.semantics { hideFromAccessibility() }, handleMessageEvent)
        }
        Spacer(modifier = Modifier.height(8.dp))
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
            GoodExample3(handleMessageEvent = {})
        }
    }
}

/**
 * Display a generic action icon button for a post.
 *
 * Applies a visible focus border and minimumInteractiveComponentSize to the icon button, and sets a
 * contentDescription to the button's icon for accessibility.
 *
 * @param customActionType the [CustomActionType] to be triggered by this button
 * @param cardId the id number of the post to trigger the action on
 * @param iconId the drawable resource identifier for the button's icon
 * @param contentDescriptionId the string resource identifier for the button's icon's contentDescription
 * @param modifier optional [Modifier] for the icon button
 * @param handleMessageEvent message event handler function to be called when the button is clicked
 */
@Composable
private fun GenericActionButton(
    customActionType: CustomActionType,
    cardId: Int,
    @DrawableRes iconId: Int,
    @StringRes contentDescriptionId: Int,
    modifier: Modifier = Modifier,
    handleMessageEvent: (CustomActionMessageEvent) -> Unit
) {
    VisibleFocusBorderIconButton(
        onClick = {
            handleMessageEvent(CustomActionMessageEvent(customActionType, cardId))
        },
        modifier = modifier.minimumInteractiveComponentSize()
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = stringResource(contentDescriptionId),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * Display a button to like a post.
 *
 * @param cardId the id number of the post
 * @param modifier optional [Modifier] for the icon button
 * @param handleMessageEvent message event handler function to be called when the button is clicked
 */
@Composable
private fun LikeButton(
    cardId: Int,
    modifier: Modifier = Modifier,
    handleMessageEvent: (CustomActionMessageEvent) -> Unit
) {
    GenericActionButton(
        customActionType = CustomActionType.Like,
        cardId = cardId,
        iconId = R.drawable.ic_plus_fill,
        contentDescriptionId = R.string.custom_accessibility_actions_example_like_button,
        modifier = modifier,
        handleMessageEvent = handleMessageEvent
    )
}

/**
 * Display a button to share a post.
 *
 * @param cardId the id number of the post
 * @param modifier optional [Modifier] for the icon button
 * @param handleMessageEvent message event handler function to be called when the button is clicked
 */
@Composable
private fun ShareButton(
    cardId: Int,
    modifier: Modifier = Modifier,
    handleMessageEvent: (CustomActionMessageEvent) -> Unit
) {
    GenericActionButton(
        customActionType = CustomActionType.Share,
        cardId = cardId,
        iconId = R.drawable.ic_share_fill,
        contentDescriptionId = R.string.custom_accessibility_actions_example_share_button,
        modifier = modifier,
        handleMessageEvent = handleMessageEvent
    )
}

/**
 * Display a button to report a post as inappropriate.
 *
 * @param cardId the id number of the post
 * @param modifier optional [Modifier] for the icon button
 * @param handleMessageEvent message event handler function to be called when the button is clicked
 */
@Composable
private fun ReportButton(
    cardId: Int,
    modifier: Modifier = Modifier,
    handleMessageEvent: (CustomActionMessageEvent) -> Unit
) {
    GenericActionButton(
        customActionType = CustomActionType.Report,
        cardId = cardId,
        iconId = R.drawable.ic_minus_fill,
        contentDescriptionId = R.string.custom_accessibility_actions_example_report_button,
        modifier = modifier,
        handleMessageEvent = handleMessageEvent
    )
}

/**
 * Displays a Snackbar with the appropriate message string for the [CustomActionMessageEvent]
 * emitted by the [CustomAccessibilityActionsViewModel].
 *
 * @param context the [Context] necessary to retrieve string resource values
 * @param snackbarLauncher a [SnackbarLauncher] that encapsulates data necessary to show a Snackbar
 * @param messageEvent the [CustomActionMessageEvent] to be announced
 */
private fun displayMessageEvent(
    context: Context,
    snackbarLauncher: SnackbarLauncher,
    messageEvent: CustomActionMessageEvent
) {
    val messageId = when (messageEvent.actionType) {
        CustomActionType.ShowDetails -> R.string.custom_accessibility_actions_show_details_event
        CustomActionType.Like -> R.string.custom_accessibility_actions_like_event
        CustomActionType.Share -> R.string.custom_accessibility_actions_share_event
        CustomActionType.Report -> R.string.custom_accessibility_actions_report_event
    }
    val message = context.getString(messageId, messageEvent.cardId)
    snackbarLauncher.show(message)
}