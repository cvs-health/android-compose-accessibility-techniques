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
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CustomAccessibilityActionsViewModel : ViewModel() {
    private val _messageEvent = MutableSharedFlow<CustomActionMessageEvent>()
    val messageEvent = _messageEvent.asSharedFlow()

    fun showPostDetails(cardId: Int) {
        viewModelScope.launch {
            _messageEvent.emit(CustomActionMessageEvent(CustomActionType.ShowDetails, cardId))
        }
    }

    fun likePost(cardId: Int) {
        viewModelScope.launch {
            _messageEvent.emit(CustomActionMessageEvent(CustomActionType.Like, cardId))
        }
    }

    fun sharePost(cardId: Int) {
        viewModelScope.launch {
            _messageEvent.emit(CustomActionMessageEvent(CustomActionType.Share, cardId))
        }
    }

    fun reportPost(cardId: Int) {
        viewModelScope.launch {
            _messageEvent.emit(CustomActionMessageEvent(CustomActionType.Report, cardId))
        }
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