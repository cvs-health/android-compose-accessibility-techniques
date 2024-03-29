/*
   Copyright 2024 CVS Health and/or one of its affiliates

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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_click_labels

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.OkExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SnackbarLauncher
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.VisibleFocusBorderButton
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.VisibleFocusBorderTextButton
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.visibleCardFocusBorder
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val customClickLabelsHeadingTestTag = "customClickLabelsHeading"
const val customClickLabelsExample1HeadingTestTag = "customClickLabelsExample1Heading"
const val customClickLabelsExample1CardTestTag = "customClickLabelsExample1Card"
const val customClickLabelsExample2HeadingTestTag = "customClickLabelsExample2Heading"
const val customClickLabelsExample2CardTestTag = "customClickLabelsExample2Card"
const val customClickLabelsExample3HeadingTestTag = "customClickLabelsExample3Heading"
const val customClickLabelsExample3ButtonTestTag = "customClickLabelsExample3Button"


@Composable
fun CustomClickLabelsScreen(
    onBackPressed: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarLauncher = SnackbarLauncher(rememberCoroutineScope(), snackbarHostState)

    GenericScaffold(
        title = stringResource(id = R.string.custom_click_labels_title),
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
                text = stringResource(id = R.string.custom_click_labels_heading),
                modifier = Modifier.testTag(customClickLabelsHeadingTestTag)
            )
            BodyText(textId = R.string.custom_click_labels_description_1)
            BodyText(textId = R.string.custom_click_labels_description_2)
            BodyText(textId = R.string.custom_click_labels_description_3)
            BodyText(textId = R.string.custom_click_labels_description_4)

            OkExample1(snackbarLauncher)
            GoodExample2(snackbarLauncher)
            GoodExample3(snackbarLauncher)

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        CustomClickLabelsScreen {}
    }
}


@Composable
private fun OkExample1(snackbarLauncher: SnackbarLauncher?) {
    OkExampleHeading(
        text = stringResource(id = R.string.custom_click_labels_example_1_heading),
        modifier = Modifier.testTag(customClickLabelsExample1HeadingTestTag)
    )
    Spacer(modifier = Modifier.height(8.dp))

    val example1Message = stringResource(id = R.string.custom_click_labels_example_1_message)
    // Note: The overload of OutlinedCard with the onClick parameter does not allow ready provision
    // of a custom click action label.
    OutlinedCard(
        onClick = {
            snackbarLauncher?.show(example1Message)
        },
        modifier = Modifier
            .testTag(customClickLabelsExample1CardTestTag)
            .fillMaxWidth()
            .visibleCardFocusBorder(),
        border = BorderStroke(2.dp, CardDefaults.outlinedCardBorder().brush)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
                .minimumInteractiveComponentSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(id = R.string.custom_click_labels_example_1_label),
                fontWeight = FontWeight.Medium
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_angle_right_outline),
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OkExample1Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            OkExample1(snackbarLauncher = null)
        }
    }
}

@Composable
private fun GoodExample2(snackbarLauncher: SnackbarLauncher?) {
    GoodExampleHeading(
        text = stringResource(id = R.string.custom_click_labels_example_2_heading),
        modifier = Modifier.testTag(customClickLabelsExample2HeadingTestTag)
    )
    Spacer(modifier = Modifier.height(8.dp))

    val example2Message = stringResource(id = R.string.custom_click_labels_example_2_message)
    // Note the use of the OutlinedCard overload without the onClick parameter, instead applying
    // Modifier.clickable with an onClickLabel directly to the Card.
    OutlinedCard(
        modifier = Modifier
            .testTag(customClickLabelsExample2CardTestTag)
            .fillMaxWidth()
            .visibleCardFocusBorder()
            // Key technique: Provide a custom onClickLabel to Modifier.clickable.
            .clickable(
                onClickLabel = stringResource(id = R.string.custom_click_labels_example_2_custom_click_label)
            ) {
                snackbarLauncher?.show(example2Message)
            },
        border = BorderStroke(2.dp, CardDefaults.outlinedCardBorder().brush)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
                .minimumInteractiveComponentSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(id = R.string.custom_click_labels_example_2_label),
                fontWeight = FontWeight.Medium
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_angle_right_outline),
                contentDescription = null
            )
        }
    }
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
private fun GoodExample3(snackbarLauncher: SnackbarLauncher?) {
    GoodExampleHeading(
        text = stringResource(id = R.string.custom_click_labels_example_3_heading),
        modifier = Modifier.testTag(customClickLabelsExample3HeadingTestTag)
    )
    Spacer(modifier = Modifier.height(8.dp))

    val exampleMessage = stringResource(id = R.string.custom_click_labels_example_3_message)
    // onClickLabel semantics are handled by VisibleFocusBorderButton.
    // Key technique: Apply Modifier.semantics { onClick( label = ..., onClick = ...) } in addition
    // to the Button onClick parameter, instead of Modifier.clickable().
    VisibleFocusBorderButton(
        onClick = {
            snackbarLauncher?.show(exampleMessage)
        },
        modifier = Modifier.testTag(customClickLabelsExample3ButtonTestTag),
        onClickLabel = stringResource(id = R.string.custom_click_labels_example_3_custom_click_label)
    ) {
        Text(
            text = stringResource(id = R.string.custom_click_labels_example_3_label),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GoodExample3Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample3(snackbarLauncher = null)
        }
    }
}