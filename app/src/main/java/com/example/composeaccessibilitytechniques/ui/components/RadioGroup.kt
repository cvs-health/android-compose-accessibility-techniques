package com.example.composeaccessibilitytechniques.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

@Composable
fun RadioGroup(
    groupLabel: String,
    itemLabels: List<String>,
    current: Int,
    selectHandler: (Int) -> Unit
) {
    Column(modifier = Modifier.selectableGroup()) {
        Text(groupLabel)
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
                    modifier = Modifier.defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                )
                Text(text = label, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RadioGroupPreview() {
    val options = listOf( "Banana", "Grape", "Orange")
    val selectedOption = remember { mutableStateOf(0) }
    ComposeAccessibilityTechniquesTheme(dynamicColor = false) {
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

//@Preview(showBackground = true)
//@Composable
//fun XXXPreview() {
//
//    ComposeAccessibilityTechniquesTheme(dynamicColor = false) {
//
//    }
//}
