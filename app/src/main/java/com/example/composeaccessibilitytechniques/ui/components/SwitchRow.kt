package com.example.composeaccessibilitytechniques.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeaccessibilitytechniques.RadioGroupComponent
import com.example.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

/**
 * SwitchRow properly labels a Switch composable and hoists the selected state handling to its caller.
 *
 * SwitchRow applies the following key techniques:
 * 1. The Row() applied Modifier.toggleable() with role=Role.Switch and performs click handling at
 *    the Row level.
 * 2. Applying click handling at this level automatically applies semantics(mergeDescentants = true)
 *    which programmatically associates the Text() label with the Switch().
 * 3. The Switch() composable itself has a null onClick handler; clicks are handled at the
 *    Row()-level only. (This prevents the Switch() from becoming a separate focusable element
 *    within the Row(), leading to a poor audio user experience.)
 * 4. Given that the Switch() is no longer clickable, appropriate padding is applied manually to
 *    replace the automatic padding that Compose adds to clickable elements.
 */
@Composable
fun SwitchRow(
    text: String,
    checked: Boolean,
    toggleHandler: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .toggleable(
                value = checked,
                role = Role.Switch,
                onValueChange = toggleHandler
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, modifier = Modifier.padding(end = 8.dp))
        Switch(
            checked = checked,
            onCheckedChange = null,
            modifier = Modifier.defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SwitchRowPreview() {
    ComposeAccessibilityTechniquesTheme(dynamicColor = false) {
        val switchState = remember { mutableStateOf(false) }
        SwitchRow(
            text = "Test Switch",
            checked = switchState.value,
            toggleHandler = { newState ->
                switchState.value = newState
            }
        )
    }
}