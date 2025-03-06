package com.chudofishe.shopito.ui.shoppinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chudofishe.shopito.UIEvent
import com.chudofishe.shopito.asStateFlow
import com.chudofishe.shopito.data.db.repository.ShoppingListRepository
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingList
import com.chudofishe.shopito.model.ShoppingListItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ShoppingListScreenViewModel(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {
    private val uiEventChannel = Channel<UIEvent>()
    val uiEventChannelFlow = uiEventChannel.receiveAsFlow()


    private val currentList = shoppingListRepository.observeCurrentList().asStateFlow(viewModelScope, ShoppingList())
    private val _state = MutableStateFlow(ShoppingListScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            currentList.collectLatest { list ->
                _state.update {
                    ShoppingListScreenState(
                        entries = list.toCategorisedMap(),
                        isListEmpty = list.items.isEmpty(),
                        collapsedCategories = list.collapsedCategories,
                        completedCategories = list.completedCategories
                    )
                }
            }
        }
    }

    fun removeItem(item: ShoppingListItem) {
        viewModelScope.launch {
            val isCompleted = shoppingListRepository.removeItemFromList(currentList.value, item)

            if (isCompleted) uiEventChannel.send(UIEvent.ShowCompleteAnimationEvent)
        }
    }

    fun updateItem(item: ShoppingListItem) {
        viewModelScope.launch {
            val isCompleted = shoppingListRepository.updateItemInList(currentList.value, item)

            if (isCompleted) uiEventChannel.send(UIEvent.ShowCompleteAnimationEvent)
        }
    }

    fun updateCollapsedCategory(category: Category) {
        viewModelScope.launch {
            shoppingListRepository.updateCollapsedCategory(currentList.value, category)
        }
    }
}

