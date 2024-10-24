# Exposed Dropdown Selection Menus
Exposed dropdown selection menus allow selecting a single value from a list of values. The Material Design [Exposed Dropdown Menu pattern](https://m2.material.io/components/menus#exposed-dropdown-menu) works well with assistive technologies -- creating dropdown menu controls that announce their name, role, and value in accordance with the WCAG [Success Criterion 4.1.2 Name, Role, Value](https://www.w3.org/TR/WCAG22/#name-role-value) and correctly express their relationships according to WCAG [Success Criterion 1.3.1 Info and Relationships](https://www.w3.org/TR/WCAG22/#info-and-relationships). However, as of Compose BOM 2024.09.03, the Compose version of this pattern is still not keyboard operable, as required by WCAG [Success Criterion 2.1.1 Keyboard](https://www.w3.org/TR/WCAG22/#keyboard). 

Custom approaches are likely to be less accessible; make sure any dropdown selection menu is operable by all assistive technologies, including the keyboard, and announces the role "Drop down list."

## The Compose Exposed Dropdown Menu pattern

For examples of how to implement the Exposed Dropdown menu pattern using the Material Design components ExposedDropdownMenuBox, TextField, DropdownMenu, and DropdownMenuItem, see:

* [Material Design 2 ExposedDropdownMenuBox](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#ExposedDropdownMenuBox(kotlin.Boolean,kotlin.Function1,androidx.compose.ui.Modifier,kotlin.Function1)). 

* [Material Design 3 ExposedDropdownMenuBox](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#ExposedDropdownMenuBox(kotlin.Boolean,kotlin.Function1,androidx.compose.ui.Modifier,kotlin.Function1)).

Notes:

* Material 3 requires applying `Modifier.menuAnchor(...)` to the `TextField` (and sometimes its `trailingIcon`) in order to link the `TextView` with the menu with the proper semantics.
* For non-editable Compose dropdown menus, set `readOnly = true` and `onValueChange = {}` on the `TextField` to prevent it from accepting user keyboard text (only data from the dropdown selection list is allowed). Also, apply `Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)`.
* Editable Compose dropdown menus pose challenges for screen reader users and are a keyboard trap, so they should be avoided in Compose if at all possible.

For example:

```kotlin
val options = listOf("Yes", "No", "Maybe")
var isExpanded by remember { mutableStateOf(false) }
var selectedValue by remember { mutableStateOf(options[0]) }

// Key techniques for non-editable exposed dropdown menus:
// 1. Wrap the entire dropdown menu ensemble in an ExposedDropdownMenuBox.
// 2. Use Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable) to link the TextField to the 
//    ExposedDropdownMenuBox with correct semantics.
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

## Wrapping the View Exposed Dropdown Menu pattern for Compose

One accessible approach to exposed dropdown menus is to fall back on using View components, wrapped in an `AndroidView`. 

For example, given a file named `layout/view_dropdown_menu.xml` with the following View Exposed Dropdown Menu pattern controls:

```xml
<com.google.android.material.textfield.TextInputLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/wrapped_dropdown_menu"
    style="@style/Widget.MaterialComponents.TextInputLayout.FilledBox.ExposedDropdownMenu"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="Are you sure?"
    android:textColorHint="#616161">

    <com.google.android.material.textfield.MaterialAutoCompleteTextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:inputType="none"
        tools:ignore="Deprecated,LabelFor" />
</com.google.android.material.textfield.TextInputLayout>
```

The following code will display the View Exposed Dropdown Menu pattern in a Compose screen:

```kotlin
val options = listOf("Yes", "No", "Maybe")
var selectedValue by remember { mutableStateOf(options[0]) }

AndroidViewBinding(
    factory = ViewDropdownMenuBinding::inflate,
    modifier = Modifier
        .fillMaxSize()
        .focusable() // allows user to scroll the screen to this control using Tab key
) {
    val context = this.root.context
    val autoCompleteAdapter = ArrayAdapter(
        context,
        R.layout.list_item_dropdown,
        options
    )
    
    // Apply the adapter to the MaterialAutoCompleteTextView in the TextInputLayout
    val autoCompleteTextView = (wrappedDropdownMenu.editText as? MaterialAutoCompleteTextView)
    autoCompleteTextView?.setAdapter(autoCompleteAdapter)
    
    // Set an item click listener on the AutoCompleteTextView
    autoCompleteTextView?.onItemClickListener =
        AdapterView.OnItemClickListener { _, _, position: Int, _ ->
            selectedValue = options[position]
        }
    // Note: Do not call autoCompleteTextView.setText(selectedValue) as you would with Compose; the 
    // MaterialAutoCompleteTextView maintains its own state, and setting its text will limit the 
    // values displayed in the dropdown menu list.
}
```

(Note: The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.)

----

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