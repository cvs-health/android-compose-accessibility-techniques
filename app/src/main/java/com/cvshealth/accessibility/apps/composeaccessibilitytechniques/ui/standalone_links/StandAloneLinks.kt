/*
   Copyright 2024-2025 CVS Health and/or one of its affiliates

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
const val standaloneLinksExample4LinkTextTestTag = "standaloneLinksExample4LinkText"
const val standaloneLinksExample5HeadingTestTag = "standaloneLinksExample5Heading"
const val standaloneLinksExample5LinkButtonTestTag = "standaloneLinksExample5LinkButton"

private const val COMPOSE_ACCESSIBILITY_URL = "https://developer.android.com/jetpack/compose/accessibility"

/**
 * Demonstrate accessibility techniques for stand-alone links.
 *
 * Applies [GenericScaffold] to wrap the screen content. Hosts Snackbars.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
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
            GoodExample5()
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
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
private fun BadExample1Preview() {
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
    BodyText(textId = R.string.standalone_links_example_2_description)

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
private fun GoodExample3Preview() {
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
    // Good example 4: Accessible stand-alone link using icon with contentDescription
    val uriHandler = LocalUriHandler.current

    GoodExampleHeading(
        text = stringResource(id = R.string.standalone_links_example_4_header),
        modifier = Modifier.testTag(standaloneLinksExample4HeadingTestTag)
    )
    BodyText(textId = R.string.standalone_links_example_4_description)

    GenericStandAloneLink(
        text = stringResource(id = R.string.standalone_links_example_4_text),
        modifier = Modifier.testTag(standaloneLinksExample4LinkTextTestTag),
        showExternalLinkIcon = true,
        externalLinkIconContentDescription =
            stringResource(R.string.standalone_links_example_4_icon_description),
        linkOnClickLabel = null // Announce click action as "Double-tap to activate"
    ) {
        uriHandler.openUri(COMPOSE_ACCESSIBILITY_URL)
    }
}

@Preview(showBackground = true)
@Composable
private fun GoodExample4Preview() {
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

@Composable
private fun GoodExample5() {
    // Good example 5: Stand-alone link TextButton with semantic onClickLabel
    val uriHandler = LocalUriHandler.current
    val goToComposeAccessibility = {
        uriHandler.openUri(COMPOSE_ACCESSIBILITY_URL)
    }

    GoodExampleHeading(
        text = stringResource(id = R.string.standalone_links_example_5_header),
        modifier = Modifier.testTag(standaloneLinksExample5HeadingTestTag)
    )
    Spacer(modifier = Modifier.height(8.dp))

    // onClickLabel semantics are handled by VisibleFocusBorderTextButton.
    // Key technique: Apply Modifier.semantics { onClick( label = ..., onClick = ...) } in addition
    // to the Button onClick parameter, instead of Modifier.clickable().
    VisibleFocusBorderTextButton(
        onClick = goToComposeAccessibility,
        modifier = Modifier.testTag(standaloneLinksExample5LinkButtonTestTag),
        onClickLabel = stringResource(id = R.string.standalone_link_on_click_label)
    ) {
        Text(
            text = stringResource(id = R.string.standalone_links_example_5_text),
            // Key technique: Use weight() without fill to keep the icon visible when the text
            // wraps at large text size.
            modifier = Modifier.weight(1f, fill = false),
            textDecoration = TextDecoration.Underline
        )
        Spacer(Modifier.width(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_external_link_outline),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GoodExample5Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample5()
        }
    }
}