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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R

/**
 * visibleFocusBorder - a Modifier extension that applies a highly-visible custom keyboard focus
 * indicator border to a Composable.
 *
 * See [React to focus](https://developer.android.com/jetpack/compose/touch-input/focus/react-to-focus) for other options.
 *
 * Key techniques:
 * 1. Remember the appropriate border color for the control's current state. Use a transparent color
 *    for the unfocused state.
 * 2. Retrieve the appropriate day/night color resource for the border.
 * 3. On focus change, change the border color value. Note that isFocused is used instead of
 *    hasFocus so that parent composables do not highlight when one of their children has focus.
 * 4. Apply a narrow border of the appropriate color.
 */
@Composable
fun Modifier.visibleFocusBorder(): Modifier {
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

/**
 * visibleCardFocusBorder - a Modifier extension that applies a highly-visible custom keyboard focus
 * indicator border to a Card Composable using the default Card outline shape.
 */
@Composable
fun Modifier.visibleCardFocusBorder(): Modifier {
    var borderColor by remember {
        mutableStateOf(Color.Transparent)
    }
    val focusIndicatorColor = colorResource(id = R.color.focus_indicator_outline)
    return this
        .onFocusChanged {
            borderColor = if (it.isFocused) focusIndicatorColor else Color.Transparent
        }
        .border(2.dp, borderColor, shape = CardDefaults.shape)
}

/**
 * VisibleFocusBorderButton - creates a standard filled Button with a visible custom keyboard focus
 * indicator border, unless an overriding BorderStroke is provided.
 *
 * @param onClick called when this button is clicked
 * @param modifier the [Modifier] to be applied to this button
 * @param enabled controls the enabled state of this button. When `false`, this component will not
 * respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param shape defines the shape of this button's container, border (when [border] is not null),
 * and shadow (when using [elevation])
 * @param colors [ButtonColors] that will be used to resolve the colors for this button in different
 * states. See [ButtonDefaults.buttonColors].
 * @param elevation [ButtonElevation] used to resolve the elevation for this button in different
 * states. This controls the size of the shadow below the button. Additionally, when the container
 * color is [ColorScheme.surface], this controls the amount of primary color applied as an overlay.
 * See [ButtonElevation.shadowElevation] and [ButtonElevation.tonalElevation].
 * @param border overrides the default custom focus indicator border drawn around the container of
 * this button. If [border] is provided, [interactionSource] is likely also required for focus
 * coordination; otherwise, [Interaction]s can be tracked locally.
 * @param contentPadding the spacing values to apply internally between the container and the
 * content
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this button. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this button in different states.
 * @param onClickLabel semantic / accessibility label for the [onClick] action
 */
@Composable
fun VisibleFocusBorderButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClickLabel: String? = null,
    content: @Composable RowScope.() -> Unit
) {
    // Key technique: Apply the appropriate focus outline color for dark and light themes.
    // This color must also contrast with the fill color used in either theme.
    val focusIndicatorColor = colorResource(id = R.color.focus_indicator_outline)
    // Key technique: Remember focused state using border color.
    // Use a transparent border color for the unfocused state.
    var buttonBorderColor by remember {
        mutableStateOf(Color.Transparent)
    }
    Button(
        onClick = onClick, // activates in touch mode or via keyboard
        modifier = modifier
            // Key technique: Track focus state change.
            .onFocusChanged {
                buttonBorderColor = if (it.isFocused) focusIndicatorColor else Color.Transparent
            }
            .semantics {
                // Key onClickLabel technique: Add a custom onClickLabel to Buttons using semantics,
                // not Modifier.clickable, because the later would add a second tab stop for the
                // Button (with different focus highlighting).
                if (onClickLabel != null) {
                    onClick(label = onClickLabel) { // activates in Assistive Technologies
                        // Note that the same onClick() handler is used both here and for the
                        // onClick parameter above, but here the lambda must return a Boolean
                        // indicating whether the action was successfully handled. This is only
                        // presumed to be true here.
                        onClick.invoke()
                        true
                    }
                }
            },
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        // Key technique: Provide a border that is visible in the focused state.
        border = border ?: BorderStroke(
            width = 2.dp,
            color = buttonBorderColor
        ),
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}

/**
 * VisibleFocusBorderTextButton - creates a standard TextButton with a visible custom keyboard focus
 * indicator border, unless an overriding BorderStroke is provided.
 *
 * @param onClick called when this button is clicked
 * @param modifier the [Modifier] to be applied to this button
 * @param enabled controls the enabled state of this button. When `false`, this component will not
 * respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param shape defines the shape of this button's container, border (when [border] is not null),
 * and shadow (when using [elevation])
 * @param colors [ButtonColors] that will be used to resolve the colors for this button in different
 * states. See [ButtonDefaults.buttonColors].
 * @param elevation [ButtonElevation] used to resolve the elevation for this button in different
 * states. This controls the size of the shadow below the button. Additionally, when the container
 * color is [ColorScheme.surface], this controls the amount of primary color applied as an overlay.
 * See [ButtonElevation.shadowElevation] and [ButtonElevation.tonalElevation].
 * @param border overrides the default custom focus indicator border drawn around the container of
 * this button. If [border] is provided, [interactionSource] is likely also required for focus
 * coordination; otherwise, [Interaction]s can be tracked locally.
 * @param contentPadding the spacing values to apply internally between the container and the
 * content
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this button. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this button in different states.
 * @param onClickLabel semantic / accessibility label for the [onClick] action
 */
@Composable
fun VisibleFocusBorderTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.textShape,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    elevation: ButtonElevation? = null,
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClickLabel: String? = null,
    content: @Composable RowScope.() -> Unit
) {
    // Key technique: Apply the appropriate focus outline color for dark and light themes.
    // This color must also contrast with the fill color used in either theme.
    val focusIndicatorColor = colorResource(id = R.color.focus_indicator_outline)
    // Key technique: Remember focused state using border color.
    // Use a transparent border color for the unfocused state.
    var buttonBorderColor by remember {
        mutableStateOf(Color.Transparent)
    }
    TextButton(
        onClick = onClick, // activates in touch mode or via keyboard
        modifier = modifier
            // Key technique: Track focus state change.
            .onFocusChanged {
                buttonBorderColor = if (it.isFocused) focusIndicatorColor else Color.Transparent
            }
            .semantics {
                // Key onClickLabel technique: Add a custom onClickLabel to Buttons using semantics,
                // not Modifier.clickable, because the later would add a second tab stop for the
                // Button (with different focus highlighting).
                if (onClickLabel != null) {
                    onClick(label = onClickLabel) { // activates in Assistive Technologies
                        // Note that the same onClick() handler is used both here and for the
                        // onClick parameter above, but here the lambda must return a Boolean
                        // indicating whether the action was successfully handled. This is only
                        // presumed to be true here.
                        onClick.invoke()
                        true
                    }
                }
            },
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        // Key technique: Provide a border that is visible in the focused state.
        border = border ?: BorderStroke(
            width = 2.dp,
            color = buttonBorderColor
        ),
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}

/**
 * VisibleFocusBorderIconButton - creates a standard IconButton with a visible custom keyboard focus
 * indicator border, unless an overriding BorderStroke is provided.
 *
 * @param onClick called when this icon button is clicked
 * @param modifier the [Modifier] to be applied to this icon button
 * @param enabled controls the enabled state of this icon button. When `false`, this component will
 * not respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param shape defines the shape of this icon button's container and border (when [border] is not
 * null)
 * @param colors [IconButtonColors] that will be used to resolve the colors used for this icon
 * button in different states. See [IconButtonDefaults.outlinedIconButtonColors].
 * @param border overrides the default custom focus indicator border drawn around the container of
 * this button. If [border] is provided, [interactionSource] is likely also required for focus
 * coordination; otherwise, [Interaction]s can be tracked locally.
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this icon button. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this icon button in different states.
 * @param onClickLabel semantic / accessibility label for the [onClick] action
 * @param content the content of this icon button, typically an [Icon]
 */
@Composable
fun VisibleFocusBorderIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = IconButtonDefaults.outlinedShape,
    colors: IconButtonColors = IconButtonDefaults.outlinedIconButtonColors(),
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClickLabel: String? = null,
    content: @Composable () -> Unit
) {
    // Key technique: Apply the appropriate focus outline color for dark and light themes.
    // This color must also contrast with the fill color used in either theme.
    val focusIndicatorColor = colorResource(id = R.color.focus_indicator_outline)

    // Key technique: Remember focused state using border color.
    // Use a transparent border color for the unfocused state.
    var buttonBorderColor by remember {
        mutableStateOf(Color.Transparent)
    }

    // Key technique: Use an OutlinedIconButton, even though when unfocused, the button will appear
    // as if it has no outline.
    OutlinedIconButton(
        onClick = onClick, // activates in touch mode or via keyboard
        modifier = modifier
            // Key technique: Track focus state change.
            .onFocusChanged {
                buttonBorderColor = if (it.isFocused) focusIndicatorColor else Color.Transparent
            }
            .semantics {
                // Key onClickLabel technique: Add a custom onClickLabel to Buttons using semantics,
                // not Modifier.clickable, because the later would add a second tab stop for the
                // Button (with different focus highlighting).
                if (onClickLabel != null) {
                    onClick(label = onClickLabel) { // activates in Assistive Technologies
                        // Note that the same onClick() handler is used both here and for the
                        // onClick parameter above, but here the lambda must return a Boolean
                        // indicating whether the action was successfully handled. This is only
                        // presumed to be true here.
                        onClick.invoke()
                        true
                    }
                }
            },
        enabled = enabled,
        shape = shape,
        colors = colors,
        // Key technique: Provide a border that is visible in the focused state.
        border = border ?: BorderStroke(
            width = 2.dp,
            color = buttonBorderColor
        ),
        interactionSource = interactionSource,
        content = content
    )
}

