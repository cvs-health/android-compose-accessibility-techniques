# List Semantics
Elements which create a visually-presented list must be marked as a list using the Android Accessibility API. This supports the WCAG [Success Criterion 1.3.1 Info and Relationships](https://www.w3.org/TR/WCAG22/#info-and-relationships), which requires information conveyed through presentation (such as lists) to be programmatically available to accessibility services.

Lists created with the standard lazy list controls, such as `LazyColumn` and `LazyRow`, have list semantics applied automatically.

But if layout composables like `Column` are used to manually create a visual list, then `Modifier.semantics` properties must be used to apply list semantics to those composables manually. The semantics properties involved are `collectionInfo` (which is applied to the enclosing layout composable) and `collectionItemInfo` (which is applied to each list item composable).

For example:

```kotlin
Column(
    modifier = Modifier
        .fillMaxWidth()
        .semantics {
            collectionInfo = CollectionInfo(rowCount = 3, columnCount = 1)
        }
) {
    Row(
        modifier = Modifier.semantics(mergeDescendants = true) {
            collectionItemInfo = CollectionItemInfo(
                rowIndex = 0, rowSpan = 1,
                columnIndex = 0, columnSpan = 1
            )
        }
    ) {
        Text("\u2022 List item 1")
    }
    Row(
        modifier = Modifier.semantics(mergeDescendants = true) { 
            collectionItemInfo = CollectionItemInfo(
                rowIndex = 1, rowSpan = 1,
                columnIndex = 0, columnSpan = 1
            )
        }
    ) {
        Text("\u2022 List item 2")
    }
}
```

The following `Modifier` extension functions make manually applying list semantics more straightforward.

```kotlin
/**
 * Add accessibility collection semantics to a Modifier for a layout composable.
 * Used for manually marking visually-presented lists with list semantics.
 *
 * @param size the number of rows in the list
 */
fun Modifier.addListSemantics(size: Int): Modifier = this.semantics { 
    collectionInfo = CollectionInfo(rowCount = size, columnCount = 1)
}

/**
 * Add accessibility collection item semantics to a Modifier for a layout's child composables.
 * Used for manually marking items within a visually-presented list.
 *
 * Multiple associated composables can share the same index and will be treated semantically as the
 * same list item.
 *
 * Note: index is zero-based.
 *
 * @param index the 0-based list index of this list item
 */
fun Modifier.addListItemSemantics(index: Int): Modifier = this.semantics(mergeDescendants = true) {
    collectionItemInfo = CollectionItemInfo(
        rowIndex = index,
        rowSpan = 1,
        columnIndex = 0,
        columnSpan = 1
    )
}
```

The example above would then become:

```kotlin
Column(
    modifier = Modifier
        .fillMaxWidth()
        .addListSemantics(size = 3)
) {
    Row(modifier = Modifier.addListItemSemantics(index = 0)) {
        Text("\u2022 List item 1")
    }
    Row(modifier = Modifier.addListItemSemantics(index = 1)) {
        Text("\u2022 List item 2")
    }
}
```

TalkBack will then announce "In list" when entering the list and "Out of list" when exiting the list, as well as the item number and list size, e.g., "1 of 3".

Notes:

* One downside of the automatic list semantics applied to `LazyColumn` is that not all uses of lazy layouts create visually-presented lists; it is also used for non-list dynamic content and server-driven user interfaces. In those cases, list semantics is inappropriate. 
* The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.

----

Copyright 2023-2024 CVS Health and/or one of its affiliates

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.