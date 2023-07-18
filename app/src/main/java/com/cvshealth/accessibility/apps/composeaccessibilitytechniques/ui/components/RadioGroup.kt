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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

/**
 * RadioGroup presents a list of String labels are radio button options and hoists the selected
 * option state handling to its caller.
 *
 * RadioGroup applies the following key techniques:
 * 1. The Column with the list of radio buttons applies Modifier.selectableGroup(), which adds
 *    single-selection list semantics. (It could be argued whether the group label Text() should be
 *    inside or outside the selectableGroup(). Each way has advantages and disadvantages at present.)
 * 2. Each radio button Row() applied Modifier.selectable() with role=Role.RadioButton and performs
 *    click handling at the Row level.
 * 3. Each RadioButton() has a null onClick handler; clicks are handled at the Row()-level only.
 * 4. Given that the RadioButton is no longer clickable, appropriate padding is applied manually
 *    with .minimumInteractiveComponentSize() to replace the automatic padding that Compose adds to
 *    clickable elements.
 */
@Composable
fun RadioGroup(
    groupLabel: String,
    itemLabels: List<String>,
    current: Int,
    selectHandler: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(groupLabel)
        Column(modifier = Modifier.selectableGroup()) {
            itemLabels.forEachIndexed { index: Int, label: String ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (current == index),
                        role = Role.RadioButton,
                        onClick = { selectHandler(index) }
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (current == index),
                        onClick = null,
                        modifier = Modifier.minimumInteractiveComponentSize()
                    )
                    Text(text = label, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RadioGroupPreview() {
    val options = listOf( "Banana", "Grape", "Orange")
    val selectedOption = remember { mutableStateOf(0) }
    ComposeAccessibilityTechniquesTheme() {
        Column() {
            RadioGroup(
                groupLabel = "Pick a fruit:",
                itemLabels = options,
                current = selectedOption.value,
                selectHandler = { selectedOption.value = it }
            )
        }
    }
}
