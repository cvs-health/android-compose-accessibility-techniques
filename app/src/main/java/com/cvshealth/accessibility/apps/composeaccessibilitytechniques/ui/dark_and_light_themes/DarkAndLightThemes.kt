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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dark_and_light_themes

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val darkAndLightThemesHeadingTestTag = "darkAndLightThemesHeading"

/**
 * DarkAndLightThemesScreen -- describes Dark and Light Theme support techniques and how to observe
 * them. Actual theme color techniques are found in Color.kt, Theme.kt, and MainActivity.kt. (Also
 * see the special coding required for custom Indication classes in CustomFocusIndicators.kt and
 * KeyboardFocusIndicators.kt.)
 *
 * Note also the techniques of setting uiMode to control theme in Previews that are shown below.
 */
@Composable
fun DarkAndLightThemesScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.dark_theme_title),
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
                text = stringResource(id = R.string.dark_theme_heading),
                modifier = Modifier.testTag(darkAndLightThemesHeadingTestTag)
            )
            BodyText(textId = R.string.dark_theme_description_1)
            BodyText(textId = R.string.dark_theme_description_2)
            BodyText(textId = R.string.dark_theme_description_3)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DarkThemeScreenDefaultThemePreview() {
    ComposeAccessibilityTechniquesTheme() {
        DarkAndLightThemesScreen(
            onBackPressed = {}
        )
    }
}

// Key technique: Set @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES) to preview a composable in
// Dark Theme without regard to system settings.
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun DarkThemeScreenDarkThemePreview() {
    ComposeAccessibilityTechniquesTheme() {
        DarkAndLightThemesScreen(
            onBackPressed = {}
        )
    }
}

// Key technique: Set @Preview(uiMode = Configuration.UI_MODE_NIGHT_NO) to preview a composable in
// Light Theme without regard to system settings.
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun DarkThemeScreenLightThemePreview() {
    ComposeAccessibilityTechniquesTheme() {
        DarkAndLightThemesScreen(
            onBackPressed = {}
        )
    }
}
