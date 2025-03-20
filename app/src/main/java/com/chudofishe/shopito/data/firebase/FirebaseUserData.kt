package com.chudofishe.shopito.data.firebase

import com.chudofishe.shopito.model.UserData
import com.google.firebase.auth.FirebaseAuth

class FirebaseUserData(private val auth: FirebaseAuth) {

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            email = email,
            profilePictureUrl = photoUrl?.toString()
        )
    }
}