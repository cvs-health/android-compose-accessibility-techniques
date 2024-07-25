# Minimum Touch Target Size
Touch targets should be at least 48dp by 48dp and _must_ be at least 24dp by 24dp.

These minimum sizes are required to support WCAG [Success Criterion 2.5.8 Target Size (Minimum)](https://www.w3.org/TR/WCAG22/#target-size-minimum) (Level AA), WCAG [Success Criterion 2.5.5 Target Size](https://www.w3.org/TR/WCAG22/#target-size) (Level AAA), and the Material Design Guidelines [Touch targets](https://m2.material.io/design/usability/accessibility.html#layout-and-typography).

There are several techniques for achieving an appropriate visible target size on an interactive composable. 

## Do nothing; clickable components with non-null onClick are automatically sized properly

Compose automatically sizes clickable components to an appropriate minimum size, as long as onClick is non-null. This is the preferred approach when you know the click handler is non-null.

For example:

```kotlin
    OutlinedIconButton(
        onClick = {
            // ... onClick must be non-null for this to work
        }
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add"
        )
    }
```

Where you need to be most aware of this behavior is when `onClick` (or its equivalent) must be set to `null`, because that can cause a composable's size to shrink unexpectedly. 

For example, when correctly implementing a `Checkbox` inside a `Row` to associate it with a `Text` label, the inner `Checkbox` must set `onCheckedChange = null`. That will cause the size of the `Checkbox` to visibly shrink, since its implicit size has been removed, so one of the following techniques must also be applied to make it appear at a normal size. See [Checkbox Controls](../components/CheckboxControls.md) for example code.  

## Use `Modifier.minimumInteractiveComponentSize()` for Material Design 3

In Material Design 3, `Modifier.minimumInteractiveComponentSize()` will force the size of a composable to be at least 48dp by 48dp. This is the preferred approach when the click handler may or may not be null, or when the click handler is known to be null, but sizing should be preserved (as in the `Checkbox` example above). 

For example:

```kotlin
    OutlinedIconButton(
        onClick = nullableOnClickLamba,
        modifier = Modifier.minimumInteractiveComponentSize()
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add"
        )
    }
```

## Use `Modifier.defaultMinSize()` or `Modifier.sizeIn()` for Material Design 2

Since Material Design 2 lacks `Modifier.minimumInteractiveComponentSize()`, use `Modifier.defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)` or `Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)` instead.

For example:

```kotlin
    OutlinedIconButton(
        onClick = nullableOnClickLamba,
        modifier = Modifier.defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add"
        )
    }
```

## Grouping composables that have a common action into a single clickable composable

Touch target size can also be increased by appropriately grouping composables into one clickable composable. One example would be making an entire `Card` clickable instead of its clickable image and its single interior button when both open the same screen. Grouping an avatar image with a person's name into a single clickable `Row` or `Column` within a `Card` could be another example.

# Implicit touch target sizing

Even when the visible touch target size of an interactive composable is smaller than Material Design's minimum size, Jetpack Compose will also automatically apply an _invisible_ touch target size region around such a composable so that its touch target size is at least 48dp by 48dp. 

This implicit touch target sizing can not prevent problems if small interactive composable are crowded together so their touch target regions overlap. So, if interactive elements are not visibly as large as 48dp by 48dp, be sure they are spaced far enough apart to that their touch targets do not overlap.

For example, if two adjacent `IconButton`s each have the size 24dp by 24dp, they should be separated by a `Spacer` of at least 24dp width. 

(Note: The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.)

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
