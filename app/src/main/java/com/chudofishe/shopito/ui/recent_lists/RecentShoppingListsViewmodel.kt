// Модифицированный RecentShoppingListsViewmodel.kt
package com.chudofishe.shopito.ui.recent_lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chudofishe.shopito.util.asStateFlow
import com.chudofishe.shopito.data.db.repository.ShoppingListRepository
import com.chudofishe.shopito.model.ShoppingList
import com.chudofishe.shopito.navigation.NavigationRoute
import com.chudofishe.shopito.navigation.TopLevelNavigationRoute
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RecentShoppingListsViewmodel(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    private val _lists = shoppingListRepository.getAll()
    private val _currentList = shoppingListRepository.observeCurrentList().asStateFlow(viewModelScope, null)

    private val navigationChannel = Channel<NavigationRoute>()
    val navigationEventsChannelFlow = navigationChannel.receiveAsFlow()

    val state = combine(
        _lists,
        _currentList
    ) { lists, currentList ->
        RecentShoppingListsScreenState(
            items = lists.filterNot { it == currentList }.sortedByDescending { it.timestamp },
            currentList = currentList
        )
    }.asStateFlow(
        viewModelScope,
        RecentShoppingListsScreenState()
    )


    fun setCurrentListId(id: Long) {
        viewModelScope.launch {
            shoppingListRepository.setCurrentListId(id)
            navigationChannel.send(TopLevelNavigationRoute.HomeRoute)
        }
    }

    fun navigateToViewList(id: Long) {
        viewModelScope.launch {
            navigationChannel.send(TopLevelNavigationRoute.ViewListRoute(id))
        }
    }

    fun addNewList() {
        viewModelScope.launch {
            shoppingListRepository.createListAndSetAsCurrent()
            navigationChannel.send(TopLevelNavigationRoute.HomeRoute)
        }
    }
}

data class RecentShoppingListsScreenState(
    val items: List<ShoppingList> = emptyList(),
    val currentList: ShoppingList? = null
)