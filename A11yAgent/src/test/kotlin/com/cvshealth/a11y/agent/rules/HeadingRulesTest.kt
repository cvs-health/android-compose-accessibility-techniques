package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class HeadingRulesTest {

    private fun analyze(source: String): List<A11yDiagnostic> {
        val registry = RuleRegistry()
        return registry.analyze(source, "test.kt")
    }

    // --- HeadingSemanticsMissingRule ---

    @Test
    fun `text with heading style without heading semantics flags warning`() {
        val source = """
            @Composable
            fun MyScreen() {
                Text(
                    text = "Section Title",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.any { it.ruleID == "heading-semantics-missing" })
    }

    @Test
    fun `text with heading style and heading semantics is clean`() {
        val source = """
            @Composable
            fun MyScreen() {
                Text(
                    text = "Section Title",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.semantics { heading() }
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.none { it.ruleID == "heading-semantics-missing" })
    }

    @Test
    fun `text with body style is not flagged`() {
        val source = """
            @Composable
            fun MyScreen() {
                Text(
                    text = "Regular body text",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.none { it.ruleID == "heading-semantics-missing" })
    }

    @Test
    fun `text with titleLarge without heading semantics flags warning`() {
        val source = """
            @Composable
            fun MyScreen() {
                Text(
                    text = "Page Title",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.any { it.ruleID == "heading-semantics-missing" })
    }

    // --- FakeHeadingInLabelRule ---

    @Test
    fun `contentDescription containing heading flags warning`() {
        val source = """
            @Composable
            fun MyScreen() {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Section heading"
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.any { it.ruleID == "fake-heading-in-label" })
    }
}
