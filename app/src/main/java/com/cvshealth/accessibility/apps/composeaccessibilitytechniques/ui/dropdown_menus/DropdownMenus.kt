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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dropdown_menus

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericExposedDropdownMenu
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.OkExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val dropdownMenusHeadingTestTag = "dropdownMenusHeading"
const val dropdownMenusExample1HeadingTestTag = "dropdownMenusExample1Heading"
const val dropdownMenusExample1DropdownMenuBoxTestTag = "dropdownMenusExample1DropdownMenuBox"
const val dropdownMenusExample1DropdownMenuTextFieldTestTag = "dropdownMenusExample1DropdownMenuTextField"
const val dropdownMenusExample2HeadingTestTag = "dropdownMenusExample2Heading"
const val dropdownMenusExample2DropdownMenuBoxTestTag = "dropdownMenusExample2DropdownMenuBox"
const val dropdownMenusExample2DropdownMenuTextFieldTestTag = "dropdownMenusExample2DropdownMenuTextField"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenusScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.dropdown_menus_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.dropdown_menus_heading),
                modifier = Modifier.testTag(dropdownMenusHeadingTestTag)
            )
            BodyText(textId = R.string.dropdown_menus_description_1)
            BodyText(textId = R.string.dropdown_menus_description_2)

            // Good example 1: Non-editable Exposed Dropdown Menu
            GoodExampleHeading(
                text = stringResource(id = R.string.dropdown_menus_example_1_heading),
                modifier = Modifier.testTag(dropdownMenusExample1HeadingTestTag)
            )
            BodyText(textId = R.string.dropdown_menus_example_1_description)
            Spacer(modifier = Modifier.height(8.dp))
            
            val example1Options = listOf(
                stringResource(id = R.string.dropdown_menus_example_option_not_selected),
                stringResource(id = R.string.dropdown_menus_example_option_1),
                stringResource(id = R.string.dropdown_menus_example_option_2),
                stringResource(id = R.string.dropdown_menus_example_option_3),
                stringResource(id = R.string.dropdown_menus_example_option_4),
                stringResource(id = R.string.dropdown_menus_example_option_5),
            )
            var selectedItemText1 by remember { mutableStateOf(example1Options[0]) }
            GenericExposedDropdownMenu(
                value = selectedItemText1,
                setValue = { selectedItemText1 = it },
                options = example1Options,
                modifier = Modifier.testTag(dropdownMenusExample1DropdownMenuBoxTestTag),
                textFieldModifier = Modifier
                    .testTag(dropdownMenusExample1DropdownMenuTextFieldTestTag)
                    .fillMaxWidth()
            ) {
                Text(stringResource(id = R.string.dropdown_menus_example_label))
            }

            // OK example 2: Editable Exposed Dropdown Menu
            // Note: Any text can be entered in the input field; its value is not limited to the
            // contents of the dropdown list. Use in TalkBack is challenging and require use of
            // TalkBack's Window Reading Mode to switch between the keyboard and app UIs.
            OkExampleHeading(
                text = stringResource(id = R.string.dropdown_menus_example_2_heading),
                modifier = Modifier.testTag(dropdownMenusExample2HeadingTestTag)
            )
            BodyText(textId = R.string.dropdown_menus_example_2_description)
            Spacer(modifier = Modifier.height(8.dp))

            val example2Options = listOf(
                stringResource(id = R.string.dropdown_menus_example_option_1),
                stringResource(id = R.string.dropdown_menus_example_option_2),
                stringResource(id = R.string.dropdown_menus_example_option_3),
                stringResource(id = R.string.dropdown_menus_example_option_4),
                stringResource(id = R.string.dropdown_menus_example_option_5),
            )
            var selectedItemText2 by remember { mutableStateOf("") }
            GenericExposedDropdownMenu(
                value = selectedItemText2,
                setValue = { selectedItemText2 = it },
                options = example2Options,
                modifier = Modifier.testTag(dropdownMenusExample2DropdownMenuBoxTestTag),
                textFieldModifier = Modifier
                    .testTag(dropdownMenusExample2DropdownMenuTextFieldTestTag)
                    .fillMaxWidth(),
                readOnly = false,
            ) {
                Text(stringResource(id = R.string.dropdown_menus_example_label))
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        DropdownMenusScreen {}
    }
}