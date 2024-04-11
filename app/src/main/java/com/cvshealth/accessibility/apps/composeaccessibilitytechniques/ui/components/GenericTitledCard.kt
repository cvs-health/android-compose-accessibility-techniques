/*
   © Copyright 2024 CVS Health and/or one of its affiliates. All rights reserved.

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

/**
 * Displays a reusable [OutlinedCard] with an title string and content.
 *
 * @param title card title string
 * @param modifier optional [Modifier] for the [OutlinedCard]
 * @param content composable contents of the [OutlinedCard] following the title row
 */
@Composable
fun GenericTitledCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    OutlinedCard(
        modifier = modifier,
    ) {
        GenericExampleTitle(
            text = title,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        HorizontalDivider()
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )  {
            content(this)
        }
    }
}

/**
 * Displays a reusable [OutlinedCard] with an title string and content.
 *
 * @param titleId card title string resource identifier
 * @param modifier optional [Modifier] for the [OutlinedCard]
 * @param content composable contents of the [OutlinedCard] following the title row
 */
@Composable
fun GenericTitledCard(
    @StringRes titleId: Int,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) = GenericTitledCard(
    title = stringResource(titleId),
    modifier = modifier,
    content = content
)

@Preview(showBackground = true)
@Composable
private fun GenericPanePreview() {
    ComposeAccessibilityTechniquesTheme {
        GenericTitledCard(
            title = "Test pane"
        ) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                Text("This is a test pane item")
                Text("This is another test pane item")
            }
        }
    }
}

/**
 * Displays a reusable [OutlinedCard] with an title string and content, grouping them for
 * accessibility.
 *
 * @param title card title string
 * @param modifier optional [Modifier] for the [OutlinedCard]
 * @param content composable contents of the [OutlinedCard] following the title row
 */
@Composable
fun GenericGroupedTitledCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    OutlinedCard(
        modifier = modifier.semantics(mergeDescendants = true) {},
    ) {
        GenericExampleTitle(
            text = title,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        HorizontalDivider()
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )  {
            content(this)
        }
    }
}

/**
 * Displays a reusable [OutlinedCard] with an title string and content, grouping them for
 * accessibility.
 *
 * @param titleId card title string resource identifier
 * @param modifier optional [Modifier] for the [OutlinedCard]
 * @param content composable contents of the [OutlinedCard] following the title row
 */
@Composable
fun GenericGroupedTitledCard(
    @StringRes titleId: Int,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) = GenericGroupedTitledCard(
    title = stringResource(titleId),
    modifier = modifier,
    content = content
)