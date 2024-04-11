# Interactive Control Labels
All interactive controls must have associated labels, as required by WCAG [Success Criterion 1.3.1 Info and Relationships](https://www.w3.org/TR/WCAG22/#info-and-relationships). Android Jetpack Compose uses several techniques to associate controls and labels, depending on the control type.

## Use the `label` parameter to associate a label with a `TextField`

`TextField` and `OutlinedTextField` composables are labeled by their `label` parameter.

For example:

```kotlin
val (text, setText) = remember { mutableStateOf("") }
OutlinedTextField(
    value = text,
    onValueChange = setText,
    modifier = Modifier.fillMaxWidth(),
    label = {
        Text(text = "TextField label")
    }
)
```

## Use the `content` parameter to associate a label with a `Button` or `IconButton`

`Button` composables are labeled by their `@Composable` `content` parameter (which is often coded as a trailing lambda). 

For example:

```kotlin
Button(
    onClick = { /* TODO */ }
) {
    Text("Button label")
}
```

In the case of the `IconButton`, the label is sometimes only an icon, in which case, it must also have a non-null (and non-empty) `contentDescription`.

```kotlin
IconButton(onClick = { onCloseIconClick() }) {
    Icon(
        painter = painterResource(id = R.drawable.ic_close_outline),
        contentDescription = "Close"
    )
}
```

## Use wrapping layouts to label the `Checkbox`, `Switch`, and `RadioButton` composables

The `Checkbox`, `Switch`, and `RadioButton` composables all require a wrapping layout with special coding to associate a label with the control.

For example, a `Checkbox` can be labeled as follows.

```kotlin
val (checkboxValue, setCheckboxValue) = remember { mutablestateOf(false) }
Row(
    modifier = modifier
        .toggleable(
            value = checkboxValue,
            role = Role.Checkbox,
            onValueChange = setCheckboxValue
        ),
    verticalAlignment = Alignment.CenterVertically
) {
    Checkbox(
        checked = checkboxValue,
        onCheckedChange = null,
        modifier = Modifier.minimumInteractiveComponentSize()
    )
    Text(
        text = "Checkbox", 
        modifier = Modifier.padding(end = 12.dp)
    )
}
```

The key points are:

1. Set `Modifier.toggleable` on the enclosing `Row` layout with the checkbox state handling and `role = Role.Checkbox`. (Applying the click handler here automatically merges the child descendants' semantics.)
2. In the `Checkbox`, set `onCheckedChange = null`.
3. Appropriately size the `Checkbox` using `Modifier.minimumInteractiveComponentSize()` (Material 3) or `Modifier.defaultMinSize()` (Material 2), because nulling `onCheckedChange` will remove Compose's automatic tap target padding.  

`Switch` works similarly with `role = Role.Switch`.

`RadioButton` requires `Modifier.selectable` on the enclosing `Row` layout, `role = Role.RadioButton`, and the layout enclosing all of the labeling radio button layouts (here, a `Column`) requires `Modifier.selectableGroup()` to impose single-selection semantics.

```kotlin
val initialRadioButtonIndex = 0
val buttonLabels = listOf("Yes", "No")
val (radioButtonSelection, setRadioButtonSelection) = remember { mutableState(initialRadioButtonIndex) }
Column(modifier = Modifier.selectableGroup()) {
    buttonLabels.forEachIndexed { index: Int, label: String ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = (radioButtonSelectionState == index),
                    role = Role.RadioButton,
                    onClick = setRadioButtonSelection
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = (radioButtonSelectionState == index),
                onClick = null,
                modifier = Modifier.minimumInteractiveComponentSize()
            )
            Text(
                text = label, 
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}
```


## Use a visible label text and the `Modifier.semantics` `contentDescription` property to associate a label with a `Slider` or `RangeSlider`

`Slider` and `RangeSlider` composables are labeled by their `contentDescription` semantics property. They must also have a visible label and the text of the visible label must be present in the `contentDescription`.

Notes: 

- `Slider` composables also require keyboard accessibility remediation, `stateDescription`, and `liveRegion` semantics, but these are not shown below.
- `RangeSlider` composable are not keyboard accessible, so should be avoided in Compose screens, if possible. 
    - (Even wrapping a View-based `RangeSlider` in `AndroidView` will fail due to a known keyboard focus issue with View interop. See https://issuetracker.google.com/issues/255628260 for details.)

For example:

```kotlin

// Technique: Provide a visible label
val labelText = "Rating"
Text(labelText)

val (ratingValue, setRatingValue) = remember { mutableStateOf(0.0f) }
val range = 0f..10f
val steps = 9 // steps between the start and end point (exclusive of both)
Slider(
    value = ratingValue,
    onValueChange = setRatingValue,
    modifier = Modifier
        // ... handle keyboard accessibility
        .semantics {
            // Technique: Slider contentDescription must duplicate label text, because Slider does 
            // not support a text label. (See https://issuetracker.google.com/issues/236988201.)
            // However, contentDescription can extend the label text.
            contentDescription = labelText
            // ... handle stateDescription and liveRegion announcement
        },
    valueRange = range,
    steps = steps
)
```

(Note: The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.)

----

© Copyright 2023-2024 CVS Health and/or one of its affiliates. All rights reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.
