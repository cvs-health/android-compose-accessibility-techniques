/*
   Copyright 2023 CVS Health and/or one of its affiliates

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

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ErrorRed
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.SuccessGreen

@Composable
private fun GenericExampleTitle(
    @StringRes textId: Int,
    @DrawableRes drawableId: Int,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    Row(
        modifier = modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(id = drawableId),
            contentDescription = null,
            modifier = Modifier
                .defaultMinSize(24.dp, minHeight = 24.dp)
                .align(Alignment.CenterVertically),
            tint = tint
        )
        Text(
            stringResource(id = textId),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp)
        )
    }
}

@Composable
fun GoodExampleTitle(
    @StringRes textId: Int,
    modifier: Modifier = Modifier
) {
    GenericExampleTitle(
        textId = textId,
        drawableId = R.drawable.ic_check_fill,
        modifier = modifier,
        tint = SuccessGreen
    )
}

@Preview(showBackground = true)
@Composable
private fun GoodExampleTitlePreview() {
    ComposeAccessibilityTechniquesTheme() {
        GoodExampleTitle(textId = R.string.content_grouping_table_example_3)
    }
}

@Composable
fun BadExampleTitle(
    @StringRes textId: Int,
    modifier: Modifier = Modifier
) {
    GenericExampleTitle(
        textId = textId,
        drawableId = R.drawable.ic_close_fill,
        modifier = modifier,
        tint = ErrorRed
    )
}

@Preview(showBackground = true)
@Composable
private fun BadExampleTitlePreview() {
    ComposeAccessibilityTechniquesTheme() {
        BadExampleTitle(textId = R.string.content_grouping_card1_title)
    }
}