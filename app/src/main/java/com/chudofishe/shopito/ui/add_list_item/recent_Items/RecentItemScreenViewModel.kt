package com.chudofishe.shopito.ui.add_list_item.recent_Items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chudofishe.shopito.util.asStateFlow
import com.chudofishe.shopito.data.db.repository.ShoppingListItemRepository
import com.chudofishe.shopito.data.db.repository.ShoppingListRepository
import com.chudofishe.shopito.navigation.NavigateBack
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class RecentItemScreenViewModel(
    private val shoppingListItemRepository: ShoppingListItemRepository,
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    val items = shoppingListItemRepository.getAllSortedByTimestampDesc().asStateFlow(viewModelScope, emptyList())

    private val navigationChannel = Channel<NavigateBack>()
    val navigationEventsChannelFlow = navigationChannel.receiveAsFlow()

//    fun onItemSelected(item: ShoppingListItem) {
//        viewModelScope.launch(Dispatchers.IO) {
//            shoppingListRepository.addItemToCurrentList(item)
//            navigationChannel.send(NavigateBack)
//        }
//    }
}