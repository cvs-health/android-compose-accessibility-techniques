# Pop-up Messages (AlertDialog, Snackbar, and Toast controls)

The use of pop-up messages in Android involves usability trade-offs for different groups, particularly users of assistive technologies like TalkBack and Switch Access.

There are three standard controls used for pop-up messages, and one non-pop-up message option. 

* `Toast`s are an early Android non-modal pop-up message control.
* The `Snackbar` displays a non-modal pop-up message with optional action buttons. It is Material Design's replacement for the `Toast`.
* `AlertDialog` (or `BasicAlertDialog`) creates a pop-up modal dialog.
* On-screen text with `liveRegion` semantics (and/or accessibility announcements) can deliver similar messages without a pop-up. 

Each approach has advantages and disadvantages and different approaches serve different use cases better.

## `Toast` non-modal, pop-up messages
`Toast`s are an early Android non-modal pop-up message control.

* The content in a `Toast` is announced by TalkBack when it is shown.
* `Toast`s cannot contain action buttons nor be manually dismissed.
* A `Toast` only appears for a limited time, making it hard for some users to read and understand.
* A `Toast` obscures some screen content, although not much and only temporarily.

A `Toast` does one thing: it announces a status message, and then it goes away. However, it is not a Material Design control.

For example:

```kotlin
// Technique 1: Toasts require context.
val context = LocalContext.current
val toastMessage = "I am a Toast pop-up message."
Button(
    onClick = {
        // Technique 2: Use Toast.makeText(...).show().
        Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
    }
) {
    Text(stringResource("Show a Toast pop-up message"))
}
```

## `Snackbar` non-modal pop-up messages
The `Snackbar` control is Material Design's replacement for the `Toast`. It displays a non-modal pop-up message with optional action buttons and can be of either limited duration or appear until dismissed.

* The content in a `Snackbar` is announced by TalkBack when it is shown.
* A `Snackbar` may contain action buttons and/or a "Dismiss" icon button. However, those buttons are extremely hard to focus on with some assistive technologies, especially TalkBack.
* A `Snackbar` may appear only for a limited time, making it hard for some users to understand, and making it unlikely for an assistive technology user to activate any contextual buttons on them.
* A `Snackbar` obscures more screen content than a `Toast`.

`Snackbar`s are flexible controls, but they do not do all things well for all users.

For example:

```kotlin
// Technique 1: Remember SnackbarHostState and CoroutineScope.
val snackbarHostState = remember { SnackbarHostState() }
val coroutineScope = rememberCoroutineScope()

// Technique 2: Pass the SnackbarHost to the Scaffold.
Scaffold(
    // ...
    snackbarHost = { SnackbarHost(snackbarHostState) }
) {
    val snackbarMessage = "This is a Snackbar pop-up message with dismiss button."
    Button(
        onClick = {
            // Technique 3: Use snackbarHostState.showSnackbar() in a coroutine scope.
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = snackbarMessage,
                    withDismissAction = true,
                    duration = SnackbarDuration.Long
                )
            }
        }
    ) {
        Text("Show a limited-duration, dismissible Snackbar pop-up message.")
    }
}
```

## `AlertDialog` modal pop-up messages

The `AlertDialog` (or `BasicAlertDialog`) composable creates a modal pop-up dialog.

* The dialog's first composable's content is announced by TalkBack.
    * Note: Dialog titles do not require heading semantics.
* The dialog is persistent, providing time for a user to read the content, but requiring taking an action to dismiss it.
* Focus is constrained to the pop-up dialog, so the underlying screen is unavailable, which makes it easier to focus on the dialog's action controls.

For example:

```kotlin
// Remember if the dialog is open.
val isAlertDialogOpen by remember { mutableStateOf(false) }

Button(
    onClick = { isAlertDialogOpen = true } // Open the dialog.
) {
    Text("Show AlertDialog pop-up message")
}

// Compose the dialog if it is open.
if (isAlertDialogOpen) {
    AlertDialog(
        onDismissRequest = { isAlertDialogOpen = false }, // Close the dialog if it is dismissed
        confirmButton = {
            TextButton(
                onClick = { setIsShowMoreDialogOpen(false) }
            ) {
                Text("OK")
            }
        },
        title = {
            Text("AlertDialog title")
        },
        text = {
            Text("This is an AlertDialog pop-up message.")
        }
    )
}
```

