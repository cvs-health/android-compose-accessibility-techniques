/*
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
 */
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.inline_links

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.ProblematicExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val inlineLinksHeadingTestTag = "inlineLinksHeading"
const val inlineLinksExample1HeadingTestTag = "inlineLinksExample1Heading"
const val inlineLinksExample1TextWithLinksTestTag = "inlineLinksExample1TextWithLinks"
const val inlineLinksExample2HeadingTestTag = "inlineLinksExample2Heading"
const val inlineLinksExample2TextWithLinksTestTag = "inlineLinksExample2TextWithLinks"

/**
 * Demonstrate accessibility techniques for links inline with text.
 *
 * Applies [GenericScaffold] to wrap the screen content. Opens links in an external browser.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@Composable
fun InlineLinksScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.inline_links_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.inline_links_heading),
                modifier = Modifier.testTag(inlineLinksHeadingTestTag)
            )
            BodyText(textId = R.string.inline_links_description_1)
            BodyText(textId = R.string.inline_links_description_2)

            ProblematicExample1()
            ProblematicExample2()

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        InlineLinksScreen {}
    }
}

@Composable
@OptIn(ExperimentalTextApi::class)
private fun ProblematicExample1() {
    // Problematic example 1: Using AnnotatedString and ClickableText
    val linkMap = mapOf(
        stringResource(id = R.string.inline_links_example_1_link_text_1) to
                "https://developer.android.com/jetpack/compose/text/user-interactions#click-with-annotation",
        stringResource(id = R.string.inline_links_example_1_link_text_2) to
                "https://developer.android.com/reference/kotlin/androidx/compose/ui/text/AnnotatedString",
        stringResource(id = R.string.inline_links_example_1_link_text_3) to
                "https://developer.android.com/reference/kotlin/androidx/compose/foundation/text/package-summary#ClickableText(androidx.compose.ui.text.AnnotatedString,androidx.compose.ui.Modifier,androidx.compose.ui.text.TextStyle,kotlin.Boolean,androidx.compose.ui.text.style.TextOverflow,kotlin.Int,kotlin.Function1,kotlin.Function1)",
    )
    ProblematicExampleHeading(
        text = stringResource(id = R.string.inline_links_example_1),
        modifier = Modifier.testTag(inlineLinksExample1HeadingTestTag)
    )

    // Key technique 1: Use buildAnnotatedString and addUrlAnnotation to mark the parts of the
    // display text that are links. Uses a map of link text strings to URL values, so
    // link texts must be unique. Note that addUrlAnnotation does not style the link text as
    // a link, so use addStyle to do so.
    val annotatedText = buildAnnotatedString {
        val baseText = stringResource(id = R.string.inline_links_example_1_text)
        append(baseText)
        for ((linkText, url) in linkMap) {
            val start = baseText.indexOf(linkText)
            val end = start + linkText.length
            addStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                ),
                start = start,
                end = end
            )
            addUrlAnnotation(
                urlAnnotation = UrlAnnotation(url),
                start = start,
                end = end
            )
        }
    }

    // Key technique 2: Use ClickableText to display the AnnotatedString and intercept
    // interactions with the link texts.
    //
    // Note: ClickableText does not make links keyboard focusable or actionable.
    // See https://issuetracker.google.com/issues/311488543.
    val uriHandler = LocalUriHandler.current
    ClickableText(
        text = annotatedText,
        modifier = Modifier
            .testTag(inlineLinksExample1TextWithLinksTestTag)
            .fillMaxWidth()
            .padding(top = 8.dp),
        style = MaterialTheme.typography.bodyMedium.copy(color = LocalContentColor.current)
    ) { position ->
        // Key technique 3: Use AnnotatedString.getUrlAnnotations() to map from a string
        // tap position to any intersecting UrlAnnotation. Then retrieve the URL string from
        // the annotation's item.url property and open that URL.
        annotatedText.getUrlAnnotations(position, position).firstOrNull()?.let { annotation ->
            uriHandler.openUri(annotation.item.url)
        }
    }

    BodyText(textId = R.string.inline_links_example_1_note)
}

@Preview(showBackground = true)
@Composable
private fun ProblematicExample1Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            ProblematicExample1()
        }
    }
}

@Composable
private fun ProblematicExample2() {
    // Problematic example 2: Using AndroidView and TextView
    ProblematicExampleHeading(
        text = stringResource(id = R.string.inline_links_example_2),
        modifier = Modifier.testTag(inlineLinksExample2HeadingTestTag)
    )

    val htmlText: CharSequence = LocalContext.current.getText(R.string.inline_links_example_2_text)
    AndroidView(
        modifier = Modifier
            .testTag(inlineLinksExample2TextWithLinksTestTag)
            .padding(top = 8.dp),
        factory = { context ->
            TextView(context).apply {
                this.text = htmlText // spannableString
                movementMethod = LinkMovementMethod.getInstance()
                setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyMedium)
            }
        }
    )

    BodyText(textId = R.string.inline_links_example_2_note)
}

@Preview(showBackground = true)
@Composable
private fun ProblematicExample2Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            ProblematicExample2()
        }
    }
}