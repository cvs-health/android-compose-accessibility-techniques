package com.cvshealth.composeaccessibilitytechniques

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.adaptive_layouts.AdaptiveLayoutsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.adaptive_layouts.adaptiveLayoutsExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.adaptive_layouts.adaptiveLayoutsExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.adaptive_layouts.adaptiveLayoutsHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 *  AdaptiveLayoutsTests - test adaptive layouts screen semantics to the extent possible. Since
 *  adaptive layouts are primarily a visual effect, not a semantic effect, the scope of these tests
 *  is limited to other properties, such as headings.
 */
class AdaptiveLayoutsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme() {
                AdaptiveLayoutsScreen(
                    onBackPressed = { },
                    // Use a Medium screen width
                    windowSizeClass = WindowSizeClass.calculateFromSize(
                        DpSize(width=600.dp, height=400.dp) // effectively a phone landscape mode
                    )
                )
            }
        }
    }

    @Test
    fun verifyScreenHasPaneTitle() {
        composeTestRule.onNode(hasPaneTitle()).assertExists()
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(adaptiveLayoutsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(adaptiveLayoutsExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(adaptiveLayoutsExample2HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyEveryHeadingsHasATestTag() {
        composeTestRule.onNode(hasNoTestTag() and isHeading()).assertDoesNotExist()
    }

}