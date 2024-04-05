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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.databinding.ViewExposedDropdownMenuBinding
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericExposedDropdownMenu
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.ProblematicExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import com.google.android.material.textfield.MaterialAutoCompleteTextView

const val exposedDropdownMenusHeadingTestTag = "exposedDropdownMenusHeading"
const val exposedDropdownMenusExample1HeadingTestTag = "exposedDropdownMenusExample1Heading"
const val exposedDropdownMenusExample1DropdownMenuBoxTestTag =
    "exposedDropdownMenusExample1DropdownMenuBox"
const val exposedDropdownMenusExample1DropdownMenuTextFieldTestTag =
    "exposedDropdownMenusExample1DropdownMenuTextField"
const val exposedDropdownMenusExample2HeadingTestTag = "exposedDropdownMenusExample2Heading"
const val exposedDropdownMenusExample2AndroidViewTestTag = "exposedDropdownMenusExample2AndroidView"

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

            // Problematic example 1: Non-editable Exposed Dropdown Menu
            ProblematicExampleHeading(
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

            // Problematic example 2: View non-editable Exposed Dropdown Menu
            ProblematicExampleHeading(
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
            //
            // Note: This approach fails to be keyboard accessible, because of a known Compose-View
            // interop issue: see https://issuetracker.google.com/issues/255628260 for details.
            AndroidViewBinding(
                factory = ViewExposedDropdownMenuBinding::inflate,
                modifier = Modifier
                    .testTag(exposedDropdownMenusExample2AndroidViewTestTag)
                    .fillMaxSize()
                    .focusable()
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

            // Note: Editable Exposed Dropdown Menus are not demonstrated, because they have
            // even more accessibility problems than the non-editable version.

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