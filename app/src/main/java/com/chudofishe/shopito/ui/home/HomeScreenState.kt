package com.chudofishe.shopito.ui.home

import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingList

data class HomeScreenState(
    val currentListTitle: String = "",
    val shoppingList: ShoppingList? = null,
    val showCompleteAnimation: Boolean = false,
    val collapsedCategories: Set<Category> = emptySet(),
    val selectedCategory: Category = Category.OTHER,
    val itemNameInput: String = ""
)
