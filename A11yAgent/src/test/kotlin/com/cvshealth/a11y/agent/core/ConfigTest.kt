package com.cvshealth.a11y.agent.core

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConfigTest {

    @Test
    fun `parse valid yaml config`() {
        val yaml = """
            disabled_rules:
              - fixed-font-size
              - hardcoded-color
            severity_overrides:
              icon-missing-label: WARNING
            options:
              min_touch_target: 44
            exclude_paths:
              - "**/test/**"
        """.trimIndent()

        val config = ConfigLoader.parse(yaml)
        assertTrue(config.disabledRules.contains("fixed-font-size"))
        assertTrue(config.disabledRules.contains("hardcoded-color"))
        assertEquals(A11ySeverity.WARNING, config.severityOverrides["icon-missing-label"])
        assertEquals(44, config.configOptions.minTouchTarget)
        assertTrue(config.shouldExclude("src/test/MyTest.kt"))
    }

    @Test
    fun `parse empty yaml returns defaults`() {
        val config = ConfigLoader.parse("")
        assertTrue(config.disabledRules.isEmpty())
        assertTrue(config.severityOverrides.isEmpty())
        assertEquals(48, config.configOptions.minTouchTarget)
        assertEquals(4.5, config.configOptions.contrastRatio)
    }

    @Test
    fun `parse invalid yaml returns empty config`() {
        val config = ConfigLoader.parse("{{invalid yaml}}")
        assertEquals(A11yConfig.empty, config)
    }

    @Test
    fun `enabled_only restricts rules`() {
        val yaml = """
            enabled_only:
              - icon-missing-label
              - heading-semantics-missing
        """.trimIndent()

        val config = ConfigLoader.parse(yaml)
        assertEquals(setOf("icon-missing-label", "heading-semantics-missing"), config.enabledOnly)
    }

    @Test
    fun `shouldExclude with glob patterns`() {
        val config = A11yConfig(exclude_paths = listOf("**/build/**", "*.generated.kt"))
        assertTrue(config.shouldExclude("app/build/generated/File.kt"))
        assertTrue(config.shouldExclude("Theme.generated.kt"))
        assertFalse(config.shouldExclude("app/src/main/MyScreen.kt"))
    }

    @Test
    fun `shouldExclude with simple pattern`() {
        val config = A11yConfig(exclude_paths = listOf("*.test.kt"))
        assertTrue(config.shouldExclude("MyFile.test.kt"))
        assertFalse(config.shouldExclude("MyFile.kt"))
    }

    @Test
    fun `severity override mapping is correct`() {
        val config = A11yConfig(severity_overrides = mapOf(
            "icon-missing-label" to "WARNING",
            "heading-semantics-missing" to "INFO",
            "invalid-rule" to "INVALID_SEVERITY"
        ))
        assertEquals(A11ySeverity.WARNING, config.severityOverrides["icon-missing-label"])
        assertEquals(A11ySeverity.INFO, config.severityOverrides["heading-semantics-missing"])
        assertFalse(config.severityOverrides.containsKey("invalid-rule"))
    }
}
