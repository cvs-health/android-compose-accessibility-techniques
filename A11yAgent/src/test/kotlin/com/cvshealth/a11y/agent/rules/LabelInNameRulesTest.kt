package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LabelInNameRulesTest {

    private fun analyze(source: String): List<A11yDiagnostic> {
        val registry = RuleRegistry()
        return registry.analyze(source, "test.kt")
    }

    private fun labelInNameDiags(source: String) =
        analyze(source).filter { it.ruleID == "label-in-name" }

    // --- ERROR cases: visible text not in accessible name ---

    @Test
    fun `button with contentDescription that excludes visible text flags error`() {
        val source = """
            @Composable
            fun MyScreen() {
                Button(
                    onClick = {},
                    modifier = Modifier.semantics { contentDescription = "Submit form" }
                ) {
                    Text("Save")
                }
            }
        """.trimIndent()
        val diags = labelInNameDiags(source)
        assertEquals(1, diags.size)
        assertEquals(A11ySeverity.ERROR, diags[0].severity)
        assertTrue(diags[0].message.contains("not contained"))
    }

    @Test
    fun `icon button with mismatched contentDescription flags error`() {
        val source = """
            @Composable
            fun MyScreen() {
                IconButton(
                    onClick = {},
                    modifier = Modifier.semantics { contentDescription = "Navigation drawer" }
                ) {
                    Text("Menu")
                }
            }
        """.trimIndent()
        val diags = labelInNameDiags(source)
        assertEquals(1, diags.size)
        assertEquals(A11ySeverity.ERROR, diags[0].severity)
    }

    @Test
    fun `tab with mismatched contentDescription flags error`() {
        val source = """
            @Composable
            fun MyScreen() {
                Tab(
                    selected = true,
                    onClick = {},
                    text = { Text("Home") },
                    modifier = Modifier.semantics { contentDescription = "Dashboard" }
                )
            }
        """.trimIndent()
        val diags = labelInNameDiags(source)
        assertEquals(1, diags.size)
        assertEquals(A11ySeverity.ERROR, diags[0].severity)
    }

    // --- WARNING cases: visible text present but not at start ---

    @Test
    fun `button with visible text as suffix in accessible name flags warning`() {
        val source = """
            @Composable
            fun MyScreen() {
                Button(
                    onClick = {},
                    modifier = Modifier.semantics { contentDescription = "Quick Save" }
                ) {
                    Text("Save")
                }
            }
        """.trimIndent()
        val diags = labelInNameDiags(source)
        assertEquals(1, diags.size)
        assertEquals(A11ySeverity.WARNING, diags[0].severity)
        assertTrue(diags[0].message.contains("not at the start"))
    }

    // --- CLEAN cases: no diagnostic expected ---

    @Test
    fun `button with accessible name starting with visible text is clean`() {
        val source = """
            @Composable
            fun MyScreen() {
                Button(
                    onClick = {},
                    modifier = Modifier.semantics { contentDescription = "Save document" }
                ) {
                    Text("Save")
                }
            }
        """.trimIndent()
        val diags = labelInNameDiags(source)
        assertTrue(diags.isEmpty(), "Expected no diagnostics but got: $diags")
    }

    @Test
    fun `button with exact match accessible name is clean`() {
        val source = """
            @Composable
            fun MyScreen() {
                Button(
                    onClick = {},
                    modifier = Modifier.semantics { contentDescription = "Save" }
                ) {
                    Text("Save")
                }
            }
        """.trimIndent()
        val diags = labelInNameDiags(source)
        assertTrue(diags.isEmpty())
    }

    @Test
    fun `button with case-insensitive match is clean`() {
        val source = """
            @Composable
            fun MyScreen() {
                Button(
                    onClick = {},
                    modifier = Modifier.semantics { contentDescription = "save document" }
                ) {
                    Text("SAVE")
                }
            }
        """.trimIndent()
        val diags = labelInNameDiags(source)
        assertTrue(diags.isEmpty())
    }

    @Test
    fun `button without contentDescription is clean`() {
        val source = """
            @Composable
            fun MyScreen() {
                Button(onClick = {}) {
                    Text("Save")
                }
            }
        """.trimIndent()
        val diags = labelInNameDiags(source)
        assertTrue(diags.isEmpty())
    }

    @Test
    fun `button with non-literal contentDescription is skipped`() {
        val source = """
            @Composable
            fun MyScreen() {
                Button(
                    onClick = {},
                    modifier = Modifier.semantics { contentDescription = stringResource(R.string.save) }
                ) {
                    Text("Save")
                }
            }
        """.trimIndent()
        val diags = labelInNameDiags(source)
        assertTrue(diags.isEmpty())
    }

    @Test
    fun `button with clearAndSetSemantics is skipped`() {
        val source = """
            @Composable
            fun MyScreen() {
                Button(
                    onClick = {},
                    modifier = Modifier.clearAndSetSemantics { contentDescription = "Submit" }
                ) {
                    Text("Save")
                }
            }
        """.trimIndent()
        val diags = labelInNameDiags(source)
        assertTrue(diags.isEmpty())
    }
}
