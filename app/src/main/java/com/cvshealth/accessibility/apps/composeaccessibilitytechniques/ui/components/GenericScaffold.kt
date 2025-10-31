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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R

const val genericScaffoldTitleTestTag = "genericScaffoldTitle"
const val genericScaffoldBackButtonTestTag = "genericScaffoldBackButton"

/**
 * Creates a default screen [Scaffold], including proper screen title handling.
 *
 * @param title screen title string
 * @param onBackPressed callback for [TopAppBar] "Navigate Up" button
 * @param modifier optional [Modifier] for [Scaffold]
 * @param bottomBar optional bottom app bar composable
 * @param snackbarHost optional snackbar host composable
 * @param floatingActionButton optional FloatingActionButton composable
 * @param floatingActionButtonPosition optional [FabPosition] for the FloatingActionButton
 * @param content the [Scaffold] content composable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericScaffold(
    title: String,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    content: @Composable (modifier: Modifier) -> Unit
) {
    Scaffold(
        // Key technique 1: Use paneTitle semantics to announce the screen title.
        modifier = modifier.semantics {
            paneTitle = title
        },
        topBar = {
            TopAppBar(
                // Key technique 2: Use TopAppBar and its title parameter to visually display a
                // screen title.
                title = {
                    Text(
                        text = title,
                        modifier = Modifier
                            .testTag(genericScaffoldTitleTestTag)
                            .semantics { heading() },
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    VisibleFocusBorderIconButton(
                        onClick = onBackPressed,
                        modifier = Modifier.testTag(genericScaffoldBackButtonTestTag)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back_24dp),
                            contentDescription = stringResource(id = R.string.navigate_up)
                        )
                    }
                }
            )
        },
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition
    ) { contentPadding ->
        // Screen content
        content(Modifier.padding(contentPadding))
    }
}