package com.cvshealth.a11y.agent.core

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BaselineTest {

    @Test
    fun `baseline fingerprint format`() {
        val entry = Baseline.Entry(
            ruleID = "icon-missing-label",
            filePath = "test.kt",
            line = 10,
            message = "Icon is missing contentDescription"
        )
        assertEquals("icon-missing-label|test.kt|Icon is missing contentDescription", entry.fingerprint)
    }

    @Test
    fun `filterNew removes baselined issues`() {
        val baselineEntries = listOf(
            Baseline.Entry("icon-missing-label", "test.kt", 10, "Icon is missing contentDescription")
        )
        val baseline = Baseline(entries = baselineEntries)

        val diagnostics = listOf(
            A11yDiagnostic(
                ruleID = "icon-missing-label",
                severity = A11ySeverity.ERROR,
                message = "Icon is missing contentDescription",
                filePath = "test.kt",
                line = 10
            ),
            A11yDiagnostic(
                ruleID = "heading-semantics-missing",
                severity = A11ySeverity.WARNING,
                message = "Text uses heading typography",
                filePath = "test.kt",
                line = 20
            )
        )

        val newIssues = baseline.filterNew(diagnostics)
        assertEquals(1, newIssues.size)
        assertEquals("heading-semantics-missing", newIssues[0].ruleID)
    }

    @Test
    fun `from creates baseline from diagnostics`() {
        val diagnostics = listOf(
            A11yDiagnostic(
                ruleID = "icon-missing-label",
                severity = A11ySeverity.ERROR,
                message = "test message",
                filePath = "test.kt",
                line = 5
            )
        )
        val baseline = Baseline.from(diagnostics, 85.0)
        assertEquals(1, baseline.entries.size)
        assertEquals(85.0, baseline.score)
        assertEquals("icon-missing-label", baseline.entries[0].ruleID)
    }

    @Test
    fun `save and load baseline roundtrip`(@TempDir tempDir: File) {
        val diagnostics = listOf(
            A11yDiagnostic(
                ruleID = "icon-missing-label",
                severity = A11ySeverity.ERROR,
                message = "test",
                filePath = "test.kt",
                line = 5
            )
        )
        val baseline = Baseline.from(diagnostics, 90.0)
        baseline.save(tempDir.absolutePath)

        val loaded = Baseline.loadFrom(tempDir.absolutePath)
        assertNotNull(loaded)
        assertEquals(1, loaded.entries.size)
        assertEquals("icon-missing-label", loaded.entries[0].ruleID)
        assertEquals(90.0, loaded.score)
    }

    @Test
    fun `loadFrom returns null when no baseline exists`(@TempDir tempDir: File) {
        val loaded = Baseline.loadFrom(tempDir.absolutePath)
        assertEquals(null, loaded)
    }
}
