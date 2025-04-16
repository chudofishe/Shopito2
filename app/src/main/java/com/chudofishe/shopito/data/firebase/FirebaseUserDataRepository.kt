package com.chudofishe.shopito.data.firebase

import com.chudofishe.shopito.model.UserData
import com.chudofishe.shopito.util.RealtimeDatabaseResult
import com.chudofishe.shopito.util.RealtimeDatabaseValueResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.tasks.await

class FirebaseUserDataRepository {

    private val DB_URL = "https://shopito-d61cb-default-rtdb.europe-west1.firebasedatabase.app/"
    private val REF_USERS = "users"

    private val db = Firebase.database(DB_URL)

    fun setUserData(userData: UserData) = callbackFlow<RealtimeDatabaseResult> {
        db.getReference(REF_USERS).child(userData.userId).setValue(userData).addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(RealtimeDatabaseResult.Success)
            } else {
                trySend(RealtimeDatabaseResult.Error(it.exception ?: Exception("Unknown exception")))
            }
            close()
        }

        awaitClose()
    }

    suspend fun createUserIfAbsent() {
        Firebase.auth.currentUser?.let {
            if (getUserData(it.uid) == null) {
                setUserData(UserData(
                    userId = it.uid,
                    username = it.displayName,
                    email = it.email,
                    profilePictureUrl = it.photoUrl.toString()
                )).collect()
            }
        }
    }

    fun getUserDataFlow(userId: String) = callbackFlow<RealtimeDatabaseValueResult<UserData>> {
        db.getReference(REF_USERS).child(userId).get().addOnSuccessListener {
            it.getValue<UserData>()?.let {
                trySend(RealtimeDatabaseValueResult.Success<UserData>(it))
            }
            close()
        }.addOnFailureListener {
            trySend(RealtimeDatabaseValueResult.Error<UserData>(it))
            close()
        }

        awaitClose()
    }

    suspend fun getUserData(userId: String): UserData? {
        return db.getReference(REF_USERS).child(userId).get().await().getValue<UserData>()
    }
}