package com.cvshealth.a11y.agent.core

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ScoreCalculatorTest {

    private val calculator = ScoreCalculator()

    private fun makeRule(
        id: String = "test-rule",
        severity: A11ySeverity = A11ySeverity.ERROR,
        impact: A11yImpact = A11yImpact.SERIOUS,
        wcag: List<String> = listOf("1.1.1")
    ): A11yRule = object : A11yRule {
        override val id = id
        override val name = "Test Rule"
        override val severity = severity
        override val impact = impact
        override val wcagCriteria = wcag
        override val description = "Test rule"
        override fun check(
            file: com.cvshealth.a11y.agent.scanner.ParsedKotlinFile,
            context: RuleContext
        ) = emptyList<A11yDiagnostic>()
    }

    private fun makeDiag(
        ruleID: String = "test-rule",
        severity: A11ySeverity = A11ySeverity.ERROR,
        impact: A11yImpact = A11yImpact.SERIOUS,
        wcag: List<String> = listOf("1.1.1"),
        filePath: String = "test.kt"
    ) = A11yDiagnostic(
        ruleID = ruleID,
        severity = severity,
        impact = impact,
        message = "Test issue",
        filePath = filePath,
        line = 1,
        wcagCriteria = wcag
    )

    @Test
    fun `perfect score with no diagnostics`() {
        val score = calculator.calculate(emptyList(), listOf(makeRule()), listOf("test.kt"))
        assertEquals(100.0, score.score)
        assertEquals("A+", score.grade)
        assertEquals(0, score.totalErrors)
        assertEquals(0, score.totalWarnings)
        assertEquals(0, score.totalInfo)
    }

    @Test
    fun `score decreases with errors`() {
        val diags = listOf(makeDiag())
        val score = calculator.calculate(diags, listOf(makeRule()), listOf("test.kt"))
        assertTrue(score.score < 100.0)
        assertEquals(1, score.totalErrors)
    }

    @Test
    fun `errors penalize more than warnings`() {
        val errorDiags = listOf(makeDiag(severity = A11ySeverity.ERROR))
        val warningDiags = listOf(makeDiag(severity = A11ySeverity.WARNING))

        val rules = listOf(makeRule())
        val files = listOf("test.kt")

        val errorScore = calculator.calculate(errorDiags, rules, files)
        val warningScore = calculator.calculate(warningDiags, rules, files)

        assertTrue(errorScore.score < warningScore.score,
            "Error score (${errorScore.score}) should be less than warning score (${warningScore.score})")
    }

    @Test
    fun `critical impact penalizes more than minor`() {
        val criticalDiags = listOf(makeDiag(impact = A11yImpact.CRITICAL))
        val minorDiags = listOf(makeDiag(impact = A11yImpact.MINOR))

        val rules = listOf(makeRule())
        val files = listOf("test.kt")

        val criticalScore = calculator.calculate(criticalDiags, rules, files)
        val minorScore = calculator.calculate(minorDiags, rules, files)

        assertTrue(criticalScore.score < minorScore.score,
            "Critical score (${criticalScore.score}) should be less than minor score (${minorScore.score})")
    }

    @Test
    fun `counts errors warnings and info correctly`() {
        val diags = listOf(
            makeDiag(severity = A11ySeverity.ERROR),
            makeDiag(severity = A11ySeverity.ERROR),
            makeDiag(severity = A11ySeverity.WARNING),
            makeDiag(severity = A11ySeverity.INFO)
        )
        val score = calculator.calculate(diags, listOf(makeRule()), listOf("test.kt"))
        assertEquals(2, score.totalErrors)
        assertEquals(1, score.totalWarnings)
        assertEquals(1, score.totalInfo)
    }

    @Test
    fun `catalogs all 48 WCAG criteria`() {
        assertEquals(48, ScoreCalculator.wcagCatalog.size)
    }

    @Test
    fun `letter grades follow thresholds`() {
        assertEquals("A+", A11yScore.letterGrade(100.0))
        assertEquals("A+", A11yScore.letterGrade(97.0))
        assertEquals("A", A11yScore.letterGrade(96.0))
        assertEquals("A", A11yScore.letterGrade(93.0))
        assertEquals("A-", A11yScore.letterGrade(92.0))
        assertEquals("A-", A11yScore.letterGrade(90.0))
        assertEquals("B+", A11yScore.letterGrade(89.0))
        assertEquals("B+", A11yScore.letterGrade(87.0))
        assertEquals("B", A11yScore.letterGrade(86.0))
        assertEquals("B", A11yScore.letterGrade(83.0))
        assertEquals("B-", A11yScore.letterGrade(82.0))
        assertEquals("B-", A11yScore.letterGrade(80.0))
        assertEquals("C+", A11yScore.letterGrade(79.0))
        assertEquals("C+", A11yScore.letterGrade(77.0))
        assertEquals("C", A11yScore.letterGrade(76.0))
        assertEquals("C", A11yScore.letterGrade(73.0))
        assertEquals("C-", A11yScore.letterGrade(72.0))
        assertEquals("C-", A11yScore.letterGrade(70.0))
        assertEquals("D+", A11yScore.letterGrade(69.0))
        assertEquals("D+", A11yScore.letterGrade(67.0))
        assertEquals("D", A11yScore.letterGrade(66.0))
        assertEquals("D", A11yScore.letterGrade(63.0))
        assertEquals("D-", A11yScore.letterGrade(62.0))
        assertEquals("D-", A11yScore.letterGrade(60.0))
        assertEquals("F", A11yScore.letterGrade(59.0))
        assertEquals("F", A11yScore.letterGrade(0.0))
    }

    @Test
    fun `file scores are calculated per file`() {
        val diags = listOf(
            makeDiag(filePath = "a.kt"),
            makeDiag(filePath = "a.kt"),
            makeDiag(filePath = "b.kt", severity = A11ySeverity.WARNING)
        )
        val score = calculator.calculate(
            diags, listOf(makeRule()), listOf("a.kt", "b.kt", "clean.kt")
        )
        assertEquals(3, score.fileScores.size)
        // a.kt has 2 errors, b.kt has 1 warning, clean.kt has none
        val aScore = score.fileScores.first { it.filePath == "a.kt" }
        val bScore = score.fileScores.first { it.filePath == "b.kt" }
        val cleanScore = score.fileScores.first { it.filePath == "clean.kt" }
        assertTrue(aScore.score < bScore.score)
        assertEquals(100.0, cleanScore.score)
    }

    @Test
    fun `criteria status reflects diagnostics`() {
        val diags = listOf(makeDiag(wcag = listOf("1.1.1")))
        val rule = makeRule(wcag = listOf("1.1.1", "4.1.2"))
        val score = calculator.calculate(diags, listOf(rule), listOf("test.kt"))

        val criterion111 = score.criteriaScores.first { it.criterion == "1.1.1" }
        assertEquals(CriterionStatus.FAIL, criterion111.status)

        val criterion412 = score.criteriaScores.first { it.criterion == "4.1.2" }
        assertEquals(CriterionStatus.PASS, criterion412.status)
    }

    @Test
    fun `computeFileScore basic calculations`() {
        assertEquals(100.0, ScoreCalculator.computeFileScore(0, 0, 0))
        assertEquals(95.0, ScoreCalculator.computeFileScore(1, 0, 0))
        assertEquals(98.0, ScoreCalculator.computeFileScore(0, 1, 0))
        assertEquals(99.5, ScoreCalculator.computeFileScore(0, 0, 1))
        assertEquals(0.0, ScoreCalculator.computeFileScore(100, 0, 0))
    }
}
