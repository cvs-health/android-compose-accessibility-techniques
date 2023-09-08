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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.AccessibleOutlinedTextField
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BadExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val keyboardTypesHeadingTestTag = "keyboardTypesHeading"
const val keyboardTypesExample1HeadingTestTag = "keyboardTypesExample1Heading"
const val keyboardTypesExample1TextFieldTestTag = "keyboardTypesExample1TextField"
const val keyboardTypesExample2HeadingTestTag = "keyboardTypesExample2Heading"
const val keyboardTypesExample2TextFieldTestTag = "keyboardTypesExample2TextField"
const val keyboardTypesExample3HeadingTestTag = "keyboardTypesExample3Heading"
const val keyboardTypesExample3TextFieldTestTag = "keyboardTypesExample3TextField"
const val keyboardTypesExample4HeadingTestTag = "keyboardTypesExample4Heading"
const val keyboardTypesExample4TextFieldTestTag = "keyboardTypesExample4TextField"
const val keyboardTypesExample5HeadingTestTag = "keyboardTypesExample5Heading"
const val keyboardTypesExample5TextFieldTestTag = "keyboardTypesExample5TextField"
const val keyboardTypesExample6HeadingTestTag = "keyboardTypesExample6Heading"
const val keyboardTypesExample6TextFieldTestTag = "keyboardTypesExample6TextField"
const val keyboardTypesExample7HeadingTestTag = "keyboardTypesExample7Heading"
const val keyboardTypesExample7TextFieldTestTag = "keyboardTypesExample7TextField"
const val keyboardTypesExample8HeadingTestTag = "keyboardTypesExample8Heading"
const val keyboardTypesExample8TextFieldTestTag = "keyboardTypesExample8TextField"
const val keyboardTypesExample9HeadingTestTag = "keyboardTypesExample9Heading"
const val keyboardTypesExample9TextFieldTestTag = "keyboardTypesExample9TextField"
const val keyboardTypesExample10HeadingTestTag = "keyboardTypesExample10Heading"
const val keyboardTypesExample10TextFieldTestTag = "keyboardTypesExample10TextField"
const val keyboardTypesExample11HeadingTestTag = "keyboardTypesExample11Heading"
const val keyboardTypesExample11TextFieldTestTag = "keyboardTypesExample11TextField"
const val keyboardTypesExample12HeadingTestTag = "keyboardTypesExample12Heading"
const val keyboardTypesExample12TextFieldTestTag = "keyboardTypesExample12TextField"
const val keyboardTypesExample13HeadingTestTag = "keyboardTypesExample13Heading"
const val keyboardTypesExample13TextFieldTestTag = "keyboardTypesExample13TextField"
const val keyboardTypesExample14HeadingTestTag = "keyboardTypesExample14Heading"
const val keyboardTypesExample14TextFieldTestTag = "keyboardTypesExample14TextField"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KeyboardTypesScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.keyboard_types_title),
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
                text = stringResource(id = R.string.keyboard_types_heading),
                modifier = Modifier.testTag(keyboardTypesHeadingTestTag)
            )
            BodyText(textId = R.string.keyboard_types_description_1)
            BodyText(textId = R.string.keyboard_types_description_2)

            BadExample1()
            BadExample2()
            GoodExample3()
            GoodExample4()
            GoodExample5()
            GoodExample6()
            GoodExample7()
            GoodExample8()
            GoodExample9()
            GoodExample10()
            GoodExample11()
            GoodExample12()
            GoodExample13()
            GoodExample14()

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme() {
        KeyboardTypesScreen {}
    }
}

@Composable
private fun BadExample1() {
    // Bad example 1: Numeric keyboard type prevents text data entry
    BadExampleHeading(
        text = stringResource(id = R.string.keyboard_types_example_1_header),
        modifier = Modifier.testTag(keyboardTypesExample1HeadingTestTag)
    )
    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    AccessibleOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        modifier = Modifier
            .testTag(keyboardTypesExample1TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_types_example_1_label))
        },
        // Key mistake: should use KeyboardType.Text.
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Decimal
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun BadExample1Preview() {
    ComposeAccessibilityTechniquesTheme() {
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
private fun BadExample2() {
    // Bad example 2: Keyboard type (Decimal) allows most data entry, but is not
    // specific to the Phone field data type
    BadExampleHeading(
        text = stringResource(id = R.string.keyboard_types_example_2_header),
        modifier = Modifier.testTag(keyboardTypesExample2HeadingTestTag)
    )
    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    AccessibleOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        modifier = Modifier
            .testTag(keyboardTypesExample2TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_types_example_2_label))
        },
        // Key mistake: should use KeyboardType.Phone.
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Decimal
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun BadExample2Preview() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            BadExample2()
        }
    }
}

