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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_focus_indicators

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.ProblematicExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SnackbarLauncher
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.VisibleFocusBorderButton
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.VisibleFocusBorderIconButton
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.VisibleFocusIndication
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.visibleCardFocusBorder
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val customFocusIndicatorsHeadingTestTag = "customFocusIndicatorsHeading"
const val customFocusIndicatorsExample1HeadingTestTag = "customFocusIndicatorsExample1Heading"
const val customFocusIndicatorsExample1ButtonTestTag = "customFocusIndicatorsExample1Button"
const val customFocusIndicatorsExample2HeadingTestTag = "customFocusIndicatorsExample2Heading"
const val customFocusIndicatorsExample2ButtonTestTag = "customFocusIndicatorsExample2Button"
const val customFocusIndicatorsExample3HeadingTestTag = "customFocusIndicatorsExample3Heading"
const val customFocusIndicatorsExample3ButtonTestTag = "customFocusIndicatorsExample3Button"
const val customFocusIndicatorsExample4HeadingTestTag = "customFocusIndicatorsExample4Heading"
const val customFocusIndicatorsExample4ButtonTestTag = "customFocusIndicatorsExample4Button"
const val customFocusIndicatorsExample5HeadingTestTag = "customFocusIndicatorsExample5Heading"
const val customFocusIndicatorsExample5CardTestTag = "customFocusIndicatorsExample5Card"
const val customFocusIndicatorsExample6HeadingTestTag = "customFocusIndicatorsExample6Heading"
const val customFocusIndicatorsExample6CardTestTag = "customFocusIndicatorsExample6Card"
const val customFocusIndicatorsExample7HeadingTestTag = "customFocusIndicatorsExample7Heading"
const val customFocusIndicatorsExample7CardTestTag = "customFocusIndicatorsExample7Card"

