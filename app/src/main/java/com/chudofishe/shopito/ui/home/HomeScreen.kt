package com.chudofishe.shopito.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chudofishe.shopito.R
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.navigation.ProfileNavigationRoute
import com.chudofishe.shopito.navigation.TopLevelNavigationRoute.HomeRoute
import com.chudofishe.shopito.navigation.TopLevelNavigationRoute.ViewListRoute
import com.chudofishe.shopito.ui.composables.CategoryChip
import com.chudofishe.shopito.ui.home.profile.ProfileScreen
import com.chudofishe.shopito.ui.home.current_shoppinglist.ShoppingListScreen
import com.chudofishe.shopito.util.ObserveAsEvents
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
    val scope = rememberCoroutineScope()

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(Icons.Default.DateRange, contentDescription = "Category")
                        }
                        TextField(
                            value = "",
                            onValueChange = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            placeholder = { Text("Add item") },
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                errorContainerColor = Color.Transparent,
                            ),
                            singleLine = true
                        )
                        IconButton(
                            onClick = onFabClicked
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }
                    // Для превью показываем оба состояния
                    // В реальном компоненте используйте AnimatedVisibility с проверкой состояния
                    Column(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = "",
                            onValueChange = { },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Description") },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent
                            ),
                            singleLine = true
                        )

                        // Категории
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            CategoryChip(
                                icon = R.drawable.icons8_babys_room_100,
                                text = "Kids"
                            )
                            CategoryChip(
                                icon = R.drawable.icons8_bread_100,
                                text = "Bakery"
                            )
                            CategoryChip(
                                icon = R.drawable.icons8_cola_100,
                                text = "Drinks"
                            )
                            CategoryChip(
                                icon = R.drawable.icons8_corgi_100,
                                text = "Pets"
                            )
                        }

                        // Кнопка "Add from favorites"
                        Button(
                            onClick = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text("ADD FROM FAVORITES")
                        }
                    }
                }
            },
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
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    }
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