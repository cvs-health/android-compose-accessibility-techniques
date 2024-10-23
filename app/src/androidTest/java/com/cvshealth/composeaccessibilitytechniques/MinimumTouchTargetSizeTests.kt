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
package com.cvshealth.composeaccessibilitytechniques

import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.genericScaffoldTitleTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.target_size.MinimumTouchTargetSizeScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.target_size.minimumTouchTargetSizeExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.target_size.minimumTouchTargetSizeExample1IconButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.target_size.minimumTouchTargetSizeExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.target_size.minimumTouchTargetSizeExample2IconButton1TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.target_size.minimumTouchTargetSizeExample2IconButton2TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.target_size.minimumTouchTargetSizeExample2IconButton3TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.target_size.minimumTouchTargetSizeExample2IconButton4TestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.target_size.minimumTouchTargetSizeExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.target_size.minimumTouchTargetSizeExample3IconButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.target_size.minimumTouchTargetSizeExample4HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.target_size.minimumTouchTargetSizeExample4IconButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.target_size.minimumTouchTargetSizeExample5HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.target_size.minimumTouchTargetSizeExample5IconButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.target_size.minimumTouchTargetSizeExample6HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.target_size.minimumTouchTargetSizeExample6IconButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.target_size.minimumTouchTargetSizeHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 *  Test MinimumTouchTargetSizeScreen semantics.
 *
 *  Note that Compose imposes a minimum touch target size on all clickable controls which can
 *  exceed their size.
 */
class MinimumTouchTargetSizeTests {
    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var density: Density

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme {
                density = LocalDensity.current
                MinimumTouchTargetSizeScreen { }
            }
        }
    }

    @Test
    fun verifyScreenHasPaneTitle() {
        composeTestRule.onNode(hasPaneTitle()).assertExists()
    }

    @Test
    fun verifyHeadingsCount() {
        composeTestRule.onAllNodes(isHeading()).assertCountEquals(8)
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(genericScaffoldTitleTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(minimumTouchTargetSizeHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(minimumTouchTargetSizeExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(minimumTouchTargetSizeExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(minimumTouchTargetSizeExample3HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(minimumTouchTargetSizeExample4HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(minimumTouchTargetSizeExample5HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(minimumTouchTargetSizeExample6HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExampleListItemsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample1IconButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample2IconButton1TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample2IconButton2TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample2IconButton3TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample2IconButton4TestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample3IconButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample4IconButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample5IconButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample6IconButtonTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
    }

    @Test
    fun verifyEveryHeadingsHasATestTag() {
        composeTestRule.onNode(hasNoTestTag() and isHeading()).assertDoesNotExist()
    }

    @Test
    fun verifyExampleListItemsAreClickable() {
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample1IconButtonTestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample2IconButton1TestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample2IconButton2TestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample2IconButton3TestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample2IconButton4TestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample3IconButtonTestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample4IconButtonTestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample5IconButtonTestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample6IconButtonTestTag)
                        and
                        hasRole(Role.Button)
                        and
                        hasClickAction()
            )
            .assertExists()
    }

    @Test
    fun verifyExample1Size() {
        composeTestRule
            .onNodeWithTag(minimumTouchTargetSizeExample1IconButtonTestTag)
            .performScrollTo()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample1IconButtonTestTag)
                        and
                        !hasMinimumSize(density.density, 24.dp) // size is too small
                        and
                        hasMinimumTouchTargetSize(density.density, 48.dp) // tap target size is larger
            )
            .assertExists()
    }

    @Test
    fun verifyExample2Size() {
        composeTestRule
            .onNodeWithTag(minimumTouchTargetSizeExample2IconButton1TestTag)
            .performScrollTo()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample2IconButton1TestTag)
                        and
                        !hasMinimumSize(density.density, 24.dp) // size is too small
                        and
                        hasMinimumTouchTargetSize(density.density, 48.dp) // tap target size is larger
            )
            .assertExists()
        composeTestRule
            .onNodeWithTag(minimumTouchTargetSizeExample2IconButton2TestTag)
            .performScrollTo()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample2IconButton2TestTag)
                        and
                        !hasMinimumSize(density.density, 24.dp) // size is too small
                        and
                        hasMinimumTouchTargetSize(density.density, 48.dp) // tap target size is larger
            )
            .assertExists()
        composeTestRule
            .onNodeWithTag(minimumTouchTargetSizeExample2IconButton3TestTag)
            .performScrollTo()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample2IconButton3TestTag)
                        and
                        !hasMinimumSize(density.density, 24.dp) // size is too small
                        and
                        hasMinimumTouchTargetSize(density.density, 48.dp) // tap target size is larger
            )
            .assertExists()
        composeTestRule
            .onNodeWithTag(minimumTouchTargetSizeExample2IconButton4TestTag)
            .performScrollTo()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample2IconButton4TestTag)
                        and
                        !hasMinimumSize(density.density, 24.dp) // size is too small
                        and
                        hasMinimumTouchTargetSize(density.density, 48.dp) // tap target size is larger
            )
            .assertExists()
    }

    @Test
    fun verifyExample3Size() {
        composeTestRule
            .onNodeWithTag(minimumTouchTargetSizeExample3IconButtonTestTag)
            .performScrollTo()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample3IconButtonTestTag)
                        and
                        hasMinimumSize(density.density, 24.dp) // size is large enough
                        and
                        hasMinimumTouchTargetSize(density.density, 48.dp) // tap target size is larger
            )
            .assertExists()
    }

    @Test
    fun verifyExample4Size() {
        composeTestRule
            .onNodeWithTag(minimumTouchTargetSizeExample4IconButtonTestTag)
            .performScrollTo()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample4IconButtonTestTag)
                        and
                        hasMinimumSize(density.density, 24.dp) // size is large enough
                        and
                        hasMinimumTouchTargetSize(density.density, 48.dp) // tap target size is larger
            )
            .assertExists()
    }

    @Test
    fun verifyExample5Size() {
        composeTestRule
            .onNodeWithTag(minimumTouchTargetSizeExample5IconButtonTestTag)
            .performScrollTo()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample5IconButtonTestTag)
                        and
                        hasMinimumSize(density.density, 24.dp) // size is large enough
                        and
                        hasMinimumTouchTargetSize(density.density, 48.dp) // tap target size is larger
            )
            .assertExists()
    }

    @Test
    fun verifyExample6Size() {
        composeTestRule
            .onNodeWithTag(minimumTouchTargetSizeExample6IconButtonTestTag)
            .performScrollTo()
        composeTestRule
            .onNode(
                hasTestTag(minimumTouchTargetSizeExample6IconButtonTestTag)
                        and
                        hasMinimumSize(density.density, 24.dp) // size is large enough
                        and
                        hasMinimumTouchTargetSize(density.density, 48.dp) // tap target size is larger
            )
            .assertExists()
    }
}