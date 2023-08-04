package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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