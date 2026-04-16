/*
 * Copyright 2026 CVS Health and/or one of its affiliates
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cvshealth.a11y.agent.core

/**
 * Generates Markdown documentation for all registered rules.
 * Produces a summary table, WCAG criterion grouping, and severity breakdown.
 */
class RuleDocsGenerator {

    fun generate(rules: List<A11yRule>): String {
        val sb = StringBuilder()

        val uniqueCriteria = rules.flatMap { it.wcagCriteria }.toSet()
        sb.appendLine("# a11y-check Rules Reference")
        sb.appendLine()
        sb.appendLine("Generated automatically. ${rules.size} rules across ${uniqueCriteria.size} WCAG criteria.")
        sb.appendLine()

        // Summary table
        sb.appendLine("| # | Rule ID | Severity | WCAG | Description |")
        sb.appendLine("|---|---------|----------|------|-------------|")
        for ((i, rule) in rules.sortedBy { it.wcagCriteria.firstOrNull() ?: "z" }.withIndex()) {
            val wcag = rule.wcagCriteria.joinToString(", ")
            sb.appendLine("| ${i + 1} | `${rule.id}` | ${rule.severity.label} | $wcag | ${rule.description} |")
        }
        sb.appendLine()

        // Group by WCAG criterion
        sb.appendLine("---")
        sb.appendLine()
        sb.appendLine("## By WCAG Criterion")
        sb.appendLine()

        val byCriterion = mutableMapOf<String, MutableList<A11yRule>>()
        for (rule in rules) {
            for (criterion in rule.wcagCriteria) {
                byCriterion.getOrPut(criterion) { mutableListOf() }.add(rule)
            }
        }
        for ((criterion, rulesForCriterion) in byCriterion.toSortedMap()) {
            sb.appendLine("### WCAG $criterion")
            sb.appendLine()
            for (rule in rulesForCriterion) {
                sb.appendLine("- **`${rule.id}`** (${rule.severity.label}) — ${rule.name}")
            }
            sb.appendLine()
        }

        // Severity breakdown
        sb.appendLine("## By Severity")
        sb.appendLine()

        val errors = rules.filter { it.severity == A11ySeverity.ERROR }
        val warnings = rules.filter { it.severity == A11ySeverity.WARNING }
        val infos = rules.filter { it.severity == A11ySeverity.INFO }

        if (errors.isNotEmpty()) {
            sb.appendLine("### Errors (${errors.size})")
            sb.appendLine()
            for (rule in errors) {
                sb.appendLine("- `${rule.id}` — ${rule.name}")
            }
            sb.appendLine()
        }
        if (warnings.isNotEmpty()) {
            sb.appendLine("### Warnings (${warnings.size})")
            sb.appendLine()
            for (rule in warnings) {
                sb.appendLine("- `${rule.id}` — ${rule.name}")
            }
            sb.appendLine()
        }
        if (infos.isNotEmpty()) {
            sb.appendLine("### Info (${infos.size})")
            sb.appendLine()
            for (rule in infos) {
                sb.appendLine("- `${rule.id}` — ${rule.name}")
            }
            sb.appendLine()
        }

        return sb.toString()
    }
}
