/*
   Copyright 2024 CVS Health and/or one of its affiliates

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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R

/**
 * GenericStandAloneLink - Presents an accessible stand-alone link with an optional external link
 * icon and a custom onClickLabel.
 *
 * Note that Android does not have a defined semantic "link" role. As a proxy, provide an
 * onClickLabel the specifies that this control opens a browser.
 *
 * @param text - Link text to display
 * @param modifier - Modifies the clickable Row layout of the stand-alone link
 * @param showExternalLinkIcon - Selects whether to display external link icon (default) or not
 * @param onClick - Callback when the stand-alone link is activated.
 */
@Composable
fun GenericStandAloneLink(
    text: String,
    modifier: Modifier = Modifier,
    showExternalLinkIcon: Boolean = true,
    onClick: () -> Unit
) {
    // Key technique 1a: Acquire the onClickLabel text.
    val genericLinkOnClickLabel =
        stringResource(id = R.string.standalone_link_on_click_label)

    Row(
        modifier = modifier
            // Optional technique: Provide an enhanced keyboard focus indicator.
            .visibleFocusBorder()
            .clickable(
                // Key technique 1b: Apply the onClickLabel
                onClickLabel = genericLinkOnClickLabel,
                onClick = onClick
            )
            // Key technique 2: Make the stand-alone link large enough to easily tap.
            .minimumInteractiveComponentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            // Key technique 3a: Use weight() without fill to keep the icon visible when the text
            // wraps at large text size.
            modifier = Modifier.weight(1f, fill = false),
            // Key technique 4: Apply a distinctive link text style that goes beyond color alone.
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.bodyMedium,
        )
        if (showExternalLinkIcon) {
            Spacer(Modifier.width(8.dp))
            // Key technique: 3b: Use an external link icon to visually indicate that the link opens in
            // the browser, not within this app.
            Icon(
                painter = painterResource(id = R.drawable.ic_external_link_outline),
                contentDescription = null, // Note: This information is already conveyed in onClickLabel.
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}