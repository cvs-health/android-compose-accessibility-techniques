# Screen and Pane Titles
All applications screens require titles that communicate the screen's topic or purpose to support the WCAG [Success Criterion 2.4.2 Page Titled](https://www.w3.org/TR/WCAG22/#page-titled).

The screen title should be both visible and announced by screen readers when it is given focus. Screen titles must be descriptive so they communicate the specific screen being displayed. 

To the extent reasonable, screen titles should also be unique. However, this must be balanced against the need for conciseness.

One way to visually display a screen title is using a `TopAppBar` `title`. Note that `TopAppBar` titles have fixed vertical space, so title text display is often limited to a single line. TalkBack will still announce the full title text.

It is also suggested to make screen and pane titles into semantic headings using `Modifier.semantics { heading() }`. 

For example:

```kotlin
import java.lang.reflect.Modifier

Scaffold(
    // ...
    topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Screen Title Example",
                    modifier = Modifier.semantics { heading() },
                    overflow = TextOverflow . Ellipsis,
                    maxLines = 1
                )
            },
            navigationIcon = {
                IconButton(onClick = { /* Handle Back navigation */ }) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back_24dp),
                        contentDescription = "Navigate up"
                    )
                }
            }
        )
    },
    // ...
) {
    // Screen content ...
}
```

One good way to provide screen reader announcement of screen titles is using the `Modifier.semantics` `paneTitle` property at a high layout level, such as a screen's `Scaffold`.

For example:

```kotlin
Scaffold(
    modifier = modifier.semantics {
        paneTitle = "Screen Title Example"
    },
    // ...
) {
    // Screen content ...
}
```

Significant panes within a screen should also be titled. For example, a navigation drawer or the separate panes in a list-detail layout should each have their own `paneTitle`.

(Note: The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.)

----

Copyright 2024-2025  CVS Health and/or one of its affiliates

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.