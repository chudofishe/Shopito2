package com.chudofishe.shopito.data.firebase.repo

import com.chudofishe.shopito.data.firebase.RealtimeDatabaseResult
import com.chudofishe.shopito.data.firebase.RealtimeDatabaseValueResult
import com.chudofishe.shopito.model.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseUserDataRepository(
    private val firebaseEmailToUIdRepository: FirebaseEmailToUIdRepository
) : FirebaseDataRepository() {

    private val REF_USERS = "users"

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
        Firebase.auth.currentUser?.let {fbUser ->
            if (getUserData(fbUser.uid) == null) {
                setUserData(UserData(
                    userId = fbUser.uid,
                    username = fbUser.displayName,
                    email = fbUser.email,
                    profilePictureUrl = fbUser.photoUrl.toString()
                )).collect {
                    if (it is RealtimeDatabaseResult.Success) {
                        fbUser.email?.let {
                            firebaseEmailToUIdRepository.setEmailToUid(fbUser.email!!, fbUser.uid)
                        }
                    }
                }
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

    suspend fun getUserData(userId: String?): UserData? {
        return userId?.let {
            db.getReference(REF_USERS).child(userId).get().await().getValue<UserData>()
        }
    }
}