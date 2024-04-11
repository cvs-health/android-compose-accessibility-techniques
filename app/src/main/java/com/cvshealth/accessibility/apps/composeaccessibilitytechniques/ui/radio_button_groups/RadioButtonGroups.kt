/*
   © Copyright 2023-2024 CVS Health and/or one of its affiliates. All rights reserved.

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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.radio_button_groups

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BadExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.RadioButtonGroup
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.visibleFocusBorder
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme


const val radioButtonGroupsHeadingTestTag = "radioButtonGroupsHeading"
const val radioButtonGroupsExample1HeadingTestTag = "radioButtonGroupsExample1Heading"
const val radioButtonGroupsExample1RadioButtonGroupTestTag = "radioButtonGroupsExample1RadioButtonGroup"
const val radioButtonGroupsExample2HeadingTestTag = "radioButtonGroupsExample2Heading"
const val radioButtonGroupsExample2RadioButtonGroupTestTag = "radioButtonGroupsExample2RadioButtonGroup"
const val radioButtonGroupsExample3HeadingTestTag = "radioButtonGroupsExample3Heading"
const val radioButtonGroupsExample3RadioButtonGroupTestTag = "radioButtonGroupsExample3RadioButtonGroup"

/**
 * Demonstrate accessibility techniques for [RadioButton] groups in conformance with WCAG
 * [Success Criterion 1.3.1 Info and Relationships](https://www.w3.org/TR/WCAG22/#info-and-relationships) and [Success Criterion 4.1.2 Name, Role, Value](https://www.w3.org/TR/WCAG22/#name-role-value).
 *
 * Applies [GenericScaffold] to wrap the screen content.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@Composable
fun RadioButtonGroupsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.radio_button_groups_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()
        val options = listOf(
            stringResource(id = R.string.radio_button_groups_option_1),
            stringResource(id = R.string.radio_button_groups_option_2),
            stringResource(id = R.string.radio_button_groups_option_3)
        )

        Column(modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.radio_button_groups_heading),
                modifier = Modifier.testTag(radioButtonGroupsHeadingTestTag)
            )
            BodyText(textId = R.string.radio_button_groups_description_1)
            BodyText(textId = R.string.radio_button_groups_description_2)

            BadExampleHeading(
                text = stringResource(id = R.string.radio_button_groups_example_1_header),
                modifier = Modifier.testTag(radioButtonGroupsExample1HeadingTestTag)
            )
            Spacer(modifier = Modifier.height(8.dp))
            val (example1Selection, setExample1Selection) = remember { mutableIntStateOf(0) }
            FauxRadioButtonGroup(
                groupLabel = stringResource(id = R.string.radio_button_groups_group_label),
                itemLabels = options,
                selectedIndex = example1Selection,
                selectHandler = setExample1Selection,
                modifier = Modifier.testTag(radioButtonGroupsExample1RadioButtonGroupTestTag)
            )

            BadExampleHeading(
                text = stringResource(id = R.string.radio_button_groups_example_2_header),
                modifier = Modifier.testTag(radioButtonGroupsExample2HeadingTestTag)
            )
            BodyText(textId = R.string.radio_button_groups_example_2_description)
            Spacer(modifier = Modifier.height(8.dp))
            val (example2Selection, setExample2Selection) = remember { mutableIntStateOf(0) }
            FauxRadioButtonGroup2(
                groupLabel = stringResource(id = R.string.radio_button_groups_group_label),
                itemLabels = options,
                selectedIndex = example2Selection,
                selectHandler = setExample2Selection,
                modifier = Modifier.testTag(radioButtonGroupsExample2RadioButtonGroupTestTag)
            )

            GoodExampleHeading(
                text = stringResource(id = R.string.radio_button_groups_example_3_header),
                modifier = Modifier.testTag(radioButtonGroupsExample3HeadingTestTag)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Important technique: RadioButton group state has been hoisted up to this parent
            // composable. The state value and a state mutator function lambda are passed down to
            // the RadioButtonGroup() composable.
            val (example3Selection, setExample3Selection) = remember { mutableIntStateOf(0) }
            // Key accessibility techniques are described in components/RadioButtonGroup.kt.
            RadioButtonGroup(
                groupLabel = stringResource(id = R.string.radio_button_groups_group_label),
                itemLabels = options,
                selectedIndex = example3Selection,
                selectHandler = setExample3Selection,
                modifier = Modifier.testTag(radioButtonGroupsExample3RadioButtonGroupTestTag)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        RadioButtonGroupsScreen {}
    }
}

/**
 * Constructs a deliberately inaccessible group of [RadioButton] controls. Fails to implement proper
 * accessibility semantics in the following ways:
 * 1. The Column is not marked as an selectableGroup().
 * 2. The Row is not selectable() with Role.RadioButton. This means the Text() and RadioButton() are
 *    not programmatically-associated and makes the touch target smaller.
 * 3. The RadioButton selection click handler is non-null; this should be handled by the Row-level
 *    selectable().
 * Also, the tap target size of the RadioButton is fragile: it is automatically expanded to 48x48 dp
 * by Compose only because it is clickable.
 *
 * @param groupLabel the radio button group label string
 * @param itemLabels a [List] of radio button label strings
 * @param selectedIndex the currently selected [RadioButton]'s index in the itemLabels list
 * @param selectHandler callback for selecting a [RadioButton]
 * @param modifier optional [Modifier] for the radio button group's [Column] layout
 */
