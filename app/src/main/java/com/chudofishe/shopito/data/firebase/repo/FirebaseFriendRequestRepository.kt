package com.chudofishe.shopito.data.firebase.repo

import android.util.Log
import com.chudofishe.shopito.data.firebase.FbDataUtil
import com.chudofishe.shopito.data.firebase.RealtimeDatabaseResult
import com.chudofishe.shopito.data.firebase.RealtimeDatabaseValueResult
import com.chudofishe.shopito.model.FriendData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import com.google.firebase.database.getValue

class FirebaseFriendRequestRepository(
    private val firebaseUserDataRepository: FirebaseUserDataRepository,
    private val emailToUIdRepository: FirebaseEmailToUIdRepository
) : FirebaseDataRepository() {

    private val REF_FRIEND_REQUESTS = "friendRequest"

    suspend fun sendFriendRequest(
        email: String
    ): RealtimeDatabaseResult {
        val friendUid = emailToUIdRepository.getUidByEmail(email)
        if (friendUid == null) {
            return RealtimeDatabaseResult.Error(Exception("User not found"))
        }
        return firebaseUserDataRepository.getUserData(Firebase.auth.currentUser?.uid)?.let {
            try {
                val friendRequest = FbDataUtil.currentUserToFriendRequest(it)

                val ref = db
                    .getReference(REF_FRIEND_REQUESTS)
                    .child(friendUid)
                    .child(it.userId)

                ref.setValue(friendRequest).await()
                RealtimeDatabaseResult.Success
            } catch (e: Exception) {
                RealtimeDatabaseResult.Error(e)
            }
        } ?: RealtimeDatabaseResult.Error(Exception("Couldn't get current user data"))
    }

    fun getFriendRequests(): Flow<RealtimeDatabaseValueResult<List<FriendData>>> = callbackFlow {
        val userId = Firebase.auth.currentUser?.uid

        if (userId == null) {
            trySend(RealtimeDatabaseValueResult.Error(Exception("User is not authenticated")))
            close()
            return@callbackFlow
        }

        val ref = db
            .getReference(REF_FRIEND_REQUESTS)
            .child(userId)

        trySend(RealtimeDatabaseValueResult.Loading())

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val result = mutableListOf<FriendData>()
                    for (child in snapshot.children) {
                        val request = child.getValue<FriendData>()
                        request?.let { result.add(it) }
                    }
                    trySend(RealtimeDatabaseValueResult.Success(result))
                }
                catch (e: Exception) {
                    Log.e("getFriendRequests", e.toString())
                    close(e)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(RealtimeDatabaseValueResult.Error(error.toException()))
                close(error.toException())
            }
        }

        ref.addValueEventListener(listener)

        awaitClose {
            ref.removeEventListener(listener)
        }
    }

    suspend fun deleteFriendRequestByUid(friendUid: String): RealtimeDatabaseResult {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
            ?: return RealtimeDatabaseResult.Error(Exception("User is not authenticated"))

        return try {
            val ref = db
                .getReference(REF_FRIEND_REQUESTS)
                .child(userId)
                .child(friendUid)

            ref.removeValue().await()
            RealtimeDatabaseResult.Success
        } catch (e: Exception) {
            RealtimeDatabaseResult.Error(e)
        }
    }

}