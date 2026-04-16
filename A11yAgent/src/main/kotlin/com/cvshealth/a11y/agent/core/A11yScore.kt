package com.cvshealth.a11y.agent.core

import kotlinx.serialization.Serializable

@Serializable
enum class WCAGLevel(val label: String) {
    A("A"), AA("AA"), AAA("AAA")
}

@Serializable
enum class WCAGPrinciple(val label: String) {
    PERCEIVABLE("Perceivable"),
    OPERABLE("Operable"),
    UNDERSTANDABLE("Understandable"),
    ROBUST("Robust");

    companion object {
        fun from(criterion: String): WCAGPrinciple {
            return when (criterion.firstOrNull()) {
                '1' -> PERCEIVABLE
                '2' -> OPERABLE
                '3' -> UNDERSTANDABLE
                '4' -> ROBUST
                else -> PERCEIVABLE
            }
        }
    }
}

@Serializable
enum class CriterionStatus(val label: String) {
    PASS("pass"),
    FAIL("fail"),
    REVIEW("review"),
    NOT_CHECKED("not_checked")
}

data class CriterionScore(
    val criterion: String,
    val name: String,
    val principle: WCAGPrinciple,
    val level: WCAGLevel,
    val status: CriterionStatus,
    val errorCount: Int,
    val warningCount: Int,
    val infoCount: Int = 0,
    val ruleIDs: List<String>
)

data class FileScore(
    val filePath: String,
    val score: Double,
    val errorCount: Int,
    val warningCount: Int,
    val infoCount: Int
)

data class A11yScore(
    val score: Double,
    val grade: String,
    val criteriaScores: List<CriterionScore>,
    val principleScores: Map<WCAGPrinciple, Double>,
    val fileScores: List<FileScore>,
    val totalErrors: Int,
    val totalWarnings: Int,
    val totalInfo: Int,
    val filesAnalyzed: Int,
    val criteriaPassed: Int,
    val criteriaFailed: Int,
    val criteriaNotChecked: Int
) {
    companion object {
        fun letterGrade(score: Double): String = when {
            score >= 97.0 -> "A+"
            score >= 93.0 -> "A"
            score >= 90.0 -> "A-"
            score >= 87.0 -> "B+"
            score >= 83.0 -> "B"
            score >= 80.0 -> "B-"
            score >= 77.0 -> "C+"
            score >= 73.0 -> "C"
            score >= 70.0 -> "C-"
            score >= 67.0 -> "D+"
            score >= 63.0 -> "D"
            score >= 60.0 -> "D-"
            else -> "F"
        }
    }
}
