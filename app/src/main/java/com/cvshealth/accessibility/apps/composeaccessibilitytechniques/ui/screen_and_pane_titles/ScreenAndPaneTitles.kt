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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.screen_and_pane_titles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val screenAndPaneTitlesHeadingTestTag = "screenAndPaneTitlesHeading"

/**
 * Demonstrate accessibility techniques for screen and pane titles and how to observe them.
 * Actual screen title techniques are found in [GenericScaffold] code.
 *
 * Applies [GenericScaffold] to wrap the screen content.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@Composable
fun ScreenAndPaneTitlesScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.screen_and_pane_titles_title),
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
                text = stringResource(id = R.string.screen_and_pane_titles_heading),
                modifier = Modifier.testTag(screenAndPaneTitlesHeadingTestTag)
            )
            BodyText(textId = R.string.screen_and_pane_titles_description_1)
            BodyText(textId = R.string.screen_and_pane_titles_description_2)
            BodyText(textId = R.string.screen_and_pane_titles_description_3)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenAndPaneTitlesScreenPreview() {
    ComposeAccessibilityTechniquesTheme {
        ScreenAndPaneTitlesScreen(
            onBackPressed = {}
        )
    }
}
