# ListItem Layouts
`ListItem` layout composables are accessible by default. They improve accessibility by applying `Modifier.semantics(mergeDescendants=true)` on an inner `Surface` so all text across the ListItem is unified during TalkBack announcement.

If a `ListItem` wraps and labels an actionable control (`Switch`, `Checkbox`, or `RadioButton`), be sure to apply the appropriate action Modifier (`toggleable()`, or `selectable()`) to the `ListItem` as a whole. Or if the `ListItem` itself is to be clickable, apply `Modifier.clickable()`.

## Example accessible, toggleable `ListItem` layout
The following example code shows an accessible, toggleable `ListItem` for a "Dark theme" setting:

```kotlin
val (useDarkTheme, setUseDarkThem) = remember { mutableStateOf(false) }
ListItem(
    headlineContent = {
        Text("Dark theme")
    },
    modifier = Modifier.toggleable(
        value = useDarkTheme,
        role = Role.Switch,
        onValueChange = setUseDarkThem
    ),
    supportingContent = {
        Text("Forces light text on dark background.")
    },
    trailingContent = {
        Switch(checked = useDarkTheme, onCheckedChange = null)
    }
)
```

(Note: The hard-coded text shown in these examples are only used for simplicity. _Always_ use externalized string resource references in actual code.)

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
