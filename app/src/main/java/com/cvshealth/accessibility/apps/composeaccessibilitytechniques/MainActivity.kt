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
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.RadioGroup
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.heading_semantics.HeadingSemanticsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.home.HomeScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.interactive_control_labels.InteractiveControlLabelsScreen
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAccessibilityTechniquesTheme(dynamicColor = false) {
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
        composable(route = ComposeAccessibilityTechniquesRoute.Home.route) {
            HomeScreen { route: ComposeAccessibilityTechniquesRoute ->
                navController.navigate(route.route)
            }
        }
        composable(route = ComposeAccessibilityTechniquesRoute.HeadingSemantics.route) {
            HeadingSemanticsScreen { navController.popBackStack() }
        }
        composable(route = ComposeAccessibilityTechniquesRoute.InteractiveControlLabels.route) {
            InteractiveControlLabelsScreen { navController.popBackStack() }
        }
        composable(route = ComposeAccessibilityTechniquesRoute.RadioGroupSample.route) {
            RadioGroupComponent { navController.popBackStack() }
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
    ComposeAccessibilityTechniquesTheme(dynamicColor = false) {
        RadioGroupComponent() {}
    }
}