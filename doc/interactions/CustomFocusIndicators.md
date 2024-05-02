# Custom Focus Indicators
By default, all standard Android control types have focus indicators which display when keyboard focus is on a control. However, the default focus indicators are generally low-contrast, which makes the focussed control very hard to identify for keyboard users with low vision. Applying custom focus indicators is one way to better support those users in accordance with the WCAG [Success Criterion 2.4.7 Focus Visible](https://www.w3.org/TR/WCAG22/#focus-visible) and [Success Criterion 2.4.13 Focus Appearance](https://www.w3.org/TR/WCAG22/#focus-appearance) (Level AAA).

(Note that using the default focus indicator is in technical conformance with WCAG [Success Criterion 2.4.13 Focus Appearance](https://www.w3.org/TR/WCAG22/#focus-appearance) (Level AAA), but it provides a poor user experience for keyboard users.)

There are three major techniques available to customize the focus indicators for controls:

1. Apply `Modifier.border()` with a focus-state-based color
2. Apply `Button`'s `border` property with a focus-state-based color
3. Apply a custom `Indication` using `Modifier.indication()` or `Modifier.clickable()`

## Apply `Modifier.border()` with a focus-state-based color

Setting a composable's `Modifier.border()` property can produce more visible focus indicators.

This technique involves three steps:

1. Remember appropriate border color for the current focus state. (And initially use `Color.Transparent`, since the composable is not yet focused.)
2. Use `Modifier.onFocusChanged()` to track changes in the focus state and update the border color appropriately.
3. Use `Modifier.border()` to apply an appropriate custom border. (Alternatively, `Modifier.background()` could be used.)

For example:

```kotlin
// Remember the focus state color
var borderColor by remember {
    mutableStateOf(Color.Transparent)
}
Text(
    text = "Show terms and conditions",
    modifier = Modifier
        // Track the focus state and set the appropriate color
        .onFocusChanged {
            borderColor = if (it.isFocused) Color.Red else Color.Transparent
        }
        // Apply a border indicator
        .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(4.dp))
        .clickable(role = Role.Button) {
            // Show terms & conditions...
        },
    // Apply clickable text styling ...
)
```

Notes:

- Be sure to make the border color distinguishable in both Light and Dark themes.
- But sure to make the border distinguished by more than hue to avoid a failure of WCAG [Success Criterion 1.4.1 Use of Color](https://www.w3.org/TR/WCAG22/#use-of-color). In the example above, this is done by making the border only appear visible in the focused state. 
- See also [React to focus](https://developer.android.com/jetpack/compose/touch-input/focus/react-to-focus).

This technique can also be encapsulated into a reusable `Modifier` extension function:

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

Such a Modifier extension function simplifies the application of a custom focus border to a composable. For example, the earlier example `Text` would become:

```kotlin
Text(
    text = "Show terms and conditions",
    modifier = Modifier
        .visibleFocusBorder()
        .clickable(role = Role.Button) {
          // Show terms & conditions...
        },
)
```

## Apply `Button`'s `border` property with a focus-state-based color

To apply a custom focus indicator to a `Button` composable, use its `border` property instead of `Modifier.border()`. Otherwise, the same steps apply.

For example:

```kotlin
var borderColor by remember {
    mutableStateOf(Color.Transparent)
}
TextButton(
    onClick = {
        // Show terms and conditions...
    },
    modifier = Modifier
        .onFocusChanged {
            borderColor = if (it.isFocused) Color.Red else Color.Transparent
        },
    // Using the border property will allow the focus indicator to conform to the Button's shape
    border = BorderStroke(
        width = 2.dp,
        color = borderColor
    )
) {
    Text(text = "Show terms and conditions")
}
```

Note: `IconButton` composables can be handled similarly using an `OutlinedIconButton` with a border that is only visible when the composable has focus (as above). 

## Apply a custom `Indication` using `Modifier.indication()` or `Modifier.clickable()`

Jetpack Compose also allows precise, low-level drawing to be performed when state changes using custom `Indicator` and `IndicationInstance` subclasses. This technique involves the following steps:

- Create an `IndicationInstance` subclass that accepts a `State<Boolean>` constructor parameter. This parameter will pass in a `State` holder containing the current focus state. 
  - Override the `ContentDrawScope.drawIndication()` method. 
      - Be certain to call `drawContent()` in this method. 
      - Use `compose.ui.graphics.drawscope` methods to apply an appropriate focus indicator based on the current focus state value. 
- Create an `Indication` subclass.
  - Override the `rememberUpdatedInstance()` method. 
      - Use the method's `interactionSource` parameter to create a focus state holder using `interactionSource.collectIsFocusedAsState()`. 
      - Return a remembered instance of the `IndicationInstance` subclass created using the focus state holder.
- Apply this custom `Indication` subclass to a composable using the `Modifier.indication()` method or by passing it as a parameter to `Modifier.clickable()`.

See also [React to focus - Implement advanced visual cues](https://developer.android.com/jetpack/compose/touch-input/focus/react-to-focus#advanced-visual-cues) and [Handling user interactions](https://developer.android.com/jetpack/compose/touch-input/handling-interactions).

For example:

```kotlin
class CustomFocusIndication : Indication {
    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        // Collect the current focus state in a State holder.
        val isFocusedState = interactionSource.collectIsFocusedAsState()
        return remember(interactionSource) {
            // Return the IndicationInstance that will perform the focus-state-based drawing.
            CustomFocusIndicationInstance(isFocusedState)
        }
    }
}

private class CustomFocusIndicationInstance(
    val isFocusedState: State<Boolean>
) : IndicationInstance {
    override fun ContentDrawScope.drawIndication() {
        drawContent()
        if (isFocused.value) {
            drawRoundRect(
                color = Color.Red,
                size = size,
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
                style = Stroke(width = 2.dp.toPx()),
                alpha = 1.0f
            )
        }
    }
}
```

To layer the custom focus indication on top of the default click ripple and focus background indication, use `Modifier.indication()`:

```kotlin
val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
Text(
    text = "Show terms and conditions",
    modifier = Modifier
        // Apply the custom focus Indication; clickable will still apply the default ripple indication
        .indication(
            interactionSource = interactionSource,
            indication = VisibleFocusIndication()
        )
        .clickable(role = Role.Button) {
            // Show terms & conditions...
        },
    // Apply clickable text styling ...
)
```

To replace the default click ripple and focus background indication with the new custom focus indication, use `Modifier.clickable` with the `interactionSource` and `indication` parameters:

```kotlin
val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
Text(
    text = "Show terms and conditions",
    modifier = Modifier
        // Apply the custom focus Indication, replacing the default ripple indication
        .clickable(
          interactionSource = interactionSource,
          indication = CustomFocusIndication(),
          role = Role.Button
        ) {
            // Show terms & conditions...
        },
    // Apply clickable text styling ...
)
```

Notes: 

- The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.
- The hard-coded colors shown in these examples are only used for simplicity. Color resources or theme-based colors are strongly preferred in actual code. Colors should also respond to Light and Dark theme settings.

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