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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels

import android.view.KeyEvent
import android.view.KeyEvent.ACTION_UP
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
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
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.OkExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.ProblematicExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.RadioButtonGroup
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SnackbarLauncher
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SwitchRow
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.VisibleFocusBorderButton
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import kotlin.math.roundToInt

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
const val interactiveControlLabelsExample7ControlTestTag = "interactiveControlLabelsExample7Control"
const val interactiveControlLabelsExample8HeadingTestTag = "interactiveControlLabelsExample8Heading"
const val interactiveControlLabelsExample8ControlTestTag = "interactiveControlLabelsExample8Control"
const val interactiveControlLabelsExample9HeadingTestTag = "interactiveControlLabelsExample9Heading"
const val interactiveControlLabelsExample9ControlTestTag = "interactiveControlLabelsExample9Control"
const val interactiveControlLabelsExample10HeadingTestTag = "interactiveControlLabelsExample10Heading"
const val interactiveControlLabelsExample10ControlTestTag = "interactiveControlLabelsExample10Control"
const val interactiveControlLabelsExample11HeadingTestTag = "interactiveControlLabelsExample11Heading"
const val interactiveControlLabelsExample11ControlTestTag = "interactiveControlLabelsExample11Control"
const val interactiveControlLabelsExample12HeadingTestTag = "interactiveControlLabelsExample12Heading"
const val interactiveControlLabelsExample12ControlTestTag = "interactiveControlLabelsExample12Control"

