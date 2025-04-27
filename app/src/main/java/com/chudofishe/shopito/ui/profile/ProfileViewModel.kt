package com.chudofishe.shopito.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chudofishe.shopito.data.db.repository.AuthRepository
import com.chudofishe.shopito.data.firebase.repo.FirebaseUserDataRepository
import com.chudofishe.shopito.model.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileScreenState(UserData()))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                authRepository.observeAuthStatus(),
                authRepository.observeUserData()
            ) { isAuthenticated, userData ->
                _state.update {
                    it.copy(
                        isAuthenticated = isAuthenticated,
                        userData = userData
                    )
                }
            }.collect()
        }
    }
}

data class ProfileScreenState(
    val userData: UserData = UserData(),
    val isAuthenticated: Boolean = false
)