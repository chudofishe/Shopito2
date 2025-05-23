package com.chudofishe.shopito.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class ShoppingList(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val items: List<ShoppingListItem> = emptyList(),
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val isCompleted: Boolean = false
) {
    fun getCategories(): List<Category> = items.map { it.category }.distinct()
}

fun List<ShoppingListItem>.getCategoryToItemsList(): List<Pair<Category, List<ShoppingListItem>>> {
    return this.groupBy { it.currentCategory }.toList()
}