Alternatively, this can be done using Material Design 3's `BasicAlertDialog` (with far more manual spacing and theming):

```kotlin
// Remember if the dialog is open.
val isBasicAlertDialogOpen by remember { mutableStateOf(false) }

Button(
    onClick = { isBasicAlertDialogOpen = true } // Open the dialog.
) {
    Text("Show BasicAlertDialog pop-up message")
}

// Compose the dialog if it is open.
if (isBasicAlertDialogOpen) {
    BasicAlertDialog(
        onDismissRequest = { isBasicAlertDialogOpen = false } // Close the dialog if it is dismissed
    ) {
        // Apply Material Design-specified shape, background tone, padding, and spacing.
        Surface(
            modifier = Modifier.wrapContentWidth().wrapContentHeight(),
            shape = MaterialTheme.shapes.extraLarge, // dialog default shape
            tonalElevation = AlertDialogDefaults.TonalElevation // dialog default tonal elevation
        ) {
            Column(modifier = Modifier.padding(24.dp)) { // dialog surrounding padding
                Text(
                    text = "BasicAlertDialog title",
                    modifier = Modifier.padding(bottom = 16.dp), // space between title and content
                    style = MaterialTheme.typography.headlineSmall // dialog title text style
                )
              
                Text(
                    text = "This is a BasicAlertDialog pop-up message.",
                    modifier = Modifier.padding(bottom = 24.dp) // space between content and buttons
                )
              
                TextButton(
                    onClick = { isBasicAlertDialogOpen = false }, // Close the dialog.
                    modifier = Modifier.align(Alignment.End) // button positioning
                ) {
                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.labelLarge // dialog button text style
                    )
                }
            }
        }
    }
}
```

## On-screen `Text` messages

On-screen `Text` with `liveRegion` semantics can also be used for status messages. 
* `liveRegion` content is announced by a screen reader whenever it changes (excepting initial composition and when not composed).
* On-screen `Text` remains persistently on-screen and does not obscure other content. (However, the message may not be visible, depending on the screen's scroll position.)

For example, the results of a calculation would best displayed in a visually-persistent manner using on-screen `Text` with `liveRegion` semantics to announce its new value when recalculated.  

## Conclusions

There are two major use cases for a pop-up message:

* The simple status message use case, in which only a message is displayed with no action. 
    * For example, a "Loading complete" message.
* The status message with contextual action use case, in which a message has associated potential actions.
    * For example, a "Deletion complete" message, which offers the option of "Undo" or a "Time expiring" message, which offers the option of extending the timeout.

For the simple status message use case, best practice is to use a limited-time, dismissible `Snackbar` if the message should be visually transient. Or use on-screen `Text` with `liveRegion` semantics if the message should be visually persistent.

For status messages with contextual actions, prefer `AlertDialog` modal pop-up messages; although on-screen `Text` can also work. Avoid the `Snackbar`, since its contextual action buttons are nearly inaccessible.

The relevant WCAG success criteria are:

* [Success Criterion 4.1.3 Status Messages](https://www.w3.org/TR/WCAG22/#status-messages)
* [Success Criterion 2.1.1 Keyboard](https://www.w3.org/TR/WCAG22/#keyboard), which logically extends to assistive technologies that would be keyboard-driven on the web, such as Switch Access and TalkBack
* [Success Criterion 2.2.1 Timing Adjustable](https://www.w3.org/TR/WCAG22/#timing-adjustable)
    * Note that both `Toast` and `Snackbar` honor the Android "Time to take action (Accessibility timeout)" setting.
* [Success Criterion 2.4.11 Focus Not Obscured (Minimum)](https://www.w3.org/TR/WCAG22/#focus-not-obscured-minimum)
* [Success Criterion 3.2.2 On Input](https://www.w3.org/TR/WCAG22/#on-input)

(Note: The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.)

----

Copyright 2024 CVS Health and/or one of its affiliates

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.