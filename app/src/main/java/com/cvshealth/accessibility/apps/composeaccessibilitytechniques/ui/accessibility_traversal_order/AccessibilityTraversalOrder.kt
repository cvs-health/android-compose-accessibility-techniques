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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accessibility_traversal_order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BadExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BoldBodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val accessibilityTraversalOrderHeadingTestTag = "accessibilityTraversalOrderHeading"
const val accessibilityTraversalOrderExample1HeadingTestTag = "accessibilityTraversalOrderExample1Heading"
const val accessibilityTraversalOrderExample1RowTestTag = "accessibilityTraversalOrderExample1Row"
const val accessibilityTraversalOrderExample1MainColumnTestTag = "accessibilityTraversalOrderExample1MainColumn"
const val accessibilityTraversalOrderExample1SidebarColumnTestTag = "accessibilityTraversalOrderExample1SidebarColumn"
const val accessibilityTraversalOrderExample2HeadingTestTag = "accessibilityTraversalOrderExample2Heading"
const val accessibilityTraversalOrderExample2RowTestTag = "accessibilityTraversalOrderExample2Row"
const val accessibilityTraversalOrderExample2MainColumnTestTag = "accessibilityTraversalOrderExample2MainColumn"
const val accessibilityTraversalOrderExample2SidebarColumnTestTag = "accessibilityTraversalOrderExample2SidebarColumn"
const val accessibilityTraversalOrderExample3HeadingTestTag = "accessibilityTraversalOrderExample3Heading"
const val accessibilityTraversalOrderExample3RowTestTag = "accessibilityTraversalOrderExample3Row"
const val accessibilityTraversalOrderExample3MainColumnTestTag = "accessibilityTraversalOrderExample3MainColumn"
const val accessibilityTraversalOrderExample3SidebarColumnTestTag = "accessibilityTraversalOrderExample3SidebarColumn"

