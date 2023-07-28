# Text Alternatives
All informative non-text content must have a text alternative associated with it, generally via the `contentDescription` property. Providing text alternatives is required by the WCAG 2 [Success Criterion 1.1.1 Non-Text Content](https://www.w3.org/TR/WCAG21/#non-text-content).

## Informative non-text content

Examples of informative non-text content include `Icon` composables that convey information and `IconButton` composable content without text. Use a non-empty `contentDescription` parameter value to provide a text alternative to an `Icon`.

```
Icon(
    painter = painterResource(id = R.drawable.ic_new_product),
    contentDescription = "New Product!"
)
```

```
IconButton(
    onClick = { ... },
    modifier = Modifier.minimumInteractiveComponentSize()
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_share_fill),
        contentDescription = "Share"
    )
}
```

## Decorative non-text content

Purely decorative non-text content that conveys no meaning should be marked as such with a null `contentDescription`.

```
Icon(
    painter = painterResource(id = R.drawable.ic_sprout_fill),
    contentDescription = null
)
```

If necessary, it is possible to mark a composable with `invisibleToUser()` semantics to achieve the same effect, but that is not recommended in simple cases such as decorative `Icon`s.

```
// Avoid applying this technique in this circumstance; prefer contentDescription = null.
Icon(
    painter = painterResource(id = R.drawable.ic_sprout_fill),
    contentDescription = "Sprout",
    modifier = Modifier.semantics { invisibleToUser() }
)
```

## Avoid redundancy in grouped content

Informative non-text content that is redundant with adjacent text content should be grouped with that text content, rather than given a redundant `contentDescription`. Using `Modifier.semantics(mergeDescendants = true) { }` on the enclosing group layout (and nulling out the non-text content's `contentDescription`) is the simplest way to achieve this.

```
/* Good example of grouping content. 
   The green check mark icon is redundant with the text "Good example ...", but is not redundantly 
   announced in the TalkBack screen reader, because: 
   Enclosing Row has semantics( mergeDescendants=true ) and 
   check mark Icon has null contentDescription. */
Row(
    modifier = modifier
        .fillMaxWidth()
        .semantics(mergeDescendants = true) { }
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_check_fill,
        contentDescription = null,
        modifier = ...,
        tint = Color(0xFF009900) // success green
    )
    Text("Good example of grouping redundant content",
        ...
    )
}
```

## Overriding grouped content with a single description

Another alternative for grouping images and text content is to set a `contentDescription` that overrides all of the semantic text of a layout group's child composables.

However, if a layout composable supplies a `contentDescription` and sets `mergeDescendants=true`, both the layout's `contentDescription` and any semantic text in the layout's child composables will be announced. There are two approaches to that issue.

### Overriding content with `invisibleToUser()`

One way to avoid announcing both the layout-level and child-level text alternatives is to make any child composable that would announce text `invisibleToUser()`:

```kotlin
Row(
    modifier = modifier.semantics(mergeDescendants = true) {
        contentDescription = "Sunrise at 6:35am" 
    },
    verticalAlignment = Alignment.CenterVertically
) {
    Text(
        textId = "6:35am",
        modifier = Modifier
            .padding(end = 8.dp)
            .semantics { invisibleToUser() }
    )
    Icon(
        painter = painterResource(id = R.drawable_ic_sunrise),
        contentDescription = null
    )
}
```

The issue with this approach is fragility: any change to the child composables may introduce additional (and unexpected) announced text. Automated regression testing of the layout's `contentDescription` on the merged accessibility tree is one approach to this challenge.

### Overriding content with `Modifier.clearAndSetSemantics`

The other approach of overriding grouped composable content is to use `Modifier.clearAndSetSemantics`. This method removes the existing semantics of the element _and all of its child composables_ and replaces it with the semantic values supplied by a lambda. 

```kotlin
Row(
    modifier = modifier.clearAndSetSemantics {
        contentDescription = "Sunrise at 6:35am" 
    },
    verticalAlignment = Alignment.CenterVertically
) {
    Text(
        textId = "6:35am",
        modifier = Modifier.padding(end = 8.dp)
    )
    Icon(
        painter = painterResource(id = R.drawable_ic_sunrise),
        contentDescription = null
    )
}
```

Pruning the accessibility semantics tree so heavily is almost never appropriate with simple image and text combinations. Its downside in more complex situations is that important semantic information from child composables (or their children) can be omitted. 

However, sometime using `clearAndSetSemantics` is the right approach, as is illustrated in the [Content Group Replacement](./ContentGroupReplacement.md) sample.

## Describing "decorative" images

Although you should never put a non-null `contentDescription` on a purely decorative image, sometimes there are arguments that an apparently decorative image provides tone or emotional content to a screen that should be described for screen reader users. This is a judgement call for the app's designer.

## Handling complex images

Complex images, such as charts and graphs, should have a concise `contentDescription` covering their purpose, but their details also need to be fully presented in the app's text nearby. (If necessary, this text may be on a separate screen that can be easily navigated to using a control near the image). The accessibility of such data visualizations is a separate (and deep) topic.

(Note: The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.)

----

Copyright 2023 CVS Health and/or one of its affiliates

