# ModalBottomSheet Layouts
`ModalBottomSheet` layouts require specific construction in order to be accessible. Specifically, these techniques support WCAG 2 [Success Criterion 2.1.1 Keyboard](https://www.w3.org/TR/WCAG21/#keyboard), among others.

The following additions are needed to make a `ModalBottomSheet` accessible:

- Apply `Modifier.onKeyEvent` to capture any use of the escape (Esc) key and close the bottom sheet.
    - Note that a suspend function call in a coroutine scope is used to hide the bottom sheet asynchronously. 
- Limit the size of half-opened bottom sheets with `fillMaxHeight(0.5f)` so that no part of their content is off-screen.
- Provide a semantic pane title.
- Override the default `dragHandle` `contentDescription` to provide a meaningful bottom sheet title.
- Set the `ModalBottomSheet` `windowInsets` so bottom sheet padding can be measured, and add `bottom` padding to the bottom sheet contents so they avoid sliding under the bottom system navigation bar.
- Provide a visible bottom sheet title (preferably the same as the pane title).
- If a bottom sheet will change state that affects a `liveRegion` composable, be sure that state is changed only after the bottom sheet is dismissed; otherwise, the live region will not announce its new value. (Not shown below.)
- Set keyboard focus onto the bottom sheet contents when the bottom sheet opens. 

For example:

```kotlin
var openBottomSheet by rememberSaveable { mutableStateOf(false) }
val scope = rememberCoroutineScope()
val sheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = false // only expand this sheet half-way when opened
)
val focusRequester = remember { FocusRequester() } // to set keyboard focus

// Main screen contents...
OutlinedButton(
    onClick = { openBottomSheet = true }
) {
    Text("Select a bottom sheet item")
}

if (openBottomSheet) {
    val bottomSheetTitle = "Select an item"
    val dragHandleDescription = stringResource("'Select an item' bottom sheet drag handle")
    ModalBottomSheet(
        onDismissRequest = { openBottomSheet = false },
        modifier = Modifier
            // Allow the Esc key to dismiss the bottom sheet.
            .onKeyEvent { keyEvent ->
                if (keyEvent.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ESCAPE) {
                    // Handle sheet state change asynchronously. Use similar code to dismisses the 
                    // sheet anywhere outside of onDismissRequest(), such as button onClick 
                    // handlers in sheet contents.
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) { openBottomSheet = false }
                    }
                    true
                } else {
                    false
                }
            }
            // Since only a half-expanded sheet is shown, limit the size of the sheet; otherwise, 
            // it is possible to use a keyboard to tab to and select content that is off-screen.
            .fillMaxHeight(fraction = 0.5f)
            // The bottom sheet forms a separate pane, so give it a unique pane title.
            .semantics {
                paneTitle = bottomSheetTitle
            },
        sheetState = sheetState,
        // Override the default drag handle contentDescription to provide
        // a meaningful bottom sheet title.
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                modifier = Modifier.semantics { contentDescription = dragHandleDescription }
            )
        },
        // Set the windowInsets so bottom sheet padding can be measured.
        windowInsets = WindowInsets.safeDrawing
    ) {
        // Pad the bottom of the bottom sheet contents to avoid them sliding under the bottom system 
        // navigation bar and the last item becoming unfocusable with assistive technologies. 
        val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        LazyColumn(
            modifier = Modifier
                // Apply the bottom padding calculated above.
                .padding(bottom = bottomPadding)
                // Mark the bottom sheet contents so they can receive focus.
                .focusRequester(focusRequester)
        ) {
            // Provide a visual bottom sheet title, preferably with the same text as the pane title.
            item {
                Text(
                    text = bottomSheetTitle,
                    style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
                )
                HorizontalDivider()
            }
            
            // Bottom sheet contents ...
        }
    }
}

// Set keyboard focus onto a newly-opened bottom sheet. 
LaunchedEffect(openBottomSheet) {
    if (openBottomSheet) {
        delay(500)
        focusRequester.requestFocus()
    }
}
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