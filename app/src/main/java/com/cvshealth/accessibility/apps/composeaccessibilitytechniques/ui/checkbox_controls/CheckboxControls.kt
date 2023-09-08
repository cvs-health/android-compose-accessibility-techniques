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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.checkbox_controls

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
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
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.CheckboxRow
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val checkboxControlsHeadingTestTag = "checkboxControlsHeading"
const val checkboxControlsExample1HeadingTestTag = "checkboxControlsExample1Heading"
const val checkboxControlsExample1CheckboxTestTag = "checkboxControlsExample1Checkbox"
const val checkboxControlsExample2HeadingTestTag = "checkboxControlsExample2Heading"
const val checkboxControlsExample2CheckboxTestTag = "checkboxControlsExample2Checkbox"

@Composable
fun CheckboxControlsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.checkbox_controls_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()

        Column(modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.checkbox_controls_heading),
                modifier = Modifier.testTag(checkboxControlsHeadingTestTag)
            )
            BodyText(textId = R.string.checkbox_controls_description_1)
            BodyText(textId = R.string.checkbox_controls_description_2)

            BadExampleHeading(
                text = stringResource(id = R.string.checkbox_controls_example_1_header),
                modifier = Modifier.testTag(checkboxControlsExample1HeadingTestTag)
            )
            Spacer(modifier = Modifier.height(8.dp))
            val (example1Checked, setExample1Checked) = remember { mutableStateOf(false) }
            FauxCheckboxRow(
                text = stringResource(id = R.string.checkbox_controls_example_1_label),
                checked = example1Checked,
                toggleHandler = setExample1Checked,
                modifier = Modifier.testTag(checkboxControlsExample1CheckboxTestTag)
            )

            GoodExampleHeading(
                text = stringResource(id = R.string.checkbox_controls_example_2_header),
                modifier = Modifier.testTag(checkboxControlsExample2HeadingTestTag)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Important technique: Checkbox state has been hoisted up to this parent composable.
            // The state value and a state mutator function lambda are passed down to the
            // CheckboxRow() composable.
            val (example2Checked, setExample2Checked) = remember { mutableStateOf(false) }
            // Key accessibility techniques are described in components/CheckboxRow.kt.
            CheckboxRow(
                text = stringResource(id = R.string.checkbox_controls_example_2_label),
                checked = example2Checked,
                toggleHandler = setExample2Checked,
                modifier = Modifier.testTag(checkboxControlsExample2CheckboxTestTag)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        CheckboxControlsScreen {}
    }
}

@Composable
fun FauxCheckboxRow(
    text: String,
    checked: Boolean,
    toggleHandler: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    // Key mistake 1: The following Row needs Modifier.toggleable() with role = Role.Checkbox.
    // That modifier would merge the child semantics, associating the Checkbox control with its
    // label, as well as making the entire Row a touch target.
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Key mistake #2: Related to mistake #1, this Checkbox should not do its own
        // onCheckedChange handling; that should be done at the Row level only. (If onCheckedChange
        // handling is also retained here, the Checkbox would become separately focusable from the
        // Row in assistive technologies like TalkBack and Switch Access.)
        Checkbox(
            checked = checked,
            onCheckedChange = toggleHandler
        )
        Text(text = text, modifier = Modifier.padding(end = 12.dp))
    }
}