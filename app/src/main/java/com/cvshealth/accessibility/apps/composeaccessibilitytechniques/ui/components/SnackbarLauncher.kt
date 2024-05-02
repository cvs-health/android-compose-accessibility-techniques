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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Encapsulates the data and operation of displaying a Snackbar.
 *
 * @property coroutineScope the [CoroutineScope] in which to launch Snackbar display
 * @property snackbarHostState the [SnackbarHostState] provided to the Scaffold and used to show a
 * Snackbar
 */
class SnackbarLauncher(
    private val coroutineScope: CoroutineScope,
    val snackbarHostState: SnackbarHostState,
) {
    /**
     * Display a simple Snackbar pop-up message without actions.
     *
     * @param message text to be shown in the Snackbar
     * @param withDismissAction a boolean to show a dismiss action in the Snackbar
     * @param duration duration to control how long snackbar will be shown in [SnackbarHost], either
     * [SnackbarDuration.Short], [SnackbarDuration.Long] or [SnackbarDuration.Indefinite].
     */
    fun show(
        message: String,
        withDismissAction: Boolean = true,
        duration: SnackbarDuration = SnackbarDuration.Long
    ) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                withDismissAction = withDismissAction,
                duration = duration
            )
        }
    }
}