/**
 * Demonstrate accessibility techniques for labeling interactive controls in conformance with WCAG
 * [Success Criterion 1.3.1 Info and Relationships](https://www.w3.org/TR/WCAG22/#info-and-relationships).
 *
 * Applies [GenericScaffold] to wrap the screen content. Hosts Snackbars.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@Composable
fun InteractiveControlLabelsScreen(
    onBackPressed: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarLauncher = SnackbarLauncher(rememberCoroutineScope(), snackbarHostState)

    GenericScaffold(
        title = stringResource(id = R.string.interactive_control_labels_title),
        onBackPressed = onBackPressed,
        snackbarHost = { SnackbarHost(snackbarHostState) }
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

            // TextField examples
            BadExample1()
            GoodExample2()

            // Checkbox examples
            BadExample3()
            GoodExample4()

            // Switch examples
            BadExample5(columnScope = this)
            GoodExample6(columnScope = this)

            // RadioButton examples
            BadExample7()
            GoodExample8()

            // Button example
            GoodExample9(snackbarLauncher)

            // Slider examples
            BadExample10()
            OkExample11()

            // RangeSlider example
            ProblematicExample12()

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InteractiveControlLabelsScreenPreview() {
    ComposeAccessibilityTechniquesTheme {
        InteractiveControlLabelsScreen {}
    }
}

@Composable
private fun BadExample1() {
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
    val (example1Text, setExample1Text) = remember { mutableStateOf("") }
    AccessibleTextField(
        value = example1Text,
        onValueChange = setExample1Text,
        modifier = Modifier
            .testTag(interactiveControlLabelsExample1ControlTestTag)
            .fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun BadExample1Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )  {
            BadExample1()
        }
    }
}

@Composable
private fun GoodExample2() {
    // Good example 2: TextField automatically associates label and uses as hint
    GoodExampleHeading(
        text = stringResource(id = R.string.interactive_control_labels_example_2),
        modifier = Modifier.testTag(interactiveControlLabelsExample2HeadingTestTag)
    )
    val (example2Text, setExample2Text) = remember { mutableStateOf("") }
    AccessibleOutlinedTextField(
        value = example2Text,
        onValueChange = setExample2Text,
        modifier = Modifier
            .testTag(interactiveControlLabelsExample2ControlTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.interactive_control_labels_associated_textinput_label))
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExample2Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )  {
            GoodExample2()
        }
    }
}

@Composable
private fun BadExample3() {
    // Bad example 3: CheckBox without associated field label
    BadExampleHeading(
        text = stringResource(id = R.string.interactive_control_labels_example_3),
        modifier = Modifier.testTag(interactiveControlLabelsExample3HeadingTestTag)
    )
    val (example3CheckboxValue, setExample3CheckboxValue) = remember { mutableStateOf(false) }
    FauxCheckboxRow(
        text = stringResource(id = R.string.interactive_control_labels_unassociated_checkbox_label),
        checked = example3CheckboxValue,
        toggleHandler = setExample3CheckboxValue,
        modifier = Modifier.testTag(interactiveControlLabelsExample3ControlTestTag)
    )
}

@Preview(showBackground = true)
@Composable
private fun BadExample3Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )  {
            BadExample3()
        }
    }
}

@Composable
private fun GoodExample4() {
    // Good example 4: CheckBox with associated field label
    GoodExampleHeading(
        text = stringResource(id = R.string.interactive_control_labels_example_4),
        modifier = Modifier.testTag(interactiveControlLabelsExample4HeadingTestTag)
    )
    val (example4CheckboxValue, setExample4CheckboxValue) = remember { mutableStateOf(false) }
    CheckboxRow(
        text = stringResource(id = R.string.interactive_control_labels_associated_checkbox_label),
        checked = example4CheckboxValue,
        toggleHandler = setExample4CheckboxValue,
        modifier = Modifier.testTag(interactiveControlLabelsExample4ControlTestTag)
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExample4Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )  {
            GoodExample4()
        }
    }
}

@Composable
private fun BadExample5(columnScope: ColumnScope){
    // Bad example 5: Switch without associated field label
    BadExampleHeading(
        text = stringResource(id = R.string.interactive_control_labels_example_5),
        modifier = Modifier.testTag(interactiveControlLabelsExample5HeadingTestTag)
    )
    val (example5SwitchValue, setExample5SwitchValue) = remember { mutableStateOf(false) }
    columnScope.apply {
        FauxSwitchRow(
            text = stringResource(id = R.string.interactive_control_labels_unassociated_switch_label),
            checked = example5SwitchValue,
            toggleHandler = setExample5SwitchValue,
            modifier = Modifier
                .testTag(interactiveControlLabelsExample5ControlTestTag)
                .align(alignment = Alignment.End)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BadExample5Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )  {
            BadExample5(columnScope = this)
        }
    }
}

@Composable
private fun GoodExample6(columnScope: ColumnScope) {
    // Good example 6: Switch with associated field label
    GoodExampleHeading(
        text = stringResource(id = R.string.interactive_control_labels_example_6),
        modifier = Modifier.testTag(interactiveControlLabelsExample6HeadingTestTag)
    )
    val (example6SwitchValue, setExample6SwitchValue) = remember { mutableStateOf(false) }
    columnScope.apply {
        SwitchRow(
            text = stringResource(id = R.string.interactive_control_labels_associated_switch_label),
            checked = example6SwitchValue,
            toggleHandler = setExample6SwitchValue,
            modifier = Modifier
                .testTag(interactiveControlLabelsExample6ControlTestTag)
                .align(alignment = Alignment.End)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GoodExample6Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )  {
            GoodExample6(columnScope = this)
        }
    }
}

@Composable
private fun BadExample7() {
    // Bad example 7: RadioButton group without associated field labels, etc.
    BadExampleHeading(
        text = stringResource(id = R.string.interactive_control_labels_example_7),
        modifier = Modifier.testTag(interactiveControlLabelsExample7HeadingTestTag)
    )
    Spacer(modifier = Modifier.height(8.dp))
    val (example7SelectedRadioGroupOption, setExample7RadioGroupOption) = remember {
        mutableIntStateOf(
            0
        )
    }
    FauxRadioButtonGroup(
        groupLabel = stringResource(id = R.string.interactive_control_labels_radio_button_group_label),
        itemLabels = listOf(
            stringResource(id = R.string.interactive_control_labels_unassociated_radio_button_label_1),
            stringResource(id = R.string.interactive_control_labels_unassociated_radio_button_label_2)
        ),
        selectedIndex = example7SelectedRadioGroupOption,
        selectHandler = setExample7RadioGroupOption,
        modifier = Modifier.testTag(interactiveControlLabelsExample7ControlTestTag)
    )
}

@Preview(showBackground = true)
@Composable
private fun BadExample7Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )  {
            BadExample7()
        }
    }
}

@Composable
private fun GoodExample8() {
    // Good example 8: RadioButton group with associated field labels, etc.
    GoodExampleHeading(
        text = stringResource(id = R.string.interactive_control_labels_example_8),
        modifier = Modifier.testTag(interactiveControlLabelsExample8HeadingTestTag)
    )
    Spacer(modifier = Modifier.height(8.dp))
    val (example8SelectedRadioGroupOption, setExample8RadioGroupOption) = remember {
        mutableIntStateOf(
            0
        )
    }
    RadioButtonGroup(
        groupLabel = stringResource(id = R.string.interactive_control_labels_radio_button_group_label),
        itemLabels = listOf(
            stringResource(id = R.string.interactive_control_labels_associated_radio_button_label_1),
            stringResource(id = R.string.interactive_control_labels_associated_radio_button_label_2)
        ),
        selectedIndex = example8SelectedRadioGroupOption,
        selectHandler = setExample8RadioGroupOption,
        modifier = Modifier.testTag(interactiveControlLabelsExample8ControlTestTag)
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExample8Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )  {
            GoodExample8()
        }
    }
}

@Composable
private fun GoodExample9(
    snackbarLauncher: SnackbarLauncher?
) {
    // Good example 9: Button with associated label
    GoodExampleHeading(
        text = stringResource(id = R.string.interactive_control_labels_example_9),
        modifier = Modifier.testTag(interactiveControlLabelsExample9HeadingTestTag)
    )
    Spacer(modifier = Modifier.height(8.dp))
    // Key Technique: Button() composable content is its programmatically-associated label
    val buttonMessage =
        stringResource(id = R.string.interactive_control_labels_associated_button_message)
    VisibleFocusBorderButton(
        onClick = {
            snackbarLauncher?.show(buttonMessage)
        },
        modifier = Modifier
            .testTag(interactiveControlLabelsExample9ControlTestTag)
    ) {
        Text(
            text = stringResource(id = R.string.interactive_control_labels_associated_button_label)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GoodExample9Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )  {
            GoodExample9(snackbarLauncher = null)
        }
    }
}

@Composable
private fun BadExample10() {
    // Bad example 10: Slider without associated field label
    BadExampleHeading(
        text = stringResource(id = R.string.interactive_control_labels_example_10),
        modifier = Modifier.testTag(interactiveControlLabelsExample10HeadingTestTag)
    )
    Spacer(modifier = Modifier.height(8.dp))
    val slider1LabelText = stringResource(
        id = R.string.interactive_control_labels_unassociated_slider_label
    )
    BodyText(slider1LabelText)
    val (slider1Value, setSlider1Value) = remember { mutableFloatStateOf(0.0f) }
    Slider(
        value = slider1Value,
        onValueChange = setSlider1Value,
        modifier = Modifier
            .testTag(interactiveControlLabelsExample10ControlTestTag),
        valueRange = 0f..10f,
        steps = 9
    )
}

@Preview(showBackground = true)
@Composable
private fun BadExample10Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )  {
            BadExample10()
        }
    }
}

@Composable
private fun OkExample11() {
    // OK example 11: Slider with associated field label
    OkExampleHeading(
        text = stringResource(id = R.string.interactive_control_labels_example_11),
        modifier = Modifier.testTag(interactiveControlLabelsExample11HeadingTestTag)
    )
    BodyText(textId = R.string.interactive_control_labels_example_11_description)
    Spacer(modifier = Modifier.height(8.dp))

    BodyText(textId = R.string.interactive_control_labels_associated_slider_label)
    val (slider2Value, setSlider2Value) = remember { mutableFloatStateOf(0.0f) }
    val slider2ContentDescription = stringResource(
        id = R.string.interactive_control_labels_associated_slider_content_description
    )
    val range = 0f..10f
    val steps = 9 // steps between the start and end point (exclusive of both)
    val increment = (range.endInclusive - range.start) / (steps + 1)
    Slider(
        value = slider2Value,
        onValueChange = setSlider2Value,
        modifier = Modifier
            .testTag(interactiveControlLabelsExample11ControlTestTag)
            // Key technique: Allow the left and right arrow keys to adjust the slider value
            // provided the resulting value is within the slider's range; otherwise, allow
            // normal arrow key navigation to apply.
            .onKeyEvent { keyEvent ->
                when (keyEvent.nativeKeyEvent.keyCode) {
                    KeyEvent.KEYCODE_DPAD_LEFT -> {
                        // Have to absorb both the DPAD_LEFT key DOWN and UP events, because
                        // otherwise screen navigation captures key DOWN and the key UP
                        // event is never received.
                        if (range.contains(slider2Value - increment)) {
                            if (keyEvent.nativeKeyEvent.action == ACTION_UP) {
                                setSlider2Value(slider2Value - increment)
                            }
                            true
                        } else {
                            false
                        }
                    }

                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        if (keyEvent.nativeKeyEvent.action == ACTION_UP &&
                            range.contains(slider2Value + increment)
                        ) {
                            setSlider2Value(slider2Value + increment)
                            true
                        } else {
                            false
                        }
                    }

                    else -> {
                        false
                    }
                }
            }
            .semantics {
                // Key technique: Slider contentDescription must duplicate label text,
                // because Slider does not support a text label. (See
                // https://issuetracker.google.com/issues/236988201.)
                // However, contentDescription can extend the label text.
                contentDescription = slider2ContentDescription

                // stateDescription replaces the default "xx percent" text for a Slider.
                stateDescription = slider2Value
                    .roundToInt()
                    .toString()

                // liveRegion announces the Slider's state when its value changes.
                liveRegion = LiveRegionMode.Polite
            },
        valueRange = range,
        steps = steps
    )
}

@Preview(showBackground = true)
@Composable
private fun OkExample11Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )  {
            OkExample11()
        }
    }
}

@Composable
private fun ProblematicExample12() {
    // Problematic example 12: RangeSlider with associated field label
    ProblematicExampleHeading(
        text = stringResource(id = R.string.interactive_control_labels_example_12),
        modifier = Modifier.testTag(interactiveControlLabelsExample12HeadingTestTag)
    )
    BodyText(textId = R.string.interactive_control_labels_example_12_description)
    Spacer(modifier = Modifier.height(8.dp))

    BodyText(stringResource(id = R.string.interactive_control_labels_associated_rangeslider_label))

    val range = 0f..10f
    val steps = 9 // steps between the start and end point (exclusive of both)
    val (rangeSlider2Value, setRangeSlider2Value) = remember { mutableStateOf(range) }
    val rangeSlider2ContentDescription = stringResource(
        id = R.string.interactive_control_labels_associated_slider_content_description
    )
    val stateDescriptionText = stringResource(
        id = R.string.interactive_control_labels_associated_rangeslider_state_description,
        rangeSlider2Value.start.roundToInt(),
        rangeSlider2Value.endInclusive.roundToInt()
    )
    RangeSlider(
        value = rangeSlider2Value,
        onValueChange = setRangeSlider2Value,
        modifier = Modifier
            .testTag(interactiveControlLabelsExample12ControlTestTag)
            .semantics {
                // RangeSlider contentDescription must duplicate label text, because
                // RangeSlider does not support a text label, just as Slider does not.
                contentDescription = rangeSlider2ContentDescription

                // stateDescription adds the selected range value to a RangeSlider.
                stateDescription = stateDescriptionText

                // liveRegion announces the RangeSlider's state when its value changes.
                liveRegion = LiveRegionMode.Polite
            },
        steps = steps,
        valueRange = range
    )
}

@Preview(showBackground = true)
@Composable
private fun ProblematicExample12Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )  {
            ProblematicExample12()
        }
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
 *
 * @param text the [Checkbox] control's label string
 * @param checked the current [Checkbox] state
 * @param toggleHandler callback for toggling the [Checkbox]
 * @param modifier optional [Modifier] for the [Checkbox] control's layout [Row]
 */
