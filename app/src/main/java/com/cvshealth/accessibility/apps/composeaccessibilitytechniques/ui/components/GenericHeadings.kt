package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.collapse
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.CvsRed
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ErrorRed
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.SuccessGreen

@Composable
fun SimpleHeading(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(text,
        modifier = modifier
            .fillMaxWidth()
            .semantics { heading() },
        style = MaterialTheme.typography.headlineSmall
    )
}

@Preview(showBackground = true)
@Composable
fun SimpleHeadingHeadingLongPreview() {
    ComposeAccessibilityTechniquesTheme() {
        SimpleHeading(text = "This is a test. This is only a test. If this were a real heading, it would be shorter.")
    }
}

@Composable
fun GenericHeading(
    text: String,
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    tint: Color = LocalContentColor.current
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                heading()
            }
    ) {
        if (painter != null) {
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .defaultMinSize(24.dp, minHeight = 24.dp)
                    .align(Alignment.CenterVertically),
                tint = tint
            )
        }
        Text(text,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp)
        )
    }
}

@Composable
fun SuccessHeading(text: String, modifier: Modifier = Modifier) {
    GenericHeading(
        text = text,
        modifier = modifier
            .padding(top = 8.dp),
        painter = painterResource(id = R.drawable.ic_check_fill),
        tint = SuccessGreen
    )
}


@Preview(showBackground = true)
@Composable
fun SuccessHeadingShortPreview() {
    ComposeAccessibilityTechniquesTheme() {
        SuccessHeading(text = "This is a test")
    }
}

@Composable
fun ErrorHeading(text: String, modifier: Modifier = Modifier) {
    GenericHeading(
        text = text,
        modifier = modifier
            .padding(top = 8.dp),
        painter = painterResource(id = R.drawable.ic_close_fill),
        tint = ErrorRed
    )
}

@Preview(showBackground = true)
@Composable
fun ErrorHeadingLongPreview() {
    ComposeAccessibilityTechniquesTheme() {
        ErrorHeading(
            text = "This is a test. This is only a test. If this were a real heading, it would be shorter."
        )
    }
}

@Composable
fun GenericAccordionHeading(
    text: String,
    modifier: Modifier = Modifier,
    headingIconPainter: Painter? = null,
    headingIconTint: Color = LocalContentColor.current,
    expanderIconTint: Color = LocalContentColor.current,
    content: @Composable () -> Unit
) {
    val isExpanded = rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClickLabel = if (isExpanded.value)
                        stringResource(id = R.string.collapse_button_description)
                    else
                        stringResource(id = R.string.expand_button_description)
                ) {
                    isExpanded.value = !isExpanded.value
                }
                .semantics (mergeDescendants = true) {
                    heading()
                    if (isExpanded.value) {
                        collapse {
                            isExpanded.value = false
                            return@collapse true
                        }
                    } else {
                        expand {
                            isExpanded.value = true
                            return@expand true
                        }
                    }
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (headingIconPainter != null) {
                Icon(
                    painter = headingIconPainter,
                    contentDescription = null,
                    modifier = Modifier.defaultMinSize(24.dp, minHeight = 24.dp),
                    tint = headingIconTint
                )
            }
            Text(text,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            )
            Icon(
                painter = painterResource(
                    id = if (isExpanded.value)
                        R.drawable.ic_minus_fill
                    else
                        R.drawable.ic_plus_fill,
                ),
                contentDescription = null, // expand/collapsed is a state of the Row
                modifier = Modifier.minimumInteractiveComponentSize(),
                tint = expanderIconTint
            )
        }
        if (isExpanded.value) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GenericAccordionHeadingShortPreview() {
    ComposeAccessibilityTechniquesTheme() {
        GenericAccordionHeading(text = "This is a test accordion") {
            Column {
                BodyText(textId = R.string.accordion_item_2_1)
                BodyText(textId = R.string.accordion_item_2_2)
                BodyText(textId = R.string.accordion_item_2_3)
            }
        }
    }
}


@Composable
fun SuccessAccordionHeading(
    text: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    GenericAccordionHeading(
        text = text,
        modifier = modifier
            .padding(top = 8.dp),
        headingIconPainter = painterResource(id = R.drawable.ic_check_fill),
        headingIconTint = SuccessGreen,
        expanderIconTint = CvsRed
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun SuccessAccordionHeadingShortPreview() {
    ComposeAccessibilityTechniquesTheme() {
        SuccessAccordionHeading(
            text = "This is a success accordion heading"
        ) {
            Column {
                BodyText(textId = R.string.accordion_item_2_1)
                BodyText(textId = R.string.accordion_item_2_2)
                BodyText(textId = R.string.accordion_item_2_3)
            }
        }
    }
}