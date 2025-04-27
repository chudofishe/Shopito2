package com.chudofishe.shopito.data.db.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AuthDatastore(
    private val context: Context
) {
    companion object {
        private const val AUTH_PREF = "auth_pref"
        private val IS_AUTHENTICATED = booleanPreferencesKey("is_authenticated")
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_PHOTO_URL = stringPreferencesKey("user_photo_url")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AUTH_PREF)

    fun getIsAuthenticatedFlow(): Flow<Boolean> = context.dataStore.data.map {
        it[IS_AUTHENTICATED] == true
    }

    fun getUserIdFlow(): Flow<String?> = context.dataStore.data.map {
        it[USER_ID]
    }

    fun getUserNameFlow(): Flow<String?> = context.dataStore.data.map {
        it[USER_NAME]
    }

    fun getUserEmailFlow(): Flow<String?> = context.dataStore.data.map {
        it[USER_EMAIL]
    }

    fun getUserPhotoUrlFlow(): Flow<String?> = context.dataStore.data.map {
        it[USER_PHOTO_URL]
    }

    suspend fun getIsAuthenticated(): Boolean {
        return context.dataStore.data.first()[IS_AUTHENTICATED] == true
    }

    suspend fun getUserId(): String? {
        return context.dataStore.data.first()[USER_ID]
    }

    suspend fun getUserName(): String? {
        return context.dataStore.data.first()[USER_NAME]
    }

    suspend fun getUserEmail(): String? {
        return context.dataStore.data.first()[USER_EMAIL]
    }

    suspend fun getUserPhotoUrl(): String? {
        return context.dataStore.data.first()[USER_PHOTO_URL]
    }

    suspend fun setAuthData(
        isAuthenticated: Boolean,
        userId: String? = null,
        userName: String? = null,
        userEmail: String? = null,
        userPhotoUrl: String? = null
    ) {
        context.dataStore.edit {
            it[IS_AUTHENTICATED] = isAuthenticated
            if (userId != null) it[USER_ID] = userId
            if (userName != null) it[USER_NAME] = userName
            if (userEmail != null) it[USER_EMAIL] = userEmail
            if (userPhotoUrl != null) it[USER_PHOTO_URL] = userPhotoUrl
        }
    }

    suspend fun clearAuthData() {
        context.dataStore.edit {
            it.remove(IS_AUTHENTICATED)
            it.remove(USER_ID)
            it.remove(USER_NAME)
            it.remove(USER_EMAIL)
            it.remove(USER_PHOTO_URL)
        }
    }
}