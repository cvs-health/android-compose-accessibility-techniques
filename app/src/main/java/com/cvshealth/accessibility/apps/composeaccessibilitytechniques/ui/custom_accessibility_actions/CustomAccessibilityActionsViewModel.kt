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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_accessibility_actions

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for demonstrating custom accessibility actions.
 *
 * Surfaces methods to accept UI events from the Compose UI and a StateFlow of values to be
 * collected by the Compose UI for display.
 *
 * Supports unidirectional data flow by separating this state holder from the Compose UI code.
 * Real-world implementations would implement domain logic and data layers in addition to a state
 * holder.
 */
class CustomAccessibilityActionsViewModel : ViewModel() {
    private val initialCustomActionScreenState = CustomActionScreenState(
        cardStates = mapOf(
            1 to CustomActionCardState(actionsActivated = setOf()),
            2 to CustomActionCardState(actionsActivated = setOf()),
            3 to CustomActionCardState(actionsActivated = setOf()),
        )
    )
    private val _customActionScreenState = MutableStateFlow(initialCustomActionScreenState)
    val customActionScreenState = _customActionScreenState.asStateFlow()

    /**
     * Handle UI events that will trigger a message back to the UI.
     *
     * This kind of event handling cycle would normally involve domain and data layer logic. It is
     * simplified here for demonstration purposes, while still making clear the importance of
     * handling events in a state holder, such as a ViewModel, rather than in the Compose UI code
     * itself.
     *
     * @param messageEvent a [CustomActionMessageEvent] describing the UI action and post id
     */
    fun handleMessageEvent(messageEvent: CustomActionMessageEvent) {
        if (messageEvent.cardId < 0 ||
            messageEvent.cardId > customActionScreenState.value.cardStates.size
        ) {
            return
        }

        _customActionScreenState.update { state ->
            val newActionsActivated =
                state.cardStates[messageEvent.cardId]?.actionsActivated?.plus(
                    messageEvent.actionType
                )
            return@update if (newActionsActivated != null) {
                val newCardStates = state.cardStates.toMutableMap()
                newCardStates.replace(messageEvent.cardId, CustomActionCardState(newActionsActivated))
                CustomActionScreenState(newCardStates)
            } else
                state
        }
    }

    /**
     * Clears all message events queued for UI display.
     */
    fun clearMessageEvents() {
        _customActionScreenState.value = initialCustomActionScreenState
    }
}

/**
 * Enumerates the available UI actions for any given card.
 */
enum class CustomActionType {
    ShowDetails,
    Like,
    Share,
    Report
}

/**
 * Holds a UI event, indicating the action invoked and the card it was invoked on.
 */
data class CustomActionMessageEvent(
    val actionType: CustomActionType,
    val cardId: Int
)

/**
 * Holds a map of card ids to actions activated on those cards.
 */
data class CustomActionScreenState(
    val cardStates: Map<Int, CustomActionCardState>
)

/**
 * Holds the set of custom actions that have been activated (on an unspecified card).
 */
data class CustomActionCardState(
    val actionsActivated: Set<CustomActionType>
)