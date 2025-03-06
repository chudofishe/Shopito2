package com.chudofishe.shopito.ui.home

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.chudofishe.shopito.navigation.BottomNavigationRoute

@Composable
fun BottomBar(
    currentRoute: BottomNavigationRoute?,
    onNavItemSelected: (BottomNavigationRoute) -> Unit
) {
    NavigationBar {
        BottomBarNavigationItem.entries.forEach { destination ->
            NavigationBarItem(
                selected = destination.route == currentRoute,
                onClick = {
                    onNavItemSelected(destination.route)
                },
                icon = {
                    Icon(destination.icon, contentDescription = destination.name)
                },
                label = {
                    Text(destination.text)
                }
            )
        }
    }
}

