package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class VisualRulesTest {

    private fun analyze(source: String): List<A11yDiagnostic> {
        val registry = RuleRegistry()
        return registry.analyze(source, "test.kt")
    }

    // --- FixedFontSizeRule ---

    @Test
    fun `hardcoded sp font size flags warning`() {
        val source = """
            @Composable
            fun MyScreen() {
                Text(
                    text = "Hello",
                    fontSize = 16.sp
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.any { it.ruleID == "fixed-font-size" })
    }

    @Test
    fun `MaterialTheme typography is clean`() {
        val source = """
            @Composable
            fun MyScreen() {
                Text(
                    text = "Hello",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.none { it.ruleID == "fixed-font-size" })
    }

    // --- MaxLinesOneRule ---

    @Test
    fun `maxLines 1 flags info`() {
        val source = """
            @Composable
            fun MyScreen() {
                Text(
                    text = "Some long text",
                    maxLines = 1
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.any { it.ruleID == "max-lines-one" })
    }

    // --- HardcodedColorRule ---

    @Test
    fun `Color dot Black flags warning`() {
        val source = """
            @Composable
            fun MyScreen() {
                Text(
                    text = "Hello",
                    color = Color.Black
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.any { it.ruleID == "hardcoded-color" })
    }

    @Test
    fun `MaterialTheme colorScheme color is clean`() {
        val source = """
            @Composable
            fun MyScreen() {
                Text(
                    text = "Hello",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.none { it.ruleID == "hardcoded-color" })
    }

    // --- ClickableMissingRoleRule ---

    @Test
    fun `clickable without role flags warning`() {
        val source = """
            @Composable
            fun MyScreen() {
                Box(modifier = Modifier.clickable { doSomething() }) {
                    Text("Click me")
                }
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.any { it.ruleID == "clickable-missing-role" })
    }

    @Test
    fun `clickable with onClickLabel is clean`() {
        val source = """
            @Composable
            fun MyScreen() {
                Box(modifier = Modifier.clickable(onClickLabel = "Activate item") { doSomething() }) {
                    Text("Click me")
                }
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.none { it.ruleID == "clickable-missing-role" })
    }
}
