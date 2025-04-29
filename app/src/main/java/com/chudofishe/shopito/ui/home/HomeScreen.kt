package com.chudofishe.shopito.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.chudofishe.shopito.R
import com.chudofishe.shopito.navigation.BottomNavigationRoute
import com.chudofishe.shopito.navigation.ProfileNavigationRoute
import com.chudofishe.shopito.ui.profile.ProfileScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentRoute: BottomNavigationRoute,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    onFabClicked: () -> Unit,
    onSignInRequest: () -> Unit,
    onSignOutRequest: () -> Unit,
    onDrawerItemSelected: (ProfileNavigationRoute?) -> Unit,
    onNavItemSelected: (BottomNavigationRoute) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {

    val homeViewModel: HomeViewModel = koinViewModel()
    val state by homeViewModel.homeScreenState.collectAsState()

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(),
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerContentColor = MaterialTheme.colorScheme.onSurface
            ) {
                ProfileScreen(
                    onNavigateUp = {
                        scope.launch { drawerState.close() }
                    },
                    onSignOut = {
                        scope.launch { drawerState.close() }
                        onSignOutRequest()
                    },
                    onNavigateTo = onDrawerItemSelected,
                    onSignIn = onSignInRequest
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                BottomBar(
                    currentRoute = currentRoute,
                    onNavItemSelected = onNavItemSelected
                )
            },
            topBar = {
                TopBar(
                    title = when (currentRoute) {
                        BottomNavigationRoute.CurrentListRoute -> state.currentListTitle
                    },
                    scrollBehavior = topAppBarScrollBehavior,
                    onDeleteList = { showDeleteDialog = true },
                    onCompleteList = {

                    },
                    onMenuClicked = {
                        scope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = {
                        Text(text = stringResource(R.string.add_item))
                    },
                    icon = {
                        Icon(Icons.Filled.Add, "Small floating action button.")
                    },
                    onClick = onFabClicked,
                    expanded = state.isFabExpanded
                )
            },
        ) { paddingValues ->
            if (showDeleteDialog) {
                Box {
                    DeleteListDialog(
                        onDismissRequest = {
                            showDeleteDialog = false
                        },
                        onConfirmClicked = {

                        }
                    )
                }
            }
            content(paddingValues)
        }
    }
}

@Composable
fun DeleteListDialog(
    onDismissRequest: () -> Unit,
    onConfirmClicked: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(Icons.Default.Delete, contentDescription = "Example Icon")
        },
        title = {
            Text(text = "Delete")
        },
        text = {
            Text(text = "Are you sure you want to delete current list?")
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onConfirmClicked
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Dismiss")
            }
        }
    )
}