package com.chudofishe.shopito.ui.add_list_item.create_item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.chudofishe.shopito.util.asStateFlow
import com.chudofishe.shopito.data.db.repository.ShoppingListItemRepository
import com.chudofishe.shopito.data.db.repository.ShoppingListRepository
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingListItem
import com.chudofishe.shopito.navigation.NavigateBack
import com.chudofishe.shopito.navigation.TopLevelNavigationRoute
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class CreateItemScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val shoppingListItemRepository: ShoppingListItemRepository,
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    private val _name = MutableStateFlow("")
    private val _nameErrorText = MutableStateFlow("")
    private val _category = MutableStateFlow(savedStateHandle.toRoute<TopLevelNavigationRoute.AddItemRoute>().categoryToSelect)
    private val _description = MutableStateFlow("")

    private val navigationChannel = Channel<NavigateBack>()
    val navigationEventsChannelFlow = navigationChannel.receiveAsFlow()

    val state = combine(
        _name,
        _category,
        _nameErrorText,
        _description
    ) { name, category, nameErrorText, description ->
        CreateItemScreenState(
            itemName = name,
            category = category,
            nameErrorText = nameErrorText,
            description = description
        )
    }.asStateFlow(viewModelScope, CreateItemScreenState())

    fun updateTextInput(text: String) {
        _name.value = text
        _nameErrorText.value = ""
    }

    fun updateCategoryInput(category: Category) {
        _category.value = if (category == _category.value) {
            Category.OTHER
        } else {
            category
        }
    }

    fun updateDescription(text: String) {
        _description.value = text
    }

    fun addItem() {
        viewModelScope.launch {
            if (_name.value.isBlank() || _nameErrorText.value.isNotBlank()) {
                _nameErrorText.value = "Enter name"
            } else {
                val newItem = ShoppingListItem(
                    name = _name.value,
                    category = _category.value,
                    description = _description.value,
                    isChecked = false,
                    timeStamp = LocalDateTime.now()
                )
                shoppingListItemRepository.add(newItem)
                shoppingListRepository.addItemToList(shoppingListRepository.getCurrentList(), newItem)
                navigationChannel.send(NavigateBack)
            }
        }
    }
}

data class CreateItemScreenState(
    val itemName: String = "",
    val description: String = "",
    val category: Category = Category.OTHER,
    val nameErrorText: String = ""
)