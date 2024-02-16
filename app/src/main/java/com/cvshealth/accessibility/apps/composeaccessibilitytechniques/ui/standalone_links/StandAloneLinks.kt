/*
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
 */
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.standalone_links

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BadExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericStandAloneLink
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.VisibleFocusBorderTextButton
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val standaloneLinksHeadingTestTag = "standaloneLinksHeading"
const val standaloneLinksExample1HeadingTestTag = "standaloneLinksExample1Heading"
const val standaloneLinksExample1LinkTextTestTag = "standaloneLinksExample1LinkText"
const val standaloneLinksExample2HeadingTestTag = "standaloneLinksExample2Heading"
const val standaloneLinksExample2LinkTextTestTag = "standaloneLinksExample2LinkText"
const val standaloneLinksExample3HeadingTestTag = "standaloneLinksExample3Heading"
const val standaloneLinksExample3LinkTextTestTag = "standaloneLinksExample3LinkText"
const val standaloneLinksExample4HeadingTestTag = "standaloneLinksExample4Heading"
const val standaloneLinksExample4LinkButtonTestTag = "standaloneLinksExample4LinkButton"

private const val COMPOSE_ACCESSIBILITY_URL = "https://developer.android.com/jetpack/compose/accessibility"

@Composable
fun StandAloneLinksScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.standalone_links_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.standalone_links_heading),
                modifier = Modifier.testTag(standaloneLinksHeadingTestTag)
            )
            BodyText(textId = R.string.standalone_links_description_1)
            BodyText(textId = R.string.standalone_links_description_2)

            BadExample1()
            GoodExample2()
            GoodExample3()
            GoodExample4()
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        StandAloneLinksScreen {}
    }
}

@Composable
private fun BadExample1() {
    // Bad example 1: Indistinguishable text link
    val uriHandler = LocalUriHandler.current
    val goToComposeAccessibility = {
        uriHandler.openUri(uri = COMPOSE_ACCESSIBILITY_URL)
    }

    BadExampleHeading(
        text = stringResource(id = R.string.standalone_links_example_1_header),
        modifier = Modifier.testTag(standaloneLinksExample1HeadingTestTag)
    )
    Text(
        text = stringResource(id = R.string.standalone_links_example_1_text),
        modifier = Modifier
            .testTag(standaloneLinksExample1LinkTextTestTag)
            .padding(top = 8.dp)
            .clickable(
                onClick = goToComposeAccessibility
            ),
        style = MaterialTheme.typography.bodyMedium
    )
}

@Preview(showBackground = true)
@Composable
fun BadExample1Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            BadExample1()
        }
    }
}

@Composable
private fun GoodExample2() {
    // Good example 2: Accessible Stand-alone link text
    val uriHandler = LocalUriHandler.current

    GoodExampleHeading(
        text = stringResource(id = R.string.standalone_links_example_2_header),
        modifier = Modifier.testTag(standaloneLinksExample2HeadingTestTag)
    )

    GenericStandAloneLink(
        text = stringResource(id = R.string.standalone_links_example_2_text),
        modifier = Modifier.testTag(standaloneLinksExample2LinkTextTestTag),
        showExternalLinkIcon = false
    ) {
        uriHandler.openUri(COMPOSE_ACCESSIBILITY_URL)
    }
}

@Preview(showBackground = true)
@Composable
fun GoodExample2Preview() {
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

@Composable
private fun GoodExample3() {
    // Good example 3: Accessible stand-alone link and icon
    val uriHandler = LocalUriHandler.current

    GoodExampleHeading(
        text = stringResource(id = R.string.standalone_links_example_3_header),
        modifier = Modifier.testTag(standaloneLinksExample3HeadingTestTag)
    )

    GenericStandAloneLink(
        text = stringResource(id = R.string.standalone_links_example_3_text),
        modifier = Modifier.testTag(standaloneLinksExample3LinkTextTestTag)
    ) {
        uriHandler.openUri(COMPOSE_ACCESSIBILITY_URL)
    }
}

@Preview(showBackground = true)
@Composable
fun GoodExample3Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample3()
        }
    }
}

@Composable
private fun GoodExample4() {
    // OK example 4: Accessible stand-alone link button
    val uriHandler = LocalUriHandler.current
    val goToComposeAccessibility = {
        uriHandler.openUri(COMPOSE_ACCESSIBILITY_URL)
    }
    // Optional (see below):
    // val genericLinkOnClickLabel =
    //     stringResource(id = R.string.standalone_links_generic_on_click_label)

    GoodExampleHeading(
        text = stringResource(id = R.string.standalone_links_example_4_header),
        modifier = Modifier.testTag(standaloneLinksExample4HeadingTestTag)
    )
    Spacer(modifier = Modifier.height(8.dp))

    VisibleFocusBorderTextButton(
        onClick = goToComposeAccessibility, // activates in touch mode
        modifier = Modifier
            .testTag(standaloneLinksExample4LinkButtonTestTag)
            // Optional: Customizes TalkBack click action announcement, but at the cost of adding a
            // second keyboard tab stop on the same button (with different focus highlighting).
            // Note that the same lambda is passed to both the button and Modifier.clickable()
            // onClick parameters.
            // .clickable(
            //     onClickLabel = genericLinkOnClickLabel,
            //     onClick = goToComposeAccessibility // activates in Assistive Technologies
            // )
    ) {
        Text(
            text = stringResource(id = R.string.standalone_links_example_4_text),
            // Key technique: Use weight() without fill to keep the icon visible when the text
            // wraps at large text size.
            modifier = Modifier.weight(1f, fill = false),
            textDecoration = TextDecoration.Underline
        )
        Spacer(Modifier.width(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_external_link_outline),
            // Key technique: Alternative to onClickLabel. Merged with button text into one
            // announcement.
            contentDescription = stringResource(id = R.string.standalone_links_example_4_icon_description),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GoodExample4Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample4()
        }
    }
}