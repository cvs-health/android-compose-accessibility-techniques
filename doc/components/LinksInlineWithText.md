# Links Inline with Text
Always create links which are inline with text that announce their name, role, and value in accordance with the WCAG [Success Criterion 4.1.2 Name, Role, Value](https://www.w3.org/TR/WCAG22/#name-role-value). 

The goal is to make TalkBack announce inline links with the "Links available..." action message and display the TalkBack menu's Links menu. Switch Access should allow the entire text block to be selected, then display a modal menu of links. Inline links should also be focusable and selectable from the keyboard.

Two approaches to creating accessible links inline with text are using native Compose components and using a View interop approach. 

## Native Compose inline links

Native Compose inline links are created using `AnnotatedString` and `LinkAnnotation.Url`. Specifically, use `buildAnnotatedString()` with either `pushLink()` to begin linked text (ended by `pop()`) or `addLink()` to apply an link to a range of existing text. 

Both `pushLink()` and `addLink()` take an instance of the `LinkAnnotation` interface which contains the link information and the link text styling. `LinkAnnotation.Url` is the most appropriate implementation class for inline links that open web pages.

Native Compose inline links are displayed using `Text` or another composable which displays annotated strings.

For example:

```kotlin
val linkStyles = TextLinkStyles(
    style = SpanStyle(
        color  = MaterialTheme.colorScheme.primary, // Handles both Dark Theme and Light Theme
        textDecoration = TextDecoration.Underline
    ),
    focusedStyle = SpanStyle(
        color  = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        textDecoration = TextDecoration.Underline
    ),
    hoveredStyle = SpanStyle(
        color  = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        textDecoration = TextDecoration.Underline
    ),
    pressedStyle = SpanStyle(
        color  = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        textDecoration = TextDecoration.Underline
    )
)

val text = buildAnnotatedString {
    append("Learn how to build simple links in-line with text using ")
    pushLink(
        LinkAnnotation.Url(
            url = "https://developer.android.com/jetpack/compose/text/user-interactions#click-with-annotation",
            styles = linkStyles
        )
    )
    append("Create clickable sections of text with LinkAnnotation")
    pop()
    append(".")
}

Text(
    text = text, 
    modifier = Modifier.fillMaxWidth()
        // Merge the descendants of this Text; otherwise, the link annotations will force the Text 
        // too early in the content traversal (and focus) orders.
        .semantics(mergeDescendants = true) {},
)
```

Notes:

- Unless remediated with `mergeDescendant=true` semantics, `LinkAnnotion`s in `Text` composables receive TalkBack and Switch Access focus priority on entry to a screen (and when TalkBack focus wraps around from the bottom of the screen). That means they get focus before the top `AppBar`. This causes clear violations of WCAG [Success Criterion 1.3.2 Meaningful Sequence](https://www.w3.org/TR/WCAG22/#meaningful-sequence) and [Success Criterion 2.4.3 Focus Order](https://www.w3.org/TR/WCAG22/#focus-order). (See the Google Issues [441137287](https://issuetracker.google.com/issues/441137287), [435583641](https://issuetracker.google.com/issues/435583641), and [434156309](https://issuetracker.google.com/issues/434156309).)
- The hard-coded text shown in these examples are only used for simplicity. _Always_ use externalized string resource references in actual code. However, URL values may be an exceptions, depending on how the website in question handles internationalization.
- `LinkAnnotation.Url` can also take a `LinkInteractionListener` for link click handling. The default listener opens a link in the default browser. 
    - The more generic class `LinkAnnotation.Clickable` is also available for non-URL-based interactions and requires a `LinkInteractionListener`.
- Using `pushLink` does not scale well, and specifically is not particularly internationalizable. (Given that the order of links in a text may change when translated.) One solid approach is to embed link annotations in translation strings and applying reusable parsing code to create the correct `AnnotatedString`.
- The TalkBack Links menu now displays link texts using the `TextLinkStyles` specified in the `AnnotatedString`. This can be a problem if the app does not support both Light theme and Dark theme based on the system setting, because TalkBack does. In the worst case, the link text color and background color can have insufficient contrast for the text to be read.
- Automated testing of inline links is limited, since `Text` only surfaces the literal text of an `AnnotatedString`, not its annotations.

Warning: There was an older Compose approach to in-line links using the `ClickableText` composable. That approach never worked well with keyboards and that composable is now deprecated. Avoid that technique, and if you have older code that uses, convert it to the approach show above as soon as possible; otherwise, you will have trouble conforming to WCAG [Success Criterion 2.2.1 Keyboard](https://www.w3.org/TR/WCAG22/#keyboard), among other issues.


## Inline links with View interop (`AndroidView`)

The alternative approach to creating inline links is to fall back on the View UI framework's `TextView` inline link handling. This is done by wrapping a `TextView` in an `AndroidView` composable. 

All existing View inline link options are then possible, including HTML anchors in string resources or the use of `URLSpan`. See [Android View Accessibility Techniques - Links Inline with Text](https://github.com/cvs-health/android-view-accessibility-techniques/blob/main/doc/componenttypes/LinksInlineWithText.md) for more details. 

The View interop approach is fully accessible but the keyboard handling is slightly unusual: the Tab key will focus on the entire text block and arrow key navigation is required to focus on the individual link texts within it. 

(Note: Elements within an `AndroidView` also do not follow Compose theming: the Material Design Components - Android library must be included and its theming mechanisms used.)

For example, given the following string resource:

```xml
<string name="anchor_tag_string">This string contains an HTML anchor tag link: <a href="https://www.google.com/search?q=jetpack+compose+link+in+text">Search for \"jetpack compose link in text\"</a>.</string>
```

The following Composable code will display this text with its link:

```kotlin
val htmlText: CharSequence = LocalContext.current.getText(R.string.anchor_tag_string)
AndroidView(
    factory = { context ->
        TextView(context).apply {
            this.text = htmlText
            movementMethod = LinkMovementMethod.getInstance()
        }
    }
)
```

Notes: 

- HTML linked text will not display as a link in Compose Preview, but does appear as a link when run in an app. Links using `URLSpan` do display correctly as links in Preview.
- Automated testing of `AndroidView` is limited, since it appears to surface almost no semantic properties at all and its View-based contents are opaque to Compose jUnit UI tests.


----

Copyright 2023-2025 CVS Health and/or one of its affiliates

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.