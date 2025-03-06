package com.chudofishe.shopito.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chudofishe.shopito.model.ShoppingList
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shoppinglist ORDER BY ID")
    fun getAll(): Flow<List<ShoppingList>>

    @Query("SELECT * FROM shoppinglist WHERE id = :id LIMIT 1")
    fun getFlowById(id: Long): Flow<ShoppingList>

    @Query("SELECT * FROM shoppinglist WHERE id = :id")
    fun getById(id: Long?): ShoppingList?

    @Query("DELETE FROM shoppinglist")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(item: ShoppingList)

    @Update
    suspend fun update(item: ShoppingList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShoppingList): Long
}