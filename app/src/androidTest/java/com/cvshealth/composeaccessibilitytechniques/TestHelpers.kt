package com.cvshealth.composeaccessibilitytechniques

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher


fun hasNoTestTag(): SemanticsMatcher =
    SemanticsMatcher.keyNotDefined(SemanticsProperties.TestTag)

fun hasText(): SemanticsMatcher {
    val propertyName = "${SemanticsProperties.Text.name} + ${SemanticsProperties.EditableText.name}"

    return SemanticsMatcher(
        "$propertyName contains Text"
    ) {
        val isInEditableTextValue = it.config.getOrNull(SemanticsProperties.EditableText)
            ?.text?.isNotBlank() ?: false
        val isInTextValue = it.config.getOrNull(SemanticsProperties.Text)
            ?.any { item -> item.text.isNotBlank() } ?: false
        isInEditableTextValue || isInTextValue
    }
}