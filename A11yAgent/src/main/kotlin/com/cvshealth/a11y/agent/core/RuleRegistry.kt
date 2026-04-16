package com.cvshealth.a11y.agent.core

import com.cvshealth.a11y.agent.rules.*
import com.cvshealth.a11y.agent.scanner.ComposableFunction
import com.cvshealth.a11y.agent.scanner.KotlinFileScanner
import java.io.File

class RuleRegistry {

    private val _rules = mutableListOf<A11yRule>()
    val rules: List<A11yRule> get() = _rules

    var disabledRuleIDs = mutableSetOf<String>()
    var config: A11yConfig = A11yConfig.empty

    data class LocatedComposable(val filePath: String, val composable: ComposableFunction)

    private val _composables = mutableListOf<LocatedComposable>()
    val composables: List<LocatedComposable> get() = _composables

    init {
        registerBuiltInRules()
    }

    fun register(rule: A11yRule) {
        _rules.add(rule)
    }

    private fun registerBuiltInRules() {
        // Images (WCAG 1.1.1)
        register(IconMissingLabelRule())
        register(LabelContainsRoleImageRule())
        register(EmptyContentDescriptionRule())

        // Headings (WCAG 1.3.1, 2.4.6)
        register(HeadingSemanticsMissingRule())
        register(FakeHeadingInLabelRule())

        // Buttons (WCAG 4.1.2)
        register(LabelContainsRoleButtonRule())
        register(IconButtonMissingLabelRule())
        register(VisuallyDisabledNotSemanticallyRule())

        // Label in Name (WCAG 2.5.3)
        register(LabelInNameRule())

        // Clickable / Traits (WCAG 4.1.2)
        register(ClickableMissingRoleRule())

        // Toggles (WCAG 4.1.2)
        register(ToggleMissingLabelRule())

        // Links (WCAG 2.4.4)
        register(GenericLinkTextRule())
        register(ButtonUsedAsLinkRule())

        // Touch Targets (WCAG 2.5.8)
        register(SmallTouchTargetRule())

        // Dynamic Type (WCAG 1.4.4)
        register(FixedFontSizeRule())
        register(MaxLinesOneRule())

        // Page Titles (WCAG 2.4.2)
        register(MissingPaneTitleRule())
        register(TabMissingLabelRule())

        // Accessibility Hidden (WCAG 4.1.2)
        register(HiddenWithInteractiveChildrenRule())

        // Color / Contrast (WCAG 1.4.3)
        register(HardcodedColorRule())
        register(ColorContrastRule())

        // Form Controls (WCAG 4.1.2)
        register(TextFieldMissingLabelRule())
        register(SliderMissingLabelRule())
        register(DropdownMissingLabelRule())

        // Focus (WCAG 2.4.3)
        register(DialogFocusManagementRule())

        // Animation / Motion (WCAG 2.3.1)
        register(ReduceMotionRule())

        // Input Purpose (WCAG 1.3.5)
        register(InputPurposeRule())

        // Gestures (WCAG 2.1.1)
        register(GestureMissingAlternativeRule())

        // Grouping (WCAG 1.3.1)
        register(AccessibilityGroupingRule())

        // Meaningful Sequence (WCAG 1.3.2)
        register(BoxChildOrderRule())

        // Timing (WCAG 2.2.1)
        register(TimingAdjustableRule())

        // Android-specific: RadioButton grouping
        register(RadioGroupMissingRule())
    }

    fun applyConfig(config: A11yConfig) {
        this.config = config
        disabledRuleIDs.addAll(config.disabledRules)
    }

    val enabledRules: List<A11yRule>
        get() {
            var filtered = _rules.filter { it.id !in disabledRuleIDs }
            if (config.enabledOnly.isNotEmpty()) {
                filtered = filtered.filter { it.id in config.enabledOnly }
            }
            return filtered
        }

    fun clearComposables() {
        _composables.clear()
    }

    fun analyze(sourceText: String, filePath: String): List<A11yDiagnostic> {
        val parsedFile = KotlinFileScanner.scan(sourceText, filePath)
        _composables.addAll(parsedFile.composables.map { LocatedComposable(filePath, it) })
        val context = RuleContext(
            filePath = filePath,
            sourceText = sourceText,
            sourceLines = sourceText.lines(),
            disabledRules = disabledRuleIDs,
            severityOverrides = config.severityOverrides,
            configOptions = config.configOptions
        )

        var diagnostics = enabledRules.flatMap { rule ->
            rule.check(parsedFile, context)
        }

        diagnostics = InlineSuppression.filter(diagnostics, sourceText)

        // Populate source snippets
        val sourceLines = sourceText.lines()
        diagnostics = diagnostics.map { diag ->
            val diagLine = diag.line
            val start = maxOf(0, diagLine - 2)
            val end = minOf(sourceLines.size - 1, diagLine)
            val snippet = (start..end).joinToString("\n") { idx ->
                val lineNum = idx + 1
                val marker = if (lineNum == diagLine) ">" else " "
                "$marker $lineNum | ${sourceLines[idx]}"
            }
            diag.copy(sourceSnippet = snippet)
        }

        return diagnostics.sortedWith(compareBy({ it.line }, { it.column }))
    }

    fun analyzeFile(path: String): List<A11yDiagnostic> {
        val file = File(path)
        if (!file.exists() || !file.isFile) return emptyList()
        return analyze(file.readText(), file.absolutePath)
    }

    fun analyzeDirectory(path: String): List<A11yDiagnostic> {
        val dir = File(path)
        if (!dir.exists()) return emptyList()

        return dir.walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
            .filter { !config.shouldExclude(it.relativeTo(dir).path) }
            .flatMap { file ->
                try {
                    analyzeFile(file.absolutePath)
                } catch (_: Exception) {
                    emptyList()
                }
            }
            .sortedWith(compareBy({ it.filePath }, { it.line }, { it.column }))
            .toList()
    }

    fun allFilePaths(path: String): List<String> {
        val dir = File(path)
        if (!dir.exists()) return emptyList()
        return dir.walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
            .filter { !config.shouldExclude(it.relativeTo(dir).path) }
            .map { it.absolutePath }
            .sorted()
            .toList()
    }
}
