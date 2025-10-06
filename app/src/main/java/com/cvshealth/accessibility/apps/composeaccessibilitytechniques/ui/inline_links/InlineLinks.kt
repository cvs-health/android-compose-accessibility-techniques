/*
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
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

            GoodExample1()
            GoodExample2()

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
private fun GoodExample1() {
    // Good example 1: Using AnnotatedString and LinkAnnotation.Url
    val linkMap = mapOf(
        stringResource(id = R.string.inline_links_example_1_link_text_1) to
                "https://developer.android.com/develop/ui/compose/text/user-interactions#create-clickable-text",
        stringResource(id = R.string.inline_links_example_1_link_text_2) to
                "https://developer.android.com/reference/kotlin/androidx/compose/ui/text/AnnotatedString",
        stringResource(id = R.string.inline_links_example_1_link_text_3) to
                "https://developer.android.com/reference/kotlin/androidx/compose/ui/text/LinkAnnotation.Url",
    )
    GoodExampleHeading(
        text = stringResource(id = R.string.inline_links_example_1),
        modifier = Modifier.testTag(inlineLinksExample1HeadingTestTag)
    )

    // Key technique: Use buildAnnotatedString, addLink, and LinkAnnotation.Url to mark the parts
    // of the display text that are links. Uses a map of link text strings to URL values, so link
    // texts must be unique. Note that LinkAnnotation.Url(...) allows styling the link text.
    val annotatedText = buildAnnotatedString {
        val baseText = stringResource(id = R.string.inline_links_example_1_text)
        append(baseText)
        for ((linkText, url) in linkMap) {
            val start = baseText.indexOf(linkText)
            val end = start + linkText.length
            addLink(
                LinkAnnotation.Url(
                    url = url,
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color  = MaterialTheme.colorScheme.primary,
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
                    ),
                    // A LinkInteractionListener can also be specified for link click handling.
                ),
                start,
                end
            )
        }
    }

    // Technique: Use Text to display the AnnotatedString.
    Text(
        text = annotatedText,
        modifier = Modifier
            .testTag(inlineLinksExample1TextWithLinksTestTag)
            .fillMaxWidth()
            .padding(top = 8.dp)
            // Key Technique: Merge the descendants of this Text; otherwise, the link annotations
            // will force the Text too early in the content traversal (and focus) orders.
            .semantics(mergeDescendants = true) {},
        style = MaterialTheme.typography.bodyMedium.copy(color = LocalContentColor.current)
    )

    BodyText(textId = R.string.inline_links_example_1_note)
}

@Preview(showBackground = true)
@Composable
private fun GoodExample1Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample1()
        }
    }
}

@Composable
private fun GoodExample2() {
    // Good example 2: Using AndroidView and TextView
    GoodExampleHeading(
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
}

@Preview(showBackground = true)
@Composable
private fun GoodExample2Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample2()
        }
    }
}