package com.cvshealth.a11y.agent.rules

import com.cvshealth.a11y.agent.core.*
import com.cvshealth.a11y.agent.scanner.ParsedKotlinFile

class HardcodedColorRule : A11yRule {
    override val id = "hardcoded-color"
    override val name = "Hardcoded Color"
    override val severity = A11ySeverity.WARNING
    override val impact = A11yImpact.MODERATE
    override val wcagCriteria = listOf("1.4.3")
    override val description = "Avoid hardcoded colors — use MaterialTheme.colorScheme for dark mode and contrast support"

    private val hardcodedColors = setOf(
        "Color.Black", "Color.White", "Color.Red", "Color.Green", "Color.Blue",
        "Color.Yellow", "Color.Cyan", "Color.Magenta", "Color.Gray",
        "Color.LightGray", "Color.DarkGray", "Color.Transparent"
    )
    private val hexColorPattern = Regex("""Color\s*\(\s*0x[0-9A-Fa-f]+\s*\)""")

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()

        for ((index, line) in context.sourceLines.withIndex()) {
            val trimmed = line.trim()
            if (trimmed.startsWith("//") || trimmed.startsWith("*")) continue
            // Skip color definitions in theme files
            if (trimmed.contains("= Color(") && (trimmed.contains("val ") || trimmed.contains("private "))) continue

            for (color in hardcodedColors) {
                if (line.contains(color)) {
                    diagnostics.add(
                        makeDiagnostic(
                            message = "Hardcoded color $color won't adapt to dark mode or high-contrast themes.",
                            line = index + 1,
                            column = line.indexOf(color) + 1,
                            context = context,
                            suggestion = "Use MaterialTheme.colorScheme.onSurface (or appropriate semantic color) instead"
                        )
                    )
                    break
                }
            }

            val hexMatch = hexColorPattern.find(line)
            if (hexMatch != null && !trimmed.contains("val ") && !trimmed.contains("private ")) {
                diagnostics.add(
                    makeDiagnostic(
                        message = "Hardcoded hex color ${hexMatch.value} won't adapt to dark mode or high-contrast themes.",
                        line = index + 1,
                        column = hexMatch.range.first + 1,
                        context = context,
                        suggestion = "Use MaterialTheme.colorScheme tokens instead of hardcoded hex values"
                    )
                )
            }
        }
        return diagnostics
    }
}

object ColorUtils {
    private val namedColors = mapOf(
        "Color.Black" to Triple(0, 0, 0),
        "Color.DarkGray" to Triple(68, 68, 68),
        "Color.Gray" to Triple(136, 136, 136),
        "Color.LightGray" to Triple(192, 192, 192),
        "Color.White" to Triple(255, 255, 255),
        "Color.Red" to Triple(255, 0, 0),
        "Color.Green" to Triple(0, 255, 0),
        "Color.Blue" to Triple(0, 0, 255),
        "Color.Yellow" to Triple(255, 255, 0),
        "Color.Cyan" to Triple(0, 255, 255),
        "Color.Magenta" to Triple(255, 0, 255),
        "Color.Transparent" to Triple(0, 0, 0),
        "Color.Unspecified" to null
    )

    private val hexPattern = Regex("""Color\s*\(\s*0x([0-9A-Fa-f]{6,8})\s*\)""")

    fun parseColor(expr: String): Triple<Int, Int, Int>? {
        val trimmed = expr.trim()

        // Named color
        for ((name, rgb) in namedColors) {
            if (trimmed.contains(name)) return rgb
        }

        // Hex color: Color(0xAARRGGBB) or Color(0xRRGGBB)
        val hexMatch = hexPattern.find(trimmed)
        if (hexMatch != null) {
            val hex = hexMatch.groupValues[1]
            val argb = hex.toLongOrNull(16) ?: return null
            return if (hex.length == 8) {
                Triple(((argb shr 16) and 0xFF).toInt(), ((argb shr 8) and 0xFF).toInt(), (argb and 0xFF).toInt())
            } else {
                Triple(((argb shr 16) and 0xFF).toInt(), ((argb shr 8) and 0xFF).toInt(), (argb and 0xFF).toInt())
            }
        }
        return null
    }

    private fun linearize(channel: Int): Double {
        val sRGB = channel / 255.0
        return if (sRGB <= 0.04045) sRGB / 12.92 else Math.pow((sRGB + 0.055) / 1.055, 2.4)
    }

