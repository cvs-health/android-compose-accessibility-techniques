# TextField Controls
`TextField` controls require specific construction in order to be accessible. Specifically, they must have a label, be adjusted to remove a keyboard trap, and set the appropriate keyboard type, options, and actions for their data. These techniques support WCAG [Success Criterion 1.3.5 Identify Input Purpose](https://www.w3.org/TR/WCAG22/#identify-input-purpose) and [Success Criterion 2.1.2 No Keyboard Trap](https://www.w3.org/TR/WCAG22/#no-keyboard-trap).

The required techniques are:

1. Set the `TextField` `label` property.
2. Remediate the keyboard trap that makes Tabs part of the `TextField` data:
    1. Debounce focus changes with `Modifier.onFocusChanged()`.
    2. Remember the focus state after debouncing.
    3. Apply `Modifier.onPreviewKeyEvent()` to handle keyboard activity (Tab, Shift+Tab, Enter, and Up/Down direction pad keys), if the field remained focused after debouncing, and before the key event became part of the `TextField` data.
3. Set soft keyboard type and options with the `TextField` `keyboardOptions` property. See [Keyboard Types and Options](../interactions/KeyboardTypes.md) for details.
4. Set the appropriate soft keyboard actions using the `TextField` `keyboardOptions` `imeAction` property and the `TextField` `keyboardActions` property. See [Keyboard Actions](../interactions/KeyboardActions.md) for details.
5. Apply autofill with appropriate `ContentType`(s) semantics to the `TextField`, if the data to be entered is supported by autofill. See [Autofill Controls](../components/AutofillControls.md) for details.

Appropriate error handling must be added to any `TextField` in support of WCAG [Success Criterion 3.3.1 Error Identification](https://www.w3.org/TR/WCAG22/#error-identification). Error handling techniques include:

* Setting the `TextField` `isError` property when the field's value is in error.
* Declaring a semantic error text using the `Modifier.semantics` `error()` property.
* Displaying a visual error text using the Material Design 3 `TextField` `supportingText` property (or a secondary `Text` field for Material Design 2 apps). (This error text may need to be hidden from accessibility services so it does not duplicate the announcement made by error semantics.)
* Displaying a visual error icon using the `TextField` `leadingIcon` or `trailingIcon` property.

Brief instructions can be presented using the Material Design 3 `TextField` `supportingText` property (or in a separate, preceding `Text` field for Material Design 2 apps).

If a `leadingIcon` or `trailingIcon` is used an active control, for example to clear the `TextField`'s value or to perform a search, assure that this icon receives a sufficiently visible keyboard focus indicator. (Not shown below.)

## Keyboard Trap Remediation
The following `Modifier` extension function encapsulates all of the keyboard trap remediations:

```kotlin
fun Modifier.nextOnTabAndHandleEnter(
    enterCallback: (() -> Unit)? = null
): Modifier {
    return this.composed {
        val focusManager = LocalFocusManager.current
        val scope = rememberCoroutineScope()
       
        // Key technique 2a: Remember the focus state. hasFocus tracks focus on this composable
        // and all its children; isFocus tracks focus on this composable itself, not its children.
        var hasFocus by remember { mutableStateOf(false) }
        var isFocused by remember { mutableStateOf(false) }
       
        this@composed
            // Key techniques 1 & 2b: Debounce focus changes and track the focus state.
            .onFocusChanged { focusState ->
                scope.launch {
                    delay(FOCUS_DEBOUNCE_TIME)
                    hasFocus = focusState.hasFocus
                    isFocused = focusState.isFocused
                }
            }
            // Key technique 3: If focus remains on this control, handle Tab, Shift+Tab, Enter, and 
            // the Up and Down direction pad keys before a keyEvent becomes part of the TextField's 
            // text data. (Note that Enter key handling is optional and is hoisted to the caller's 
            // context.)
            .onPreviewKeyEvent { keyEvent ->
                if (hasFocus && keyEvent.nativeKeyEvent.keyCode == KEYCODE_TAB) {
                    if (keyEvent.nativeKeyEvent.action == ACTION_UP) {
                        if (keyEvent.nativeKeyEvent.isShiftPressed) {
                            focusManager.moveFocus(FocusDirection.Previous)
                        } else {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    }
                    true
                } else if (hasFocus && keyEvent.nativeKeyEvent.keyCode == KEYCODE_DPAD_DOWN) {
                    if (keyEvent.nativeKeyEvent.action == ACTION_UP) {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                    true
                } else if (hasFocus && keyEvent.nativeKeyEvent.keyCode == KEYCODE_DPAD_UP) {
                    if (keyEvent.nativeKeyEvent.action == ACTION_UP) {
                        focusManager.moveFocus(FocusDirection.Up)
                    }
                    true
                } else if (
                    // Key technique 4: Use isFocused here, so that if focus is on a child
                    // composable, such as a trailing icon button, the child's own keyboard handling
                    // will not get overridden. (Note that the keyboard navigation handling above
                    // does apply to all child composables.)
                    isFocused
                    && enterCallback != null
                    && (keyEvent.key == Key.Enter || keyEvent.key == Key.NumPadEnter)
                ) {
                    if (keyEvent.nativeKeyEvent.action == ACTION_UP) {
                        enterCallback()
                    }
                    true
                } else {
                    false
                }
            }
    }
}
```

## Example `TextField`

The following code presents a `TextField` for entering a required website address (URI):

```kotlin
val (uri, setUri) = remember { mutableStateOf("") }
val isError = uri.isBlank()

OutlinedTextField(
    value = uri,
    onValueChange = setUri,
    // Key technique: Handle keyboard trap with Modifier.nextOnTabAndHandleEnter().
    modifier = Modifier
        .nextOnTabAndHandleEnter()
        .padding(top = 8.dp)
        .fillMaxWidth()
        .semantics {
            // Error technique: Announce errors. TalkBack prepends the text "Error: " to the
            // announcements, so use a different error string than the supportingText.
            if (isError) error("Website URI is required. Enter a web site address.")
        },
    // Key technique: Label the TextField.
    label = {
        Text("Website URI")
    },
    // Use supportingText for instructions and error message display.
    supportingText = {
        if (isError) {
           // Error technique: Display a visual error message using supportingText.
           // Because the Modifier.semantics error property is set, the supportingText must be 
           // hidden from accessibility services (with hideFromAccessibility() semantics) to prevent
           // a duplicate announcement.
            Text(
                text = "Error: Website URI is required. Enter a web site address.",
                modifier = Modifier.semantics { hideFromAccessibility() }
            )
        } else {
            // Optional instruction technique: Display brief instructions using supportingText when 
            // field is not in error.
            Text("A non-empty URI is required.")
        }
    },
    // Error technique: Signal display of the error state by the TextField.
    isError = isError,
    // Key technique: use KeyboardType.Uri to enter web site addresses.
    keyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Uri
    )
)
```

(Note: The hard-coded text shown in these examples are only used for simplicity. _Always_ use externalized string resource references in actual code.)

----

Copyright 2023-2025 CVS Health and/or one of its affiliates

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.