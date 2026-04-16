package com.cvshealth.a11y.agent.core

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InlineSuppressionTest {

    @Test
    fun `same-line disable suppresses specific rule`() {
        val source = """
            Icon(contentDescription = null) // a11y-check:disable icon-missing-label
        """.trimIndent()

        val suppressions = InlineSuppression.parseSuppressionsFromSource(source)
        assertEquals(1, suppressions.size)
        assertEquals(1, suppressions[0].line)
        assertTrue(suppressions[0].ruleIDs!!.contains("icon-missing-label"))
    }

    @Test
    fun `same-line disable without rule IDs suppresses all`() {
        val source = """
            Icon(contentDescription = null) // a11y-check:disable
        """.trimIndent()

        val suppressions = InlineSuppression.parseSuppressionsFromSource(source)
        assertEquals(1, suppressions.size)
        assertEquals(null, suppressions[0].ruleIDs)
    }

    @Test
    fun `disable-next-line suppresses next line`() {
        val source = """
            // a11y-check:disable-next-line icon-missing-label
            Icon(contentDescription = null)
        """.trimIndent()

        val suppressions = InlineSuppression.parseSuppressionsFromSource(source)
        assertEquals(1, suppressions.size)
        assertEquals(2, suppressions[0].line)
        assertTrue(suppressions[0].ruleIDs!!.contains("icon-missing-label"))
    }

    @Test
    fun `filter removes matching diagnostics`() {
        val source = """
            Icon(contentDescription = null) // a11y-check:disable icon-missing-label
        """.trimIndent()

        val diags = listOf(
            A11yDiagnostic(
                ruleID = "icon-missing-label",
                severity = A11ySeverity.ERROR,
                message = "test",
                filePath = "test.kt",
                line = 1
            )
        )

        val filtered = InlineSuppression.filter(diags, source)
        assertTrue(filtered.isEmpty())
    }

    @Test
    fun `filter keeps non-matching diagnostics`() {
        val source = """
            Icon(contentDescription = null) // a11y-check:disable other-rule
        """.trimIndent()

        val diags = listOf(
            A11yDiagnostic(
                ruleID = "icon-missing-label",
                severity = A11ySeverity.ERROR,
                message = "test",
                filePath = "test.kt",
                line = 1
            )
        )

        val filtered = InlineSuppression.filter(diags, source)
        assertEquals(1, filtered.size)
    }

    @Test
    fun `multiple comma-separated rules parsed`() {
        val source = """
            Icon(contentDescription = null) // a11y-check:disable icon-missing-label, empty-content-description
        """.trimIndent()

        val suppressions = InlineSuppression.parseSuppressionsFromSource(source)
        assertEquals(1, suppressions.size)
        assertTrue(suppressions[0].ruleIDs!!.contains("icon-missing-label"))
        assertTrue(suppressions[0].ruleIDs!!.contains("empty-content-description"))
    }
}
