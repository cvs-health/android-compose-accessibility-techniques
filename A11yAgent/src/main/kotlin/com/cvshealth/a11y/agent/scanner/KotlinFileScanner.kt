package com.cvshealth.a11y.agent.scanner

object KotlinFileScanner {

    private val RECOGNIZED_CALLS = setOf(
        "Icon", "Image", "Text", "Button", "IconButton", "TextButton", "OutlinedButton",
        "FilledTonalButton", "ElevatedButton", "FloatingActionButton", "ExtendedFloatingActionButton",
        "TextField", "OutlinedTextField", "BasicTextField",
        "Checkbox", "TriStateCheckbox", "Switch", "RadioButton",
        "Slider", "RangeSlider",
        "Scaffold", "TopAppBar", "CenterAlignedTopAppBar", "MediumTopAppBar", "LargeTopAppBar",
        "TabRow", "ScrollableTabRow", "Tab", "LeadingIconTab",
        "NavigationBar", "NavigationBarItem", "NavigationRail", "NavigationRailItem",
        "BottomSheetScaffold", "ModalBottomSheet",
        "Column", "Row", "Box", "LazyColumn", "LazyRow", "LazyVerticalGrid",
        "Card", "ElevatedCard", "OutlinedCard",
        "AlertDialog", "Dialog",
        "DropdownMenu", "DropdownMenuItem", "ExposedDropdownMenuBox",
        "AnimatedVisibility", "AnimatedContent", "Crossfade",
        "ClickableText", "SelectionContainer",
        "Surface", "ListItem"
    )

    private val COMPOSABLE_ANNOTATION = Regex("""@Composable""")
    private val FUNCTION_DECL = Regex("""^\s*(private\s+|internal\s+|public\s+)?(suspend\s+)?fun\s+(\w+)\s*\(""")

    fun scan(sourceText: String, filePath: String): ParsedKotlinFile {
        val lines = sourceText.lines()
        val composables = detectComposables(lines)
        val allCalls = detectCalls(lines, composables)
        return ParsedKotlinFile(filePath, composables, allCalls)
    }

    private fun detectComposables(lines: List<String>): List<ComposableFunction> {
        val composables = mutableListOf<ComposableFunction>()
        var i = 0
        while (i < lines.size) {
            val line = lines[i]
            if (COMPOSABLE_ANNOTATION.containsMatchIn(line)) {
                // Look ahead for function declaration (may be on same line or next few lines)
                for (j in i..minOf(i + 3, lines.size - 1)) {
                    val funcMatch = FUNCTION_DECL.find(lines[j])
                    if (funcMatch != null) {
                        val funcName = funcMatch.groupValues[3]
                        val openBraceLine = findOpenBrace(lines, j)
                        if (openBraceLine != null) {
                            val closeBraceLine = findMatchingCloseBrace(lines, openBraceLine)
                            if (closeBraceLine != null) {
                                val bodyText = lines.subList(openBraceLine, closeBraceLine + 1)
                                    .joinToString("\n")
                                composables.add(
                                    ComposableFunction(
                                        name = funcName,
                                        startLine = j + 1, // 1-based
                                        endLine = closeBraceLine + 1, // 1-based
                                        bodyText = bodyText
                                    )
                                )
                            }
                        }
                        i = j + 1
                        break
                    }
                    if (j == minOf(i + 3, lines.size - 1)) {
                        i = j + 1
                    }
                }
            } else {
                i++
            }
        }
        return composables
    }

    private fun findOpenBrace(lines: List<String>, fromLine: Int): Int? {
        for (i in fromLine..minOf(fromLine + 10, lines.size - 1)) {
            if (lines[i].contains("{")) return i
        }
        return null
    }

    private fun findMatchingCloseBrace(lines: List<String>, openBraceLine: Int): Int? {
        var depth = 0
        for (i in openBraceLine until lines.size) {
            val line = lines[i]
            for (ch in line) {
                when (ch) {
                    '{' -> depth++
                    '}' -> {
                        depth--
                        if (depth == 0) return i
                    }
                }
            }
        }
        return null
    }

