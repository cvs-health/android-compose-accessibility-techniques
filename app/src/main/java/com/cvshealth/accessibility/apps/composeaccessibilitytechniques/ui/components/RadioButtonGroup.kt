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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

/**
 * Presents a list of [String] labels as [RadioButton] options and hoists the selected option state
 * handling to its caller.
 *
 * RadioButtonGroup applies the following key techniques:
 * 1. The [Column] with the list of radio buttons applies Modifier.selectableGroup(), which adds
 *    single-selection list semantics. (It could be argued whether the group label Text() should be
 *    inside or outside the selectableGroup(). Each way has advantages and disadvantages at present.)
 * 2. Each radio button [Row] applied Modifier.selectable() with role=Role.RadioButton and performs
 *    click handling at the Row level.
 * 3. Each [RadioButton] has a null onClick handler; clicks are handled at the [Row]-level only.
 * 4. Given that the [RadioButton] is no longer clickable, appropriate padding is applied manually
 *    with Modifier.minimumInteractiveComponentSize() to replace the automatic padding that Compose
 *    adds to clickable elements.
 *
 * @param groupLabel label string for the radio button group
 * @param itemLabels list of radio button label strings
 * @param selectedIndex the currently selected radio button's 0-based index in the [itemLabels] list
 * @param selectHandler: callback to set the currently selected radio button, given its index value
 * @param modifier optional [Modifier] for the RadioButtonGroup layout [Column]
 */
@Composable
fun RadioButtonGroup(
    groupLabel: String,
    itemLabels: List<String>,
    selectedIndex: Int,
    selectHandler: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(groupLabel)
        // Key technique 1: Apply Modifier.selectableGroup() to apply single-selection list
        // semantics at the RadioButton group level.
        Column(modifier = Modifier.selectableGroup()) {
            itemLabels.forEachIndexed { index: Int, label: String ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .visibleFocusBorder()
                    // Key technique 2: Apply Modifier.selectable() to handle all RadioButton role
                    // semantics and click handling at the Row level. This also merges all child
                    // composable semantics, so the RadioButton and its label are programmatically
                    // associated.
                    .selectable(
                        selected = (selectedIndex == index),
                        role = Role.RadioButton,
                        onClick = { selectHandler(index) }
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (selectedIndex == index),
                        // Key technique 3: Only perform onClick handling at the Row level.
                        onClick = null,
                        // Key technique 4: Without an onClick handler, Compose will no longer
                        // automatically size the RadioButton to the 48dp x 48dp minimum touch
                        // target size. Do so manually here, unless there is a design reason for it
                        // to be smaller.
                        modifier = Modifier.minimumInteractiveComponentSize()
                    )
                    Text(text = label, modifier = Modifier.padding(start = 4.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RadioGroupPreview() {
    val options = listOf("Banana", "Grape", "Orange")
    val (selectedOption, setSelectedOption) = remember { mutableIntStateOf(0) }
    ComposeAccessibilityTechniquesTheme {
        Column {
            RadioButtonGroup(
                groupLabel = "Pick a fruit:",
                itemLabels = options,
                selectedIndex = selectedOption,
                selectHandler = setSelectedOption
            )
        }
    }
}
