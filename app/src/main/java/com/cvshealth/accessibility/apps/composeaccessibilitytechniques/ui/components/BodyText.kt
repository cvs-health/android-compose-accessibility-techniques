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

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun BodyTextNoPadding(
    @StringRes textId: Int,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null
) {
    Text(
        stringResource(id = textId),
        modifier = modifier
            .fillMaxWidth(),
        textAlign = textAlign,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun BodyText(
    @StringRes textId: Int,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null
) {
    BodyTextNoPadding(
        textId,
        modifier.padding(top = 8.dp),
        textAlign
    )
}

@Composable
fun BoldBodyTextNoPadding(
    @StringRes textId: Int,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null
) {
    Text(
        stringResource(id = textId),
        modifier = modifier
            .fillMaxWidth(),
        fontWeight = FontWeight.Bold,
        textAlign = textAlign,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun BoldBodyText(
    @StringRes textId: Int,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null
) {
    BoldBodyTextNoPadding(
        textId,
        modifier.padding(top = 8.dp),
        textAlign
    )
}
