# UX Change Announcements
All user experiences changes resulting from user interactions or automatic updates must be announced to accessibility services (such as TalkBack) in order to support the WCAG 2 [Success Criterion 4.1.3 Status Messages](https://www.w3.org/TR/WCAG21/#status-messages). (That said, it is reasonable and acceptable to rate-limit the announcement of automatic updates or they can overwhelm the user.)

There are two key techniques for providing UX change announcements: `accessibilityLiveRegion` and `announceForAccessibility()`.

## Use the `Modifier.semantics` `liveRegion` property to announce field value changes

The `Modifier.semantics` `liveRegion` property indicates a field that should announce its value whenever it is changed. This property has two possible values: `LiveRegionMode.Polite` waits for any announcement in progress to complete, and `LiveRegionMode.Assertive` interrupts any announcement in progress. (The absence of a `liveRegion` semantic property indicates that the field should not announce its value on change.)

However, there is a known issue with `liveRegion` semantics (see https://issuetracker.google.com/issues/225780131). The work-around is to force a `contentDescription` in the same `Modifier.semantics` block that declares the `liveRegion`.

In the following example, whenever the `counterText` state value is updated, the `Text`'s new value will be announced by accessibility services.

```
val counterText = remember{ mutableStateOf("Counter: 0") }
Text(
    text = counterText.value,
    modifier = Modifier.semantics {
        liveRegion = LiveRegionMode.Polite
        contentDescription = counterText.value // required workaround for Google bug
    }
)
```

Notes:
* Fields with `accessibilityLiveRegion` active will announce their value when they are made visible (composed), including initial screen construction and on orientation change. This may not be an ideal audio user experience.
* No announcement is made when widgets are made not composed.

## Discouraged: Use the `View.announceForAccessibility()` method to announce events from code

The `View.announceForAccessibility()` method can be called at any point from code to make an announcement. This announcement is always "assertive." A View object can be obtained in a Composable context using `LocalView.current`.

For example:

```
val v = LocalView.current
v.announceForAccessibility("Loading completed.")
```

This approach to announcements is strongly discouraged, and is only described for completeness. Do not use it unless it is _essential_.

(Note: The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.)

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