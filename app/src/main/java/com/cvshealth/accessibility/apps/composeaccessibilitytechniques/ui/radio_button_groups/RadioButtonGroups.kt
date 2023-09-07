package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.radio_button_groups

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BadExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.RadioButtonGroup
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme


const val radioButtonGroupsHeadingTestTag = "radioButtonGroupsHeading"
const val radioButtonGroupsExample1HeadingTestTag = "radioButtonGroupsExample1Heading"
const val radioButtonGroupsExample1RadioButtonGroupTestTag = "radioButtonGroupsExample1RadioButtonGroup"
const val radioButtonGroupsExample2HeadingTestTag = "radioButtonGroupsExample2Heading"
const val radioButtonGroupsExample2RadioButtonGroupTestTag = "radioButtonGroupsExample2RadioButtonGroup"

@Composable
fun RadioButtonGroupsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.radio_button_groups_title),
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()
        val options = listOf(
            stringResource(id = R.string.radio_button_groups_option_1),
            stringResource(id = R.string.radio_button_groups_option_2),
            stringResource(id = R.string.radio_button_groups_option_3)
        )

        Column(modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.radio_button_groups_heading),
                modifier = Modifier.testTag(radioButtonGroupsHeadingTestTag)
            )
            BodyText(textId = R.string.radio_button_groups_description_1)
            BodyText(textId = R.string.radio_button_groups_description_2)

            BadExampleHeading(
                text = stringResource(id = R.string.radio_button_groups_example_1_header),
                modifier = Modifier.testTag(radioButtonGroupsExample1HeadingTestTag)
            )
            Spacer(modifier = Modifier.height(8.dp))
            val (example1Selection, setExample1Selection) = remember { mutableStateOf(0) }
            FauxRadioButtonGroup(
                groupLabel = stringResource(id = R.string.radio_button_groups_group_label),
                itemLabels = options,
                selectedIndex = example1Selection,
                selectHandler = setExample1Selection,
                modifier = Modifier.testTag(radioButtonGroupsExample1RadioButtonGroupTestTag)
            )

            GoodExampleHeading(
                text = stringResource(id = R.string.radio_button_groups_example_2_header),
                modifier = Modifier.testTag(radioButtonGroupsExample2HeadingTestTag)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Important technique: RadioButton group state has been hoisted up to this parent
            // composable. The state value and a state mutator function lambda are passed down to
            // the RadioButtonGroup() composable.
            val (example2Selection, setExample2Selection) = remember { mutableStateOf(0) }
            // Key accessibility techniques are described in components/RadioButtonGroup.kt.
            RadioButtonGroup(
                groupLabel = stringResource(id = R.string.radio_button_groups_group_label),
                itemLabels = options,
                selectedIndex = example2Selection,
                selectHandler = setExample2Selection,
                modifier = Modifier.testTag(radioButtonGroupsExample2RadioButtonGroupTestTag)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        RadioButtonGroupsScreen {}
    }
}

@Composable
fun FauxRadioButtonGroup(
    groupLabel: String,
    itemLabels: List<String>,
    selectedIndex: Int,
    selectHandler: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(groupLabel)
        // Key mistake 1: The following Column needs Modifier.selectableGroup().
        Column {
            itemLabels.forEachIndexed { index: Int, label: String ->
                // Key mistake 2: The following Row needs Modifier.selectable() with role =
                // Role.RadioButton. That modifier would merge the child semantics, associating the
                // RadioButton control with its label, as well as making the entire Row a touch
                // target.
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Key mistake #3: Related to mistake #2, this RadioButton should not do its own
                    // onClick handling; that should be done at the Row level only. (If onClick
                    // handling is also retained here, the RodioButton would become separately
                    // focusable from the Row in assistive technologies like TalkBack and Switch
                    // Access.)
                    RadioButton(
                        selected = (selectedIndex == index),
                        onClick = { selectHandler(index) }
                    )
                    Text(text = label, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}