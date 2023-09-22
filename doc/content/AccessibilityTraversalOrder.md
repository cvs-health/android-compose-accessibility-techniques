# Accessibility Traversal Order
Sequence content composables appropriately so they are read by screen readers in an order that preserves the meaning of their visual layout. Incorrectly sequenced content is hard to understand and violates the WCAG 2 [Success Criterion 1.3.2 Meaningful Sequence](https://www.w3.org/TR/WCAG21/#meaningful-sequence).

There are three techniques for influencing the accessibility traversal order (i.e., TalkBack's reading order) of composables:

* Correctly sequencing visual layout in left-to-right, top-to-bottom order (or right-to-left, depending on language direction). If possible, this is the preferred method.
* Set the `Modifier.semantics` property `isTraversalGroup = true` on a layout composable to keep all of its content together as a block. 
    * For example, to create a sidebar layout in which the left-most column is read completely before the right column is read, each `Column` should set `isTraversalGroup=true`. 
* Apply the `Modifier.semantics` property `traversalIndex` to override the default ordering of composable -- but only if necessary.
    * The `traversalIndex` property is a floating-point value, with lower values being read before higher values.
    * It is often necessary to apply the `isTraversalGroup` property to an enclosing layout composable in order to limit the effect of `traversalIndex` to the only the composables that need to be reordered.
      * For example, to create a sidebar layout in which a right sidebar is read before the left main column, the enclosing `Row` should set `isTraversalGroup=true`, the left main `Column` should set `traversalIndex = 1f`, and the right sidebar `Column` should set `traversalIndex = 0f`. 
    * Application of overrides like `traversalIndex` tends to be fragile with regard to change over time. Be aware that such layouts will need maintenance and monitoring.

For example:

```kotlin
Column {
    Row(
        modifier = Modifier.semantics {
            isTraversalGroup = true // Done to constrain the effects of the traveralIndex values below. 
        } 
    ) {
        Column(
            modifier = Modifier
                .weight(0.67f)
                .semantics {
                    // Traverse all of this Column's Texts as a group. 
                    isTraversalGroup = true 
                    // Higher traversalIndex is read later, so this Column is read second.
                    traversalIndex = 1f 
                }
        ) {
            Text("Main column is read second", fontWeight = FontWeight.Bold)
            Text("First main paragraph.")
            Text("Second main paragraph.")
            Text("Third main paragraph.")
        }
        Spacer(Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .weight(0.33f)
                .semantics {
                    // Traverse all of this Column's Texts as a group.
                    isTraversalGroup = true 
                    // Lower traversalIndex is read earlier, so this Column is read first.
                    traversalIndex = 0f 
                }
        ) {
            Text("Sidebar column is read first", fontWeight = FontWeight.Bold)
            Text("First sidebar paragraph.")
            Text("Second sidebar paragraph.")
        }
    }
    // The following Text is read last, because the Row above has set isTraversalGroup = true, and 
    // this Text is below the Row, making it later in the default traversal order.
    Text("Text after both columns.") 
}
```

(Note: The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.)

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