@Composable
private fun GoodExample3() {
    // Good example 3: Keyboard type for phone number (Phone)
    GoodExampleHeading(
        text = stringResource(id = R.string.keyboard_types_example_3_header),
        modifier = Modifier.testTag(keyboardTypesExample3HeadingTestTag)
    )
    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    AccessibleOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        modifier = Modifier
            .testTag(keyboardTypesExample3TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_types_example_3_label))
        },
        // Key technique: use KeyboardType.Phone to enter phone numbers.
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Phone
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

@Composable
private fun GoodExample4() {
    // Good example 4: Keyboard type for whole number (Number)
    GoodExampleHeading(
        text = stringResource(id = R.string.keyboard_types_example_4_header),
        modifier = Modifier.testTag(keyboardTypesExample4HeadingTestTag)
    )
    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    AccessibleOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        modifier = Modifier
            .testTag(keyboardTypesExample4TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_types_example_4_label))
        },
        // Key technique: use KeyboardType.Number to enter whole numbers.
        // Note: This keyboard type itself will not force only a whole number to be entered, it only
        // restricts the soft (virtual) keyboard to show numbers and some punctuation symbols.
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
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

@Composable
private fun GoodExample5() {
    // Good example 5: Keyboard type for decimal number (Decimal)
    GoodExampleHeading(
        text = stringResource(id = R.string.keyboard_types_example_5_header),
        modifier = Modifier.testTag(keyboardTypesExample5HeadingTestTag)
    )
    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    AccessibleOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        modifier = Modifier
            .testTag(keyboardTypesExample5TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_types_example_5_label))
        },
        // Key technique: use KeyboardType.Decimal to enter decimal numbers.
        // Note: This keyboard type itself will not force the insertion of a decimal value, only
        // restrict the soft (virtual) keyboard to show numbers and some punctuation symbols.
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Decimal
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

@Composable
private fun GoodExample6() {
    // Good example 6: Keyboard type for generic text (Text)
    GoodExampleHeading(
        text = stringResource(id = R.string.keyboard_types_example_6_header),
        modifier = Modifier.testTag(keyboardTypesExample6HeadingTestTag)
    )
    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    AccessibleOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        modifier = Modifier
            .testTag(keyboardTypesExample6TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_types_example_6_label))
        },
        // Key technique: use KeyboardType.Text to enter text
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text
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

@Composable
private fun GoodExample7() {
    // Good example 7: Keyboard type for full ASCII text (Ascii)
    GoodExampleHeading(
        text = stringResource(id = R.string.keyboard_types_example_7_header),
        modifier = Modifier.testTag(keyboardTypesExample7HeadingTestTag)
    )
    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    AccessibleOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        modifier = Modifier
            .testTag(keyboardTypesExample7TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_types_example_7_label))
        },
        // Key technique: use KeyboardType.Ascii to enter ASCII text
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Ascii
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExample7Preview() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample7()
        }
    }
}

@Composable
private fun GoodExample8() {
    // Good example 8: Keyboard options for word-capitalized text (e.g., names)
    GoodExampleHeading(
        text = stringResource(id = R.string.keyboard_types_example_8_header),
        modifier = Modifier.testTag(keyboardTypesExample8HeadingTestTag)
    )
    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    AccessibleOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        modifier = Modifier
            .testTag(keyboardTypesExample8TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_types_example_8_label))
        },
        // Key techniques: use KeyboardType.Text to enter Text, use KeyboardCapitalization.Words
        // for initial-letter capitalization on all words. (Note: This capitalization convention may
        // not apply to names in all languages.)
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Words,
            keyboardType = KeyboardType.Text
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExample8Preview() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample8()
        }
    }
}

