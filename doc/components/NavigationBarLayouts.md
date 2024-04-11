# NavigationBar Layouts

The Material Design 3 [NavigationBar](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#NavigationBar(androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.unit.Dp,androidx.compose.foundation.layout.WindowInsets,kotlin.Function1)) and [NavigationBarItem](https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#(androidx.compose.foundation.layout.RowScope).NavigationBarItem(kotlin.Boolean,kotlin.Function0,kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Boolean,kotlin.Function0,kotlin.Boolean,androidx.compose.material3.NavigationBarItemColors,androidx.compose.foundation.interaction.MutableInteractionSource)) composables (or in Material Design 2, [BottomNavigation](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#bottomnavigation) and [BottomNavigationItem](https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#bottomnavigationitem)) create a horizontal row of three to five icons and text labels that control overall screen contents.

These standard controls announce their name, role, and value in accordance with the WCAG [Success Criterion 4.1.2 Name, Role, Value](https://www.w3.org/TR/WCAG22/#name-role-value) and correctly express their relationships according to WCAG [Success Criterion 1.3.1 Info and Relationships](https://www.w3.org/TR/WCAG22/#info-and-relationships). Custom approaches are likely to be less accessible; make sure any navigation bar item announces list semantics.

`NavigationBar` layouts operate like `TabRow` composables and are fully accessible by default. (`NavigationBarItems` even announce in TalkBack as "Tab".)

Take the following steps to implement a `NavigationBar`:

- Create a `NavHostController` with `rememberNavController()`.
- Remember which navigation bar item is selected (and preserve that value across configuration changes).
- In the `Scaffold` `bottomBar`, compose a `NavigationBar` and populate its content with `NavigationBarItem`s.
- For each `NavigationBarItem`:
    - Use `selected` to identify the currently selected `NavigationBarItem`.
    - In `onClick`, change the selected navigation bar item and perform screen navigation using `NavHostController.navigate()` and a route string.
    - Use `icon` to display an appropriate icon for the navigation bar item. 
        - Use a filled icon for the selected navigation bar item, and an outlined icon otherwise.
        - Best practice is to always specify an `Icon`'s `contentDescription`; however, not that it will only be used when a text label is not present or not displayed.
    - Use `label` to define the appropriate navigation bar item's text label.
        - Do not use `Text.maxLines` or `Modifier.height` to limit text display.
    - Use `alwaysShowLabel` to determine if the text label is displayed for unselected navigation bar items.
        - Prefer displaying a text label on all navigation bar items per Material Design guidelines.
        - Best practice is to omit this property, letting it default to `true`.
- In the `Scaffold` `content`, compose a `NavHost`, tie it to the remembered `NavHostController` and define a `composable` for each navigation bar item by route string.

For example:

```kotlin
// Controls navigation by associating the NavigationBar with a NavHost. Perform navigation in each
// NavigationBarItems' onClick method by calling pageNavController.navigate() with a route string.
val pageNavController = rememberNavController()

// NavigationBarItem data
data class NavItemData(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)
val navItemsData = listOf(
    NavItemData("Home", Icons.Filled.Home, Icons.Outlined.Home),
    NavItemData("Favorites", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
    NavItemData("Settings", Icons.Filled.Settings, Icons.Outlined.Settings),
)

Scaffold(
    // ...
    bottomBar = {
        // Remember the selected navigation item across configuration changes.
        var selectedItemIndex by rememberSaveable { mutableStateOf(0) }

        // Place the NavigationBar in the Scaffold's bottomBar so it is locked to the screen bottom.
        NavigationBar {
            // Populate the NavigationBar content with NavigationBarItem composeables.
            navItemsData.forEachIndexed { index, navItemData ->
                NavigationBarItem(
                    // Indicate the selected NavigationBarItem
                    selected = selectedItemIndex == index,
                    // On click, change the selected NavigationBarItem and perform navigation
                    onClick = {
                        selectedItemIndex = index
                        pageNavController.navigate(navItemData.label)
                    },
                    // Display the appropriate icon. Use a filled icon for the selected item.
                    icon = {
                        Icon(
                            imageVector = if (selectedItemIndex == 0)
                                navItemData.selectedIcon 
                            else
                                navItemData.unselectedIcon,
                            // Icon contentDescription will only be used if there is no label or the
                            // label is not shown. Best practice is to provide it.
                            contentDescription = navItemData.label
                        )
                    },
                    // Always provide a text label. Take care not to set maxLines or height limits.
                    label = { Text(navItemData.label) },
                )
            }
        }
    }
) {  paddingValues ->
    // Place the NavHost in the Scaffold's content.
    NavHost(
        navController = pageNavController,
        startDestination = "Home",
        modifier = Modifier.padding(paddingValues)
    ) {
        composable("Home") {
            // ... Home screen content
        }
        composable("Favorites") {
            // ... Favorites screen content
        }
        composable("Settings") {
            // ... Settings screen content
        }
    }
}
```

(Note: The hard-coded text shown in these examples is only used for simplicity. _Always_ use externalized string resource references in actual code.)

----

© Copyright 2024 CVS Health and/or one of its affiliates. All rights reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and
limitations under the License.