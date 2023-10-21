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

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

/**
 * GenericExposedDropdownMenu encapsulates the Exposed Dropdown menu pattern for a list of String
 * menu options. This pattern consist of the ExposedDropdownMenuBox, TextField, DropdownMenu, and
 * DropdownMenuItem composable widgets, operating in coordination. This composable contains its own
 * expanded/collapsed state handling.
 *
 * This composable can also be applied either as a fixed, uneditable menu or as an unconstrained,
 * editable text field with a suggested list of values. Note that editable menus pose accessibility
 * challenges for screen reader users and should be applied with caution.
 *
 * GenericExposedDropdownMenu applies the following key techniques:
 * 1. Wrap the entire dropdown menu ensemble in an ExposedDropdownMenuBox.
 * 2. Use Modifier.menuAnchor() to link the input field to the menu box.
 * 3.a. When applied as a fixed menu, use readOnly = true and onValueChange = {} to prevent editing.
 * 3.b. When applied as an editable menu, set readOnly = false and set the selected text value in
 *      the onValueChange lambda to allow TextField editing.
 * 4. Label the TextField.
 * 5. Set trailingIcon to visually indicate the collapsed/expanded state.
 * 6. When applied as an editable menu, filter the list items based on the text already entered.
 * 7. Wrap the list of menu items in an ExposedDropdownMenu.
 * 8. Hold each menu item in a DropdownMenuItem.
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
    label: @Composable () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    // Key technique 1: Wrap the dropdown menu ensemble in an ExposedDropdownMenuBox.
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
        modifier = modifier,
    ) {
        TextField(
            modifier = textFieldModifier
                .menuAnchor(), // Key technique 2: Anchor the TextField to the menu box.
            readOnly = readOnly, // Key technique 3a: Set readOnly appropriately.
            value = value,
            onValueChange = { newValue ->
                // Key technique 3b: If editable, set the value; otherwise, do nothing.
                if (!readOnly) {
                    setValue(newValue)
                }
            },
            label = label, // Key technique 4: Apply a label.
            supportingText = supportingText, // Optional: Allow supporting/error messaging.
            isError = isError, // Optional: Support announcing an error state.
            // Key technique 5: Set the trailing icon to show the expanded/collapsed state.
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )

        // Key technique 6: For an editable dropdown menu, filter the list items based on the text
        // field value. (Note: Could use it.startsWith() instead of it.contains(), depending on the
        // type of list filtering desired.)
        val filteredOptions = if (readOnly)
            options
        else
            options.filter { it.contains(value, ignoreCase = true) }
        if (filteredOptions.isNotEmpty()) {
            // Key technique 7: Wrap the list of menu items in an ExposedDropdownMenu.
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
            ) {
                filteredOptions.forEach { option ->
                    // Key technique 8: Hold each menu item in a DropdownMenuItem.
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            setValue(option)
                            isExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyDropdownMenuPreview() {
    ComposeAccessibilityTechniquesTheme() {
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
    ComposeAccessibilityTechniquesTheme() {
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
    ComposeAccessibilityTechniquesTheme() {
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