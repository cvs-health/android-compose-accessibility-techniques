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
import androidx.compose.material3.Checkbox
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
 * Properly labels a [Checkbox] composable and hoists the selected state handling to its caller.
 *
 * CheckboxRow applies the following key techniques:
 * 1. The [Row] applied Modifier.toggleable() with role=Role.Checkbox and performs click handling at
 * the [Row] level.
 * 2. Applying click handling at this level automatically applies semantics(mergeDescendants = true)
 * which programmatically associates the [Text] label with the [Checkbox].
 * 3. The [Checkbox] composable itself has a null onClick handler; clicks are handled at the
 * [Row]-level only. (This prevents the [Checkbox] from becoming a separate focusable element
 * within the [Row], leading to a poor audio user experience.)
 * 4. Given that the [Checkbox] is no longer clickable, appropriate padding is applied manually
 * with .minimumInteractiveComponentSize() to replace the automatic padding that Compose adds to
 * clickable elements.
 *
 * @param text label for the Checkbox,
 * @param checked current state of the Checkbox,
 * @param toggleHandler toggles the current state of the Checkbox,
 * @param modifier settings for the wrapping Row
 */
@Composable
fun CheckboxRow(
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
                role = Role.Checkbox,
                onValueChange = toggleHandler
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            modifier = Modifier.minimumInteractiveComponentSize()
        )
        Text(text = text, modifier = Modifier.padding(end = 12.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckboxRowPreview() {
    ComposeAccessibilityTechniquesTheme {
        val (checkboxValue, setCheckboxValue) = remember { mutableStateOf(false) }
        CheckboxRow(
            text = "Test Checkbox",
            checked = checkboxValue,
            toggleHandler = setCheckboxValue
        )
    }
}