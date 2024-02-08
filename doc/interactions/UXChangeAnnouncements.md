# UX Change Announcements
All user experiences changes resulting from user interactions or automatic updates must be announced to accessibility services (such as TalkBack) in order to support the WCAG 2 [Success Criterion 4.1.3 Status Messages](https://www.w3.org/TR/WCAG21/#status-messages). (That said, it is reasonable and acceptable to rate-limit the announcement of automatic updates or they can overwhelm the user.)

There are two key techniques for providing UX change announcements: `liveRegion` semantics and `announceForAccessibility()`.

## Use the `Modifier.semantics` `liveRegion` property to announce field value changes

The `Modifier.semantics` `liveRegion` property indicates a field that should announce its value whenever it is changed. This property has two possible values: `LiveRegionMode.Polite` waits for any announcement in progress to complete, and `LiveRegionMode.Assertive` interrupts any announcement in progress. (The absence of a `liveRegion` semantic property indicates that the field should not announce its value on change.)

However, there is a known issue with `liveRegion` semantics (see https://issuetracker.google.com/issues/225780131). The work-around is to force a `contentDescription` in the same `Modifier.semantics` block that declares the `liveRegion`.

In the following example, whenever the `counterText` state value is updated, the `Text`'s new value will be announced by accessibility services.

```
var counterText by remember{ mutableStateOf("Counter: 0") }
Text(
    text = counterText,
    modifier = Modifier.semantics {
        liveRegion = LiveRegionMode.Polite
        contentDescription = counterText // required workaround for Google bug
    }
)
```

Note: No announcement is made when widgets are initially composed or when they are no longer composed.

## Discouraged: Use the `View.announceForAccessibility()` method to announce events from code

The `View.announceForAccessibility()` method can be called at any point from code to make an announcement. This announcement is always "assertive." A View object can be obtained in a Composable context using `LocalView.current`.

For example:

```
val v = LocalView.current
v.announceForAccessibility("Loading completed.")
```

This approach to announcements is strongly discouraged, and is only described for completeness. Do not use it unless it is _essential_.

## AlertDialog announcements

A final option for announcing content is to put it into a `BasicAlertDialog` as the first composable, because that content is automatically announced when the dialog is displayed. 

This approach works particularly well for waiting indicators, which would otherwise require a `View.announceForAccessibility()` call on start. Also, `AlertDialog` automatically prevents all underlying screen content from being accessed, so custom lock-out mechanisms for the waiting state are not required.

For example:

```kotlin
// Button to launch the waiting indicator dialog
var isWaitingDialogVisible by remember { mutableStateOf(false) }
Button(
    onClick = { isWaitingDialogVisible = true }
) {
    Text("Show waiting indicator in AlertDialog")
}

// Display the waiting indicator dialog.
if (isWaitingDialogVisible) {
    BasicAlertDialog(
        onDismissRequest = { isWaitingDialogVisible = false }
    ) {
        Surface(
            // ...
        ) {
            CircularProgressIndicator(
                modifier = Modifier.semantics {
                    contentDescription = "Waiting..."
                }
            )
        }
    }
}

// Close the waiting indicator dialog when the process involved in completed.
// Also, use View.announceForAccessibility() to signal the end of the waiting period.
```

(Note: The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.)

----

Copyright 2023-2024 CVS Health and/or one of its affiliates

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
[http://www.apache.org/licenses/LICENSE-2.0]()

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.