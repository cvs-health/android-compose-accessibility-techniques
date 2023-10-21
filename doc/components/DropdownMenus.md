# Dropdown Selection Menus
Use the Material Design [Exposed Dropdown Menu pattern](https://m2.material.io/components/menus#exposed-dropdown-menu) to create dropdown menu controls that announce their name, role, and value in accordance with the WCAG 2 [Success Criterion 4.1.2 Name, Role, Value](https://www.w3.org/TR/WCAG21/#name-role-value) and correctly express their relationships according to WCAG 2 [Success Criterion 1.3.1 Info and Relationships](https://www.w3.org/TR/WCAG21/#info-and-relationships). Custom approaches are likely to be less accessible; make sure any dropdown selection menu announces the role "Dropdown menu".

## The Exposed Dropdown Menu pattern

For examples of how to implement the Exposed Dropdown menu pattern using the Material Design components ExposedDropdownMenuBox, TextField, DropdownMenu, and DropdownMenuItem, see:

* [Material Design 2 ExposedDropdownMenuBox](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#ExposedDropdownMenuBox(kotlin.Boolean,kotlin.Function1,androidx.compose.ui.Modifier,kotlin.Function1)). 

* [Material Design 3 ExposedDropdownMenuBox](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#ExposedDropdownMenuBox(kotlin.Boolean,kotlin.Function1,androidx.compose.ui.Modifier,kotlin.Function1)).

Notes:

* Material 3 requires applying `Modifier.menuAnchor()` to the `TextField` in order to link it with the menu.

* For non-editable dropdown menus, set `readOnly = true` and `onValueChange = {}` on the `TextField` to prevent it from accepting user keyboard text (only data from the dropdown selection list is allowed).

* Editable dropdown menus pose challenges for screen reader users, so they should be used with caution.

For example:

```kotlin
val options = listOf("Yes", "No", "Maybe")
var isExpanded by remember { mutableStateOf(false) }
var selectedValue by remember { mutableStateOf(options[0]) }

// Key techniques for non-editable exposed dropdown menus:
// 1. Wrap the entire dropdown menu ensemble in an ExposedDropdownMenuBox.
// 2. Use Modifier.menuAnchor() to link the TextField to the ExposedDropdownMenuBox.
// 3. Use readOnly = true and onValueChange = {} to prevent editing.
// 4. Label the TextField.
// 5. Set trailingIcon to visually indicate the collapsed/expanded state.
// 7. Wrap the list of menu items in an ExposedDropdownMenu.
// 8. Hold each menu item in a DropdownMenuItem.
ExposedDropdownMenuBox(
    expanded = isExpanded,
    onExpandedChange = { isExpanded = !isExpanded },
) {
    TextField(
        modifier = Modifier
            .menuAnchor()
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
```

(Note: The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.)

----

Copyright 2023 CVS Health and/or one of its affiliates

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
[http://www.apache.org/licenses/LICENSE-2.0]()

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.