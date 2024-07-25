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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.target_size

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BadExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.OkExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SnackbarLauncher
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.VisibleFocusBorderIconButton
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme


const val minimumTouchTargetSizeHeadingTestTag = "minimumTouchTargetSizeHeading"
const val minimumTouchTargetSizeExample1HeadingTestTag = "minimumTouchTargetSizeExample1Heading"
const val minimumTouchTargetSizeExample1IconButtonTestTag = "minimumTouchTargetSizeExample1IconButton"
const val minimumTouchTargetSizeExample2HeadingTestTag = "minimumTouchTargetSizeExample1aHeading"
const val minimumTouchTargetSizeExample2IconButton1TestTag = "minimumTouchTargetSizeExample2IconButton1"
const val minimumTouchTargetSizeExample2IconButton2TestTag = "minimumTouchTargetSizeExample2IconButton2"
const val minimumTouchTargetSizeExample2IconButton3TestTag = "minimumTouchTargetSizeExample2IconButton3"
const val minimumTouchTargetSizeExample2IconButton4TestTag = "minimumTouchTargetSizeExample2IconButton4"
const val minimumTouchTargetSizeExample3HeadingTestTag = "minimumTouchTargetSizeExample3Heading"
const val minimumTouchTargetSizeExample3IconButtonTestTag = "minimumTouchTargetSizeExample3IconButton"
const val minimumTouchTargetSizeExample4HeadingTestTag = "minimumTouchTargetSizeExample4Heading"
const val minimumTouchTargetSizeExample4IconButtonTestTag = "minimumTouchTargetSizeExample4IconButton"
const val minimumTouchTargetSizeExample5HeadingTestTag = "minimumTouchTargetSizeExample5Heading"
const val minimumTouchTargetSizeExample5IconButtonTestTag = "minimumTouchTargetSizeExample5IconButton"
const val minimumTouchTargetSizeExample6HeadingTestTag = "minimumTouchTargetSizeExample6Heading"
const val minimumTouchTargetSizeExample6IconButtonTestTag = "minimumTouchTargetSizeExample6IconButton"

