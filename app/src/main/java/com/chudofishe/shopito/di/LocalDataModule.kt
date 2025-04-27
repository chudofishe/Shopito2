package com.chudofishe.shopito.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.chudofishe.shopito.data.db.datastore.CurrentListDatastore
import com.chudofishe.shopito.data.db.RoomDb
import com.chudofishe.shopito.data.db.dao.ShoppingListDao
import com.chudofishe.shopito.data.db.dao.ShoppingListItemDao
import com.chudofishe.shopito.data.db.datastore.AuthDatastore
import com.chudofishe.shopito.data.db.repository.AuthRepository
import com.chudofishe.shopito.data.db.repository.ShoppingListItemRepository
import com.chudofishe.shopito.data.db.repository.ShoppingListRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    single { provideDataBase(get()) }
    single { provideShoppingListItemDao(get()) }
    single { provideShoppingListItemRepository(get()) }
    single { provideShoppingListDao(get()) }
    single { provideShoppingListRepository(get(), get()) }
    single { provideCurrentListDatastore(androidContext()) }
    single { provideAuthDatastore(androidContext()) }
    single { provideAuthRepository(get()) }
}

fun provideDataBase(application: Application): RoomDb =
    Room.databaseBuilder(
        application,
        RoomDb::class.java,
        "shopito_db"
    ).
    fallbackToDestructiveMigration().build()

fun provideShoppingListItemDao(db: RoomDb): ShoppingListItemDao = db.shoppingListItemDao
fun provideShoppingListItemRepository(dao: ShoppingListItemDao): ShoppingListItemRepository = ShoppingListItemRepository(dao)

fun provideShoppingListDao(db: RoomDb): ShoppingListDao = db.shoppListDao
fun provideShoppingListRepository(dao: ShoppingListDao, currentListDatastore: CurrentListDatastore): ShoppingListRepository = ShoppingListRepository(dao, currentListDatastore)

fun provideCurrentListDatastore(context: Context) = CurrentListDatastore(context)
fun provideAuthDatastore(context: Context) = AuthDatastore(context)
fun provideAuthRepository(authDatastore: AuthDatastore) = AuthRepository(authDatastore)