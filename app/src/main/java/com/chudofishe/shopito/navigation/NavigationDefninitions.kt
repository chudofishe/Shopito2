package com.chudofishe.shopito.navigation

import com.chudofishe.shopito.model.Category
import kotlinx.serialization.Serializable

interface NavigationRoute

data object NavigateBack : NavigationRoute

sealed class BottomNavigationRoute : NavigationRoute {
    @Serializable
    data object CurrentListRoute : BottomNavigationRoute()
}

sealed class TopLevelNavigationRoute : NavigationRoute {
    @Serializable
    data object HomeRoute : TopLevelNavigationRoute()
    @Serializable
    data class AddItemRoute(val categoryToSelect: Category = Category.OTHER): TopLevelNavigationRoute()
    @Serializable
    data class ViewListRoute(val listId: Long) : TopLevelNavigationRoute()
}

sealed class ProfileNavigationRoute : TopLevelNavigationRoute() {
    @Serializable
    data object FriendsRoute: ProfileNavigationRoute()
    @Serializable
    data object FriendRequestsRoute : ProfileNavigationRoute()
    @Serializable
    data object RecentListsRoute : ProfileNavigationRoute()
}

