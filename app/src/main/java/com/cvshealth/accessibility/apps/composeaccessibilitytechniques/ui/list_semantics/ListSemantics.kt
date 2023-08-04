package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BadExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyTextNoPadding
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BulletListItemRow
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericListColumn
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.TextListItemRow
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val listSemanticsHeadingTestTag = "listSemanticsHeading"

@Composable
fun ListSemanticsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.list_semantics_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()
        Column(
            modifier = modifier
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.list_semantics_heading),
                modifier = Modifier.testTag(listSemanticsHeadingTestTag)
            )
            BodyText(textId = R.string.list_semantics_description_1)
            BodyText(textId = R.string.list_semantics_description_2)

            BadExample1()
            GoodExample2()
            GoodExample3()
            GoodExample4()

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme() {
        ListSemanticsScreen {}
    }
}

@Composable
private fun FauxBulletListRow(
    @StringRes textId: Int,
    modifier: Modifier = Modifier
) {
    val bulletPointAltText = stringResource(id = R.string.list_semantics_bullet_point_alt_text)
    Row(
        modifier = modifier.semantics(mergeDescendants = true) {},
        verticalAlignment = Alignment.Top
    ) {
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

@Composable
private fun BadExample1() {
    // Bad example 1: Visual list without list semantics
    BadExampleHeading(text = stringResource(id = R.string.list_semantics_example_1))
    Column(
        modifier = Modifier.padding(top = 8.dp)
    ) {
        FauxBulletListRow(textId = R.string.list_semantics_bad_point_1)
        FauxBulletListRow(textId = R.string.list_semantics_bad_point_2)
        FauxBulletListRow(textId = R.string.list_semantics_bad_point_3)
    }
    BodyText(textId = R.string.list_semantics_after_bad_example)
}

@Preview(showBackground = true)
@Composable
private fun BadExample1Preview() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            BadExample1()
        }
    }
}

@Composable
private fun GoodExample2() {
    // Good example 2: Bullet list with list semantics
    GoodExampleHeading(text = stringResource(id = R.string.list_semantics_example_2))
    GenericListColumn(
        rowCount = 3,
        modifier = Modifier.padding(top = 8.dp)
    ) {
        BulletListItemRow(rowIndex = 0, textId = R.string.list_semantics_good_point_1)
        BulletListItemRow(rowIndex = 1, textId = R.string.list_semantics_good_point_2)
        BulletListItemRow(rowIndex = 2, textId = R.string.list_semantics_good_point_3)
    }
    BodyText(textId = R.string.list_semantics_after_list)
}

@Preview(showBackground = true)
@Composable
private fun GoodExample2Preview() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample2()
        }
    }
}

@Composable
private fun GoodExample3() {
    // Good example 3: Numbered list with list semantics
    GoodExampleHeading(text = stringResource(id = R.string.list_semantics_example_3))
    GenericListColumn(
        rowCount = 3,
        modifier = Modifier.padding(top = 8.dp)
    ) {
        TextListItemRow(rowIndex = 0, textId = R.string.list_semantics_good_point_1_numbered)
        TextListItemRow(rowIndex = 1, textId = R.string.list_semantics_good_point_2_numbered)
        TextListItemRow(rowIndex = 2, textId = R.string.list_semantics_good_point_3_numbered)
    }
    BodyText(textId = R.string.list_semantics_after_list_2)
}

@Preview(showBackground = true)
@Composable
private fun GoodExample3Preview() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample3()
        }
    }
}

@Composable
private fun LazyRowItem(
    @StringRes textId: Int,
    modifier: Modifier = Modifier
) {
    MaterialTheme.colorScheme.onSurface
    Box(
        modifier = modifier
            .padding(end = 8.dp)
            .semantics(mergeDescendants = true) {}
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
            .padding(all = 4.dp)
    ) {
        Text(text = stringResource(id = textId))
    }
}

@Composable
private fun GoodExample4() {
    // Good example 4: LazyColumn list with list semantics
    GoodExampleHeading(text = stringResource(id = R.string.list_semantics_example_4))
    LazyRow(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        item { LazyRowItem(textId = R.string.list_semantics_lazyrow_1) }
        item { LazyRowItem(textId = R.string.list_semantics_lazyrow_2) }
        item { LazyRowItem(textId = R.string.list_semantics_lazyrow_3) }
        item { LazyRowItem(textId = R.string.list_semantics_lazyrow_4) }
        item { LazyRowItem(textId = R.string.list_semantics_lazyrow_5) }
        item { LazyRowItem(textId = R.string.list_semantics_lazyrow_6) }
    }
    BodyText(textId = R.string.list_semantics_after_list_3)
}

@Preview(showBackground = true)
@Composable
private fun GoodExample4Preview() {
    ComposeAccessibilityTechniquesTheme() {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample4()
        }
    }
}