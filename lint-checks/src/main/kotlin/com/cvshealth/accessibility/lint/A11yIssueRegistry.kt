/*
 * Copyright 2026 CVS Health and/or one of its affiliates
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cvshealth.accessibility.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class A11yIssueRegistry : IssueRegistry() {
    override val issues: List<Issue> = listOf(
        // ComposeA11yDetector — UAST method-call rules (11)
        ComposeA11yDetector.ICON_MISSING_LABEL,
        ComposeA11yDetector.EMPTY_CONTENT_DESCRIPTION,
        ComposeA11yDetector.TEXTFIELD_MISSING_LABEL,
        ComposeA11yDetector.ICON_BUTTON_MISSING_LABEL,
        ComposeA11yDetector.SLIDER_MISSING_LABEL,
        ComposeA11yDetector.DROPDOWN_MISSING_LABEL,
        ComposeA11yDetector.TAB_MISSING_LABEL,
        ComposeA11yDetector.MISSING_PANE_TITLE,
        ComposeA11yDetector.TOGGLE_MISSING_LABEL,
        ComposeA11yDetector.LABEL_CONTAINS_ROLE_IMAGE,
        ComposeA11yDetector.LABEL_CONTAINS_ROLE_BUTTON,
        ComposeA11yDetector.LABEL_IN_NAME_ERROR,
        ComposeA11yDetector.LABEL_IN_NAME_WARNING,

        // ComposeTextScanDetector — source text scan rules (12)
        ComposeTextScanDetector.FIXED_FONT_SIZE,
        ComposeTextScanDetector.MAX_LINES_ONE,
        ComposeTextScanDetector.HEADING_SEMANTICS_MISSING,
        ComposeTextScanDetector.FAKE_HEADING_IN_LABEL,
        ComposeTextScanDetector.CLICKABLE_MISSING_ROLE,
        ComposeTextScanDetector.HARDCODED_COLOR,
        ComposeTextScanDetector.VISUALLY_DISABLED_NOT_SEMANTICALLY,
        ComposeTextScanDetector.GENERIC_LINK_TEXT,
        ComposeTextScanDetector.REDUCE_MOTION,
        ComposeTextScanDetector.GESTURE_MISSING_ALTERNATIVE,
        ComposeTextScanDetector.TIMING_ADJUSTABLE,
        ComposeTextScanDetector.DIALOG_FOCUS_MANAGEMENT,

        // ComposeFormDetector — form control rules (2)
        ComposeFormDetector.INPUT_PURPOSE,
        ComposeFormDetector.RADIO_GROUP_MISSING,

        // ComposeStructureDetector — structural rules (4)
        ComposeStructureDetector.HIDDEN_WITH_INTERACTIVE_CHILDREN,
        ComposeStructureDetector.ACCESSIBILITY_GROUPING,
        ComposeStructureDetector.BOX_CHILD_ORDER,
        ComposeStructureDetector.BUTTON_USED_AS_LINK,

        // ComposeColorContrastDetector — contrast rules (1)
        ComposeColorContrastDetector.COLOR_CONTRAST,
    )

    override val api: Int = CURRENT_API

    override val vendor: Vendor = Vendor(
        vendorName = "CVS Health Accessibility",
        feedbackUrl = "https://github.com/cvs-health/android-compose-accessibility-techniques/issues"
    )
}
