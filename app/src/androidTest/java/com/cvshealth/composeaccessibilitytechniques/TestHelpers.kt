/*
   Copyright 2023-2025 CVS Health and/or one of its affiliates

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.cvshealth.composeaccessibilitytechniques

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun hasNoTestTag(): SemanticsMatcher =
    SemanticsMatcher.keyNotDefined(SemanticsProperties.TestTag)

@OptIn(ExperimentalComposeUiApi::class)
fun isHiddenFromAccessibility(): SemanticsMatcher =
    SemanticsMatcher.keyIsDefined(SemanticsProperties.HideFromAccessibility).or(
        // Covers the old way of doing things...
        SemanticsMatcher.keyIsDefined(SemanticsProperties.InvisibleToUser)
    )

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

fun hasContentDescription(): SemanticsMatcher {
    return SemanticsMatcher(
        "${SemanticsProperties.ContentDescription.name} contains non-empty value"
    ) {
        it.config.getOrNull(SemanticsProperties.ContentDescription)
            ?.any { item -> item.isNotBlank() } ?: false
    }
}

fun hasPaneTitle(): SemanticsMatcher {
    return SemanticsMatcher(
        "${SemanticsProperties.PaneTitle.name} contains non-empty value"
    ) {
        it.config.getOrNull(SemanticsProperties.PaneTitle)?.isNotBlank() ?: false
    }
}

fun hasPaneTitle(title: String): SemanticsMatcher {
    return SemanticsMatcher(
        "${SemanticsProperties.PaneTitle.name} equals $title"
    ) {
        it.config.getOrNull(SemanticsProperties.PaneTitle)?.equals(title) ?: false
    }
}

// Collection testing helpers
fun hasCollectionInfo(): SemanticsMatcher =
    SemanticsMatcher.keyIsDefined(SemanticsProperties.CollectionInfo)

fun hasNoCollectionInfo(): SemanticsMatcher =
    SemanticsMatcher.keyNotDefined(SemanticsProperties.CollectionInfo)

fun hasCollectionRowCount(expectedRowCount: Int) : SemanticsMatcher {
    return SemanticsMatcher("CollectionInfo.rowCount == ${expectedRowCount}") {
        expectedRowCount == it.config.getOrNull(SemanticsProperties.CollectionInfo)?.rowCount
    }
}
fun hasCollectionColumnCount(expectedColumnCount: Int) : SemanticsMatcher {
    return SemanticsMatcher("CollectionInfo.columnCount == ${expectedColumnCount}") {
        expectedColumnCount == it.config.getOrNull(SemanticsProperties.CollectionInfo)?.columnCount
    }
}

fun hasCollectionItemInfo(): SemanticsMatcher =
    SemanticsMatcher.keyIsDefined(SemanticsProperties.CollectionItemInfo)

fun hasNoCollectionItemInfo(): SemanticsMatcher =
    SemanticsMatcher.keyNotDefined(SemanticsProperties.CollectionItemInfo)

fun hasCollectionItemRowIndex(expectedRowIndex: Int) : SemanticsMatcher {
    return SemanticsMatcher("CollectionInfo.rowIndex == ${expectedRowIndex}") {
        expectedRowIndex == it.config.getOrNull(SemanticsProperties.CollectionItemInfo)?.rowIndex
    }
}
fun hasCollectionItemRowSpan(expectedRowSpan: Int) : SemanticsMatcher {
    return SemanticsMatcher("CollectionInfo.rowSpan == ${expectedRowSpan}") {
        expectedRowSpan == it.config.getOrNull(SemanticsProperties.CollectionItemInfo)?.rowSpan
    }
}

fun hasCollectionItemColumnIndex(expectedColumnIndex: Int) : SemanticsMatcher {
    return SemanticsMatcher("CollectionInfo.columnIndex == ${expectedColumnIndex}") {
        expectedColumnIndex == it.config.getOrNull(SemanticsProperties.CollectionItemInfo)?.columnIndex
    }
}
fun hasCollectionItemColumnSpan(expectedColumnSpan: Int) : SemanticsMatcher {
    return SemanticsMatcher("CollectionInfo.columnSpan == ${expectedColumnSpan}") {
        expectedColumnSpan == it.config.getOrNull(SemanticsProperties.CollectionItemInfo)?.columnSpan
    }
}

// CustomAction testing helpers
fun hasCustomActions(): SemanticsMatcher =
    SemanticsMatcher.keyIsDefined(SemanticsActions.CustomActions)

fun hasNoCustomActions(): SemanticsMatcher =
    SemanticsMatcher.keyNotDefined(SemanticsActions.CustomActions)

fun hasCustomActionLabelled(label: String) : SemanticsMatcher {
    return SemanticsMatcher("CustomActions.label contains ${label}") {
        it.config.getOrNull(SemanticsActions.CustomActions)?.map { action -> action.label }?.contains(label) ?: false
    }
}

// LiveRegion testing helpers
fun hasLiveRegion(): SemanticsMatcher =
    SemanticsMatcher.keyIsDefined(SemanticsProperties.LiveRegion)

fun hasNoLiveRegion(): SemanticsMatcher =
    SemanticsMatcher.keyNotDefined(SemanticsProperties.LiveRegion)

fun hasLiveRegionMode(mode: LiveRegionMode) : SemanticsMatcher {
    return SemanticsMatcher("SemanticsProperties.LiveRegion == ${mode}") {
        mode == it.config.getOrNull(SemanticsProperties.LiveRegion)
    }
}

// State Description helpers
fun hasSomeStateDescription(): SemanticsMatcher =
    SemanticsMatcher.keyIsDefined(SemanticsProperties.StateDescription)

fun hasNoStateDescription(): SemanticsMatcher =
    SemanticsMatcher.keyNotDefined(SemanticsProperties.StateDescription)

// Traversal Order helpers
fun hasIsTraversalGroup(): SemanticsMatcher =
    SemanticsMatcher.keyIsDefined(SemanticsProperties.IsTraversalGroup).or(
        // Covers the old way of doing things...
        SemanticsMatcher.keyIsDefined(SemanticsProperties.IsContainer)
    )

fun isTraversalGroup(): SemanticsMatcher {
    return SemanticsMatcher("SemanticsProperties.IsTraversalGroup == true") {
        true == it.config.getOrNull(SemanticsProperties.IsTraversalGroup)
    }
}

fun isNotATraversalGroup(): SemanticsMatcher {
    return SemanticsMatcher("SemanticsProperties.IsTraversalGroup == null or false") {
        val isTraversalGroupValue = it.config.getOrNull(SemanticsProperties.IsTraversalGroup)
        return@SemanticsMatcher isTraversalGroupValue == null || isTraversalGroupValue == false
    }
}

fun hasTraversalIndex(value: Float): SemanticsMatcher {
    return SemanticsMatcher("SemanticsProperties.TraversalIndex == {$value}") {
        value == it.config.getOrNull(SemanticsProperties.TraversalIndex)
    }
}

fun hasAnyTraversalIndex(): SemanticsMatcher =
    SemanticsMatcher.keyIsDefined(SemanticsProperties.TraversalIndex)

fun hasNoTraversalIndex(): SemanticsMatcher =
    SemanticsMatcher.keyNotDefined(SemanticsProperties.TraversalIndex)

// Error helpers
fun isError(): SemanticsMatcher =
    SemanticsMatcher.keyIsDefined(SemanticsProperties.Error)

fun hasError(value: String): SemanticsMatcher {
    return SemanticsMatcher("SemanticsProperties.Error == {$value}") {
        value == it.config.getOrNull(SemanticsProperties.Error)
    }
}

// Role helpers
fun hasAnyRole(): SemanticsMatcher =
    SemanticsMatcher.keyIsDefined(SemanticsProperties.Role)

fun hasRole(value: Role): SemanticsMatcher {
    return SemanticsMatcher("SemanticsProperties.Role == {$value}") {
        value == it.config.getOrNull(SemanticsProperties.Role)
    }
}

// SemanticsConfiguration helpers

fun hasClearAndSetSemantics(): SemanticsMatcher {
    return SemanticsMatcher("SemanticsConfiguration.isClearingSemantics") {
        it.config.isClearingSemantics
    }
}

fun hasMergeSemantics(): SemanticsMatcher {
    return SemanticsMatcher("SemanticsConfiguration.isMergingSemanticsOfDescendants") {
        it.config.isMergingSemanticsOfDescendants
    }
}

// OnClick helpers
fun hasClickActionLabel() : SemanticsMatcher {
    return SemanticsMatcher("OnClick.label exists") {
        it.config.getOrNull(SemanticsActions.OnClick)?.label != null
    }
}

fun hasClickActionLabelled(label: String) : SemanticsMatcher {
    return SemanticsMatcher("OnClick.label contains ${label}") {
        it.config.getOrNull(SemanticsActions.OnClick)?.label?.contains(label) ?: false
    }
}

// Dismiss action testing helpers
fun hasDismissAction(): SemanticsMatcher =
    SemanticsMatcher.keyIsDefined(SemanticsActions.Dismiss)

fun hasNoDismissAction(): SemanticsMatcher =
    SemanticsMatcher.keyNotDefined(SemanticsActions.Dismiss)

// RadioButton helpers
fun hasSelectableGroup(): SemanticsMatcher =
    SemanticsMatcher.keyIsDefined(SemanticsProperties.SelectableGroup)

// Target size helpers
fun hasMinimumSize(density: Float, minSize: Dp) : SemanticsMatcher {
    return SemanticsMatcher("SemanticsNode.size >= $minSize x $minSize") {
        val heightDp = (it.size.height / density).dp
        val widthDp = (it.size.width / density).dp
        heightDp >= minSize && widthDp >= minSize
    }
}

fun hasMinimumTouchTargetSize(density: Float, minSize: Dp) : SemanticsMatcher {
    return SemanticsMatcher("SemanticsNode.touchBoundsInRoot >= $minSize x $minSize") {
        if (it.touchBoundsInRoot.isEmpty || it.touchBoundsInRoot.isInfinite) {
            false
        } else {
            val touchHeightDp = (it.touchBoundsInRoot.height / density).dp
            val touchWidthDp = (it.touchBoundsInRoot.width / density).dp
            touchHeightDp >= minSize && touchWidthDp >= minSize
        }
    }
}

// Autofill contentType helpers
fun hasAnyContentType(): SemanticsMatcher =
    SemanticsMatcher.keyIsDefined(SemanticsProperties.ContentType)

fun hasNoContentType(): SemanticsMatcher =
    SemanticsMatcher.keyNotDefined(SemanticsProperties.ContentType)

fun hasContentType(value: ContentType): SemanticsMatcher {
    return SemanticsMatcher("SemanticsProperties.ContentType == {$value}") {
        value == it.config.getOrNull(SemanticsProperties.ContentType)
    }
}