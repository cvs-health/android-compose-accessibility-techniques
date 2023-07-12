package com.example.composeaccessibilitytechniques

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.composeaccessibilitytechniques.ui.heading_semantics.HeadingSemanticsScreen
import com.example.composeaccessibilitytechniques.ui.heading_semantics.headingSemanticsContentDescriptionFauxHeading
import com.example.composeaccessibilitytechniques.ui.heading_semantics.headingSemanticsExample3HeadingTestTag
import com.example.composeaccessibilitytechniques.ui.heading_semantics.headingSemanticsHeadingTestTag
import com.example.composeaccessibilitytechniques.ui.heading_semantics.headingSemanticsLargeTextFauxHeading
import com.example.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HeadingSemanticsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme(dynamicColor = false) {
                HeadingSemanticsScreen { }
            }
        }
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(headingSemanticsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(headingSemanticsExample3HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyFauxHeadingsAreNotHeadings() {
        composeTestRule
            .onNode(hasTestTag(headingSemanticsLargeTextFauxHeading) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(headingSemanticsContentDescriptionFauxHeading) and !isHeading())
            .assertExists()
    }

    @Test
    fun verifyEveryHeadingsHasATestTag() {
        composeTestRule.onNode(hasNoTestTag() and isHeading()).assertDoesNotExist()
    }
}