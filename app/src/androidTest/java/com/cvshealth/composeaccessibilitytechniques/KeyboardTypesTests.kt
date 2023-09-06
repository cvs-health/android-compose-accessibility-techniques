package com.cvshealth.composeaccessibilitytechniques

import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isHeading
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.input.ImeAction
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.KeyboardTypesScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample10HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample10TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample11HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample11TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample12HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample12TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample13HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample13TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample14HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample14TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample1HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample1TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample2HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample2TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample3HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample3TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample4HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample4TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample5HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample5TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample6HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample6TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample7HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample7TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample8HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample8TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample9HeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesExample9TextFieldTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.keyboardTypesHeadingTestTag
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * KeyboardTypesTests verifies the semantic properties of the KeyboardTypesScreen composable.
 * As the keyboardOptions property of a TextField is not a semantic property, only the heading and
 * imeActions statuses of the screen's component composables are verified.
 */
class KeyboardTypesTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ComposeAccessibilityTechniquesTheme() {
                KeyboardTypesScreen { }
            }
        }
    }

    @Test
    fun verifyHeadingsAreHeadings() {
        composeTestRule
            .onNode(hasTestTag(keyboardTypesHeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample1HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample2HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample3HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample4HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample5HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample6HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample7HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample8HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample9HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample10HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample11HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample12HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample13HeadingTestTag) and isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample14HeadingTestTag) and isHeading())
            .assertExists()
    }

    @Test
    fun verifyExampleTextFieldsAreNotHeadings() {
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample1TextFieldTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample2TextFieldTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample3TextFieldTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample4TextFieldTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample5TextFieldTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample6TextFieldTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample7TextFieldTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample8TextFieldTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample9TextFieldTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample10TextFieldTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample11TextFieldTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample12TextFieldTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample13TextFieldTestTag) and !isHeading())
            .assertExists()
        composeTestRule
            .onNode(hasTestTag(keyboardTypesExample14TextFieldTestTag) and !isHeading())
            .assertExists()
    }

    @Test
    fun verifyEveryHeadingsHasATestTag() {
        composeTestRule.onNode(hasNoTestTag() and isHeading()).assertDoesNotExist()
    }

    @Test
    fun verifyExampleTextFieldsHaveDefaultImeAction() {
        composeTestRule
            .onNode(
                hasTestTag(keyboardTypesExample1TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Default)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardTypesExample2TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Default)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardTypesExample3TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Default)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardTypesExample4TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Default)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardTypesExample5TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Default)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardTypesExample6TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Default)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardTypesExample7TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Default)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardTypesExample8TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Default)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardTypesExample9TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Default)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardTypesExample10TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Default)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardTypesExample11TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Default)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardTypesExample12TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Default)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardTypesExample13TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Default)
            )
            .assertExists()
        composeTestRule
            .onNode(
                hasTestTag(keyboardTypesExample14TextFieldTestTag)
                        and
                        hasImeAction(ImeAction.Default)
            )
            .assertExists()
    }
}