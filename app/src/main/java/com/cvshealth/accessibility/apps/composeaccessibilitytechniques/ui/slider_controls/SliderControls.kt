/*
   © Copyright 2024 CVS Health and/or one of its affiliates. All rights reserved.

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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.slider_controls

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BadExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericSlider
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.ProblematicExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import com.google.android.material.slider.RangeSlider
import kotlin.math.roundToInt

const val sliderControlsHeadingTestTag = "sliderControlsHeading"
const val sliderControlsExample1HeadingTestTag = "sliderControlsExample1Heading"
const val sliderControlsExample1ControlTestTag = "sliderControlsExample1Control"
const val sliderControlsExample2HeadingTestTag = "sliderControlsExample2Heading"
const val sliderControlsExample2ControlTestTag = "sliderControlsExample2Control"
const val sliderControlsExample3HeadingTestTag = "sliderControlsExample3Heading"
const val sliderControlsExample3ControlTestTag = "sliderControlsExample3Control"
const val sliderControlsExample4HeadingTestTag = "sliderControlsExample4Heading"
const val sliderControlsExample4ControlTestTag = "sliderControlsExample4Control"

/**
 * Demonstrate accessibility techniques for [Slider] controls in conformance with WCAG
 * [Success Criterion 1.3.1 Info and Relationships](https://www.w3.org/TR/WCAG22/#info-and-relationships) and [Success Criterion 4.1.2 Name, Role, Value](https://www.w3.org/TR/WCAG22/#name-role-value).
 *
 * Applies [GenericScaffold] to wrap the screen content.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@Composable
fun SliderControlsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.slider_controls_title),
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
                text = stringResource(id = R.string.slider_controls_heading),
                modifier = Modifier.testTag(sliderControlsHeadingTestTag)
            )
            BodyText(textId = R.string.slider_controls_description_1)
            BodyText(textId = R.string.slider_controls_description_2)

            // Slider examples
            BadExample1()
            GoodExample2()

            // RangeSlider examples
            ProblematicExample3()
            ProblematicExample4()

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InteractiveControlLabelsScreenPreview() {
    ComposeAccessibilityTechniquesTheme {
        SliderControlsScreen {}
    }
}


@Composable
private fun BadExample1() {
    // Bad example 1: Unremediated Slider control
    BadExampleHeading(
        text = stringResource(id = R.string.slider_controls_example_1_header),
        modifier = Modifier.testTag(sliderControlsExample1HeadingTestTag)
    )
    BodyText(stringResource(id = R.string.slider_controls_example_1_description))
    Spacer(modifier = Modifier.height(8.dp))

    BodyText(stringResource(id = R.string.slider_controls_example_1_label))

    val (ratingValue, setRatingValue) = remember { mutableFloatStateOf(0.0f) }
    Slider(
        value = ratingValue,
        onValueChange = setRatingValue,
        modifier = Modifier
            .testTag(sliderControlsExample1ControlTestTag),
        valueRange = 0f..10f,
        steps = 9
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
    // Good example 2: Remediated Slider control
    GoodExampleHeading(
        text = stringResource(id = R.string.slider_controls_example_2_header),
        modifier = Modifier.testTag(sliderControlsExample2HeadingTestTag)
    )
    BodyText(textId = R.string.slider_controls_example_2_description)
    Spacer(modifier = Modifier.height(8.dp))

    val (ratingValue, setRatingValue) = remember { mutableFloatStateOf(0.0f) }
    GenericSlider(
        label = stringResource(id = R.string.slider_controls_example_2_label),
        value = ratingValue,
        onValueChange = setRatingValue,
        modifier = Modifier.testTag(sliderControlsExample2ControlTestTag),
        valueRange = 0f..10f,
        steps = 9, // steps between the start and end point (exclusive of both)
        // Announce integer step value as Slider state
        toStateDescription = { value -> value.roundToInt().toString() }
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
private fun ProblematicExample3() {
    // Problematic example 3: Remediated RangeSlider is not keyboard accessible
    ProblematicExampleHeading(
        text = stringResource(id = R.string.slider_controls_example_3_header),
        modifier = Modifier.testTag(sliderControlsExample3HeadingTestTag)
    )
    BodyText(textId = R.string.slider_controls_example_3_description)
    Spacer(modifier = Modifier.height(8.dp))

    BodyText(stringResource(id = R.string.slider_controls_example_3_label))

    val range = 0f..10f
    val steps = 9 // steps between the start and end point (exclusive of both)
    val (ratingFilterRange, setRatingFilterRange) = remember { mutableStateOf(range) }
    val contentDescriptionText = stringResource(
        id = R.string.slider_controls_example_3_content_description
    )
    val stateDescriptionText = stringResource(
        id = R.string.slider_controls_example_3_state_description,
        ratingFilterRange.start.roundToInt(),
        ratingFilterRange.endInclusive.roundToInt()
    )
    RangeSlider(
        value = ratingFilterRange,
        onValueChange = setRatingFilterRange,
        modifier = Modifier
            .testTag(sliderControlsExample3ControlTestTag)
            .semantics {
                // RangeSlider contentDescription must duplicate label text, because
                // RangeSlider does not support a text label, just as Slider does not.
                contentDescription = contentDescriptionText

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
private fun ProblematicExample3Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )  {
            ProblematicExample3()
        }
    }
}

@Composable
private fun ProblematicExample4() {
    // Problematic example 4: View RangeSlider is keyboard inaccessible
    ProblematicExampleHeading(
        text = stringResource(id = R.string.slider_controls_example_4_header),
        modifier = Modifier.testTag(sliderControlsExample4HeadingTestTag)
    )
    BodyText(textId = R.string.slider_controls_example_4_description)
    Spacer(modifier = Modifier.height(8.dp))

    // Key technique: Provide a visible control label
    BodyText(stringResource(id = R.string.slider_controls_example_4_label))

    val range = 0f..10f
    val (ratingFilterRange, setRatingFilterRange) = remember { mutableStateOf(range) }
    val contentDescriptionText = stringResource(
        id = R.string.slider_controls_example_4_content_description
    )
    // Key technique: Wrap a com.google.android.material.slider.RangeSlider in AndroidView.
    // Note: This approach fails to be keyboard accessible, because of a known Compose-View
    // interop issue: see https://issuetracker.google.com/issues/255628260 for details.
    AndroidView(
        modifier = Modifier
            .testTag(sliderControlsExample4ControlTestTag)
            .padding(top = 8.dp),
        factory = { context ->
            RangeSlider(context).apply {
                valueFrom = range.start
                valueTo = range.endInclusive
                stepSize = 1.0f
                values = listOf(range.start, range.endInclusive) // sets the initially selected range

                // Key technique: Label RangeSlider using contentDescription
                contentDescription = contentDescriptionText

                // Key technique: Extract the value of the RangeSlider to MutableState as it changes
                addOnChangeListener { slider: RangeSlider, _: Float, fromUser: Boolean ->
                    if (fromUser) {
                        setRatingFilterRange(slider.values.first() .. slider.values.last())
                    }
                }
            }
        }
    )

    // Display the currently selected range
    val selectedRangeText = stringResource(
        id = R.string.slider_controls_example_4_selected_range,
        ratingFilterRange.start.roundToInt(),
        ratingFilterRange.endInclusive.roundToInt()
    )
    BodyText(selectedRangeText)
}

@Preview(showBackground = true)
@Composable
private fun ProblematicExample4Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )  {
            ProblematicExample4()
        }
    }
}

