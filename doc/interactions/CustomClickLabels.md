# Custom Click Labels
By default, "clickable" composables announce their click action in TalkBack as "Double-tap to activate." This announcement can be partially customized to improve the user experience; for example, to say "Double-tap to show details" or "Double-tap to sign in." 

Notes: 

* Assigning a custom action label does not change TalkBack's standard click action description "Double-tap to ...", only what is said after that point.
* Custom click action labels can also affect how Switch Access displays a selected control when multiple actions are available on that control.

## `Modifier.clickable()`'s `onClickLabel` parameter

Most composables can accept a customized click action label when they are made clickable by `Modifier.clickable()`. Just set that function's `onClickLabel` parameter with the custom label text.

For example:

```kotlin
Card(
    modifier = Modifier
        // Technique: Provide a custom onClickLabel to Modifier.clickable().
        .clickable(
            onClickLabel = "see details"
        ) {
            // ...
        }
) {
    // ...
}
```

# Buttons and `Modifier.semantics`' `onClick()` property's `label` parameter

Unfortunately, Material Design `Button`s, `IconButton`s, and `Surface` take an `onClick` handler, but do not support a corresponding `onClickLabel` parameter. 

Fortunately, it is possible to customize their click action labels by applying `Modifier.semantics` with an `onClick()` property and setting its `label` parameter.

For example:

```kotlin
val clickAction = { 
    // do something...
}
Button(
    onClick = clickAction, // Activated by touchscreen and keyboard.
    modifier = Modifier.semantics {
        onClick(
            label = "show details"
        ) {
            // Activated by assistive technologies.
            // Always do the same thing onClick does.
            clickAction.invoke() 
            // And must return if that operation was successfully applied, if known.
            true
        }
    }
) {
    // Button label content...
}
```

Do not use `Modifier.clickable()` to add a custom click action label to a button or clickable `Surface`, as it will affect that composable's keyboard accessibility.

## Custom long-click labels

Composables can also be made long-clickable and given a customized long-click action label. 

For most composables, a custom long-click action and its custom label are set using `Modifier.combinedClickable()`'s `onLongClickLabel` property.

For `Button` composables, the long-click action and its custom label can be set with `Modifier.semantics` `onLongClick()` property.

(Note: The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.)

----

© Copyright 2024 CVS Health and/or one of its affiliates. All rights reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.