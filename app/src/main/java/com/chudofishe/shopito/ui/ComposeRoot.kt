package com.chudofishe.shopito.ui

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.chudofishe.shopito.navigation.TopLevelNavigationRoute
import com.chudofishe.shopito.navigation.addItemScreenDestination
import com.chudofishe.shopito.navigation.friendRequestsScreenDestination
import com.chudofishe.shopito.navigation.friendsScreenDestination
import com.chudofishe.shopito.navigation.homeScreenDestination
import com.chudofishe.shopito.navigation.recentListsDestination
import com.chudofishe.shopito.navigation.viewShoppingListScreenDestination
import com.chudofishe.shopito.util.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun ComposeRoot() {
    val navController = rememberNavController()
    val rootViewModel = koinViewModel<RootViewModel>()
    val activity = LocalActivity.current
    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }

    ObserveAsEvents(rootViewModel.signInChannelFlow) {
        Toast.makeText(
            activity,
            "Sign in successful",
            Toast.LENGTH_LONG
        ).show()
    }

    ObserveAsEvents(rootViewModel.signOutChannelFlow) {
        Toast.makeText(
            activity,
            "Sign out successful",
            Toast.LENGTH_LONG
        ).show()
        navController.navigate(TopLevelNavigationRoute.HomeRoute)
    }

    NavHost(
        navController = navController,
        startDestination = TopLevelNavigationRoute.HomeRoute(),
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        }
    ) {
        homeScreenDestination(
            onNavigateToAddItemScreen = {
                navController.navigate(TopLevelNavigationRoute.AddItemRoute(categoryToSelect = it))
            },
            onSignInRequest = {
                activity?.let {
                    rootViewModel.signIn(it)
                }
            },
            onSignOutRequest = {
                activity?.let {
                    rootViewModel.signOut(it)
                }
            },
            onNavigateFromDrawer = {
                it?.let {
                    navController.navigate(it)
                }
            }
        )
        addItemScreenDestination(
            onNavigateUp = navigateUp
        )
        viewShoppingListScreenDestination(
            onNavigateUp = navigateUp
        )
        friendsScreenDestination(
            onNavigateUp = navigateUp
        )
        friendRequestsScreenDestination(
            onNavigateUp = navigateUp
        )
        recentListsDestination(
            onNavigateUp = navigateUp,
            onNavigateToViewListScreen = {
                navController.navigate(TopLevelNavigationRoute.ViewListRoute(it))
            },
            onNavigateToHome = {
                navController.navigate(
                    TopLevelNavigationRoute.HomeRoute(it),
                    navOptions {
                        popUpTo(TopLevelNavigationRoute.HomeRoute()) {
                            inclusive = true
                        }
                    }
                )
            }
        )
    }
}