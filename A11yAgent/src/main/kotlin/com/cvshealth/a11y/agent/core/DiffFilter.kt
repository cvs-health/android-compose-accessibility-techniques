package com.cvshealth.a11y.agent.core

import java.io.File

object DiffFilter {

    typealias ChangedLineMap = Map<String, Set<Int>>

    fun changedLines(directory: String, baseBranch: String? = null): ChangedLineMap {
        val args = mutableListOf("diff", "--unified=0", "--no-color")
        if (baseBranch != null) args.add(baseBranch)
        args.addAll(listOf("--", "*.kt"))

        val output = runGit(args, directory) ?: return emptyMap()
        return parseUnifiedDiff(output, directory)
    }

    fun filter(diagnostics: List<A11yDiagnostic>, changedLines: ChangedLineMap): List<A11yDiagnostic> {
        if (changedLines.isEmpty()) return emptyList()
        return diagnostics.filter { diag ->
            changedLines[diag.filePath]?.contains(diag.line) == true
        }
    }

    private fun runGit(args: List<String>, directory: String): String? {
        return try {
            val process = ProcessBuilder(listOf("git") + args)
                .directory(File(directory))
                .redirectErrorStream(false)
                .start()
            val output = process.inputStream.bufferedReader().readText()
            process.waitFor()
            output
        } catch (_: Exception) {
            null
        }
    }

    internal fun parseUnifiedDiff(diff: String, workingDirectory: String): ChangedLineMap {
        val result = mutableMapOf<String, MutableSet<Int>>()
        var currentFile: String? = null

        for (line in diff.lines()) {
            if (line.startsWith("+++ b/")) {
                val relativePath = line.removePrefix("+++ b/")
                currentFile = File(workingDirectory, relativePath).absolutePath
                result.getOrPut(currentFile) { mutableSetOf() }
                continue
            }

            if (line.startsWith("@@") && currentFile != null) {
                val range = parseHunkHeader(line)
                if (range != null) {
                    result.getOrPut(currentFile) { mutableSetOf() }.addAll(range)
                }
            }
        }
        return result
    }

    internal fun parseHunkHeader(line: String): IntRange? {
        val plusIdx = line.indexOf('+', startIndex = 2)
        if (plusIdx < 0) return null

        val afterPlus = line.substring(plusIdx + 1)
        val commaIdx = afterPlus.indexOf(',')

        val start: Int
        val count: Int

        if (commaIdx >= 0) {
            start = afterPlus.substring(0, commaIdx).toIntOrNull() ?: return null
            val endIdx = afterPlus.indexOfFirst { it == ' ' || it == '@' }.let {
                if (it < 0) afterPlus.length else it
            }
            count = afterPlus.substring(commaIdx + 1, endIdx).toIntOrNull() ?: return null
        } else {
            val endIdx = afterPlus.indexOfFirst { it == ' ' || it == '@' }.let {
                if (it < 0) afterPlus.length else it
            }
            start = afterPlus.substring(0, endIdx).toIntOrNull() ?: return null
            count = 1
        }

        if (count <= 0) return null
        return start..(start + count - 1)
    }
}
