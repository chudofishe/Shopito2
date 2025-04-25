package com.chudofishe.shopito.ui.add_list_item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chudofishe.shopito.ui.add_list_item.create_item.CreateItemScreen
import com.chudofishe.shopito.ui.add_list_item.create_item.CreateItemScreenState
import com.chudofishe.shopito.ui.add_list_item.recent_Items.RecentItemsScreen
import com.chudofishe.shopito.ui.theme.ShopitoTheme

private data class TabItem(
    val title: String,
    val unSelectedIcon: ImageVector,
    val selectedIcon: ImageVector
)

private val tabItem = listOf(
    TabItem(
        title = "Add new",
        unSelectedIcon = Icons.Outlined.AddCircle,
        selectedIcon = Icons.Filled.AddCircle
    ), TabItem(
        title = "Recent",
        unSelectedIcon = Icons.Outlined.Menu,
        selectedIcon = Icons.Filled.Menu
    )
)

@Composable
fun AddShoppingListItemScreen(
    onNavigateUp: () -> Unit
) {
    AddShoppingListItemScreenContent(
        onNavigateUp = {
            onNavigateUp.invoke()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShoppingListItemScreenContent(
    onNavigateUp: () -> Unit,
) {
    val pagerState = rememberPagerState {
        tabItem.size
    }

    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    val (onCheckClicked, setOnCheckClicked) = remember { mutableStateOf<(() -> Unit)?>(null) }

    LaunchedEffect(key1 = selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    LaunchedEffect(key1 = pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress)
            selectedTabIndex = pagerState.currentPage
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Add item")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigateUp.invoke()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                actions = {
                    if (selectedTabIndex == 0) {
                        IconButton(onClick = {
                            onCheckClicked?.invoke()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "done"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabItem.forEachIndexed { index, tabItem ->
                    LeadingIconTab(
                        selected = index == selectedTabIndex,
                        onClick = {
                            selectedTabIndex = index
                        },
                        text = { Text(text = tabItem.title) },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedTabIndex) {
                                    tabItem.selectedIcon
                                } else tabItem.unSelectedIcon,
                                contentDescription = tabItem.title
                            )
                        })
                }
            }
            HorizontalPager(
                state = pagerState, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { index ->
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                    contentAlignment = Alignment.Center) {
                    when (index) {
                        0 -> {
                            CreateItemScreen(
                                setOnCheckClicked = setOnCheckClicked,
                                onNavigateUp = onNavigateUp
                            )
                        }
                        else -> {
                            RecentItemsScreen(
                                onItemClicked = {
                                    onNavigateUp()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AddShoppingListItemScreenPreview(

) {
    ShopitoTheme {
        AddShoppingListItemScreenContent(
            {}
        )
    }
}
