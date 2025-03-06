package com.chudofishe.shopito.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chudofishe.shopito.asStateFlow
import com.chudofishe.shopito.data.db.repository.ShoppingListRepository
import com.chudofishe.shopito.model.ShoppingList
import com.chudofishe.shopito.toDayOfWeekDateTimeString
import com.chudofishe.shopito.ui.shoppinglist.ShoppingListScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    private val currentList = shoppingListRepository.observeCurrentList().asStateFlow(viewModelScope, ShoppingList())
    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()

    init {
        viewModelScope.launch {
            currentList.collectLatest { list ->
                _homeScreenState.update {
                    HomeScreenState(
                        isFabExpanded = list.items.isEmpty(),
                        currentListTitle = list.timestamp.toDayOfWeekDateTimeString()
                    )
                }
            }
        }
    }

//    fun deleteCurrentList() {
//        viewModelScope.launch {
//            shoppingListRepository.de
//        }
//    }
}