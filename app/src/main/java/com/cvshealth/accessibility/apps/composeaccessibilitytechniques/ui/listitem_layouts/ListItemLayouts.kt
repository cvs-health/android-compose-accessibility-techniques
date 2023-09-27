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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BadExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val listItemLayoutsHeadingTestTag = "listItemLayoutsHeading"
const val listItemLayoutsExample1HeadingTestTag = "listItemLayoutsExample1Heading"
const val listItemLayoutsExample1ListItemTestTag = "listItemLayoutsExample1ListItem"
const val listItemLayoutsExample2HeadingTestTag = "listItemLayoutsExample2Heading"
const val listItemLayoutsExample2ListItemTestTag = "listItemLayoutsExample2ListItem"
const val listItemLayoutsExample3HeadingTestTag = "listItemLayoutsExample3Heading"
const val listItemLayoutsExample3ListItemTestTag = "listItemLayoutsExample3ListItem"
const val listItemLayoutsExample4HeadingTestTag = "listItemLayoutsExample4Heading"
const val listItemLayoutsExample4ListItemTestTag = "listItemLayoutsExample4ListItem"
const val listItemLayoutsExample5HeadingTestTag = "listItemLayoutsExample5Heading"
const val listItemLayoutsExample5ListItemTestTag = "listItemLayoutsExample5ListItem"
const val listItemLayoutsExample6HeadingTestTag = "listItemLayoutsExample6Heading"
const val listItemLayoutsExample6ListItemTestTag = "listItemLayoutsExample6ListItem"
const val listItemLayoutsExample7HeadingTestTag = "listItemLayoutsExample7Heading"
const val listItemLayoutsExample7ListItemTestTag = "listItemLayoutsExample7ListItem"

@Composable
fun ListItemLayoutsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.listitem_layouts_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.listitem_layouts_heading),
                modifier = Modifier.testTag(listItemLayoutsHeadingTestTag)
            )
            BodyText(textId = R.string.listitem_layouts_description_1)
            BodyText(textId = R.string.listitem_layouts_description_2)

            GoodExample1()
            BadExample2()
            GoodExample3()
            BadExample4()
            GoodExample5()
            BadExample6()
            GoodExample7()

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        ListItemLayoutsScreen {}
    }
}

