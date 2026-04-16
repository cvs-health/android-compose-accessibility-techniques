package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class ToggleAndButtonRulesTest {

    private fun analyze(source: String): List<A11yDiagnostic> {
        val registry = RuleRegistry()
        return registry.analyze(source, "test.kt")
    }

    // --- ToggleMissingLabelRule ---

    @Test
    fun `checkbox with onCheckedChange flags error`() {
        val source = """
            @Composable
            fun MyScreen() {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it }
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.any { it.ruleID == "toggle-missing-label" })
    }

    @Test
    fun `checkbox with null onCheckedChange is clean`() {
        val source = """
            @Composable
            fun MyScreen() {
                Row(modifier = Modifier.toggleable(value = checked, onValueChange = { checked = it }, role = Role.Checkbox)) {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = null
                    )
                    Text("Accept terms")
                }
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.none { it.ruleID == "toggle-missing-label" })
    }

    @Test
    fun `switch with onCheckedChange flags error`() {
        val source = """
            @Composable
            fun MyScreen() {
                Switch(
                    checked = isOn,
                    onCheckedChange = { isOn = it }
                )
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.any { it.ruleID == "toggle-missing-label" })
    }

    // --- IconButtonMissingLabelRule ---

    @Test
    fun `icon button without semantics label flags error`() {
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
        assertTrue(diags.any { it.ruleID == "icon-button-missing-label" })
    }

    @Test
    fun `icon button with semantics contentDescription is clean`() {
        val source = """
            @Composable
            fun MyScreen() {
                IconButton(
                    onClick = {},
                    modifier = Modifier.semantics { contentDescription = "Open menu" }
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null
                    )
                }
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.none { it.ruleID == "icon-button-missing-label" })
    }

    // --- LabelContainsRoleButtonRule ---

    @Test
    fun `button with button in text arg flags warning`() {
        // Rule checks rawArgumentText, so the text must be in paren args
        val source = """
            @Composable
            fun MyScreen() {
                TextButton(onClick = {}, content = { Text("Submit button") })
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.any { it.ruleID == "label-contains-role-button" })
    }

    @Test
    fun `button without role word in args is clean`() {
        val source = """
            @Composable
            fun MyScreen() {
                TextButton(onClick = {}, content = { Text("Submit") })
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.none { it.ruleID == "label-contains-role-button" })
    }

    // --- RadioGroupMissingRule ---

    @Test
    fun `radio button outside selectableGroup flags error`() {
        val source = """
            @Composable
            fun MyScreen() {
                Column {
                    RadioButton(selected = true, onClick = {})
                    RadioButton(selected = false, onClick = {})
                }
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.any { it.ruleID == "radio-group-missing" })
    }

    @Test
    fun `radio button inside selectableGroup is clean`() {
        val source = """
            @Composable
            fun MyScreen() {
                Column(modifier = Modifier.selectableGroup()) {
                    RadioButton(selected = true, onClick = {})
                    RadioButton(selected = false, onClick = {})
                }
            }
        """.trimIndent()
        val diags = analyze(source)
        assertTrue(diags.none { it.ruleID == "radio-group-missing" })
    }
}
