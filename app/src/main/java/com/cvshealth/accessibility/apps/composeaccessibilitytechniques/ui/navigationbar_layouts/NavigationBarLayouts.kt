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
package com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.navigationbar_layouts

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.R
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BadExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.BodyText
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericExampleTitle
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GenericScaffold
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.GoodExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.OkExampleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.SimpleHeading
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.components.visibleFocusBorder
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme

const val navigationBarLayoutsHeadingTestTag = "navigationBarLayoutsHeading"
const val navigationBarLayoutsExample1HeadingTestTag = "navigationBarLayoutsExample1Heading"
const val navigationBarLayoutsExample1NavigationBarTestTag = "navigationBarLayoutsExample1NavigationBar"
const val navigationBarLayoutsExample1NavigationHostTestTag = "navigationBarLayoutsExample1NavigationHost"
const val navigationBarLayoutsExample2HeadingTestTag = "navigationBarLayoutsExample2Heading"
const val navigationBarLayoutsExample2NavigationBarTestTag = "navigationBarLayoutsExample2NavigationBar"
const val navigationBarLayoutsExample2NavigationHostTestTag = "navigationBarLayoutsExample2NavigationHost"
const val navigationBarLayoutsExample3HeadingTestTag = "navigationBarLayoutsExample3Heading"
const val navigationBarLayoutsExample3NavigationBarTestTag = "navigationBarLayoutsExample3NavigationBar"
const val navigationBarLayoutsExample3NavigationHostTestTag = "navigationBarLayoutsExample3NavigationHost"

/**
 * Demonstrate accessibility techniques for [NavigationBar] layouts.
 *
 * Applies [GenericScaffold] to wrap the screen content.
 *
 * @param onBackPressed handler function for "Navigate Up" button
 */