/**
 * Demonstrate accessibility techniques for customizing keyboard focus indicators.
 *
 * Applies [GenericScaffold] to wrap the screen content. Hosts Snackbars.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@Composable
fun CustomFocusIndicatorsScreen(
    onBackPressed: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarLauncher = SnackbarLauncher(rememberCoroutineScope(), snackbarHostState)

    GenericScaffold(
        title = stringResource(id = R.string.custom_focus_indicators_title),
        onBackPressed = onBackPressed,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()

        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.custom_focus_indicators_heading),
                modifier = Modifier.testTag(customFocusIndicatorsHeadingTestTag)
            )
            BodyText(textId = R.string.custom_focus_indicators_description_1)
            BodyText(textId = R.string.custom_focus_indicators_description_2)
            BodyText(textId = R.string.custom_focus_indicators_description_3)

            ProblematicExample1(snackbarLauncher)
            GoodExample2(snackbarLauncher)
            ProblematicExample3(snackbarLauncher)
            GoodExample4(snackbarLauncher)
            ProblematicExample5(snackbarLauncher)
            GoodExample6(snackbarLauncher)
            GoodExample7(snackbarLauncher)

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        CustomFocusIndicatorsScreen {}
    }
}

@Composable
private fun ProblematicExample1(
    snackbarLauncher: SnackbarLauncher?
) {
    // Problematic example 1: Default button focus indicator
    ProblematicExampleHeading(
        text = stringResource(id = R.string.custom_focus_indicators_example_1_heading),
        modifier = Modifier.testTag(customFocusIndicatorsExample1HeadingTestTag)
    )

    BodyText(textId = R.string.custom_focus_indicators_example_1_description)

    val buttonMessage = stringResource(id = R.string.custom_focus_indicators_example_1_message)
    Button(
        onClick = {
            snackbarLauncher?.show(buttonMessage)
        },
        modifier = Modifier
            .testTag(customFocusIndicatorsExample1ButtonTestTag)
            .padding(top = 8.dp),
    ) {
        Text(stringResource(id = R.string.custom_focus_indicators_example_1_button))
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
private fun ProblematicExample1Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            ProblematicExample1(snackbarLauncher = null)
        }
    }
}

@Composable
private fun GoodExample2(
    snackbarLauncher: SnackbarLauncher?
) {
    // Good example 2: Custom button focus indicator
    GoodExampleHeading(
        text = stringResource(id = R.string.custom_focus_indicators_example_2_heading),
        modifier = Modifier.testTag(customFocusIndicatorsExample2HeadingTestTag)
    )

    BodyText(textId = R.string.custom_focus_indicators_example_2_description)

    val buttonMessage = stringResource(id = R.string.custom_focus_indicators_example_2_message)
    VisibleFocusBorderButton(
        onClick = {
            snackbarLauncher?.show(buttonMessage)
        },
        modifier = Modifier
            .testTag(customFocusIndicatorsExample2ButtonTestTag)
            .padding(top = 8.dp),
    ) {
        Text(stringResource(id = R.string.custom_focus_indicators_example_2_button))
    }

    Spacer(modifier = Modifier.height(8.dp))
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
            GoodExample2(snackbarLauncher = null)
        }
    }
}

@Composable
private fun ProblematicExample3(
    snackbarLauncher: SnackbarLauncher?
) {
    // Problematic example 3: Default icon button focus indicator
    ProblematicExampleHeading(
        text = stringResource(id = R.string.custom_focus_indicators_example_3_heading),
        modifier = Modifier.testTag(customFocusIndicatorsExample3HeadingTestTag)
    )

    val buttonMessage = stringResource(id = R.string.custom_focus_indicators_example_3_message)
    IconButton(
        onClick = {
            snackbarLauncher?.show(buttonMessage)
        },
        modifier = Modifier
            .testTag(customFocusIndicatorsExample3ButtonTestTag)
            .padding(top = 8.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_share_fill),
            contentDescription = stringResource(
                id = R.string.custom_focus_indicators_example_3_button_description
            ),
            tint = MaterialTheme.colorScheme.primary,
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
private fun ProblematicExample3Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            ProblematicExample3(snackbarLauncher = null)
        }
    }
}

@Composable
private fun GoodExample4(
    snackbarLauncher: SnackbarLauncher?
) {
    // Good example 4: Custom icon button focus indicator
    GoodExampleHeading(
        text = stringResource(id = R.string.custom_focus_indicators_example_4_heading),
        modifier = Modifier.testTag(customFocusIndicatorsExample4HeadingTestTag)
    )

    val buttonMessage = stringResource(id = R.string.custom_focus_indicators_example_4_message)
    VisibleFocusBorderIconButton(
        onClick = {
            snackbarLauncher?.show(buttonMessage)
        },
        modifier = Modifier
            .testTag(customFocusIndicatorsExample4ButtonTestTag)
            .padding(top = 8.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_share_fill),
            contentDescription = stringResource(
                id = R.string.custom_focus_indicators_example_4_button_description
            ),
            tint = MaterialTheme.colorScheme.primary,
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
private fun GoodExample4Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample4(snackbarLauncher = null)
        }
    }
}

@Composable
private fun ProblematicExample5(
    snackbarLauncher: SnackbarLauncher?
) {
    // Problematic example 5: Default clickable Card focus indicator
    ProblematicExampleHeading(
        text = stringResource(id = R.string.custom_focus_indicators_example_5_heading),
        modifier = Modifier.testTag(customFocusIndicatorsExample5HeadingTestTag)
    )

    val cardClickMessage = stringResource(id = R.string.custom_focus_indicators_example_5_message)
    OutlinedCard(
        onClick = {
            snackbarLauncher?.show(cardClickMessage)
        },
        modifier = Modifier
            .testTag(customFocusIndicatorsExample5CardTestTag)
            .padding(top = 8.dp),
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            stringResource(id = R.string.custom_focus_indicators_example_5_card_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(horizontal = 12.dp)
        )
        BodyText(
            textId = R.string.custom_focus_indicators_example_5_card_description,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
private fun ProblematicExample5Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            ProblematicExample5(snackbarLauncher = null)
        }
    }
}

@Composable
private fun GoodExample6(
    snackbarLauncher: SnackbarLauncher?
) {
    // Good example 6: Custom clickable Card focus indicator
    GoodExampleHeading(
        text = stringResource(id = R.string.custom_focus_indicators_example_6_heading),
        modifier = Modifier.testTag(customFocusIndicatorsExample6HeadingTestTag)
    )

    val cardClickMessage = stringResource(id = R.string.custom_focus_indicators_example_6_message)
    OutlinedCard(
        onClick = {
            snackbarLauncher?.show(cardClickMessage)
        },
        modifier = Modifier
            .testTag(customFocusIndicatorsExample6CardTestTag)
            .padding(top = 8.dp)
            .visibleCardFocusBorder(),
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            stringResource(id = R.string.custom_focus_indicators_example_6_card_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(horizontal = 12.dp)
        )
        BodyText(
            textId = R.string.custom_focus_indicators_example_6_card_description,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
private fun GoodExample6Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample6(snackbarLauncher = null)
        }
    }
}

@Composable
private fun GoodExample7(
    snackbarLauncher: SnackbarLauncher?
) {
    // Good example 7: Custom Indication focus indicator
    GoodExampleHeading(
        text = stringResource(id = R.string.custom_focus_indicators_example_7_heading),
        modifier = Modifier.testTag(customFocusIndicatorsExample7HeadingTestTag)
    )

    val cardClickMessage = stringResource(id = R.string.custom_focus_indicators_example_7_message)
    // Key technique: Hoist the interactionSource to the parent composable, so it can be shared
    // between all of the properties in this composable function call.
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    OutlinedCard(
        onClick = {
            snackbarLauncher?.show(cardClickMessage)
        },
        modifier = Modifier
            .testTag(customFocusIndicatorsExample7CardTestTag)
            .padding(top = 8.dp)
            // Key technique: Use Modifier.indication() to associate the interactionSource and an
            // additional indication factory class instance. OutlinedCard internals will still apply
            // the default ripple indication. Since Indication instances do not have access to
            // Compose theme colors, the appropriate Dark or Light theme color must be passed in.
            .indication(
                interactionSource = interactionSource,
                indication = VisibleFocusIndication(
                    themedIndicationColor = MaterialTheme.colorScheme.primary
                )
            ),
        // Key technique: Provide a common interactionSource to both clickable Card and indication.
        interactionSource = interactionSource
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            stringResource(id = R.string.custom_focus_indicators_example_7_card_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(horizontal = 12.dp)
        )
        BodyText(
            textId = R.string.custom_focus_indicators_example_7_card_description,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
private fun GoodExample7Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample7(snackbarLauncher = null)
        }
    }
}

