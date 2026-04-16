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

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import org.junit.Test

/**
 * Tests for ComposeColorContrastDetector which scans raw source for color pairs.
 * We use TestMode.DEFAULT to avoid whitespace-transformation modes that alter
 * the source patterns the detector uses for color parsing.
 */
class ComposeColorContrastDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = ComposeColorContrastDetector()

    override fun allowCompilationErrors(): Boolean = true

    override fun getIssues(): List<Issue> = listOf(
        ComposeColorContrastDetector.COLOR_CONTRAST,
    )

    // ---- A11yColorContrast ----

    @Test
    fun testTextWithColorGrayOnColorWhiteTriggersA11ycolorcontrast() {
        // Color.Gray (136,136,136) on Color.White (255,255,255) has contrast ~3.95:1, below 4.5:1
        lint().files(
            kotlin(
                """
                package test
                @Composable
                fun Foo() {
                    Text(
                        text = "Low contrast text",
                        color = Color.Gray,
                        modifier = Modifier.background(Color.White)
                    )
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).allowCompilationErrors().run().expectContains("A11yColorContrast")
    }

    @Test
    fun testTextWithColorDarkgrayOnColorLightgrayTriggersA11ycolorcontrast() {
        // Color.DarkGray (68,68,68) on Color.LightGray (192,192,192) — actual contrast is ~5.35:1,
        // which is above the 4.5:1 threshold. The detector correctly does not flag this pair.
        lint().files(
            kotlin(
                """
                package test
                @Composable
                fun Foo() {
                    Text(
                        text = "Low contrast",
                        color = Color.DarkGray,
                        modifier = Modifier.background(Color.LightGray)
                    )
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testTextWithColorBlackOnColorWhiteIsClean() {
        // Color.Black (0,0,0) on Color.White (255,255,255) = 21:1 contrast ratio
        lint().files(
            kotlin(
                """
                package test
                @Composable
                fun Foo() {
                    Text(
                        text = "High contrast text",
                        color = Color.Black,
                        modifier = Modifier.background(Color.White)
                    )
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testTextWithColorWhiteOnColorBlackIsClean() {
        // Color.White (255,255,255) on Color.Black (0,0,0) = 21:1 contrast ratio
        lint().files(
            kotlin(
                """
                package test
                @Composable
                fun Foo() {
                    Text(
                        text = "White on black",
                        color = Color.White,
                        modifier = Modifier.background(Color.Black)
                    )
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testTextWithoutExplicitColorIsClean() {
        lint().files(
            kotlin(
                """
                package test
                @Composable
                fun Foo() {
                    Text(text = "Default color text")
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testTextWithMaterialthemeColorsIsClean() {
        lint().files(
            kotlin(
                """
                package test
                @Composable
                fun Foo() {
                    Text(
                        text = "Themed text",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).allowCompilationErrors().run().expectClean()
    }

    @Test
    fun testTextWithColorRedOnColorWhiteTriggersA11ycolorcontrast() {
        // Color.Red (255,0,0) on Color.White (255,255,255) = ~4.0:1 contrast, below 4.5:1
        lint().files(
            kotlin(
                """
                package test
                @Composable
                fun Foo() {
                    Text(
                        text = "Red text on white",
                        color = Color.Red,
                        modifier = Modifier.background(Color.White)
                    )
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).allowCompilationErrors().run().expectContains("A11yColorContrast")
    }

    @Test
    fun testTextWithColorLightgrayOnColorWhiteTriggersA11ycolorcontrast() {
        // Color.LightGray (192,192,192) on Color.White (255,255,255) — contrast ~1.74:1, below 4.5:1
        lint().files(
            kotlin(
                """
                package test
                @Composable
                fun Foo() {
                    Text(
                        text = "Light gray text",
                        color = Color.LightGray,
                        modifier = Modifier.background(Color.White)
                    )
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).allowCompilationErrors().run().expectContains("A11yColorContrast")
    }

    @Test
    fun testTextWithHexColorHighContrastIsClean() {
        // Color(0xFF212121) = RGB(33,33,33) on Color.White = ~16:1 contrast ratio
        lint().files(
            kotlin(
                """
                package test
                @Composable
                fun Foo() {
                    Text(
                        text = "Dark text on white",
                        color = Color(0xFF212121),
                        modifier = Modifier.background(Color.White)
                    )
                }
                """.trimIndent()
            )
        ).testModes(TestMode.DEFAULT).allowCompilationErrors().run().expectClean()
    }
}
