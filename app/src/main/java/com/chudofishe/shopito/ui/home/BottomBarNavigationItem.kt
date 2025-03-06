package com.chudofishe.shopito.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.graphics.vector.ImageVector
import com.chudofishe.shopito.navigation.BottomNavigationRoute

enum class BottomBarNavigationItem(
    val text: String,
    val route: BottomNavigationRoute,
    val icon: ImageVector
) {
    CURRENT("Current", BottomNavigationRoute.CurrentListRoute, Icons.Default.Home),
    RECENT("Recent", BottomNavigationRoute.RecentListsRoute, Icons.Default.Refresh);
}