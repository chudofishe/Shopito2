package com.chudofishe.shopito

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.chudofishe.shopito.navigation.TopLevelNavigationRoute
import com.chudofishe.shopito.navigation.addItemScreenDestination
import com.chudofishe.shopito.navigation.homeScreenDestination
import com.chudofishe.shopito.navigation.profileScreenDestination
import com.chudofishe.shopito.navigation.viewShoppingListScreenDestination

@Composable
fun ComposeApp(
    signInRequest: () -> Unit,
    signOutRequest: () -> Unit,
    onExitApp: () -> Unit
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
            },
            onNavigateToViewListScreen = {
                rootNavController.navigate(TopLevelNavigationRoute.ViewListRoute(it))
            },
            onNavigateToProfileScreen = {
                rootNavController.navigate(TopLevelNavigationRoute.ProfileRoute)
            },
            onSignInRequest = signInRequest,
            onExitApp = onExitApp
        )
        addItemScreenDestination(
            onNavigateUp = {
                rootNavController.navigateUp()
            }
        )
        viewShoppingListScreenDestination(
            onNavigateUp = {
                rootNavController.navigateUp()
            }
        )
        profileScreenDestination(
            onNavigateUp = {
                rootNavController.navigateUp()
            },
            onSignOut = {
                signOutRequest()
                rootNavController.navigate(TopLevelNavigationRoute.HomeRoute)
            }
        )
    }
}