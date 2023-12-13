# TextField Controls
`TextField` controls require specific construction in order to be accessible. Specifically, they must have a label, be adjusted to remove a keyboard trap, and set the appropriate keyboard type, options, and actions for their data. These techniques support WCAG 2 [Success Criterion 1.3.5 Identify Input Purpose](https://www.w3.org/TR/WCAG21/#identify-input-purpose) and [Success Criterion 2.1.2 No Keyboard Trap](https://www.w3.org/TR/WCAG21/#no-keyboard-trap).

The required techniques are:

1. Set the `TextField` `label` property.
2. Remediate the keyboard trap that makes Tabs part of the `TextField` data:
  1. Debounce focus changes with `Modifier.onFocusChanged()`.
  2. Remember the focus state after debouncing.
  3. Apply `Modifier.onPreviewKeyEvent()` to handle keyboard activity (Tab, Shift+Tab, and Enter), if the field remained focused after debouncing, and before the key event became part of the `TextField` data.
3. Set soft keyboard type and options with the `TextField` `keyboardOptions` property. See [Keyboard Types and Options](../interactions/KeyboardTypes.md) for details.
4. Set the appropriate soft keyboard actions using the `TextField` `keyboardOptions` `imeAction` property and the `TextField` `keyboardActions` property. See [Keyboard Actions](../interactions/KeyboardActions.md) for details.

In addition, appropriate error handling must be added to any `TextField` in support of WCAG 2 [Success Criterion 3.3.1 Error Identification](https://www.w3.org/TR/WCAG21/#error-identification). Error handling techniques include:

* Setting the `TextField` `isError` property when the field's value is in error.
* Declaring a semantic error text using the `Modifier.semantics` `error()` property.
* Displaying a visual error text using the Material Design 3 `TextField` `supportingText` property (or a secondary `Text` field for Material Design 2 apps). (This error text may need to be hidden from accessibility services so it does not duplicate the announcement made by error semantics.)
* Displaying a visual error icon using the `TextField` `leadingIcon` or `trailingIcon` property.

Note also that `TextField` presently announces an additional message, "Error: Invalid input", whenever isError is true. This message cannot presently by suppressed. See the Jetpack Compose source code for CommonDecorationBox() for details.

## Keyboard Trap Remediation
The following `Modifier` extension function encapsulates all of the keyboard trap remediations:

```kotlin
fun Modifier.nextOnTabAndHandleEnter(
    enterCallback: (() -> Unit)? = null
): Modifier {
    return this.composed {
        val focusManager = LocalFocusManager.current
        // Key technique 2a: Remember the focus state.
        var hasFocus by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        this@composed
            // Key techniques 1 & 2b: Debounce focus changes and track the focus state.
            .onFocusChanged { focusState ->
                scope.launch {
                    delay(FOCUS_DEBOUNCE_TIME)
                    hasFocus = focusState.hasFocus
                }
            }
            // Key technique 3: If focus remains on this control, handle Tab, Shift+Tab, and Enter 
            // keys before a keyEvent becomes part of the TextField's text data.
            // Note that Enter key handling is optional and is hoisted to the caller's context.
            .onPreviewKeyEvent { keyEvent ->
                if (hasFocus && keyEvent.nativeKeyEvent.keyCode == KEYCODE_TAB) {
                    if (keyEvent.nativeKeyEvent.isShiftPressed) {
                        focusManager.moveFocus(FocusDirection.Previous)
                    } else {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                    true
                } else if (
                    enterCallback != null
                    && (keyEvent.key == Key.Enter || keyEvent.key == Key.NumPadEnter)
                ) {
                    enterCallback()
                    true
                } else {
                    false
                }
            }
    }
}
```

## Example `TextField`

The following code presents a `TextField` for entering a required phone number:

```kotlin
val (phoneNumber, setPhoneNumber) = remember { mutableStateOf("") }
val isError = phoneNumber.isBlank()

OutlinedTextField(
    value = phoneNumber,
    onValueChange = setPhoneNumber,
    // Key technique: Handle keyboard trap with Modifier.nextOnTabAndHandleEnter().
    modifier = Modifier
        .nextOnTabAndHandleEnter()
        .padding(top = 8.dp)
        .fillMaxWidth()
        .semantics {
            // Error technique: Announce errors. TalkBack prepends the text "Error: " to the
            // announcements, so use a different error string than the supportingText.
            if (isError) error("Phone number required. Enter a phone number.")
        },
    // Key technique: Label the TextField.
    label = {
        Text("Phone (required)")
    },
    // Error technique: Display a visual error message using supportingText.
    supportingText = {
        // Because the Modifier.semantics error property is set, the supportingText must be hidden 
        // from accessibility services (with invisibleToUser() semantics) to prevent a duplicate 
        // announcement.
        if (isError) {
            Text(
                text = "Error: Phone number required. Enter a phone number.",
                modifier = Modifier.semantics { invisibleToUser() }
            )
        }
    },
    // Error technique: Signal display of the error state by the TextField.
    isError = isError,
    // Key technique: use KeyboardType.Phone to enter phone numbers.
    keyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Phone
    )
)
```

(Note: The hard-coded text shown in these examples are only used for simplicity. _Always_ use externalized string resource references in actual code.)

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