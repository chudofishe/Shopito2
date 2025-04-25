package com.chudofishe.shopito.data.firebase

/**
 * Константы для ссылок на коллекции Firebase Realtime Database.
 * Используются для доступа к различным путям в базе данных.
 */
object DBRef {
    // Ссылка на коллекцию пользователей
    const val USERS = "users"

    // Ссылка на коллекцию друзей
    const val FRIEND = "friend"

    // Ссылка на коллекцию запросов в друзья
    const val FRIEND_REQUESTS = "friendRequest"

    // Ссылка на коллекцию соответствия email к UID
    const val EMAIL_TO_UID = "emailToUID"
}