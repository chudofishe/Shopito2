package com.chudofishe.shopito.data.firebase.repo

import android.util.Log
import com.chudofishe.shopito.data.firebase.RealtimeDatabaseResult
import com.chudofishe.shopito.data.firebase.RealtimeDatabaseValueResult
import com.chudofishe.shopito.model.FriendData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseFriendRepository : FirebaseDataRepository() {

    private val REF_FRIEND = "friend"

    suspend fun addFriend(
        friendId: String
    ): RealtimeDatabaseResult {
        Firebase.auth.currentUser?.uid?.let {
            return try {
                val ref = db.getReference(REF_FRIEND).child(it).child(friendId)

                ref.setValue(true).await()
                RealtimeDatabaseResult.Success
            } catch (e: Exception) {
                Log.e("sendFriendRequest", e.toString())
                RealtimeDatabaseResult.Error(e)
            }
        } ?: return RealtimeDatabaseResult.Error(Exception("Current user is null"))
    }

    suspend fun removeFriend(
        friendId: String
    ): RealtimeDatabaseResult {
        Firebase.auth.currentUser?.uid?.let {
            return try {
                val ref = db.getReference(REF_FRIEND).child(it).child(friendId)

                ref.removeValue().await()
                RealtimeDatabaseResult.Success
            } catch (e: Exception) {
                Log.e("sendFriendRequest", e.toString())
                RealtimeDatabaseResult.Error(e)
            }
        } ?: return RealtimeDatabaseResult.Error(Exception("Current user is null"))
    }

    suspend fun getFriendRequests(userId: String): List<FriendData> {
        val ref = db
            .getReference(REF_FRIEND)
            .child(userId)

        val snapshot = ref.get().await()
        val result = mutableListOf<FriendData>()

        for (child in snapshot.children) {
            val request = child.getValue(FriendData::class.java)
            request?.let { result.add(it) }
        }

        return result
    }

    fun getFriends(): Flow<RealtimeDatabaseValueResult<List<FriendData>>> = callbackFlow {
        val userId = Firebase.auth.currentUser?.uid

        if (userId == null) {
            trySend(RealtimeDatabaseValueResult.Error(Exception("User is not authenticated")))
            close()
            return@callbackFlow
        }

        val ref = db
            .getReference(REF_FRIEND)
            .child(userId)

        trySend(RealtimeDatabaseValueResult.Loading())

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<FriendData>()
                for (child in snapshot.children) {
                    val request = child.getValue(FriendData::class.java)
                    request?.let { result.add(it) }
                }
                trySend(RealtimeDatabaseValueResult.Success(result))
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
}