/**
 * Demonstrate accessibility techniques for minimum touch target size in accordance with WCAG
 * [Success Criterion 2.5.8 Target Size (Minimum)](https://www.w3.org/TR/WCAG22/#target-size-minimum).
 *
 * Applies [GenericScaffold] to wrap the screen content. Hosts Snackbars.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@Composable
fun MinimumTouchTargetSizeScreen(
    onBackPressed: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarLauncher = SnackbarLauncher(rememberCoroutineScope(), snackbarHostState)

    GenericScaffold(
        title = stringResource(id = R.string.target_size_title),
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
                text = stringResource(id = R.string.target_size_heading),
                modifier = Modifier.testTag(minimumTouchTargetSizeHeadingTestTag)
            )
            BodyText(textId = R.string.target_size_description_1)
            BodyText(textId = R.string.target_size_description_2)

            BadExample1(snackbarLauncher)
            BadExample2(snackbarLauncher)
            OkExample3(snackbarLauncher)
            GoodExample4(snackbarLauncher)
            GoodExample5(snackbarLauncher)
            GoodExample6(snackbarLauncher)

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        MinimumTouchTargetSizeScreen {}
    }
}

@Composable
private fun BadExample1(
    snackbarLauncher: SnackbarLauncher?
) {
    // Bad example 1: A 16dp by 22dp icon button
    BadExampleHeading(
        text = stringResource(id = R.string.target_size_example_1_heading),
        modifier = Modifier.testTag(minimumTouchTargetSizeExample1HeadingTestTag)
    )
    BodyText(textId = R.string.target_size_example_1_description)
    Spacer(modifier = Modifier.height(8.dp))

    val popupMessage = stringResource(id = R.string.target_size_example_1_message)

    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = CircleShape
            )
            .width(48.dp)
            .height(48.dp)
    ) {
        VisibleFocusBorderIconButton(
            onClick = {
                snackbarLauncher?.show(popupMessage)
            },
            modifier = Modifier
                .testTag(minimumTouchTargetSizeExample1IconButtonTestTag)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = CircleShape
                )
                .width(12.dp)
                .height(12.dp)
                .align(Alignment.Center)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_angle_right_outline),
                contentDescription = stringResource(id = R.string.target_size_example_1_content_description)
            )
        }
    }
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
            BadExample1(snackbarLauncher = null)
        }
    }
}

@Composable
private fun BadExample2(
    snackbarLauncher: SnackbarLauncher?
) {
    // Bad example 2: Overlapping touch targets
    BadExampleHeading(
        text = stringResource(id = R.string.target_size_example_2_heading),
        modifier = Modifier.testTag(minimumTouchTargetSizeExample2HeadingTestTag)
    )
    BodyText(textId = R.string.target_size_example_2_description_1)
    BodyText(textId = R.string.target_size_example_2_description_2)
    Spacer(modifier = Modifier.height(8.dp))

    val popupMessage1 = stringResource(id = R.string.target_size_example_2_message, 1)
    val popupMessage2 = stringResource(id = R.string.target_size_example_2_message, 2)
    val popupMessage3 = stringResource(id = R.string.target_size_example_2_message, 3)
    val popupMessage4 = stringResource(id = R.string.target_size_example_2_message, 4)

    Box(
        modifier = Modifier
            .padding(4.dp)
            .width(32.dp)
            .height(32.dp)
    ) {
        VisibleFocusBorderIconButton(
            onClick = {
                snackbarLauncher?.show(popupMessage1)
            },
            modifier = Modifier
                .testTag(minimumTouchTargetSizeExample2IconButton1TestTag)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = CircleShape
                )
                .width(12.dp)
                .height(12.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_angle_right_outline),
                contentDescription = stringResource(
                    id = R.string.target_size_example_2_content_description,
                    1
                )
            )
        }

        VisibleFocusBorderIconButton(
            onClick = {
                snackbarLauncher?.show(popupMessage2)
            },
            modifier = Modifier
                .testTag(minimumTouchTargetSizeExample2IconButton2TestTag)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = CircleShape
                )
                .width(12.dp)
                .height(12.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_angle_right_outline),
                contentDescription = stringResource(id = R.string.target_size_example_2_content_description, 2)
            )
        }
        VisibleFocusBorderIconButton(
            onClick = {
                snackbarLauncher?.show(popupMessage3)
            },
            modifier = Modifier
                .testTag(minimumTouchTargetSizeExample2IconButton3TestTag)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = CircleShape
                )
                .width(12.dp)
                .height(12.dp)
                .align(Alignment.BottomStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_angle_right_outline),
                contentDescription = stringResource(id = R.string.target_size_example_2_content_description, 3)
            )
        }
        VisibleFocusBorderIconButton(
            onClick = {
                snackbarLauncher?.show(popupMessage4)
            },
            modifier = Modifier
                .testTag(minimumTouchTargetSizeExample2IconButton4TestTag)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = CircleShape
                )
                .width(12.dp)
                .height(12.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_angle_right_outline),
                contentDescription = stringResource(id = R.string.target_size_example_2_content_description, 4)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BadExample2Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            BadExample2(snackbarLauncher = null)
        }
    }
}

@Composable
private fun OkExample3(
    snackbarLauncher: SnackbarLauncher?
) {
    // OK example 3: A 24dp by 24dp icon button
    OkExampleHeading(
        text = stringResource(id = R.string.target_size_example_3_heading),
        modifier = Modifier.testTag(minimumTouchTargetSizeExample3HeadingTestTag)
    )
    BodyText(textId = R.string.target_size_example_3_description)
    Spacer(modifier = Modifier.height(8.dp))

    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = CircleShape
            )
            .width(48.dp)
            .height(48.dp)
    ) {
        val popupMessage = stringResource(id = R.string.target_size_example_3_message)
        VisibleFocusBorderIconButton(
            onClick = {
                snackbarLauncher?.show(popupMessage)
            },
            modifier = Modifier
                .testTag(minimumTouchTargetSizeExample3IconButtonTestTag)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = CircleShape
                )
                .size(24.dp)
                .align(Alignment.Center)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_angle_right_outline),
                contentDescription = stringResource(id = R.string.target_size_example_3_content_description)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OkExample3Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            OkExample3(snackbarLauncher = null)
        }
    }
}

@Composable
private fun GoodExample4(
    snackbarLauncher: SnackbarLauncher?
) {
    // Good example 4: A 48dp by 48dp icon button using Modifier.sizeIn
    GoodExampleHeading(
        text = stringResource(id = R.string.target_size_example_4_heading),
        modifier = Modifier.testTag(minimumTouchTargetSizeExample4HeadingTestTag)
    )
    Spacer(modifier = Modifier.height(8.dp))

    val popupMessage = stringResource(id = R.string.target_size_example_4_message)
    VisibleFocusBorderIconButton(
        onClick = {
            snackbarLauncher?.show(popupMessage)
        },
        modifier = Modifier
            .testTag(minimumTouchTargetSizeExample4IconButtonTestTag)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.onSurface, shape = CircleShape)
            .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_angle_right_outline),
            contentDescription = stringResource(id = R.string.target_size_example_4_content_description)
        )
    }
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
private fun GoodExample5(
    snackbarLauncher: SnackbarLauncher?
) {
    // Good example 5: A 48dp by 48dp icon button using Modifier.minimumInteractiveComponentSize
    GoodExampleHeading(
        text = stringResource(id = R.string.target_size_example_5_heading),
        modifier = Modifier.testTag(minimumTouchTargetSizeExample5HeadingTestTag)
    )
    BodyText(textId = R.string.target_size_example_5_description)
    Spacer(modifier = Modifier.height(8.dp))

    val popupMessage = stringResource(id = R.string.target_size_example_5_message)
    VisibleFocusBorderIconButton(
        onClick = {
            snackbarLauncher?.show(popupMessage)
        },
        modifier = Modifier
            .testTag(minimumTouchTargetSizeExample5IconButtonTestTag)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.onSurface, shape = CircleShape)
            .minimumInteractiveComponentSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_angle_right_outline),
            contentDescription = stringResource(id = R.string.target_size_example_5_content_description)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GoodExample5Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample5(snackbarLauncher = null)
        }
    }
}

@Composable
private fun GoodExample6(
    snackbarLauncher: SnackbarLauncher?
) {
    // Good example 6: Clickable composables are made large enough by default
    GoodExampleHeading(
        text = stringResource(id = R.string.target_size_example_6_heading),
        modifier = Modifier.testTag(minimumTouchTargetSizeExample6HeadingTestTag)
    )
    BodyText(textId = R.string.target_size_example_6_description)
    Spacer(modifier = Modifier.height(8.dp))

    val popupMessage = stringResource(id = R.string.target_size_example_6_message)
    VisibleFocusBorderIconButton(
        onClick = {
            snackbarLauncher?.show(popupMessage)
        },
        modifier = Modifier
            .testTag(minimumTouchTargetSizeExample6IconButtonTestTag)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.onSurface, shape = CircleShape)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_angle_right_outline),
            contentDescription = stringResource(id = R.string.target_size_example_6_content_description)
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
        ) {
            GoodExample6(snackbarLauncher = null)
        }
    }
}
