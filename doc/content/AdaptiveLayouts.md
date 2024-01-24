# Adaptive Layouts
Support both portrait and landscape orientations in accordance with the WCAG 2 [Success Criterion 1.3.4 Orientation](https://www.w3.org/TR/WCAG21/#orientation) and improve the user experience for different display sizes using the following techniques.

In AndroidManifest.xml, avoid setting any `<activity/>` element's `android:screenOrientation` to a value that fixes the orientation. In other words, avoid `portrait`, `landscape`, etc., in favor of `unspecified` (the default), `sensor`, `fullSensor`, `user`, or `fullUser` orientations. (See the [android:screenOrientation](https://developer.android.com/guide/topics/manifest/activity-element.html#screen) documentation for more details.)

Implement the outermost `Column` of a screen layout with `Modifier.verticalScroll()` so the screen can be displayed in any display space without loss of content. Alternatively, use a scrolling list such as `LazyColumn` instead of an outer `Column`.

When appropriate, provide alternative layouts for different display sizes and orientations using the [Window size classes](https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes#window_size_classes) and `WindowSizeClass`. (See [Build adaptive layouts](https://developer.android.com/jetpack/compose/layouts/adaptive) for more details.)

For example, in the controlling `Activity`, determine the current `WindowSizeClass` and pass it (or a summary proxy value) down to the child composables:

```kotlin
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
        val windowSizeClass = calculateWindowSizeClass(this)
        // ...
        MyComposable(windowSizeClass)
    }
}
```

Use `WindowSizeClass` in a composable as follows:

```kotlin
@Composable
fun MyComposable(
    windowSizeClass: WindowSizeClass
) {
    // ...

    // Technique 1a: Ensure all screen content is scrollable.
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .verticalScroll(scrollState) // Technique 1b: Ensure all screen content is scrollable.
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        // Technique 2: Adapt layout based on WindowSizeClass.widthSizeClass. (Can be combined with 
        // WindowSizeClass.heightSizeClass when appropriate.)
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                // Narrow-width layout, e.g. phone portrait mode ...
            }

            WindowWidthSizeClass.Medium -> {
                // Medium-width layout, e.g. phone landscape mode ...
            }

            WindowWidthSizeClass.Expanded -> {
                // Large-width layout, e.g. tablet displays ...
            }
        }
    }
}

```

To Preview adaptive layouts based on `WindowSizeClass`, set the `@Preview` `widthDp` and `heightDp` parameters, and create an appropriate `WindowSizeClass` to pass into the composable being previewed.

For example:

```kotlin
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
// Preview technique: To preview layouts, set their size with widthDp and heightDp.
// The following widthDp sets a Medium width.
@Preview(showBackground = true, widthDp = 600, heightDp = 900)
@Composable
fun AdaptiveLayoutsScreenMediumPreview() {
    ComposeAccessibilityTechniquesTheme() {
        AdaptiveLayoutsScreen(
            // Preview technique: Create a WindowSizeClass matching this preview's window.
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(width=600.dp, height=900.dp))
        )
    }
}
```

While it is possible to create alternative layouts using `LocalConfiguration.current.orientation`, that technique is fragile, particularly on tablet devices, foldables, and in multi-window modes. That said, orientation configuration may be useful for minor layout adjustments, such as changing padding.

Material Design 3 also provides two (currently experimental) adaptive layout composables: [ListDetailPaneScaffold](https://developer.android.com/jetpack/compose/layouts/list-detail) and [NavigationSuiteScaffold](https://developer.android.com/reference/kotlin/androidx/compose/material3/adaptive/navigation/suite/package-summary#NavigationSuiteScaffold(kotlin.Function1,androidx.compose.ui.Modifier,androidx.compose.material3.adaptive.navigation.suite.NavigationSuiteType,androidx.compose.material3.adaptive.navigation.suite.NavigationSuiteColors,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,kotlin.Function0)).

Be sure to preserve application state across configurations changes (such as orientation change) using `rememberSaveable()`, a `ViewModel`, or other state management techniques. See [Handle configuration changes](https://developer.android.com/guide/topics/resources/runtime-changes) for more details.

Always test layouts on a variety of devices, orientations, and display modes.

----

Copyright 2024 CVS Health and/or one of its affiliates

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
[http://www.apache.org/licenses/LICENSE-2.0]()

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.