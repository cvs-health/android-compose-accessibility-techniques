package com.cvshealth.a11y.agent.core

import kotlinx.serialization.Serializable

@Serializable
enum class A11ySeverity : Comparable<A11ySeverity> {
    INFO, WARNING, ERROR;

    val label: String get() = name.lowercase()
}

@Serializable
enum class A11yImpact : Comparable<A11yImpact> {
    MINOR, MODERATE, SERIOUS, CRITICAL;

    val label: String get() = name.lowercase()
}

data class A11yFix(
    val description: String,
    val replacementText: String,
    val startOffset: Int,
    val endOffset: Int
)

data class A11yDiagnostic(
    val ruleID: String,
    val severity: A11ySeverity,
    val impact: A11yImpact = A11yImpact.MODERATE,
    val message: String,
    val filePath: String,
    val line: Int,
    val column: Int = 1,
    val wcagCriteria: List<String> = emptyList(),
    val fix: A11yFix? = null,
    val suggestion: String? = null,
    var sourceSnippet: String? = null
)
