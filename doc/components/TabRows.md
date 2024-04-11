# Tab rows
Use the Material Design [TabRow](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#TabRow(kotlin.Int,androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,kotlin.Function1,kotlin.Function0,kotlin.Function0)) or [ScrollableTabRow](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#ScrollableTabRow(kotlin.Int,androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.unit.Dp,kotlin.Function1,kotlin.Function0,kotlin.Function0)) and [Tab](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#Tab(kotlin.Boolean,kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Boolean,kotlin.Function0,kotlin.Function0,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.foundation.interaction.MutableInteractionSource)) composables to create a fixed or scrolling set of tabs that announce their name, role, and value in accordance with the WCAG [Success Criterion 4.1.2 Name, Role, Value](https://www.w3.org/TR/WCAG22/#name-role-value) and correctly express their relationships according to WCAG [Success Criterion 1.3.1 Info and Relationships](https://www.w3.org/TR/WCAG22/#info-and-relationships). Custom approaches are likely to be less accessible; make sure any tab control announces the role "Tab" and any tab row has list semantics.

## Fixed tab rows
A `TabRow` composable creates a fixed row of tabs (represented by `Tab` composables) and is followed by its tab pane content composable(s). All tabs in a `TabRow` are displayed and must fit within the screen width.

For example:

```kotlin
var tabIndex by rememberSaveable { mutableStateOf(0) }
val tabTitles = listOf("Tab 1", "Tab 2")
val tabContents = listOf("Contents of tab 1.", "Contents of tab 2.")
Column {
    TabRow(selectedTabIndex = tabIndex) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                selected = tabIndex == index,
                onClick = { tabIndex = index },
                text = { Text(text = title) }
            )
        }
    }
    Text(
        text = tabContents[tabIndex],
        modifier = Modifier.padding(vertical = 8.dp),
        style = MaterialTheme.typography.bodyLarge
    )
}
```

Note: Material Design components version 1.2 will deprecate `TabRow` in favor of `PrimaryTabRow` and `SecondaryTabRow`. These examples are less applicable once that version of the library is in stable release and should be replaced with the more modern controls.

## Scrollable tab rows

The `ScrollableTabRow` composable creates a scrollable tab row: not all tabs on such a row are necessarily visible and the length of the tab labels is less constrained. `ScrollableTabRow` also works in conjunction with the `Tab` composable and is followed by its tab pane content composable(s). 

At present `ScrollableTabRow` use presents two challenges to accessibility:
1. TalkBack has trouble shifting from tab pane content back to the tab row with the right-to-left ("previous") swipe.
2. Switch Access puts early focus on any visible `ScrollableTabRow`, because it is scrollable, even if it is lower on the page than other focusable widgets.

For example:

```kotlin
var tabIndex by rememberSaveable { mutableStateOf(0) }
val tabTitles = listOf(
    "Long Tab 1", 
    "Longer Tab 2", 
    "Even Longer Tab 3", 
    "Still Longer Tab 4", 
    "Long Scrollable Tab 5", 
    "Longest Scrollable Tab 6"
)
Column {
    ScrollableTabRow(selectedTabIndex = tabIndex) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                selected = tabIndex == index,
                onClick = { tabIndex = index },
                text = { Text(text = title) }
            )
        }
    }
    Text(
        text = "Contents of tab $tabIndex.",
        modifier = Modifier.padding(vertical = 8.dp),
        style = MaterialTheme.typography.bodyLarge
    )
}
```

## Tab rows with Pagers as content

The contents of tabs can be collected into a Pager, generally a `HorizontalPager`, that coordinates the displayed content page with the `TabRow` selected `Tab`. This is done by holding the tab selection state in the pager state, created with `rememberPagerState()`, retrieving the selected tab index from the `PagerState.currentPage` property, and setting a new page index by calling `PagerStatepagerState.animateScrollToPage()` in a coroutine context.

At present `ScrollableTabRow` use presents two challenges to accessibility:

1. Switch Access puts early focus on any visible `HorizontalPager`, because it is scrollable, even if it is lower on the page than other focusable widgets.
2. TalkBack announces the content as a "Page" belonging to a "vertical pager," even though the pager is actually horizontal.

Example:

```kotlin
val tabTitles = listOf("Tab 1", "Tab 2")
val tabContents = listOf("Contents of tab 1.", "Contents of tab 2.")
val pagerState = rememberPagerState { tabTitles.size }
val scope = rememberCoroutineScope()
Column {
    TabRow(selectedTabIndex = pagerState.currentPage) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }         
                },
                text = { Text(text = title) }
            )
        }
    }
    HorizontalPager(
        state = pagerState
    ) { page ->
        Text(
            text = tabContents[page],
            modifier = Modifier.padding(vertical = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
```

## Tab selection state management

It is possible to create a wrapper composable that holds tab selection state instead of hoisting it to the parent composable. In this case, always hold tab selection state using `rememberSaveable` or tab selection state will be lost on configuation changes (such as device orientation change).

Only use such a stateful tab group composable if the tab selection state is pure UI state and changes in tab selection do not affect the parent composable's functionality or data.

For example, if an app retrieves different data when a new tab is selected, then a stateful tab widget should not be applied, because it would not inform the parent componsable to perform the data retrieval.  

An example of using such a wrapper composable would be:

```kotlin
StatefulFixedTabGroup(
    tabTitles = listOf("Tab Index 0", "Tab Index 1"),
    initialTabIndex = 1,
) { selectedTabIndex ->
    Text("This is tab $selectedTabIndex.")
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