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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.textfield_controls

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.AccessibleOutlinedTextField
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val textFieldControlsHeadingTestTag = "textFieldControlsHeading"
const val textFieldControlsExample1HeadingTestTag = "textFieldControlsExample1Heading"
const val textFieldControlsExample1TextFieldTestTag = "textFieldControlsExample1TextField"
const val textFieldControlsExample1ButtonTestTag = "textFieldControlsExample1Button"

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldControlsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.textfield_controls_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.textfield_controls_heading),
                modifier = Modifier.testTag(textFieldControlsHeadingTestTag)
            )
            BodyText(textId = R.string.textfield_controls_description_1)
            BodyText(textId = R.string.textfield_controls_description_2)

            GoodExample1()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        TextFieldControlsScreen {}
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun GoodExample1() {
    // Good example 1: Accessible required TextField
    // Also, illustrates one approach to required TextField error handling.
    GoodExampleHeading(
        text = stringResource(id = R.string.textfield_controls_example_1_header),
        modifier = Modifier.testTag(textFieldControlsExample1HeadingTestTag)
    )

    val (name, setName) = remember { mutableStateOf("") }
    // Note: using external error state avoids marking the (initially empty) Name field as in error
    // until a submit occurs. Setting TextField.isError via a boolean expression would be simpler.
    val (isError, setIsError) = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val shortErrorMessage =
        if (isError) stringResource(id = R.string.textfield_controls_example_1_short_error) else ""
    val longErrorMessage =
        if (isError) stringResource(id = R.string.textfield_controls_example_1_long_error) else ""

    // Key technique 1: Apply fixes for keyboard Tab handling; see AccessibleTextFields.kt for
    // details.
    AccessibleOutlinedTextField(
        value = name,
        onValueChange = { newName ->
            // Note: Clear any error message as soon as the field has a value. Set the error
            // state only on submit.
            if (newName.isNotBlank()) {
                setIsError(false)
            }
            setName(newName)
        },
        modifier = Modifier
            .testTag(textFieldControlsExample1TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth()
            .semantics {
                // Note: Announce errors. TalkBack prepends the text "Error: " to the
                // announcements, so use a different error string than the supportingText.
                //
                // Note also that TextField presently announces another annoying message,
                // "Error: Invalid input", whenever isError is true. See TextFieldImpl.kt,
                // CommonDecorationBox() for details.
                if (isError) error(shortErrorMessage)
            },
        label = {
            Text(text = stringResource(id = R.string.textfield_controls_example_1_label))
        },
        supportingText = {
            // Note: Display a visual error message using supportingText. In this case, the
            // text should begin with "Error: " to indicate the type of message shown.
            //
            // Also, because the Modifier.semantics error property is set, the
            // supportingText must be hidden from accessibility services (with
            // invisibleToUser() semantics) to prevent duplicate announcements.
            // This is hacky, but improves the audio-semantic user experience.
            if (isError) {
                Text(
                    text = longErrorMessage,
                    modifier = Modifier.semantics { invisibleToUser() }
                )
            }
        },
        // Note: Signal display of the error state by the TextField.
        isError = isError,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            // Key technique 3: Use ImeAction.Done when a field should submit the form.
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                // Key technique 4: Supply an onDone handler that does the same things as
                // the button onClick handler.
                onFormSubmit(context, name, setIsError)
            }
        )
    ) {
        // Key technique 5: To handle the Enter key as Done, supply an onEnterHandler that
        // does the same things as the onDone and button onClick handler. See
        // AccessibleTextFields.kt for how this parameter is added to the TextField.
        onFormSubmit(context, name, setIsError)
    }

    Button(
        onClick = {
            onFormSubmit(context, name, setIsError)
        },
        modifier = Modifier.testTag(textFieldControlsExample1ButtonTestTag)
    ) {
        Text(stringResource(id = R.string.textfield_controls_example_1_button))
    }
}

private fun onFormSubmit(context: Context, name: String, setIsError: (Boolean) -> Unit) {
    // Note: Typically, the ViewModel would be invoked here to validate and submit the form data.
    setIsError(name.isBlank())
    if (name.isNotBlank()) {
        val message = context.getString(R.string.textfield_controls_example_1_message, name)
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}

@Preview(showBackground = true)
@Composable
fun GoodExample1Preview() {
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