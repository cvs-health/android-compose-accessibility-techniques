# ListItem Layouts
`ListItem` layout composables improve accessibility in their default use case by applying `Modifier.semantics(mergeDescendants=true)` on an inner `Row`. This `Modifier` tweak unifies all text across the ListItem during TalkBack announcement. However, those merge semantics create problems if the ListItem is made actionable with `Modifier.clickable()`, `Modifier.selectable()`, or `Modifier.toggleable()`.

If a `ListItem` is made `clickable`, the item becomes clickable, but is not announced with either the click action or the Button role in TalkBack. This violates WCAG 2 [Success Criterion 4.1.2 Name, Role, Value](https://www.w3.org/TR/WCAG21/#name-role-value).

If a `ListItem` is make `selectable` or `toggleable`, TalkBack presents the `ListItem` in two parts: as an unlabeled, selectable `RadioButton` or toggleable `Switch`, and as a separate inner text block. Double-tapping at either point activates the action, but only activating the outer wrapper will announce any state change. This separation of control and label violates WCAG 2 [Success Criterion 1.3.1 Info and Relationships](https://www.w3.org/TR/WCAG21/#info-and-relationships).

Actionable `ListItem` layout composables can be made accessible by using `Modifier.clearAndSetSemantics` to set a `contentDescription` for the `ListItem` as a whole. This fix is fragile, because the `contentDescription` can not use text from the composables passed into the `ListItem`, but will have to be maintained manually. But the other alternative is duplicating all of the `ListItem` code locally and removing the `mergeDescendants` semantics from the inner `Row`.

## Example accessible, toggleable `ListItem` layout
The following example code shows an accessible, toggleable `ListItem` for a "Dark theme" setting:

```kotlin
val (useDarkTheme, setUseDarkThem) = remember { mutableStateOf(false) }
ListItem(
    headlineContent = {
        Text("Dark theme")
    },
    modifier = Modifier
        .toggleable(
            value = useDarkTheme,
            role = Role.Switch,
            onValueChange = setUseDarkThem
        )
        // Key remediation: Override all ListItem semantics, except toggleable() above,
        // which is applied as a wrapper over the clearAndSetSemantics call.
        .clearAndSetSemantics {
            contentDescription = "Dark theme. Forces light text on dark background."
        },
    supportingContent = {
        Text("Forces light text on dark background.")
    },
    trailingContent = {
        Switch(checked = useDarkTheme, onCheckedChange = null)
    }
)
```