    private fun detectCalls(
        lines: List<String>,
        composables: List<ComposableFunction>
    ): List<DetectedCall> {
        val calls = mutableListOf<DetectedCall>()

        for (composable in composables) {
            val startIdx = composable.startLine - 1 // to 0-based
            val endIdx = composable.endLine - 1

            for (i in startIdx..endIdx) {
                val line = lines[i]
                for (callName in RECOGNIZED_CALLS) {
                    val pattern = Regex("""(?<!\w)$callName\s*\(""")
                    val match = pattern.find(line)
                    if (match != null) {
                        val column = match.range.first + 1 // 1-based
                        val rawArgs = extractBalancedBlock(lines, i, match.range.first, '(', ')')
                        val modifierChain = extractModifierChain(lines, i, rawArgs)
                        val arguments = parseArguments(rawArgs)
                        val enclosingCall = findEnclosingCall(lines, i, startIdx)
                        val enclosingScopeText = extractEnclosingScopeText(lines, i, startIdx, endIdx)

                        calls.add(
                            DetectedCall(
                                name = callName,
                                line = i + 1, // 1-based
                                column = column,
                                arguments = arguments,
                                rawArgumentText = rawArgs,
                                modifierChain = modifierChain,
                                parentComposable = composable.name,
                                enclosingCallName = enclosingCall,
                                enclosingScopeText = enclosingScopeText,
                                depth = calculateDepth(lines, i, startIdx)
                            )
                        )
                    }
                }
            }
        }
        return calls
    }

    fun extractBalancedBlock(
        lines: List<String>,
        startLine: Int,
        startCol: Int,
        openChar: Char,
        closeChar: Char
    ): String {
        val sb = StringBuilder()
        var depth = 0
        var started = false
        var inString = false
        var escaped = false

        for (i in startLine until lines.size) {
            val line = lines[i]
            val startJ = if (i == startLine) startCol else 0

            for (j in startJ until line.length) {
                val ch = line[j]

                if (escaped) {
                    sb.append(ch)
                    escaped = false
                    continue
                }
                if (ch == '\\' && inString) {
                    sb.append(ch)
                    escaped = true
                    continue
                }
                if (ch == '"' && !escaped) {
                    inString = !inString
                    sb.append(ch)
                    continue
                }

                if (!inString) {
                    if (ch == openChar) {
                        if (!started) started = true
                        depth++
                    } else if (ch == closeChar) {
                        depth--
                        if (started && depth == 0) {
                            sb.append(ch)
                            return sb.toString()
                        }
                    }
                }
                if (started) sb.append(ch)
            }
            if (started) sb.append('\n')
        }
        return sb.toString()
    }

    private fun extractModifierChain(lines: List<String>, callLine: Int, rawArgs: String): String {
        // Check for modifier = ... in arguments
        val modifierPattern = Regex("""modifier\s*=\s*""")
        val modMatch = modifierPattern.find(rawArgs)
        if (modMatch != null) {
            // Extract the modifier expression from the raw arguments
            val afterModifier = rawArgs.substring(modMatch.range.last + 1).trim()
            return extractModifierExpression(afterModifier)
        }

        // Also look for modifier chain applied after the call (trailing dot syntax)
        // e.g., Text("hello").semantics { heading() }
        val fullCallText = extractBalancedBlock(lines, callLine, lines[callLine].indexOf(lines[callLine].trimStart().first()), '(', ')')
        val endOfCall = rawArgs.length
        if (callLine < lines.size) {
            val remaining = lines.getOrNull(callLine)?.substring(
                minOf(lines[callLine].length, lines[callLine].indexOf(rawArgs.take(10)) + endOfCall)
            ) ?: ""
            if (remaining.trimStart().startsWith(".")) {
                return remaining.trim()
            }
        }

        return ""
    }

    private fun extractModifierExpression(text: String): String {
        var depth = 0
        val sb = StringBuilder()
        var inString = false

        for (ch in text) {
            if (ch == '"') inString = !inString
            if (!inString) {
                when (ch) {
                    '(', '{' -> depth++
                    ')', '}' -> {
                        depth--
                        if (depth < 0) return sb.toString().trim()
                    }
                    ',' -> if (depth == 0) return sb.toString().trim()
                }
            }
            sb.append(ch)
        }
        return sb.toString().trim()
    }

