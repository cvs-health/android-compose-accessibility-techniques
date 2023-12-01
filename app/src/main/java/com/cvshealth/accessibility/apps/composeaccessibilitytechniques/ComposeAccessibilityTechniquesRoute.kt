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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques

/**
 * Define the allowed route strings for navigation within the app.
 *
 * @param route a unique route string
 */
enum class ComposeAccessibilityTechniquesRoute(val route: String) {
    Home("home"),
    TextAlternatives("textAlternatives"),
    AccessibilityTraversalOrder("accessibilityTraversalOrder"),
    ContentGrouping("contentGrouping"),
    ContentGroupReplacement("contentGroupReplacement"),
    HeadingSemantics("headingSemantics"),
    ListSemantics("listSemantics"),
    InteractiveControlLabels("interactiveControlLabels"),
    UxChangeAnnouncements("uxChangeAnnouncements"),
    KeyboardTypes("keyboardTypes"),
    KeyboardActions("keyboardActions"),
    CustomStateDescriptions("customStateDescriptions"),
    CustomAccessibilityActions("customAccessibilityActions"),
    AccordionControls("accordionControls"),
    ListItemLayouts("listItemLayouts"),
    CheckboxControls("checkboxControls"),
    DropdownMenus("dropdownMenus"),
    RadioButtonGroups("radioButtonGroups"),
    SwitchControls("switchControls"),
    TabRows("tabRows"),
    TextFieldControls("textFieldControls"),
}