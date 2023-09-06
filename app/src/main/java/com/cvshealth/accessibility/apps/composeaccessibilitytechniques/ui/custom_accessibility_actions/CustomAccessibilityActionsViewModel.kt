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

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

    fun clearMessageEvents() {
        _customActionScreenState.value = initialCustomActionScreenState
    }
}

enum class CustomActionType {
    ShowDetails,
    Like,
    Share,
    Report
}

data class CustomActionMessageEvent(
    val actionType: CustomActionType,
    val cardId: Int
)

data class CustomActionScreenState(
    val cardStates: Map<Int, CustomActionCardState>
)

data class CustomActionCardState(
    val actionsActivated: Set<CustomActionType>
)