@Composable
private fun GoodExample9() {
    // Good example 9: Keyboard options for sentences with autocorrect
    GoodExampleHeading(
        text = stringResource(id = R.string.keyboard_types_example_9_header),
        modifier = Modifier.testTag(keyboardTypesExample9HeadingTestTag)
    )
    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    AccessibleOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        modifier = Modifier
            .testTag(keyboardTypesExample9TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_types_example_9_label))
        },
        // Key techniques: use KeyboardType.Text to enter Text, use KeyboardCapitalization.Sentences
        // for sentence-case capitalization, and autoCorrect=true (default) to apply auto-correct.
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrect = true, // autocorrect is the default, so no real need to set its value here.
            keyboardType = KeyboardType.Text
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExample9Preview() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample9()
        }
    }
}

@Composable
private fun GoodExample10() {
    // Good example 10: Keyboard options for sentences without autocorrect
    GoodExampleHeading(
        text = stringResource(id = R.string.keyboard_types_example_10_header),
        modifier = Modifier.testTag(keyboardTypesExample10HeadingTestTag)
    )
    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    AccessibleOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        modifier = Modifier
            .testTag(keyboardTypesExample10TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_types_example_10_label))
        },
        // Key techniques: use KeyboardType.Text to enter Text, use KeyboardCapitalization.Sentences
        // for sentence-case capitalization, and autoCorrect=false to turn off auto-correct.
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrect = false, // autocorrect is the default, must be set explicitly to turn off
            keyboardType = KeyboardType.Text
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExample10Preview() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample10()
        }
    }
}

@Composable
private fun GoodExample11() {
    // Good example 11: Keyboard type for email (Email)
    GoodExampleHeading(
        text = stringResource(id = R.string.keyboard_types_example_11_header),
        modifier = Modifier.testTag(keyboardTypesExample11HeadingTestTag)
    )
    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    AccessibleOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        modifier = Modifier
            .testTag(keyboardTypesExample11TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_types_example_11_label))
        },
        // Key technique: use KeyboardType.Email to enter email addresses.
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExample11Preview() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample11()
        }
    }
}

@Composable
private fun GoodExample12() {
    // Good example 12: Keyboard type for URL (Uri)
    GoodExampleHeading(
        text = stringResource(id = R.string.keyboard_types_example_12_header),
        modifier = Modifier.testTag(keyboardTypesExample12HeadingTestTag)
    )
    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    AccessibleOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        modifier = Modifier
            .testTag(keyboardTypesExample12TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_types_example_12_label))
        },
        // Key technique: use KeyboardType.Uri to enter URLs.
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Uri
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExample12Preview() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample12()
        }
    }
}

@Composable
private fun GoodExample13() {
    // Good example 13: Keyboard options for text passwords (Password)
    GoodExampleHeading(
        text = stringResource(id = R.string.keyboard_types_example_13_header),
        modifier = Modifier.testTag(keyboardTypesExample13HeadingTestTag)
    )
    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    AccessibleOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        modifier = Modifier
            .testTag(keyboardTypesExample13TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_types_example_13_label))
        },
        // Key techniques: use KeyboardType.Password to enter text passwords, use
        // visualTransformation = PasswordVisualTransformation() to do masking.
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExample13Preview() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample13()
        }
    }
}

@Composable
private fun GoodExample14() {
    // Good example 14: Keyboard type for numeric passwords (NumberPassword)
    GoodExampleHeading(
        text = stringResource(id = R.string.keyboard_types_example_14_header),
        modifier = Modifier.testTag(keyboardTypesExample14HeadingTestTag)
    )
    val (exampleText, setExampleText) = remember { mutableStateOf("") }
    AccessibleOutlinedTextField(
        value = exampleText,
        onValueChange = setExampleText,
        modifier = Modifier
            .testTag(keyboardTypesExample14TextFieldTestTag)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        label = {
            Text(text = stringResource(id = R.string.keyboard_types_example_14_label))
        },
        // Key techniques: use KeyboardType.NumberPassword to enter numeric passwords, use
        // visualTransformation = PasswordVisualTransformation() to do masking.
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.NumberPassword
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExample14Preview() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample14()
        }
    }
}