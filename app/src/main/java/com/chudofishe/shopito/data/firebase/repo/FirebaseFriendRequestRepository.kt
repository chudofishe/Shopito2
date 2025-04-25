package com.chudofishe.shopito.data.firebase.repo

import android.util.Log
import com.chudofishe.shopito.data.firebase.DBRef
import com.chudofishe.shopito.data.firebase.FbDataUtil
import com.chudofishe.shopito.data.firebase.RealtimeDatabaseResult
import com.chudofishe.shopito.data.firebase.RealtimeDatabaseValueResult
import com.chudofishe.shopito.model.UserData
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
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

    suspend fun sendFriendRequest(
        email: String
    ): RealtimeDatabaseResult {
        val friendUid = emailToUIdRepository.getUidByEmail(email)
        if (friendUid == null) {
            return RealtimeDatabaseResult.Error(Exception("User not found"))
        }
        return firebaseUserDataRepository.getUserData(Firebase.auth.currentUser?.uid)?.let {
            try {
                val ref = db
                    .getReference(DBRef.FRIEND_REQUESTS)
                    .child(friendUid)
                    .child(it.userId)

                ref.setValue(true).await()
                RealtimeDatabaseResult.Success
            } catch (e: Exception) {
                RealtimeDatabaseResult.Error(e)
            }
        } ?: RealtimeDatabaseResult.Error(Exception("Couldn't get current user data"))
    }


    fun getFriendRequests(): Flow<RealtimeDatabaseValueResult<List<UserData>>> = callbackFlow {
        val userId = Firebase.auth.currentUser?.uid

        if (userId == null) {
            trySend(RealtimeDatabaseValueResult.Error(Exception("User is not authenticated")))
            close()
            return@callbackFlow
        }

        val ref = db
            .getReference(DBRef.FRIEND_REQUESTS)
            .child(userId)

        trySend(RealtimeDatabaseValueResult.Loading())

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val pendingTasks = mutableListOf<Task<DataSnapshot>>()
                    val result = mutableListOf<UserData>()

                    // Если запросов в друзья нет, сразу возвращаем пустой список
                    if (!snapshot.exists() || snapshot.childrenCount == 0L) {
                        trySend(RealtimeDatabaseValueResult.Success(emptyList()))
                        return
                    }

                    // Создаем список задач для получения данных каждого пользователя
                    for (child in snapshot.children) {
                        val friendUserId = child.key ?: continue
                        val userTask = db.getReference(DBRef.USERS).child(friendUserId).get()
                        pendingTasks.add(userTask)

                        userTask.addOnSuccessListener { userSnapshot ->
                            val userData = userSnapshot.getValue<UserData>()
                            if (userData != null) {
                                result.add(userData)
                            }
                        }
                    }

                    // Ждем выполнения всех задач и отправляем результат
                    Tasks.whenAllComplete(pendingTasks).addOnCompleteListener {
                        trySend(RealtimeDatabaseValueResult.Success(result))
                    }

                } catch (e: Exception) {
                    Log.e("getFriendRequests", e.toString())
                    trySend(RealtimeDatabaseValueResult.Error(e))
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
                .getReference(DBRef.FRIEND_REQUESTS)
                .child(userId)
                .child(friendUid)

            ref.removeValue().await()
            RealtimeDatabaseResult.Success
        } catch (e: Exception) {
            RealtimeDatabaseResult.Error(e)
        }
    }

}