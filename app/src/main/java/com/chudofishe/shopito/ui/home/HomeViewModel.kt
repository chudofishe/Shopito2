package com.chudofishe.shopito.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.chudofishe.shopito.util.asStateFlow
import com.chudofishe.shopito.data.db.repository.ShoppingListRepository
import com.chudofishe.shopito.model.ShoppingList
import com.chudofishe.shopito.navigation.TopLevelNavigationRoute
import com.chudofishe.shopito.util.toDayOfWeekDateTimeString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    savedStateHandle: SavedStateHandle,
    shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    private val _currentList = shoppingListRepository.observeCurrentList().asStateFlow(viewModelScope, ShoppingList())
    private val _showDrawer = MutableStateFlow(savedStateHandle.toRoute<TopLevelNavigationRoute.HomeRoute>().showDrawer)

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()

    init {
        viewModelScope.launch {
            _currentList.combine(_showDrawer) { list, showDrawer ->
                _homeScreenState.update {
                    HomeScreenState(
                        isFabExpanded = list.items.isEmpty(),
                        isDrawerExpanded = showDrawer,
                        currentListTitle = list.timestamp.toDayOfWeekDateTimeString()
                    )
                }
            }.collect()
        }
    }

    fun openDrawer() {
        viewModelScope.launch {
            _showDrawer.value = true
        }
    }

    fun closeDrawer() {
        viewModelScope.launch {
            _showDrawer.value = false
        }
    }

    fun toggleDrawer() {
        viewModelScope.launch {
            _showDrawer.value = !_showDrawer.value
        }
    }
}