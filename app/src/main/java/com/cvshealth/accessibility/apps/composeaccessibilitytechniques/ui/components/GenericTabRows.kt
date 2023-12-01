/*
   Copyright 2023 CVS Health and/or one of its affiliates

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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.snapping.SnapFlingBehavior
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cvshealth.accessibility.apps.composeaccessibilitytechniques.ui.theme.ComposeAccessibilityTechniquesTheme
import kotlinx.coroutines.launch

/**
 * StatelessFixedTabGroup presents a fixed TabRow of String labels in Tab composables and displays
 * their corresponding tab pane content when each is selected. StatelessFixedTabGroup hoists the
 * selected tab state handling to its caller, so it is appropriate to use when the parent composable
 * must know that the tab selection has changed.
 *
 * @param tabIndex the current selected tab index
 * @param setTabIndex sets the current selected tab index
 * @param tabTitles the list of all tab titles; tabIndex must be within the range of this list
 * @param modifier Modifier of the TabRow
 * @param containerColor the color used for the background of this tab row. Use Color.Transparent to have no color.
 * @param contentColor the preferred color for content inside this tab row. Defaults to either the matching content color for containerColor, or to the current LocalContentColor if containerColor is not a color from the theme.
 * @param content displays the contents of a tab pane, given its index
 */
@Composable
fun StatelessFixedTabGroup(
    tabIndex: Int,
    setTabIndex: (Int) -> Unit,
    tabTitles: List<String>,
    modifier: Modifier = Modifier,
    containerColor: Color = TabRowDefaults.containerColor,
    contentColor: Color = TabRowDefaults.contentColor,
    content: @Composable (tabIndex: Int) -> Unit
) {
    Column {
        TabRow(
            selectedTabIndex = tabIndex,
            modifier = modifier,
            containerColor = containerColor,
            contentColor = contentColor
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { setTabIndex(index) },
                    text = { Text(text = title) }
                )
            }
        }
        content(tabIndex)
    }
}

@Preview(showBackground = true)
@Composable
fun StatelessFixedTabGroupPreview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            val (tabIndex, setTabIndex) = rememberSaveable { mutableStateOf(0) }
            StatelessFixedTabGroup(
                tabIndex = tabIndex,
                setTabIndex = setTabIndex,
                tabTitles = listOf("Tab Index 0", "Tab Index 1")
            ) { selectedTabIndex ->
                Text( "This is tab $selectedTabIndex.")
            }
        }
    }
}

/**
 * StatefulFixedTabGroup presents a fixed TabRow of String labels in Tab composables and displays
 * their corresponding tab pane content when each is selected. StatefulFixedTabGroup performs its
 * own selected tab state handling without informing its parent composable, so it is only applicable
 * when tab selection is a pure UI operation.
 *
 * rememberSaveable is used to hold tab selection state across configuration changes.
 *
 * @param tabTitles the list of all tab titles; tabIndex must be within the range of this list
 * @param modifier Modifier of the TabRow
 * @param initialTabIndex the tab index to be initially displays; defaults to the first tab (0)
 * @param containerColor the color used for the background of this tab row. Use Color.Transparent
 * to have no color.
 * @param contentColor the preferred color for content inside this tab row. Defaults to either the
 * matching content color for containerColor, or to the current LocalContentColor if containerColor
 * is not a color from the theme.
 * @param content displays the contents of a tab pane, given its index
 */
@Composable
fun StatefulFixedTabGroup(
    tabTitles: List<String>,
    modifier: Modifier = Modifier,
    initialTabIndex: Int = 0,
    containerColor: Color = TabRowDefaults.containerColor,
    contentColor: Color = TabRowDefaults.contentColor,
    content: @Composable (tabIndex: Int) -> Unit
) {
    val (tabIndex, setTabIndex) = rememberSaveable { mutableStateOf(initialTabIndex) }
    StatelessFixedTabGroup(
        tabIndex = tabIndex,
        setTabIndex = setTabIndex,
        tabTitles = tabTitles,
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        content = content)
}

