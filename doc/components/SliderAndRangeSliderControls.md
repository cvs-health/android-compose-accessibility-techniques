# Slider and RangeSlider Controls
`Slider` and `RangeSlider` controls require remediations in order to be accessible. Unfortunately, at this time remediations for the keyboard accessibility of `RangeSlider` are not known, and the option of falling back on a wrapped View `RangeSlider` is blocked by a known keyboard focus issue with Compose/View interoperation.

## Accessible `Slider` controls 
`Slider` controls require specific construction in order to be accessible and conform to WCAG [Success Criterion 1.3.1 Info and Relationships](https://www.w3.org/TR/WCAG22/#info-and-relationships), [Success Criterion 2.5.3 Label in Name](https://www.w3.org/TR/WCAG22/#label-in-name), [Success Criterion 2.1.1 Keyboard](https://www.w3.org/TR/WCAG22/#keyboard), and [Success Criterion 4.1.2 Name, Role, Value](https://www.w3.org/TR/WCAG22/#name-role-value).

The required techniques are:

1. Provide a separate visual control label.
2. Use `Modifier.semantics` `contentDescription` on the `Slider` to provide an accessible label. 
    - And the `contentDescription` must contain the visible label text.
3. (Optional) Provide a `Modifier.semantics` `stateDescription` to provide an appropriate state announcement, if the default announcement of "xx percent" does not apply.
4. Set `Modifier.semantics` `liveRegion = LiveRegionMode.Polite` to announce state changes.
5. Apply `Modifier.onKeyEvent` handling to allow `Slider` value to be changed using the keyboard.

For example:

```kotlin
// Technique: Provide a visible text label for the Slider control
Text("Rating")

val (ratingValue, setRatingValue) = remember { mutableStateOf(0.0f) }
val range = 0f..10f
val steps = 9 // steps between the start and end point (exclusive of both)
val increment = (range.endInclusive - range.start) / (steps + 1)
Slider(
   value = ratingValue,
   onValueChange = setRatingValue,
   modifier = Modifier
      .semantics {
         // Technique: Slider contentDescription must duplicate (or extend) the visible label text,
         // because Slider does not support a text label. (See
         // https://issuetracker.google.com/issues/236988201.)
         contentDescription = "Rating"

         // Optional Technique: stateDescription replaces the default "xx percent" state 
         // announcement for a Slider. (In this case, integer rating values (0-10) are announced.)
         stateDescription = ratingValue.roundToInt().toString()

         // Technique: Set liveRegion to announce the Slider's state when its value changes.
         liveRegion = LiveRegionMode.Polite
      }
      // Technique: Allow the left and right arrow keys to adjust the slider value
      // provided the resulting value is within the slider's range; otherwise, allow
      // normal arrow key navigation to apply.
      .onKeyEvent { keyEvent ->
         when (keyEvent.nativeKeyEvent.keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
               // Absorb both the DPAD_LEFT key DOWN and UP events, because
               // otherwise screen navigation captures key DOWN and the key UP
               // event is never received.
               if (range.contains(ratingValue - increment)) {
                  if (keyEvent.nativeKeyEvent.action == KeyEvent.ACTION_UP) {
                     setRatingValue(ratingValue - increment)
                  }
                  true
               } else {
                  false
               }
            }

            KeyEvent.KEYCODE_DPAD_RIGHT -> {
               if (keyEvent.nativeKeyEvent.action == KeyEvent.ACTION_UP &&
                  range.contains(ratingValue + increment)
               ) {
                  setRatingValue(ratingValue + increment)
                  true
               } else {
                  false
               }
            }

            else -> {
               false
            }
         }
      },
   valueRange = range,
   steps = steps
)
```

## Partially-accessible `RangeSlider` controls
`RangeSlider` controls require specific construction in order to conform to WCAG [Success Criterion 1.3.1 Info and Relationships](https://www.w3.org/TR/WCAG22/#info-and-relationships), [Success Criterion 2.5.3 Label in Name](https://www.w3.org/TR/WCAG22/#label-in-name), and [Success Criterion 4.1.2 Name, Role, Value](https://www.w3.org/TR/WCAG22/#name-role-value).

Unfortunately, no remediation is known at this time that allows the Compose `RangeSlider` control to conform to WCAG [Success Criterion 2.1.1 Keyboard](https://www.w3.org/TR/WCAG22/#keyboard). This is due to all key events being handled by the `RangeSlider` itself, and not passed to its individual range start and end `Thumb` controls when they have keyboard focus.

The required techniques are:

1. Provide a separate visual control label.
2. Use `Modifier.semantics` `contentDescription` on the `Slider` to provide an accessible label.
    - And the `contentDescription` must contain the visible label text.
3. Provide a `Modifier.semantics` `stateDescription` to provide an appropriate state announcement. 
    - Unlike `Slider`, this remediation is required, because `RangeSlider` does not provide a default state announcement.)
4. Set `Modifier.semantics` `liveRegion = LiveRegionMode.Polite` to announce state changes.

For example:

```kotlin
// Technique: Provide a visible text label for the RangeSlider control
Text("Rating filter")

val range = 0f..10f
val steps = 9 // steps between the start and end point (exclusive of both)
val (ratingFilterRange, setRatingFilterRange) = remember { mutableStateOf(range) }
RangeSlider(
   value = ratingFilterRange,
   onValueChange = setRatingFilterRange,
   modifier = Modifier
      .testTag(sliderControlsExample3ControlTestTag)
      .semantics {
         // Technique: RangeSlider contentDescription must duplicate (or extend) label text, because
         // RangeSlider does not support a text label, just as Slider does not.
         contentDescription = "Rating filter"

         // Technique: stateDescription adds the selected range value to a RangeSlider.
         stateDescription = 
            "${ratingFilterRange.start.roundToInt()} to ${ratingFilterRange.endInclusive.roundToInt()}"

         // Technique: Set liveRegion to announce the RangeSlider's state when its value changes.
         liveRegion = LiveRegionMode.Polite
      },
   steps = steps,
   valueRange = range
)
```

## Wrapping a View RangeSlider in AndroidView
An alternative to using the Compose `RangeSlider` control is to wrap a View-based `RangeSlider` control in `AndroidView`. 

Unfortunately, this approach does not improve keyboard accessibility at this time, because of a known Compose/View interopability issue. See [Issue 255628260: While navigating elements using external bluetooth keyboard, only compose elements gets highlighted where as Compose elements internally using AndroidView never responds](https://issuetracker.google.com/issues/255628260) for details.

For example:

```kotlin
 // Technique: Provide a visible text label for the RangeSlider control
Text("Rating filter")

val range = 0f..10f
 val (ratingFilterRange, setRatingFilterRange) = remember { mutableStateOf(range) }
 // Technique: Wrap a com.google.android.material.slider.RangeSlider in AndroidView.
 // Note: This approach is not keyboard accessible, because of a known Compose-View interop issue.
 AndroidView(
     factory = { context ->
         RangeSlider(context).apply {
             valueFrom = range.start
             valueTo = range.endInclusive
             stepSize = 1.0f
             values = listOf(range.start, range.endInclusive) // sets the initially selected range

             // Technique: Label RangeSlider using contentDescription
             contentDescription = "Rating filter"

             // Technique: Extract the value of the RangeSlider to MutableState as it changes
             addOnChangeListener { slider: RangeSlider, _: Float, fromUser: Boolean ->
                 if (fromUser) {
                     setRatingFilterRange(slider.values.first() .. slider.values.last())
                 }
             }
         }
     }
 )

```

(Note: The hard-coded text and the string interpolation shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.)

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
