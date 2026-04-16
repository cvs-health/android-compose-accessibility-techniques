package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import com.cvshealth.a11y.agent.scanner.KotlinFileScanner
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ImageRulesTest {

    private fun analyze(source: String): List<A11yDiagnostic> {
        val registry = RuleRegistry()
        return registry.analyze(source, "test.kt")
    }

    // --- IconMissingLabelRule ---

    @Test
    fun `icon with null contentDescription flags error`() {
        val source = """
            @Composable
            fun MyScreen() {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.any { it.ruleID == "icon-missing-label" })
    }

    @Test
    fun `icon with contentDescription is clean`() {
        val source = """
            @Composable
            fun MyScreen() {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star"
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.none { it.ruleID == "icon-missing-label" })
    }

    @Test
    fun `icon inside IconButton with null contentDescription is allowed`() {
        val source = """
            @Composable
            fun MyScreen() {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null
                    )
                }
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.none { it.ruleID == "icon-missing-label" })
    }

    // --- LabelContainsRoleImageRule ---

    @Test
    fun `contentDescription containing image flags warning`() {
        val source = """
            @Composable
            fun MyScreen() {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "star image"
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.any { it.ruleID == "label-contains-role-image" })
    }

    @Test
    fun `contentDescription containing icon flags warning`() {
        val source = """
            @Composable
            fun MyScreen() {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "menu icon"
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.any { it.ruleID == "label-contains-role-image" })
    }

    @Test
    fun `contentDescription without role word is clean`() {
        val source = """
            @Composable
            fun MyScreen() {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Favorite"
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.none { it.ruleID == "label-contains-role-image" })
    }

    // --- EmptyContentDescriptionRule ---

    @Test
    fun `empty string contentDescription flags error`() {
        val source = """
            @Composable
            fun MyScreen() {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = ""
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.any { it.ruleID == "empty-content-description" })
    }

    @Test
    fun `null contentDescription does not flag empty content description rule`() {
        val source = """
            @Composable
            fun MyScreen() {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.none { it.ruleID == "empty-content-description" })
    }
}
