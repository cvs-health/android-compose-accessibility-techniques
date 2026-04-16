package com.cvshealth.a11y.agent.scanner

data class ParsedKotlinFile(
    val filePath: String,
    val composables: List<ComposableFunction>,
    val allCalls: List<DetectedCall>
)

data class ComposableFunction(
    val name: String,
    val startLine: Int,
    val endLine: Int,
    val bodyText: String
)

data class DetectedCall(
    val name: String,
    val line: Int,
    val column: Int,
    val arguments: Map<String, String>,
    val rawArgumentText: String,
    val modifierChain: String,
    val parentComposable: String?,
    val enclosingCallName: String?,
    val enclosingScopeText: String,
    val depth: Int
) {
    fun hasArgument(argName: String): Boolean = arguments.containsKey(argName)

    fun getArgument(argName: String): String? = arguments[argName]

    fun hasModifier(modifierName: String): Boolean =
        modifierChain.contains(".$modifierName(") || modifierChain.contains(".$modifierName {")

    fun hasSemanticsProperty(property: String): Boolean {
        val semanticsPattern = Regex("""\.\s*semantics\s*(\([^)]*\))?\s*\{[^}]*$property""", RegexOption.DOT_MATCHES_ALL)
        return semanticsPattern.containsMatchIn(modifierChain) || semanticsPattern.containsMatchIn(enclosingScopeText)
    }

    fun getModifierBlock(modifierName: String): String? {
        val pattern = Regex("""\.$modifierName\s*\{([^}]*)\}""", RegexOption.DOT_MATCHES_ALL)
        return pattern.find(modifierChain)?.groupValues?.getOrNull(1)?.trim()
    }
}
