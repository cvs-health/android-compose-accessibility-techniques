package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class FormControlRulesTest {

    private fun analyze(source: String): List<A11yDiagnostic> {
        val registry = RuleRegistry()
        return registry.analyze(source, "test.kt")
    }

    // --- TextFieldMissingLabelRule ---

    @Test
    fun `textfield without label flags error`() {
        val source = """
            @Composable
            fun MyScreen() {
                TextField(
                    value = text,
                    onValueChange = { text = it }
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.any { it.ruleID == "textfield-missing-label" })
    }

    @Test
    fun `textfield with label is clean`() {
        val source = """
            @Composable
            fun MyScreen() {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Username") }
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.none { it.ruleID == "textfield-missing-label" })
    }

    @Test
    fun `outlined textfield without label flags error`() {
        val source = """
            @Composable
            fun MyScreen() {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it }
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.any { it.ruleID == "textfield-missing-label" })
    }

    // --- SliderMissingLabelRule ---

    @Test
    fun `slider without label flags error`() {
        val source = """
            @Composable
            fun MyScreen() {
                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it }
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.any { it.ruleID == "slider-missing-label" })
    }

    @Test
    fun `slider with semantics contentDescription is clean`() {
        val source = """
            @Composable
            fun MyScreen() {
                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    modifier = Modifier.semantics { contentDescription = "Volume" }
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.none { it.ruleID == "slider-missing-label" })
    }

    // --- DropdownMissingLabelRule ---

    @Test
    fun `ExposedDropdownMenuBox without labeled TextField flags error`() {
        val source = """
            @Composable
            fun MyScreen() {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    TextField(
                        value = selected,
                        onValueChange = {},
                        readOnly = true
                    )
                }
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.any { it.ruleID == "dropdown-missing-label" })
    }
}
