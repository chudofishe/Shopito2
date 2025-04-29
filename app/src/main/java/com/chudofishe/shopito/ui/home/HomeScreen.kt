package com.chudofishe.shopito.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.chudofishe.shopito.R
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.navigation.ProfileNavigationRoute
import com.chudofishe.shopito.ui.home.profile.ProfileScreen
import com.chudofishe.shopito.ui.home.current_shoppinglist.ShoppingListScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onFabClicked: () -> Unit,
    onSignInRequest: () -> Unit,
    onSignOutRequest: () -> Unit,
    onDrawerItemSelected: (ProfileNavigationRoute?) -> Unit,
    onNavigateToAddItemScreen: (Category) -> Unit
) {

    val homeViewModel: HomeViewModel = koinViewModel()
    val state by homeViewModel.homeScreenState.collectAsState()
    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    val drawerState = rememberDrawerState(
        initialValue = if (state.isDrawerExpanded) DrawerValue.Open else DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.isDrawerExpanded) {
        if (state.isDrawerExpanded && drawerState.isClosed) {
            drawerState.open()
        } else if (!state.isDrawerExpanded && drawerState.isOpen) {
            drawerState.close()
        }
    }

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
                        scope.launch { homeViewModel.closeDrawer() }
                    },
                    onSignOut = {
                        scope.launch { homeViewModel.closeDrawer() }
                        onSignOutRequest()
                    },
                    onNavigateTo = onDrawerItemSelected,
                    onSignIn = onSignInRequest
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    title = state.currentListTitle,
                    scrollBehavior = topBarScrollBehavior,
                    onDeleteList = { showDeleteDialog = true },
                    onCompleteList = {

                    },
                    onMenuClicked = {
                        scope.launch {
                            if (drawerState.isClosed) {
                                homeViewModel.openDrawer()
                            } else {
                                homeViewModel.closeDrawer()
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
            ShoppingListScreen(
                modifier = Modifier.padding(paddingValues).nestedScroll(topBarScrollBehavior.nestedScrollConnection),
                onNavigateToAddItemScreenWithCategory = onNavigateToAddItemScreen
            )
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