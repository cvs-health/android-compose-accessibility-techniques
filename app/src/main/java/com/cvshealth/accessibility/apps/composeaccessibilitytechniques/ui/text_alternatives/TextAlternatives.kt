/*
   Copyright 2023-2024 CVS Health and/or one of its affiliates

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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
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

const val textAlternativesHeadingTestTag = "textAlternativesHeading"
const val textAlternativesExample1HeadingTestTag = "textAlternativesExample1Heading"
const val textAlternativesExample1UngroupedTextAndIcon1TestTag = "textAlternativesExample1UngroupedTextAndIcon1"
const val textAlternativesExample1UngroupedTextAndIcon2TestTag = "textAlternativesExample1UngroupedTextAndIcon2"
const val textAlternativesExample2HeadingTestTag = "textAlternativesExample2Heading"
const val textAlternativesExample2UngroupedTextAndIcon1TestTag = "textAlternativesExample2UngroupedTextAndIcon1"
const val textAlternativesExample2UngroupedTextAndIcon2TestTag = "textAlternativesExample2UngroupedTextAndIcon2"
const val textAlternativesExample3HeadingTestTag = "textAlternativesExample3Heading"
const val textAlternativesExample3GroupedTextAndIcon1TestTag = "textAlternativesExample3GroupedTextAndIcon1"
const val textAlternativesExample3GroupedTextAndIcon2TestTag = "textAlternativesExample3GroupedTextAndIcon2"
const val textAlternativesExample4HeadingTestTag = "textAlternativesExample4Heading"
const val textAlternativesExample4GroupedTextAndIcon1TestTag = "textAlternativesExample4GroupedTextAndIcon1"
const val textAlternativesExample4GroupedTextAndIcon2TestTag = "textAlternativesExample4GroupedTextAndIcon2"
const val textAlternativesExample5HeadingTestTag = "textAlternativesExample5Heading"
const val textAlternativesExample5GroupedTextAndIcon1TestTag = "textAlternativesExample5GroupedTextAndIcon1"
const val textAlternativesExample5GroupedTextAndIcon2TestTag = "textAlternativesExample5GroupedTextAndIcon2"
const val textAlternativesExample6HeadingTestTag = "textAlternativesExample6Heading"
const val textAlternativesExample6IconButtonTestTag = "textAlternativesExample6IconButton"
const val textAlternativesExample7HeadingTestTag = "textAlternativesExample7Heading"
const val textAlternativesExample7IconButtonTestTag = "textAlternativesExample7IconButton"
const val textAlternativesExample8HeadingTestTag = "textAlternativesExample8Heading"
const val textAlternativesExample8IconButtonTestTag = "textAlternativesExample8IconButton"
const val textAlternativesExample9HeadingTestTag = "textAlternativesExample9Heading"
const val textAlternativesExample9Icon1TestTag = "textAlternativesExample9Icon1"
const val textAlternativesExample9Icon2TestTag = "textAlternativesExample9Icon2"
const val textAlternativesExample9TextTestTag = "textAlternativesExample9Text"
const val textAlternativesExample10HeadingTestTag = "textAlternativesExample10Heading"
const val textAlternativesExample10Icon1TestTag = "textAlternativesExample10Icon1"
const val textAlternativesExample10Icon2TestTag = "textAlternativesExample10Icon2"
const val textAlternativesExample10TextTestTag = "textAlternativesExample10Text"
const val textAlternativesExample11HeadingTestTag = "textAlternativesExample11Heading"
const val textAlternativesExample11Icon1TestTag = "textAlternativesExample11Icon1"
const val textAlternativesExample11Icon2TestTag = "textAlternativesExample11Icon2"
const val textAlternativesExample11TextTestTag = "textAlternativesExample11Text"
const val textAlternativesExample12HeadingTestTag = "textAlternativesExample12Heading"
const val textAlternativesExample12GroupedDecorativeIconsAndTextTestTag = "textAlternativesExample12GroupedDecorativeIconsAndText"
const val textAlternativesExample13HeadingTestTag = "textAlternativesExample13Heading"
const val textAlternativesExample13GroupedDecorativeIconsAndTextTestTag = "textAlternativesExample13GroupedDecorativeIconsAndText"

@Composable
fun TextAlternativesScreen(
    onBackPressed: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarLauncher = SnackbarLauncher(rememberCoroutineScope(), snackbarHostState)

    GenericScaffold(
        title = stringResource(id = R.string.text_alternatives_title),
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
            TextAlternativesHeadingSection()
            BadExample1()
            OkExample2()
            GoodExample3()
            GoodExample4()
            GoodExample5()
            BadExample6(snackbarLauncher)
            BadExample7(snackbarLauncher)
            GoodExample8(snackbarLauncher)
            BadExample9()
            BadExample10()
            GoodExample11()
            OkExample12()
            GoodExample13()

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme() {
        TextAlternativesScreen {}
    }
}

@Composable
private fun TextAlternativesHeadingSection() {
    SimpleHeading(
        text = stringResource(id = R.string.text_alternatives_heading),
        modifier = Modifier.testTag(textAlternativesHeadingTestTag)
    )
    BodyText(textId = R.string.text_alternatives_description_1)
    BodyText(textId = R.string.text_alternatives_description_2)
    BodyText(textId = R.string.text_alternatives_description_3)
}

@Composable
private fun PreviewWrapper(
    content: @Composable () -> Unit
) {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TextAlternativesHeadingSectionPreview() {
    PreviewWrapper {
        TextAlternativesHeadingSection()
    }
}

@Composable
private fun BadExample1() {
    // Bad example 1: Sunrise and sunset times and icons with empty text alternatives
    BadExampleHeading(
        text = stringResource(id = R.string.text_alternatives_example_1_heading),
        modifier = Modifier.testTag(textAlternativesExample1HeadingTestTag)
    )
    UngroupedTextAndImage(
        textId = R.string.text_alternatives_example_sunrise_time,
        iconId = R.drawable.ic_sunrise_fill,
        modifier = Modifier.testTag(textAlternativesExample1UngroupedTextAndIcon1TestTag),
        contentDescription = "" // Never use the empty string as a contentDescription
    )
    UngroupedTextAndImage(
        textId = R.string.text_alternatives_example_sunset_time,
        iconId = R.drawable.ic_sunset_fill,
        modifier = Modifier.testTag(textAlternativesExample1UngroupedTextAndIcon2TestTag),
        contentDescription = "" // Never use the empty string as a contentDescription
    )
}

@Preview(showBackground = true)
@Composable
private fun BadExample1Preview() {
    PreviewWrapper {
        BadExample1()
    }
}

@Composable
private fun OkExample2() {
    // OK example 2: Sunrise and sunset times and icons with text alternatives
    OkExampleHeading(
        text = stringResource(id = R.string.text_alternatives_example_2_heading),
        modifier = Modifier.testTag(textAlternativesExample2HeadingTestTag)
    )

    // Key technique: Provide a concise contentDescription for all informative images.
    UngroupedTextAndImage(
        textId = R.string.text_alternatives_example_sunrise_time,
        iconId = R.drawable.ic_sunrise_fill,
        modifier = Modifier.testTag(textAlternativesExample2UngroupedTextAndIcon1TestTag),
        contentDescription = stringResource(id = R.string.text_alternatives_example_sunrise_description)
    )
    UngroupedTextAndImage(
        textId = R.string.text_alternatives_example_sunset_time,
        iconId = R.drawable.ic_sunset_fill,
        modifier = Modifier.testTag(textAlternativesExample2UngroupedTextAndIcon2TestTag),
        contentDescription = stringResource(id = R.string.text_alternatives_example_sunset_description)
    )
}

@Preview(showBackground = true)
@Composable
private fun OkExample2Preview() {
    PreviewWrapper {
        OkExample2()
    }
}

@Composable
private fun GoodExample3() {
    // Good example 3: Sunrise and sunset times grouped with their icons
    GoodExampleHeading(
        text = stringResource(id = R.string.text_alternatives_example_3_heading),
        modifier = Modifier.testTag(textAlternativesExample3HeadingTestTag)
    )

    // Key techniques:
    // 1. Group Column content with Modifier.semantics( mergeDescendants = true ).
    // 2. Provide a concise contentDescription for all informative images.
    TextAndImageGroup(
        textId = R.string.text_alternatives_example_sunrise_time,
        iconId = R.drawable.ic_sunrise_fill,
        modifier = Modifier.testTag(textAlternativesExample3GroupedTextAndIcon1TestTag),
        contentDescription = stringResource(id = R.string.text_alternatives_example_sunrise_description)
    )
    TextAndImageGroup(
        textId = R.string.text_alternatives_example_sunset_time,
        iconId = R.drawable.ic_sunset_fill,
        modifier = Modifier.testTag(textAlternativesExample3GroupedTextAndIcon2TestTag),
        contentDescription = stringResource(id = R.string.text_alternatives_example_sunset_description)
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExample3Preview() {
    PreviewWrapper {
        GoodExample3()
    }
}

@Composable
private fun GoodExample4() {
    // Good example 4: Sunrise and sunset times grouped with their icons and redundant text
    GoodExampleHeading(
        text = stringResource(id = R.string.text_alternatives_example_4_heading),
        modifier = Modifier.testTag(textAlternativesExample4HeadingTestTag)
    )

    // Key techniques:
    // 1. Group Column content with Modifier.semantics( mergeDescendants = true ).
    // 2. Set contentDescription = null for all decorative or redundant images.
    TextImageAndTextGroup(
        textId = R.string.text_alternatives_example_sunrise_time,
        iconId = R.drawable.ic_sunrise_fill,
        textId2 = R.string.text_alternatives_example_sunrise_description,
        modifier = Modifier.testTag(textAlternativesExample4GroupedTextAndIcon1TestTag),
        contentDescription = null // text is redundant with image, so image is decorative
    )
    TextImageAndTextGroup(
        textId = R.string.text_alternatives_example_sunset_time,
        iconId = R.drawable.ic_sunset_fill,
        textId2 = R.string.text_alternatives_example_sunset_description,
        modifier = Modifier.testTag(textAlternativesExample4GroupedTextAndIcon2TestTag),
        contentDescription = null // text is redundant with image, so image is decorative
    )
    BodyText(textId = R.string.text_alternatives_example_4_note)
}

@Preview(showBackground = true)
@Composable
private fun GoodExample4Preview() {
    PreviewWrapper {
        GoodExample4()
    }
}

@Composable
private fun GoodExample5() {
    // Good example 5: Sunrise and sunset times and icons with group text alternatives
    GoodExampleHeading(
        text = stringResource(id = R.string.text_alternatives_example_5_heading),
        modifier = Modifier.testTag(textAlternativesExample5HeadingTestTag)
    )

    // Key techniques:
    // 1. Set a group-level contentDescription with
    //    Modifier.semantics(mergeDescendants = true) { contentDescription = ... }
    // 2. Suppress unnecessary child composable accessibility text/contentDescription with
    //    Modifier.semantics { invisibleToUser() } or Modifier.clearAndSetSemantics {...}.
    // 3. Set contentDescription = null for all decorative or redundant images.
    TextAndImageGroupSemanticsReplaced(
        textId = R.string.text_alternatives_example_sunrise_time,
        iconId = R.drawable.ic_sunrise_fill,
        modifier = Modifier.testTag(textAlternativesExample5GroupedTextAndIcon1TestTag),
        groupContentDescriptionId = R.string.text_alternatives_example_5_grouped_sunrise_text
    )
    TextAndImageGroupSemanticsReplaced(
        textId = R.string.text_alternatives_example_sunset_time,
        iconId = R.drawable.ic_sunset_fill,
        modifier = Modifier.testTag(textAlternativesExample5GroupedTextAndIcon2TestTag),
        groupContentDescriptionId = R.string.text_alternatives_example_5_grouped_sunset_text
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExample5Preview() {
    PreviewWrapper {
        GoodExample5()
    }
}

@Composable
private fun BadExample6(
    snackbarLauncher: SnackbarLauncher?
) {
    // Bad example 6: A 'Share' icon button with an empty text alternative
    BadExampleHeading(
        text = stringResource(id = R.string.text_alternatives_example_6_heading),
        modifier = Modifier.testTag(textAlternativesExample6HeadingTestTag)
    )
    val popupMessage = stringResource(id = R.string.text_alternatives_example_6_message)
    VisibleFocusBorderIconButton(
        onClick = {
            snackbarLauncher?.show(popupMessage)
        },
        modifier = Modifier
            .testTag(textAlternativesExample6IconButtonTestTag)
            .minimumInteractiveComponentSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_share_fill),
            contentDescription = ""  // Never use the empty string as a contentDescription
        )
    }
    BodyText(textId = R.string.text_alternatives_example_6_note)
}

@Preview(showBackground = true)
@Composable
private fun BadExample6Preview() {
    PreviewWrapper {
        BadExample6(snackbarLauncher = null)
    }
}

@Composable
private fun BadExample7(
    snackbarLauncher: SnackbarLauncher?
) {
    // Bad example 7: A 'Share' icon button with a null text alternative
    BadExampleHeading(
        text = stringResource(id = R.string.text_alternatives_example_7_heading),
        modifier = Modifier.testTag(textAlternativesExample7HeadingTestTag)
    )
    
    // Note: Do not use Icon(..., contentDescription = null) to mark active (e.g., button)
    // images unless the active composable has text or a contentDescription.
    val popupMessage = stringResource(id = R.string.text_alternatives_example_7_message)
    VisibleFocusBorderIconButton(
        onClick = {
            snackbarLauncher?.show(popupMessage)
        },
        modifier = Modifier
            .testTag(textAlternativesExample7IconButtonTestTag)
            .minimumInteractiveComponentSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_share_fill),
            contentDescription = null // Use null for active images only if there is text too.
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BadExample7Preview() {
    PreviewWrapper {
        BadExample7(snackbarLauncher = null)
    }
}

@Composable
private fun GoodExample8(
    snackbarLauncher: SnackbarLauncher?
) {
    // Good example 8: A 'Share' icon button with a text alternative
    GoodExampleHeading(
        text = stringResource(id = R.string.text_alternatives_example_8_heading),
        modifier = Modifier.testTag(textAlternativesExample8HeadingTestTag)
    )

    // Key technique: Provide a concise contentDescription for all active images.
    val popupMessage = stringResource(id = R.string.text_alternatives_example_8_message)
    VisibleFocusBorderIconButton(
        onClick = {
            snackbarLauncher?.show(popupMessage)
        },
        modifier = Modifier
            .testTag(textAlternativesExample8IconButtonTestTag)
            .minimumInteractiveComponentSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_share_fill),
            contentDescription = stringResource(id = R.string.text_alternatives_example_8_content_description)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GoodExample8Preview() {
    PreviewWrapper {
        GoodExample8(snackbarLauncher = null)
    }
}

@Composable
private fun BadExample9() {
    // Bad example 9: Decorative images with empty contentDescription
    BadExampleHeading(
        text = stringResource(id = R.string.text_alternatives_example_9_heading),
        modifier = Modifier.testTag(textAlternativesExample9HeadingTestTag)
    )
    Row(
        modifier = Modifier.padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_sprout_fill),
            contentDescription = "", // Never use the empty string as a contentDescription
            modifier = Modifier.testTag(textAlternativesExample9Icon1TestTag)
        )
        SampleText(
            textId = R.string.text_alternatives_example_9_decorated_text,
            modifier = Modifier
                .testTag(textAlternativesExample9TextTestTag)
                .padding(start = 4.dp, end = 4.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_sprout_fill),
            contentDescription = "", // Never use the empty string as a contentDescription
            modifier = Modifier.testTag(textAlternativesExample9Icon2TestTag)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BadExample9Preview() {
    PreviewWrapper {
        BadExample9()
    }
}

@Composable
private fun BadExample10() {
    // Bad example 10: Decorative images with text alternatives
    BadExampleHeading(
        text = stringResource(id = R.string.text_alternatives_example_10_heading),
        modifier = Modifier.testTag(textAlternativesExample10HeadingTestTag)
    )

    Row(
        modifier = Modifier.padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_sprout_fill),
            contentDescription = stringResource(
                id = R.string.text_alternatives_example_10_content_description
            ), // Do not use a non-null contentDescription for a purely decorative image.
            modifier = Modifier.testTag(textAlternativesExample10Icon1TestTag)
        )
        SampleText(
            textId = R.string.text_alternatives_example_10_decorated_text,
            modifier = Modifier
                .testTag(textAlternativesExample10TextTestTag)
                .padding(start = 4.dp, end = 4.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_sprout_fill),
            contentDescription = stringResource(
                id = R.string.text_alternatives_example_10_content_description
            ), // Do not use a non-null contentDescription for a purely decorative image.
            modifier = Modifier.testTag(textAlternativesExample10Icon2TestTag)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BadExample10Preview() {
    PreviewWrapper {
        BadExample10()
    }
}

@Composable
private fun GoodExample11() {
    // Good example 11: Decorative images with null contentDescription

    // Key technique: Use Icon(..., contentDescription = null) to mark the images as decorative.
    GoodExampleHeading(
        text = stringResource(id = R.string.text_alternatives_example_11_heading),
        modifier = Modifier.testTag(textAlternativesExample11HeadingTestTag)
    )
    Row(
        modifier = Modifier.padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_sprout_fill),
            contentDescription = null,
            modifier = Modifier.testTag(textAlternativesExample11Icon1TestTag)
        )
        SampleText(
            textId = R.string.text_alternatives_example_11_decorated_text,
            modifier = Modifier
                .testTag(textAlternativesExample11TextTestTag)
                .padding(start = 4.dp, end = 4.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_sprout_fill),
            contentDescription = null,
            modifier = Modifier.testTag(textAlternativesExample11Icon2TestTag)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GoodExample11Preview() {
    PreviewWrapper {
        GoodExample11()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun OkExample12() {
    // OK example 12: Grouped decorative images with invisibleToUser()

    // Key techniques:
    // 1. Use Modifier.semantics(mergeDescendants = true) {} to group content semantic text.
    // 2. Use Modifier.semantics { invisibleToUser() } to mark the Icons as not
    //    applicable to the accessibility API.
    // Use the invisibleToUser() technique for more complex composables, because for Icons,
    // contentDescription=null is a simpler approach. See Good Example 13 below.
    OkExampleHeading(
        text = stringResource(id = R.string.text_alternatives_example_12_heading),
        modifier = Modifier.testTag(textAlternativesExample12HeadingTestTag)
    )
    Row(
        modifier = Modifier
            .testTag(textAlternativesExample12GroupedDecorativeIconsAndTextTestTag)
            .padding(top = 8.dp)
            .semantics(mergeDescendants = true) { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_sprout_fill),
            contentDescription = stringResource(
                id = R.string.text_alternatives_example_12_content_description
            ),
            modifier = Modifier.semantics { invisibleToUser() }
        )
        SampleText(
            textId = R.string.text_alternatives_example_12_decorated_text,
            modifier = Modifier.padding(start = 4.dp, end = 4.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_sprout_fill),
            contentDescription = stringResource(
                id = R.string.text_alternatives_example_12_content_description
            ),
            modifier = Modifier.semantics { invisibleToUser() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OkExample12Preview() {
    PreviewWrapper {
        OkExample12()
    }
}

@Composable
private fun GoodExample13() {
    // Good example 13: Grouped decorative images with null contentDescription

    // Key techniques:
    // 1. Use Modifier.semantics(mergeDescendants = true) to merge the Row contents.
    // 2. Use Icon(..., contentDescription = null) to mark the images as decorative.
    GoodExampleHeading(
        text = stringResource(id = R.string.text_alternatives_example_13_heading),
        modifier = Modifier.testTag(textAlternativesExample13HeadingTestTag)
    )
    Row(
        modifier = Modifier
            .testTag(textAlternativesExample13GroupedDecorativeIconsAndTextTestTag)
            .padding(top = 8.dp)
            .semantics(mergeDescendants = true) { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_sprout_fill),
            contentDescription = null
        )
        SampleText(
            textId = R.string.text_alternatives_example_13_decorated_text,
            modifier = Modifier.padding(start = 4.dp, end = 4.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_sprout_fill),
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GoodExample13Preview() {
    PreviewWrapper {
        GoodExample13()
    }
}


// Helper composables:

@Composable
fun SampleText(@StringRes textId: Int, modifier: Modifier = Modifier) {
    Text(
        stringResource(id = textId),
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Preview(showBackground = true)
@Composable
private fun SampleTextPreview() {
    ComposeAccessibilityTechniquesTheme() {
        SampleText(textId = R.string.text_alternatives_title)
    }
}

@Composable
private fun UngroupedTextAndImage(
    @StringRes textId: Int,
    @DrawableRes iconId: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    Row(
        modifier = modifier.padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SampleText(
            textId = textId,
            modifier = Modifier.padding(end = 8.dp)
        )
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = contentDescription
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UngroupedTextAndImagePreview() {
    ComposeAccessibilityTechniquesTheme() {
        UngroupedTextAndImage(
            textId = R.string.text_alternatives_title,
            iconId = R.drawable.ic_angle_right_outline
        )
    }
}

@Composable
private fun TextAndImageGroup(
    @StringRes textId: Int,
    @DrawableRes iconId: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    Row(
        modifier = modifier
            .padding(top = 8.dp)
            .semantics(mergeDescendants = true) { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        SampleText(
            textId = textId,
            modifier = Modifier.padding(end = 8.dp)
        )
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = contentDescription
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextAndImageGroupPreview() {
    ComposeAccessibilityTechniquesTheme() {
        TextAndImageGroup(
            textId = R.string.text_alternatives_title,
            iconId = R.drawable.ic_angle_right_outline
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun TextAndImageGroupSemanticsReplaced(
    @StringRes textId: Int,
    @DrawableRes iconId: Int,
    @StringRes groupContentDescriptionId: Int,
    modifier: Modifier = Modifier
) {
    val groupContentDescription = stringResource(id = groupContentDescriptionId)
    Row(
        modifier = modifier
            .padding(top = 8.dp)
            // Key technique 1: Supply a layout-level contentDescription and set
            // semantics(mergeDescendants = true) to combine layout and child semantic texts.
            .semantics(mergeDescendants = true) {
                contentDescription = groupContentDescription
            },

        // Alternative key technique: clearAndSetSemantics { contentDescription = ... } replaces
        // the current composable's semantics, and that of all of its children, with this single
        // contentDescription. With that approach, child element semantics do not need to be
        // altered, because they are ignored. This is a very heavy-weight approach and can lead
        // to problems if applied when there are many nested children that may need to express
        // their own semantics. But it is sometimes appropriate.
        // .clearAndSetSemantics {
        //     contentDescription = groupContentDescription
        // },
        verticalAlignment = Alignment.CenterVertically
    ) {
        SampleText(
            textId = textId,
            modifier = Modifier
                .padding(end = 8.dp)
                // Key technique 2: Make all redundant child composable semantics invisibleToUser()
                .semantics {
                    invisibleToUser()
                }
        )
        Icon(
            painter = painterResource(id = iconId),
            // Key technique 3: Suppress semantic text from any Icon child elements with a null
            // contentDescription.
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextAndImageGroupReplacedPreview() {
    ComposeAccessibilityTechniquesTheme() {
        TextAndImageGroupSemanticsReplaced(
            textId = R.string.text_alternatives_title,
            iconId = R.drawable.ic_angle_right_outline,
            groupContentDescriptionId = R.string.text_alternatives_heading
        )
    }
}

@Composable
private fun TextImageAndTextGroup(
    @StringRes textId: Int,
    @DrawableRes iconId: Int,
    @StringRes textId2: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    Row(
        modifier = modifier
            .padding(top = 8.dp)
            .semantics(mergeDescendants = true) { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        SampleText(
            textId = textId,
            modifier = Modifier.padding(end = 8.dp)
        )
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = contentDescription
        )
        SampleText(
            textId = textId2,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextImageAndTextGroupPreview() {
    ComposeAccessibilityTechniquesTheme() {
        TextImageAndTextGroup(
            textId = R.string.home_title,
            iconId = R.drawable.ic_angle_right_outline,
            textId2 = R.string.text_alternatives_title
        )
    }
}