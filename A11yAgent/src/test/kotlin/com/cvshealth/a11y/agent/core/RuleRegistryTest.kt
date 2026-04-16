package com.cvshealth.a11y.agent.core

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RuleRegistryTest {

    @Test
    fun `registry has 32 built-in rules`() {
        val registry = RuleRegistry()
        assertEquals(32, registry.rules.size)
    }

    @Test
    fun `all rules have unique IDs`() {
        val registry = RuleRegistry()
        val ids = registry.rules.map { it.id }
        assertEquals(ids.size, ids.distinct().size)
    }

    @Test
    fun `all rules have WCAG criteria`() {
        val registry = RuleRegistry()
        for (rule in registry.rules) {
            assertTrue(rule.wcagCriteria.isNotEmpty(), "Rule ${rule.id} should have WCAG criteria")
        }
    }

    @Test
    fun `disabling a rule excludes it from enabled`() {
        val registry = RuleRegistry()
        registry.disabledRuleIDs.add("icon-missing-label")
        assertTrue(registry.enabledRules.none { it.id == "icon-missing-label" })
    }

    @Test
    fun `applyConfig disables rules from config`() {
        val config = A11yConfig(disabled_rules = listOf("fixed-font-size", "hardcoded-color"))
        val registry = RuleRegistry()
        registry.applyConfig(config)
        assertTrue(registry.enabledRules.none { it.id == "fixed-font-size" })
        assertTrue(registry.enabledRules.none { it.id == "hardcoded-color" })
    }

    @Test
    fun `enabled_only limits rules to specified set`() {
        val config = A11yConfig(enabled_only = listOf("icon-missing-label"))
        val registry = RuleRegistry()
        registry.applyConfig(config)
        assertEquals(1, registry.enabledRules.size)
        assertEquals("icon-missing-label", registry.enabledRules[0].id)
    }

    @Test
    fun `analyze returns diagnostics for bad code`() {
        val source = """
            @Composable
            fun MyScreen() {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null
                )
            }
        """.trimIndent()

        val registry = RuleRegistry()
        val diags = registry.analyze(source, "test.kt")
        assertTrue(diags.isNotEmpty())
        assertTrue(diags.any { it.ruleID == "icon-missing-label" })
    }

    @Test
    fun `analyze returns empty for clean code`() {
        val source = """
            @Composable
            fun MyScreen() {
                Text(
                    text = "Hello World",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        """.trimIndent()

        val registry = RuleRegistry()
        val diags = registry.analyze(source, "test.kt")
        assertTrue(diags.isEmpty())
    }

    @Test
    fun `inline suppression works through registry`() {
        val source = """
            @Composable
            fun MyScreen() {
                Icon( // a11y-check:disable icon-missing-label
                    imageVector = Icons.Default.Star,
                    contentDescription = null
                )
            }
        """.trimIndent()

        val registry = RuleRegistry()
        val diags = registry.analyze(source, "test.kt")
        assertTrue(diags.none { it.ruleID == "icon-missing-label" })
    }
}