@Composable
private fun FauxCheckboxRow(
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
private fun FauxCheckboxRowPreview() {
    ComposeAccessibilityTechniquesTheme {
        val (checkBoxValue, setCheckboxValue) = remember { mutableStateOf(false) }
        FauxCheckboxRow(
            text = "Test Checkbox",
            checked = checkBoxValue,
            toggleHandler = setCheckboxValue
        )
    }
}

/**
 * Displays a deliberately inaccessible [Switch] control. This control fails to properly label the
 * [Switch] composable in the following ways:
 * 1. The [Row] layout does not apply Modifier.toggleable() with role=Role.Switch and row-level
 * click handling.
 * 2. Nor does the [Row] apply semantics(mergeDescendants = true), which is automatic with a
 * toggleable() click handler, so the [Text] label is not programmatically-associated with the
 * [Switch].
 * 3. The [Switch] composable itself performs the onClick handling, so it is a separate, unlabeled
 * focusable element, leading to a confusing user experience.
 *
 * Also, the tappable area of the [Switch] is fragile, because appropriate padding is only added
 * automatically by Compose since the [Switch] is clickable.
 *
 * @param text the [Switch] control's label string
 * @param checked the current [Switch] state
 * @param toggleHandler callback for toggling the [Switch]
 * @param modifier optional [Modifier] for the [Switch] control's layout [Row]
 */
@Composable
private fun FauxSwitchRow(
    text: String,
    checked: Boolean,
    toggleHandler: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 2.dp),
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
private fun FauxSwitchRowPreview() {
    ComposeAccessibilityTechniquesTheme {
        val (switchValue, setSwitchValue) = remember { mutableStateOf(false) }
        FauxSwitchRow(
            text = "Test Switch",
            checked = switchValue,
            toggleHandler = setSwitchValue
        )
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
        itemLabels.forEachIndexed { index: Int, label: String ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (selectedIndex == index),
                    onClick = { selectHandler(index) }
                )
                Text(text = label, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FauxRadioGroupPreview() {
    val options = listOf( "Banana", "Grape", "Orange")
    val (selectedOption, setSelectedOption) = remember { mutableIntStateOf(0) }
    ComposeAccessibilityTechniquesTheme {
        FauxRadioButtonGroup(
            groupLabel = "Pick a fruit:",
            itemLabels = options,
            selectedIndex = selectedOption,
            selectHandler = setSelectedOption
        )
    }
}