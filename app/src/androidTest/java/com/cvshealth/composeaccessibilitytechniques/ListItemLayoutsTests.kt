package com.cvshealth.composeaccessibilitytechniques

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescriptionExactly
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.ListItemLayoutsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample1ListItemTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample2ListItemTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample3ListItemTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample4HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample4ListItemTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample5HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample5ListItemTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample6HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample6ListItemTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample7HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsExample7ListItemTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.listItemLayoutsHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ListItemLayoutsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme() {
                ListItemLayoutsScreen { }
            }
        }
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(listItemLayoutsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(listItemLayoutsExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(listItemLayoutsExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(listItemLayoutsExample3HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(listItemLayoutsExample4HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(listItemLayoutsExample5HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(listItemLayoutsExample6HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(listItemLayoutsExample7HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExampleListItemsAreNotHeadings() {
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample1ListItemTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample2ListItemTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample3ListItemTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample4ListItemTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample5ListItemTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample6ListItemTestTag)
                        and
                        !isHeading()
                        and
                        !hasAnyDescendant(isHeading())
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample7ListItemTestTag)
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
    fun verifyExample1ListItemHasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample1ListItemTestTag)
                        and
                        hasNoClickAction()
                        and
                        isTraversalGroup()
                        and
                        hasTextExactly()
                        and
                        hasContentDescriptionExactly()
                        and
                        hasAnyChild(
                            hasTextExactly("Accessible inactive ListItem", "Announces as a single text.")
                        )
            )
            .assertExists()
    }

    @Test
    fun verifyExample2ListItemHasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample2ListItemTestTag)
                        and
                        hasClickAction()
                        and
                        isTraversalGroup()
                        and
                        hasRole(Role.Button)
                        and
                        hasTextExactly()
                        and
                        hasContentDescriptionExactly()
                        and
                        hasAnyChild(
                            hasTextExactly("Inaccessible clickable ListItem", "Announces as an inactive text; although it is clickable.")
                        )
            )
            .assertExists()
    }

    @Test
    fun verifyExample3ListItemHasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample3ListItemTestTag)
                        and
                        hasClickAction()
                        and
                        hasRole(Role.Button)
                        and
                        hasContentDescriptionExactly("Accessible clickable ListItem. Announces as a single, labeled, clickable Button.")
                        and
                        hasClearAndSetSemantics()
            )
            .assertExists()
    }

    @Test
    fun verifyExample4ListItemHasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample4ListItemTestTag)
                        and
                        isSelectable()
                        and
                        isTraversalGroup()
                        and
                        hasRole(Role.RadioButton)
                        and
                        hasTextExactly()
                        and
                        hasContentDescriptionExactly()
                        and
                        hasAnyChild(
                            hasTextExactly("Inaccessible selectable ListItem", "Announces as a unlabeled RadioButton. Swipe again to hear the label.")
                        )
            )
            .assertExists()
    }

    @Test
    fun verifyExample5ListItemHasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample5ListItemTestTag)
                        and
                        isSelectable()
                        and
                        hasRole(Role.RadioButton)
                        and
                        hasContentDescriptionExactly("Accessible selectable ListItem. Announces as a single, labeled, selectable radio button.")
                        and
                        hasClearAndSetSemantics()
            )
            .assertExists()
    }

    @Test
    fun verifyExample6ListItemHasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample6ListItemTestTag)
                        and
                        isToggleable()
                        and
                        isTraversalGroup()
                        and
                        hasRole(Role.Switch)
                        and
                        hasTextExactly()
                        and
                        hasContentDescriptionExactly()
                        and
                        hasAnyChild(
                            hasTextExactly("Inaccessible toggleable ListItem", "Announces as a unlabeled Switch. Swipe again to hear the label.")
                        )
            )
            .assertExists()
    }

    @Test
    fun verifyExample7ListItemHasExpectedSemantics() {
        composeTestRule
            .onNode(
                hasTestTag(listItemLayoutsExample7ListItemTestTag)
                        and
                        isToggleable()
                        and
                        hasRole(Role.Switch)
                        and
                        hasContentDescriptionExactly("Accessible toggleable ListItem. Announces as a single, labeled, toggleable Switch.")
                        and
                        hasClearAndSetSemantics()
            )
            .assertExists()
    }

}