@Preview(showBackground = true)
@Composable
fun StatefulFixedTabGroupPreview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            StatefulFixedTabGroup(
                tabTitles = listOf("Tab Index 0", "Tab Index 1"),
                initialTabIndex = 1,
            ) { selectedTabIndex ->
                Text( "This is tab $selectedTabIndex.")
            }
        }
    }
}

/**
 * StatelessScrollableTabGroup presents a ScrollableTabRow of String labels in Tab composables and
 * displays their corresponding tab pane content when each is selected. StatelessScrollableTabGroup
 * hoists the selected tab state handling to its caller, so it is appropriate to use when the parent
 * composable must know that the tab selection has changed.
 *
 * @param tabIndex the current selected tab index
 * @param setTabIndex sets the current selected tab index
 * @param tabTitles the list of all tab titles; tabIndex must be within the range of this list
 * @param modifier Modifier of the TabRow
 * @param containerColor the color used for the background of this tab row. Use Color.Transparent to have no color.
 * @param contentColor the preferred color for content inside this tab row. Defaults to either the matching content color for containerColor, or to the current LocalContentColor if containerColor is not a color from the theme.
 * @param content displays the contents of a tab pane, given its index
 */
@Composable
fun StatelessScrollableTabGroup(
    tabIndex: Int,
    setTabIndex: (Int) -> Unit,
    tabTitles: List<String>,
    modifier: Modifier = Modifier,
    containerColor: Color = TabRowDefaults.containerColor,
    contentColor: Color = TabRowDefaults.contentColor,
    content: @Composable (tabIndex: Int) -> Unit
) {
    Column {
        ScrollableTabRow(
            selectedTabIndex = tabIndex,
            modifier = modifier,
            containerColor = containerColor,
            contentColor = contentColor
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { setTabIndex(index) },
                    text = { Text(text = title) }
                )
            }
        }
        content(tabIndex)
    }
}

@Preview(showBackground = true)
@Composable
fun StatelessScrollableTabGroupPreview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            val (tabIndex, setTabIndex) = rememberSaveable { mutableStateOf(0) }
            StatelessScrollableTabGroup(
                tabIndex = tabIndex,
                setTabIndex = setTabIndex,
                tabTitles = listOf("Tab Index 0", "Tab Index 1", "Tab Index 2", "Tab Index 3")
            ) { selectedTabIndex ->
                Text( "This is tab $selectedTabIndex.")
            }
        }
    }
}

/**
 * StatefulScrollableTabGroup presents a ScrollableTabRow of String labels in Tab composables and
 * displays their corresponding tab pane content when each is selected. StatefulScrollableTabGroup
 * performs its own selected tab state handling without informing its parent composable, so it is
 * only applicable when tab selection is a pure UI operation.
 *
 * rememberSaveable is used to hold tab selection state across configuration changes.
 *
 * @param tabTitles the list of all tab titles; tabIndex must be within the range of this list
 * @param modifier Modifier of the TabRow
 * @param initialTabIndex the tab index to be initially displays; defaults to the first tab (0)
 * @param containerColor the color used for the background of this tab row. Use Color.Transparent
 * to have no color.
 * @param contentColor the preferred color for content inside this tab row. Defaults to either the
 * matching content color for containerColor, or to the current LocalContentColor if containerColor
 * is not a color from the theme.
 * @param content displays the contents of a tab pane, given its index
 */
@Composable
fun StatefulScrollableTabGroup(
    tabTitles: List<String>,
    modifier: Modifier = Modifier,
    initialTabIndex: Int = 0,
    containerColor: Color = TabRowDefaults.containerColor,
    contentColor: Color = TabRowDefaults.contentColor,
    content: @Composable (tabIndex: Int) -> Unit
) {
    val (tabIndex, setTabIndex) = rememberSaveable { mutableStateOf(initialTabIndex) }
    StatelessScrollableTabGroup(
        tabIndex = tabIndex,
        setTabIndex = setTabIndex,
        tabTitles = tabTitles,
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        content = content)
}

