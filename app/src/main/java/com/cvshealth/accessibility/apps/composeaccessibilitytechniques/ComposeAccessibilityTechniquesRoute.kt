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
 */
enum class ComposeAccessibilityTechniquesRoute(val route: String) {
    Home("home"),
    RadioGroupSample("radioGroupSample"),
    TextAlternatives("textAlternatives"),
    ContentGrouping("contentGrouping"),
    ContentGroupReplacement("contentGroupReplacement"),
    HeadingSemantics("headingSemantics"),
    ListSemantics("listSemantics"),
    InteractiveControlLabels("interactiveControlLabels"),
    AccordionControls("accordionControls"),
}