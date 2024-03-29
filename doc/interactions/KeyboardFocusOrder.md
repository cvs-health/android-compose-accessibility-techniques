# Keyboard Focus Order
Sequence all focusable active control elements appropriately so they are activated by the keyboard in an order that preserves the meaning of their visual layout. Incorrect control focus sequences are hard to operate or understand and violate the WCAG 2 [Success Criterion 2.4.3 Focus Order](https://www.w3.org/TR/WCAG21/#focus-order).

Also, avoid creating focus traps, in accordance with WCAG 2 [Success Criterion 2.1.2 No Keyboard Trap](https://www.w3.org/TR/WCAG21/#no-keyboard-trap).

The same accessibility rules apply to the focus order of active control elements when assistive technologies like Switch Access are used; however, that sequence is controlled by [Accessibility Traversal Order](../content/AccessibilityTraversalOrder.md) techniques. Often these techniques must both be applied to achieve full accessibility.

There are two techniques for influencing keyboard focus order: correct layout grouping and sequencing, and the `Modifier.focusProperties` used in conjunction with `FocusRequester` references.

## Correct layout grouping and sequencing

By default, Jetpack Compose establishes the focus order of active controls by a depth-first traversal of the composition tree. Therefore, when possible, set the active control focus order by correctly sequencing layout elements and controls into properly ordered groups.

For example, to create a two-column control layout in which each column is read completely before the other column is read, an outer `Row` should have two inner `Column` layouts. (If instead the outer layout is a `Column`, with inner two-element `Row` layouts, the elements of the main column and the sidebar column will be mixed together in focus order.)

Also, put the controls in focus order sequence within a layout composable.

See [Focus in Compose](https://developer.android.com/jetpack/compose/touch-input/focus) for more details.

This approach is preferred when possible.

For example:

```kotlin
    // Row and Column take care of keyboard focus order; accessibility focus order requires use of
    // isTraversalGroup to override strict left-to-right traversal order.
    Row {
        Column(
            modifier = Modifier
                .weight(1f)
                .semantics {
                    isTraversalGroup = true
                }
        ) {
            Text("Group 1")
            CheckboxRow(
                text = "Checkbox 1a", 
                // ...
            )
            CheckboxRow(
                text = "Checkbox 1b",
                // ...
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .semantics {
                    isTraversalGroup = true
                }
        ) {
            Text("Group 1")
            CheckboxRow(
                text = "Checkbox 2a",
                // ...
            )
            CheckboxRow(
                text = "Checkbox 2b",
                // ...
            )
        }
    }
```

## Use `Modifier.focusProperties` and `FocusRequester` references to set keyboard focus order

If focus order cannot be correctly sequenced using layout, as can be the case with `ConstraintLayout`, apply the following steps to force keyboard focus order into a specific sequence:

* Create focus references for all relevant controls using `FocusRequester.createRefs()`.
* Assign a focus reference to each relevant control using `Modifier.focusRequester(...)`.
* Set the `Modifier.focusProperties` property `next` to the focus reference of the next element in keyboard focus order. 
  * When applicable, set the property `previous` to the previous element in focus order.
* If required, override the consideration of whether an element is on-screen using `Modifier.focusGroup()`.

Notes:

* This approach will likely also require the use of `isTraversalGroup` and `traversalIndex` for accessibility focus and reading order.
* Care must be taken never to create a focus loop by pointing `next` to the same element or to an element that is earlier in the focus order (or pointing `previous` to the same element or one later in the focus order).
* See [Change focus traversal order](https://developer.android.com/jetpack/compose/touch-input/focus/change-focus-traversal-order) and [Change focus behavior](https://developer.android.com/jetpack/compose/touch-input/focus/change-focus-behavior) for more details.

For example below is code to display two columns of buttons in a `ConstraintLayout`, such that focus order should pass down the first column and then down the second column. 

(`ConstraintLayout` details have been omitted for brevity, but note that constraint references are not interchangeable with focus references: only use a constraint reference in `constrainAs()` and only use a focus reference in `focusRequester()`.)

```kotlin
// Create FocusRequester references for all relevant controls.
val (button1a, button1b, button2a, button2b) = remember { FocusRequester.createRefs() }

ConstraintLayout(
  modifier = Modifier.semantics {
    // Confine the effects of using accessibility traversalIndex semantics to this layout.
    isTraversalGroup = true
  }
) {
  // ...
  Text(
    textId = "Group 1",
    modifier = Modifier // ...
      // Set accessibility traversal order. Includes non-focusable elements. Fragile to change.          
      .semantics { traversalIndex = 1f }
      // This element is not a focusable control, so no focus properties are set.
  )
  Text(
    textId = "Group 2",
    modifier = Modifier // ...
      .semantics { traversalIndex = 4f }
  )
  Button(
    text = "Button 1a", // ...
    modifier = Modifier // ...
      // Set a FocusRequester reference on this control.
      .focusRequester(button1a)
      // Specify the next and/or previous focusable control in focusProperties.
      .focusProperties { next = button1b }
      .semantics { traversalIndex = 2f }
  )
  Button(
    text = "Button 2a", // ...
    modifier = Modifier // ...
      .focusRequester(button2a)
      .focusProperties {
        next = button2b
        previous = button1b
      }
      .semantics { traversalIndex = 5f }
  )
  Button(
    text = "Button 1b", // ...
    modifier = Modifier // ...
      .focusRequester(button1b)
      .focusProperties {
        next = button2a
        previous = button1a
      }
      .semantics { traversalIndex = 3f }
  )
  Button(
    text = "Button 2b", // ...
    modifier = Modifier // ...
      .focusRequester(button2b)
      .focusProperties { previous = button2a }
      .semantics { traversalIndex = 6f }
  )
}
```

Notes: 

* At the time of writing, keyboard focus is only testable for TextFields with jUnit Compose UI tests.
* The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.

----

Copyright 2023 CVS Health and/or one of its affiliates

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
[http://www.apache.org/licenses/LICENSE-2.0]()

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.