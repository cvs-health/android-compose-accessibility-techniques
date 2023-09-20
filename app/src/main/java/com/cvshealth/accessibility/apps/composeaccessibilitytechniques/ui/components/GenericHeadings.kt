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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ErrorRed
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.SuccessGreen

@Composable
fun SimpleHeading(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(text,
        modifier = modifier
            .fillMaxWidth()
            .semantics { heading() },
        style = MaterialTheme.typography.headlineSmall
    )
}

@Preview(showBackground = true)
@Composable
fun SimpleHeadingHeadingLongPreview() {
    ComposeAccessibilityTechniquesTheme() {
        SimpleHeading(text = "This is a test. This is only a test. If this were a real heading, it would be shorter.")
    }
}

@Composable
private fun GenericExampleHeading(
    text: String,
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    tint: Color = LocalContentColor.current
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                heading()
            }
    ) {
        if (painter != null) {
            Icon(
                painter = painter,
                // Note: Suppress decorative icon description; caller must convey icon meaning
                // in the visible text.
                contentDescription = null,
                modifier = Modifier
                    .defaultMinSize(24.dp, minHeight = 24.dp)
                    .align(Alignment.CenterVertically),
                tint = tint
            )
        }
        Text(text,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp)
        )
    }
}

@Composable
fun GoodExampleHeading(text: String, modifier: Modifier = Modifier) {
    GenericExampleHeading(
        text = text,
        modifier = modifier
            .padding(top = 8.dp),
        painter = painterResource(id = R.drawable.ic_check_fill),
        tint = SuccessGreen
    )
}

@Preview(showBackground = true)
@Composable
fun GoodExampleHeadingShortPreview() {
    ComposeAccessibilityTechniquesTheme() {
        GoodExampleHeading(text = "This is a test")
    }
}

@Composable
fun OkExampleHeading(text: String, modifier: Modifier = Modifier) {
    GenericExampleHeading(
        text = text,
        modifier = modifier
            .padding(top = 8.dp),
        painter = painterResource(id = R.drawable.ic_circle_filled),
        tint = SuccessGreen
    )
}


@Preview(showBackground = true)
@Composable
fun OkExampleHeadingShortPreview() {
    ComposeAccessibilityTechniquesTheme() {
        OkExampleHeading(text = "This is a test")
    }
}

@Composable
fun ProblematicExampleHeading(text: String, modifier: Modifier = Modifier) {
    GenericExampleHeading(
        text = text,
        modifier = modifier
            .padding(top = 8.dp),
        painter = painterResource(id = R.drawable.ic_circle_filled),
        tint = ErrorRed
    )
}


@Preview(showBackground = true)
@Composable
fun ProblematicExampleHeadingShortPreview() {
    ComposeAccessibilityTechniquesTheme() {
        ProblematicExampleHeading(text = "This is a test")
    }
}

@Composable
fun BadExampleHeading(text: String, modifier: Modifier = Modifier) {
    GenericExampleHeading(
        text = text,
        modifier = modifier
            .padding(top = 8.dp),
        painter = painterResource(id = R.drawable.ic_close_fill),
        tint = ErrorRed
    )
}

@Preview(showBackground = true)
@Composable
fun BadExampleHeadingLongPreview() {
    ComposeAccessibilityTechniquesTheme() {
        BadExampleHeading(
            text = "This is a test. This is only a test. If this were a real heading, it would be shorter."
        )
    }
}
