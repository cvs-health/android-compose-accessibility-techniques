package com.cvshealth.a11y.agent.core

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class A11yConfig(
    val severity_overrides: Map<String, String> = emptyMap(),
    val disabled_rules: List<String> = emptyList(),
    val enabled_only: List<String> = emptyList(),
    val options: YamlConfigOptions = YamlConfigOptions(),
    val exclude_paths: List<String> = emptyList()
) {
    @Serializable
    data class YamlConfigOptions(
        val min_touch_target: Int? = null,
        val contrast_ratio: Double? = null
    )

    val severityOverrides: Map<String, A11ySeverity>
        get() = severity_overrides.mapNotNull { (k, v) ->
            val severity = try { A11ySeverity.valueOf(v.uppercase()) } catch (_: Exception) { null }
            if (severity != null) k to severity else null
        }.toMap()

    val disabledRules: Set<String> get() = disabled_rules.toSet()
    val enabledOnly: Set<String> get() = enabled_only.toSet()
    val configOptions: ConfigOptions
        get() = ConfigOptions(
            minTouchTarget = options.min_touch_target ?: 48,
            contrastRatio = options.contrast_ratio ?: 4.5
        )

    fun shouldExclude(relativePath: String): Boolean =
        exclude_paths.any { pattern -> fnmatchGlob(pattern, relativePath) }

    companion object {
        val empty = A11yConfig()
    }
}

object ConfigLoader {
    const val FILE_NAME = ".a11ycheck.yml"

    fun load(directory: String): A11yConfig {
        val path = findConfigFile(directory) ?: return A11yConfig.empty
        return loadAt(path)
    }

    fun loadAt(path: String): A11yConfig {
        val contents = File(path).readText()
        return parse(contents)
    }

    fun parse(yaml: String): A11yConfig {
        return try {
            Yaml.default.decodeFromString(A11yConfig.serializer(), yaml)
        } catch (_: Exception) {
            A11yConfig.empty
        }
    }

    private fun findConfigFile(startingAt: String): String? {
        var dir = File(startingAt).canonicalFile
        while (true) {
            val candidate = File(dir, FILE_NAME)
            if (candidate.exists()) return candidate.absolutePath
            val parent = dir.parentFile ?: break
            if (parent == dir) break
            dir = parent
        }
        return null
    }
}

private fun fnmatchGlob(pattern: String, path: String): Boolean {
    if (!pattern.contains("/")) {
        return path.split("/").any { matchWildcard(pattern, it) }
    }
    val patternParts = pattern.split("/")
    val pathParts = path.split("/")
    return matchParts(patternParts, 0, pathParts, 0)
}

private fun matchParts(pattern: List<String>, pi: Int, path: List<String>, pa: Int): Boolean {
    if (pi >= pattern.size) return pa >= path.size

    if (pattern[pi] == "**") {
        for (skip in 0..(path.size - pa)) {
            if (matchParts(pattern, pi + 1, path, pa + skip)) return true
        }
        return false
    }

    if (pa >= path.size) return false
    if (!matchWildcard(pattern[pi], path[pa])) return false
    return matchParts(pattern, pi + 1, path, pa + 1)
}

private fun matchWildcard(pattern: String, string: String): Boolean {
    if (pattern == "*") return true

    val p = pattern.toCharArray()
    val s = string.toCharArray()
    var pi = 0; var si = 0
    var starP = -1; var starS = -1

    while (si < s.size) {
        if (pi < p.size && (p[pi] == s[si] || p[pi] == '?')) {
            pi++; si++
        } else if (pi < p.size && p[pi] == '*') {
            starP = pi; starS = si; pi++
        } else if (starP >= 0) {
            pi = starP + 1; starS++; si = starS
        } else {
            return false
        }
    }
    while (pi < p.size && p[pi] == '*') pi++
    return pi == p.size
}
