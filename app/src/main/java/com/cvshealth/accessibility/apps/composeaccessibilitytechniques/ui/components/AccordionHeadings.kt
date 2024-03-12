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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
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
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.SuccessGreen

/**
 * GenericAccordionHeading properly labels a Row composable as both an accordion control and a
 * heading. It contains its own expanded/collapsed state handling.
 *
 * GenericAccordionHeading applies the following key techniques:
 * 1. The Row applies Modifier.clickable() with a state-specific onClickLabel (but without a role).
 * 2. The Row uses Modifier.semantics to set expand() or collapse() semantic state and actions,
 *    depending on whether the control is in the collapsed or expanded state. (Heading semantics is
 *    also applied in this case, but would not be present for all accordion controls.)
 * 3. A visual state indicator Icon is present, but has a null contentDescription, so all state
 *    announcement is made by the Row-level expand/collapse semantics. Also, given that the visual
 *    state indicator Icon is not clickable, appropriate padding is applied manually with
 *    .minimumInteractiveComponentSize(). (This will vary with the visual design of the accordion
 *    control.)
 * 4. Display or reveal the accordion control's content based on its expansion state.
 *
 * @param text the accordion heading text to display
 * @param modifier settings for the wrapping Column
 * @param initiallyExpanded whether the accordion first displays expanded or collapsed; default is
 * collapsed
 * @param headingIconPainter optional Painter for a leading icon on the accordion heading
 * @param headingIconTint tint for any leading icon
 * @param expanderIconTint tint for the expander icon
 * @param content the accordion body to display when expanded
 */
@Composable
fun GenericAccordionHeading(
    text: String,
    modifier: Modifier = Modifier,
    initiallyExpanded: Boolean = false,
    headingIconPainter: Painter? = null,
    headingIconTint: Color = LocalContentColor.current,
    expanderIconTint: Color = LocalContentColor.current,
    content: @Composable () -> Unit
) {
    val (isExpanded, setIsExpanded) = rememberSaveable { mutableStateOf(initiallyExpanded) }
    Column(
        modifier = modifier
    ) {
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .visibleFocusBorder()
                // Key technique 1a: Expand/Collapse an accordion control on Row click with clickable().
                .clickable(
                    // Key technique 1b: Apply a more specific onClickLabel, based on expansion state.
                    onClickLabel = if (isExpanded)
                        stringResource(id = R.string.collapse_button_description)
                    else
                        stringResource(id = R.string.expand_button_description)
                ) {
                    setIsExpanded(!isExpanded)
                }
                // Key technique 2: Add collapse() or expand() semantics, depending on expansion state.
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
                    // Note: Suppress decorative icon description; caller must convey icon meaning
                    // in the visible text.
                    contentDescription = null,
                    modifier = Modifier
                        .minimumInteractiveComponentSize(),
                    tint = headingIconTint
                )
            } else {
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(
                text,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )
            // Key technique 3a: Visually indicate expanded/collapsed state (in this case, by an Icon).
            Icon(
                painter = painterResource(
                    id = if (isExpanded)
                        R.drawable.ic_minus_fill
                    else
                        R.drawable.ic_plus_fill,
                ),
                // Key technique 3b: Null the contentDescription on the visual state representation
                // icon. Expand/collapsed is a state of the Row, not this Icon, and so should be
                // announced by expand/collapse semantics.
                contentDescription = null,
                // Key technique 3c: Properly size the visual state indicator (in this case, as if
                // it were a clickable icon, although click-handling is performed at the Row level).
                modifier = Modifier.minimumInteractiveComponentSize(),
                tint = expanderIconTint
            )
        }
        // Key technique 4: Control the visibility of the accordion control's content based on
        // expanded/collapsed state. (Note: Normally the expansion and collapse of an accordion
        // control would be animated. Animation is omitted here for brevity.)
        if (isExpanded) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GenericAccordionHeadingCollapsedShortPreview() {
    ComposeAccessibilityTechniquesTheme() {
        GenericAccordionHeading(text = "This is a test accordion") {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                BodyText(textId = R.string.accordion_item_2_1)
                BodyText(textId = R.string.accordion_item_2_2)
                BodyText(textId = R.string.accordion_item_2_3)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GenericAccordionHeadingExpandedShortPreview() {
    ComposeAccessibilityTechniquesTheme() {
        GenericAccordionHeading(
            text = "This is a test accordion",
            initiallyExpanded = true
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
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
    initiallyExpanded: Boolean = false,
    content: @Composable () -> Unit
) {
    GenericAccordionHeading(
        text = text,
        modifier = modifier
            .padding(top = 8.dp),
        initiallyExpanded = initiallyExpanded,
        headingIconPainter = painterResource(id = R.drawable.ic_check_fill),
        headingIconTint = SuccessGreen,
        expanderIconTint = MaterialTheme.colorScheme.primary
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
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                BodyText(textId = R.string.accordion_item_2_1)
                BodyText(textId = R.string.accordion_item_2_2)
                BodyText(textId = R.string.accordion_item_2_3)
            }
        }
    }
}

@Composable
fun BasicAccordionHeading(
    text: String,
    modifier: Modifier = Modifier,
    initiallyExpanded: Boolean = false,
    content: @Composable () -> Unit
) {
    GenericAccordionHeading(
        text = text,
        modifier = modifier
            .padding(top = 8.dp),
        initiallyExpanded = initiallyExpanded,
        expanderIconTint = MaterialTheme.colorScheme.primary
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun BasicAccordionHeadingShortPreview() {
    ComposeAccessibilityTechniquesTheme() {
        BasicAccordionHeading(
            text = "This is a basic accordion heading"
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                BodyText(textId = R.string.accordion_item_2_1)
                BodyText(textId = R.string.accordion_item_2_2)
                BodyText(textId = R.string.accordion_item_2_3)
            }
        }
    }
}