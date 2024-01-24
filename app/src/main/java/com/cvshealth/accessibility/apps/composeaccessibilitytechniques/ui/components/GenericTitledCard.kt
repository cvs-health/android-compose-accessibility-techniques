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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

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
fun GenericPanePreview() {
    ComposeAccessibilityTechniquesTheme() {
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