package com.chudofishe.shopito.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.chudofishe.shopito.model.ShoppingListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListItemDao {

    @Query("SELECT * FROM shoppinglistitem ORDER BY id DESC")
    fun getAll(): Flow<List<ShoppingListItem>>

    @Query("SELECT * FROM shoppinglistitem ORDER BY timeStamp DESC")
    fun getAllSortedByTimeStampDesc(): Flow<List<ShoppingListItem>>

    @Query("DELETE FROM shoppinglistitem")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(item: ShoppingListItem)

    @Update
    suspend fun update(item: ShoppingListItem)

    @Insert
    suspend fun insert(item: ShoppingListItem)
}