@Composable
private fun FauxRadioButtonGroup(
    groupLabel: String,
    itemLabels: List<String>,
    selectedIndex: Int,
    selectHandler: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(groupLabel)
        // Key mistake 1: The following Column needs Modifier.selectableGroup().
        Column {
            itemLabels.forEachIndexed { index: Int, label: String ->
                // Key mistake 2: The following Row needs Modifier.selectable() with role =
                // Role.RadioButton. That modifier would merge the child semantics, associating the
                // RadioButton control with its label, as well as making the entire Row a touch
                // target.
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Key mistake #3: Related to mistake #2, this RadioButton should not do its own
                    // onClick handling; that should be done at the Row level only. (If onClick
                    // handling is also retained here, the RadioButton would become separately
                    // focusable from the Row in assistive technologies like TalkBack and Switch
                    // Access.)
                    RadioButton(
                        selected = (selectedIndex == index),
                        onClick = { selectHandler(index) }
                    )
                    Text(text = label, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FauxRadioGroupPreview() {
    val options = listOf("Banana", "Grape", "Orange")
    val (selectedOption, setSelectedOption) = remember { mutableIntStateOf(0) }
    ComposeAccessibilityTechniquesTheme {
        Column {
            FauxRadioButtonGroup(
                groupLabel = "Pick a fruit:",
                itemLabels = options,
                selectedIndex = selectedOption,
                selectHandler = setSelectedOption
            )
        }
    }
}

/**
 * Constructs a deliberately semi-inaccessible group of [RadioButton] controls. Although
 * FauxRadioButtonGroup2 correctly remediates the individual [RadioButton] controls, it fails to
 * apply single-selection list semantics to the radio button group as a whole, i.e. the inner
 * [Column] is not marked as a selectableGroup().
 *
 * @param groupLabel the radio button group label string
 * @param itemLabels a [List] of radio button label strings
 * @param selectedIndex the currently selected [RadioButton]'s index in the itemLabels list
 * @param selectHandler callback for selecting a [RadioButton]
 * @param modifier optional [Modifier] for the radio button group's [Column] layout
 */
@Composable
private fun FauxRadioButtonGroup2(
    groupLabel: String,
    itemLabels: List<String>,
    selectedIndex: Int,
    selectHandler: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(groupLabel)
        // Key mistake: The following Column needs Modifier.selectableGroup() to supply
        // single-selection list semantics to the group.
        Column {
            itemLabels.forEachIndexed { index: Int, label: String ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .visibleFocusBorder()
                    // Key technique: Apply Modifier.selectable() to handle all RadioButton role
                    // semantics and click handling at the Row level. This also merges all child
                    // composable semantics, so the RadioButton and its label are programmatically
                    // associated.
                    .selectable(
                        selected = (selectedIndex == index),
                        role = Role.RadioButton,
                        onClick = { selectHandler(index) }
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (selectedIndex == index),
                        // Key technique: Only perform onClick handling at the Row level.
                        onClick = null,
                        // Key technique: Without an onClick handler, Compose will no longer
                        // automatically size the RadioButton to the 48dp x 48dp minimum touch
                        // target size. Do so manually here, unless there is a design reason for it
                        // to be smaller.
                        modifier = Modifier.minimumInteractiveComponentSize()
                    )
                    Text(text = label, modifier = Modifier.padding(start = 4.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FauxRadioGroupPreview2() {
    val options = listOf("Banana", "Grape", "Orange")
    val (selectedOption, setSelectedOption) = remember { mutableIntStateOf(0) }
    ComposeAccessibilityTechniquesTheme {
        Column {
            FauxRadioButtonGroup2(
                groupLabel = "Pick a fruit:",
                itemLabels = options,
                selectedIndex = selectedOption,
                selectHandler = setSelectedOption
            )
        }
    }
}
