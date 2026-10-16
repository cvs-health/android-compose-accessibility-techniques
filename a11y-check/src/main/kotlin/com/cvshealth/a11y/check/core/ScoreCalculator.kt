package com.cvshealth.a11y.check.core

import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class ScoreCalculator {

    companion object {
        val wcagCatalog: List<Triple<String, String, WCAGLevel>> = listOf(
            // Principle 1 - Perceivable
            Triple("1.1.1", "Non-text Content", WCAGLevel.A),
            Triple("1.3.1", "Info and Relationships", WCAGLevel.A),
            Triple("1.3.2", "Meaningful Sequence", WCAGLevel.A),
            Triple("1.3.3", "Sensory Characteristics", WCAGLevel.A),
            Triple("1.3.4", "Orientation", WCAGLevel.AA),
            Triple("1.3.5", "Identify Input Purpose", WCAGLevel.AA),
            Triple("1.4.1", "Use of Color", WCAGLevel.A),
            Triple("1.4.2", "Audio Control", WCAGLevel.A),
            Triple("1.4.3", "Contrast (Minimum)", WCAGLevel.AA),
            Triple("1.4.4", "Resize Text", WCAGLevel.AA),
            Triple("1.4.5", "Images of Text", WCAGLevel.AA),
            Triple("1.4.10", "Reflow", WCAGLevel.AA),
            Triple("1.4.11", "Non-text Contrast", WCAGLevel.AA),
            Triple("1.4.12", "Text Spacing", WCAGLevel.AA),
            Triple("1.4.13", "Content on Hover or Focus", WCAGLevel.AA),
            // Principle 2 - Operable
            Triple("2.1.1", "Keyboard", WCAGLevel.A),
            Triple("2.1.2", "No Keyboard Trap", WCAGLevel.A),
            Triple("2.1.4", "Character Key Shortcuts", WCAGLevel.A),
            Triple("2.2.1", "Timing Adjustable", WCAGLevel.A),
            Triple("2.2.2", "Pause, Stop, Hide", WCAGLevel.A),
            Triple("2.3.1", "Three Flashes or Below Threshold", WCAGLevel.A),
            Triple("2.4.1", "Bypass Blocks", WCAGLevel.A),
            Triple("2.4.2", "Page Titled", WCAGLevel.A),
            Triple("2.4.3", "Focus Order", WCAGLevel.A),
            Triple("2.4.4", "Link Purpose (In Context)", WCAGLevel.A),
            Triple("2.4.5", "Multiple Ways", WCAGLevel.AA),
            Triple("2.4.6", "Headings and Labels", WCAGLevel.AA),
            Triple("2.4.7", "Focus Visible", WCAGLevel.AA),
            Triple("2.4.11", "Focus Not Obscured (Minimum)", WCAGLevel.AA),
            Triple("2.5.1", "Pointer Gestures", WCAGLevel.A),
            Triple("2.5.2", "Pointer Cancellation", WCAGLevel.A),
            Triple("2.5.3", "Label in Name", WCAGLevel.A),
            Triple("2.5.4", "Motion Actuation", WCAGLevel.A),
            Triple("2.5.7", "Dragging Movements", WCAGLevel.AA),
            Triple("2.5.8", "Target Size (Minimum)", WCAGLevel.AA),
            // Principle 3 - Understandable
            Triple("3.1.1", "Language of Page", WCAGLevel.A),
            Triple("3.1.2", "Language of Parts", WCAGLevel.AA),
            Triple("3.2.1", "On Focus", WCAGLevel.A),
            Triple("3.2.2", "On Input", WCAGLevel.A),
            Triple("3.2.6", "Consistent Help", WCAGLevel.A),
            Triple("3.3.1", "Error Identification", WCAGLevel.A),
            Triple("3.3.2", "Labels or Instructions", WCAGLevel.A),
            Triple("3.3.3", "Error Suggestion", WCAGLevel.AA),
            Triple("3.3.4", "Error Prevention (Legal, Financial, Data)", WCAGLevel.AA),
            Triple("3.3.7", "Redundant Entry", WCAGLevel.A),
            Triple("3.3.8", "Accessible Authentication (Minimum)", WCAGLevel.AA),
            // Principle 4 - Robust
            Triple("4.1.2", "Name, Role, Value", WCAGLevel.A),
            Triple("4.1.3", "Status Messages", WCAGLevel.AA),
        )

        private const val ERROR_PENALTY = 5.0
        private const val WARNING_PENALTY = 2.0
        private const val INFO_PENALTY = 0.5

        /// Most credit a criterion under review can lose to warning volume.
        /// A reviewed criterion is never worth less than half a passing one.
        private const val REVIEW_MAX_DISCOUNT = 0.5

        /// Warning count at which a reviewed criterion has lost half of
        /// REVIEW_MAX_DISCOUNT. Chosen so a handful of warnings costs a little
        /// and thousands cost no more than the cap.
        private const val REVIEW_HALF_LIFE = 10.0

        /// Credit a single checked criterion contributes to its principle, in 0.0...1.0.
        ///
        /// Warnings scale the discount through a saturating curve rather than being
        /// subtracted from the principle percentage directly, so warning volume can
        /// never drive a principle to zero on its own.
        internal fun criterionCredit(status: CriterionStatus, warnings: Int): Double = when (status) {
            CriterionStatus.FAIL -> 0.0
            CriterionStatus.NOT_CHECKED -> 0.0
            CriterionStatus.PASS -> 1.0
            CriterionStatus.REVIEW -> {
                val w = warnings.toDouble()
                1.0 - REVIEW_MAX_DISCOUNT * (w / (w + REVIEW_HALF_LIFE))
            }
        }

        private fun impactMultiplier(impact: A11yImpact): Double = when (impact) {
            A11yImpact.CRITICAL -> 2.0
            A11yImpact.SERIOUS -> 1.5
            A11yImpact.MODERATE -> 1.0
            A11yImpact.MINOR -> 0.5
        }

        fun computeFileScore(errors: Int, warnings: Int, info: Int): Double {
            val penalty = errors * ERROR_PENALTY + warnings * WARNING_PENALTY + info * INFO_PENALTY
            return max(0.0, min(100.0, 100.0 - penalty))
        }
    }

    fun calculate(
        diagnostics: List<A11yDiagnostic>,
        rules: List<A11yRule>,
        filePaths: List<String>
    ): A11yScore {
        val totalErrors = diagnostics.count { it.severity == A11ySeverity.ERROR }
        val totalWarnings = diagnostics.count { it.severity == A11ySeverity.WARNING }
        val totalInfo = diagnostics.count { it.severity == A11ySeverity.INFO }

        val diagsByCriterion = mutableMapOf<String, MutableList<A11yDiagnostic>>()
        for (diag in diagnostics) {
            for (c in diag.wcagCriteria) {
                diagsByCriterion.getOrPut(c) { mutableListOf() }.add(diag)
            }
        }

        val rulesByCriterion = mutableMapOf<String, MutableList<String>>()
        for (rule in rules) {
            for (c in rule.wcagCriteria) {
                rulesByCriterion.getOrPut(c) { mutableListOf() }.add(rule.id)
            }
        }

        val checkedCriteria = rules.flatMap { it.wcagCriteria }.toSet()

        var passCount = 0
        var failCount = 0
        var notCheckedCount = 0
        val criteriaScores = mutableListOf<CriterionScore>()

        for ((criterion, name, level) in wcagCatalog) {
            val isChecked = criterion in checkedCriteria
            val diags = diagsByCriterion[criterion] ?: emptyList()
            val errors = diags.count { it.severity == A11ySeverity.ERROR }
            val warnings = diags.count { it.severity == A11ySeverity.WARNING }
            val infos = diags.count { it.severity == A11ySeverity.INFO }

            val status: CriterionStatus
            if (!isChecked) {
                status = CriterionStatus.NOT_CHECKED
                notCheckedCount++
            } else if (errors > 0) {
                status = CriterionStatus.FAIL
                failCount++
            } else if (warnings > 0 || infos > 0) {
                status = CriterionStatus.REVIEW
                passCount++
            } else {
                status = CriterionStatus.PASS
                passCount++
            }

            criteriaScores.add(
                CriterionScore(
                    criterion = criterion,
                    name = name,
                    principle = WCAGPrinciple.from(criterion),
                    level = level,
                    status = status,
                    errorCount = errors,
                    warningCount = warnings,
                    infoCount = infos,
                    ruleIDs = rulesByCriterion[criterion] ?: emptyList()
                )
            )
        }

        // Principle-level scores.
        //
        // Only principles with at least one checked criterion get an entry. A
        // principle no rule covers is absent rather than 100.0, so missing rule
        // coverage cannot inflate the overall score.
        val principleScores = mutableMapOf<WCAGPrinciple, Double>()
        for (principle in WCAGPrinciple.entries) {
            val critForPrinciple = criteriaScores.filter {
                it.principle == principle && it.status != CriterionStatus.NOT_CHECKED
            }
            if (critForPrinciple.isEmpty()) continue
            val credit = critForPrinciple.sumOf { criterionCredit(it.status, it.warningCount) }
            principleScores[principle] = (credit / critForPrinciple.size) * 100.0
        }

        // Per-file scores
        val diagsByFile = diagnostics.groupBy { it.filePath }
        val fileScores = filePaths.sorted().map { path ->
            val fileDiags = diagsByFile[path] ?: emptyList()
            val fe = fileDiags.count { it.severity == A11ySeverity.ERROR }
            val fw = fileDiags.count { it.severity == A11ySeverity.WARNING }
            val fi = fileDiags.count { it.severity == A11ySeverity.INFO }
            FileScore(path, computeFileScore(fe, fw, fi), fe, fw, fi)
        }

        // Overall score. principleScores holds only covered principles, so this
        // averages what was actually checked. An empty map means no rule covered
        // any catalogued criterion, in which case there is nothing to report against.
        val principleAvg = if (principleScores.isEmpty()) 100.0
        else principleScores.values.sum() / principleScores.size

        val issuePenalty = diagnostics.sumOf { diag ->
            val basePenalty = when (diag.severity) {
                A11ySeverity.ERROR -> ERROR_PENALTY
                A11ySeverity.WARNING -> WARNING_PENALTY
                A11ySeverity.INFO -> INFO_PENALTY
            }
            basePenalty * impactMultiplier(diag.impact)
        }
        val filesCount = max(1, filePaths.size)
        val normalizedPenalty = min(100.0, issuePenalty / filesCount * 2.0)
        val issueScore = max(0.0, 100.0 - normalizedPenalty)

        val overallScore = min(100.0, max(0.0, principleAvg * 0.5 + issueScore * 0.5))
        val rounded = (overallScore * 10).roundToInt() / 10.0
        val grade = A11yScore.letterGrade(rounded)

        return A11yScore(
            score = rounded,
            grade = grade,
            criteriaScores = criteriaScores,
            principleScores = principleScores,
            fileScores = fileScores,
            totalErrors = totalErrors,
            totalWarnings = totalWarnings,
            totalInfo = totalInfo,
            filesAnalyzed = filePaths.size,
            criteriaPassed = passCount,
            criteriaFailed = failCount,
            criteriaNotChecked = notCheckedCount
        )
    }
}
