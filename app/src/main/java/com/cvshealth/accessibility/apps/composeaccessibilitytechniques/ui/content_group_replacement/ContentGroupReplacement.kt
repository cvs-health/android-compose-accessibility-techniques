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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_group_replacement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BadExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme


const val contentGroupReplacementHeadingTestTag = "contentGroupReplacementHeading"
const val contentGroupReplacementExample1HeadingTestTag = "contentGroupReplacementExample1Heading"
const val contentGroupReplacementExample1RowTestTag = "contentGroupReplacementExample1Row"
const val contentGroupReplacementExample1RatingLabelTestTag = "contentGroupReplacementExample1RatingLabel"
const val contentGroupReplacementExample1ProgressBarTestTag = "contentGroupReplacementExample1ProgressBar"
const val contentGroupReplacementExample1RatingTextTestTag = "contentGroupReplacementExample1RatingText"
const val contentGroupReplacementExample1ReviewTextTestTag = "contentGroupReplacementExample1ReviewText"
const val contentGroupReplacementExample2HeadingTestTag = "contentGroupReplacementExample2Heading"
const val contentGroupReplacementExample2RowTestTag = "contentGroupReplacementExample2Row"
const val contentGroupReplacementExample3HeadingTestTag = "contentGroupReplacementExample3Heading"
const val contentGroupReplacementExample3RowTestTag = "contentGroupReplacementExample3Row"
const val contentGroupReplacementExample4HeadingTestTag = "contentGroupReplacementExample4Heading"
const val contentGroupReplacementExample4RowTestTag = "contentGroupReplacementExample4Row"

// Common example values
const val contentGroupReplacementExamplesRating = 3.4f
const val contentGroupReplacementExamplesMaxRating = 5
const val contentGroupReplacementExamplesReviews = 856