@Composable
fun NavigationBarLayoutsScreen(
    onBackPressed: () -> Unit
) {
    GenericScaffold(
        title = stringResource(id = R.string.navigationbar_layouts_title),
        onBackPressed = onBackPressed,
    ) { modifier: Modifier ->
        val scrollState = rememberScrollState()
        Column(
            modifier = modifier
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            SimpleHeading(
                text = stringResource(id = R.string.navigationbar_layouts_heading),
                modifier = Modifier.testTag(navigationBarLayoutsHeadingTestTag)
            )
            BodyText(textId = R.string.navigationbar_layouts_description_1)
            BodyText(textId = R.string.navigationbar_layouts_description_2)

            BadExample1()
            HorizontalDivider(modifier = Modifier.padding(top = 8.dp))

            GoodExample2()
            HorizontalDivider(modifier = Modifier.padding(top = 8.dp))

            OkExample3()
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithScaffold() {
    ComposeAccessibilityTechniquesTheme {
        NavigationBarLayoutsScreen {}
    }
}

private data class NavigationItemData(
    @StringRes val labelStringId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String,
    @StringRes val bodyTextId: Int
)

private val navigationBarItemsData = listOf(
    NavigationItemData(
        labelStringId = R.string.navigationbar_layouts_tab_1_label,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        route = "Home",
        bodyTextId = R.string.navigationbar_layouts_tab_1_description_1
    ),
    NavigationItemData(
        labelStringId = R.string.navigationbar_layouts_tab_2_label,
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder,
        route = "Favorites",
        bodyTextId = R.string.navigationbar_layouts_tab_2_description_1
    ),
    NavigationItemData(
        labelStringId = R.string.navigationbar_layouts_tab_3_label,
        selectedIcon = Icons.Filled.Notifications,
        unselectedIcon = Icons.Outlined.Notifications,
        route = "Notifications",
        bodyTextId = R.string.navigationbar_layouts_tab_3_description_1
    ),
    NavigationItemData(
        labelStringId = R.string.navigationbar_layouts_tab_4_label,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        route = "Settings",
        bodyTextId = R.string.navigationbar_layouts_tab_4_description_1
    ),
)

@Composable
private fun BadExample1() {
    // Bad example 1: NavigationBar layout with limited text lines

    val exampleNavController = rememberNavController()
    BadExampleHeading(
        text = stringResource(id = R.string.navigationbar_layouts_example_1_header),
        modifier = Modifier.testTag(navigationBarLayoutsExample1HeadingTestTag)
    )

    NavHost(
        navController = exampleNavController,
        startDestination = navigationBarItemsData[0].route,
        modifier = Modifier
            .testTag(navigationBarLayoutsExample1NavigationHostTestTag)
            .padding(top = 8.dp)
    ) {
        navigationBarItemsData.forEach { navigationItemData ->
            composable(navigationItemData.route) {
                Column {
                    GenericExampleTitle(textId = navigationItemData.labelStringId)
                    BodyText(navigationItemData.bodyTextId)
                }
            }
        }
    }

    // Note: To construct a standard bottom-positioned NavigationBar, put the following code in a
    // composable assigned to the Scaffold's bottomBar property. This is not done here so that
    // multiple NavigationBar examples can be shown on the same screen.

    // Key technique: Track the currently selected NavigationBarItem by index.
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar(
        modifier = Modifier
            .testTag(navigationBarLayoutsExample1NavigationBarTestTag)
            .padding(top = 8.dp)
    ) {
        navigationBarItemsData.forEachIndexed { index, navigationItemData ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    exampleNavController.navigate(navigationItemData.route)
                },
                icon = {
                    Icon(
                        // Display filled icon for selected navigation bar items and an outlined
                        // icon otherwise.
                        imageVector = if (selectedItemIndex == index)
                            navigationItemData.selectedIcon
                        else
                            navigationItemData.unselectedIcon,
                        // Good practice suggests always providing an Icon contentDescription, but
                        // it will only be used if there is no label or when the label is not shown.
                        // In this example, the contentDescription will not be used, since the text
                        // label is always displayed.
                        contentDescription = stringResource(id = navigationItemData.labelStringId)
                    )
                },
                // Optional technique: Enhance focus visibility with a custom focus indicator.
                modifier = Modifier.visibleFocusBorder(),
                // Key technique: Always provide a text label.
                label = {
                    Text(
                        text = stringResource(id = navigationItemData.labelStringId),
                        // Key error: Limiting the navigation bar item label text height with
                        // maxLines = 1 (or by setting Modifier.height()) prevents the text from
                        // reflowing onto multiple lines properly.
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                alwaysShowLabel = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BadExample1Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            BadExample1()
        }
    }
}

@Composable
private fun GoodExample2() {
    // Good example 2: NavigationBar layout with text reflow

    val exampleNavController = rememberNavController()
    GoodExampleHeading(
        text = stringResource(id = R.string.navigationbar_layouts_example_2_header),
        modifier = Modifier.testTag(navigationBarLayoutsExample2HeadingTestTag)
    )

    NavHost(
        navController = exampleNavController,
        startDestination = navigationBarItemsData[0].route,
        modifier = Modifier
            .testTag(navigationBarLayoutsExample2NavigationHostTestTag)
            .padding(top = 8.dp)
    ) {
        navigationBarItemsData.forEach { navigationItemData ->
            composable(navigationItemData.route) {
                Column {
                    GenericExampleTitle(textId = navigationItemData.labelStringId)
                    BodyText(navigationItemData.bodyTextId)
                }
            }
        }
    }

    // Note: To construct a standard bottom-positioned NavigationBar, put the following code in a
    // composable assigned to the Scaffold's bottomBar property. This is not done here so that
    // multiple NavigationBar examples can be shown on the same screen.

    // Key technique: Track the currently selected NavigationBarItem by index.
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar(
        modifier = Modifier
            .testTag(navigationBarLayoutsExample2NavigationBarTestTag)
            .padding(top = 8.dp)
    ) {
        navigationBarItemsData.forEachIndexed { index, navigationItemData ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    exampleNavController.navigate(navigationItemData.route)
                },
                icon = {
                    Icon(
                        // Display filled icon for selected navigation bar items and an outlined
                        // icon otherwise.
                        imageVector = if (selectedItemIndex == index)
                            navigationItemData.selectedIcon
                        else
                            navigationItemData.unselectedIcon,
                        // Good practice suggests always providing an Icon contentDescription, but
                        // it will only be used if there is no label or when the label is not shown.
                        // In this example, the contentDescription will not be used, since the text
                        // label is always displayed.
                        contentDescription = stringResource(id = navigationItemData.labelStringId),
                    )
                },
                // Optional technique: Enhance focus visibility with a custom focus indicator.
                modifier = Modifier.visibleFocusBorder(),
                // Key technique: Always provide a text label.
                label = { Text(stringResource(id = navigationItemData.labelStringId)) },
                alwaysShowLabel = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GoodExample2Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            GoodExample2()
        }
    }
}

@Composable
private fun OkExample3() {
    // OK example 3: NavigationBar layout with only selected item labeled

    val exampleNavController = rememberNavController()
    OkExampleHeading(
        text = stringResource(id = R.string.navigationbar_layouts_example_3_header),
        modifier = Modifier.testTag(navigationBarLayoutsExample3HeadingTestTag)
    )

    NavHost(
        navController = exampleNavController,
        startDestination = navigationBarItemsData[0].route,
        modifier = Modifier
            .testTag(navigationBarLayoutsExample3NavigationHostTestTag)
            .padding(top = 8.dp)
    ) {
        navigationBarItemsData.forEach { navigationItemData ->
            composable(navigationItemData.route) {
                Column {
                    GenericExampleTitle(textId = navigationItemData.labelStringId)
                    BodyText(navigationItemData.bodyTextId)
                }
            }
        }
    }

    // Note: To construct a standard bottom-positioned NavigationBar, put the following code in a
    // composable assigned to the Scaffold's bottomBar property. This is not done here so that
    // multiple NavigationBar examples can be shown on the same screen.

    // Key technique: Track the currently selected NavigationBarItem by index.
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar(
        modifier = Modifier
            .testTag(navigationBarLayoutsExample3NavigationBarTestTag)
            .padding(top = 8.dp)
    ) {
        navigationBarItemsData.forEachIndexed { index, navigationItemData ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    exampleNavController.navigate(navigationItemData.route)
                },
                icon = {
                    Icon(
                        // Display filled icon for selected navigation bar items and an outlined
                        // icon otherwise.
                        imageVector = if (selectedItemIndex == index)
                            navigationItemData.selectedIcon
                        else
                            navigationItemData.unselectedIcon,
                        // Good practice suggests always providing an Icon contentDescription, but
                        // it will only be used if there is no label or when the label is not shown.
                        // In this example, the contentDescription will be used, since the text
                        // label is only displayed when selected.
                        contentDescription = stringResource(id = navigationItemData.labelStringId)
                    )
                },
                // Optional technique: Enhance focus visibility with a custom focus indicator.
                modifier = Modifier.visibleFocusBorder(),
                // Key technique: Always provide a text label.
                label = { Text(stringResource(id = navigationItemData.labelStringId)) },
                // Optional technique: Only show the text label for the selected navigation bar
                // item. This technique is OK, but not encouraged. It makes the selected item more
                // obvious, but makes the unselected navigation bar items less clear to users.
                // Prefer displaying text labels on all navigation bar items whenever possible.
                alwaysShowLabel = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OkExample3Preview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            OkExample3()
        }
    }
}