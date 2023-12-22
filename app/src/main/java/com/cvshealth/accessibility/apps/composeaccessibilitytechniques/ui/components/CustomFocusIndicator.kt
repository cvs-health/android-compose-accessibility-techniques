package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R

/**
 * customFocusBorder - a Modifier extension that applies a custom focus indicator border to a
 * Composable.
 *
 * See [React to focus](https://developer.android.com/jetpack/compose/touch-input/focus/react-to-focus) for other options.
 *
 * Key techniques:
 * 1. Remember the appropriate border color for the control's current state. Use a transparent color
 *    for the unfocused state.
 * 2. Retrieve the appropriate day/night color resource for the border.
 * 3. On focus change, change the border color value. Note that isFocused is used instead of
 *    hasFocus so that parent composables do not highlight when one of their children has focus.
 * 4. Apply a 2dp border of the appropriate color.
 */
@Composable
fun Modifier.customFocusBorder(): Modifier {
    var borderColor by remember {
        mutableStateOf(Color.Transparent)
    }
    val focusIndicatorColor = colorResource(id = R.color.focus_indicator_outline)
    return this
        .onFocusChanged {
            borderColor = if (it.isFocused) focusIndicatorColor else Color.Transparent
        }
        .border(2.dp, borderColor, shape = RoundedCornerShape(4.dp))
}