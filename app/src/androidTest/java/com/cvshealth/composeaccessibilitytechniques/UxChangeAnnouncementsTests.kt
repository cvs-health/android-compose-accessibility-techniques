package com.cvshealth.composeaccessibilitytechniques

import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.UxChangeAnnouncementsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample1IncrementCounterTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample1LiveRegionTextTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample1ResetCounterTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample2ButtonTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsExample2LiveRegionTextTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.uxChangeAnnouncementsHeadingTestTag
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UxChangeAnnouncementsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme() {
                UxChangeAnnouncementsScreen { }
            }
        }
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(uxChangeAnnouncementsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(uxChangeAnnouncementsExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(uxChangeAnnouncementsExample2HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExampleTextFieldsAreNotHeadings() {
        composeTestRule
            .onNode(hasTestTag(uxChangeAnnouncementsExample1LiveRegionTextTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(uxChangeAnnouncementsExample1IncrementCounterTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(uxChangeAnnouncementsExample1ResetCounterTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(uxChangeAnnouncementsExample2ButtonTestTag) and !isHeading())
            .assertExists()

        // Make the Example 2 LiveRegion text appear, before testing it
        composeTestRule
            .onNodeWithTag(uxChangeAnnouncementsExample2ButtonTestTag)
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNodeWithTag(uxChangeAnnouncementsExample2LiveRegionTextTestTag)
            .performScrollTo()
        composeTestRule
            .onNode(hasTestTag(uxChangeAnnouncementsExample2LiveRegionTextTestTag) and !isHeading())
            .assertExists()
    }

    @Test
    fun verifyEveryHeadingsHasATestTag() {
        composeTestRule.onNode(hasNoTestTag() and isHeading()).assertDoesNotExist()
    }

    @Test
    fun verifyExample1LiveRegionIsAPoliteLiveRegion() {
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample1LiveRegionTextTestTag)
                        and
                        hasLiveRegionMode(LiveRegionMode.Polite)
            )
            .assertExists()
    }

    @Test
    fun verifyExample2LiveRegionIsAPoliteLiveRegion() {
        // Make the Example 2 LiveRegion text appear, before testing it
        composeTestRule
            .onNodeWithTag(uxChangeAnnouncementsExample2ButtonTestTag)
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNodeWithTag(uxChangeAnnouncementsExample2LiveRegionTextTestTag)
            .performScrollTo()
        composeTestRule
            .onNode(
                hasTestTag(uxChangeAnnouncementsExample2LiveRegionTextTestTag)
                        and
                        hasLiveRegionMode(LiveRegionMode.Polite)
            )
            .assertExists()
    }
}