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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_state_descriptions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.CheckboxRow
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.OkExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SwitchRow
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val customStateDescriptionsHeadingTestTag = "customStateDescriptionsHeading"
const val customStateDescriptionsExample1HeadingTestTag = "customStateDescriptionsExample1Heading"
const val customStateDescriptionsExample1CheckboxTestTag = "customStateDescriptionsExample1Checkbox"
const val customStateDescriptionsExample2HeadingTestTag = "customStateDescriptionsExample2Heading"
const val customStateDescriptionsExample2CheckboxTestTag = "customStateDescriptionsExample2Checkbox"
const val customStateDescriptionsExample3HeadingTestTag = "customStateDescriptionsExample3Heading"
const val customStateDescriptionsExample3SwitchTestTag = "customStateDescriptionsExample3Switch"
const val customStateDescriptionsExample4HeadingTestTag = "customStateDescriptionsExample4Heading"
const val customStateDescriptionsExample4SwitchTestTag = "customStateDescriptionsExample4Switch"

@Composable
fun CustomStateDescriptionsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.custom_state_descriptions_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.custom_state_descriptions_heading),
                modifier = Modifier.testTag(customStateDescriptionsHeadingTestTag)
            )
            BodyText(textId = R.string.custom_state_descriptions_description)
            BodyText(textId = R.string.custom_state_descriptions_description_2)

            OkExample1()
            GoodExample2()
            OkExample3()
            GoodExample4()

            Spacer(modifier = Modifier.height(8.dp))
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        CustomStateDescriptionsScreen {}
    }
}

@Composable
private fun OkExample1() {
    // OK example 1: Checkbox with default state labels
    val (exampleValue, setExampleValue) = rememberSaveable { mutableStateOf(false) }
    OkExampleHeading(
        text = stringResource(id = R.string.custom_state_descriptions_example_1_heading),
        modifier = Modifier.testTag(customStateDescriptionsExample1HeadingTestTag)
    )
    CheckboxRow(
        text = stringResource(id = R.string.custom_state_descriptions_default_checkbox),
        checked = exampleValue,
        toggleHandler = setExampleValue,
        modifier = Modifier.testTag(customStateDescriptionsExample1CheckboxTestTag)
    )
}

@Preview(showBackground = true)
@Composable
fun OkExample1Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            OkExample1()
        }
    }
}

@Composable
private fun GoodExample2() {
    // Good example 2: Checkbox with customized state labels
    val (isAlarmActive, setIsAlarmActive) = rememberSaveable { mutableStateOf(false) }

    // Key technique 1a: Create a custom state description message based on the widget's state.
    // Note: Retrieving externalized strings needs to be done in a composable context, and therefore
    // outside the Modifier.semantics block.
    val customStateDescription = stringResource(
        id = if (isAlarmActive)
            R.string.custom_state_descriptions_checked_label
        else
            R.string.custom_state_descriptions_unchecked_label
    )

    GoodExampleHeading(
        text = stringResource(id = R.string.custom_state_descriptions_example_2_heading),
        modifier = Modifier.testTag(customStateDescriptionsExample2HeadingTestTag)
    )
    CheckboxRow(
        text = stringResource(id = R.string.custom_state_descriptions_customized_checkbox),
        checked = isAlarmActive,
        toggleHandler = setIsAlarmActive,
        modifier = Modifier
            .testTag(customStateDescriptionsExample2CheckboxTestTag)
            // Key technique 1b: Set the Modifier.semantics stateDescription property.
            .semantics {
                stateDescription = customStateDescription
            }
    )
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
private fun OkExample3() {
    // OK example 3: Switch with default state labels
    val (shieldsStateValue, setShieldsStateValue) = rememberSaveable { mutableStateOf(false) }
    OkExampleHeading(
        text = stringResource(id = R.string.custom_state_descriptions_example_3_heading),
        modifier = Modifier.testTag(customStateDescriptionsExample3HeadingTestTag)
    )
    SwitchRow(
        text = stringResource(id = R.string.custom_state_descriptions_default_switch),
        checked = shieldsStateValue,
        toggleHandler = setShieldsStateValue,
        modifier = Modifier.testTag(customStateDescriptionsExample3SwitchTestTag)
    )
}

@Preview(showBackground = true)
@Composable
fun OkExample3Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            OkExample3()
        }
    }
}

@Composable
private fun GoodExample4() {
    // Good example 4: Switch with custom state and click labels
    val (shieldsStateValue, setShieldsStateValue) = rememberSaveable { mutableStateOf(false) }

    // Key technique 1a: Create a custom state description message based on the widget's state.
    // Note: Retrieving externalized strings needs to be done in a composable context, and therefore
    // outside the Modifier.semantics block.
    val shieldsStateDescription = stringResource(
        id = if (shieldsStateValue)
            R.string.custom_state_descriptions_shields_raised
        else
            R.string.custom_state_descriptions_shields_lowered
    )

    GoodExampleHeading(
        text = stringResource(id = R.string.custom_state_descriptions_example_4_heading),
        modifier = Modifier.testTag(customStateDescriptionsExample4HeadingTestTag)
    )
    SwitchRow(
        text = stringResource(id = R.string.custom_state_descriptions_customized_switch),
        checked = shieldsStateValue,
        toggleHandler = setShieldsStateValue,
        modifier = Modifier
            .testTag(customStateDescriptionsExample4SwitchTestTag)
            // Key technique 1b: Set the Modifier.semantics stateDescription property.
            .semantics {
                stateDescription = shieldsStateDescription
            }
    )
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