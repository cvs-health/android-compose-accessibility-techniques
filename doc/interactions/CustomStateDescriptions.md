# Custom State Descriptions
Some standard Android composables also contain _state_ which they expose to Accessibility Services. For example, a `Checkbox` composable has two states announced by TalkBack as "checked" and "not checked". Similarly, the `Switch` composable has the states "On" and "Off". It is possible to customize these state descriptions to improve the user experience in TalkBack.

To change the state description of a stateful composable, use the `Modifier.semantics` `stateDescription` property as follows:

```
val (isAlarmActive, setIsAlarmActive) = remember { mutableStateOf(false) }
Row(
    modifier = modifier
        .toggleable(
            value = isAlarmActive,
            role = Role.Checkbox,
            onValueChange = setIsAlarmActive
        )
        .semantics {
            // Key technique: Set the Modifier.semantics stateDescription property based on the 
            // toggleable widget's current state.
            stateDescription = if (isAlarmActive) "Activated" else "Deactivated"
        },
    verticalAlignment = Alignment.CenterVertically
) {
    Checkbox(
        checked = isAlarmActive,
        onCheckedChange = null,
        modifier = Modifier.minimumInteractiveComponentSize()
    )
    Text("Home alarm system")
}
```

This `Checkbox` will announce its state in TalkBack as "Activated" or "Deactivated" instead of "Checked" and "Not checked".

Notes:

* Customizing `stateDescription` may not work successfully on all Android platforms; `ViewCompat.setStateDescription()` certainly does not. However, if `stateDescription` does not work, the standard state description text is announced. So attempting to improve the user experience by customizing the state description does no harm.
* The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code. 
  * When strings are externalized, the conditional code determining the localized state description value should be moved outside of the `Modifier.semantics` block so `stringResource()` can be called in a Composable context. 

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