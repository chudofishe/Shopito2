package com.chudofishe.shopito.data.db.repository

import com.chudofishe.shopito.data.db.datastore.AuthDatastore
import com.chudofishe.shopito.model.UserData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class AuthRepository(
    private val authDatastore: AuthDatastore
) {
    /**
     * Получает статус аутентификации пользователя из локального хранилища
     */
    fun observeAuthStatus(): Flow<Boolean> = authDatastore.getIsAuthenticatedFlow()

    suspend fun isAuthenticated(): Boolean = authDatastore.getIsAuthenticated()

    /**
     * Получает данные пользователя из локального хранилища
     */
    fun observeUserData(): Flow<UserData> = combine(
        authDatastore.getUserIdFlow(),
        authDatastore.getUserNameFlow(),
        authDatastore.getUserEmailFlow(),
        authDatastore.getUserPhotoUrlFlow()
    ) { userId, userName, userEmail, userPhotoUrl ->
        UserData(
            userId = userId ?: "",
            name = userName ?: "",
            email = userEmail ?: "",
            photoUrl = userPhotoUrl ?: ""
        )
    }

    suspend fun getCurrentUserData(): UserData {
        val userId = authDatastore.getUserId()
        val userName = authDatastore.getUserName()
        val userEmail = authDatastore.getUserEmail()
        val userPhotoUrl = authDatastore.getUserPhotoUrl()

        return UserData(
            userId = userId ?: "",
            name = userName ?: "",
            email = userEmail ?: "",
            photoUrl = userPhotoUrl ?: ""
        )
    }

    /**
     * Сохраняет данные пользователя в локальное хранилище при успешной аутентификации
     */
    suspend fun saveUserAuthData(user: FirebaseUser) {
        authDatastore.setAuthData(
            isAuthenticated = true,
            userId = user.uid,
            userName = user.displayName,
            userEmail = user.email,
            userPhotoUrl = user.photoUrl.toString()
        )
    }

    /**
     * Очищает данные аутентификации при выходе пользователя
     */
    suspend fun clearAuthData() {
        authDatastore.clearAuthData()
    }

    /**
     * Проверяет наличие аутентификации в Firebase
     * и синхронизирует состояние локального хранилища
     */
    suspend fun syncAuthState() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            // Пользователь авторизован в Firebase, обновляем локальное хранилище
            saveUserAuthData(
                currentUser
            )
        } else {
            // Пользователь не авторизован в Firebase, очищаем локальное хранилище
            clearAuthData()
        }
    }
}