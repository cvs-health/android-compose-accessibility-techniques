# Compose Accessibility Checker — IDE Plugin

An IntelliJ Platform plugin that shows **inline accessibility warnings** in the Android Studio editor as you type. It runs the [a11y-check-android](../A11yAgent/README.md) analysis engine with **32 rules mapped to WCAG 2.2** and displays results as squiggly underlines with hover tooltips.

## What it does

- Highlights Compose accessibility issues directly in the editor (warnings, errors, info)
- Hover over an underline to see the issue, WCAG criteria, and suggested fix
- Works on any `.kt` file — no build step required, analysis runs as you type
- Severity levels map to IntelliJ's standard error/warning/weak-warning highlighting

## Install from disk (manual)

If the plugin is not yet on the JetBrains Marketplace, install it manually:

### Prerequisites

1. **Build the A11yAgent shadow JAR** (from the repo root):
   ```bash
   ./gradlew :A11yAgent:shadowJar
   ```

2. **Set your Android Studio path** (if not the default). Create `ide-plugin/gradle.properties`:
   ```properties
   androidStudioPath=/path/to/Android Studio.app/Contents
   ```
   On Linux: `/path/to/android-studio/`
   On Windows: `C:\\Program Files\\Android\\Android Studio\\`

3. **Corporate proxy (Zscaler/etc.)**: If behind a corporate SSL proxy, import the CA certificate into your JDK trust store:
   ```bash
   keytool -importcert -file proxy-ca.pem \
     -keystore $JAVA_HOME/lib/security/cacerts \
     -alias corporate-proxy
   ```

### Build the plugin

```bash
cd ide-plugin
./gradlew buildPlugin
```

The plugin zip is created at:
```
ide-plugin/build/distributions/compose-accessibility-checker-0.3.0.zip
```

### Install in Android Studio

1. Open **Settings > Plugins**
2. Click the **gear icon** (top right) > **Install Plugin from Disk...**
3. Select `compose-accessibility-checker-0.3.0.zip`
4. Restart Android Studio

After restart, open any Compose `.kt` file — accessibility issues appear as squiggly underlines. Hover to see WCAG criteria and fix suggestions.

### Uninstall

**Settings > Plugins** > find "Compose Accessibility Checker" > **Uninstall** > Restart.

## Install from JetBrains Marketplace

Once published, install directly from Android Studio:

1. Open **Settings > Plugins > Marketplace**
2. Search for **"Compose Accessibility Checker"**
3. Click **Install** > Restart

## Publishing to JetBrains Marketplace

To publish a new version:

1. Create an account at [plugins.jetbrains.com](https://plugins.jetbrains.com)
2. Generate a **Permanent Upload Token** at [Account > Tokens](https://plugins.jetbrains.com/author/me/tokens)
3. First-time upload — use the web UI:
   - Go to [Upload Plugin](https://plugins.jetbrains.com/plugin/add)
   - Select the `.zip` from `build/distributions/`
   - JetBrains reviews and approves (1-3 business days)
4. Subsequent updates — use Gradle:
   ```bash
   cd ide-plugin
   ./gradlew publishPlugin -PintellijPublishToken=YOUR_TOKEN
   ```
   Or set `ORG_GRADLE_PROJECT_intellijPublishToken` as an environment variable.

### Version bumps

Update the version in `build.gradle.kts`:
```kotlin
version = "0.3.0"
```

And add change notes in `src/main/resources/META-INF/plugin.xml` under `<change-notes>`.

## Rules

The plugin runs all 32 rules from the a11y-check-android engine. See [`A11yAgent/README.md`](../A11yAgent/README.md) for the full rule list with WCAG mappings.

## Architecture

```
ide-plugin/
  src/main/kotlin/.../
    A11yExternalAnnotator.kt   — ExternalAnnotator that runs analysis and creates annotations
    A11yAnnotatorInfo.kt       — Data class holding file content and path
  src/main/resources/META-INF/
    plugin.xml                 — Plugin descriptor
  build.gradle.kts             — Standalone Gradle build (IntelliJ Platform Plugin v2.x)
  settings.gradle.kts          — Standalone project settings
```

The plugin uses IntelliJ's `ExternalAnnotator` extension point:
1. `collectInformation()` — captures file text and path for Kotlin files
2. `doAnnotate()` — runs the A11yAgent `RuleRegistry.analyze()` engine
3. `apply()` — converts diagnostics to editor annotations with severity and tooltips

## Relationship to lint-checks

| | IDE Plugin | lint-checks |
|---|---|---|
| **When it runs** | Real-time as you type | During `./gradlew lint` |
| **Where results appear** | Editor squiggly underlines + hover tooltips | HTML report + Build tab |
| **Rules** | 31 (via A11yAgent engine) | 31 (via Android Lint detectors) |
| **Install** | Plugin zip (manual or Marketplace) | `lintChecks project(':lint-checks')` in build.gradle |
| **Requires** | A11yAgent shadow JAR | Nothing extra |

Both provide the same 31 accessibility rules. Use the IDE plugin for real-time feedback while coding, and lint-checks for CI/CD and build reports.

## Contributor Guide

1. Before contributing to this CVS Health sponsored project, you will need to sign the associated [Contributor License Agreement](https://forms.office.com/r/9e9VmE7qLW).
2. See the [contributing](../CONTRIBUTING.md) page.

## License

Licensed under the Apache License, Version 2.0. See [LICENSE](../LICENSE) for details.

Copyright 2026 CVS Health and/or one of its affiliates
