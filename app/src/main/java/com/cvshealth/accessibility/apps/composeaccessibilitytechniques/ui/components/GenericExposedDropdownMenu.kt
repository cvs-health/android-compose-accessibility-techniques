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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components

import android.view.KeyEvent.ACTION_UP
import android.view.KeyEvent.KEYCODE_ESCAPE
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import kotlinx.coroutines.delay

/**
 * Encapsulates the Exposed Dropdown menu pattern for a list of String menu options. This pattern
 * consist of the [ExposedDropdownMenuBox], TextField, DropdownMenu, and [DropdownMenuItem]
 * composable widgets, operating in coordination. This composable hoists its expanded/collapsed
 * state handling to its parent.
 *
 * This composable can also be applied either as a fixed, uneditable menu or as an unconstrained,
 * editable text field with a suggested list of values. However, editable menus pose accessibility
 * challenges for screen reader users as well as being keyboard traps and otherwise keyboard
 * inaccessible. While values can be typed into the [OutlinedTextField] with a keyboard, the
 * dropdown menu does not appear to suggest values (nor would such a list be keyboard selectable).
 *
 * GenericExposedDropdownMenu applies the following key techniques:
 * 1. Wrap the entire dropdown menu ensemble in an ExposedDropdownMenuBox.
 * 2. Remediate the TextField for Tab and Enter handling.
 * 3. Use Modifier.menuAnchor() to link the input field to the menu box.
 * 4.a. When applied as a fixed menu, use readOnly = true and onValueChange = {} to prevent editing.
 * 4.b. When applied as an editable menu, set readOnly = false and set the selected text value in
 *      the onValueChange lambda to allow TextField editing.
 * 5. Label the TextField.
 * 6. Set trailingIcon to visually indicate the collapsed/expanded state.
 * 7. When applied as an editable menu, force a non-default ImeAction to allow the Enter key handling to expand the dropdown menu.
 * 8. When applied as an editable menu, filter the list items based on the text already entered.
 * 9. Wrap the list of menu items in an ExposedDropdownMenu.
 * 10: Close the dropdown list when Esc key is pressed.
 * 11. Hold each menu item in a DropdownMenuItem.
 * 12. Set keyboard focus onto a newly-expanded dropdown menu pop-up.
 *
 * @param value current String value of the dropdown selection menu
 * @param setValue sets the current String value of the dropdown selection menu
 * @param options list of dropdown selection menu options
 * @param isExpanded whether dropdown list is expanded or not
 * @param setIsExpanded sets whether dropdown list is expanded or not
 * @param modifier setting for the ExposedDropdownMenuBox
 * @param textFieldModifier settings for the internal TextField
 * @param readOnly disallows typing in the TextField; defaults to true
 * @param supportingText optional content to display under the TextField, such as an error message
 * @param isError current error state of the TextView; defaults to false
 * @param keyboardOptions software keyboard options; defaults to KeyboardType.Text and ImeAction.Search
 * @param keyboardActions IME action callbacks; defaults to expanding dropdown menu
 * @param label floating label for the TextView
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericExposedDropdownMenu(
    value: String,
    setValue: (String) -> Unit,
    options: List<String>,
    isExpanded: Boolean,
    setIsExpanded: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    readOnly: Boolean = true,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Search
    ),
    keyboardActions: KeyboardActions = KeyboardActions {
        setIsExpanded(true)
    },
    label: @Composable () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    // Key technique 1: Wrap the dropdown menu ensemble in an ExposedDropdownMenuBox.
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { setIsExpanded(!isExpanded) },
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = textFieldModifier
                // Key technique 2: Remediate the TextField for Tab and Enter handling.
                // (Note: Only Enter key handling is required for read-only menus.)
                .nextOnTabAndHandleEnter {
                    setIsExpanded(true)
                }
                // Key technique 3: Anchor the TextField to the menu box.
                // If the text field is read-only, then the menu gets focus; if it is editable,
                // then the text field keeps focus.
                .menuAnchor(
                    if (readOnly) MenuAnchorType.PrimaryNotEditable else MenuAnchorType.PrimaryEditable
                ),
            readOnly = readOnly, // Key technique 4a: Set readOnly appropriately.
            value = value,
            onValueChange = { newValue: String ->
                // Key technique 4b: If editable, set the value; otherwise, do nothing.
                if (!readOnly) {
                    setValue(newValue)
                }
            },
            label = label, // Key technique 5: Apply a label.
            supportingText = supportingText, // Optional: Allow supporting/error messaging.
            isError = isError, // Optional: Support announcing an error state.
            // Key technique 6: Set the trailing icon to show the expanded/collapsed state.
            // If the dropdown text box is editable, make the trailing icon a secondary anchor.
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isExpanded,
                    modifier = if (readOnly) Modifier else Modifier.menuAnchor(MenuAnchorType.SecondaryEditable)
                )
            },
            // Key technique 7: If the dropdown text box is editable, force a non-default ImeAction
            // onto this field to allow the Enter key handling to expand the dropdown menu. Use
            // ImeAction.Search if none provided and supply a matching KeyboardAction that expands
            // the dropdown menu. (See also the keyboardOptions and keyboardActions parameters'
            // default values in the function signature.)
            keyboardOptions = if (
                readOnly || (
                    keyboardOptions.imeAction != ImeAction.Unspecified &&
                    keyboardOptions.imeAction != ImeAction.None &&
                    keyboardOptions.imeAction != ImeAction.Default
                )
            ) {
                keyboardOptions
            } else {
                keyboardOptions.copy(imeAction = ImeAction.Search)
            },
            keyboardActions = if (
                readOnly || (
                    keyboardOptions.imeAction != ImeAction.Unspecified &&
                    keyboardOptions.imeAction != ImeAction.None &&
                    keyboardOptions.imeAction != ImeAction.Default
                )
            ) {
                keyboardActions
            } else {
                KeyboardActions(onAny = { setIsExpanded(true) })
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        // Key technique 8: For an editable dropdown menu, filter the list items based on the text
        // field value. (Note: Could use it.startsWith() instead of it.contains(), depending on the
        // type of list filtering desired.)
        val filteredOptions = if (readOnly)
            options
        else
            options.filter { it.contains(value, ignoreCase = true) }
        if (filteredOptions.isNotEmpty()) {
            // Key technique 9: Wrap the list of menu items in an ExposedDropdownMenu.
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { setIsExpanded(false) },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    // Key technique 10: Close the dropdown list when Esc key is pressed.
                    .onPreviewKeyEvent { keyEvent ->
                        if (keyEvent.nativeKeyEvent.keyCode == KEYCODE_ESCAPE) {
                            if (keyEvent.nativeKeyEvent.action == ACTION_UP) {
                                setIsExpanded(false)
                            }
                            true
                        } else {
                            false
                        }
                    }
            ) {
                filteredOptions.forEach { option ->
                    // Key technique 11: Hold each menu item in a DropdownMenuItem.
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            setValue(option)
                            setIsExpanded(false)
                        },
                        modifier = Modifier.visibleFocusBorder(),
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }

            // Key technique 12: Set keyboard focus onto a newly-expanded dropdown menu pop-up.
            LaunchedEffect(isExpanded) {
                if (isExpanded) {
                    delay(500)
                    focusRequester.requestFocus()
                }
            }
        }
    }
}

/**
 * Encapsulates the Exposed Dropdown menu pattern for a list of String menu options. This pattern
 * consist of the [ExposedDropdownMenuBox], TextField, DropdownMenu, and [DropdownMenuItem]
 * composable widgets, operating in coordination. This composable contains its own
 * expanded/collapsed state handling. This state is not maintained across configuration changes.
 *
 * This composable can also be applied either as a fixed, uneditable menu or as an unconstrained,
 * editable text field with a suggested list of values. However, editable menus pose accessibility
 * challenges for screen reader users as well as being keyboard traps and otherwise keyboard
 * inaccessible. While values can be typed into the [OutlinedTextField] with a keyboard, the
 * dropdown menu does not appear to suggest values (nor would such a list be keyboard selectable).
 *
 * @param value current String value of the dropdown selection menu
 * @param setValue sets the current String value of the dropdown selection menu
 * @param options list of dropdown selection menu options
 * @param modifier setting for the ExposedDropdownMenuBox
 * @param textFieldModifier settings for the internal TextField
 * @param readOnly disallows typing in the TextField; defaults to true
 * @param supportingText optional content to display under the TextField, such as an error message
 * @param isError current error state of the TextView; defaults to false
 * @param keyboardOptions software keyboard options; defaults to KeyboardType.Text and ImeAction.Search
 * @param keyboardActions IME action callbacks; defaults to expanding dropdown menu
 * @param label floating label for the TextView
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericExposedDropdownMenu(
    value: String,
    setValue: (String) -> Unit,
    options: List<String>,
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    readOnly: Boolean = true,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Search
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    label: @Composable () -> Unit,
) {
    val (isExpanded, setIsExpanded) = remember { mutableStateOf(false) }
    GenericExposedDropdownMenu(
        value = value,
        setValue = setValue,
        isExpanded = isExpanded,
        setIsExpanded = setIsExpanded,
        options = options,
        modifier = modifier,
        textFieldModifier = textFieldModifier,
        readOnly = readOnly,
        supportingText = supportingText,
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = if (keyboardActions != KeyboardActions.Default) {
            keyboardActions
        } else {
            KeyboardActions {
                setIsExpanded(true)
            }
        },
        label = label,
    )
}

@Preview(showBackground = true)
@Composable
private fun EmptyDropdownMenuPreview() {
    ComposeAccessibilityTechniquesTheme {
        val options = listOf("Yes", "No", "It depends")
        val (menuValue, setMenuValue) = remember { mutableStateOf("")}
        GenericExposedDropdownMenu(value = menuValue, setValue = setMenuValue, options = options) {
            Text("Are you sure?")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FirstDefaultDropdownMenuPreview() {
    ComposeAccessibilityTechniquesTheme {
        val options = listOf("Yes", "No", "It depends")
        val (menuValue, setMenuValue) = remember { mutableStateOf(options.first())}
        GenericExposedDropdownMenu(value = menuValue, setValue = setMenuValue, options = options) {
            Text("Are you sure?")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorDropdownMenuPreview() {
    ComposeAccessibilityTechniquesTheme {
        val options = listOf("Yes", "No", "It depends")
        val (menuValue, setMenuValue) = remember { mutableStateOf("")}
        GenericExposedDropdownMenu(
            value = menuValue,
            setValue = setMenuValue,
            options = options,
            supportingText = {
                Text("Error: A choice is required. Please select whether or not you are sure.")
            },
            isError = true,
        ) {
            Text("Are you sure? (required)")
        }
    }
}