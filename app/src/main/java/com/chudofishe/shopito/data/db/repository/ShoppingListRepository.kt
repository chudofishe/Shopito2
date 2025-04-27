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
import kotlinx.coroutines.withContext

class ShoppingListRepository(
    private val dao: ShoppingListDao,
    private val currentListDatastore: CurrentListDatastore,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeCurrentList(): Flow<ShoppingList> =  currentListDatastore.getCurrentListId()
        .flatMapLatest { currentListId ->
            if (currentListId == null) {
                flow {
                    val newCurrentListId = createListAndSetAsCurrent()
                    emit(newCurrentListId)
                }.flatMapLatest { dao.getFlowById(it) }
            } else {
                dao.getFlowById(currentListId)
            }
        }

    suspend fun getCurrentList() = observeCurrentList().first()

    suspend fun addItemToList(list: ShoppingList, item: ShoppingListItem) = withContext(defaultDispatcher) {
        with (list) {
            val updatedItems = items.toMutableList().apply { add(item) }
            val updatedCompletedCategories = completedCategories.toMutableSet().apply { remove(item.category) }
            val updatedCollapsedCategories = collapsedCategories.toMutableSet().apply { remove(item.category) }

            val updatedList = copy(
                items = updatedItems,
                completedCategories = updatedCompletedCategories,
                collapsedCategories = updatedCollapsedCategories,
                isCompleted = false
            )

            dao.insert(updatedList)
        }
    }

    suspend fun removeItemFromList(list: ShoppingList, item: ShoppingListItem) = withContext(defaultDispatcher) {
        with (list) {
            val updatedItems = items.toMutableList().apply { remove(item) }
            val categoryItems = updatedItems.filter { it.category == item.category }

            val updatedCompletedCategories = completedCategories.toMutableSet()
            if (categoryItems.all { it.isChecked } && categoryItems.isNotEmpty()) {
                updatedCompletedCategories.add(item.category)
            }

            val isCompleted = updatedItems.areAllItemsComplete()

            dao.update(
                copy(
                    items = updatedItems,
                    completedCategories = updatedCompletedCategories,
                    isCompleted = isCompleted
                )
            )

            if (isCompleted) createListAndSetAsCurrent()

            return@withContext isCompleted
        }
    }

    suspend fun updateItemInList(list: ShoppingList, item: ShoppingListItem) = withContext(defaultDispatcher) {
        val updatedItems = list.items.map {
            if (it.name == item.name && it.category == item.category && it.description == item.description) item else it
        }
        val categoryItems = updatedItems.filter { it.category == item.category }

        val updatedCompletedCategories = list.completedCategories.toMutableSet()
        val updatedCollapsedCategories = list.collapsedCategories.toMutableSet()

        if (categoryItems.all { it.isChecked } && categoryItems.isNotEmpty()) {
            updatedCompletedCategories.add(item.category)
            updatedCollapsedCategories.add(item.category)
        } else {
            updatedCompletedCategories.remove(item.category)
            updatedCollapsedCategories.remove(item.category)
        }

        val isCompleted = updatedItems.areAllItemsComplete()

        dao.update(
            list.copy(
                items = updatedItems,
                completedCategories = updatedCompletedCategories,
                collapsedCategories = updatedCollapsedCategories,
                isCompleted = isCompleted
            )
        )

        if (isCompleted) createListAndSetAsCurrent()

        isCompleted
    }

    suspend fun updateCollapsedCategory(list: ShoppingList, category: Category) {
        val updatedCollapsedCategories = list.collapsedCategories.toMutableSet().apply {
            if (!this.remove(category)) this.add(category)
        }

        dao.update(
            list.copy(
                collapsedCategories = updatedCollapsedCategories
            )
        )
    }

    suspend fun createListAndSetAsCurrent() = withContext(Dispatchers.IO) {
        val newCurrentListId = dao.insert(ShoppingList())
        currentListDatastore.setCurrentListId(newCurrentListId)
        newCurrentListId
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

    suspend fun deleteCurrentList() {
        dao.delete(getCurrentList())
        createListAndSetAsCurrent()
    }

//    suspend fun completeCurrentList() {
//        val list = getCurrentList()
//        val categories = mutableSetOf<Category>()
//        list.items.map {
//            categories.add(it.category)
//            it.isChecked = true
//        }
//        dao.update(getCurrentList().copy())
//    }

}