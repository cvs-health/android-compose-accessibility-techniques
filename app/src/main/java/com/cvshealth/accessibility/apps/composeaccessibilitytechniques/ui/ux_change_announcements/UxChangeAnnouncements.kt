package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val uxChangeAnnouncementsHeadingTestTag = "uxChangeAnnouncementsHeading"
const val uxChangeAnnouncementsExample1HeadingTestTag = "uxChangeAnnouncementsExample1Heading"
const val uxChangeAnnouncementsExample1LiveRegionTextTestTag = "uxChangeAnnouncementsExample1LiveRegionText"
const val uxChangeAnnouncementsExample1IncrementCounterTestTag = "uxChangeAnnouncementsExample1IncrementCounter"
const val uxChangeAnnouncementsExample1ResetCounterTestTag = "uxChangeAnnouncementsExample1ResetCounter"
const val uxChangeAnnouncementsExample2HeadingTestTag = "uxChangeAnnouncementsExample2Heading"
const val uxChangeAnnouncementsExample2ButtonTestTag = "uxChangeAnnouncementsExampleButton"
const val uxChangeAnnouncementsExample2LiveRegionTextTestTag = "uxChangeAnnouncementsExample2LiveRegionText"

@Composable
fun UxChangeAnnouncementsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.ux_change_announcements_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()
        val (counterValue, setCounterValue) = rememberSaveable { mutableStateOf(0) }

        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.ux_change_announcements_heading),
                modifier = Modifier.testTag(uxChangeAnnouncementsHeadingTestTag)
            )
            BodyText(textId = R.string.ux_change_announcements_description)
            BodyText(textId = R.string.ux_change_announcements_description_2)

            GoodExampleHeading(
                text = stringResource(id = R.string.ux_change_announcements_example_1),
                modifier = Modifier.testTag(uxChangeAnnouncementsExample1HeadingTestTag)
            )
            val counterText = stringResource(id = R.string.ux_change_announcements_counter, counterValue)
            Text(
                text = counterText,
                modifier = Modifier
                    .testTag(uxChangeAnnouncementsExample1LiveRegionTextTestTag)
                    .padding(top = 8.dp)
                    .semantics {
                        // Key technique 1: Set liveRegion semantics on the Text that changes when
                        // the buttons below are pressed.
                        liveRegion = LiveRegionMode.Polite
                        // contentDescription use is necessary, due to
                        // https://issuetracker.google.com/issues/225780131.
                        contentDescription = counterText
                    },
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { setCounterValue(counterValue + 1) },
                    modifier = Modifier.testTag(uxChangeAnnouncementsExample1IncrementCounterTestTag)
                ) {
                    Text(text = stringResource(id = R.string.ux_change_announcements_increment_counter))
                }
                Button(
                    onClick = { setCounterValue(0) },
                    modifier = Modifier.testTag(uxChangeAnnouncementsExample1ResetCounterTestTag)
                ) {
                    Text(text = stringResource(id = R.string.ux_change_announcements_reset_counter))
                }
            }

            GoodExampleHeading(
                text = stringResource(id = R.string.ux_change_announcements_example_2),
                modifier = Modifier.testTag(uxChangeAnnouncementsExample2HeadingTestTag)
            )

            val (isTextVisible, setIsTextVisible) = rememberSaveable { mutableStateOf(false) }
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val v = LocalView.current
                val hiddenMessage = stringResource(id = R.string.ux_change_announcements_example_2_text_hidden)
                Button(
                    onClick = {
                        setIsTextVisible(!isTextVisible)
                        if (isTextVisible) {
                            // Key technique 2: Fall back on the View.announceForAccessibility()
                            // method to make TalkBack announcements only when absolutely necessary.
                            v.announceForAccessibility(hiddenMessage)
                        }
                    },
                    modifier = Modifier.testTag(uxChangeAnnouncementsExample2ButtonTestTag)
                ) {
                    Text(
                        text = stringResource(
                            id = if (isTextVisible)
                                R.string.ux_change_announcements_example_2_hide_text
                            else
                                R.string.ux_change_announcements_example_2_show_text
                        )
                    )
                }
            }
            if (isTextVisible) {
                val exampleText = stringResource(id = R.string.ux_change_announcements_example_2_text)
                Text(
                    text = exampleText,
                    modifier = Modifier
                    .testTag(uxChangeAnnouncementsExample2LiveRegionTextTestTag)
                        .padding(top = 8.dp)
                        .semantics {
                            // Key technique 1: Set liveRegion semantics on the Text that changes when
                            // the buttons below are pressed.
                            liveRegion = LiveRegionMode.Polite
                            // contentDescription use is necessary, due to
                            // https://issuetracker.google.com/issues/225780131.
                            contentDescription = exampleText
                        },
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        UxChangeAnnouncementsScreen {}
    }
}