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
    val collapsedCategories: Set<Category> = emptySet(),
    val completedCategories: Set<Category> = emptySet(),
    val isCompleted: Boolean = false
) {
    fun toCategorisedMap(sortByCompletion: Boolean = true): LinkedHashMap<Category, List<ShoppingListItem>> {
        val categorisedMap = this.items.groupBy { it.category } as LinkedHashMap<Category, List<ShoppingListItem>>

        return if (sortByCompletion) {
            // Sort categories by completion: not completed first, then completed.
            val sortedCategories = categorisedMap.keys.sortedWith(compareBy { it in completedCategories })

            // Create a new LinkedHashMap to preserve the order.
            val sortedMap = LinkedHashMap<Category, List<ShoppingListItem>>()
            for (category in sortedCategories) {
                sortedMap[category] = categorisedMap[category] ?: emptyList()
            }
            sortedMap
        } else {
            // Return the map as is, without sorting.
            categorisedMap
        }
    }

    fun getCategories(): List<Category> = items.map { it.category }.distinct()
}