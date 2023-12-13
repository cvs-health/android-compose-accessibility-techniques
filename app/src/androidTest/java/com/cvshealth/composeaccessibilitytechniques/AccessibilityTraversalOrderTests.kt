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
package com.cvshealth.composeaccessibilitytechniques

import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accessibility_traversal_order.AccessibilityTraversalOrderScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accessibility_traversal_order.accessibilityTraversalOrderExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accessibility_traversal_order.accessibilityTraversalOrderExample1RowTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accessibility_traversal_order.accessibilityTraversalOrderExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accessibility_traversal_order.accessibilityTraversalOrderExample2MainColumnTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accessibility_traversal_order.accessibilityTraversalOrderExample2RowTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accessibility_traversal_order.accessibilityTraversalOrderExample2SidebarColumnTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accessibility_traversal_order.accessibilityTraversalOrderExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accessibility_traversal_order.accessibilityTraversalOrderExample3MainColumnTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accessibility_traversal_order.accessibilityTraversalOrderExample3RowTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accessibility_traversal_order.accessibilityTraversalOrderExample3SidebarColumnTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accessibility_traversal_order.accessibilityTraversalOrderHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AccessibilityTraversalOrderTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme() {
                AccessibilityTraversalOrderScreen { }
            }
        }
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(accessibilityTraversalOrderHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(accessibilityTraversalOrderExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(accessibilityTraversalOrderExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(accessibilityTraversalOrderExample3HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExampleTextsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(accessibilityTraversalOrderExample1RowTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(accessibilityTraversalOrderExample2RowTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(accessibilityTraversalOrderExample3RowTestTag)
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
    fun verifyExample1ContentHasNoTraversalControls() {
        composeTestRule
            .onNode(
                hasTestTag(accessibilityTraversalOrderExample1RowTestTag)
                        and
                        !hasAnyDescendant(isTraversalGroup())
                        and
                        !hasAnyDescendant(hasAnyTraversalIndex())
            )
            .assertExists()
    }

    @Test
    fun verifyExample2ContentHasExpectedTraversalControls() {
        composeTestRule
            .onNode(
                hasTestTag(accessibilityTraversalOrderExample2RowTestTag)
                        and
                        isNotATraversalGroup()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(accessibilityTraversalOrderExample2MainColumnTestTag)
                        and
                        isTraversalGroup()
                        and
                        hasNoTraversalIndex()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(accessibilityTraversalOrderExample2SidebarColumnTestTag)
                        and
                        isTraversalGroup()
                        and
                        hasNoTraversalIndex()
            )
            .assertExists()
    }

    @Test
    fun verifyExample3ContentHasExpectedTraversalControls() {
        composeTestRule
            .onNode(
                hasTestTag(accessibilityTraversalOrderExample3RowTestTag)
                        and
                        isTraversalGroup()
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(accessibilityTraversalOrderExample3MainColumnTestTag)
                        and
                        isTraversalGroup()
                        and
                        hasTraversalIndex(1f)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(accessibilityTraversalOrderExample3SidebarColumnTestTag)
                        and
                        isTraversalGroup()
                        and
                        hasTraversalIndex(0f)
            )
            .assertExists()
    }
}