package com.chudofishe.shopito.ui.home.current_shoppinglist

import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingListItem

typealias ShoppingListEntries = LinkedHashMap<Category, List<ShoppingListItem>>

data class ShoppingListScreenState(
    val entries: ShoppingListEntries = LinkedHashMap(),
    val isListEmpty: Boolean = false,
    val collapsedCategories: Set<Category> = emptySet(),
    val completedCategories: Set<Category> = emptySet()
)