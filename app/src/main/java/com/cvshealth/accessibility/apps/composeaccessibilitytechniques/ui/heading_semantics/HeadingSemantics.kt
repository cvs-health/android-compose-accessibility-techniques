/*
   Copyright 2023 CVS Health and/or one of its affiliates

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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.heading_semantics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ErrorRed

const val headingSemanticsHeadingTestTag = "headingSemanticsHeading"
const val headingSemanticsLargeTextFauxHeading = "headingSemanticsLargeTextFauxHeading"
const val headingSemanticsContentDescriptionFauxHeading = "headingSemanticsContentDescriptionFauxHeading"
const val headingSemanticsExample3HeadingTestTag = "headingSemanticsExample3Heading"

@Composable
fun HeadingSemanticsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.heading_semantics_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()
        Column(
            modifier = modifier
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.heading_semantics_heading),
                modifier = Modifier.testTag(headingSemanticsHeadingTestTag)
            )
            BodyText(textId = R.string.heading_semantics_description_1)
            BodyText(textId = R.string.heading_semantics_description_2)

            // Bad example 1: Big text is not a heading
            LargeTextFauxHeading(text = stringResource(id = R.string.heading_semantics_example_1_heading))
            BodyText(textId = R.string.heading_semantics_example_1_body_text)

            // Bad example 2: Ending contentDescription with 'Heading' is not a heading
            ContentDescriptionFauxHeading(text = stringResource(id = R.string.heading_semantics_example_2_heading))
            BodyText(textId = R.string.heading_semantics_example_2_body_text)

            // Good example 3: Use semantics heading() property
            GoodExampleHeading(
                text = stringResource(id = R.string.heading_semantics_example_3_heading),
                modifier = Modifier.testTag(headingSemanticsExample3HeadingTestTag)
            )
            BodyText(textId = R.string.heading_semantics_example_3_body_text)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun LargeTextFauxHeading(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .testTag(headingSemanticsLargeTextFauxHeading)
            .semantics(mergeDescendants = true) { }
    ) {
        Icon(
            painterResource(id = R.drawable.ic_close_fill),
            // Note: Suppress decorative icon description; caller must convey icon meaning
            // in the visible text.
            contentDescription = null,
            modifier = Modifier
                .defaultMinSize(24.dp, minHeight = 24.dp)
                .align(Alignment.CenterVertically),
            tint = ErrorRed
        )
        Text(text,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp)
        )
    }
}

@Composable
private fun ContentDescriptionFauxHeading(
    text: String,
    modifier: Modifier = Modifier
) {
    // Appends ", Heading" to the text. Never do this!
    val fauxHeadingContentDescription =
        stringResource(id = R.string.heading_semantics_faux_heading_content_description, text)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .testTag(headingSemanticsContentDescriptionFauxHeading)
            .clearAndSetSemantics {
                contentDescription = fauxHeadingContentDescription
            }
    ) {
        Icon(
            painterResource(id = R.drawable.ic_close_fill),
            contentDescription = null,
            modifier = Modifier
                .defaultMinSize(24.dp, minHeight = 24.dp)
                .align(Alignment.CenterVertically),
            tint = ErrorRed
        )
        Text(text,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme() {
        HeadingSemanticsScreen {}
    }
}