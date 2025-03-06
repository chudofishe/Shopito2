package com.chudofishe.shopito.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.chudofishe.shopito.R
import com.chudofishe.shopito.navigation.BottomNavigationRoute
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentRoute: BottomNavigationRoute,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    onFabClicked: () -> Unit,
    onNavItemSelected: (BottomNavigationRoute) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {

    val homeViewModel: HomeViewModel = koinViewModel()
    val state by homeViewModel.homeScreenState.collectAsState()

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

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
                    BottomNavigationRoute.RecentListsRoute -> "Recent"
                },
                scrollBehavior = topAppBarScrollBehavior,
                onDeleteList = { showDeleteDialog = true },
                onCompleteList = {

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
        }
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