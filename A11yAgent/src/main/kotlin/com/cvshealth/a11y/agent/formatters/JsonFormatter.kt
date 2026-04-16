package com.cvshealth.a11y.agent.formatters

import com.cvshealth.a11y.agent.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object JsonFormatter {

    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
    }

    @Serializable
    data class DiagnosticJson(
        val ruleID: String,
        val severity: String,
        val impact: String,
        val message: String,
        val filePath: String,
        val line: Int,
        val column: Int,
        val wcagCriteria: List<String>,
        val suggestion: String? = null
    )

    @Serializable
    data class ScoreJson(
        val score: Double,
        val grade: String,
        val totalErrors: Int,
        val totalWarnings: Int,
        val totalInfo: Int,
        val filesAnalyzed: Int,
        val criteriaPassed: Int,
        val criteriaFailed: Int,
        val criteriaNotChecked: Int,
        val failedCriteria: List<String> = emptyList(),
        val reviewCriteria: List<String> = emptyList()
    )

    @Serializable
    data class TrendJson(
        val entries: List<TrendEntryJson>,
        val delta: Double
    )

    @Serializable
    data class TrendEntryJson(
        val date: String,
        val score: Double,
        val grade: String,
        val errors: Int,
        val warnings: Int
    )

    @Serializable
    data class OutputJson(
        val diagnostics: List<DiagnosticJson>,
        val score: ScoreJson,
        val trend: TrendJson? = null
    )

    fun format(
        diagnostics: List<A11yDiagnostic>,
        score: A11yScore,
        trendEntries: List<TrendTracker.Entry>? = null
    ): String {
        val diagsJson = diagnostics.map { diag ->
            DiagnosticJson(
                ruleID = diag.ruleID,
                severity = diag.severity.label,
                impact = diag.impact.label,
                message = diag.message,
                filePath = diag.filePath,
                line = diag.line,
                column = diag.column,
                wcagCriteria = diag.wcagCriteria,
                suggestion = diag.suggestion
            )
        }

        val failedCriteria = score.criteriaScores
            .filter { it.status == CriterionStatus.FAIL }
            .map { "${it.criterion} ${it.name}" }
        val reviewCriteria = score.criteriaScores
            .filter { it.status == CriterionStatus.REVIEW }
            .map { "${it.criterion} ${it.name}" }

        val scoreJson = ScoreJson(
            score = score.score,
            grade = score.grade,
            totalErrors = score.totalErrors,
            totalWarnings = score.totalWarnings,
            totalInfo = score.totalInfo,
            filesAnalyzed = score.filesAnalyzed,
            criteriaPassed = score.criteriaPassed,
            criteriaFailed = score.criteriaFailed,
            criteriaNotChecked = score.criteriaNotChecked,
            failedCriteria = failedCriteria,
            reviewCriteria = reviewCriteria
        )

        val trendJson = trendEntries?.let { entries ->
            val delta = if (entries.isNotEmpty()) score.score - entries.last().score else 0.0
            TrendJson(
                entries = entries.map {
                    TrendEntryJson(it.date, it.score, it.grade, it.errors, it.warnings)
                },
                delta = delta
            )
        }

        val output = OutputJson(
            diagnostics = diagsJson,
            score = scoreJson,
            trend = trendJson
        )

        return json.encodeToString(output)
    }
}
