package com.chudofishe.shopito.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.ui.add_list_item.AddShoppingListItemScreen
import com.chudofishe.shopito.ui.friend_requests.FriendRequestsScreen
import com.chudofishe.shopito.ui.friends.FriendsScreen
import com.chudofishe.shopito.ui.home.HomeScreen
import com.chudofishe.shopito.ui.recent_lists.RecentShoppingListsScreen
import com.chudofishe.shopito.ui.shoppinglistview.ShoppingListViewScreen


fun NavGraphBuilder.homeScreenDestination(
    onNavigateToAddItemScreen: (Category) -> Unit,
    onSignInRequest: () -> Unit,
    onSignOutRequest: () -> Unit,
    onNavigateFromDrawer: (ProfileNavigationRoute?) -> Unit,
) {
    composable<TopLevelNavigationRoute.HomeRoute> {
        HomeScreen(
            onFabClicked = {
                onNavigateToAddItemScreen.invoke(Category.OTHER)
            },
            onSignInRequest = onSignInRequest,
            onSignOutRequest = onSignOutRequest,
            onDrawerItemSelected = onNavigateFromDrawer,
            onNavigateToAddItemScreen = onNavigateToAddItemScreen,
        )
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
    onNavigateToViewListScreen: (Long) -> Unit,
    onNavigateToHome: (showDrawer: Boolean) -> Unit
) {
    composable<ProfileNavigationRoute.RecentListsRoute> {
        RecentShoppingListsScreen(
            onNavigateUp = onNavigateUp,
            onNavigateToViewList = onNavigateToViewListScreen,
            onNavigateToHome = onNavigateToHome
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