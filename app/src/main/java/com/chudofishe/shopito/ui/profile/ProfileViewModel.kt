package com.chudofishe.shopito.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chudofishe.shopito.data.firebase.repo.FirebaseUserDataRepository
import com.chudofishe.shopito.model.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(private val firebaseUserDataRepository: FirebaseUserDataRepository) : ViewModel() {

    private val _state = MutableStateFlow(ProfileScreenState(UserData()))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    userData = Firebase.auth.currentUser?.uid?.let {
                        firebaseUserDataRepository.getUserData(it)
                    } ?: UserData()
                )
            }
        }
    }
}

data class ProfileScreenState(
    val userData: UserData
)