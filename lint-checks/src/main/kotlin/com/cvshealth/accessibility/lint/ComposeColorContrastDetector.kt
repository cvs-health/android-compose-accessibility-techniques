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

package com.cvshealth.accessibility.lint

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Location
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.client.api.UElementHandler
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UFile
import kotlin.math.pow

class ComposeColorContrastDetector : Detector(), SourceCodeScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>> =
        listOf(UFile::class.java)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {
        override fun visitFile(node: UFile) {
            if (!context.file.name.endsWith(".kt")) return
            val fileName = context.file.name
            if (fileName in THEME_FILES) return

            val source = context.getContents()?.toString() ?: return
            checkColorContrast(context, source)
        }
    }

    private fun checkColorContrast(context: JavaContext, source: String) {
        // Find Text calls with explicit color arg near background colors
        val textPattern = Regex("""Text\s*\(""")
        for (match in textPattern.findAll(source)) {
            val lineStart = source.lastIndexOf('\n', match.range.first) + 1
            val lineText = source.substring(lineStart, source.indexOf('\n', match.range.first).let { if (it == -1) source.length else it })
            if (lineText.trimStart().startsWith("//") || lineText.trimStart().startsWith("*")) continue

            // Get the call text (approximate — next 500 chars)
            val callEnd = minOf(match.range.first + 500, source.length)
            val callText = source.substring(match.range.first, callEnd)

            // Look for color = ... argument. Use a pattern that handles Color(...) with nested parens.
            val colorArgMatch = Regex("""color\s*=\s*([^\n,)]*(?:\([^)]*\))?)""").find(callText) ?: continue
            val fgExpr = colorArgMatch.groupValues[1].trim()
            val fgColor = parseColor(fgExpr) ?: continue

            // Search surrounding context for background color
            val ctxStart = maxOf(0, match.range.first - 500)
            val ctxEnd = minOf(match.range.first + 500, source.length)
            val contextText = source.substring(ctxStart, ctxEnd)

            val bgMatch = Regex("""\.background\s*\(\s*(?:color\s*=\s*)?(.+?)\s*\)""").find(contextText) ?: continue
            val bgExpr = bgMatch.groupValues[1].trim()
            val bgColor = parseColor(bgExpr) ?: continue

            val ratio = contrastRatio(fgColor, bgColor)
            if (ratio < MIN_CONTRAST_RATIO) {
                context.report(
                    COLOR_CONTRAST,
                    Location.create(context.file, source, match.range.first, match.range.last + 1),
                    "Color contrast ratio %.2f:1 is below the minimum 4.5:1 (WCAG 1.4.3). Foreground: %s, Background: %s".format(
                        ratio, fgExpr, bgExpr
                    )
                )
            }
        }
    }

    companion object {
        private val THEME_FILES = setOf("Type.kt", "Theme.kt", "Color.kt", "Typography.kt")
        private const val MIN_CONTRAST_RATIO = 4.5

        private val NAMED_COLORS = mapOf(
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
        )

        private val HEX_PATTERN = Regex("""Color\s*\(\s*0x([0-9A-Fa-f]{6,8})\s*\)""")

        fun parseColor(expr: String): Triple<Int, Int, Int>? {
            for ((name, rgb) in NAMED_COLORS) {
                if (expr.contains(name)) return rgb
            }
            val hexMatch = HEX_PATTERN.find(expr)
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
            return if (sRGB <= 0.04045) sRGB / 12.92 else ((sRGB + 0.055) / 1.055).pow(2.4)
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

        val COLOR_CONTRAST = Issue.create(
            id = "A11yColorContrast",
            briefDescription = "Insufficient color contrast",
            explanation = """
                Hardcoded foreground/background color pairs should meet the WCAG minimum \
                contrast ratio of 4.5:1 for normal text (3.0:1 for large text). Use \
                MaterialTheme.colorScheme tokens for automatic contrast compliance.

                WCAG 1.4.3 Contrast (Minimum) (Level AA)
            """.trimIndent(),
            category = Category.A11Y,
            priority = 7,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposeColorContrastDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}
