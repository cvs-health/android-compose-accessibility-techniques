# Stand-alone Links

Stand-alone links (links not embedded in blocks of text) require specific construction in order to be accessible. Specific techniques are required to address each of the follow WCAG 2 success criteria:
* [Success Criterion 2.4.4 Link Purpose (In Context)](https://www.w3.org/TR/WCAG22/#link-purpose-in-context)
* [Success Criterion 1.4.1 Use of Color](https://www.w3.org/TR/WCAG22/#use-of-color)
* [Success Criterion 2.5.8 Target Size (Minimum)](https://www.w3.org/TR/WCAG22/#target-size-minimum)
* [Success Criterion 4.1.2 Name, Role, Value](https://www.w3.org/TR/WCAG22/#name-role-value)

The following techniques are important to make stand-alone links accessible:
* Link text must convey its purpose
* Link text style must be sufficiently distinct from other text
* Links must be large enough to tap easily
* The link role should be communicated as best possible

## Link text must convey its purpose

One of the simplest things necessary is for a stand-alone link's text to convey where that link goes.

A bad example of link text would be:
```kotlin
Text(
    text = "Is this a link?",
    // ...
)
```

An example of link text that clearly indicates the link's purpose would be:
```kotlin
Text(
    text = "Read about Jetpack Compose Accessibility",
    // ...
)
```

This example also illustrates the use of "call to action" wording that is a good practice for stand-alone links. This style of wording convey not only a link's destination, but that the text is, in fact, a link.

## Link text style must be sufficiently distinct from other text

The visual styling of link text must be sufficiently distinct from other text that a user can see that it is a link. And that visual distinction must not just be made using color alone. Font weight and decoration (specifically underlining) are commonly used in addition to color to make link texts distinct from non-link texts.

For example, the following stand-alone link uses color, boldface, and underline text styling to distinguish itself:

```kotlin
Text(
    // ...
    color = MaterialTheme.colorScheme.primary,
    fontWeight = FontWeight.Bold,
    textDecoration = TextDecoration.Underline,
)
```

## Stand-alone links must be large enough to tap easily

As with all interactive components, stand-alone links need a sufficient tap size to be easily selectable. Material Design standards specify a 48dp by 48dp minimum tap target size.

The Material Design 3 extension function `Modifier.minimumInteractiveComponentSize()` makes appropriate link sizing easy. For best effect, apply this sizing before `Modifier.clickable`. (And for Material Design 2 apps, apply `Modifier.sizeIn(48.dp, 48.dp)` instead.)

For example:
```kotlin
Text(
    text = "Read about Jetpack Compose Accessibility",
    modifier = Modifier
        .clickable { uriHandler.openUri(COMPOSE_ACCESSIBILITY_URL) }
        .minimumInteractiveComponentSize()
    // ...
)
```

## The link role should be communicated as best possible

Addressing [Success Criterion 4.1.2 Name, Role, Value](https://www.w3.org/TR/WCAG22/#name-role-value) for stand-alone links is particularly tricky, because unlike the web and iOS, Android has never had a distinct "link" semantic role. That renders most web accessibility discussion and advice about distinguishing links from buttons entirely moot, because the Android platform does not support that distinction in its accessibility API. 

In the Android View framework, it was possible to supply an overriding "roleDescription" which could be used to announce a "link" role in TalkBack; although doing so did not actually supply any different semantics from other text. However, at the time of writing, even that approach is not possible in Jetpack Compose. 

All that said, there are things developers should do to make stand-alone links understandable to all app users. As mentioned above, text wording and styling help indicate that text is a stand-alone link.

A stand-alone link can include an "external link" icon to show that it opens in a browser.

A stand-alone link requires a visible focus indicator. Although, applying `clickable()` to a `Text` adds a default focus indicator, a custom focus indicator can improve focus visibility. 

The TalkBack announcement of the link click action can also be adjusted from "Double-tap to activate" to an announcement that better indicates that clicking opens a link. For example, "Double-tap to open in browser."

The following example includes these techniques (except for a custom focus indicator):

```kotlin
val uriHandler = LocalUriHandler.current
Row(
    modifier = Modifier
        .clickable(
            onClickLabel = "open in browser"
        ) {
            uriHandler.openUri(COMPOSE_ACCESSIBILITY_URL)
        }
        .minimumInteractiveComponentSize(),
    verticalAlignment = Alignment.CenterVertically
) {
    Text(
        text = "Read about Jetpack Compose Accessibility",
        // Use weight() without fill to keep the icon visible when the text wraps at large text size
        modifier = Modifier.weight(1f, fill = false),
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        textDecoration = TextDecoration.Underline
    )
    Spacer(Modifier.width(8.dp))
    Icon(
        painter = painterResource(id = R.drawable.ic_external_link_outline),
        contentDescription = null, // information is already conveyed in onClickLabel
        tint = MaterialTheme.colorScheme.primary
    )
}
```

## Alternative approach using `TextButton`

Alternative approaches to stand-alone links include the use of `Button` or `TextButton` for a stand-alone link. In this case, the "Button" role becomes a proxy for the non-existent "link" role.  

However, `Button` composables do not presently directly support onClickLabel. (And when `Modifier.clickable()` is added to a `Button` to customize the click label in TalkBack, keyboard accessibility is affected, because the `Button` will be focusable twice.) Instead, add a `contentDescription` to the external link icon.

For example:

```kotlin
val uriHandler = LocalUriHandler.current
TextButton(
    onClick = {
        uriHandler.openUri(COMPOSE_ACCESSIBILITY_URL)
    }
) {
    Text(
        text = "Read about Jetpack Compose Accessibility",
        // Use weight() without fill to keep the icon visible when the text wraps at large text size
        modifier = Modifier.weight(1f, fill = false),
        textDecoration = TextDecoration.Underline
    )
    Spacer(Modifier.width(8.dp))
    Icon(
        painter = painterResource(id = R.drawable.ic_external_link_outline),
        // Alternative to setting onClickLabel. Merged with button text into one TalkBack announcement.
        contentDescription = "Opens in browser",
        tint = MaterialTheme.colorScheme.primary
    )
}
```

(Note: The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.)

----

Copyright 2024 CVS Health and/or one of its affiliates

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
[http://www.apache.org/licenses/LICENSE-2.0]()

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.