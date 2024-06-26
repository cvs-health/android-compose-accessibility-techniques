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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Switch
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
 * Displays a properly labeled [Switch] composable and hoists the selected state handling to its
 * caller.
 *
 * SwitchRow applies the following key techniques:
 * 1. The [Row] applied Modifier.toggleable() with role=Role.Switch and performs click handling at
 * the [Row] level.
 * 2. Applying click handling at this level automatically applies semantics(mergeDescentants = true)
 * which programmatically associates the [Text] label with the [Switch].
 * 3. The [Switch] composable itself has a null onClick handler; clicks are handled at the
 * [Row]-level only. (This prevents the [Switch] from becoming a separate focusable element
 * within the [Row], leading to a poor audio user experience.)
 * 4. Given that the [Switch] is no longer clickable, appropriate padding is applied manually
 * with Modifier.minimumInteractiveComponentSize() to replace the automatic padding that Compose
 * adds to clickable elements.
 *
 * @param text label string for the [Switch]
 * @param checked the current [Switch] state
 * @param toggleHandler callback that sets the current [Switch] state
 * @param modifier optional [Modifier] for the layout [Row]
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
            .visibleFocusBorder()
            .toggleable(
                value = checked,
                role = Role.Switch,
                onValueChange = toggleHandler
            )
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, modifier = Modifier.padding(end = 8.dp))
        Switch(
            checked = checked,
            onCheckedChange = null,
            modifier = Modifier.minimumInteractiveComponentSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SwitchRowPreview() {
    ComposeAccessibilityTechniquesTheme {
        val (switchValue, setSwitchValue) = remember { mutableStateOf(false) }
        SwitchRow(
            text = "Test Switch",
            checked = switchValue,
            toggleHandler = setSwitchValue
        )
    }
}