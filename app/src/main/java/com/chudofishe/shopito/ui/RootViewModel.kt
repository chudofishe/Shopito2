package com.chudofishe.shopito.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chudofishe.shopito.auth.FirebaseAuthHandler
import com.chudofishe.shopito.data.db.repository.AuthRepository
import com.chudofishe.shopito.data.firebase.FirebaseAuthResult
import com.chudofishe.shopito.data.firebase.repo.FirebaseUserDataRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RootViewModel(
    private val firebaseUserDataRepository: FirebaseUserDataRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val signInChannel = Channel<Unit>()
    val signInChannelFlow = signInChannel.receiveAsFlow()

    private val signOutChannel = Channel<Unit>()
    val signOutChannelFlow = signOutChannel.receiveAsFlow()

    fun signIn(context: Context) {
        viewModelScope.launch {
            FirebaseAuthHandler.handleSignIn(context).collect {
                when(it) {
                    is FirebaseAuthResult.Error -> TODO()
                    FirebaseAuthResult.Loading -> TODO()
                    is FirebaseAuthResult.Success -> {
                        firebaseUserDataRepository.createUserIfAbsent()
                        signInChannel.send(Unit)
                        authRepository.saveUserAuthData(it.user)
                    }
                }
            }
        }
    }

    fun signOut(context: Context) {
        viewModelScope.launch {
            FirebaseAuthHandler.handleSignOut(context)
            signOutChannel.trySend(Unit)
            authRepository.clearAuthData()
        }
    }
}