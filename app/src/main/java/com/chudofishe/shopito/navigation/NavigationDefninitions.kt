package com.chudofishe.shopito.navigation

import com.chudofishe.shopito.model.Category
import kotlinx.serialization.Serializable

interface NavigationRoute

data object NavigateBack : NavigationRoute

sealed class TopLevelNavigationRoute : NavigationRoute {
    @Serializable
    data class HomeRoute(val showDrawer: Boolean = false) : TopLevelNavigationRoute()
    @Serializable
    data class AddItemRoute(val categoryToSelect: Category = Category.OTHER): TopLevelNavigationRoute()
    @Serializable
    data class ViewListRoute(val listId: Long) : TopLevelNavigationRoute()
}

sealed class ProfileNavigationRoute : NavigationRoute {
    @Serializable
    data object FriendsRoute: ProfileNavigationRoute()
    @Serializable
    data object FriendRequestsRoute : ProfileNavigationRoute()
    @Serializable
    data object RecentListsRoute : ProfileNavigationRoute()
}

