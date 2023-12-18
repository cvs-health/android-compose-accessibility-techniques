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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
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
        ListItemLayoutsScreen {}
    }
}

@Composable
private fun GoodExample1() {
    // Good example 1: Accessible inactive ListItem layout
    // This is ListItem's designed use case: an inactive layout container that automatically groups
    // its content texts for semantics.
    GoodExampleHeading(
        text = stringResource(id = R.string.listitem_layouts_example_1_header),
        modifier = Modifier.testTag(listItemLayoutsExample1HeadingTestTag)
    )
    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider()
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
    HorizontalDivider()
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
private fun GoodExample2() {
    // Good example 2: Accessible clickable ListItem layout
    GoodExampleHeading(
        text = stringResource(id = R.string.listitem_layouts_example_2_header),
        modifier = Modifier.testTag(listItemLayoutsExample2HeadingTestTag)
    )

    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider()

    val context = LocalContext.current
    ListItem(
        headlineContent = {
            Text(stringResource(id = R.string.listitem_layouts_example_2_label))
        },
        modifier = Modifier
            .testTag(listItemLayoutsExample2ListItemTestTag)
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

    HorizontalDivider()
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
    // Good example 3: Accessible selectable ListItem layout
    val (exampleValue, setExampleValue) = remember { mutableStateOf(false) }
    GoodExampleHeading(
        text = stringResource(id = R.string.listitem_layouts_example_3_header),
        modifier = Modifier.testTag(listItemLayoutsExample3HeadingTestTag)
    )

    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider()

    ListItem(
        headlineContent = {
            Text(stringResource(id = R.string.listitem_layouts_example_3_label))
        },
        modifier = Modifier
            .testTag(listItemLayoutsExample3ListItemTestTag)
            .selectable(
                selected = exampleValue,
                role = Role.RadioButton,
                onClick = { setExampleValue(!exampleValue) }
            ),
        supportingContent = {
            Text(stringResource(id = R.string.listitem_layouts_example_3_label_2))
        },
        leadingContent = {
            RadioButton(selected = exampleValue, onClick = null)
        }
    )

    HorizontalDivider()
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
    // Good example 4: Accessible toggleable ListItem layout
    val (exampleValue, setExampleValue) = remember { mutableStateOf(false) }
    GoodExampleHeading(
        text = stringResource(id = R.string.listitem_layouts_example_4_header),
        modifier = Modifier.testTag(listItemLayoutsExample4HeadingTestTag)
    )

    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider()

    ListItem(
        headlineContent = {
            Text(stringResource(id = R.string.listitem_layouts_example_4_label))
        },
        modifier = Modifier
            .testTag(listItemLayoutsExample4ListItemTestTag)
            .toggleable(
                value = exampleValue,
                role = Role.Switch,
                onValueChange = setExampleValue
            ),
        supportingContent = {
            Text(stringResource(id = R.string.listitem_layouts_example_4_label_2))
        },
        trailingContent = {
            Switch(checked = exampleValue, onCheckedChange = null)
        }
    )

    HorizontalDivider()
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
