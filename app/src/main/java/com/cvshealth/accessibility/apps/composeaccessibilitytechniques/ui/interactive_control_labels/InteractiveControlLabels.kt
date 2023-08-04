package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.AccessibleOutlinedTextField
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.AccessibleTextField
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BadExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.CheckboxRow
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.RadioGroup
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SwitchRow
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val interactiveControlLabelsHeadingTestTag = "interactiveControlLabelsHeading"
const val interactiveControlLabelsExample1HeadingTestTag = "interactiveControlLabelsExample1Heading"
const val interactiveControlLabelsExample1ControlTestTag = "interactiveControlLabelsExample1Control"
const val interactiveControlLabelsExample2HeadingTestTag = "interactiveControlLabelsExample2Heading"
const val interactiveControlLabelsExample2ControlTestTag = "interactiveControlLabelsExample2Control"
const val interactiveControlLabelsExample3HeadingTestTag = "interactiveControlLabelsExample3Heading"
const val interactiveControlLabelsExample3ControlTestTag = "interactiveControlLabelsExample3Control"
const val interactiveControlLabelsExample4HeadingTestTag = "interactiveControlLabelsExample4Heading"
const val interactiveControlLabelsExample4ControlTestTag = "interactiveControlLabelsExample4Control"
const val interactiveControlLabelsExample5HeadingTestTag = "interactiveControlLabelsExample5Heading"
const val interactiveControlLabelsExample5ControlTestTag = "interactiveControlLabelsExample5Control"
const val interactiveControlLabelsExample6HeadingTestTag = "interactiveControlLabelsExample6Heading"
const val interactiveControlLabelsExample6ControlTestTag = "interactiveControlLabelsExample6Control"
const val interactiveControlLabelsExample7HeadingTestTag = "interactiveControlLabelsExample7Heading"
const val interactiveControlLabelsExample8HeadingTestTag = "interactiveControlLabelsExample8Heading"
const val interactiveControlLabelsExample9HeadingTestTag = "interactiveControlLabelsExample9Heading"
const val interactiveControlLabelsExample9ControlTestTag = "interactiveControlLabelsExample9Control"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractiveControlLabelsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.interactive_control_labels_title),
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
                text = stringResource(id = R.string.interactive_control_labels_heading),
                modifier = Modifier.testTag(interactiveControlLabelsHeadingTestTag)
            )
            BodyText(textId = R.string.interactive_control_labels_description)

            // Bad example 1: TextField without associated field label
            BadExampleHeading(
                text = stringResource(id = R.string.interactive_control_labels_example_1),
                modifier = Modifier.testTag(interactiveControlLabelsExample1HeadingTestTag)
            )
            Text(
                text = stringResource(id = R.string.interactive_control_labels_unassociated_textfield_label),
                modifier = Modifier.padding(top = 8.dp),
                fontWeight = FontWeight.Medium
            )
            val example1Text = remember { mutableStateOf("") }
            AccessibleTextField(
                value = example1Text.value,
                onValueChange = { example1Text.value = it },
                modifier = Modifier
                    .testTag(interactiveControlLabelsExample1ControlTestTag)
                    .fillMaxWidth()
            )

            // Good example 2: TextField automatically associates label and uses as hint
            GoodExampleHeading(
                text = stringResource(id = R.string.interactive_control_labels_example_2),
                modifier = Modifier.testTag(interactiveControlLabelsExample2HeadingTestTag)
            )
            val example2Text = remember { mutableStateOf("") }
            AccessibleOutlinedTextField(
                value = example2Text.value,
                onValueChange = { example2Text.value = it },
                modifier = Modifier
                    .testTag(interactiveControlLabelsExample2ControlTestTag)
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                label = {
                    Text(text = stringResource(id = R.string.interactive_control_labels_associated_textinput_label))
                }
            )

            // Bad example 3: CheckBox without associated field label
            BadExampleHeading(
                text = stringResource(id = R.string.interactive_control_labels_example_3),
                modifier = Modifier.testTag(interactiveControlLabelsExample3HeadingTestTag)
            )
            val example3CheckboxState = remember { mutableStateOf(false) }
            FauxCheckboxRow(
                text = stringResource(id = R.string.interactive_control_labels_unassociated_checkbox_label),
                checked = example3CheckboxState.value,
                toggleHandler = { newState ->
                    example3CheckboxState.value = newState
                },
                modifier = Modifier.testTag(interactiveControlLabelsExample3ControlTestTag)
            )

            // Good example 4: CheckBox with associated field label
            GoodExampleHeading(
                text = stringResource(id = R.string.interactive_control_labels_example_4),
                modifier = Modifier.testTag(interactiveControlLabelsExample4HeadingTestTag)
            )
            val example4CheckboxState = remember { mutableStateOf(false) }
            CheckboxRow(
                text = stringResource(id = R.string.interactive_control_labels_associated_checkbox_label),
                checked = example4CheckboxState.value,
                toggleHandler = { newState ->
                    example4CheckboxState.value = newState
                },
                modifier = Modifier.testTag(interactiveControlLabelsExample4ControlTestTag)
            )

            // Bad example 5: Switch without associated field label
            BadExampleHeading(
                text = stringResource(id = R.string.interactive_control_labels_example_5),
                modifier = Modifier.testTag(interactiveControlLabelsExample5HeadingTestTag)
            )
            val example5SwitchState = remember { mutableStateOf(false) }
            FauxSwitchRow(
                text = stringResource(id = R.string.interactive_control_labels_unassociated_switch_label),
                checked = example5SwitchState.value,
                toggleHandler = { newState ->
                    example5SwitchState.value = newState
                },
                modifier = Modifier
                    .testTag(interactiveControlLabelsExample5ControlTestTag)
                    .align(alignment = Alignment.End)
            )

            // Good example 6: Switch with associated field label
            GoodExampleHeading(
                text = stringResource(id = R.string.interactive_control_labels_example_6),
                modifier = Modifier.testTag(interactiveControlLabelsExample6HeadingTestTag)
            )
            val example6SwitchState = remember { mutableStateOf(false) }
            SwitchRow(
                text = stringResource(id = R.string.interactive_control_labels_associated_switch_label),
                checked = example6SwitchState.value,
                toggleHandler = { newState ->
                    example6SwitchState.value = newState
                },
                modifier = Modifier
                    .testTag(interactiveControlLabelsExample6ControlTestTag)
                    .align(alignment = Alignment.End)
            )

            // Bad example 7: RadioButton group without associated field labels, etc.
            BadExampleHeading(
                text = stringResource(id = R.string.interactive_control_labels_example_7),
                modifier = Modifier.testTag(interactiveControlLabelsExample7HeadingTestTag)
            )
            Spacer(modifier = Modifier.height(8.dp))
            val example7SelectedRadioGroupOption = remember { mutableStateOf(0) }
            FauxRadioGroup(
                groupLabel = stringResource(id = R.string.interactive_control_labels_radio_button_group_label),
                itemLabels = listOf(
                    stringResource(id = R.string.interactive_control_labels_unassociated_radio_button_label_1),
                    stringResource(id = R.string.interactive_control_labels_unassociated_radio_button_label_2)
                ),
                current = example7SelectedRadioGroupOption.value,
                selectHandler = { example7SelectedRadioGroupOption.value = it }
            )

            // Good example 8: RadioButton group with associated field labels, etc.
            GoodExampleHeading(
                text = stringResource(id = R.string.interactive_control_labels_example_8),
                modifier = Modifier.testTag(interactiveControlLabelsExample8HeadingTestTag)
            )
            Spacer(modifier = Modifier.height(8.dp))
            val example8SelectedRadioGroupOption = remember { mutableStateOf(0) }
            RadioGroup(
                groupLabel = stringResource(id = R.string.interactive_control_labels_radio_button_group_label),
                itemLabels = listOf(
                    stringResource(id = R.string.interactive_control_labels_associated_radio_button_label_1),
                    stringResource(id = R.string.interactive_control_labels_associated_radio_button_label_2)
                ),
                current = example8SelectedRadioGroupOption.value,
                selectHandler = { example8SelectedRadioGroupOption.value = it }
            )

            // Good example 9: Button with associated label
            GoodExampleHeading(
                text = stringResource(id = R.string.interactive_control_labels_example_9),
                modifier = Modifier.testTag(interactiveControlLabelsExample9HeadingTestTag)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Key Technique: Button() composable content is its programmatically-associated label
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.testTag(interactiveControlLabelsExample9ControlTestTag)
            ) {
                Text(
                    text = stringResource(id = R.string.interactive_control_labels_associated_button_label)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InteractiveControlLabelsScreenPreview() {
    ComposeAccessibilityTechniquesTheme() {
        InteractiveControlLabelsScreen() {}
    }
}

/**
 * FauxCheckboxRow fails to properly labels a Checkbox composable in the following ways:
 * 1. The Row() does not apply Modifier.toggleable() with role=Role.Checkbox and row-level click
 *    handling.
 * 2. Nor does the Row apply semantics(mergeDescendants = true), which is automatic with a
 *    toggleable() click handler, so the Text() label is not programmatically-associated with the
 *    checkbox().
 * 3. The Checkbox() composable itself performs the onClick handling, so it is a separate, unlabeled
 *    focusable element, leading to a confusing user experience.
 *
 * Also, the tappable area of the Checkbox is fragile, because appropriate padding is only added
 * automatically by Compose since the Switch is clickable.
 */
@Composable
fun FauxCheckboxRow(
    text: String,
    checked: Boolean,
    toggleHandler: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = toggleHandler
        )
        Text(text = text, modifier = Modifier.padding(end = 12.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun FauxCheckboxRowPreview() {
    ComposeAccessibilityTechniquesTheme() {
        val checkboxState = remember { mutableStateOf(false) }
        FauxCheckboxRow(
            text = "Test Checkbox",
            checked = checkboxState.value,
            toggleHandler = { newState ->
                checkboxState.value = newState
            }
        )
    }
}

/**
 * FauxSwitchRow fails to properly label a Switch composable in the following ways:
 * 1. The Row() does not apply Modifier.toggleable() with role=Role.Switch and row-level click
 *    handling.
 * 2. Nor does the Row apply semantics(mergeDescendants = true), which is automatic with a
 *    toggleable() click handler, so the Text() label is not programmatically-associated with the
 *    Switch().
 * 3. The Switch() composable itself performs the onClick handling, so it is a separate, unlabeled
 *    focusable element, leading to a confusing user experience.
 *
 * Also, the tappable area of the Switch is fragile, because appropriate padding is only added
 * automatically by Compose since the Switch is clickable.
 */
@Composable
fun FauxSwitchRow(
    text: String,
    checked: Boolean,
    toggleHandler: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, modifier = Modifier.padding(end = 8.dp))
        Switch(
            checked = checked,
            onCheckedChange = toggleHandler
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FauxSwitchRowPreview() {
    ComposeAccessibilityTechniquesTheme() {
        val switchState = remember { mutableStateOf(false) }
        FauxSwitchRow(
            text = "Test Switch",
            checked = switchState.value,
            toggleHandler = { newState ->
                switchState.value = newState
            }
        )
    }
}

/**
 * FauxRadioGroup fails to implement proper accessibility semantics in the following ways:
 * 1. The Column is not marked as an selectableGroup().
 * 2. The Row is not selectable() with Role.RadioButton. This means the Text() and RadioButton() are
 *    not programmatically-associated and makes the touch target smaller.
 * 3. The RadioButton selection click handler is non-null; this should be handled by the Row-level
 *    selectable().
 * Also, the tap target size of the RadioButton is fragile: it is automatically expanded to 48x48 dp
 * by Compose, only because it is clickable.
 */
@Composable
fun FauxRadioGroup(
    groupLabel: String,
    itemLabels: List<String>,
    current: Int,
    selectHandler: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(groupLabel)
        itemLabels.forEachIndexed { index: Int, label: String ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (current == index),
                    onClick = { selectHandler(index) }
                )
                Text(text = label, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FauxRadioGroupPreview() {
    val options = listOf( "Banana", "Grape", "Orange")
    val selectedOption = remember { mutableStateOf(0) }
    ComposeAccessibilityTechniquesTheme() {
        FauxRadioGroup(
            groupLabel = "Pick a fruit:",
            itemLabels = options,
            current = selectedOption.value,
            selectHandler = { selectedOption.value = it }
        )
    }
}