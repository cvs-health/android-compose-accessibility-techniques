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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.collapse
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.CvsRed
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.SuccessGreen

@Composable
fun GenericAccordionHeading(
    text: String,
    modifier: Modifier = Modifier,
    headingIconPainter: Painter? = null,
    headingIconTint: Color = LocalContentColor.current,
    expanderIconTint: Color = LocalContentColor.current,
    content: @Composable () -> Unit
) {
    val (isExpanded, setIsExpanded) = rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClickLabel = if (isExpanded)
                        stringResource(id = R.string.collapse_button_description)
                    else
                        stringResource(id = R.string.expand_button_description)
                ) {
                    setIsExpanded(!isExpanded)
                }
                .semantics(mergeDescendants = true) {
                    heading()
                    if (isExpanded) {
                        collapse {
                            setIsExpanded(false)
                            return@collapse true
                        }
                    } else {
                        expand {
                            setIsExpanded(true)
                            return@expand true
                        }
                    }
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (headingIconPainter != null) {
                Icon(
                    painter = headingIconPainter,
                    contentDescription = null,
                    modifier = Modifier.defaultMinSize(24.dp, minHeight = 24.dp),
                    tint = headingIconTint
                )
            }
            Text(
                text,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            )
            Icon(
                painter = painterResource(
                    id = if (isExpanded)
                        R.drawable.ic_minus_fill
                    else
                        R.drawable.ic_plus_fill,
                ),
                contentDescription = null, // expand/collapsed is a state of the Row
                modifier = Modifier.minimumInteractiveComponentSize(),
                tint = expanderIconTint
            )
        }
        if (isExpanded) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GenericAccordionHeadingShortPreview() {
    ComposeAccessibilityTechniquesTheme() {
        GenericAccordionHeading(text = "This is a test accordion") {
            Column {
                BodyText(textId = R.string.accordion_item_2_1)
                BodyText(textId = R.string.accordion_item_2_2)
                BodyText(textId = R.string.accordion_item_2_3)
            }
        }
    }
}

@Composable
fun SuccessAccordionHeading(
    text: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    GenericAccordionHeading(
        text = text,
        modifier = modifier
            .padding(top = 8.dp),
        headingIconPainter = painterResource(id = R.drawable.ic_check_fill),
        headingIconTint = SuccessGreen,
        expanderIconTint = CvsRed
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun SuccessAccordionHeadingShortPreview() {
    ComposeAccessibilityTechniquesTheme() {
        SuccessAccordionHeading(
            text = "This is a success accordion heading"
        ) {
            Column {
                BodyText(textId = R.string.accordion_item_2_1)
                BodyText(textId = R.string.accordion_item_2_2)
                BodyText(textId = R.string.accordion_item_2_3)
            }
        }
    }
}