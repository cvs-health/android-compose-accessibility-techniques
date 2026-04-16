package com.cvshealth.a11y.agent.core

import java.io.File

class AutoFixer {

    data class FileResult(
        val filePath: String,
        val fixesApplied: Int,
        val fixDescriptions: List<String>
    )

    data class Result(
        val fileResults: List<FileResult>,
        val totalFixed: Int,
        val totalFiles: Int
    )

    fun applyFixes(diagnostics: List<A11yDiagnostic>, dryRun: Boolean = false): Result {
        val withFixes = diagnostics.filter { it.fix != null }
        val byFile = withFixes.groupBy { it.filePath }
        val fileResults = mutableListOf<FileResult>()

        for ((filePath, fileDiags) in byFile) {
            // Sort fixes by startOffset descending so applying end-to-start preserves earlier offsets
            val sortedFixes = fileDiags.mapNotNull { it.fix }.sortedByDescending { it.startOffset }

            // Deduplicate overlapping ranges
            val appliedRanges = mutableListOf<IntRange>()
            val fixesToApply = mutableListOf<A11yFix>()

            for (fix in sortedFixes) {
                val range = fix.startOffset until fix.endOffset
                val overlaps = appliedRanges.any { existing ->
                    range.first < existing.last && range.last > existing.first
                }
                if (!overlaps) {
                    appliedRanges.add(range)
                    fixesToApply.add(fix)
                }
            }

            if (!dryRun && fixesToApply.isNotEmpty()) {
                val file = File(filePath)
                var source = file.readText()
                for (fix in fixesToApply) {
                    if (fix.startOffset >= 0 && fix.endOffset <= source.length && fix.startOffset <= fix.endOffset) {
                        source = source.replaceRange(fix.startOffset, fix.endOffset, fix.replacementText)
                    }
                }
                file.writeText(source)
            }

            fileResults.add(
                FileResult(
                    filePath = filePath,
                    fixesApplied = fixesToApply.size,
                    fixDescriptions = fixesToApply.map { it.description }
                )
            )
        }

        return Result(
            fileResults = fileResults,
            totalFixed = fileResults.sumOf { it.fixesApplied },
            totalFiles = fileResults.size
        )
    }

    fun formatResult(result: Result, dryRun: Boolean): String {
        if (result.totalFixed == 0) {
            return if (dryRun) "Dry run: no auto-fixable issues found."
            else "No auto-fixable issues found."
        }

        val verb = if (dryRun) "would fix" else "fixed"
        val suffix = if (dryRun) " (dry run — no files modified)" else ""

        return buildString {
            appendLine("Auto-fix: $verb ${result.totalFixed} issue(s) in ${result.totalFiles} file(s)$suffix")
            appendLine()
            for (fr in result.fileResults) {
                val shortPath = fr.filePath.substringAfterLast("/")
                appendLine("  $shortPath — ${fr.fixesApplied} fix(es)")
                for (desc in fr.fixDescriptions) {
                    val mark = if (dryRun) "~" else "\u2713"
                    appendLine("    $mark $desc")
                }
            }
        }
    }
}
