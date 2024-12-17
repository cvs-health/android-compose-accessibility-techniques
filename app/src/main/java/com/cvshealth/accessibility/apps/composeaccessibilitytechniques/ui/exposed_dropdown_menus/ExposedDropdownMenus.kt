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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.exposed_dropdown_menus

import android.view.KeyEvent.ACTION_UP
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.KeyEvent.KEYCODE_ESCAPE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.databinding.ViewExposedDropdownMenuBinding
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericExposedDropdownMenu
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.ProblematicExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.nextOnTabAndHandleEnter
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import kotlinx.coroutines.delay


const val exposedDropdownMenusHeadingTestTag = "exposedDropdownMenusHeading"
const val exposedDropdownMenusExample1HeadingTestTag = "exposedDropdownMenusExample1Heading"
const val exposedDropdownMenusExample1DropdownMenuBoxTestTag =
    "exposedDropdownMenusExample1DropdownMenuBox"
const val exposedDropdownMenusExample1DropdownMenuTextFieldTestTag =
    "exposedDropdownMenusExample1DropdownMenuTextField"
const val exposedDropdownMenusExample2HeadingTestTag = "exposedDropdownMenusExample2Heading"
const val exposedDropdownMenusExample2AndroidViewTestTag = "exposedDropdownMenusExample2AndroidView"
const val exposedDropdownMenusExample3HeadingTestTag = "exposedDropdownMenusExample3Heading"
const val exposedDropdownMenusExample3DropdownMenuBoxTestTag =
    "exposedDropdownMenusExample3DropdownMenuBox"
const val exposedDropdownMenusExample3DropdownMenuTextFieldTestTag =
    "exposedDropdownMenusExample3DropdownMenuTextField"

