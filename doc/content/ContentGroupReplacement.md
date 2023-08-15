# Content Group Replacement
Sometimes content groups should be replaced with a single text that better describes the group for a screen reader user. Applying the `Modifier.semantics` `contentDescription` property to the enclosing layout composable adds a group text alternative. To fully replace all of the existing group accessibility text, the enclosed content is either rendered `invisibleToUser()` or its semantics is completely overridden by `Modifier.clearAndSetSemantics {}`.

## Replacing group content with `invisibleToUser()`

Children of a composable can remove themselves from the accessibility tree using the experimental `Modifier.semantics { invisibleToUser() }` property. (Note that this may not remove their own child content.)

The challenge of applying `invisibleToUser()` is that all applicable composables must apply it over time or the parent content description will be read in addition to its (unmarked) children. Automated testing can help catch such regression errors.

```kotlin
val rating = 3.4f
val maxRating = 5
Row(
    modifier = Modifier.semantics(mergeDescendants = true) { 
        contentDescription = "Rating: ${rating} out of ${maxRating} stars" // "Rating: 3.4 out of 5 stars"
    },
    verticalAlignment = Alignment.CenterVertically
) {
    Text(
        text = "Rating:",
        modifier = Modifier.semantics { invisibleToUser() }
    )
    LinearProgressIndicator(
        progress = rating / maxRating,
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .semantics { invisibleToUser() }
    )
    Text(
        text = "${rating} / ${maxRating}", // "3.4 / 5"
        modifier = Modifier.semantics { invisibleToUser() }
    )
}
```

## Replacing group content with `Modifier.clearAndSetSemantics`

`Modifier.clearAndSetSemantics` completely replaces all of the semantics of a composable, and the semantics of _all of its child composables_, with the semantics declared in its `properties` lambda. This modifier prunes entire limbs off the accessibility node tree.

That makes it a simple technique for completely replacing a layout's complex child content with a single text alternative, when you are sure that the child composables will never contribute any meaningful semantic value to the ensemble. 

`Modifier.clearAndSetSemantics` can also be an appropriate approach to replacing interactive child composables with custom accessibility actions. 

It is not the technique to use when child content should control its own interactions with assistive technologies. Therefore, `Modifier.clearAndSetSemantics` is best applied nearer to the leaf nodes of the composition tree, rather than near its root.   

```kotlin
val rating = 3.4f
val maxRating = 5
Row(
    modifier = Modifier.clearAndSetSemantics { 
        contentDescription = "Rating: ${rating} out of ${maxRating} stars" // "Rating: 3.4 out of 5 stars"
    },
    verticalAlignment = Alignment.CenterVertically
) {
    Text(text = "Rating:")
    LinearProgressIndicator(
        progress = rating / maxRating,
        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
    )
    Text(text = "${rating} / ${maxRating}") // "3.4 / 5"
}
```

(Note: The hard-coded text, string substitution, and fixed data values shown in these examples are only used for simplicity. _Always_ use externalized string resource references, string resource parameters, and dynamic data providers in actual code.)

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
