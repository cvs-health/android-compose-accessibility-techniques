package com.cvshealth.a11y.agent.core

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.Instant

@Serializable
data class Baseline(
    val entries: List<Entry>,
    val createdAt: String = Instant.now().toString(),
    val score: Double? = null
) {
    @Serializable
    data class Entry(
        val ruleID: String,
        val filePath: String,
        val line: Int,
        val message: String
    ) {
        val fingerprint: String get() = "$ruleID|$filePath|$message"
    }

    fun filterNew(diagnostics: List<A11yDiagnostic>): List<A11yDiagnostic> {
        val baselineFingerprints = entries.map { it.fingerprint }.toSet()
        return diagnostics.filter { diag ->
            val fp = "${diag.ruleID}|${diag.filePath}|${diag.message}"
            fp !in baselineFingerprints
        }
    }

    fun save(directory: String) {
        val file = File(directory, DEFAULT_FILE_NAME)
        file.writeText(json.encodeToString(this))
    }

    companion object {
        const val DEFAULT_FILE_NAME = ".a11y-baseline.json"

        private val json = Json {
            prettyPrint = true
            encodeDefaults = true
        }

        fun loadFrom(directory: String): Baseline? {
            val path = findFile(directory) ?: return null
            return loadAt(path)
        }

        fun loadAt(path: String): Baseline? {
            val file = File(path)
            if (!file.exists()) return null
            return try {
                json.decodeFromString<Baseline>(file.readText())
            } catch (_: Exception) {
                null
            }
        }

        fun from(diagnostics: List<A11yDiagnostic>, score: Double? = null): Baseline {
            val entries = diagnostics.map { diag ->
                Entry(
                    ruleID = diag.ruleID,
                    filePath = diag.filePath,
                    line = diag.line,
                    message = diag.message
                )
            }
            return Baseline(entries = entries, score = score)
        }

        private fun findFile(startingAt: String): String? {
            var dir = File(startingAt).canonicalFile
            while (true) {
                val candidate = File(dir, DEFAULT_FILE_NAME)
                if (candidate.exists()) return candidate.absolutePath
                val parent = dir.parentFile ?: break
                if (parent == dir) break
                dir = parent
            }
            return null
        }
    }
}