    fun relativeLuminance(r: Int, g: Int, b: Int): Double {
        return 0.2126 * linearize(r) + 0.7152 * linearize(g) + 0.0722 * linearize(b)
    }

    fun contrastRatio(fg: Triple<Int, Int, Int>, bg: Triple<Int, Int, Int>): Double {
        val l1 = relativeLuminance(fg.first, fg.second, fg.third)
        val l2 = relativeLuminance(bg.first, bg.second, bg.third)
        val lighter = maxOf(l1, l2)
        val darker = minOf(l1, l2)
        return (lighter + 0.05) / (darker + 0.05)
    }
}

class ColorContrastRule : A11yRule {
    override val id = "color-contrast"
    override val name = "Color Contrast"
    override val severity = A11ySeverity.WARNING
    override val impact = A11yImpact.SERIOUS
    override val wcagCriteria = listOf("1.4.3")
    override val description = "Hardcoded foreground/background color pairs should meet WCAG contrast requirements"

    private val colorArgPattern = Regex("""color\s*=\s*(.+?)(?:\s*[,)]|$)""")
    private val backgroundPattern = Regex("""\.background\s*\(\s*(?:color\s*=\s*)?(.+?)\s*\)""")

    override fun check(file: ParsedKotlinFile, context: RuleContext): List<A11yDiagnostic> {
        val diagnostics = mutableListOf<A11yDiagnostic>()
        val minRatio = context.configOptions.contrastRatio

        for (call in file.allCalls.filter { it.name == "Text" }) {
            val colorArg = call.getArgument("color") ?: continue
            val fgColor = ColorUtils.parseColor(colorArg) ?: continue

            // Search enclosing scope for background color
            val bgMatch = backgroundPattern.find(call.enclosingScopeText ?: "") ?: continue
            val bgExpr = bgMatch.groupValues[1]
            val bgColor = ColorUtils.parseColor(bgExpr) ?: continue

            val ratio = ColorUtils.contrastRatio(fgColor, bgColor)
            if (ratio < minRatio) {
                diagnostics.add(
                    makeDiagnostic(
                        message = "Color contrast ratio %.2f:1 is below the minimum %.1f:1 (WCAG 1.4.3). Foreground: %s, Background: %s".format(
                            ratio, minRatio, colorArg.trim(), bgExpr.trim()
                        ),
                        line = call.line,
                        column = call.column,
                        context = context,
                        suggestion = "Use colors with a contrast ratio of at least %.1f:1, or use MaterialTheme.colorScheme tokens".format(minRatio)
                    )
                )
            }
        }

        // Also check hardcoded color pairs in nearby lines (fallback heuristic)
        val colorLinePattern = Regex("""(Color\.\w+|Color\s*\(\s*0x[0-9A-Fa-f]+\s*\))""")
        for ((index, line) in context.sourceLines.withIndex()) {
            val trimmed = line.trim()
            if (trimmed.startsWith("//") || trimmed.startsWith("*")) continue

            val matches = colorLinePattern.findAll(line).toList()
            if (matches.isEmpty()) continue

            // Check surrounding lines for a second color
            val start = maxOf(0, index - 5)
            val end = minOf(context.sourceLines.size - 1, index + 5)
            val nearby = context.sourceLines.subList(start, end + 1).joinToString("\n")

            for (match in matches) {
                val fg = ColorUtils.parseColor(match.value) ?: continue
                val nearbyColors = colorLinePattern.findAll(nearby).toList()
                for (nearbyMatch in nearbyColors) {
                    if (nearbyMatch.value == match.value) continue
                    val bg = ColorUtils.parseColor(nearbyMatch.value) ?: continue
                    val ratio = ColorUtils.contrastRatio(fg, bg)
                    if (ratio < minRatio) {
                        // Deduplicate — only report from Text call checks or unique line
                        val alreadyReported = diagnostics.any { it.line == index + 1 && it.ruleID == id }
                        if (!alreadyReported) {
                            diagnostics.add(
                                makeDiagnostic(
                                    message = "Potential low contrast %.2f:1 between %s and %s (minimum %.1f:1).".format(
                                        ratio, match.value, nearbyMatch.value, minRatio
                                    ),
                                    line = index + 1,
                                    column = match.range.first + 1,
                                    context = context,
                                    suggestion = "Use MaterialTheme.colorScheme colors which are designed to meet contrast requirements"
                                )
                            )
                            break
                        }
                    }
                }
            }
        }
        return diagnostics
    }
}
