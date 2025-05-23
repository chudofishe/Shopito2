package com.chudofishe.shopito.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chudofishe.shopito.data.db.dao.ShoppingListDao
import com.chudofishe.shopito.data.db.dao.ShoppingListItemDao
import com.chudofishe.shopito.model.ShoppingList
import com.chudofishe.shopito.model.ShoppingListItem

@Database(entities = [ShoppingListItem::class, ShoppingList::class], version = 7, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RoomDb : RoomDatabase() {
    abstract val shoppingListItemDao: ShoppingListItemDao
    abstract val shoppListDao: ShoppingListDao
}