/**
 * Demonstrate techniques regarding accessibility traversal order. This demonstration illustrates
 * traversal using TalkBack reading order, but the same techniques also apply to Switch Access focus
 * order.
 *
 * Applies [GenericScaffold] to wrap the screen content.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@Composable
fun AccessibilityTraversalOrderScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.accessibility_traversal_order_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.accessibility_traversal_order_heading),
                modifier = Modifier.testTag(accessibilityTraversalOrderHeadingTestTag)
            )
            BodyText(textId = R.string.accessibility_traversal_order_description_1)
            BodyText(textId = R.string.accessibility_traversal_order_description_2)

            BadExample1()
            GoodExample2()
            GoodExample3()

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        AccessibilityTraversalOrderScreen {}
    }
}

@Composable
private fun BadExample1() {
    // Bad example 1: Texts in an illogical traversal order
    BadExampleHeading(
        text = stringResource(id = R.string.accessibility_traversal_order_example_1_heading),
        modifier = Modifier.testTag(accessibilityTraversalOrderExample1HeadingTestTag)
    )
    BodyText(textId = R.string.accessibility_traversal_order_example_1_description)

    // Missing techniques: Does not apply isTraversalGroup or traversalIndex.
    Row(
        modifier = Modifier.testTag(accessibilityTraversalOrderExample1RowTestTag)
    ) {
        Column(
            modifier = Modifier
                .testTag(accessibilityTraversalOrderExample1MainColumnTestTag)
                .weight(0.67f)
        ) {
            BoldBodyText(textId = R.string.accessibility_traversal_order_example_group_1_label)
            BodyText(textId = R.string.accessibility_traversal_order_example_group_1_text_a)
            BodyText(textId = R.string.accessibility_traversal_order_example_group_1_text_b)
            BodyText(textId = R.string.accessibility_traversal_order_example_group_1_text_c)
        }
        Spacer(Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .testTag(accessibilityTraversalOrderExample1SidebarColumnTestTag)
                .weight(0.33f)
        ) {
            BoldBodyText(textId = R.string.accessibility_traversal_order_example_group_2_label)
            BodyText(textId = R.string.accessibility_traversal_order_example_group_2_text_a)
            BodyText(textId = R.string.accessibility_traversal_order_example_group_2_text_b)
        }

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
private fun GoodExample2() {
    // Good example 2: Texts arranged in a logical traversal order with isTraversalGroup
    GoodExampleHeading(
        text = stringResource(id = R.string.accessibility_traversal_order_example_2_heading),
        modifier = Modifier.testTag(accessibilityTraversalOrderExample2HeadingTestTag)
    )
    BodyText(textId = R.string.accessibility_traversal_order_example_2_description)

    Row(
        modifier = Modifier.testTag(accessibilityTraversalOrderExample2RowTestTag)
    ) {
        // Note: Columns remain in default left-to-right traversal order within the Row.
        Column(
            modifier = Modifier
                .testTag(accessibilityTraversalOrderExample2MainColumnTestTag)
                .weight(0.67f)
                // Key technique 1: Mark this column so it will act as a unit in traversal order.
                .semantics {
                    isTraversalGroup = true
                }
        ) {
            // Note: The Texts in the Column remain in default top-to-bottom traversal order.
            BoldBodyText(textId = R.string.accessibility_traversal_order_example_group_1_label)
            BodyText(textId = R.string.accessibility_traversal_order_example_group_1_text_a)
            BodyText(textId = R.string.accessibility_traversal_order_example_group_1_text_b)
            BodyText(textId = R.string.accessibility_traversal_order_example_group_1_text_c)
        }
        Spacer(Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .testTag(accessibilityTraversalOrderExample2SidebarColumnTestTag)
                .weight(0.33f)
                // Key technique 1: Mark this column so it will act as a unit in traversal order.
                .semantics {
                    isTraversalGroup = true
                }
        ) {
            // Note: The Texts in the Column remain in default top-to-bottom traversal order.
            BoldBodyText(textId = R.string.accessibility_traversal_order_example_group_2_label)
            BodyText(textId = R.string.accessibility_traversal_order_example_group_2_text_a)
            BodyText(textId = R.string.accessibility_traversal_order_example_group_2_text_b)
        }
    }
    BodyText(textId = R.string.accessibility_traversal_order_example_2_afterword)
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
    // Good example 3: Texts arranged in a different logical traversal order with isTraversalGroup
    // and traversalIndex
    GoodExampleHeading(
        text = stringResource(id = R.string.accessibility_traversal_order_example_3_heading),
        modifier = Modifier.testTag(accessibilityTraversalOrderExample3HeadingTestTag)
    )
    BodyText(textId = R.string.accessibility_traversal_order_example_3_description)

    Row(
        modifier = Modifier
            .testTag(accessibilityTraversalOrderExample3RowTestTag)
            // Key technique 3: Constrain the effect of traversalIndex by applying isTraversalGroup
            // = true to the wrapping layout composable.
            .semantics { isTraversalGroup = true }
    ) {
        Column(
            modifier = Modifier
                .testTag(accessibilityTraversalOrderExample3MainColumnTestTag)
                .weight(0.67f)
                .semantics {
                    // Key technique 1: Mark this column so it will act as a unit in traversal order.
                    isTraversalGroup = true
                    // Key technique 2: Higher traversalIndex is read later, so this Column is read second.
                    traversalIndex = 1f
                }
        ) {
            // Note: The Texts in the Column remain in default top-to-bottom traversal order.
            BoldBodyText(textId = R.string.accessibility_traversal_order_example_group_1_label)
            BodyText(textId = R.string.accessibility_traversal_order_example_group_1_text_a)
            BodyText(textId = R.string.accessibility_traversal_order_example_group_1_text_b)
            BodyText(textId = R.string.accessibility_traversal_order_example_group_1_text_c)
        }
        Spacer(Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .testTag(accessibilityTraversalOrderExample3SidebarColumnTestTag)
                .weight(0.33f)
                .semantics {
                    // Key technique 1: Mark this column so it will act as a unit in traversal order.
                    isTraversalGroup = true
                    // Key technique 2: Lower traversalIndex is read earlier, so this Column is read first.
                    traversalIndex = 0f
                }
        ) {
            // Note: The Texts in the Column remain in default top-to-bottom traversal order.
            BoldBodyText(textId = R.string.accessibility_traversal_order_example_group_2_label)
            BodyText(textId = R.string.accessibility_traversal_order_example_group_2_text_a)
            BodyText(textId = R.string.accessibility_traversal_order_example_group_2_text_b)
        }
    }
    BodyText(textId = R.string.accessibility_traversal_order_example_3_afterword)
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