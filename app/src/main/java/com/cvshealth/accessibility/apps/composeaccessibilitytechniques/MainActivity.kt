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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accessibility_traversal_order.AccessibilityTraversalOrderScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_actions.KeyboardActionsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accordion_controls.AccordionControlsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.adaptive_layouts.AdaptiveLayoutsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.autofill_controls.AutofillControlsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.checkbox_controls.CheckboxControlsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_group_replacement.ContentGroupReplacementScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.ContentGroupingScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_accessibility_actions.CustomAccessibilityActionsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_click_labels.CustomClickLabelsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_focus_indicators.CustomFocusIndicatorsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.custom_state_descriptions.CustomStateDescriptionsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dark_and_light_themes.DarkAndLightThemesScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.dropdown_menus.DropdownMenusScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.exposed_dropdown_menus.ExposedDropdownMenusScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.heading_semantics.HeadingSemanticsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.home.HomeScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.inline_links.InlineLinksScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.InteractiveControlLabelsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_focus_order.KeyboardFocusOrderScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.keyboard_types.KeyboardTypesScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.ListSemanticsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.listitem_layouts.ListItemLayoutsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.modalbottomsheet_layouts.ModalBottomSheetLayoutsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.navigationbar_layouts.NavigationBarLayoutsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.popup_messages.PopupMessagesScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.radio_button_groups.RadioButtonGroupsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.screen_and_pane_titles.ScreenAndPaneTitlesScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.slider_controls.SliderControlsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.standalone_links.StandAloneLinksScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.switch_controls.SwitchControlsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.tab_rows.TabRowsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.TextAlternativesScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.textfield_controls.TextFieldControlsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.ux_change_announcements.UxChangeAnnouncementsScreen

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Key adaptive layout technique: Calculate the appropriate WindowSizeClass and pass it
            // down to appropriate child composable functions. (Note the required OptIn annotation.)
            val windowSizeClass = calculateWindowSizeClass(this)

            // Key theming technique: Wrap all app content in the app's theme.
            ComposeAccessibilityTechniquesTheme {
                val navController = rememberNavController()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ComposeAccessibilityTechniquesNavHost(navController, windowSizeClass)
                }
            }
        }
    }
}

@Composable
fun ComposeAccessibilityTechniquesNavHost(
    navController: NavHostController,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        val popBackStack: () -> Unit = { navController.popBackStack() }
        composable(route = ComposeAccessibilityTechniquesRoute.Home.route) {
            HomeScreen { route: ComposeAccessibilityTechniquesRoute ->
                navController.navigate(route.route)
            }
        }
        composable(route = ComposeAccessibilityTechniquesRoute.TextAlternatives.route) {
            TextAlternativesScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.AccessibilityTraversalOrder.route) {
            AccessibilityTraversalOrderScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.ContentGrouping.route) {
            ContentGroupingScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.ContentGroupReplacement.route) {
            ContentGroupReplacementScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.HeadingSemantics.route) {
            HeadingSemanticsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.ListSemantics.route) {
            ListSemanticsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.AdaptiveLayouts.route) {
            AdaptiveLayoutsScreen(onBackPressed = popBackStack, windowSizeClass = windowSizeClass)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.DarkAndLightThemes.route) {
            DarkAndLightThemesScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.ScreenAndPaneTitles.route) {
            ScreenAndPaneTitlesScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.InteractiveControlLabels.route) {
            InteractiveControlLabelsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.UxChangeAnnouncements.route) {
            UxChangeAnnouncementsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.KeyboardTypes.route) {
            KeyboardTypesScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.KeyboardActions.route) {
            KeyboardActionsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.KeyboardFocusOrder.route) {
            KeyboardFocusOrderScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.CustomFocusIndicators.route) {
            CustomFocusIndicatorsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.CustomClickLabels.route) {
            CustomClickLabelsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.CustomStateDescriptions.route) {
            CustomStateDescriptionsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.CustomAccessibilityActions.route) {
            CustomAccessibilityActionsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.AccordionControls.route) {
            AccordionControlsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.AutofillControls.route) {
            AutofillControlsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.CheckboxControls.route) {
            CheckboxControlsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.DropdownMenus.route) {
            DropdownMenusScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.ExposedDropdownMenus.route) {
            ExposedDropdownMenusScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.InlineLinks.route) {
            InlineLinksScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.ListItemLayouts.route) {
            ListItemLayoutsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.ModalBottomSheetLayouts.route) {
            ModalBottomSheetLayoutsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.NavigationBarLayouts.route) {
            NavigationBarLayoutsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.PopupMessages.route) {
            PopupMessagesScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.RadioButtonGroups.route) {
            RadioButtonGroupsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.SliderControls.route) {
            SliderControlsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.StandAloneLinks.route) {
            StandAloneLinksScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.SwitchControls.route) {
            SwitchControlsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.TabRows.route) {
            TabRowsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.TextFieldControls.route) {
            TextFieldControlsScreen(onBackPressed = popBackStack)
        }
    }
}