/**
 * Demonstrate accessibility techniques for exposed dropdown selection menus.
 *
 * Applies [GenericScaffold] to wrap the screen content.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@Composable
fun ExposedDropdownMenusScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.exposed_dropdown_menus_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.exposed_dropdown_menus_heading),
                modifier = Modifier.testTag(exposedDropdownMenusHeadingTestTag)
            )
            BodyText(textId = R.string.exposed_dropdown_menus_description_1)
            BodyText(textId = R.string.exposed_dropdown_menus_description_2)

            GoodExample1()
            // GoodExample1a() // Read-only code sample from documentation
            GoodExample2()
            ProblematicExample3()
            // ProblematicExample3a() // Editable code sample from documentation

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        ExposedDropdownMenusScreen {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoodExample1() {
    // Good example 1: Non-editable Exposed Dropdown Menu
    GoodExampleHeading(
        text = stringResource(id = R.string.exposed_dropdown_menus_example_1_heading),
        modifier = Modifier.testTag(exposedDropdownMenusExample1HeadingTestTag)
    )
    BodyText(textId = R.string.exposed_dropdown_menus_example_1_description)
    Spacer(modifier = Modifier.height(8.dp))

    val example1Options = listOf(
        stringResource(id = R.string.exposed_dropdown_menus_example_option_not_selected),
        stringResource(id = R.string.exposed_dropdown_menus_example_option_1),
        stringResource(id = R.string.exposed_dropdown_menus_example_option_2),
        stringResource(id = R.string.exposed_dropdown_menus_example_option_3),
        stringResource(id = R.string.exposed_dropdown_menus_example_option_4),
        stringResource(id = R.string.exposed_dropdown_menus_example_option_5),
    )
    // See Key Techniques (and issues) in GenericExposedDropdownMenu.kt.
    var selectedItemText1 by remember { mutableStateOf(example1Options[0]) }
    GenericExposedDropdownMenu(
        value = selectedItemText1,
        setValue = { selectedItemText1 = it },
        options = example1Options,
        modifier = Modifier.testTag(exposedDropdownMenusExample1DropdownMenuBoxTestTag),
        textFieldModifier = Modifier
            .testTag(exposedDropdownMenusExample1DropdownMenuTextFieldTestTag)
            .fillMaxWidth(),
    ) {
        Text(stringResource(id = R.string.exposed_dropdown_menus_example_label))
    }
}

@Preview(showBackground = true)
@Composable
private fun GoodExample1Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample1()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoodExample2() {
    // Good example 2: View non-editable Exposed Dropdown Menu
    GoodExampleHeading(
        text = stringResource(id = R.string.exposed_dropdown_menus_example_2_heading),
        modifier = Modifier.testTag(exposedDropdownMenusExample2HeadingTestTag)
    )
    BodyText(textId = R.string.exposed_dropdown_menus_example_2_description)
    Spacer(modifier = Modifier.height(8.dp))

    val example2Options = listOf(
        stringResource(id = R.string.exposed_dropdown_menus_example_option_1),
        stringResource(id = R.string.exposed_dropdown_menus_example_option_2),
        stringResource(id = R.string.exposed_dropdown_menus_example_option_3),
        stringResource(id = R.string.exposed_dropdown_menus_example_option_4),
        stringResource(id = R.string.exposed_dropdown_menus_example_option_5),
    )
    var selectedItemText2 by remember { mutableStateOf("") }

    // Key technique: Wrap a View-based Exposed Dropdown Menu pattern control ensemble in an
    // AndroidView (or in this case, AndroidViewBinding to use XML layout). See also
    // layout/view_exposed_dropdown_menu.xml.
    AndroidViewBinding(
        factory = ViewExposedDropdownMenuBinding::inflate,
        modifier = Modifier
            .testTag(exposedDropdownMenusExample2AndroidViewTestTag)
            .fillMaxSize()
            .focusable() // allows user to scroll the screen to this control using Tab key
    ) {
        val context = this.root.context
        val autoCompleteAdapter = ArrayAdapter(
            context,
            R.layout.list_item_dropdown,
            example2Options
        )

        // Apply the adapter to the MaterialAutoCompleteTextView in the TextInputLayout
        val autoCompleteTextView =
            (exposedDropdownMenusExample2Layout.editText as? MaterialAutoCompleteTextView)
        autoCompleteTextView?.setAdapter(autoCompleteAdapter)

        // Set an item click listener on the AutoCompleteTextView
        autoCompleteTextView?.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position: Int, _ ->
                selectedItemText2 = example2Options[position]
            }
        // Note: Do not call autoCompleteTextView.setText(selectedValue) as you would with
        // Compose; the MaterialAutoCompleteTextView maintains its own state, and setting it
        // will limit the values displayed in the dropdown menu list.
    }
}

@Preview(showBackground = true)
@Composable
private fun GoodExample2Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample2()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProblematicExample3() {
    // Problematic example 3: Editable Exposed Dropdown Menu
    // Editable Exposed Dropdown Menus have more accessibility problems than the
    // non-editable version. TalkBack and Switch Access are very hard to use and many not
    // seem meaningful in their reading sequence or focus order; however, they are operable.
    ProblematicExampleHeading(
        text = stringResource(id = R.string.exposed_dropdown_menus_example_3_heading),
        modifier = Modifier.testTag(exposedDropdownMenusExample3HeadingTestTag)
    )
    BodyText(textId = R.string.exposed_dropdown_menus_example_3_description)
    Spacer(modifier = Modifier.height(8.dp))
    val example3Options = listOf(
        stringResource(id = R.string.exposed_dropdown_menus_example_option_not_selected),
        stringResource(id = R.string.exposed_dropdown_menus_example_option_1),
        stringResource(id = R.string.exposed_dropdown_menus_example_option_2),
        stringResource(id = R.string.exposed_dropdown_menus_example_option_3),
        stringResource(id = R.string.exposed_dropdown_menus_example_option_4),
        stringResource(id = R.string.exposed_dropdown_menus_example_option_5),
    )
    var selectedItemText3 by remember { mutableStateOf("") }
    GenericExposedDropdownMenu(
        value = selectedItemText3,
        setValue = { selectedItemText3 = it },
        options = example3Options,
        modifier = Modifier
            .testTag(exposedDropdownMenusExample3DropdownMenuBoxTestTag),
        textFieldModifier = Modifier
            .testTag(exposedDropdownMenusExample3DropdownMenuTextFieldTestTag)
            .fillMaxWidth(),
        readOnly = false, // Creates an editable text field
    ) {
        Text(stringResource(id = R.string.exposed_dropdown_menus_example_label))
    }
}

@Preview(showBackground = true)
@Composable
private fun ProblematicExample3Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            ProblematicExample3()
        }
    }
}


/* Test examples from documentation */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoodExample1a() {
    // Good example 1a: Read-only Code Sample from Documentation
    GoodExampleHeading(
        text = "Good example 1a: Read-only Code Sample from Documentation",
    )
    Spacer(modifier = Modifier.height(8.dp))

    val options = listOf("Yes", "No", "Maybe")
    var isExpanded by remember { mutableStateOf(false) }
    var selectedValue by remember { mutableStateOf(options[0]) }
    val focusRequester = remember { FocusRequester() }

    // Key techniques for accessible read-only exposed dropdown menus:
    // 1. Wrap the entire dropdown menu ensemble in an ExposedDropdownMenuBox.
    // 2. Expand the dropdown list when the Enter key is pressed on the TextField.
    // 3. Use Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable) to link the TextField to the
    //    ExposedDropdownMenuBox with correct semantics.
    // 4. Use readOnly = true and onValueChange = {} to prevent editing.
    // 5. Label the TextField.
    // 6. Set trailingIcon to visually indicate the collapsed/expanded state.
    // 7. Wrap the list of menu items in an ExposedDropdownMenu.
    // 8: Close the dropdown list when Esc key is pressed.
    // 9. Hold each menu item in a DropdownMenuItem.
    // 10. Set keyboard focus onto a newly-expanded dropdown menu pop-up.
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
    ) {
        TextField(
            modifier = Modifier
                .onPreviewKeyEvent { keyEvent ->
                    if (keyEvent.nativeKeyEvent.keyCode == KEYCODE_ENTER) {
                        if (keyEvent.nativeKeyEvent.action == ACTION_UP) {
                            isExpanded = true
                        }
                        true
                    } else {
                        false
                    }
                }
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            readOnly = true,
            value = selectedValue,
            onValueChange = {},
            label = {
                Text("Are you sure?")
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .focusRequester(focusRequester)
                .onPreviewKeyEvent { keyEvent ->
                    if (keyEvent.nativeKeyEvent.keyCode == KEYCODE_ESCAPE) {
                        if (keyEvent.nativeKeyEvent.action == ACTION_UP) {
                            isExpanded = false
                        }
                        true
                    } else {
                        false
                    }
                }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedValue = option
                        isExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
    LaunchedEffect(isExpanded) {
        if (isExpanded) {
            delay(500)
            focusRequester.requestFocus()
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
private fun GoodExample1aPreview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample1a()
        }
    }
}

// Test example from documentation: Editable Exposed Dropdown Menu Pattern
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProblematicExample3a() {
    // Problematic example 3a: Editable Code Sample from Documentation
    ProblematicExampleHeading(
        text = "Problematic example 3a: Editable Code Sample from Documentation",
    )
    Spacer(modifier = Modifier.height(8.dp))

    val options = listOf(
        "1 Main Street",
        "2 Main Street",
        "3 Main Street",
        "11 Main Street",
        "12 Main Street",
        "13 Main Street",
        "21 Main Street",
        "22 Main Street",
        "23 Main Street",
        "31 Main Street",
        "32 Main Street",
        "33 Main Street",
        "41 Main Street",
        "42 Main Street",
        "43 Main Street",
        "10 Elm Street",
        "11 Elm Street",
        "12 Elm Street",
    )
    var isExpanded by remember { mutableStateOf(false) }
    var currentValue by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    // Key techniques for accessible editable exposed dropdown menus:
    // 1. Wrap the entire dropdown menu ensemble in an ExposedDropdownMenuBox.
    // 2. Expand the dropdown list when the Enter key is pressed on the TextField. Use the Modifier.
    //    nextOnTabAndHandleEnter extension function to avoid the editable TextField keyboard trap.
    // 3. Use Modifier.menuAnchor(MenuAnchorType.PrimaryEditable) to link the TextField to the
    //    ExposedDropdownMenuBox with correct semantics.
    // 4. Use readOnly = false and onValueChange = { selectedValue = it } to allow editing.
    // 5. Label the TextField.
    // 6. Set trailingIcon to visually indicate the collapsed/expanded state. Make the trailing icon
    //    a secondary anchor.
    // 7. Force a non-default KeyboardOption.imeAction and a KeyboardAction callback onto this
    //    TextField to expand the dropdown menu on Enter.
    // 8. Filter the list items based on the current TextField value.
    // 9. Wrap the list of menu items in an ExposedDropdownMenu.
    // 10: Close the dropdown list when Esc key is pressed.
    // 11. Hold each menu item in a DropdownMenuItem.
    // 12. Set keyboard focus onto a newly-expanded dropdown menu pop-up.
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
    ) {
        TextField(
            modifier = Modifier
                .nextOnTabAndHandleEnter {
                    isExpanded = true
                }
                .menuAnchor(MenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
            readOnly = false,
            value = currentValue,
            onValueChange = { newValue ->
                currentValue = newValue
            },
            label = {
                Text("Street Address")
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isExpanded,
                    modifier = Modifier.menuAnchor(MenuAnchorType.SecondaryEditable)
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions {
                isExpanded = true
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        val filteredOptions = options.filter { it.contains(currentValue, ignoreCase = true) }
        if (filteredOptions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onPreviewKeyEvent { keyEvent ->
                        if (keyEvent.nativeKeyEvent.keyCode == KEYCODE_ESCAPE) {
                            if (keyEvent.nativeKeyEvent.action == ACTION_UP) {
                                isExpanded = false
                            }
                            true
                        } else {
                            false
                        }
                    }
            ) {
                filteredOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            currentValue = option
                            isExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
            LaunchedEffect(isExpanded) {
                if (isExpanded) {
                    delay(500)
                    focusRequester.requestFocus()
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
private fun ProblematicExample3aPreview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            ProblematicExample3a()
        }
    }
}
