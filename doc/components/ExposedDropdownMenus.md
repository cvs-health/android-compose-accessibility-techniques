# Exposed Dropdown Selection Menus
Exposed dropdown selection menus allow entering a single value, generally by selecting from a list of values. The Material Design [Exposed Dropdown Menu pattern](https://m2.material.io/components/menus#exposed-dropdown-menu) works well with assistive technologies -- creating dropdown menu controls that announce their name, role, and value in accordance with the WCAG [Success Criterion 4.1.2 Name, Role, Value](https://www.w3.org/TR/WCAG22/#name-role-value), correctly express their relationships according to WCAG [Success Criterion 1.3.1 Info and Relationships](https://www.w3.org/TR/WCAG22/#info-and-relationships), and can be made keyboard operable in accordance with WCAG [Success Criterion 2.1.1 Keyboard](https://www.w3.org/TR/WCAG22/#keyboard). This pattern can be made very accessible in its read-only form, but it is less accessible when used to suggest values for an editable text entry field (i.e., an "auto-complete" control); such controls are harder to use with assistive technologies and may not fully conform to WCAG [Success Criterion 1.3.2 Meaningful Sequence](https://www.w3.org/TR/WCAG22/#meaningful-sequence) or [Success Criterion 2.4.3 Focus Order](https://www.w3.org/TR/WCAG22/#focus-order). 

For examples of how to implement the Exposed Dropdown menu pattern using the Material Design components ExposedDropdownMenuBox, TextField, DropdownMenu, and DropdownMenuItem, see the following links; although they do not include the necessary accessibility remediations:

* [Material Design 2 ExposedDropdownMenuBox](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#ExposedDropdownMenuBox(kotlin.Boolean,kotlin.Function1,androidx.compose.ui.Modifier,kotlin.Function1)).

* [Material Design 3 ExposedDropdownMenuBox](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#ExposedDropdownMenuBox(kotlin.Boolean,kotlin.Function1,androidx.compose.ui.Modifier,kotlin.Function1)).

Custom approaches to dropdown selection menus are likely to be less accessible; make sure any dropdown selection menu is operable by all assistive technologies, including the keyboard, and announces the role "Drop down list."

## The read-only Compose Exposed Dropdown Menu pattern

The read-only Exposed Dropdown Menu pattern can be made fully accessibility.

Notes:

* Material 3 requires applying `Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)` to the `TextField` in order to link the `TextView` with the menu with the proper semantics.
* Set `readOnly = true` and `onValueChange = {}` on the `TextField` to prevent it from accepting user keyboard text (only data from the dropdown selection list is allowed).
* Keyboard handling and focus management menu be applied at several points.

For example:

```kotlin
val options = listOf("Yes", "No", "Maybe")
var isExpanded by remember { mutableStateOf(false) }
var selectedValue by remember { mutableStateOf(options[0]) }
val focusRequester = remember { FocusRequester() }

// Key techniques for accessible read-only exposed dropdown menus:
// 1. Wrap the entire dropdown menu ensemble in an ExposedDropdownMenuBox.
// 2. Expand the dropdown list when the Enter key is pressed on the TextField.
// 3. Use Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable) to link the 
//    TextField to the ExposedDropdownMenuBox with correct semantics.
// 4. Use readOnly = true and onValueChange = {} to prevent editing.
// 5. Label the TextField.
// 6. Set trailingIcon to visually indicate the collapsed/expanded state.
// 7. Wrap the list of menu items in an ExposedDropdownMenu.
// 8: Close the dropdown list when Esc key is pressed. 
// 9. Hold each menu item in a DropdownMenuItem.
// 10. Set keyboard focus onto a newly-expanded dropdown menu pop-up.
ExposedDropdownMenuBox(
    expanded = isExpanded,
    onExpandedChange = { isExpanded = !isExpanded },
) {
    TextField(
        modifier = Modifier
            .onPreviewKeyEvent { keyEvent ->
                if (keyEvent.nativeKeyEvent.keyCode == KEYCODE_ENTER) {
                    if (keyEvent.nativeKeyEvent.action == ACTION_UP) {
                        isExpanded = true
                    }
                    true
                } else {
                    false
                }
            }
            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
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
        modifier = Modifier
            .focusRequester(focusRequester)
            .onPreviewKeyEvent { keyEvent ->
                if (keyEvent.nativeKeyEvent.keyCode == KEYCODE_ESCAPE) {
                    if (keyEvent.nativeKeyEvent.action == ACTION_UP) {
                        isExpanded = false
                    }
                    true
                } else {
                    false
                }
            }
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
LaunchedEffect(isExpanded) {
    if (isExpanded) {
        delay(500)
        focusRequester.requestFocus()
    }
}
```

## Wrapping the View Exposed Dropdown Menu pattern for Compose

Another accessible approach to exposed dropdown menus is to fall back on using View components, wrapped in an `AndroidView`. 

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

## The editable Compose Exposed Dropdown Menu pattern

The editable Exposed Dropdown Menu pattern can be made largely accessibility; although it may pose challenges for assistive technology users.

Notes:

* Material 3 requires applying `Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)` to the `TextField` and `Modifier.menuAnchor(ExposedDropdownMenuAnchorType.SecondaryEditable)` to its `trailingIcon` in order to link the `TextView` with the menu with the proper semantics.
* Set `readOnly = false` and supply an `onValueChange` that sets the appropriate value state on the `TextField` to allow it to accept user keyboard input.
* Keyboard handling and focus management must be applied even more extensively than for the read-only case. See [TextField Controls](TextFieldControls.md) for details of the `Modifier.nextOnTabAndEnterHandler()` extension function used in the example below.

For example:

```kotlin
    val options = listOf(
    "1 Main Street",
    "2 Main Street",
    "3 Main Street",
    "11 Main Street",
    "12 Main Street",
    "13 Main Street",
    "21 Main Street",
    "22 Main Street",
    "23 Main Street",
    "31 Main Street",
    "32 Main Street",
    "33 Main Street",
    "41 Main Street",
    "42 Main Street",
    "43 Main Street",
    "10 Elm Street",
    "11 Elm Street",
    "12 Elm Street",
)
var isExpanded by remember { mutableStateOf(false) }
var currentValue by remember { mutableStateOf("") }
val focusRequester = remember { FocusRequester() }

// Key techniques for accessible editable exposed dropdown menus:
// 1. Wrap the entire dropdown menu ensemble in an ExposedDropdownMenuBox.
// 2. Expand the dropdown list when the Enter key is pressed on the TextField. Use the Modifier.
//    nextOnTabAndHandleEnter extension function to avoid the editable TextField keyboard trap.
// 3. Use Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable) to link the TextField 
//    to the ExposedDropdownMenuBox with correct semantics.
// 4. Use readOnly = false and onValueChange = { selectedValue = it } to allow editing.
// 5. Label the TextField.
// 6. Set trailingIcon to visually indicate the collapsed/expanded state. Make the trailing icon
//    a secondary anchor.
// 7. Force a non-default KeyboardOption.imeAction and a KeyboardAction callback onto this
//    TextField to expand the dropdown menu on Enter.
// 8. Filter the list items based on the current TextField value.
// 9. Wrap the list of menu items in an ExposedDropdownMenu.
// 10: Close the dropdown list when Esc key is pressed.
// 11. Hold each menu item in a DropdownMenuItem.
// 12. Set keyboard focus onto a newly-expanded dropdown menu pop-up.
ExposedDropdownMenuBox(
    expanded = isExpanded,
    onExpandedChange = { isExpanded = !isExpanded },
) {
    TextField(
        modifier = Modifier
            .nextOnTabAndHandleEnter {
                isExpanded = true
            }
            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
            .fillMaxWidth(),
        readOnly = false,
        value = currentValue,
        onValueChange = { newValue ->
            currentValue = newValue
        },
        label = {
            Text("Street Address")
        },
        trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(
                expanded = isExpanded,
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.SecondaryEditable)
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions {
            isExpanded = true
        },
        colors = ExposedDropdownMenuDefaults.textFieldColors(),
    )
    val filteredOptions = options.filter { it.contains(currentValue, ignoreCase = true) }
    if (filteredOptions.isNotEmpty()) {
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .focusRequester(focusRequester)
                .onPreviewKeyEvent { keyEvent ->
                    if (keyEvent.nativeKeyEvent.keyCode == KEYCODE_ESCAPE) {
                        if (keyEvent.nativeKeyEvent.action == ACTION_UP) {
                            isExpanded = false
                        }
                        true
                    } else {
                        false
                    }
                }
        ) {
            filteredOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        currentValue = option
                        isExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
        LaunchedEffect(isExpanded) {
            if (isExpanded) {
                delay(500)
                focusRequester.requestFocus()
            }
        }
    }
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