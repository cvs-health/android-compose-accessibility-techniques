/*
   Copyright 2023-2024 CVS Health and/or one of its affiliates

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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.CollectionItemInfo
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.collectionItemInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

/**
 * Add accessibility collection semantics to a Modifier for a layout composable.
 * Used for manually marking visually-presented lists with list semantics.
 *
 * Key technique: Apply Modifier.semantics { collectionInfo = CollectionInfo(...) }
 * to the wrapping layout composable (here, a Column). Use -1 for rowCount and/or
 * columnCount if they are indefinite or unknown.
 *
 * Note that lists are a special case of 2-dimensional collections, so Data Table/Grid Semantics
 * can be applied with the same semantics property.
 *
 * @param size the number of rows in the list
 */
fun Modifier.addListSemantics(size: Int): Modifier = this.semantics {
    collectionInfo = CollectionInfo(rowCount = size, columnCount = 1)
}


/**
 * Add accessibility collection item semantics to a Modifier for a layout's child composables.
 * Used for manually marking items within a visually-presented list.
 *
 * Multiple associated composables can share the same index and will be treated semantically as the
 * same list item.
 *
 * Key technique: Modifier.semantics { collectionItemInfo = CollectionItemInfo(...) } defines
 * that this Row is a list item and its position and span within the list.
 *
 * Note: index is zero-based.
 *
 * Also note that lists are a special case of 2-dimensional collections, so Data Table/Grid
 * Semantics can be applied with the same semantics property, including items which span multiple
 * rows and columns.
 *
 * @param index the 0-based list index of this list item
 */
fun Modifier.addListItemSemantics(
    index: Int
): Modifier = this.semantics(mergeDescendants = true) {
    collectionItemInfo = CollectionItemInfo(
        rowIndex = index,
        rowSpan = 1,
        columnIndex = 0,
        columnSpan = 1
    )
}

/**
 * Display a [Column] with list semantics.
 *
 * @param rowCount the number of rows in the list
 * @param modifier optional [Modifier] for the list layout [Column]
 * @param verticalArrangement optional [Arrangement.Vertical] for the list layout [Column]
 * @param horizontalAlignment optional [Alignment.Horizontal] for the list layout [Column]
 * @param content the content of the list layout [Column]
 */
@Composable
fun GenericListColumn(
    rowCount: Int,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.addListSemantics(rowCount),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}

/**
 * Display a [Row] with list item semantics.
 *
 * @param rowIndex the 0-based index of this row in the list
 * @param modifier optional [Modifier] for the list item layout [Row]
 * @param horizontalArrangement optional [Arrangement.Horizontal] for the list item layout [Row]
 * @param verticalAlignment optional [Alignment.Vertical] for the list item layout [Row]
 * @param content the content of the list item layout [Row]
 */
@Composable
fun GenericListItemRow(
    rowIndex: Int,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier.addListItemSemantics(rowIndex),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
private fun GenericListPreview() {
    ComposeAccessibilityTechniquesTheme {
        GenericListColumn(rowCount = 1) {
            GenericListItemRow(rowIndex = 0) {
                Text("This is a test list item")
            }
            GenericListItemRow(rowIndex = 1) {
                Text("This is another test list item")
            }
        }
    }
}

/**
 * Display a [Row] with list item semantics containing only a single [Text].
 *
 * @param rowIndex the 0-based index of this row in the list
 * @param textId the string resource identifier of the list item [Text]
 * @param modifier optional [Modifier] for the list item layout [Row]
 */
@Composable
fun TextListItemRow(
    rowIndex: Int,
    @StringRes textId: Int,
    modifier: Modifier = Modifier
) {
    GenericListItemRow(
        rowIndex = rowIndex,
        modifier = modifier,
        verticalAlignment = Alignment.Top
    ) {
        BodyTextNoPadding(textId = textId,
            modifier = Modifier.weight(1f, fill = true),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextListPreview() {
    ComposeAccessibilityTechniquesTheme {
        GenericListColumn(rowCount = 1) {
            TextListItemRow(
                rowIndex = 0,
                textId = R.string.home_description
            )
            TextListItemRow(
                rowIndex = 1,
                textId = R.string.home_description
            )
        }
    }
}

/**
 * Display a [Row] with list item semantics containing a bullet point (as [Text]) and a [Text].
 *
 * @param rowIndex the 0-based index of this row in the list
 * @param textId the string resource identifier of the list item [Text]
 * @param modifier optional [Modifier] for the list item layout [Row]
 */
@Composable
fun BulletListItemRow(
    rowIndex: Int,
    @StringRes textId: Int,
    modifier: Modifier = Modifier
) {
    GenericListItemRow(
        rowIndex = rowIndex,
        modifier = modifier,
        verticalAlignment = Alignment.Top,
    ) {
        // Key technique: The Unicode "bullet point" character is not announced by TalkBack, so add
        // a contentDescription to it, so the bulleted nature of each list item is announced.
        // Not the only way to do this; for example, an Icon could be used instead of Text...
        val bulletPointAltText = stringResource(id = R.string.list_semantics_bullet_point_alt_text)
        Text(
            text = "\u2022",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .semantics {
                    contentDescription = bulletPointAltText
                }
                .width(24.dp)
        )
        BodyTextNoPadding(textId = textId,
            modifier = Modifier.weight(1f, fill = true),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BulletListShortPreview() {
    ComposeAccessibilityTechniquesTheme {
        GenericListColumn(rowCount = 1) {
            BulletListItemRow(
                rowIndex = 0,
                textId = R.string.navigate_up
            )
            BulletListItemRow(
                rowIndex = 1,
                textId = R.string.home_title
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BulletListLongPreview() {
    ComposeAccessibilityTechniquesTheme {
        GenericListColumn(rowCount = 1) {
            BulletListItemRow(
                rowIndex = 0,
                textId = R.string.home_description
            )
            BulletListItemRow(
                rowIndex = 1,
                textId = R.string.home_description_2
            )
        }
    }
}