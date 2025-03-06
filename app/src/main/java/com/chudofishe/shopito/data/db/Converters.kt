package com.chudofishe.shopito.data.db

import androidx.room.TypeConverter
import com.chudofishe.shopito.model.Category
import com.chudofishe.shopito.model.ShoppingListItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset


object Converters {

    @TypeConverter
    fun localDateToEpochDay(value: LocalDateTime): Long {
        return value.toEpochSecond(ZoneOffset.UTC)
    }

    @TypeConverter
    fun localDateFromEpochDay(value: Long): LocalDateTime {
        return LocalDateTime.ofEpochSecond(value, 0, ZoneOffset.UTC)
    }

    @TypeConverter
    fun shoppingListItemsListToJsonString(value: List<ShoppingListItem>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun shoppingListItemsListFromJsonString(value: String): List<ShoppingListItem> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun categorySetToString(value: Set<Category>) = Json.encodeToString(value)

    @TypeConverter
    fun categorySetFromString(value: String): Set<Category> = Json.decodeFromString(value)
}