/**
 * VisibleFocusIndication - a factory method for a custom [IndicationInstance] that performs custom
 * focus state drawing over a composable. Apply this [Indication] to a composable with
 * Modifier.indication().
 *
 * [Indication]s provide low-level drawing control to indicate state, so they have a lot of power
 * and flexibility, but at the cost of considerable complexity. They also cannot directly access
 * Material Theme information, so the appropriate Dark or Light theme color much be passed in from
 * the calling site.
 *
 * For simple use cases (like this one), prefer applying state-dependant border or background
 * properties where possible.
 *
 * @param themedIndicationColor Theme-based color used to draw this [Indication]
 */
class VisibleFocusIndication(
    val themedIndicationColor: Color
) : Indication {
    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        // Key technique: collect the focus start and end Interactions on this composable's
        // InteractionSource as a State<Boolean> holding the current focus state value.
        val isFocusedState = interactionSource.collectIsFocusedAsState()
        return remember(interactionSource) {
            // Key technique: Pass the given theme-based color into the IndicationInstance.
            // Key technique: Pass the collected focus State holder (not it's current state value!)
            // into the IndicationInstance that will perform the focus-state-based drawing. That way
            // the IndicationInstance can retrieve the current focus state at time of drawing.
            VisibleFocusIndicationInstance(themedIndicationColor, isFocusedState)
        }
    }
}

/**
 * VisibleFocusIndicationInstance performs state-based rendering of a composable. Specifically, it
 * draws a rounded rectangle over a focused composable using a theme-appropriate color.
 *
 * @param themedIndicationColor Theme-based color used to draw the focus indicator
 * @param isFocusedState a State holder indicating if this composable is focused or not
 */
private class VisibleFocusIndicationInstance(
    val themedIndicationColor: Color,
    isFocusedState: State<Boolean>
) : IndicationInstance {
    private val isFocused by isFocusedState
    override fun ContentDrawScope.drawIndication() {
        drawContent()
        if (isFocused) {
            drawRoundRect(
                color = themedIndicationColor,
                size = size,
                cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
                style = Stroke(width = 2.dp.toPx()),
                alpha = 1.0f
            )
        }
    }
}