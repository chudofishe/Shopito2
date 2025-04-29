package com.chudofishe.shopito.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.ui.add_list_item.AddShoppingListItemScreen
import com.chudofishe.shopito.ui.friend_requests.FriendRequestsScreen
import com.chudofishe.shopito.ui.friends.FriendsScreen
import com.chudofishe.shopito.ui.home.HomeScreen
import com.chudofishe.shopito.ui.recent_lists.RecentShoppingListsScreen
import com.chudofishe.shopito.ui.shoppinglist.ShoppingListScreen
import com.chudofishe.shopito.ui.shoppinglistview.ShoppingListViewScreen

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.homeScreenDestination(
    onNavigateToAddItemScreen: (Category) -> Unit,
    onSignInRequest: () -> Unit,
    onSignOutRequest: () -> Unit,
    onNavigateFromDrawer: (ProfileNavigationRoute?) -> Unit,
    onExitApp: () -> Unit
) {
    composable<TopLevelNavigationRoute.HomeRoute> {
        val homeNavController = rememberNavController()
        val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.let {
            when (it.route) {
                BottomNavigationRoute.CurrentListRoute::class.qualifiedName -> BottomNavigationRoute.CurrentListRoute
                else -> null
            }
        } ?: BottomNavigationRoute.CurrentListRoute

        val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

        BackHandler {
            if (currentRoute == BottomNavigationRoute.CurrentListRoute) {
                onExitApp()
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
                }
            },
            onSignInRequest = onSignInRequest,
            onNavItemSelected = {
                homeNavController.navigate(it) {
                    popUpTo(homeNavController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onSignOutRequest = onSignOutRequest,
            onDrawerItemSelected = onNavigateFromDrawer,
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
            }
        }
    }
}

fun NavGraphBuilder.addItemScreenDestination(
    onNavigateUp: () -> Unit
) {
    composable<TopLevelNavigationRoute.AddItemRoute> {
        AddShoppingListItemScreen(
            onNavigateUp = onNavigateUp
        )
    }
}

fun NavGraphBuilder.viewShoppingListScreenDestination(
    onNavigateUp: () -> Unit
) {
    composable<TopLevelNavigationRoute.ViewListRoute> {
        ShoppingListViewScreen(
            onNavigateUp = onNavigateUp
        )
    }
}


fun NavGraphBuilder.friendsScreenDestination(
    onNavigateUp: () -> Unit
) {
    composable<ProfileNavigationRoute.FriendsRoute> {
        FriendsScreen(
            onNavigateUp = onNavigateUp
        )
    }
}

fun NavGraphBuilder.recentListsDestination(
    onNavigateUp: () -> Unit,
    onNavigateToViewListScreen: (Long) -> Unit
) {
    composable<ProfileNavigationRoute.RecentListsRoute> {
        RecentShoppingListsScreen(
            onNavigateUp = onNavigateUp,
            onNavigateToViewList = onNavigateToViewListScreen
        )
    }
}

fun NavGraphBuilder.friendRequestsScreenDestination(
    onNavigateUp: () -> Unit
) {
    composable<ProfileNavigationRoute.FriendRequestsRoute> {
        FriendRequestsScreen(
            onNavigateUp = onNavigateUp
        )
    }
}