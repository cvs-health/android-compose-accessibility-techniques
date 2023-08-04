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


@Composable
fun GenericListColumn(
    rowCount: Int,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    // Key technique: Apply Modifier.semantics { collectionInfo = CollectionInfo(...) }
    // to the wrapping layout composable (here, a Column). Use -1 for rowCount and/or
    // columnCount if they are indefinite or unknown.
    //
    // Note that "lists" are actually 2-dimensional collections, so Data Table/Grid Semantics can be
    // applied in the same way.
    Column(
        modifier = modifier
            .semantics {
                collectionInfo = CollectionInfo(rowCount = rowCount, columnCount = 1)
            },
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}

@Composable
fun GenericListItemRow(
    rowIndex: Int,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable RowScope.() -> Unit
) {
    // Key technique: Modifier.semantics { collectionItemInfo = CollectionItemInfo(...) } defines
    // that this Row is a list item and its position and span within the list.
    //
    // Note that "lists" are actually 2-dimensional collections, so Data Table/Grid Semantics can be
    // applied in the same way.
    Row(
        modifier = modifier
            .semantics(mergeDescendants = true) {
                collectionItemInfo = CollectionItemInfo(
                    rowIndex = rowIndex,
                    rowSpan = 1,
                    columnIndex = 0,
                    columnSpan = 1
                )
            },
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun GenericListPreview() {
    ComposeAccessibilityTechniquesTheme() {
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
fun textListPreview() {
    ComposeAccessibilityTechniquesTheme() {
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
fun BulletListShortPreview() {
    ComposeAccessibilityTechniquesTheme() {
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
fun BulletListLongPreview() {
    ComposeAccessibilityTechniquesTheme() {
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