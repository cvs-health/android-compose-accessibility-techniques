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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.autofill_controls

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.AccessibleOutlinedTextField
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.AutofilledOutlinedTextField
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BadExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val autofillControlsHeadingTestTag = "autofillControlsHeading"
const val autofillControlsExample1HeadingTestTag = "autofillControlsExample1Heading"
const val autofillControlsExample1TextField1TestTag = "autofillControlsExample1TextField1"
const val autofillControlsExample1TextField2TestTag = "autofillControlsExample1TextField2"
const val autofillControlsExample2HeadingTestTag = "autofillControlsExample2Heading"
const val autofillControlsExample2TextField1TestTag = "autofillControlsExample2TextField1"
const val autofillControlsExample2TextField2TestTag = "autofillControlsExample2TextField2"

/**
 * Demonstrate techniques regarding Autofill controls, specifically selecting the correct autofill
 * [ContentType] for a control. See [AutofilledOutlinedTextField] for key techniques to implement
 * autofill controls themselves.
 *
 * Applies [GenericScaffold] to wrap the screen content.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@Composable
fun AutofillControlsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.autofill_title),
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
                text = stringResource(id = R.string.autofill_heading),
                modifier = Modifier.testTag(autofillControlsHeadingTestTag)
            )
            BodyText(textId = R.string.autofill_description_1)
            BodyText(textId = R.string.autofill_description_2)

            BadExample1()
            GoodExample2()

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        AutofillControlsScreen {}
    }
}

@Composable
private fun BadExample1() {
    // Bad example 1: Non-autofilled name and email TextFields
    BadExampleHeading(
        text = stringResource(id = R.string.autofill_example_1_heading),
        modifier = Modifier.testTag(autofillControlsExample1HeadingTestTag)
    )

    // Key issue: Uses a non-autofilled TextField for autofillable data (full name).
    val (name, setName) = remember { mutableStateOf("") }
    AccessibleOutlinedTextField(
        value = name,
        onValueChange = setName,
        modifier = Modifier
            .testTag(autofillControlsExample1TextField1TestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.autofill_example_1_label_1))
        },
        // Key technique: use KeyboardType.Text and KeyboardCapitalization.Words to enter names.
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        )
    )

    // Key issue: Uses a non-autofilled TextField for autofillable data (email).
    val (email, setEmail) = remember { mutableStateOf("") }
    AccessibleOutlinedTextField(
        value = email,
        onValueChange = setEmail,
        modifier = Modifier
            .testTag(autofillControlsExample1TextField2TestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.autofill_example_1_label_2))
        },
        // Technique: use KeyboardType.Email to enter email addresses.
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
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
        ) {
            BadExample1()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun GoodExample2() {
    // Good example 2: Autofilled name and email TextFields
    GoodExampleHeading(
        text = stringResource(id = R.string.autofill_example_2_heading),
        modifier = Modifier.testTag(autofillControlsExample2HeadingTestTag)
    )

    // See AccessibleTextFields.kt for key techniques to implement autofill.
    val (name, setName) = remember { mutableStateOf("") }
    AutofilledOutlinedTextField(
        value = name,
        onValueChange = setName,
        // Key technique: Apply the appropriate autofill type(s) to a TextField.
        autofillContentType = ContentType.PersonFullName,
        modifier = Modifier
            .testTag(autofillControlsExample2TextField1TestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.autofill_example_2_label_1))
        },
        // Key technique: use KeyboardType.Text and KeyboardCapitalization.Words to enter names.
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        )
    )

    val (email, setEmail) = remember { mutableStateOf("") }
    AutofilledOutlinedTextField(
        value = email,
        onValueChange = setEmail,
        // Key technique: Apply the appropriate autofill type(s) to a TextField.
        autofillContentType = ContentType.EmailAddress,
        modifier = Modifier
            .testTag(autofillControlsExample2TextField2TestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.autofill_example_2_label_2))
        },
        // Key technique: use KeyboardType.Email to enter email addresses.
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
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
        ) {
            GoodExample2()
        }
    }
}

