# RadioButton Groups
Groups of `RadioButton` controls require specific construction in order to be accessible. Specifically, these techniques support WCAG [Success Criterion 1.3.1 Info and Relationships](https://www.w3.org/TR/WCAG22/#info-and-relationships) and [Success Criterion 4.1.2 Name, Role, Value](https://www.w3.org/TR/WCAG22/#name-role-value).

## Create a `selectableGroup()`

The first required technique is that the outer enclosing composable layout be marked with `Modifier.selectableGroup()`. This modifier imposes single-selection list semantics on the `RadioButton` controls within the group and adds list semantics announcements in TalkBack (e.g., "2 of 3") to each `RadioButton`.

For example:

```kotlin
Column(modifier = Modifier.selectableGroup()) {
    // ... RadioButton controls ...
}
```

## Associate `RadioButton` and labeling `Text`

The second required technique is that each `RadioButton` control have a programmatically associated label. This labeling is accomplished with several steps:

1. Wrap the `RadioButton` and a `Text` (or `Icon`) label in a layout composable (generally a `Row`).
2. Use `Modifier.selectable()` on the wrapping layout to take over the `RadioButton`'s role and `onClick` handling. 
3. Null out the `RadioButton`'s `onClick` handling. 
4. Resize the `RadioButton` using `Modifier.minimumInteractiveComponentSize()` (Material 3) or `Modifier.defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)` (Material 2) to maintain its prior size.

For example:

```kotlin
// Option Orange: index = 0
Row(modifier = Modifier
    .fillMaxWidth()
    .selectable(
        selected = (selectedIndex == 0),
        role = Role.RadioButton,
        onClick = { selectHandler(0) }
    ),
    verticalAlignment = Alignment.CenterVertically
) {
    RadioButton(
        selected = (selectedIndex == 0),
        onClick = null,
        modifier = Modifier.minimumInteractiveComponentSize()
    )
    Spacer(Modifier.width(4.dp))
    Text(text = "Orange")
}
```

Notes:

* Applying `Modifier.selectable` to the `Row` automatically merges the child descendants' semantics so the `Row` is read as a unit in TalkBack.
* Nulling the `RadioButton`'s `onClick` handling removes its automatic sizing, which should generally be restored a sizing modifier as shown.

Putting these pieces together yield the following sample RadioButtonGroup composable:

```kotlin
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
        Column(modifier = Modifier.selectableGroup()) {
            itemLabels.forEachIndexed { index: Int, label: String ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (selectedIndex == index),
                        role = Role.RadioButton,
                        onClick = { selectHandler(index) }
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (selectedIndex == index),
                        onClick = null,
                        modifier = Modifier.minimumInteractiveComponentSize()
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(text = label)
                }
            }
        }
    }
}
```

## Provide sufficient group context for each individual radio button option

The third technique is to make each radio button option contextually understandable when announced individually by a screen reader. Such disambiguation can be necessary because Jetpack Compose does not provide any way to associate the group label with the individual radio button controls. This is in contrast to both web sites (that use `<fieldset>` and `<legend>`) and to apps built with the Android View framework, which provides both the `labeledBy` and `containerTitle` accessibility properties to associate group labels with controls. 

For example, given a radio button with the group label "Are you at home?" and radio buttons items labeled "Yes" and "No", the context of the question is not announced by a screen reader when each radio button item is focused.

The simplest fix for such scenarios is to make the radio button label texts fully contextual. In the example above, the radio buttons could be labeled "Yes, I am at home" and "No, I am not at home".

However, if keeping the visible radio button labels terse is preferred, then the `contentDescription` of each radio button label text can be adjusted instead.

For example:

```kotlin

val (isAtHome, setIsAtHome) = remember { mutableStateOf(true) }
Column {
    // RadioButton group label
    Text("Are you at home?")

    Column(modifier = Modifier.selectableGroup()) {
        // "Yes" RadioButton with full group context in contentDescription
        Row(modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = isAtHome,
                role = Role.RadioButton,
                onClick = { setIsAtHome(true) }
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isAtHome,
                onClick = null,
                modifier = Modifier.minimumInteractiveComponentSize()
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = "Yes",
                modifier = Modifier.semantics {
                    contentDescription = "Yes, I am at home"
                }
            )
        }

        // "No" RadioButton with full group context in contentDescription
        Row(modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = !isAtHome,
                role = Role.RadioButton,
                onClick = { setIsAtHome(false) }
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = !isAtHome,
                onClick = null,
                modifier = Modifier.minimumInteractiveComponentSize()
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = "No",
                modifier = Modifier.semantics {
                    contentDescription = "No, I am not at home"
                }
            )
        }
    }
}
```

Note: Not all radio button groups need additional group context added to their radio button items. For example, a "Pick a color" radio button group does not need to additionally label an option like "Red", which is clearly a color. Whether added group context is needed or not is a design decision.

## `SingleChoiceSegmentedButtonRow` as a `RadioButton` group alternative

An alternative to the standard group of `RadioButton` user interface controls is the `SingleChoiceSegmentedButtonRow` which presents a set of horizontally-adjoining buttons, only one of which is presented in the selected state.

From an accessibility perspective, `SingleChoiceSegmentedButtonRow` automatically creates a semantic group and it groups labels with its controls. However, it too cannot associate a group label with its controls, so group context may still need to be added to each individual `SegmentedButton` control. 

Also, the limited horizontal real estate available in some mobile window sizes will limit both the number of options presented in a row and their text labels' lengths. Therefore, apply this user interface alternative with caution and test it with large font sizes.

For example:

```kotlin
val colorChoices = listOf("Red", "Green", "Blue")
val (selectedColor, setSelectedColor) = remember { mutableIntStateOf(0) }

Text("Pick a color:")
SingleChoiceSegmentedButtonRow(
    modifier = Modifier
        .testTag(radioButtonGroupsExample6SegmentedButtonRowTestTag)
        .fillMaxWidth()
        .height(IntrinsicSize.Min)
) {
    colorChoices.forEachIndexed { index, colorChoice ->
        SegmentedButton(
            selected = selectedColor == index,
            onClick = { setSelectedColor(index) },
            shape = SegmentedButtonDefaults.itemShape(index, options.size),
            modifier = Modifier.fillMaxHeight(),
            // Note: Role.RadioButton is the default, so Modifier.semantics is unnecessary here.
        ) {
            Text(colorChoice)
        }
    }
}
```

Note: The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.

----

Copyright 2023-2025 CVS Health and/or one of its affiliates

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.
