package com.chudofishe.shopito.data.db.repository

import com.chudofishe.shopito.util.areAllItemsComplete
import com.chudofishe.shopito.data.db.datastore.CurrentListDatastore
import com.chudofishe.shopito.data.db.dao.ShoppingListDao
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingList
import com.chudofishe.shopito.model.ShoppingListItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class ShoppingListRepository(
    private val dao: ShoppingListDao,
    private val currentListDatastore: CurrentListDatastore,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeCurrentList(): Flow<ShoppingList?> = currentListDatastore.getCurrentListId()
        .flatMapLatest { currentListId ->
            if (currentListId == null) {
                flowOf(null)
            } else {
                dao.getFlowById(currentListId)
            }
        }

    suspend fun getCurrentList() = observeCurrentList().first()

    suspend fun addItemToList(list: ShoppingList, item: ShoppingListItem) = withContext(defaultDispatcher) {
        with (list) {
            val updatedItems = items.toMutableList().apply { add(item) }

            val updatedList = copy(
                items = updatedItems,
                isCompleted = false
            )

            dao.insert(updatedList)
        }
    }

    suspend fun removeItemFromList(list: ShoppingList, item: ShoppingListItem) = withContext(defaultDispatcher) {
        with (list) {
            val updatedItems = items.toMutableList().apply { remove(item) }

            val isCompleted = updatedItems.areAllItemsComplete()

            dao.update(
                copy(
                    items = updatedItems,
                    isCompleted = isCompleted
                )
            )

            if (isCompleted) currentListDatastore.setCurrentListId(null)

            return@withContext isCompleted
        }
    }

    suspend fun updateItemInList(list: ShoppingList, item: ShoppingListItem) = withContext(defaultDispatcher) {
        val updatedItems = list.items.map {
            if (it.name == item.name && it.category == item.category && it.description == item.description) item else it
        }

        val isCompleted = updatedItems.areAllItemsComplete()

        dao.update(
            list.copy(
                items = updatedItems,
                isCompleted = isCompleted
            )
        )

        if (isCompleted) currentListDatastore.setCurrentListId(null)

        isCompleted
    }

//    suspend fun createListAndSetAsCurrent(): Long = withContext(Dispatchers.IO) {
//        val newCurrentListId = dao.insert(ShoppingList())
//        currentListDatastore.setCurrentListId(newCurrentListId)
//        newCurrentListId
//    }

    suspend fun createListAndSetAsCurrent(): ShoppingList = withContext(Dispatchers.IO) {
        val newCurrentListId = dao.insert(ShoppingList())
        currentListDatastore.setCurrentListId(newCurrentListId)
        ShoppingList(
            id = newCurrentListId,
        )
    }

    suspend fun setCurrentListId(id: Long) {
        currentListDatastore.setCurrentListId(id)
    }

    fun getAll(): Flow<List<ShoppingList>> = dao.getAll()

    fun getListById(id: Long): ShoppingList {
        return dao.getById(id)!!
    }

    fun observeListById(id: Long): Flow<ShoppingList> {
        return dao.getFlowById(id)
    }
}