# Keyboard Types and Options
It is generally necessary to explicitly set the soft keyboard type (and other options) of text input controls in order to obtain the appropriate values for data, create a good user experience, and to support part of the WCAG 2 [Success Criterion 1.3.5 Identify Input Purpose](https://www.w3.org/TR/WCAG21/#identify-input-purpose).

The soft keyboard type and options are set by `keyboardOptions` property of any `TextField` composable. The complete list of keyboard options includes `capitalization`, `autocorrect`, and `keyboardType`, as described in this Android documentation: [KeyboardOptions](https://developer.android.com/reference/kotlin/androidx/compose/foundation/text/KeyboardOptions). The list of `keyboardType` values is available here:
[KeyboardType](https://developer.android.com/reference/kotlin/androidx/compose/ui/text/input/KeyboardType). (The `imeActions` keyboard option is described separately, in conjunction with `TextField` `keyboardActions` property.)

In the following example, a text input field accepts a phone number using the dial keypad style of soft keyboard using `keyboardType = KeyboardType.Phone`.

```kotlin
OutlinedTextField(
    // ...
    label = {
        Text("Phone")
    },
    keyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Phone
    )
)
```

The `keyboardOptions` property can either be set by copying the `KeyboardOptions.Default` object (with adjusted properties) or by creating a new object using the `KeyboardOptions()` constructor.

Notes:
* For some devices and configurations, different keyboard types will display the same soft keyboard.
* Adding `android:windowSoftInputMode="adjustResize"` to the `<activity>` element in AndroidManifest.xml can help fit both screen content and the soft keyboard on-screen at the same time.
* Setting `KeyboardType.Password` or `KeyboardType.NumberPassword` does not supply input field masking; they only control the soft keyboard. Add `visualTransformation = PasswordVisualTransformation()` to a `TextField` for masking.
* The other part of supporting [Success Criterion 1.3.5 Identify Input Purpose](https://www.w3.org/TR/WCAG21/#identify-input-purpose) involves applying the [androidx.compose.ui.autofill](https://developer.android.com/reference/kotlin/androidx/compose/ui/autofill/package-summary) API where possible. 
* It is also necessary to handle the keyboard trap in default `TextField` composables to avoid issues with WCAG 2 [Success Criterion 2.1.2 No Keyboard Trap](https://www.w3.org/TR/WCAG21/#no-keyboard-trap).
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