package com.chudofishe.shopito.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.chudofishe.shopito.data.db.repository.ShoppingListItemRepository
import com.chudofishe.shopito.data.db.repository.ShoppingListRepository
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingList
import com.chudofishe.shopito.model.ShoppingListItem
import com.chudofishe.shopito.navigation.NavigateBack
import com.chudofishe.shopito.navigation.TopLevelNavigationRoute
import com.chudofishe.shopito.util.asStateFlow
import com.chudofishe.shopito.util.toDayOfWeekDateTimeString
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class HomeViewModel(
    savedStateHandle: SavedStateHandle,
    private val shoppingListRepository: ShoppingListRepository,
    private val shoppingListItemRepository: ShoppingListItemRepository,
) : ViewModel() {

    private val _currentList = shoppingListRepository.observeCurrentList().asStateFlow(viewModelScope, ShoppingList())
    private val _showDrawer = MutableStateFlow(savedStateHandle.toRoute<TopLevelNavigationRoute.HomeRoute>().showDrawer)
    private val _showCompleteAnimation = MutableStateFlow(false)
    private val _collapsedCategories = MutableStateFlow(emptySet<Category>())
    private val _selectedCategory = MutableStateFlow(Category.OTHER)
    private val _itemNameInputValue = MutableStateFlow("")

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()

    private val showDrawerChannel = Channel<Boolean>()
    val showDrawerChannelFlow = showDrawerChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            com.chudofishe.shopito.util.combine(
                _currentList,
                _showCompleteAnimation,
                _collapsedCategories,
                _showDrawer,
                _selectedCategory,
                _itemNameInputValue
            ) { list, completeAnim, collapsedCategories, showDrawer, selectedCategory, itemNameInput ->
                _homeScreenState.update {
                    HomeScreenState(
                        currentListTitle = list?.timestamp?.toDayOfWeekDateTimeString() ?: "Shopito",
                        shoppingList = list,
                        showCompleteAnimation = completeAnim,
                        collapsedCategories = collapsedCategories,
                        selectedCategory = selectedCategory,
                        itemNameInput = itemNameInput
                    )
                }

                showDrawerChannel.send(showDrawer)
            }.collect()
        }
    }

    fun removeItem(item: ShoppingListItem) {
        viewModelScope.launch {
            _currentList.value?.let {
                val isCompleted = shoppingListRepository.removeItemFromList(it, item)

                if (isCompleted) _showCompleteAnimation.value = true
            }
        }
    }

    fun updateItem(item: ShoppingListItem, isChecked: Boolean) {
        viewModelScope.launch {
            _currentList.value?.let {
                val newItem = item.copy(
                    currentCategory = if (isChecked) Category.COMPLETED else item.category
                )

                val isCompleted = shoppingListRepository.updateItemInList(it, newItem)

                if (isCompleted) _showCompleteAnimation.value = true
            }
        }
    }

    fun toggleCategoryCollapsedState(category: Category) {
        viewModelScope.launch {
            _collapsedCategories.update {
                if (it.contains(category)) {
                    it - category
                } else {
                    it + category
                }
            }
        }
    }

    fun hideCompleteAnimation() {
        viewModelScope.launch {
            _showCompleteAnimation.value = false
        }
    }

    fun setSelectedCategory(category: Category) {
        viewModelScope.launch {
            _selectedCategory.value = category
        }

    }

    fun updateItemNameInput(value: String) {
        viewModelScope.launch {
            _itemNameInputValue.value = value
        }
    }

    fun addItem() {
        viewModelScope.launch {
            if (_itemNameInputValue.value.isBlank()) {
                return@launch
            } else if (_currentList.value != null) {
                val newItem = ShoppingListItem(
                    name = _itemNameInputValue.value,
                    category = _selectedCategory.value,
                    timeStamp = LocalDateTime.now(),
                    currentCategory = _selectedCategory.value
                )
                shoppingListItemRepository.add(newItem)
                shoppingListRepository.addItemToList(_currentList.value!!, newItem)
                _itemNameInputValue.value = ""
            }
        }
    }
}