@Preview(showBackground = true)
@Composable
fun StatefulScrollableTabGroupPreview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            StatefulScrollableTabGroup(
                tabTitles = listOf("Tab Index 0", "Tab Index 1", "Tab Index 2", "Tab Index 3"),
                initialTabIndex = 1,
            ) { selectedTabIndex ->
                Text( "This is tab $selectedTabIndex.")
            }
        }
    }
}



/**
 * FixedPagedTabGroup presents a fixed TabRow of String labels in Tab composables and
 * displays their corresponding tab pane content within a HorizontalPager. FixedPagedTabGroup will
 * manage its own page state, unless a PagerState is provided by its caller.
 *
 * @param tabTitles the list of all tab titles; tabIndex must be within the range of this list
 * @param modifier Modifier of the TabRow
 * @param tabContainerColor the color used for the background of this tab row. Use Color.Transparent to have no color.
 * @param tabContentColor the preferred color for content inside this tab row. Defaults to either the matching content color for containerColor, or to the current LocalContentColor if containerColor is not a color from the theme.
 * @param pagerState the state of the HorizontalPager. Must supply the correct count of pages.
 * @param pagerContentPadding the padding around the entire tab pane pager.
 * @param pageSize determines how pages are laid out.
 * @param beyondBoundsPageCount pages before and after the selected tab pane to be composed.
 * @param pageSpacing spacing between pages.
 * @param verticalAlignment how pages are aligned vertically
 * @param flingBehavior how the tab panes react to user scroll gestures
 * @param userScrollEnabled whether the tab panes can be scrolled by user gestures (or only by the tabs)
 * @param reverseLayout reverses direction of layout and scrolling
 * @param pageKey returns a unique, stable item key for each page index value
 * @param pageNestedScrollConnection determines how nested lists act
 * @param pageContent displays the contents of a tab pane, given its page index in the context of a PagerScope
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FixedPagedTabGroup(
    tabTitles: List<String>,
    modifier: Modifier = Modifier,
    tabContainerColor: Color = TabRowDefaults.containerColor,
    tabContentColor: Color = TabRowDefaults.contentColor,
    pagerState: PagerState = rememberPagerState {
        tabTitles.size
    },
    pagerModifier: Modifier = Modifier,
    pagerContentPadding: PaddingValues = PaddingValues(0.dp),
    pageSize: PageSize = PageSize.Fill,
    beyondBoundsPageCount: Int = 0,
    pageSpacing: Dp = 0.dp,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    flingBehavior: SnapFlingBehavior = PagerDefaults.flingBehavior(state = pagerState),
    userScrollEnabled: Boolean = true,
    reverseLayout: Boolean = false,
    pageKey: ((index: Int) -> Any)? = null,
    pageNestedScrollConnection: NestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
        Orientation.Horizontal
    ),
    pageContent: @Composable PagerScope.(page: Int) -> Unit
) {
    // Key technique: HorizontalPager coordinates the TabRow selected tab using rememberPagerState
    // and its currentPage property and animateScrollToPage() method run within a coroutine scope.
    val pagerCoroutineScope = rememberCoroutineScope()
    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = modifier,
            containerColor = tabContainerColor,
            contentColor = tabContentColor
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        pagerCoroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(text = title) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = pagerModifier,
            contentPadding = pagerContentPadding,
            pageSize = pageSize,
            beyondBoundsPageCount = beyondBoundsPageCount,
            pageSpacing = pageSpacing,
            verticalAlignment = verticalAlignment,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
            reverseLayout = reverseLayout,
            key = pageKey,
            pageNestedScrollConnection = pageNestedScrollConnection,
            pageContent = pageContent
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun FixedPagedTabGroupPreview() {
    ComposeAccessibilityTechniquesTheme {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            FixedPagedTabGroup(
                tabTitles = listOf("Tab Index 0", "Tab Index 1")
            ) { pageIndex ->
                Text( "This is tab ${pageIndex}.")
            }
        }
    }
}