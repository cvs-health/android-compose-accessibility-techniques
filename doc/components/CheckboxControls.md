# Checkbox Controls
`Checkbox` controls require specific construction in order to be accessible. Specifically they must have a wrapping layout to programmatically associate a label and subsume the `Checkbox`'s role and toggle handling. These techniques support WCAG 2 [Success Criterion 1.3.1 Info and Relationships](https://www.w3.org/TR/WCAG21/#info-and-relationships) and [Success Criterion 4.1.2 Name, Role, Value](https://www.w3.org/TR/WCAG21/#name-role-value).

The required techniques are:

1. Wrap the `Checkbox` and a `Text` (or `Icon`) label in a layout composable (generally a `Row`).
2. Use `Modifier.toggleable()` on the wrapping layout to take over the `Checkbox`'s role and `onValueChange` handling.
3. Null out the `Checkbox`'s `onValueChange` handling.
4. Resize the `Checkbox` using `Modifier.minimumInteractiveComponentSize()` (Material 3) or `Modifier.defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)` (Material 2) to maintain its prior size.

For example:

```kotlin
val (isBoldText, setIsBoldText) = remember { mutableStateOf(false) }
Row(
    modifier = modifier
        .toggleable(
            value = isBoldText,
            role = Role.Checkbox,
            onValueChange = setIsBoldText
        ),
    verticalAlignment = Alignment.CenterVertically
) {
    Checkbox(
        checked = isBoldText,
        onCheckedChange = null,
        modifier = Modifier.minimumInteractiveComponentSize()
    )
    Text(text = "Bold text", modifier = Modifier.padding(end = 12.dp))
}
```

Notes:
* The techniques for `Checkbox` and `Switch` are identical, except for the Role assigned (and layout considerations).
* Applying `Modifier.toggleable` to the `Row` automatically merges the child descendants' semantics so the `Row` is read as a unit in TalkBack.
* Nulling the `Chackbox`'s `onCheckedChange` handling removes its automatic sizing, which should generally be restored a sizing modifier as shown.
* The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.


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
