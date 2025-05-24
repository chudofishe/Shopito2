package com.chudofishe.shopito.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.navigation.ProfileNavigationRoute
import com.chudofishe.shopito.ui.composables.HomeBottomSheet
import com.chudofishe.shopito.ui.home.current_shoppinglist.CategoryPickerDialog
import com.chudofishe.shopito.ui.home.current_shoppinglist.EmptyListPreview
import com.chudofishe.shopito.ui.home.profile.ProfileScreen
import com.chudofishe.shopito.ui.home.current_shoppinglist.ShoppingListScreen
import com.chudofishe.shopito.util.ObserveAsEvents
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

private enum class HomeScreenDialog {
    DELETE_LIST,
    PICK_CATEGORY
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSignInRequest: () -> Unit,
    onSignOutRequest: () -> Unit,
    onDrawerItemSelected: (ProfileNavigationRoute?) -> Unit,
    onNavigateToAddItemScreen: (Category) -> Unit
) {

    val homeViewModel: HomeViewModel = koinViewModel()
    val screenState by homeViewModel.homeScreenState.collectAsState()
    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()

    var currentDialog by remember {
        mutableStateOf<HomeScreenDialog?>(null)
    }

    var itemInputRequestFocus by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true
        )
    )

    ObserveAsEvents(homeViewModel.showDrawerChannelFlow) { show ->
        scope.launch {
            if (show) {
                drawerState.open()
            } else {
                drawerState.close()
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerContentColor = MaterialTheme.colorScheme.onSurface
            ) {
                ProfileScreen(
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
        BottomSheetScaffold(
            scaffoldState = bottomSheetState,
            sheetPeekHeight = 120.dp,
            sheetContent = {
                HomeBottomSheet(
                    onAddButtonClicked = homeViewModel::addItem,
                    onCategoryButtonClicked = {
                        currentDialog = HomeScreenDialog.PICK_CATEGORY
                    },
                    selectedCategory = screenState.selectedCategory,
                    textInputValue = screenState.itemNameInput,
                    onTextInputValueChanged = {
                        homeViewModel.updateItemNameInput(it)
                    },
                    shouldRequestFocus = itemInputRequestFocus,
                    onFocusRequested = { itemInputRequestFocus = false }
                )
            },
            topBar = {
                TopBar(
                    title = screenState.currentListTitle,
                    scrollBehavior = topBarScrollBehavior,
                    onDeleteList = { currentDialog = HomeScreenDialog.DELETE_LIST },
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
        ) { paddingValues ->
            when (currentDialog) {
                HomeScreenDialog.DELETE_LIST -> {
                    Box {
                        DeleteListDialog(
                            onDismissRequest = {
                                currentDialog = null
                            },
                            onConfirmClicked = {
                            }
                        )
                    }

                }
                HomeScreenDialog.PICK_CATEGORY -> {
                    Box {
                        CategoryPickerDialog(
                            onDismissRequest = {
                                currentDialog = null
                            },
                            onCategorySelected = {
                                homeViewModel.setSelectedCategory(it)
                                currentDialog = null
                                itemInputRequestFocus = true
                            }
                        )
                    }
                }
                null -> {

                }
            }
            screenState.shoppingList?.let {
                ShoppingListScreen(
                    modifier = Modifier
                        .padding(paddingValues)
                        .nestedScroll(topBarScrollBehavior.nestedScrollConnection),
                    onNavigateToAddItemScreenWithCategory = onNavigateToAddItemScreen,
                    onItemRemoved = {
                        homeViewModel.removeItem(it)
                    },
                    onItemClicked = {
                        homeViewModel.updateItem(it, it.category == it.currentCategory)
                    },
                    onCategoryCollapseStateToggled = {
                        homeViewModel.toggleCategoryCollapsedState(it)
                    },
                    shoppingList = it,
                    collapsedCategories = screenState.collapsedCategories
                )
            } ?: run {
                EmptyListPreview(
                    showCompleteAnimation = screenState.showCompleteAnimation,
                    onAnimationFinished = {
                        homeViewModel.hideCompleteAnimation()
                    }
                )
            }
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