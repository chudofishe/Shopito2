package com.chudofishe.shopito.data.db.repository

import com.chudofishe.shopito.data.db.dao.ShoppingListItemDao
import com.chudofishe.shopito.model.ShoppingListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShoppingListItemRepository(
    private val dao: ShoppingListItemDao
) {

    fun getAll(): Flow<List<ShoppingListItem>> = dao.getAll()

    fun getAllSortedByTimestampDesc(): Flow<List<ShoppingListItem>> = dao.getAllSortedByTimeStampDesc().map { item ->
        item.distinctBy { it.name }
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    suspend fun add(itemListItem: ShoppingListItem) {
        dao.insert(itemListItem)
    }

    suspend fun remove(itemListItem: ShoppingListItem) {
        dao.delete(itemListItem)
    }

    suspend fun update(itemListItem: ShoppingListItem) {
        dao.update(itemListItem)
    }
}