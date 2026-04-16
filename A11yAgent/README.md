# A11y Checker for Android Compose (a11y-check-android)

Static analysis for Jetpack Compose accessibility issues, mapped to [WCAG 2.2](https://www.w3.org/TR/WCAG22/) success criteria. Scans `.kt` source files and reports missing labels, incorrect semantics, touch target sizes, color contrast, dynamic type, and more — with 32 rules across 17 WCAG criteria. Includes a **WCAG 2.2 scoring system** that grades your files or entire project from 0-100.

## Quick start

### Run via Gradle (recommended)

The tool is integrated into the project's Gradle build. Every debug build runs the check automatically:

```bash
# Run accessibility check only
./gradlew a11yCheck

# Or just build — a11y-check runs automatically after compileDebugKotlin
./gradlew assembleDebug
```

Results appear as clickable file:line links in the Build output. An HTML report is generated at `app/build/reports/a11y-check.html`.

### Run the JAR directly

```bash
# Build the fat JAR
./gradlew :A11yAgent:shadowJar

# Run against any Compose source directory
java -jar A11yAgent/build/libs/a11y-check-android-0.1.0.jar app/src/main/java

# Run against a specific file
java -jar A11yAgent/build/libs/a11y-check-android-0.1.0.jar app/src/main/java/com/example/MyScreen.kt
```

### Quick tips

```bash
java -jar a11y-check-android.jar . --only error          # Show only errors
java -jar a11y-check-android.jar . --format html          # HTML report to stdout
java -jar a11y-check-android.jar . --diff                 # Only issues on lines you changed
java -jar a11y-check-android.jar . --format sarif         # SARIF for GitHub code scanning
java -jar a11y-check-android.jar . --badge                # SVG score badge
java -jar a11y-check-android.jar . --fix                  # Auto-fix issues where possible
java -jar a11y-check-android.jar . --fix --dry-run        # Preview fixes without applying
java -jar a11y-check-android.jar . --per-composable       # Score each @Composable separately
java -jar a11y-check-android.jar . --lines 50-120         # Check only specific lines
java -jar a11y-check-android.jar . --watch                # Re-run on file changes
java -jar a11y-check-android.jar . --compact              # Suppress file path headers
java -jar a11y-check-android.jar . --per-file             # Per-file accessibility scores
java -jar a11y-check-android.jar --baseline-save          # Save current issues as baseline
java -jar a11y-check-android.jar . --baseline             # Only report new issues
java -jar a11y-check-android.jar . --diff-report old.json # Only new issues vs previous run
java -jar a11y-check-android.jar --list-rules             # List all 32 rules
java -jar a11y-check-android.jar --generate-docs          # Generate rule documentation
```

Every run includes a **WCAG 2.2 accessibility score** (0-100 with letter grade). Use `--min-score 80` to fail CI if the score drops below a threshold.

## Building from source

Requires JDK 21+.

```bash
cd android-compose-accessibility-techniques
./gradlew :A11yAgent:shadowJar
```

The fat JAR is at `A11yAgent/build/libs/a11y-check-android-0.1.0.jar`.

## Usage

```bash
JAR=A11yAgent/build/libs/a11y-check-android-0.1.0.jar

# Check a directory (all .kt files recursively)
java -jar $JAR app/src/main/java

# Check a specific file
java -jar $JAR app/src/main/java/com/example/MyView.kt

# List all rules
java -jar $JAR --list-rules

# Only show errors (skip warnings/info)
java -jar $JAR app/src/main/java --only error

# Disable specific rules
java -jar $JAR . --disable icon-missing-label,fixed-font-size

# Use a config file
java -jar $JAR . --config .a11ycheck.yml
```

### Output formats

```bash
# Default: colored terminal output
java -jar $JAR .

# Gradle: clickable file:line output for Android Studio Build tab
java -jar $JAR . --format gradle

# JSON (for CI pipelines and tooling)
java -jar $JAR . --format json

# HTML report (WCAG summary, code snippets, fix suggestions)
java -jar $JAR . --format html > report.html

# SARIF (for GitHub code scanning)
java -jar $JAR . --format sarif > results.sarif
```

### Diff-only mode

Only report issues on lines you changed — enforce "no new accessibility issues" without legacy violations:

```bash
# Issues on uncommitted changes only
java -jar $JAR . --diff

# Issues on changes vs. a branch
java -jar $JAR . --diff --diff-base main
```

## Gradle integration

The `app/build.gradle` is configured to run a11y-check on every debug build:

```groovy
// In app/build.gradle
dependencies {
    lintChecks project(':lint-checks')  // Android Lint rules (see lint-checks/)
}

// a11y-check runs after compileDebugKotlin
tasks.register('a11yCheck', JavaExec) {
    classpath = files(project(':A11yAgent').tasks.named('shadowJar').map { it.archiveFile })
    args = ["${projectDir}/src/main/java", '--format', 'gradle']
    ignoreExitValue = true
}
```

The `gradle` format produces output like:

```
w: file:///path/to/MyScreen.kt:42:1 IconButton has no accessible label [icon-button-missing-label] (WCAG 4.1.2)
```

These appear as clickable links in Android Studio's Build tab.

## Accessibility scoring

Every run calculates a **WCAG 2.2 accessibility score** (0-100) with a letter grade:

```
5 errors, 3 warnings in 4 files

WCAG Score: 62.5 / 100  (D)
  [============........]   62.5%  8 criteria passed, 2 failed -- 5 errors, 3 warnings

Failed WCAG criteria:
  X 1.1.1 Non-text Content  (3 errors)
  X 1.3.1 Info and Relationships  (2 errors, 1 warning)

Needs review:
  ! 2.4.6 Headings and Labels  (1 warning)
```

Use `--min-score` to enforce a minimum:

```bash
java -jar $JAR . --min-score 80
```

### How scoring works

The overall score combines two components equally:

1. **Criteria coverage (50%)** — what percentage of checked WCAG criteria pass. Errors cause a criterion to fail; warnings put it in "review" status.
2. **Issue density (50%)** — a deduction based on issue counts, **weighted by impact**:
   - Critical: **2.0x** (a critical error deducts 10 points)
   - Serious: **1.5x** (a serious error deducts 7.5 points)
   - Moderate: **1.0x** (unchanged)
   - Minor: **0.5x** (a minor warning deducts only 1 point)

### Impact levels

| Impact | Meaning | Example rules |
|--------|---------|---------------|
| **Critical** | Content completely inaccessible | `icon-missing-label`, `textfield-missing-label`, `hidden-with-interactive-children` |
| **Serious** | Major barrier, workaround may exist | `color-contrast`, `small-touch-target`, `heading-semantics-missing` |
| **Moderate** | Inconvenient but usable | `input-purpose`, `label-contains-role-button` |
| **Minor** | Annoyance, best-practice | `label-contains-role-image`, `hardcoded-color` |

## Trend tracking

Score tracking is **automatic** — every run records the score, grade, error count, and git commit hash to `.a11y-scores.json`. Once you have history, the terminal output shows a sparkline and delta:

```
Score Trend:
  Change from last run: +4.2
  History: ___________

  Date                          Score  Grade  Errors  Delta
  2025-04-01T10:00:00Z          72.5   C      12      --
  2025-04-03T14:30:00Z          76.8   C      8       +4.3
  > now                          81.0   B-     5       +4.2
```

HTML reports include an SVG trend chart. Use `--no-trend` to disable tracking.

## Baseline mode

Suppress known issues and only report new regressions:

```bash
# Save current issues as baseline
java -jar $JAR . --baseline-save

# Later: only report new issues not in the baseline
java -jar $JAR . --baseline
```

The baseline is saved to `.a11y-baseline.json` in the target directory. Commit this file to track known issues.

## Auto-fix

Use `--fix` to automatically apply available fixes to your source files:

```bash
java -jar $JAR . --fix
```

Preview what would change without modifying files:

```bash
java -jar $JAR . --fix --dry-run
```

After applying fixes, a11y-check re-analyzes and shows the updated score.

## Per-composable scoring

Use `--per-composable` to score each `@Composable` function independently:

```bash
java -jar $JAR . --per-composable
```

This detects all `@Composable` functions and shows a score for each, sorted worst-first:

```
Per-Composable Scores:
  [███████░░░░░░░░░░░░░]  37.5 (F)  BadFormScreen  FormScreen.kt:45-120  (11 errors)
  [████████████████████]  100.0 (A+) GoodFormScreen  FormScreen.kt:5-43
  [████████████████████]  100.0 (A+) HomeScreen  HomeScreen.kt:1-80
```

## Per-file scoring

Show accessibility scores for each file, sorted worst-first:

```bash
java -jar $JAR . --per-file
```

Output:

```
Per-File Scores:
  [████████░░░░░░░░░░░░]  42.5 (F)  TextAlternatives.kt  (12 errors, 3 warnings)
  [██████████████░░░░░░]  72.5 (C-)  InteractiveControlLabels.kt  (3 errors, 5 warnings)
  [████████████████████]  100.0 (A+) HomeScreen.kt
```

## Watch mode

Re-run analysis automatically when files change:

```bash
java -jar $JAR . --watch
```

Press `Ctrl+C` to stop. Each time a `.kt` file is modified, a11y-check re-runs and prints updated results.

## Lines range filter

Only check specific line ranges:

```bash
java -jar $JAR MyScreen.kt --lines 50-120
java -jar $JAR . --lines 10-30,80-100
```

## Report diff

Compare current results against a previous JSON report to see only **new** issues:

```bash
# Save a report
java -jar $JAR . --format json > baseline-report.json

# Later, compare against it
java -jar $JAR . --diff-report baseline-report.json
```

## HTML report

Generate a self-contained HTML report:

```bash
java -jar $JAR app/src/main/java --format html > accessibility-report.html
```

Or via Gradle:

```bash
./gradlew a11yCheck
# Report at: app/build/reports/a11y-check.html
```

The report includes:

- **Summary banner** with error, warning, and info counts
- **WCAG 2.2 score** with grade badge and conformance breakdown
- **Score trend chart** with SVG visualization and history table
- **WCAG 2.2 conformance table** showing pass/fail for each criterion, linked to the WCAG spec
- **Issues by file** with expandable sections, code snippets, and fix suggestions
- **"Current code" / "Fixed code"** side-by-side showing the problem and how to fix it
- **Issues by rule** summary table

## SARIF output (GitHub Code Scanning)

Generate [SARIF](https://docs.oasis-open.org/sarif/sarif/v2.1.0/sarif-v2.1.0.html) output for GitHub code scanning:

```bash
java -jar $JAR . --format sarif > results.sarif
```

Upload in your GitHub Actions workflow:

```yaml
- name: Run a11y-check
  run: java -jar A11yAgent/build/libs/a11y-check-android-0.1.0.jar app/src/main/java --format sarif > results.sarif
- name: Upload SARIF
  uses: github/codeql-action/upload-sarif@v3
  with:
    sarif_file: results.sarif
```

## Score badge

Generate an SVG badge showing your project's accessibility score:

```bash
java -jar $JAR . --badge > a11y-badge.svg
```

Add to your README:

```markdown
![a11y score](a11y-badge.svg)
```

## CI integration

### GitHub Actions

A ready-to-use workflow is included at [`.github/workflows/a11y-check.yml`](../.github/workflows/a11y-check.yml). It:

1. Builds the shadow JAR
2. Runs the accessibility check
3. Posts the score as a PR comment
4. Uploads SARIF to GitHub Code Scanning
5. Fails the job if the score is below the configurable threshold

### Diff-only in CI

Only fail if the PR introduces new accessibility errors:

```yaml
- name: Accessibility check (changed files only)
  run: java -jar $JAR app/src/main/java --diff --diff-base origin/main --only error
```

### Baseline mode in CI

```bash
# Once: save the baseline (commit this file)
java -jar $JAR app/src/main/java --baseline-save

# In CI: only new issues fail the build
java -jar $JAR app/src/main/java --baseline
```

## MCP (Cursor / AI assistants)

An [MCP server](mcp-server/README.md) is included so AI assistants like Cursor can run a11y-check through natural language.

### Quick setup

1. Build the shadow JAR: `./gradlew :A11yAgent:shadowJar`
2. Install the MCP server:
   ```bash
   cd A11yAgent/mcp-server && npm install && npm run build
   ```
3. Add to Cursor Settings > Features > MCP > Edit config:
   ```json
   {
     "mcpServers": {
       "a11y-check-android": {
         "command": "node",
         "args": ["/path/to/A11yAgent/mcp-server/dist/index.js"]
       }
     }
   }
   ```
4. Restart Cursor.

### What you can ask

- "Check this project for accessibility issues"
- "Run a11y-check on TextFieldControls.kt"
- "Which issues are the most critical to fix?"
- "Fix the textfield-missing-label issues"
- "What's the accessibility score?"
- "Generate an HTML accessibility report"

## Configuration file

Create a `.a11ycheck.yml` in your project root:

```yaml
# .a11ycheck.yml

# Override rule severities
severity_overrides:
  icon-missing-label: warning
  hardcoded-color: info

# Disable rules entirely
disabled_rules:
  - max-lines-one
  - fixed-font-size

# Allowlist mode: if non-empty, only these rules run
enabled_only: []

# Rule-specific options
options:
  min_touch_target: 48    # override small-touch-target threshold (default 48dp)
  contrast_ratio: 4.5     # WCAG AA contrast minimum

# Skip paths matching these patterns
exclude_paths:
  - "*/generated/*"
  - "*/build/*"
  - "*Test*"
```

CLI flags (`--disable`, `--only`) are applied on top of the config file.

## Inline suppression

Suppress specific diagnostics in your source code:

```kotlin
// Suppress a specific rule on this line
Icon(imageVector = icon, contentDescription = null) // a11y-check:disable icon-missing-label

// Suppress multiple rules
Icon(imageVector = icon, contentDescription = null) // a11y-check:disable icon-missing-label, empty-content-description

// Suppress all rules on this line
Icon(imageVector = icon, contentDescription = null) // a11y-check:disable
```

## Options

| Option | Description |
|--------|-------------|
| `paths` | File or directory paths to analyze (default: `.`) |
| `--format` | Output format: `terminal` (default), `json`, `gradle`, `html`, `sarif` |
| `--only` | Minimum severity: `info`, `warning`, or `error` |
| `--disable` | Comma-separated rule IDs to disable |
| `--config` | Path to `.a11ycheck.yml` config file (auto-detected if not specified) |
| `--diff` | Only report diagnostics on lines changed in the git diff |
| `--diff-base` | Git ref to diff against (default: `HEAD`). Use with `--diff` |
| `--list-rules` | Print all rules and exit |
| `--min-score` | Minimum passing score (0-100). Exits with code 1 if below threshold |
| `--trend` / `--no-trend` | Score trend tracking (enabled by default) |
| `--baseline-save` | Save current issues as baseline (`.a11y-baseline.json`) |
| `--baseline` | Filter out baseline issues — only new regressions shown |
| `--badge` | Generate an SVG score badge to stdout |
| `--generate-docs` | Generate Markdown rule documentation to stdout |
| `--fix` | Automatically apply available fixes to source files |
| `--dry-run` | Show what `--fix` would change without modifying files |
| `--per-composable` | Show per-`@Composable` function scores |
| `--per-file` | Show per-file accessibility scores |
| `--watch` | Watch for file changes and re-run analysis automatically |
| `--lines` | Only check lines in a range, e.g. `50-120` or `10-30,80-100` |
| `--diff-report` | Compare against a previous JSON report — only new issues shown |
| `--compact` | Suppress file path headers in output |

## Rules

a11y-check-android includes 32 rules across these categories:

| Category | Rules | WCAG | Impact |
|----------|-------|------|--------|
| **Icons & images** | `icon-missing-label`, `label-contains-role-image`, `empty-content-description` | 1.1.1 | Critical, Minor, Serious |
| **Headings** | `heading-semantics-missing`, `fake-heading-in-label` | 2.4.6, 1.3.1 | Serious, Serious |
| **Color & contrast** | `hardcoded-color`, `color-contrast` | 1.4.3 | Minor, Serious |
| **Dynamic type** | `fixed-font-size`, `max-lines-one` | 1.4.4 | Serious, Serious |
| **Focus** | `dialog-focus-management` | 2.4.3 | Serious |
| **Pane titles** | `missing-pane-title`, `tab-missing-label` | 2.4.2 | Serious, Serious |
| **Links** | `generic-link-text`, `button-used-as-link` | 2.4.4 | Serious, Serious |
| **Touch targets** | `small-touch-target` | 2.5.8 | Serious |
| **Label in Name** | `label-in-name` | 2.5.3 | Serious |
| **Buttons** | `label-contains-role-button`, `icon-button-missing-label`, `visually-disabled-not-semantically` | 4.1.2 | Moderate, Critical, Serious |
| **Clickable** | `clickable-missing-role` | 4.1.2 | Critical |
| **Toggles** | `toggle-missing-label` | 4.1.2 | Critical |
| **Form controls** | `textfield-missing-label`, `slider-missing-label`, `dropdown-missing-label` | 4.1.2 | Critical |
| **Grouping** | `accessibility-grouping`, `hidden-with-interactive-children`, `box-child-order`, `radio-group-missing` | 1.3.1, 4.1.2, 1.3.2 | Minor, Critical, Minor, Moderate |
| **Animation** | `reduce-motion` | 2.3.1 | Serious |
| **Gestures** | `gesture-missing-alternative` | 2.1.1 | Serious |
| **Input** | `input-purpose` | 1.3.5 | Moderate |
| **Timing** | `timing-adjustable` | 2.2.1 | Moderate |

Run `java -jar $JAR --list-rules` for full descriptions and severities.

## IDE plugin (IntelliJ / Android Studio)

An IntelliJ Platform plugin is included in `ide-plugin/` that provides real-time inline accessibility warnings in the editor. It uses an `ExternalAnnotator` to run the a11y-check analysis engine on each Kotlin file and display squiggly underlines for accessibility issues.

### Building the plugin

The IDE plugin is a standalone Gradle project (separate from the main Android build) due to IntelliJ Platform SDK requirements:

```bash
# First, build the A11yAgent JAR
./gradlew :A11yAgent:shadowJar

# Then build the plugin
cd ide-plugin
./gradlew buildPlugin
```

The plugin ZIP is produced in `ide-plugin/build/distributions/`. Install via **Settings > Plugins > Install Plugin from Disk**.

### What it does

- Runs all 32 rules on the current Kotlin file as you edit
- Shows error/warning/info squiggles inline in the editor
- Tooltip shows the rule ID, WCAG criteria, and fix suggestion
- No Gradle or JAR needed — the analysis engine runs directly as a library

## Also included: Android Lint rules

The project also includes a custom Android Lint module (`lint-checks/`) that integrates 5 accessibility checks into Android's built-in Lint system. These appear in `./gradlew lint` HTML reports alongside standard Android Lint results. See [`lint-checks/README.md`](../lint-checks/README.md).

## Testing

```bash
./gradlew :A11yAgent:test
```

Tests cover the rule engine, scanner, score calculator, config loader, baseline, inline suppression, and individual rules.

## Contributor Guide

1. Before contributing to this CVS Health sponsored project, you will need to sign the associated [Contributor License Agreement](https://forms.office.com/r/9e9VmE7qLW).
2. See the [contributing](../CONTRIBUTING.md) page.

## License
a11y-check-android is licensed under the Apache License, Version 2.0. See [LICENSE](../LICENSE) file for more information.

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
