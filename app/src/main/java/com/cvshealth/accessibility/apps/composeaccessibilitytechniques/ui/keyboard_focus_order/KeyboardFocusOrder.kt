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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_focus_order

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BadExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.CheckboxRow
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.OkExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.customFocusBorder
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val keyboardFocusOrderHeadingTestTag = "keyboardFocusOrderHeading"
const val keyboardFocusOrderExample1HeadingTestTag = "keyboardFocusOrderExample1Heading"
const val keyboardFocusOrderExample1LayoutTestTag = "keyboardFocusOrderExample1Layout"
const val keyboardFocusOrderExample1Group1Checkbox1aTestTag = "keyboardFocusOrderExample1Group1Checkbox1a"
const val keyboardFocusOrderExample1Group1Checkbox1bTestTag = "keyboardFocusOrderExample1Group1Checkbox1b"
const val keyboardFocusOrderExample1Group2Checkbox2aTestTag = "keyboardFocusOrderExample1Group2Checkbox2a"
const val keyboardFocusOrderExample1Group2Checkbox2bTestTag = "keyboardFocusOrderExample1Group2Checkbox2b"
const val keyboardFocusOrderExample2HeadingTestTag = "keyboardFocusOrderExample2Heading"
const val keyboardFocusOrderExample2LayoutTestTag = "keyboardFocusOrderExample2Layout"
const val keyboardFocusOrderExample2Group1Checkbox1aTestTag = "keyboardFocusOrderExample2Group1Checkbox1a"
const val keyboardFocusOrderExample2Group1Checkbox1bTestTag = "keyboardFocusOrderExample2Group1Checkbox1b"
const val keyboardFocusOrderExample2Group2Checkbox2aTestTag = "keyboardFocusOrderExample2Group2Checkbox2a"
const val keyboardFocusOrderExample2Group2Checkbox2bTestTag = "keyboardFocusOrderExample2Group2Checkbox2b"
const val keyboardFocusOrderExample3HeadingTestTag = "keyboardFocusOrderExample3Heading"
const val keyboardFocusOrderExample3LayoutTestTag = "keyboardFocusOrderExample3Layout"
const val keyboardFocusOrderExample3Group1Checkbox1aTestTag = "keyboardFocusOrderExample3Group1Checkbox1a"
const val keyboardFocusOrderExample3Group1Checkbox1bTestTag = "keyboardFocusOrderExample3Group1Checkbox1b"
const val keyboardFocusOrderExample3Group2Checkbox2aTestTag = "keyboardFocusOrderExample3Group2Checkbox2a"
const val keyboardFocusOrderExample3Group2Checkbox2bTestTag = "keyboardFocusOrderExample3Group2Checkbox2b"
const val keyboardFocusOrderExample4HeadingTestTag = "keyboardFocusOrderExample4Heading"
const val keyboardFocusOrderExample4Button1TestTag = "keyboardFocusOrderExample4Button1"
const val keyboardFocusOrderExample4Button2TestTag = "keyboardFocusOrderExample4Button2"

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun KeyboardFocusOrderScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.keyboard_focus_order_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.keyboard_focus_order_heading),
                modifier = Modifier.testTag(keyboardFocusOrderHeadingTestTag)
            )
            BodyText(textId = R.string.keyboard_focus_order_description_1)
            BodyText(textId = R.string.keyboard_focus_order_description_2)
            BodyText(textId = R.string.keyboard_focus_order_description_3)

            BadExample1()
            OkExample2()
            GoodExample3()
            BadExample4()

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        KeyboardFocusOrderScreen {}
    }
}

