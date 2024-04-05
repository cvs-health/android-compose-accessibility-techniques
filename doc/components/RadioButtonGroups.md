# RadioButton Groups
Groups of `RadioButton` controls require specific construction in order to be accessible. Specifically, these techniques support WCAG [Success Criterion 1.3.1 Info and Relationships](https://www.w3.org/TR/WCAG22/#info-and-relationships) and [Success Criterion 4.1.2 Name, Role, Value](https://www.w3.org/TR/WCAG22/#name-role-value).

The first required technique is that the outer enclosing composable layout be marked with `Modifier.selectableGroup()`. This modifier imposes single-selection list semantics on the `RadioButton` controls within the group and adds list semantics announcements in TalkBack (e.g., "2 of 3") to each `RadioButton`.

For example:

```kotlin
Column(modifier = Modifier.selectableGroup()) {
    // ... RadioButton controls ...
}
```

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

Notes:

* The `groupLabel` is not read as part of each `RadioButton` in TalkBack. While different from the web, this behavior is expected on Android.
* The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.

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
