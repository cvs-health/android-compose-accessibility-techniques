package com.example.composeaccessibilitytechniques

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.composeaccessibilitytechniques.ui.heading_semantics.HeadingSemanticsScreen
import com.example.composeaccessibilitytechniques.ui.heading_semantics.headingSemanticsContentDescriptionFauxHeading
import com.example.composeaccessibilitytechniques.ui.heading_semantics.headingSemanticsExample3HeadingTestTag
import com.example.composeaccessibilitytechniques.ui.heading_semantics.headingSemanticsHeadingTestTag
import com.example.composeaccessibilitytechniques.ui.heading_semantics.headingSemanticsLargeTextFauxHeading
import com.example.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Rule
import org.junit.Test

class HeadingSemanticsTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun myTest() {
        // Start the app
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme(dynamicColor = false) {
                HeadingSemanticsScreen { }
            }
        }

        composeTestRule
            .onNode(hasTestTag(headingSemanticsHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(headingSemanticsLargeTextFauxHeading) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(headingSemanticsContentDescriptionFauxHeading) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(headingSemanticsExample3HeadingTestTag) and isHeading())
            .assertExists()
    }
}