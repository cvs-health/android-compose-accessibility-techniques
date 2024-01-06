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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accordion_controls

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SuccessAccordionHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.visibleFocusBorder
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.CvsRed
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ErrorRed

const val accordionHeadingTestTag = "accordionHeading"
const val accordionExample1TestTag = "accordionExample1Wrapper"
const val accordionExample1Item1TestTag = "accordionExample1Item1"
const val accordionExample1Item2TestTag = "accordionExample1Item2"
const val accordionExample1Item3TestTag = "accordionExample1Item3"
const val accordionExample2TestTag = "accordionExample2Wrapper"
const val accordionExample2Item1TestTag = "accordionExample2Item1"
const val accordionExample2Item2TestTag = "accordionExample2Item2"
const val accordionExample2Item3TestTag = "accordionExample2Item3"

@Composable
fun AccordionControlsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.accordion_title),
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
                text = stringResource(id = R.string.accordion_heading),
                modifier = Modifier.testTag(accordionHeadingTestTag)
            )
            BodyText(textId = R.string.accordion_description_1)
            BodyText(textId = R.string.accordion_description_2)
            BodyText(textId = R.string.accordion_description_3)

            // Bad example 1: Accordion without expand/collapse actions
            FauxAccordionHeading(
                text = stringResource(id = R.string.accordion_section_1),
                modifier = Modifier.testTag(accordionExample1TestTag)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    BodyText(
                        textId = R.string.accordion_item_1_1,
                        modifier = Modifier.testTag(accordionExample1Item1TestTag)
                    )
                    BodyText(
                        textId = R.string.accordion_item_1_2,
                        modifier = Modifier.testTag(accordionExample1Item2TestTag))
                    BodyText(
                        textId = R.string.accordion_item_1_3,
                        modifier = Modifier.testTag(accordionExample1Item3TestTag)
                    )
                }
            }

            // Good example 2: Accordion with expand/collapse actions
            SuccessAccordionHeading(
                text = stringResource(id = R.string.accordion_section_2),
                modifier = Modifier.testTag(accordionExample2TestTag)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    BodyText(
                        textId = R.string.accordion_item_2_1,
                        modifier = Modifier.testTag(accordionExample2Item1TestTag)
                    )
                    BodyText(
                        textId = R.string.accordion_item_2_2,
                        modifier = Modifier.testTag(accordionExample2Item2TestTag))
                    BodyText(
                        textId = R.string.accordion_item_2_3,
                        modifier = Modifier.testTag(accordionExample2Item3TestTag)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme() {
        AccordionControlsScreen {}
    }
}

@Composable
private fun FauxAccordionHeading(
    text: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val headingIconPainter = painterResource(id = R.drawable.ic_close_fill)
    val headingIconTint = ErrorRed
    val expanderIconTint = CvsRed

    val (isExpanded, setIsExpanded) = rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier.padding(top = 8.dp)
    ) {
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .visibleFocusBorder()
                .clickable(
                    onClickLabel = if (isExpanded)
                        stringResource(id = R.string.collapse_button_description)
                    else
                        stringResource(id = R.string.expand_button_description)
                ) {
                    setIsExpanded(!isExpanded)
                }
                .semantics {
                    heading()
                    // The key problem here is not applying Modifier.semantics with collapse() or
                    // expand(), depending on the isExpanded state value.
                    //
                    // Another way to do this wrong: fake a stateDescription on the Row instead of
                    // letting the semantic action expand() or collapse() inform the Row's state.
                    // stateDescription = if (isExpanded) "Expanded" else "Collapsed"
                },

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = headingIconPainter,
                contentDescription = null,
                modifier = Modifier.minimumInteractiveComponentSize(),
                tint = headingIconTint
            )
            Text(text,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .weight(1f)
            )
            Icon(
                painter = painterResource(
                    id = if (isExpanded)
                        R.drawable.ic_minus_fill
                    else
                        R.drawable.ic_plus_fill,
                ),
                contentDescription = null, // expanded/collapsed is a state of the Row, not the Icon
                // Another way to do this wrong: fake a contentDescription on the icon instead of
                // letting the semantic action expand() or collapse() inform the Row's state.
                // contentDescription = if (isExpanded)
                //     stringResource(R.string.collapse_button_description)
                // else
                //     stringResource(R.string.expand_button_description),
                modifier = Modifier.minimumInteractiveComponentSize(),
                tint = expanderIconTint
            )
        }
        if (isExpanded) {
            content()
        }
    }

}
