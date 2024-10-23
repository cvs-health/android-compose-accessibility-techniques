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
package com.cvshealth.composeaccessibilitytechniques

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performSemanticsAction
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.MainActivity
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accordion_controls.accordionExample1Item1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accordion_controls.accordionExample1Item2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accordion_controls.accordionExample1Item3TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accordion_controls.accordionExample1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accordion_controls.accordionExample2Item1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accordion_controls.accordionExample2Item2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accordion_controls.accordionExample2Item3TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accordion_controls.accordionExample2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accordion_controls.accordionHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.genericScaffoldTitleTestTag
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AccordionControlsTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        // Navigate from HomeScreen to AccordionScreen.
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.home_components))
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.accordion_title))
            .performScrollTo()
            .performClick()
    }

    @Test
    fun verifyScreenHasPaneTitle() {
        composeTestRule.onNode(
            hasPaneTitle(composeTestRule.activity.getString(R.string.accordion_title))
        ).assertExists()
    }

    @Test
    fun verifyHeadingsCount() {
        composeTestRule.onAllNodes(isHeading()).assertCountEquals(4)
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(genericScaffoldTitleTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(accordionHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(accordionExample1TestTag)
                        and
                        hasAnyDescendant(hasText(composeTestRule.activity.getString(R.string.accordion_section_1)))
                        and
                        hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(accordionExample2TestTag)
                        and
                        hasAnyDescendant(hasText(composeTestRule.activity.getString(R.string.accordion_section_2)))
                        and
                        hasAnyDescendant(isHeading())
            )
            .assertExists()
    }

    @Test
    fun verifyFauxAccordionsAreNotAccordions() {
        composeTestRule
            .onNode(
                hasAnyAncestor(hasTestTag(accordionExample1TestTag))
                        and (SemanticsMatcher.keyIsDefined(SemanticsActions.Expand)
                            or SemanticsMatcher.keyIsDefined(SemanticsActions.Collapse)
                        )
            )
            .assertDoesNotExist()
    }

    @Test
    fun verifyRealAccordionsAreAccordions() {
        composeTestRule
            .onNode(
                hasAnyAncestor(hasTestTag(accordionExample2TestTag))
                        and (SemanticsMatcher.keyIsDefined(SemanticsActions.Expand)
                            or SemanticsMatcher.keyIsDefined(SemanticsActions.Collapse)
                        )
            )
            .assertExists()
    }

    @Test
    fun verifyFauxAccordionsToggleStateOnClick() {
        // Starting state has accordions collapsed so contents are not composed.
        composeTestRule
            .onNodeWithTag(accordionExample1Item1TestTag)
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag(accordionExample1Item2TestTag)
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag(accordionExample1Item3TestTag)
            .assertDoesNotExist()

        // Expand the faux accordion with click action.
        composeTestRule
            .onNode(
                hasAnyAncestor(hasTestTag(accordionExample1TestTag))
                        and hasClickAction()
            )
            .performScrollTo()
            .performClick()

        // Verify that expanded faux accordion contents are now composed.
        composeTestRule
            .onNodeWithTag(accordionExample1Item1TestTag)
            .assertExists()
        composeTestRule
            .onNodeWithTag(accordionExample1Item2TestTag)
            .assertExists()
        composeTestRule
            .onNodeWithTag(accordionExample1Item3TestTag)
            .assertExists()

        // Collapse the faux accordion with click action.
        composeTestRule
            .onNode(
                hasAnyAncestor(hasTestTag(accordionExample1TestTag))
                        and hasClickAction()
            )
            .performScrollTo()
            .performClick()

        // Verify that collapsed faux accordion contents are not composed.
        composeTestRule
            .onNodeWithTag(accordionExample1Item1TestTag)
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag(accordionExample1Item2TestTag)
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag(accordionExample1Item3TestTag)
            .assertDoesNotExist()
    }

    @Test
    fun verifyRealAccordionsToggleStateOnClick() {
        // Starting state has accordions collapsed so contents are not composed.
        composeTestRule
            .onNodeWithTag(accordionExample2Item1TestTag)
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag(accordionExample1Item2TestTag)
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag(accordionExample2Item3TestTag)
            .assertDoesNotExist()

        // Expand the real accordion with click action
        composeTestRule
            .onNode(
                hasAnyAncestor(hasTestTag(accordionExample2TestTag))
                        and hasClickAction()
            )
            .performScrollTo()
            .performClick()

        // Verify that expanded real accordion contents are now composed.
        composeTestRule
            .onNodeWithTag(accordionExample2Item1TestTag)
            .assertExists()
        composeTestRule
            .onNodeWithTag(accordionExample2Item2TestTag)
            .assertExists()
        composeTestRule
            .onNodeWithTag(accordionExample2Item3TestTag)
            .assertExists()

        // Collapse the real accordion with click action.
        composeTestRule
            .onNode(
                hasAnyAncestor(hasTestTag(accordionExample2TestTag))
                        and hasClickAction()
            )
            .performScrollTo()
            .performClick()

        // Verify that collapsed real accordion contents are not composed.
        composeTestRule
            .onNodeWithTag(accordionExample2Item1TestTag)
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag(accordionExample2Item2TestTag)
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag(accordionExample2Item3TestTag)
            .assertDoesNotExist()
    }

    @Test
    fun verifyRealAccordionsToggleStateOnSemanticActions() {
        // Starting state has accordions collapsed so contents are not composed.
        composeTestRule
            .onNodeWithTag(accordionExample2Item1TestTag)
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag(accordionExample1Item2TestTag)
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag(accordionExample2Item3TestTag)
            .assertDoesNotExist()

        // Expand the real accordion with semantic expand action
        composeTestRule
            .onNode(
                hasAnyAncestor(hasTestTag(accordionExample2TestTag))
                        and SemanticsMatcher.keyIsDefined(SemanticsActions.Expand)
            )
            .performScrollTo()
            .performSemanticsAction(SemanticsActions.Expand)

        // Verify that expanded real accordion contents are now composed.
        composeTestRule
            .onNodeWithTag(accordionExample2Item1TestTag)
            .assertExists()
        composeTestRule
            .onNodeWithTag(accordionExample2Item2TestTag)
            .assertExists()
        composeTestRule
            .onNodeWithTag(accordionExample2Item3TestTag)
            .assertExists()

        // Collapse the real accordion with semantic collapse action
        composeTestRule
            .onNode(
                hasAnyAncestor(hasTestTag(accordionExample2TestTag))
                        and SemanticsMatcher.keyIsDefined(SemanticsActions.Collapse)
            )
            .performScrollTo()
            .performSemanticsAction(SemanticsActions.Collapse)

        // Verify that collapsed real accordion contents are not composed.
        composeTestRule
            .onNodeWithTag(accordionExample2Item1TestTag)
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag(accordionExample2Item2TestTag)
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag(accordionExample2Item3TestTag)
            .assertDoesNotExist()
    }
}