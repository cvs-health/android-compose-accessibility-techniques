/*
   Copyright 2024-2025 CVS Health and/or one of its affiliates

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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.view_interop

import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.databinding.ViewExposedDropdownMenuBinding
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import com.google.android.material.slider.RangeSlider
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import kotlin.math.roundToInt

// Note: Given that Compose UI tests fail badly with View Interop of Material Design Controls,
// these test tags are debatable value.
const val viewInteropHeadingTestTag = "viewInteropHeading"
const val viewInteropExample1HeadingTestTag = "viewInteropExample1Heading"
const val viewInteropExample1ControlTestTag = "viewInteropExample1Control"
const val viewInteropExample2HeadingTestTag = "viewInteropExample2Heading"
const val viewInteropExample2ControlTestTag = "viewInteropExample2Control"

/**
 * Demonstrate accessibility techniques for [AndroidView]-wrapped controls.
 *
 * Applies [GenericScaffold] to wrap the screen content.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@Composable
fun ViewInteropScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.view_interop_title),
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
                text = stringResource(id = R.string.view_interop_heading),
                modifier = Modifier.testTag(viewInteropHeadingTestTag)
            )
            BodyText(textId = R.string.view_interop_description_1)
            BodyText(textId = R.string.view_interop_description_2)

            // Exposed Dropdown Menu Pattern example
            GoodExample1()

            // RangeSlider example
            GoodExample2()

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InteractiveControlLabelsScreenPreview() {
    ComposeAccessibilityTechniquesTheme {
        ViewInteropScreen {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoodExample1() {
    // Good example 1: View non-editable Exposed Dropdown Menu
    GoodExampleHeading(
        text = stringResource(id = R.string.view_interop_example_1_heading),
        modifier = Modifier.testTag(viewInteropExample1HeadingTestTag)
    )
    BodyText(textId = R.string.view_interop_example_1_description)
    Spacer(modifier = Modifier.height(8.dp))

    val example1Options = listOf(
        stringResource(id = R.string.view_interop_example_1_option_1),
        stringResource(id = R.string.view_interop_example_1_option_2),
        stringResource(id = R.string.view_interop_example_1_option_3),
        stringResource(id = R.string.view_interop_example_1_option_4),
        stringResource(id = R.string.view_interop_example_1_option_5),
    )
    var selectedItemText1 by remember { mutableStateOf("") }

    // Key technique: Wrap a View-based Exposed Dropdown Menu pattern control ensemble in an
    // AndroidView (or in this case, AndroidViewBinding to use XML layout). See also
    // layout/view_exposed_dropdown_menu.xml.
    AndroidViewBinding(
        factory = ViewExposedDropdownMenuBinding::inflate,
        modifier = Modifier
            .testTag(viewInteropExample1ControlTestTag)
            .fillMaxSize()
            .focusable() // allows user to scroll the screen to this control using Tab key
    ) {
        val context = this.root.context
        val autoCompleteAdapter = ArrayAdapter(
            context,
            R.layout.list_item_dropdown,
            example1Options
        )

        // Apply the adapter to the MaterialAutoCompleteTextView in the TextInputLayout
        val autoCompleteTextView =
            (exposedDropdownMenusExample2Layout.editText as? MaterialAutoCompleteTextView)
        autoCompleteTextView?.setAdapter(autoCompleteAdapter)

        // Set an item click listener on the AutoCompleteTextView
        autoCompleteTextView?.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position: Int, _ ->
                selectedItemText1 = example1Options[position]
            }
        // Note: Do not call autoCompleteTextView.setText(selectedValue) as you would with
        // Compose; the MaterialAutoCompleteTextView maintains its own state, and setting it
        // will limit the values displayed in the dropdown menu list.
    }
}

@Preview(showBackground = true)
@Composable
private fun GoodExample1Preview() {
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
    // Good example 2: View RangeSlider is accessible
    GoodExampleHeading(
        text = stringResource(id = R.string.view_interop_example_2_header),
        modifier = Modifier.testTag(viewInteropExample2HeadingTestTag)
    )
    BodyText(textId = R.string.view_interop_example_2_description_1)
    BodyText(textId = R.string.view_interop_example_2_description_2)
    Spacer(modifier = Modifier.height(8.dp))

    // Key technique: Provide a visible control label
    BodyText(stringResource(id = R.string.view_interop_example_2_label))

    val range = 0f..10f
    val (ratingFilterRange, setRatingFilterRange) = remember { mutableStateOf(range) }
    val contentDescriptionText = stringResource(
        id = R.string.view_interop_example_2_content_description
    )
    // Key technique: Wrap a com.google.android.material.slider.RangeSlider in AndroidView.
    AndroidView(
        modifier = Modifier
            .testTag(viewInteropExample2ControlTestTag)
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
        id = R.string.view_interop_example_2_selected_range,
        ratingFilterRange.start.roundToInt(),
        ratingFilterRange.endInclusive.roundToInt()
    )
    BodyText(selectedRangeText)
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