@Composable
private fun BadExample1() {
    // Bad example 1: Controls in an illogical focus order
    BadExampleHeading(
        text = stringResource(id = R.string.keyboard_focus_order_example_1_heading),
        modifier = Modifier.testTag(keyboardFocusOrderExample1HeadingTestTag)
    )

    val (example1Checkbox1aValue, setExample1Checkbox1aValue) = remember { mutableStateOf(false) }
    val (example1Checkbox1bValue, setExample1Checkbox1bValue) = remember { mutableStateOf(false) }
    val (example1Checkbox2aValue, setExample1Checkbox2aValue) = remember { mutableStateOf(false) }
    val (example1Checkbox2bValue, setExample1Checkbox2bValue) = remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier = Modifier.testTag(keyboardFocusOrderExample1LayoutTestTag)
    ) {
        val (example1Group1, example1Checkbox1a, example1Checkbox1b, example1Group2, example1Checkbox2a, example1Checkbox2b) = createRefs()
        val verticalGuideline = createGuidelineFromStart(0.5f)
        BodyText(
            textId = R.string.keyboard_focus_order_example_group_1,
            modifier = Modifier
                .constrainAs(example1Group1) {
                    start.linkTo(parent.start)
                }
        )
        BodyText(
            textId = R.string.keyboard_focus_order_example_group_2,
            modifier = Modifier
                .constrainAs(example1Group2) {
                    start.linkTo(verticalGuideline)
                }
        )
        CheckboxRow(
            text = stringResource(id = R.string.keyboard_focus_order_example_group_1_checkbox_a),
            checked = example1Checkbox1aValue,
            toggleHandler = setExample1Checkbox1aValue,
            modifier = Modifier
                .testTag(keyboardFocusOrderExample1Group1Checkbox1aTestTag)
                .constrainAs(example1Checkbox1a) {
                    start.linkTo(parent.start)
                    end.linkTo(verticalGuideline)
                    top.linkTo(example1Group1.bottom)
                }
        )
        CheckboxRow(
            text = stringResource(id = R.string.keyboard_focus_order_example_group_2_checkbox_a),
            checked = example1Checkbox2aValue,
            toggleHandler = setExample1Checkbox2aValue,
            modifier = Modifier
                .testTag(keyboardFocusOrderExample1Group2Checkbox2aTestTag)
                .constrainAs(example1Checkbox2a) {
                    start.linkTo(verticalGuideline)
                    end.linkTo(parent.end)
                    top.linkTo(example1Group2.bottom)
                }
        )
        CheckboxRow(
            text = stringResource(id = R.string.keyboard_focus_order_example_group_1_checkbox_b),
            checked = example1Checkbox1bValue,
            toggleHandler = setExample1Checkbox1bValue,
            modifier = Modifier
                .testTag(keyboardFocusOrderExample1Group1Checkbox1bTestTag)
                .constrainAs(example1Checkbox1b) {
                    start.linkTo(parent.start)
                    end.linkTo(verticalGuideline)
                    top.linkTo(example1Checkbox1a.bottom)
                }
        )
        CheckboxRow(
            text = stringResource(id = R.string.keyboard_focus_order_example_group_2_checkbox_b),
            checked = example1Checkbox2bValue,
            toggleHandler = setExample1Checkbox2bValue,
            modifier = Modifier
                .testTag(keyboardFocusOrderExample1Group2Checkbox2bTestTag)
                .constrainAs(example1Checkbox2b) {
                    start.linkTo(verticalGuideline)
                    end.linkTo(parent.end)
                    top.linkTo(example1Checkbox2a.bottom)
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BadExample1Preview() {
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
@OptIn(ExperimentalComposeUiApi::class)
private fun OkExample2() {
    // OK example 2: Controls forced into a logical focus order with focusProperties
    OkExampleHeading(
        text = stringResource(id = R.string.keyboard_focus_order_example_2_heading),
        modifier = Modifier.testTag(keyboardFocusOrderExample2HeadingTestTag)
    )

    val (example2Checkbox1aValue, setExample2Checkbox1aValue) = remember { mutableStateOf(false) }
    val (example2Checkbox1bValue, setExample2Checkbox1bValue) = remember { mutableStateOf(false) }
    val (example2Checkbox2aValue, setExample2Checkbox2aValue) = remember { mutableStateOf(false) }
    val (example2Checkbox2bValue, setExample2Checkbox2bValue) = remember { mutableStateOf(false) }

    // Key technique: Create FocusRequester references for all relevant controls.
    val (checkbox1a, checkbox1b, checkbox2a, checkbox2b) = remember { FocusRequester.createRefs() }

    ConstraintLayout(
        modifier = Modifier
            .testTag(keyboardFocusOrderExample2LayoutTestTag)
            .semantics {
                // Confine the effects of using accessibility traversalIndex semantics to this layout.
                isTraversalGroup = true
            }
    ) {
        val (example2Group1, example2Checkbox1a, example2Checkbox1b, example2Group2, example2Checkbox2a, example2Checkbox2b) = createRefs()
        val verticalGuideline = createGuidelineFromStart(0.5f)
        BodyText(
            textId = R.string.keyboard_focus_order_example_group_1,
            modifier = Modifier
                .constrainAs(example2Group1) {
                    start.linkTo(parent.start)
                }
                .semantics {
                    // Set accessibility traversal order. Note: This technique is fragile to change.
                    // Also note that the non-focusable composables need this property to insure
                    // the correct reading order in TalkBack.
                    traversalIndex = 1f
                }
        )
        BodyText(
            textId = R.string.keyboard_focus_order_example_group_2,
            modifier = Modifier
                .constrainAs(example2Group2) {
                    start.linkTo(verticalGuideline)
                }
                .semantics {
                    // Set accessibility traversal order. Note: This technique is fragile to change.
                    traversalIndex = 4f
                }
        )
        CheckboxRow(
            text = stringResource(id = R.string.keyboard_focus_order_example_group_1_checkbox_a),
            checked = example2Checkbox1aValue,
            toggleHandler = setExample2Checkbox1aValue,
            modifier = Modifier
                .testTag(keyboardFocusOrderExample2Group1Checkbox1aTestTag)
                .constrainAs(example2Checkbox1a) {
                    start.linkTo(parent.start)
                    end.linkTo(verticalGuideline)
                    top.linkTo(example2Group1.bottom)
                }
                // Key technique: Set a FocusRequester reference on this control.
                .focusRequester(checkbox1a)
                // Key technique: Specify the next and/or previous focusable control in focusProperties.
                .focusProperties { next = checkbox1b }
                .semantics {
                    // Set accessibility traversal order. Note: This technique is fragile to change.
                    traversalIndex = 2f
                }
        )
        CheckboxRow(
            text = stringResource(id = R.string.keyboard_focus_order_example_group_2_checkbox_a),
            checked = example2Checkbox2aValue,
            toggleHandler = setExample2Checkbox2aValue,
            modifier = Modifier
                .testTag(keyboardFocusOrderExample2Group2Checkbox2aTestTag)
                .constrainAs(example2Checkbox2a) {
                    start.linkTo(verticalGuideline)
                    end.linkTo(parent.end)
                    top.linkTo(example2Group2.bottom)
                }
                // Key technique: Set a FocusRequester reference on this control.
                .focusRequester(checkbox2a)
                // Key technique: Specify the next and/or previous focusable control in focusProperties.
                .focusProperties {
                    next = checkbox2b
                    previous = checkbox1b
                }
                .semantics {
                    // Set accessibility traversal order. Note: This technique is fragile to change.
                    traversalIndex = 5f
                }
        )
        CheckboxRow(
            text = stringResource(id = R.string.keyboard_focus_order_example_group_1_checkbox_b),
            checked = example2Checkbox1bValue,
            toggleHandler = setExample2Checkbox1bValue,
            modifier = Modifier
                .testTag(keyboardFocusOrderExample2Group1Checkbox1bTestTag)
                .constrainAs(example2Checkbox1b) {
                    start.linkTo(parent.start)
                    end.linkTo(verticalGuideline)
                    top.linkTo(example2Checkbox1a.bottom)
                }
                // Key technique: Set a FocusRequester reference on this control.
                .focusRequester(checkbox1b)
                // Key technique: Specify the next and/or previous focusable control in focusProperties.
                .focusProperties {
                    next = checkbox2a
                    previous = checkbox1a
                }
                .semantics {
                    // Set accessibility traversal order. Note: This technique is fragile to change.
                    traversalIndex = 3f
                }
        )
        CheckboxRow(
            text = stringResource(id = R.string.keyboard_focus_order_example_group_2_checkbox_b),
            checked = example2Checkbox2bValue,
            toggleHandler = setExample2Checkbox2bValue,
            modifier = Modifier
                .testTag(keyboardFocusOrderExample2Group2Checkbox2bTestTag)
                .constrainAs(example2Checkbox2b) {
                    start.linkTo(verticalGuideline)
                    end.linkTo(parent.end)
                    top.linkTo(example2Checkbox2a.bottom)
                }
                // Key technique: Set a FocusRequester reference on this control.
                .focusRequester(checkbox2b)
                // Key technique: Specify the next and/or previous focusable control in focusProperties.
                .focusProperties { previous = checkbox2a }
                .semantics {
                    // Set accessibility traversal order. Note: This technique is fragile to change.
                    traversalIndex = 6f
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OkExample2Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            OkExample2()
        }
    }
}

@Composable
private fun GoodExample3() {
    // Good example 3: Controls grouped into a logical focus order
    GoodExampleHeading(
        text = stringResource(id = R.string.keyboard_focus_order_example_3_heading),
        modifier = Modifier.testTag(keyboardFocusOrderExample3HeadingTestTag)
    )

    val (example3Checkbox1aValue, setExample3Checkbox1aValue) = remember { mutableStateOf(false) }
    val (example3Checkbox1bValue, setExample3Checkbox1bValue) = remember { mutableStateOf(false) }
    val (example3Checkbox2aValue, setExample3Checkbox2aValue) = remember { mutableStateOf(false) }
    val (example3Checkbox2bValue, setExample3Checkbox2bValue) = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.testTag(keyboardFocusOrderExample3LayoutTestTag)
    ) {
        // Key technique: In this case, simply organizing the checkboxes into the composition tree
        // in the desired focus order is sufficient. Default focus order proceeds depth-first,
        // accessing the first Column, its CheckboxRows in order, and then the second Column and its
        // CheckboxRows in order. See [Focus in Compose](https://developer.android.com/jetpack/compose/touch-input/focus).
        //
        // In other cases, adding Modifier.focusGroup() to a layout composable may be necessary. See
        // [Change focus behavior](https://developer.android.com/jetpack/compose/touch-input/focus/change-focus-behavior).
        Column(
            modifier = Modifier
                .weight(1f)
                .semantics {
                    // Note: Mark this column so it will act as a unit in accessibility traversal order.
                    isTraversalGroup = true
                }
        ) {
            BodyText(textId = R.string.keyboard_focus_order_example_group_1)
            CheckboxRow(
                text = stringResource(id = R.string.keyboard_focus_order_example_group_1_checkbox_a),
                checked = example3Checkbox1aValue,
                toggleHandler = setExample3Checkbox1aValue,
                modifier = Modifier.testTag(keyboardFocusOrderExample3Group1Checkbox1aTestTag)
            )
            CheckboxRow(
                text = stringResource(id = R.string.keyboard_focus_order_example_group_1_checkbox_b),
                checked = example3Checkbox1bValue,
                toggleHandler = setExample3Checkbox1bValue,
                modifier = Modifier.testTag(keyboardFocusOrderExample3Group1Checkbox1bTestTag)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .semantics {
                    // Note: Mark this column so it will act as a unit in accessibility traversal order.
                    isTraversalGroup = true
                }
        ) {
            BodyText(textId = R.string.keyboard_focus_order_example_group_2)
            CheckboxRow(
                text = stringResource(id = R.string.keyboard_focus_order_example_group_2_checkbox_a),
                checked = example3Checkbox2aValue,
                toggleHandler = setExample3Checkbox2aValue,
                modifier = Modifier.testTag(keyboardFocusOrderExample3Group2Checkbox2aTestTag)
            )
            CheckboxRow(
                text = stringResource(id = R.string.keyboard_focus_order_example_group_2_checkbox_b),
                checked = example3Checkbox2bValue,
                toggleHandler = setExample3Checkbox2bValue,
                modifier = Modifier.testTag(keyboardFocusOrderExample3Group2Checkbox2bTestTag)
            )
        }
    }
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun BadExample4() {
    // Bad example 4: A keyboard focus trap
    BadExampleHeading(
        text = stringResource(id = R.string.keyboard_focus_order_example_4_heading),
        modifier = Modifier.testTag(keyboardFocusOrderExample4HeadingTestTag)
    )

    BodyText(textId = R.string.keyboard_focus_order_example_4_description)

    Spacer(modifier = Modifier.height(8.dp))

    // Key technique: Create FocusRequester references for all relevant controls.
    val (button1, button2) = remember { FocusRequester.createRefs() }

    val context = LocalContext.current
    val button1Message = stringResource(id = R.string.keyboard_focus_order_example_4_buttton_1_message)
    Button(
        onClick = {
            Toast.makeText(context, button1Message, Toast.LENGTH_LONG).show()
        },
        modifier = Modifier
            .testTag(keyboardFocusOrderExample4Button1TestTag)
            // Key technique: Set a FocusRequester reference on this control.
            .focusRequester(button1)
            // Key technique: Specify the next and/or previous focusable control in focusProperties.
            .focusProperties {
                // Key error: This button sets its next and previous keyboard focus targets to
                // itself, not to another control.
                next = button1
                previous = button1
            }
            .customFocusBorder()
    ) {
        Text(text = stringResource(id = R.string.keyboard_focus_order_example_4_button_1))
    }

    val button2Message = stringResource(id = R.string.keyboard_focus_order_example_4_buttton_2_message)
    Button(
        onClick = {
            Toast.makeText(context, button2Message, Toast.LENGTH_LONG).show()
        },
        modifier = Modifier
            .testTag(keyboardFocusOrderExample4Button2TestTag)
            // Key technique: Set a FocusRequester reference on this control.
            .focusRequester(button2)
            .customFocusBorder()
    ) {
        Text(text = stringResource(id = R.string.keyboard_focus_order_example_4_button_2))
    }
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
