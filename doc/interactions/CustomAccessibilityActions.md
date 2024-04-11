# Custom Accessibility Actions
In addition to customizing the labels associated with pre-existing accessibility actions (such as the `clickLabel` property), it is possible to create custom accessibility actions to improve the user experience of those applying Assistive Technologies such as TalkBack and Switch Access.

Custom accessibility actions are commonly used to capture multiple click actions associated with a single logical unit of text, such as a card or list item so that an assistive technology user can both access those actions and navigate the logical units of text more easily.

Consider a card with multiple `Text` and `Button` controls. All of the units of text can be combined as one with the techniques of [Content grouping](../content/ContentGrouping.md), but each button remains a separate focus target that must be swiped across to get to the next card. Adding custom accessibility actions for the button controls allows these buttons to be removed from the accessibility tree: each button on the card becomes a custom accessibility action that is available from the TalkBack menu's "Actions" menu or the Switch Access "Menu" view and are no longer separate focus targets in Accessibility Services.

To implement custom accessibility actions, two steps are required:

* Add an `Modifier.semantics` to each card (or list item) containing actionable composables, and use the `customActions` property to add a list of custom accessibility actions created by new instances of `CustomAccessibilityAction()`. Each `CustomAccessibilityAction` object must declare a `label` String and an `action` function that takes no parameters and returns a Boolean value indicating the success or failure of the action.

```kotlin
OutlinedCard(
    modifier = Modifier.semantics(mergeDescendants = true) {
        customActions = listOf(
            CustomAccessibilityAction("Show post details") {
                viewModel.showPostDetails(cardId); true
            },
            CustomAccessibilityAction("Like this post") {
                viewModel.showPostDetails(cardId); true
            },
            // ...
        )
    },
    // ...
) { /* ... */ }
```

* Mark actionable composables within the card (or list item) with `Modifier.semantics { invisibleToUser() }` or wrap them in a composable employing `Modifier.clearAndSetSemantics {}`. Either of these semantics settings will prevent those actionable composables from appearing individually in Accessibility Services while remaining touch clickable and keyboard focusable.

```kotlin
Row(
    // ...
) {
    LikeButton(cardId, modifier = Modifier.semantics { invisibleToUser() })
    ShareButton(cardId, modifier = Modifier.semantics { invisibleToUser() })
    ReportButton(cardId, modifier = Modifier.semantics { invisibleToUser() }) 
}
```

```kotlin
Row(
    modifier = Modifier.clearAndSetSemantics { },
    // ...
) {
    LikeButton(cardId)
    ShareButton(cardId)
    ReportButton(cardId)
}
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