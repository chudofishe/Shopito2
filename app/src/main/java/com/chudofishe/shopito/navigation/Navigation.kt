package com.chudofishe.shopito.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.ui.add_list_item.AddShoppingListItemScreen
import com.chudofishe.shopito.ui.home.HomeScreen
import com.chudofishe.shopito.ui.recent_lists.RecentShoppingListsScreen
import com.chudofishe.shopito.ui.shoppinglist.ShoppingListScreen


@Composable
fun ComposeNavigation(

) {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = TopLevelNavigationRoute.HomeRoute,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        homeScreenDestination(
            onNavigateToAddItemScreen = {
                rootNavController.navigate(TopLevelNavigationRoute.AddItemRoute(categoryToSelect = it))
            }
        )
        addItemScreenDestination(
            onNavigateUp = {
                rootNavController.navigateUp()
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
private fun NavGraphBuilder.homeScreenDestination(
    onNavigateToAddItemScreen: (Category) -> Unit
) {
    composable<TopLevelNavigationRoute.HomeRoute> {
        val homeNavController = rememberNavController()
        val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.let {
            when (it.route) {
                BottomNavigationRoute.CurrentListRoute::class.qualifiedName -> BottomNavigationRoute.CurrentListRoute
                BottomNavigationRoute.RecentListsRoute::class.qualifiedName -> BottomNavigationRoute.RecentListsRoute
                else -> null
            }
        } ?: BottomNavigationRoute.CurrentListRoute

        val activity = LocalContext.current as Activity
        val (recentListsFabOnClick, setRecentListsFabOnClick) = remember { mutableStateOf<(() -> Unit)?>(null) }
        val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

        BackHandler {
            if (currentRoute == BottomNavigationRoute.CurrentListRoute) {
                activity.finish()
            }
        }

        HomeScreen(
            currentRoute = currentRoute,
            topAppBarScrollBehavior = topBarScrollBehavior,
            onFabClicked = {
                when (currentRoute) {
                    BottomNavigationRoute.CurrentListRoute -> {
                        onNavigateToAddItemScreen.invoke(Category.OTHER)
                    }

                    BottomNavigationRoute.RecentListsRoute -> {
                        recentListsFabOnClick?.invoke()
                    }
                }
            },
            onNavItemSelected = {
                homeNavController.navigate(it) {
                    popUpTo(homeNavController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = homeNavController,
                startDestination = BottomNavigationRoute.CurrentListRoute,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable<BottomNavigationRoute.CurrentListRoute> {
                    ShoppingListScreen(
                        modifier = Modifier.nestedScroll(topBarScrollBehavior.nestedScrollConnection),
                        onNavigateToAddItemScreenWithCategory = onNavigateToAddItemScreen
                    )
                }
                composable<BottomNavigationRoute.RecentListsRoute> {
                    RecentShoppingListsScreen(
                        modifier = Modifier.nestedScroll(topBarScrollBehavior.nestedScrollConnection),
                        setRecentListsFabOnClick = setRecentListsFabOnClick,
                        onNavigateToCurrentList = {
                            homeNavController.navigate(BottomNavigationRoute.CurrentListRoute)
                        }
                    )
                }
            }
        }
    }
}

private fun NavGraphBuilder.addItemScreenDestination(
    onNavigateUp: () -> Unit
) {
    composable<TopLevelNavigationRoute.AddItemRoute> {
        AddShoppingListItemScreen(
            onNavigateUp = onNavigateUp
        )
    }
}