@Composable
private fun GoodExample1() {
    // Good example 1: Accessible inactive ListItem layout
    // This is ListItem's designed use case: an inactive layout container that automatically groups
    // its content texts for semantics. No remediation required.
    GoodExampleHeading(
        text = stringResource(id = R.string.listitem_layouts_example_1_header),
        modifier = Modifier.testTag(listItemLayoutsExample1HeadingTestTag)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Divider()
    ListItem(
        headlineContent = {
            Text(stringResource(id = R.string.listitem_layouts_example_1_label))
        },
        modifier = Modifier
            .testTag(listItemLayoutsExample1ListItemTestTag),
        supportingContent = {
            Text(stringResource(id = R.string.listitem_layouts_example_1_label_2))
        },
    )
    Divider()
}

@Preview(showBackground = true)
@Composable
fun GoodExample1Preview() {
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
private fun BadExample2() {
    // Bad example 2: Inaccessible clickable ListItem layout
    BadExampleHeading(
        text = stringResource(id = R.string.listitem_layouts_example_2_header),
        modifier = Modifier.testTag(listItemLayoutsExample2HeadingTestTag)
    )

    Spacer(modifier = Modifier.height(8.dp))
    Divider()

    val context = LocalContext.current
    ListItem(
        headlineContent = {
            Text(stringResource(id = R.string.listitem_layouts_example_2_label))
        },
        modifier = Modifier
            .testTag(listItemLayoutsExample2ListItemTestTag)
            // Note: The click action and Button role are not announced.
            .clickable(
                role = Role.Button,
            ) {
                val message = context.getString(R.string.listitem_layouts_example_2_message)
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            },
        supportingContent = {
            Text(stringResource(id = R.string.listitem_layouts_example_2_label_2))
        }
    )

    Divider()
}

@Preview(showBackground = true)
@Composable
fun BadExample2Preview() {
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
private fun GoodExample3() {
    // Good example 3: Accessible clickable ListItem layout
    GoodExampleHeading(
        text = stringResource(id = R.string.listitem_layouts_example_3_header),
        modifier = Modifier.testTag(listItemLayoutsExample3HeadingTestTag)
    )

    Spacer(modifier = Modifier.height(8.dp))
    Divider()

    val exampleTitle = stringResource(id = R.string.listitem_layouts_example_3_label)
    val exampleSubtitle = stringResource(id = R.string.listitem_layouts_example_3_label_2)
    val exampleContentDescription = stringResource(id = R.string.listitem_layouts_example_3_content_description)
    val context = LocalContext.current
    ListItem(
        headlineContent = {
            Text(exampleTitle)
        },
        modifier = Modifier
            .testTag(listItemLayoutsExample3ListItemTestTag)
            .clickable(
                role = Role.Button,
            ) {
                val message = context.getString(R.string.listitem_layouts_example_3_message)
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
            // Key technique for clickable ListItems: Override the default ListItem merge semantics
            // in favor of a single, unified contentDescription wrapped with clickable semantics.
            // However, this approach is fragile to any change in the ListItem content composables.
            .clearAndSetSemantics {
                contentDescription = exampleContentDescription
            },
        supportingContent = {
            Text(exampleSubtitle)
        },
    )

    Divider()
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
private fun BadExample4() {
    // Bad example 4: Inaccessible selectable ListItem layout
    val (exampleValue, setExampleValue) = remember { mutableStateOf(false) }
    BadExampleHeading(
        text = stringResource(id = R.string.listitem_layouts_example_4_header),
        modifier = Modifier.testTag(listItemLayoutsExample4HeadingTestTag)
    )

    Spacer(modifier = Modifier.height(8.dp))
    Divider()

    ListItem(
        headlineContent = {
            Text(stringResource(id = R.string.listitem_layouts_example_4_label))
        },
        modifier = Modifier
            .testTag(listItemLayoutsExample4ListItemTestTag)
            // The ListItem's inner Row merge semantics separates the label text from the outer
            // selectable RadioButton in TalkBack. Thus, it takes two swipes in TalkBack to hear
            // the entire control.
            .selectable(
                selected = exampleValue,
                role = Role.RadioButton,
                onClick = { setExampleValue(!exampleValue) }
            ),
        supportingContent = {
            Text(stringResource(id = R.string.listitem_layouts_example_4_label_2))
        },
        leadingContent = {
            RadioButton(selected = exampleValue, onClick = null)
        }
    )

    Divider()
}

@Preview(showBackground = true)
@Composable
fun BadExample4Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            BadExample4()
        }
    }
}

@Composable
private fun GoodExample5() {
    // Good example 5: Accessible selectable ListItem layout
    GoodExampleHeading(
        text = stringResource(id = R.string.listitem_layouts_example_5_header),
        modifier = Modifier.testTag(listItemLayoutsExample5HeadingTestTag)
    )

    Spacer(modifier = Modifier.height(8.dp))
    Divider()

    val (exampleValue, setExampleValue) = remember { mutableStateOf(false) }
    val exampleTitle = stringResource(id = R.string.listitem_layouts_example_5_label)
    val exampleSubtitle = stringResource(id = R.string.listitem_layouts_example_5_label_2)
    val exampleContentDescription = stringResource(id = R.string.listitem_layouts_example_5_content_description)
    ListItem(
        headlineContent = {
            Text(exampleTitle)
        },
        modifier = Modifier
            .testTag(listItemLayoutsExample5ListItemTestTag)
            .selectable(
                selected = exampleValue,
                role = Role.RadioButton,
                onClick = { setExampleValue(!exampleValue) }
            )
            // Key technique for selectable ListItems: Override the default ListItem merge semantics
            // in favor of a single, unified contentDescription wrapped with selectable semantics.
            // However, this approach is fragile to any change in the ListItem content composables.
            .clearAndSetSemantics {
                contentDescription = exampleContentDescription
            },
        supportingContent = {
            Text(exampleSubtitle)
        },
        leadingContent = {
            RadioButton(selected = exampleValue, onClick = null)
        }
    )

    Divider()
}

@Preview(showBackground = true)
@Composable
fun GoodExample5Preview() {
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

@Composable
private fun BadExample6() {
    // Bad example 6: Inaccessible toggleable ListItem layout
    val (exampleValue, setExampleValue) = remember { mutableStateOf(false) }
    BadExampleHeading(
        text = stringResource(id = R.string.listitem_layouts_example_6_header),
        modifier = Modifier.testTag(listItemLayoutsExample6HeadingTestTag)
    )

    Spacer(modifier = Modifier.height(8.dp))
    Divider()

    ListItem(
        headlineContent = {
            Text(stringResource(id = R.string.listitem_layouts_example_6_label))
        },
        modifier = Modifier
            .testTag(listItemLayoutsExample6ListItemTestTag)
            // The ListItem's inner Row merge semantics separates the label text from the outer
            // toggleable Switch in TalkBack. Thus, it takes two swipes in TalkBack to hear the
            // entire control.
            .toggleable(
                value = exampleValue,
                role = Role.Switch,
                onValueChange = setExampleValue
            ),
        supportingContent = {
            Text(stringResource(id = R.string.listitem_layouts_example_6_label_2))
        },
        trailingContent = {
            Switch(checked = exampleValue, onCheckedChange = null)
        }
    )

    Divider()
}

@Preview(showBackground = true)
@Composable
fun BadExample6Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            BadExample6()
        }
    }
}

@Composable
private fun GoodExample7() {
    // Good example 7: Accessible toggleable ListItem layout
    GoodExampleHeading(
        text = stringResource(id = R.string.listitem_layouts_example_7_header),
        modifier = Modifier.testTag(listItemLayoutsExample7HeadingTestTag)
    )

    Spacer(modifier = Modifier.height(8.dp))
    Divider()

    val (exampleValue, setExampleValue) = remember { mutableStateOf(false) }
    val exampleTitle = stringResource(id = R.string.listitem_layouts_example_7_label)
    val exampleSubtitle = stringResource(id = R.string.listitem_layouts_example_7_label_2)
    val exampleContentDescription = stringResource(id = R.string.listitem_layouts_example_7_content_description)
    ListItem(
        headlineContent = {
            Text(exampleTitle)
        },
        modifier = Modifier
            .testTag(listItemLayoutsExample7ListItemTestTag)
            .toggleable(
                value = exampleValue,
                role = Role.Switch,
                onValueChange = setExampleValue
            )
            // Key technique for toggleable ListItems: Override the default ListItem merge semantics
            // in favor of a single, unified contentDescription wrapped with toggleable semantics.
            // However, this approach is fragile to any change in the ListItem content composables.
            .clearAndSetSemantics {
                contentDescription = exampleContentDescription
            },
        supportingContent = {
            Text(exampleSubtitle)
        },
        trailingContent = {
            Switch(checked = exampleValue, onCheckedChange = null)
        }
    )

    Divider()
}

@Preview(showBackground = true)
@Composable
fun GoodExample7Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample7()
        }
    }
}