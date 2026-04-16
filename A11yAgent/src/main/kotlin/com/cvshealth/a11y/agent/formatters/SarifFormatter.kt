package com.cvshealth.a11y.agent.formatters

import com.cvshealth.a11y.agent.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object SarifFormatter {

    private val json = Json {
        prettyPrint = true
        encodeDefaults = false
    }

    @Serializable
    data class SarifOutput(
        val `$schema`: String = "https://json.schemastore.org/sarif-2.1.0.json",
        val version: String = "2.1.0",
        val runs: List<SarifRun>
    )

    @Serializable
    data class SarifRun(
        val tool: SarifTool,
        val results: List<SarifResult>
    )

    @Serializable
    data class SarifTool(val driver: SarifDriver)

    @Serializable
    data class SarifDriver(
        val name: String = "a11y-check-android",
        val version: String = "0.1.0",
        val informationUri: String = "https://github.com/cvs-health/android-compose-accessibility-techniques",
        val rules: List<SarifRuleDescriptor>
    )

    @Serializable
    data class SarifRuleDescriptor(
        val id: String,
        val name: String,
        val shortDescription: SarifMessage,
        val helpUri: String? = null,
        val properties: SarifRuleProperties? = null
    )

    @Serializable
    data class SarifRuleProperties(
        val impact: String? = null,
        val wcagCriteria: List<String>? = null
    )

    @Serializable
    data class SarifResult(
        val ruleId: String,
        val level: String,
        val message: SarifMessage,
        val locations: List<SarifLocation>
    )

    @Serializable
    data class SarifMessage(val text: String)

    @Serializable
    data class SarifLocation(val physicalLocation: SarifPhysicalLocation)

    @Serializable
    data class SarifPhysicalLocation(
        val artifactLocation: SarifArtifactLocation,
        val region: SarifRegion
    )

    @Serializable
    data class SarifArtifactLocation(val uri: String)

    @Serializable
    data class SarifRegion(val startLine: Int, val startColumn: Int)

    fun format(diagnostics: List<A11yDiagnostic>, rules: List<A11yRule>): String {
        val ruleDescriptors = rules.map { rule ->
            val wcagUri = rule.wcagCriteria.firstOrNull()?.let {
                "https://www.w3.org/TR/WCAG22/#${criterionAnchor(it)}"
            }
            SarifRuleDescriptor(
                id = rule.id,
                name = rule.name,
                shortDescription = SarifMessage(rule.description),
                helpUri = wcagUri,
                properties = SarifRuleProperties(
                    impact = rule.impact.label,
                    wcagCriteria = rule.wcagCriteria
                )
            )
        }

        val results = diagnostics.map { diag ->
            SarifResult(
                ruleId = diag.ruleID,
                level = when (diag.severity) {
                    A11ySeverity.ERROR -> "error"
                    A11ySeverity.WARNING -> "warning"
                    A11ySeverity.INFO -> "note"
                },
                message = SarifMessage(diag.message),
                locations = listOf(
                    SarifLocation(
                        SarifPhysicalLocation(
                            artifactLocation = SarifArtifactLocation(diag.filePath),
                            region = SarifRegion(diag.line, diag.column)
                        )
                    )
                )
            )
        }

        val sarif = SarifOutput(
            runs = listOf(
                SarifRun(
                    tool = SarifTool(SarifDriver(rules = ruleDescriptors)),
                    results = results
                )
            )
        )

        return json.encodeToString(sarif)
    }

    private fun criterionAnchor(criterion: String): String {
        val anchors = mapOf(
            "1.1.1" to "non-text-content",
            "1.3.1" to "info-and-relationships",
            "1.3.2" to "meaningful-sequence",
            "1.3.5" to "identify-input-purpose",
            "1.4.3" to "contrast-minimum",
            "1.4.4" to "resize-text",
            "2.1.1" to "keyboard",
            "2.2.1" to "timing-adjustable",
            "2.3.1" to "three-flashes-or-below-threshold",
            "2.4.2" to "page-titled",
            "2.4.3" to "focus-order",
            "2.4.4" to "link-purpose-in-context",
            "2.4.6" to "headings-and-labels",
            "2.5.1" to "pointer-gestures",
            "2.5.8" to "target-size-minimum",
            "4.1.2" to "name-role-value"
        )
        return anchors[criterion] ?: criterion.replace(".", "-")
    }
}
