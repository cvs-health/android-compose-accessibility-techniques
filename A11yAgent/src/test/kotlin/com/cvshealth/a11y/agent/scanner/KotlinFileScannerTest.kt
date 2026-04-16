package com.cvshealth.a11y.agent.scanner

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

class KotlinFileScannerTest {

    @Test
    fun `detects composable functions`() {
        val source = """
            import androidx.compose.runtime.Composable

            @Composable
            fun MyScreen() {
                Text("Hello")
            }
        """.trimIndent()

        val result = KotlinFileScanner.scan(source, "test.kt")
        assertEquals(1, result.composables.size)
        assertEquals("MyScreen", result.composables[0].name)
    }

    @Test
    fun `detects multiple composable functions`() {
        val source = """
            import androidx.compose.runtime.Composable

            @Composable
            fun ScreenA() {
                Text("A")
            }

            @Composable
            fun ScreenB() {
                Text("B")
            }
        """.trimIndent()

        val result = KotlinFileScanner.scan(source, "test.kt")
        assertEquals(2, result.composables.size)
        assertEquals("ScreenA", result.composables[0].name)
        assertEquals("ScreenB", result.composables[1].name)
    }

    @Test
    fun `detects recognized calls within composables`() {
        val source = """
            @Composable
            fun MyScreen() {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null
                )
                Text("Hello World")
                Button(onClick = {}) {
                    Text("Click me")
                }
            }
        """.trimIndent()

        val result = KotlinFileScanner.scan(source, "test.kt")
        val callNames = result.allCalls.map { it.name }
        assertTrue("Icon" in callNames)
        assertTrue("Text" in callNames)
        assertTrue("Button" in callNames)
    }

    @Test
    fun `extracts named arguments`() {
        val source = """
            @Composable
            fun MyScreen() {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star icon"
                )
            }
        """.trimIndent()

        val result = KotlinFileScanner.scan(source, "test.kt")
        val icon = result.allCalls.first { it.name == "Icon" }
        assertEquals("Icons.Default.Star", icon.getArgument("imageVector"))
        assertEquals("\"Star icon\"", icon.getArgument("contentDescription"))
    }

    @Test
    fun `extracts modifier chain`() {
        val source = """
            @Composable
            fun MyScreen() {
                Text(
                    text = "Hello",
                    modifier = Modifier.semantics { heading() }.padding(8.dp)
                )
            }
        """.trimIndent()

        val result = KotlinFileScanner.scan(source, "test.kt")
        val text = result.allCalls.first { it.name == "Text" }
        assertTrue(text.modifierChain.contains("semantics"))
    }

    @Test
    fun `identifies enclosing call`() {
        val source = """
            @Composable
            fun MyScreen() {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null
                    )
                }
            }
        """.trimIndent()

        val result = KotlinFileScanner.scan(source, "test.kt")
        val icon = result.allCalls.first { it.name == "Icon" }
        assertEquals("IconButton", icon.enclosingCallName)
    }

    @Test
    fun `parseArguments handles positional args`() {
        val args = KotlinFileScanner.parseArguments("""(Icons.Default.Star, "hello")""")
        assertEquals("Icons.Default.Star", args["__pos_0"])
        assertEquals("\"hello\"", args["__pos_1"])
    }

    @Test
    fun `parseArguments handles mixed positional and named`() {
        val args = KotlinFileScanner.parseArguments("""(Icons.Default.Star, contentDescription = "star")""")
        assertEquals("Icons.Default.Star", args["__pos_0"])
        assertEquals("\"star\"", args["contentDescription"])
    }

    @Test
    fun `ignores non-composable functions`() {
        val source = """
            fun helperFunction() {
                val x = Icon("something")
            }

            @Composable
            fun MyScreen() {
                Text("Hello")
            }
        """.trimIndent()

        val result = KotlinFileScanner.scan(source, "test.kt")
        assertEquals(1, result.composables.size)
        assertEquals("MyScreen", result.composables[0].name)
        // Only calls within composables should be detected
        val textCalls = result.allCalls.filter { it.name == "Text" }
        assertEquals(1, textCalls.size)
    }

    @Test
    fun `handles nested braces correctly`() {
        val source = """
            @Composable
            fun MyScreen() {
                Column(modifier = Modifier) {
                    if (condition) {
                        Text("inside if")
                    }
                    Row(modifier = Modifier) {
                        Icon(Icons.Default.Star, contentDescription = null)
                    }
                }
            }
        """.trimIndent()

        val result = KotlinFileScanner.scan(source, "test.kt")
        assertEquals(1, result.composables.size)
        assertTrue(result.allCalls.any { it.name == "Column" })
        assertTrue(result.allCalls.any { it.name == "Row" })
        assertTrue(result.allCalls.any { it.name == "Icon" })
    }

    @Test
    fun `extractBalancedBlock handles nested parentheses`() {
        val lines = listOf(
            "Icon(",
            "    imageVector = Icons.Default.Star,",
            "    contentDescription = stringResource(R.string.desc)",
            ")"
        )
        val block = KotlinFileScanner.extractBalancedBlock(lines, 0, 4, '(', ')')
        assertTrue(block.contains("imageVector"))
        assertTrue(block.contains("contentDescription"))
        assertTrue(block.contains("stringResource"))
    }

    @Test
    fun `detects private composables`() {
        val source = """
            @Composable
            private fun InternalComponent() {
                Text("Internal")
            }
        """.trimIndent()

        val result = KotlinFileScanner.scan(source, "test.kt")
        assertEquals(1, result.composables.size)
        assertEquals("InternalComponent", result.composables[0].name)
    }
}
