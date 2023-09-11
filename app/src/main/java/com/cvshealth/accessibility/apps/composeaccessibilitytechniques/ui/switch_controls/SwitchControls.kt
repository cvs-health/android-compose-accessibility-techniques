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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.switch_controls

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BadExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SwitchRow
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val switchControlsHeadingTestTag = "switchControlsHeading"
const val switchControlsExample1HeadingTestTag = "switchControlsExample1Heading"
const val switchControlsExample1CheckboxTestTag = "switchControlsExample1Checkbox"
const val switchControlsExample2HeadingTestTag = "switchControlsExample2Heading"
const val switchControlsExample2CheckboxTestTag = "switchControlsExample2Checkbox"

@Composable
fun SwitchControlsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.switch_controls_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()

        Column(modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.switch_controls_heading),
                modifier = Modifier.testTag(switchControlsHeadingTestTag)
            )
            BodyText(textId = R.string.switch_controls_description_1)
            BodyText(textId = R.string.switch_controls_description_2)

            BadExampleHeading(
                text = stringResource(id = R.string.switch_controls_example_1_header),
                modifier = Modifier.testTag(switchControlsExample1HeadingTestTag)
            )
            Spacer(modifier = Modifier.height(8.dp))
            val (example1Checked, setExample1Checked) = remember { mutableStateOf(false) }
            FauxSwitchRow(
                text = stringResource(id = R.string.switch_controls_example_1_label),
                checked = example1Checked,
                toggleHandler = setExample1Checked,
                modifier = Modifier.testTag(switchControlsExample1CheckboxTestTag)
            )

            GoodExampleHeading(
                text = stringResource(id = R.string.switch_controls_example_2_header),
                modifier = Modifier.testTag(switchControlsExample2HeadingTestTag)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Important technique: Switch state has been hoisted up to this parent composable.
            // The state value and a state mutator function lambda are passed down to the
            // SwitchRow() composable.
            val (example2Checked, setExample2Checked) = remember { mutableStateOf(false) }
            // Key accessibility techniques are described in components/SwitchRow.kt.
            SwitchRow(
                text = stringResource(id = R.string.switch_controls_example_2_label),
                checked = example2Checked,
                toggleHandler = setExample2Checked,
                modifier = Modifier.testTag(switchControlsExample2CheckboxTestTag)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        SwitchControlsScreen {}
    }
}

@Composable
fun FauxSwitchRow(
    text: String,
    checked: Boolean,
    toggleHandler: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    // Key mistake 1: The following Row needs Modifier.toggleable() with role = Role.Switch.
    // That modifier would merge the child semantics, associating the Switch control with its
    // label, as well as making the entire Row a touch target.
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Key mistake #2: Related to mistake #1, this Switch should not do its own
        // onCheckedChange handling; that should be done at the Row level only. (If onCheckedChange
        // handling is also retained here, the Switch would become separately focusable from the
        // Row in assistive technologies like TalkBack and Switch Access.)
        Text(text = text, modifier = Modifier.padding(end = 8.dp))
        Switch(
            checked = checked,
            onCheckedChange = toggleHandler
        )
    }
}