package com.chudofishe.shopito.ui.profile

import androidx.lifecycle.ViewModel
import com.chudofishe.shopito.data.firebase.FirebaseUserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel(private val firebaseUserData: FirebaseUserData) : ViewModel() {

    private val _userData = MutableStateFlow(firebaseUserData.getSignedInUser()!!)
    val userData = _userData.asStateFlow()
}