# Dropdown Menus

Dropdown menus allow selection of an action from a pop-up list of available options. By default, the stock [DropdownMenu](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#DropdownMenu(kotlin.Boolean,kotlin.Function0,androidx.compose.ui.Modifier,androidx.compose.ui.unit.DpOffset,androidx.compose.foundation.ScrollState,androidx.compose.ui.window.PopupProperties,androidx.compose.ui.graphics.Shape,androidx.compose.ui.graphics.Color,androidx.compose.ui.unit.Dp,androidx.compose.ui.unit.Dp,androidx.compose.foundation.BorderStroke,kotlin.Function1)) composable is mostly accessible, but can be improved to better meet WCAG [Success Criterion 2.2.1 Keyboard](https://www.w3.org/TR/WCAG22/#keyboard) and especially [Success Criterion 2.2.2 No Keyboard Trap](https://www.w3.org/TR/WCAG22/#no-keyboard-trap).

The specific improvements suggested are:

* Provide a "Close" menu item.
* Automatically set keyboard focus to the dropdown menu when opened.
* Enable the keyboard escape key ("Esc") to close a menu.

For example:

```kotlin
// Technique 1: Remember if the menu is expanded or not.
var isMenuExpanded by remember { mutableStateOf(false) }

Box(
    modifier = Modifier.wrapContentSize(Alignment.TopStart)
) {
    // Technique 2a: Automatically move focus to the dropdown menu on open. First, create a focus 
    // requester.
    val focusRequester = remember { FocusRequester() }
    
    // Present the menu launch button; the menu will appear above, below, or covering this icon, 
    // depending on its screen position and the menu's size. (Note: This button should also have a 
    // clearly visible focus indicator not illustrated here.)
    IconButton(
        onClick = { isMenuExpanded = true }
    ) {
        Icon(
            painter = painterResource(R.drawable.more_vert_24dp),
            contentDescription = "Show menu"
        )
    }
    
    // Compose the Dropdown menu, which only composes its content if expanded.
    DropdownMenu(
        expanded = isMenuExpanded,
        onDismissRequest = { isMenuExpanded = false },
        modifier = Modifier
            // Technique 2b: Set the focus requester on the Dropdown menu.
            .focusRequester(focusRequester)
            
            // Technique 3: Allow the Esc key to dismiss the dropdown menu. This code requires that
            // the menu have focus, so it is partially predicated on Technique 2, but not entirely.
            .onKeyEvent { keyEvent ->
                if (keyEvent.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ESCAPE) {
                    isMenuExpanded = false
                    true
                } else {
                    false
                }
            }
    ) {
        // The dropdown menu's content...
        DropdownMenuItem(
            text = { Text("Menu item 1") },
            onClick = {
                isMenuExpanded = false
                // ... action to take when this menu item is selected ...
            }
        )
        // ... other menu items ...
        
        // Technique 4: Add a "Close" menu item. Offset with HorizontalDivider and uses a trailing
        // Icon for easier recognition. Different presentations of a close control work too.
        HorizontalDivider()
        DropdownMenuItem(
            text = { Text("Close") },
            onClick = {
                isMenuExpanded = false
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.close_24dp),
                    contentDescription = null // Icon information is redundant with text, so omit.
                )
            }
        )
    }

    // Key technique 2c: When the menu opens, pause, then request focus on the menu.
    LaunchedEffect(isMenuExpanded) {
        if (isMenuExpanded) {
            delay(500)
            focusRequester.requestFocus()
        }
    }
}
```

(Note: The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.)

----

Copyright 2024-2025 CVS Health and/or one of its affiliates

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.