# Scrolling Columns
Scrolling `Column` layouts require specific construction in order to be keyboard accessible. Unlike the older `ScrollView` element, Jetpack Compose `Column` elements do not become keyboard focusable when they are made scrollable. Nor do scrollable `Column`s display a keyboard focus indicator even when they are made focusable. 

The following additions are needed to make a scrollable `Column` keyboard accessible:

- Apply `Modifier.focusable()` to allow separate focusing on the `Column`.
    - Being focusable allows the `Page Up` (`PgUp`) and `Page Down` (`PgDn`) keys to scroll the `Column`'s contents. 
    - This technique also provides a separate focus stop for the `Column` itself, as distinct from any of its content items.
- Provide a custom focus indicator for the `Column`. 
    - See [Custom Focus Indicators](CustomFocusIndicators.md) for techniques.

These techniques support WCAG [Success Criterion 2.1.1 Keyboard](https://www.w3.org/TR/WCAG22/#keyboard), [Success Criterion 2.4.3 Focus Order](https://www.w3.org/TR/WCAG22/#focus-order), and [Success Criterion 2.4.7 Focus Visible](https://www.w3.org/TR/WCAG22/#focus-visible).

For example:

```kotlin
// Key technique for scrolling: Declare the ScrollState for later use.
val scrollState = rememberScrollState()
// ...

Column(
  modifier = modifier
    .fillMaxSize()
    // Key technique for scrolling: Make the column scrollable and attach the scrollState.
    .verticalScroll(scrollState)
    // Key technique for accessibility: Place a focus indicator on the scrollable column, similar to 
    // ScrollView's. This is done here with a reusable Modifier extension function (shown below).
    .visibleFocusBorder()
    // Key technique for accessibility: Make the scrollable column keyboard focusable so that the 
    // Page Up and Page Down keys will scroll the Column and so that the Column has its own separate 
    // focus stop. Always apply focusable() *after* the focus indicator, so the Column's focus 
    // indicator is visible.
    .focusable()
    // Technique: Padding is applied here to separate the focus indicator from the Column's content.
    .padding(horizontal = 16.dp)
) {
    // ... Column contents ...
}
```

That example creates a focus indicator by applying the following `Modifier` extension function from [Custom Focus Indicators](CustomFocusIndicators.md):

```kotlin
@Composable
fun Modifier.visibleFocusBorder(): Modifier {
    var borderColor by remember {
        mutableStateOf(Color.Transparent)
    }
    val focusIndicatorColor = colorResource(id = R.color.focus_indicator_outline)
    return this
        .onFocusChanged {
            borderColor = if (it.isFocused) focusIndicatorColor else Color.Transparent
        }
        .border(2.dp, borderColor, shape = RoundedCornerShape(4.dp))
}
```

Note:

* Although the presence of even a single focusable composable within the `Column`'s content would allow the `Column` to receive keyboard events, it would not create a separate focus stop for the `Column`. This would violate WCAG [Success Criterion 2.4.3 Focus Order](https://www.w3.org/TR/WCAG22/#focus-order), as well as being confusing to users since two controls would have focus indicators at the same time.

----

Copyright 2025 CVS Health and/or one of its affiliates

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.