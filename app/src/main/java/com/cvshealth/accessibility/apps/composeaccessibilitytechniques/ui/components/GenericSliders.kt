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

import android.view.KeyEvent
import androidx.annotation.IntRange
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription

/**
 * Create an accessible, labeled [Slider] control.
 *
 * Adds semantic labeling, keyboard operability with navigation key adjustment, an override for the
 * default [Slider] state description announcement ("xx percent"), and liveRegion semantics.
 *
 * @param label label text string
 * @param value the current [Slider] value
 * @param onValueChange callback for [Slider] value change
 * @param modifier optional [Modifier] for [Slider]
 * @param enabled the enabled state of this [Slider]
 * @param valueRange optional [ClosedFloatingPointRange] of allowed values for [Slider]
 * @param steps optional discrete step count between start and end points of [Slider]
 * @param onValueChangeFinished callback for [Slider] completion of value change operations
 * @param colors optional [SliderColors] for [Slider]
 * @param interactionSource optional [MutableInteractionSource] for [Slider]
 * @param toStateDescription optional callback to map [Slider] value to a state description message string
 */
@Composable
fun GenericSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    @IntRange(from = 0)
    steps: Int = 0, // steps between the start and end point (exclusive of both)
    onValueChangeFinished: (() -> Unit)? = null,
    colors: SliderColors = SliderDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    toStateDescription: ((Float) -> String)? = null
) {
    BodyText(label)

    // Note: 20 steps seems to be default step count for continuous Slider adjustment in TalkBack
    val adjustmentSteps = if (steps > 0) steps + 1 else 20
    val stepSize = (valueRange.endInclusive - valueRange.start) / adjustmentSteps
    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            // Key technique: Allow the left and right arrow keys to adjust the slider value
            // provided the resulting value is within the slider's range; otherwise, allow
            // normal arrow key navigation to apply.
            .onKeyEvent { keyEvent ->
                when (keyEvent.nativeKeyEvent.keyCode) {
                    KeyEvent.KEYCODE_DPAD_LEFT -> {
                        // Have to absorb both the DPAD_LEFT key DOWN and UP events, because
                        // otherwise screen navigation captures key DOWN and the key UP
                        // event is never received.
                        if (valueRange.contains(value - stepSize)) {
                            if (keyEvent.nativeKeyEvent.action == KeyEvent.ACTION_UP) {
                                onValueChange(value - stepSize)
                            }
                            true
                        } else {
                            false
                        }
                    }

                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        if (keyEvent.nativeKeyEvent.action == KeyEvent.ACTION_UP &&
                            valueRange.contains(value + stepSize)
                        ) {
                            onValueChange(value + stepSize)
                            true
                        } else {
                            false
                        }
                    }

                    else -> {
                        false
                    }
                }
            }
            .semantics {
                // Key technique: Slider contentDescription must duplicate label text,
                // because Slider does not support a text label. (See
                // https://issuetracker.google.com/issues/236988201.)
                // However, contentDescription can extend the label text.
                contentDescription = label

                // stateDescription (optionally) replaces the default "xx percent" text for a Slider
                if (toStateDescription != null) {
                    stateDescription = toStateDescription.invoke(value)
                }

                // liveRegion announces the Slider's state when its value changes.
                liveRegion = LiveRegionMode.Polite
            },
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        colors = colors,
        interactionSource = interactionSource
    )
}