package com.chudofishe.shopito.ui

import android.widget.Toast
import androidx.activity.compose.LocalActivity
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
import com.chudofishe.shopito.util.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun ComposeRoot() {
    val rootNavController = rememberNavController()
    val rootViewModel = koinViewModel<RootViewModel>()
    val activity = LocalActivity.current

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
        rootNavController.navigate(TopLevelNavigationRoute.HomeRoute)
    }

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
            onSignInRequest = {
                activity?.let {
                    rootViewModel.signIn(it)
                }
            },
            onExitApp = {
                activity?.finish()
            }
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
                activity?.let {
                    rootViewModel.signOut(it)
                }
            }
        )
    }
}