package com.example.composeaccessibilitytechniques.ui.components

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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeaccessibilitytechniques.R
import com.example.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import com.example.composeaccessibilitytechniques.ui.theme.ErrorRed
import com.example.composeaccessibilitytechniques.ui.theme.SuccessGreen

@Composable
fun SimpleHeading(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(text,
        style = MaterialTheme.typography.headlineSmall,
        modifier = modifier
            .fillMaxWidth()
            .semantics { heading() }
    )
}

@Preview(showBackground = true)
@Composable
fun SimpleHeadingHeadingLongPreview() {
    ComposeAccessibilityTechniquesTheme(dynamicColor = false) {
        SimpleHeading(text = "This is a test. This is only a test. If this were a real heading, it would be shorter.")
    }
}

@Composable
fun GenericHeading(
    painter: Painter,
    text: String,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                heading()
            }
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .defaultMinSize(24.dp, minHeight = 24.dp)
                .align(Alignment.CenterVertically),
            tint = tint
        )
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
        painterResource(id = R.drawable.ic_check_fill),
        text = text,
        modifier = modifier
            .padding(top = 8.dp),
        tint = SuccessGreen
    )
}


@Preview(showBackground = true)
@Composable
fun SuccessHeadingShortPreview() {
    ComposeAccessibilityTechniquesTheme(dynamicColor = false) {
        SuccessHeading(text = "This is a test")
    }
}

@Composable
fun ErrorHeading(text: String, modifier: Modifier = Modifier) {
    GenericHeading(
        painterResource(id = R.drawable.ic_close_fill),
        text = text,
        modifier = modifier
            .padding(top = 8.dp),
        tint = ErrorRed
    )
}

@Preview(showBackground = true)
@Composable
fun ErrorHeadingLongPreview() {
    ComposeAccessibilityTechniquesTheme(dynamicColor = false) {
        ErrorHeading(text = "This is a test. This is only a test. If this were a real heading, it would be shorter.")
    }
}
