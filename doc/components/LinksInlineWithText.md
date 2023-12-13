# Links Inline with Text
Always create links which are inline with text that announce their name, role, and value in accordance with the WCAG 2 [Success Criterion 4.1.2 Name, Role, Value](https://www.w3.org/TR/WCAG21/#name-role-value). 

The goal is to make TalkBack announce inline links with the "Links available..." action message and display the TalkBack menu's Links menu. Switch Access should allow the entire text block to be selected, then display a modal menu of links. Inline links should also be focusable and selectable from the keyboard.

Two approaches to creating links inline with text are using native Compose components and using a View interop approach. While using View interop is more keyboard accessible at the time of writing, neither approach works well. 

Given these accessibility shortcomings, prefer using standalone links to links inline with text at this time.

## Native Compose inline links

Native Compose inline links are created using `AnnotatedString` and `UrlAnnotation`. Specifically, use `buildAnnotatedString()` with either `pushUrlAnnotation()` to append a link and its text or `addUrlAnnotation()` to apply an link to a range of existing text.

(Note that `UrlAnnotation` does not apply any styling to the linked text, so `withStyle()` or `addStyle()` must also be used to make link texts visually distinct.)

Native Compose inline links are displayed using `ClickableText` composables. `ClickableText` detects the location of taps within a text and allows that location to perform special click handling. This allows links within a text block to be opened individually.

Within a `ClickableText`'s `onClick` lambda function, translate the selected text position to an URL using `AnnotatedString.getUrlAnnotations()`, and then invoke appropriate code to open the link.

Note: `ClickableText` has been fully enabled for accessibility services, so it works well with TalkBack and Switch Access, and it obeys standard Compose theming. Unfortunately, the same is not true for keyboard accessibility: neither a `ClickableText` nor the links with one are keyboard focusable or selectable. This is a known deficiency; see [Issue 311488543: Support for clickable portions of text to be focusable](https://issuetracker.google.com/issues/311488543). Because `ClickableText` does not conform to WCAG 2 [Success Criterion 2.1.1 Keyboard](https://www.w3.org/TR/WCAG21/#keyboard), its use is problematic; however, it should improve given time. 

For example:

```kotlin
val text = buildAnnotatedString {
    append("Learn how to build simple ")
    pushUrlAnnotation(
        UrlAnnotation("https://developer.android.com/reference/kotlin/androidx/compose/foundation/text/package-summary#ClickableText(androidx.compose.ui.text.AnnotatedString,androidx.compose.ui.Modifier,androidx.compose.ui.text.TextStyle,kotlin.Boolean,androidx.compose.ui.text.style.TextOverflow,kotlin.Int,kotlin.Function1,kotlin.Function1)")
    )
    withStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
        append("ClickableText")
    }
    pop()
    append(" links using ")
    pushUrlAnnotation(
        UrlAnnotation("https://developer.android.com/jetpack/compose/text/user-interactions#click-with-annotation")
    )
    withStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
        append("Click with annotation")
    }
    pop()
    append(".")
}

val uriHandler = LocalUriHandler.current
ClickableText(
    text = text, 
    modifier = Modifier.fillMaxWidth(),
    style = MaterialTheme.typography.bodyMedium.copy(color = LocalContentColor.current)
) { position ->
    text.getUrlAnnotations(position, position).firstOrNull()?.let { annotation ->
        uriHandler.openUri(annotation.item.url)
    }
}
```

Notes:
- The hard-coded text shown in these examples are only used for simplicity. _Always_ use externalized string resource references in actual code. However, URL values may be an exceptions, depending on how the site in question handles internationalization.
- Using `pushUrlAnnotation` does not scale well, and specifically is not particularly internationalizable. (Given that the order of links in a text may change when translated.) One solid approach is to embed link annotations in translation strings and applying reusable parsing code to create the correct `AnnotatedString`.
- `ClickableText` does not support Dark Theme by default. This can be remediated by setting its `style` parameter so that `color = LocalContentColor.current` is merged into its expected styling (as is done with `Text` unless another color is set). 


## Inline links with View interop (`AndroidView`)

The alternative approach to creating inline links is to fall back on the View UI framework's `TextView` inline link handling. This is done by wrapping a `TextView` in an `AndroidView` composable. 

All existing View inline link options are then possible, including HTML anchors in string resources or the use of `URLSpan`. See [Android View Accessibility Techniques - Links Inline with Text](https://github.com/cvs-health/android-view-accessibility-techniques/blob/main/doc/componenttypes/LinksInlineWithText.md) for more details. 

The View interop approach is fully accessible from TalkBack and Switch Access, and it is more accessible from the keyboard than `ClickableSpan`. However, View interop does not follow expected keyboard Tab focus behavior: arrow key navigation is required to focus on the text block and on the link texts within it. This makes it less than ideal from an accessibility standpoint. 

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

Note: HTML linked text will not display as a link in Compose Preview, but does appear as a link when run in an app. Links using `URLSpan` do display correctly as links in Preview.


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