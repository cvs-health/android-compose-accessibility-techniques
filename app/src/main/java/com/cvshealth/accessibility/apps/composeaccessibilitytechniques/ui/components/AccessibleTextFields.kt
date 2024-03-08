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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components

import android.view.KeyEvent.ACTION_UP
import android.view.KeyEvent.KEYCODE_DPAD_DOWN
import android.view.KeyEvent.KEYCODE_DPAD_UP
import android.view.KeyEvent.KEYCODE_TAB
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val FOCUS_DEBOUNCE_TIME = 250L

fun Modifier.nextOnTabAndHandleEnter(
    enterCallback: (() -> Unit)? = null
): Modifier {
    return this.composed {
        val focusManager = LocalFocusManager.current
        // Key technique 2a: Remember the focus state.
        var hasFocus by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        this@composed
            // Key techniques 1 & 2b: Debounce focus changes and track the focus state.
            .onFocusChanged { focusState ->
                scope.launch {
                    delay(FOCUS_DEBOUNCE_TIME)
                    hasFocus = focusState.hasFocus
                }
            }
            // Key technique 3: If focus remains on this control, handle Tab, Shift+Tab, Enter, and
            // the Up and Down direction pad keys before a keyEvent becomes part of the TextField's
            // text data. (Note that Enter key handling is optional and is hoisted to the caller's
            // context.)
            .onPreviewKeyEvent { keyEvent ->
                if (hasFocus && keyEvent.nativeKeyEvent.keyCode == KEYCODE_TAB) {
                    if (keyEvent.nativeKeyEvent.action == ACTION_UP) {
                        if (keyEvent.nativeKeyEvent.isShiftPressed) {
                            focusManager.moveFocus(FocusDirection.Previous)
                        } else {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    }
                    true
                } else if (hasFocus && keyEvent.nativeKeyEvent.keyCode == KEYCODE_DPAD_DOWN) {
                    if (keyEvent.nativeKeyEvent.action == ACTION_UP) {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                    true
                } else if (hasFocus && keyEvent.nativeKeyEvent.keyCode == KEYCODE_DPAD_UP) {
                    if (keyEvent.nativeKeyEvent.action == ACTION_UP) {
                        focusManager.moveFocus(FocusDirection.Up)
                    }
                    true
                } else if (
                    enterCallback != null
                    && (keyEvent.key == Key.Enter || keyEvent.key == Key.NumPadEnter)
                ) {
                    if (keyEvent.nativeKeyEvent.action == ACTION_UP) {
                        enterCallback()
                    }
                    true
                } else {
                    false
                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessibleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    onEnterHandler: (() -> Unit)? = null
) = TextField(
    value,
    onValueChange,
    modifier = modifier.nextOnTabAndHandleEnter(onEnterHandler),
    enabled,
    readOnly,
    textStyle,
    label,
    placeholder,
    leadingIcon,
    trailingIcon,
    prefix,
    suffix,
    supportingText,
    isError,
    visualTransformation,
    keyboardOptions,
    keyboardActions,
    singleLine,
    maxLines,
    minLines,
    interactionSource,
    shape,
    colors
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessibleOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    onEnterHandler: (() -> Unit)? = null
) = OutlinedTextField(
    value,
    onValueChange,
    modifier = modifier.nextOnTabAndHandleEnter(onEnterHandler),
    enabled,
    readOnly,
    textStyle,
    label,
    placeholder,
    leadingIcon,
    trailingIcon,
    prefix,
    suffix,
    supportingText,
    isError,
    visualTransformation,
    keyboardOptions,
    keyboardActions,
    singleLine,
    maxLines,
    minLines,
    interactionSource,
    shape,
    colors
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AutofilledOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    autofillType: AutofillType,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    onEnterHandler: (() -> Unit)? = null
) {
    // Key technique: Create an AutofillNode with AutofillType(s) and state setter lambda. Connect
    // it to the LocalAutofillTree.
    val autofillNode = AutofillNode(listOf(autofillType), onFill = onValueChange)
    LocalAutofillTree.current += autofillNode

    // Define access to LocalAutofill for later use in a non-composable context.
    val currentLocalAutofill = LocalAutofill.current

    OutlinedTextField(
        value,
        onValueChange,
        modifier = modifier
            // Handle keyboard trap and (optional) keyboard Enter processing.
            .nextOnTabAndHandleEnter(onEnterHandler)
            // Key technique: on focus change, request autofill data or cancel existing request.
            .onFocusChanged { focusState ->
                currentLocalAutofill?.run {
                    if (focusState.isFocused) {
                        requestAutofillForNode(autofillNode)
                    } else {
                        cancelAutofillForNode(autofillNode)
                    }
                }
            }
            // Key technique: Set the autofillNode bounding box for pop-up positioning.
            .onGloballyPositioned { autofillNode.boundingBox = it.boundsInWindow() },
        enabled,
        readOnly,
        textStyle,
        label,
        placeholder,
        leadingIcon,
        trailingIcon,
        prefix,
        suffix,
        supportingText,
        isError,
        visualTransformation,
        keyboardOptions,
        keyboardActions,
        singleLine,
        maxLines,
        minLines,
        interactionSource,
        shape,
        colors
    )
}