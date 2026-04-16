# Custom Android Lint Accessibility Rules (lint-checks)

Custom Android Lint rules for Jetpack Compose accessibility issues. These integrate into Android's built-in Lint system and appear in `./gradlew lint` HTML reports alongside standard Lint results. **All 32 rules show real-time squiggly underlines in the Android Studio editor.**

## Included checks (32 rules)

### UAST Method-Call Rules (ComposeA11yDetector — 13 rules)

| Issue ID | Severity | What it detects | WCAG |
|----------|----------|-----------------|------|
| `A11yIconMissingLabel` | Warning | `Icon()` or `Image()` with `contentDescription = null` and no parent semantics | 1.1.1 |
| `A11yEmptyContentDescription` | Warning | `contentDescription = ""` (empty string instead of null for decorative) | 1.1.1 |
| `A11yTextFieldMissingLabel` | Warning | `TextField` or `OutlinedTextField` without a `label` parameter | 4.1.2 |
| `A11yIconButtonMissingLabel` | Warning | `IconButton` without accessible label on inner Icon or modifier | 4.1.2 |
| `A11ySliderMissingLabel` | Warning | `Slider` or `RangeSlider` without contentDescription or labeled container | 4.1.2 |
| `A11yDropdownMissingLabel` | Warning | `ExposedDropdownMenuBox` without labeled TextField inside | 4.1.2 |
| `A11yTabMissingLabel` | Warning | `Tab` or `LeadingIconTab` without text label | 2.4.2 |
| `A11yMissingPaneTitle` | Warning | `Scaffold` without pane title semantics or TopAppBar title | 2.4.2 |
| `A11yToggleMissingLabel` | Warning | `Checkbox`, `Switch`, `TriStateCheckbox` without associated label | 4.1.2 |
| `A11yLabelContainsRoleImage` | Warning | contentDescription contains role words like "image", "icon" | 1.1.1 |
| `A11yLabelContainsRoleButton` | Warning | Button label contains the word "button" | 4.1.2 |
| `A11yLabelInNameError` | Error | Visible text not contained in accessible name (contentDescription) | 2.5.3 |
| `A11yLabelInNameWarning` | Warning | Visible text not at the start of accessible name | 2.5.3 |

### Source Text Scan Rules (ComposeTextScanDetector — 12 rules)

| Issue ID | Severity | What it detects | WCAG |
|----------|----------|-----------------|------|
| `A11yFixedFontSize` | Warning | Hardcoded `.sp` font sizes instead of `MaterialTheme.typography` | 1.4.4 |
| `A11yMaxLinesOne` | Info | `maxLines = 1` on Text (truncation risk for larger text) | 1.4.4 |
| `A11yHeadingSemanticsMissing` | Warning | Text with heading typography but no `semantics { heading() }` | 2.4.6, 1.3.1 |
| `A11yFakeHeadingInLabel` | Warning | contentDescription containing the word "heading" | 1.3.1 |
| `A11yClickableMissingRole` | Warning | `.clickable()` without a `role` parameter | 4.1.2 |
| `A11yHardcodedColor` | Warning | Hardcoded `Color.X` or `Color(0x...)` values outside theme files | 1.4.3 |
| `A11yVisuallyDisabledNotSemantically` | Warning | `.alpha()` < 0.5 without `enabled = false` | 4.1.2 |
| `A11yGenericLinkText` | Warning | Generic link text ("click here", "learn more") in clickable context | 2.4.4 |
| `A11yReduceMotion` | Info | Animation APIs without reduced motion check | 2.3.1 |
| `A11yGestureMissingAlternative` | Warning | Custom gestures without keyboard/button alternative | 2.1.1 |
| `A11yTimingAdjustable` | Info | Hardcoded `delay()`/`withTimeout()` durations | 2.2.1 |
| `A11yDialogFocusManagement` | Warning | Dialog/BottomSheet without FocusRequester | 2.4.3 |

### Form Control Rules (ComposeFormDetector — 2 rules)

| Issue ID | Severity | What it detects | WCAG |
|----------|----------|-----------------|------|
| `A11yInputPurpose` | Info | TextField with keyboard type but no autofill/content type | 1.3.5 |
| `A11yRadioGroupMissing` | Warning | RadioButton not inside selectableGroup container | 1.3.1 |

### Structural Rules (ComposeStructureDetector — 4 rules)

| Issue ID | Severity | What it detects | WCAG |
|----------|----------|-----------------|------|
| `A11yHiddenWithInteractiveChildren` | Warning | `clearAndSetSemantics` on container with interactive children | 4.1.2 |
| `A11yAccessibilityGrouping` | Info | Clickable Row/Column with Icon+Text but no mergeDescendants | 1.3.1 |
| `A11yBoxChildOrder` | Info | Box with overlapping children without traversalIndex | 1.3.2 |
| `A11yButtonUsedAsLink` | Warning | Button that navigates to a URL (should be a link) | 2.4.4 |

### Color Contrast Rules (ComposeColorContrastDetector — 1 rule)

| Issue ID | Severity | What it detects | WCAG |
|----------|----------|-----------------|------|
| `A11yColorContrast` | Warning | Hardcoded color pairs below WCAG 4.5:1 contrast ratio | 1.4.3 |

## How it works

The module provides five detectors:

- **`ComposeA11yDetector`** — UAST-based detector using `getApplicableMethodNames()` to intercept Composable calls and inspect their arguments.
- **`ComposeTextScanDetector`** — Source text scanner using regex patterns to find accessibility issues across file content.
- **`ComposeFormDetector`** — UAST-based detector for form control and input-specific rules.
- **`ComposeStructureDetector`** — Hybrid UAST + source text detector for structural/grouping rules.
- **`ComposeColorContrastDetector`** — Source text scanner that computes WCAG contrast ratios for hardcoded color pairs.

## Setup

The `app/build.gradle` already includes:

```groovy
dependencies {
    lintChecks project(':lint-checks')
}
```

And `settings.gradle` includes:

```groovy
include ':lint-checks'
```

## Running

```bash
# Run all lint checks (includes these custom rules)
./gradlew lint

# Results at:
# app/build/reports/lint-results-debug.html
```

Custom accessibility issues appear under the **"Accessibility"** category in the Lint HTML report, alongside Android's built-in accessibility checks.

## Viewing in Android Studio

Custom lint rules appear as **real-time squiggly underlines** in the editor. Configure severity in **Settings > Inspections > Android Lint: Accessibility**.

## Relationship to A11yAgent

This module provides the same 32 rules as A11yAgent, integrated into Android's native Lint system for inline editor feedback. For WCAG scoring, trend tracking, baseline suppression, and additional output formats, see [`A11yAgent/`](../A11yAgent/README.md).

## Contributor Guide

1. Before contributing to this CVS Health sponsored project, you will need to sign the associated [Contributor License Agreement](https://forms.office.com/r/9e9VmE7qLW).
2. See the [contributing](../CONTRIBUTING.md) page.

## License
lint-checks is licensed under the Apache License, Version 2.0. See [LICENSE](../LICENSE) file for more information.

Copyright 2026 CVS Health and/or one of its affiliates

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.