    fun parseArguments(rawArgs: String): Map<String, String> {
        val args = mutableMapOf<String, String>()
        if (rawArgs.isBlank()) return args

        // Remove outer parens
        val inner = if (rawArgs.startsWith("(") && rawArgs.endsWith(")")) {
            rawArgs.substring(1, rawArgs.length - 1)
        } else {
            rawArgs
        }

        val argParts = splitTopLevelCommas(inner)
        var positionalIndex = 0

        for (part in argParts) {
            val trimmed = part.trim()
            if (trimmed.isEmpty()) continue

            val equalsIndex = findTopLevelEquals(trimmed)
            if (equalsIndex != null) {
                val key = trimmed.substring(0, equalsIndex).trim()
                val value = trimmed.substring(equalsIndex + 1).trim()
                args[key] = value
            } else {
                // Positional argument
                args["__pos_$positionalIndex"] = trimmed
                positionalIndex++
            }
        }
        return args
    }

    private fun splitTopLevelCommas(text: String): List<String> {
        val parts = mutableListOf<String>()
        val sb = StringBuilder()
        var depth = 0
        var inString = false
        var escaped = false

        for (ch in text) {
            if (escaped) { sb.append(ch); escaped = false; continue }
            if (ch == '\\' && inString) { sb.append(ch); escaped = true; continue }
            if (ch == '"') { inString = !inString; sb.append(ch); continue }
            if (!inString) {
                when (ch) {
                    '(', '{', '[', '<' -> depth++
                    ')', '}', ']', '>' -> depth--
                    ',' -> if (depth == 0) { parts.add(sb.toString()); sb.clear(); continue }
                }
            }
            sb.append(ch)
        }
        if (sb.isNotEmpty()) parts.add(sb.toString())
        return parts
    }

    private fun findTopLevelEquals(text: String): Int? {
        var depth = 0
        var inString = false
        for (i in text.indices) {
            val ch = text[i]
            if (ch == '"') inString = !inString
            if (!inString) {
                when (ch) {
                    '(', '{', '[' -> depth++
                    ')', '}', ']' -> depth--
                    '=' -> if (depth == 0 && i + 1 < text.length && text[i + 1] != '=') return i
                }
            }
        }
        return null
    }

    private fun findEnclosingCall(lines: List<String>, currentLine: Int, scopeStart: Int): String? {
        // Walk backwards to find the nearest enclosing recognized call
        var depth = 0
        for (i in currentLine - 1 downTo scopeStart) {
            val line = lines[i]
            for (j in line.length - 1 downTo 0) {
                when (line[j]) {
                    '}', ')' -> depth++
                    '{', '(' -> {
                        depth--
                        if (depth < 0) {
                            // We're inside this block - check if it's a recognized call
                            for (callName in RECOGNIZED_CALLS) {
                                val pattern = Regex("""(?<!\w)$callName\s*\(""")
                                if (pattern.containsMatchIn(line)) return callName
                            }
                            // Check line above for call name (lambda on next line)
                            if (i > scopeStart) {
                                for (callName in RECOGNIZED_CALLS) {
                                    val pattern = Regex("""(?<!\w)$callName\s*\(""")
                                    if (pattern.containsMatchIn(lines[i - 1])) return callName
                                }
                            }
                            return null
                        }
                    }
                }
            }
        }
        return null
    }

    private fun extractEnclosingScopeText(
        lines: List<String>,
        currentLine: Int,
        scopeStart: Int,
        scopeEnd: Int
    ): String {
        // Get a window of context around the call (up to 20 lines before, 10 after)
        val start = maxOf(scopeStart, currentLine - 20)
        val end = minOf(scopeEnd, currentLine + 10)
        return lines.subList(start, end + 1).joinToString("\n")
    }

    private fun calculateDepth(lines: List<String>, currentLine: Int, scopeStart: Int): Int {
        var depth = 0
        for (i in scopeStart until currentLine) {
            for (ch in lines[i]) {
                when (ch) {
                    '{' -> depth++
                    '}' -> depth--
                }
            }
        }
        return depth
    }
}