/**
 * Demonstrate techniques for replacing the accessibility announcement for a content group.
 *
 * Applies [GenericScaffold] to wrap the screen content.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@Composable
fun ContentGroupReplacementScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.content_group_replacement_title),
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
                text = stringResource(id = R.string.content_group_replacement_heading),
                modifier = Modifier.testTag(contentGroupReplacementHeadingTestTag)
            )
            BodyText(textId = R.string.content_group_replacement_description)
            BodyText(textId = R.string.content_group_replacement_description_2)

            BadExample1()
            BadExample2()
            GoodExample3()
            GoodExample4()

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        ContentGroupReplacementScreen {}
    }
}

@Composable
private fun BadExample1() {
    // Bad example 1: Rating with content ungrouped
    BadExampleHeading(
        text = stringResource(id = R.string.content_group_replacement_ungrouped_rating_heading),
        modifier = Modifier.testTag(contentGroupReplacementExample1HeadingTestTag)
    )
    Row(
        modifier = Modifier
            .testTag(contentGroupReplacementExample1RowTestTag)
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.content_group_replacement_rating_label),
                modifier = Modifier.testTag(contentGroupReplacementExample1RatingLabelTestTag)
            )
            LinearProgressIndicator(
                progress = { contentGroupReplacementExamplesRating / contentGroupReplacementExamplesMaxRating },
                modifier = Modifier
                    .testTag(contentGroupReplacementExample1ProgressBarTestTag)
                    .width(80.dp)
                    .padding(start = 8.dp, end = 8.dp),
            )
            Text(
                text = stringResource(
                    id = R.string.content_group_replacement_rating_text,
                    contentGroupReplacementExamplesRating,
                    contentGroupReplacementExamplesMaxRating
                ),
                modifier = Modifier.testTag(contentGroupReplacementExample1RatingTextTestTag)
            )
        }
        Text(
            text = stringResource(
                id = R.string.content_group_replacement_reviews,
                contentGroupReplacementExamplesReviews
            ),
            modifier = Modifier.testTag(contentGroupReplacementExample1ReviewTextTestTag)
        )
    }
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
private fun BadExample2() {
    // Bad example 2: Rating with content grouped
    BadExampleHeading(
        text = stringResource(id = R.string.content_group_replacement_rating_group_heading),
        modifier = Modifier.testTag(contentGroupReplacementExample2HeadingTestTag)
    )
    Row(
        modifier = Modifier
            .testTag(contentGroupReplacementExample2RowTestTag)
            .fillMaxWidth()
            .padding(top = 8.dp)
            .semantics(mergeDescendants = true) { },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(id = R.string.content_group_replacement_rating_label))
            LinearProgressIndicator(
                progress = { contentGroupReplacementExamplesRating / contentGroupReplacementExamplesMaxRating },
                modifier = Modifier
                    .width(80.dp)
                    .padding(start = 8.dp, end = 8.dp),
            )
            Text(
                text = stringResource(
                    id = R.string.content_group_replacement_rating_text,
                    contentGroupReplacementExamplesRating,
                    contentGroupReplacementExamplesMaxRating
                )
            )
        }
        Text(
            text = stringResource(
                id = R.string.content_group_replacement_reviews,
                contentGroupReplacementExamplesReviews
            )
        )
    }
    BodyText(textId = R.string.content_group_replacement_rating_group_note)
}

@Preview(showBackground = true)
@Composable
private fun BadExample2Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            BadExample2()
        }
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun GoodExample3() {
    // Good example 3: Rating with grouped content replaced with hideFromAccessibility()

    // Key techniques:
    // 1. Group the Row's content using Modifier.semantics(mergeDescendants = true).
    // 2. Provide a Row-level contentDescription in the Modifier.semantics lambda.
    // 3. Remove the Row's existing content using Modifier.semantics { hideFromAccessibility() }
    //    on each child element, and when necessary on children of children. (In this case,
    //    the LinearProgressIndicator nested child requires this treatment.)
    GoodExampleHeading(
        text = stringResource(id = R.string.content_group_replacement_rating_group_replaced_heading),
        modifier = Modifier.testTag(contentGroupReplacementExample3HeadingTestTag)
    )

    // Minor technique: pluralStringResource() must be called in a Composable context, and
    // the clearAndSetSemantics lambda is not such a context. So the group content description
    // plural string resource must be retrieved here.
    val groupContentDescription = pluralStringResource(
        id = R.plurals.content_group_replacement_rating_group_content_description,
        count = contentGroupReplacementExamplesReviews,
        contentGroupReplacementExamplesRating,
        contentGroupReplacementExamplesMaxRating,
        contentGroupReplacementExamplesReviews
    )

    Row(
        modifier = Modifier
            .testTag(contentGroupReplacementExample3RowTestTag)
            .fillMaxWidth()
            .padding(top = 8.dp)
            .semantics(mergeDescendants = true) {
                // Note: Can't call pluralStringResource() here -- not in a Composable context.
                contentDescription = groupContentDescription
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.semantics { hideFromAccessibility() }
        ) {
            Text(text = stringResource(id = R.string.content_group_replacement_rating_label))
            LinearProgressIndicator(
                progress = { contentGroupReplacementExamplesRating / contentGroupReplacementExamplesMaxRating },
                modifier = Modifier
                    .width(80.dp)
                    .padding(start = 8.dp, end = 8.dp)
                    // Note that LinearProgressIndicator has to be made invisible too.
                    .semantics { hideFromAccessibility() },
            )
            Text(
                text = stringResource(
                    id = R.string.content_group_replacement_rating_text,
                    contentGroupReplacementExamplesRating,
                    contentGroupReplacementExamplesMaxRating
                )
            )
        }
        Text(
            text = stringResource(
                id = R.string.content_group_replacement_reviews,
                contentGroupReplacementExamplesReviews
            ),
            modifier = Modifier.semantics { hideFromAccessibility() }
        )
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
    // Good example 4: Rating with grouped content replaced with clearAndSetSemantics

    // Key technique: Modifier.clearAndSetSemantics { contentDescription = ... } both removes
    // all semantics from the Row and its children from the accessibility node tree and replaces
    // them with the specified semantics in its lambda (in this case, just a contentDescription).

    GoodExampleHeading(
        text = stringResource(id = R.string.content_group_replacement_rating_group_overridden_heading),
        modifier = Modifier.testTag(contentGroupReplacementExample4HeadingTestTag)
    )

    // Minor technique: pluralStringResource() must be called in a Composable context, and
    // the clearAndSetSemantics lambda is not such a context. So the group content description
    // plural string resource must be retrieved here.
    val groupContentDescription = pluralStringResource(
        id = R.plurals.content_group_replacement_rating_group_content_description,
        count = contentGroupReplacementExamplesReviews,
        contentGroupReplacementExamplesRating,
        contentGroupReplacementExamplesMaxRating,
        contentGroupReplacementExamplesReviews
    )

    Row(
        modifier = Modifier
            .testTag(contentGroupReplacementExample4RowTestTag)
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clearAndSetSemantics {
                // Note: Can't call pluralStringResource() here -- not in a Composable context.
                contentDescription = groupContentDescription
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(id = R.string.content_group_replacement_rating_label))
            LinearProgressIndicator(
                progress = { contentGroupReplacementExamplesRating / contentGroupReplacementExamplesMaxRating },
                modifier = Modifier
                    .width(80.dp)
                    .padding(start = 8.dp, end = 8.dp),
            )
            Text(
                text = stringResource(
                    id = R.string.content_group_replacement_rating_text,
                    contentGroupReplacementExamplesRating,
                    contentGroupReplacementExamplesMaxRating
                )
            )
        }
        Text(
            text = stringResource(
                id = R.string.content_group_replacement_reviews,
                contentGroupReplacementExamplesReviews
            )
        )
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