/*
   Copyright 2025 CVS Health and/or one of its affiliates

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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.scrolling_columns

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.visibleFocusBorder
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val scrollingTextColumnTestTag = "scrollingTextColumn"
const val scrollingTextColumnScreenHeadingTestTag = "scrollingTextColumnScreenHeading"
const val scrollingTextColumnExample1HeadingTestTag = "scrollingTextColumnExample1Heading"

/**
 * Demonstrate accessibility techniques for a scrollable [Column] of non-interactive content.
  *
 * Applies [GenericScaffold] to wrap the screen content.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScrollingTextColumnsScreen(
    onBackPressed: () -> Unit
) {
    // Key technique 1: Declare the ScrollState and CoroutineScope for later use.
    val scrollState = rememberScrollState()
    val baseText = stringResource(R.string.scrolling_text_columns_example_1_content_block)
    var fullText = baseText
    (2..10).forEach { _ -> fullText += "\n\n" + baseText }

    GenericScaffold(
        title = stringResource(id = R.string.scrolling_text_columns_title),
        onBackPressed = onBackPressed,
    ) { modifier ->
        Column(
            modifier = modifier
                .testTag(scrollingTextColumnTestTag)
                .fillMaxSize()
                // Key technique: Make the column scrollable and attach the scrollState.
                .verticalScroll(scrollState)
                // Key technique: Place a focus indicator on the scrollable column, similar to
                // ScrollView's.
                .visibleFocusBorder()
                // Key technique: Make the scrollable column keyboard focusable.
                // Actually, as long as *something* within the Column is focusable, the PageUp and
                // PageDown keys will scroll the Column. But always apply focusable() *after* the
                // focus indicator, so that the Column's focus is made visible.
                .focusable()
                // Technique: Padding is applied here to separate the focus indicator from the
                // Column's content.
                .padding(horizontal = 16.dp)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.scrolling_text_columns_heading),
                modifier = Modifier.testTag(scrollingTextColumnScreenHeadingTestTag)
            )
            BodyText(textId = R.string.scrolling_text_columns_description_1)
            BodyText(textId = R.string.scrolling_text_columns_description_2)

            GoodExampleHeading(
                text = stringResource(id = R.string.scrolling_text_columns_example_1_header),
                modifier = Modifier.testTag(scrollingTextColumnExample1HeadingTestTag)
            )
            Spacer(Modifier.height(8.dp))

            Text(fullText)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewScrollingTextWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        ScrollingTextColumnsScreen {}
    }
}