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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_actions

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.OkExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val keyboardActionsHeadingTestTag = "keyboardActionsHeading"
const val keyboardActionsExample1HeadingTestTag = "keyboardActionsExample1Heading"
const val keyboardActionsExample1TextFieldTestTag = "keyboardActionsExample1TextField"
const val keyboardActionsExample2HeadingTestTag = "keyboardActionsExample2Heading"
const val keyboardActionsExample2TextFieldTestTag = "keyboardActionsExample2TextField"
const val keyboardActionsExample3HeadingTestTag = "keyboardActionsExample3Heading"
const val keyboardActionsExample3TextFieldTestTag = "keyboardActionsExample3TextField"
const val keyboardActionsExample4HeadingTestTag = "keyboardActionsExample4Heading"
const val keyboardActionsExample4TextFieldTestTag = "keyboardActionsExample4TextField"
const val keyboardActionsExample5HeadingTestTag = "keyboardActionsExample5Heading"
const val keyboardActionsExample5TextFieldTestTag = "keyboardActionsExample5TextField"
const val keyboardActionsExample6HeadingTestTag = "keyboardActionsExample6Heading"
const val keyboardActionsExample6TextFieldTestTag = "keyboardActionsExample6TextField"


@Composable
fun KeyboardActionsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.keyboard_actions_title),
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
                text = stringResource(id = R.string.keyboard_actions_heading),
                modifier = Modifier.testTag(keyboardActionsHeadingTestTag)
            )
            BodyText(textId = R.string.keyboard_actions_description_1)
            BodyText(textId = R.string.keyboard_actions_description_2)

            OkExample1()
            GoodExample2()
            GoodExample3()
            GoodExample4()
            GoodExample5()
            GoodExample6()

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme() {
        KeyboardActionsScreen {}
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun OkExample1() {
    // OK example 1: Keyboard action None adds a newline
    OkExampleHeading(
        text = stringResource(id = R.string.keyboard_actions_example_1_header),
        modifier = Modifier.testTag(keyboardActionsExample1HeadingTestTag)
    )
    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    AutofilledOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        autofillType = AutofillType.PersonFullName,
        modifier = Modifier
            .testTag(keyboardActionsExample1TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_types_example_8_label))
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Words,
            keyboardType = KeyboardType.Text,
            // Key technique 1: Use ImeAction.None when a field should have no keyboard action.
            // Instead there is a virtual keyboard button for the Enter/Return key, which adds a
            // newline to the text.
            imeAction = ImeAction.None
        )
        // Note: No keyboardActions property is required in this case.
    )
}

@Preview(showBackground = true)
@Composable
private fun OkExample1Preview() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            OkExample1()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun GoodExample2() {
    // Good example 2: Keyboard action Next moves to next field
    GoodExampleHeading(
        text = stringResource(id = R.string.keyboard_actions_example_2_header),
        modifier = Modifier.testTag(keyboardActionsExample2HeadingTestTag)
    )
    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    AutofilledOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        autofillType = AutofillType.PostalAddress,
        modifier = Modifier
            .testTag(keyboardActionsExample2TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_actions_example_2_label))
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            // Key technique 2: Use ImeAction.Next when a field should advance to the next field on Enter.
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                // Note: This operation is generally performed entirely within the architectural
                // View layer (as is done here) and does not involve any ViewModel, but your
                // application may vary.
                focusManager.moveFocus(FocusDirection.Down)
            }
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExample2Preview() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample2()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun GoodExample3() {
    // Good example 3: Keyboard action Done submits the form
    GoodExampleHeading(
        text = stringResource(id = R.string.keyboard_actions_example_3_header),
        modifier = Modifier.testTag(keyboardActionsExample3HeadingTestTag)
    )

    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val doneMessage = stringResource(id = R.string.keyboard_actions_example_3_done_message)

    AutofilledOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        autofillType = AutofillType.PostalCode,
        modifier = Modifier
            .testTag(keyboardActionsExample3TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_actions_example_3_label))
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            // Key technique 3: Use ImeAction.Done when a field should submit the form.
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                // Note: Typically, the ViewModel would be invoked here to submit the form data.
                Toast.makeText(context, doneMessage, Toast.LENGTH_LONG).show()
            }
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExample3Preview() {
    ComposeAccessibilityTechniquesTheme() {
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
private fun GoodExample4() {
    // Good example 4: Keyboard action Send sends a message
    GoodExampleHeading(
        text = stringResource(id = R.string.keyboard_actions_example_4_header),
        modifier = Modifier.testTag(keyboardActionsExample4HeadingTestTag)
    )

    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val sendMessage = stringResource(id = R.string.keyboard_actions_example_4_send_message)

    AccessibleOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        modifier = Modifier
            .testTag(keyboardActionsExample4TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_actions_example_4_label))
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            // Key technique 4: Use ImeAction.Send when a field should send a message.
            imeAction = ImeAction.Send
        ),
        keyboardActions = KeyboardActions(
            onSend = {
                keyboardController?.hide()
                // Note: Typically, the ViewModel would be invoked here to send the field/form data.
                Toast.makeText(context, sendMessage, Toast.LENGTH_LONG).show()
            }
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExample4Preview() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample4()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun GoodExample5() {
    // Good example 5: Keyboard action Search submits a query
    GoodExampleHeading(
        text = stringResource(id = R.string.keyboard_actions_example_5_header),
        modifier = Modifier.testTag(keyboardActionsExample5HeadingTestTag)
    )

    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val searchMessage = stringResource(id = R.string.keyboard_actions_example_5_search_message)

    AccessibleOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        modifier = Modifier
            .testTag(keyboardActionsExample5TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_actions_example_5_label))
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            // Key technique 5: Use ImeAction.Search when a field should submit a search query.
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                // Note: Typically, the ViewModel would be invoked here to submit the field/form data.
                Toast.makeText(context, searchMessage, Toast.LENGTH_LONG).show()
            }
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExample5Preview() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample5()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun GoodExample6() {
    // Good example 6: Keyboard action Go opens a new view
    // In this case, the Go soft key pops up a message containing a phone number (and would
    // ordinarily dial that number). Other common uses include URL input fields that open a browser.
    GoodExampleHeading(
        text = stringResource(id = R.string.keyboard_actions_example_6_header),
        modifier = Modifier.testTag(keyboardActionsExample6HeadingTestTag)
    )

    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val goMessage = stringResource(id = R.string.keyboard_actions_example_6_go_message, exampleText)

    AutofilledOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        autofillType = AutofillType.PhoneNumberNational,
        modifier = Modifier
            .testTag(keyboardActionsExample6TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_actions_example_6_label))
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            // Key technique 6: Use ImeAction.Go when a field should open a new view using its value.
            imeAction = ImeAction.Go
        ),
        keyboardActions = KeyboardActions(
            onGo = {
                keyboardController?.hide()
                // Note: Typically, the ViewModel would be invoked here to submit the field/form data.
                Toast.makeText(context, goMessage, Toast.LENGTH_LONG).show()
            }
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExample6Preview() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample6()
        }
    }
}
