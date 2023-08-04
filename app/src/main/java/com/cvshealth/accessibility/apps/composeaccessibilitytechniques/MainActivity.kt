package com.cvshealth.accessibility.apps.composeaccessibilitytechniques

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.accordion_controls.AccordionControlsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.RadioGroup
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_group_replacement.ContentGroupReplacementScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.content_grouping.ContentGroupingScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.heading_semantics.HeadingSemanticsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.home.HomeScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.InteractiveControlLabelsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.list_semantics.ListSemanticsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.text_alternatives.TextAlternativesScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAccessibilityTechniquesTheme() {
                val navController = rememberNavController()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ComposeAccessibilityTechniquesNavHost(navController)
                }
            }
        }
    }
}

@Composable
fun ComposeAccessibilityTechniquesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        val popBackStack: () -> Unit = { navController.popBackStack() }
        composable(route = ComposeAccessibilityTechniquesRoute.Home.route) {
            HomeScreen { route: ComposeAccessibilityTechniquesRoute ->
                navController.navigate(route.route)
            }
        }
        composable(route = ComposeAccessibilityTechniquesRoute.TextAlternatives.route) {
            TextAlternativesScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.ContentGrouping.route) {
            ContentGroupingScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.ContentGroupReplacement.route) {
            ContentGroupReplacementScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.HeadingSemantics.route) {
            HeadingSemanticsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.ListSemantics.route) {
            ListSemanticsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.InteractiveControlLabels.route) {
            InteractiveControlLabelsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.AccordionControls.route) {
            AccordionControlsScreen(onBackPressed = popBackStack)
        }
        composable(route = ComposeAccessibilityTechniquesRoute.RadioGroupSample.route) {
            RadioGroupComponent(onBackPressed = popBackStack)
        }
    }
}



@Composable
fun RadioGroupComponent(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = "Radio group component",
        onBackPressed = onBackPressed
    ) { modifier: Modifier ->
        val options = listOf( "Banana", "Grape", "Orange")
        val selectedOption = remember { mutableStateOf(0) }
        Column(modifier =  modifier.padding(horizontal = 16.dp)) {
            RadioGroup(
                groupLabel = "Pick a fruit:",
                itemLabels = options,
                current = selectedOption.value,
                selectHandler = { selectedOption.value = it }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RadioGroupSamplePreview() {
    ComposeAccessibilityTechniquesTheme() {
        RadioGroupComponent() {}
    }
}