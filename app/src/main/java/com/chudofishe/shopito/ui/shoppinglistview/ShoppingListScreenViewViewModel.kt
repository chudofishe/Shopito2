package com.chudofishe.shopito.ui.shoppinglistview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.chudofishe.shopito.asStateFlow
import com.chudofishe.shopito.data.db.repository.ShoppingListRepository
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingList
import com.chudofishe.shopito.navigation.TopLevelNavigationRoute
import kotlinx.coroutines.launch

class ShoppingListScreenViewViewModel(
    savedStateHandle: SavedStateHandle,
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    val selectedList = shoppingListRepository.observeListById(savedStateHandle.toRoute<TopLevelNavigationRoute.ViewListRoute>().listId)
        .asStateFlow(viewModelScope, ShoppingList())

    fun updateCollapsedCategory(category: Category) {
        viewModelScope.launch {
            shoppingListRepository.updateCollapsedCategory(selectedList.value, category)
        }
    }
}