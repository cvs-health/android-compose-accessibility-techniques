/*
   © Copyright 2024 CVS Health and/or one of its affiliates. All rights reserved.

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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.adaptive_layouts

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericGroupedTitledCard
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.OkExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val adaptiveLayoutsHeadingTestTag = "adaptiveLayoutsHeading"
const val adaptiveLayoutsExample1HeadingTestTag = "adaptiveLayoutsExample1Heading"
const val adaptiveLayoutsExample2HeadingTestTag = "adaptiveLayoutsExample2Heading"

/**
 * Demonstrates how to make screen layouts adapt to orientation and display size changes.
 * See MainActivity.kt for information on determining the appropriate WindowSizeClass.
 *
 * Applies [GenericScaffold] to wrap the screen content.
 *
 * Key adaptive layout techniques:
 * 1. Make the entire screen's content scrollable.
 * 2. Use LocalConfiguration.current.orientation if necessary, but with caution.
 * 3. Accept a WindowSizeClass (from Activity level) and create layout conditionally based on its
 *    widthSizeClass and/or heightSizeClass properties.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 * @param windowSizeClass the displayed [WindowSizeClass] for determining adaptive layouts
 */
@Composable
fun AdaptiveLayoutsScreen(
    onBackPressed: () -> Unit,
    windowSizeClass: WindowSizeClass // Key technique: Get WindowSizeClass from activity...
) {
    GenericScaffold(
        title = stringResource(id = R.string.adaptive_layouts_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        // Key technique: Remember scrollState to make entire screen content scrollable.
        val scrollState = rememberScrollState()
        Column(
            modifier = modifier
                // Key technique: Use verticalScroll() to make entire screen content scrollable.
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.adaptive_layouts_heading),
                modifier = Modifier.testTag(adaptiveLayoutsHeadingTestTag)
            )
            BodyText(textId = R.string.adaptive_layouts_description_1)
            BodyText(textId = R.string.adaptive_layouts_description_2)

            OkExample1()

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()

            GoodExample2(windowSizeClass)

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true)
@Composable
private fun AdaptiveLayoutsScreenCompactPreview() {
    ComposeAccessibilityTechniquesTheme {
        AdaptiveLayoutsScreen(
            onBackPressed = {},
            // Key preview technique: Create a WindowSizeClass matching this preview's window.
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(width=300.dp, height=1024.dp))
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
// Key preview technique: To preview layouts, set their size with widthDp and heightDp.
// The following widthDp sets a Medium width.
@Preview(showBackground = true, widthDp = 600, heightDp = 900)
@Composable
private fun AdaptiveLayoutsScreenMediumPreview() {
    ComposeAccessibilityTechniquesTheme {
        AdaptiveLayoutsScreen(
            onBackPressed = {},
            // Key preview technique: Create a WindowSizeClass matching this preview's window.
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(width=600.dp, height=900.dp))
        )
    }
}

@Composable
private fun OkExample1() {
    // OK example 1: Orientation from configuration
    OkExampleHeading(
        text = stringResource(id = R.string.adaptive_layouts_example_1_heading),
        modifier = Modifier.testTag(adaptiveLayoutsExample1HeadingTestTag)
    )
    BodyText(textId = R.string.adaptive_layouts_example_1_description)
    Spacer(modifier = Modifier.height(8.dp))

    // Key technique: Get LocalConfiguration orientation and test for portrait/landscape setting
    val orientation = LocalConfiguration.current.orientation
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        GenericGroupedTitledCard(titleId = R.string.adaptive_layouts_pane_1_title) {
            BodyText(textId = R.string.adaptive_layouts_pane_1_description)
        }
        Spacer(modifier = Modifier.height(8.dp))
        GenericGroupedTitledCard(titleId = R.string.adaptive_layouts_pane_2_title) {
            BodyText(textId = R.string.adaptive_layouts_pane_2_description)
        }
    } else {
        Row {
            GenericGroupedTitledCard(
                titleId = R.string.adaptive_layouts_pane_1_title,
                modifier = Modifier.weight(1f)
            ) {
                BodyText(textId = R.string.adaptive_layouts_pane_1_description)
            }
            Spacer(modifier = Modifier.width(8.dp))
            GenericGroupedTitledCard(
                titleId = R.string.adaptive_layouts_pane_2_title,
                modifier = Modifier.weight(1f)
            ) {
                BodyText(textId = R.string.adaptive_layouts_pane_2_description)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OkExample1Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )  {
            OkExample1()
        }
    }
}

@Composable
private fun GoodExample2(windowSizeClass: WindowSizeClass) {
    // Good example 2: Orientation from WindowSizeClass
    GoodExampleHeading(
        text = stringResource(id = R.string.adaptive_layouts_example_2_heading),
        modifier = Modifier.testTag(adaptiveLayoutsExample2HeadingTestTag)
    )
    BodyText(textId = R.string.adaptive_layouts_example_2_description)
    Spacer(modifier = Modifier.height(8.dp))

    // Key technique: Set layout based on WindowSizeClass.widthSizeClass and/or .heightSizeClass
    // WindowWidthSizeClass and WindowHeightSizeClass each define three possible values: Compact,
    // Medium, and Expanded.
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            // Linear column of cards
            GenericGroupedTitledCard(titleId = R.string.adaptive_layouts_pane_1_title) {
                BodyText(textId = R.string.adaptive_layouts_pane_1_description)
            }
            Spacer(modifier = Modifier.height(8.dp))
            GenericGroupedTitledCard(titleId = R.string.adaptive_layouts_pane_2_title) {
                BodyText(textId = R.string.adaptive_layouts_pane_2_description)
            }
            Spacer(modifier = Modifier.height(8.dp))
            GenericGroupedTitledCard(titleId = R.string.adaptive_layouts_pane_3_title) {
                BodyText(textId = R.string.adaptive_layouts_pane_3_description)
            }
        }

        WindowWidthSizeClass.Medium -> {
            // 2-wide rows of cards
            Row {
                GenericGroupedTitledCard(
                    titleId = R.string.adaptive_layouts_pane_1_title,
                    modifier = Modifier.weight(1f)
                ) {
                    BodyText(textId = R.string.adaptive_layouts_pane_1_description)
                }
                Spacer(modifier = Modifier.width(8.dp))
                GenericGroupedTitledCard(
                    titleId = R.string.adaptive_layouts_pane_2_title,
                    modifier = Modifier.weight(1f)
                ) {
                    BodyText(textId = R.string.adaptive_layouts_pane_2_description)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                GenericGroupedTitledCard(
                    titleId = R.string.adaptive_layouts_pane_3_title,
                    modifier = Modifier.weight(1f)
                ) {
                    BodyText(textId = R.string.adaptive_layouts_pane_3_description)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        WindowWidthSizeClass.Expanded -> {
            // 3-wide row of cards
            Row {
                GenericGroupedTitledCard(
                    titleId = R.string.adaptive_layouts_pane_1_title,
                    modifier = Modifier.weight(1f)
                ) {
                    BodyText(textId = R.string.adaptive_layouts_pane_1_description)
                }
                Spacer(modifier = Modifier.width(8.dp))
                GenericGroupedTitledCard(
                    titleId = R.string.adaptive_layouts_pane_2_title,
                    modifier = Modifier.weight(1f)
                ) {
                    BodyText(textId = R.string.adaptive_layouts_pane_2_description)
                }
                Spacer(modifier = Modifier.width(8.dp))
                GenericGroupedTitledCard(
                    titleId = R.string.adaptive_layouts_pane_3_title,
                    modifier = Modifier.weight(1f)
                ) {
                    BodyText(textId = R.string.adaptive_layouts_pane_3_description)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
// Key preview technique: To preview layouts, set their size with widthDp and heightDp.
// The following widthDp sets a Compact width.
@Preview(showBackground = true, widthDp = 400, heightDp = 400)
@Composable
private fun GoodExample2CompactPreview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )  {
            GoodExample2(
                // Key preview technique: Create a WindowSizeClass matching this preview's window.
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(width=400.dp, height=400.dp))
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
// Key preview technique: To preview layouts, set their size with widthDp and heightDp.
// The following widthDp sets a Medium width.
@Preview(showBackground = true, widthDp = 600, heightDp = 300)
@Composable
private fun GoodExample2MediumPreview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )  {
            GoodExample2(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(width=600.dp, height=300.dp))
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
// Key preview technique: To preview layouts, set their size with widthDp and heightDp.
// The following widthDp sets an Expanded width.
@Preview(showBackground = true, widthDp = 1000, heightDp = 200)
@Composable
private fun GoodExample2ExpandedPreview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )  {
            GoodExample2(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(width=1000.dp, height=200.dp))
            )
        }
    }
}