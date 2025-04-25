package com.chudofishe.shopito.data.firebase.repo

import android.util.Log
import com.chudofishe.shopito.data.firebase.DBRef
import com.chudofishe.shopito.data.firebase.RealtimeDatabaseResult
import com.chudofishe.shopito.data.firebase.RealtimeDatabaseValueResult
import com.chudofishe.shopito.model.UserData
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
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

    suspend fun addFriend(friend: UserData): RealtimeDatabaseResult {
        val currentUserId = Firebase.auth.currentUser?.uid
            ?: return RealtimeDatabaseResult.Error(Exception("Current user is null"))

        return try {
            // Создаем Map для обновления нескольких путей одновременно
            val updates = hashMapOf<String, Any>(
                "${DBRef.FRIEND}/$currentUserId/${friend.userId}" to true,
                "${DBRef.FRIEND}/${friend.userId}/$currentUserId" to true
            )

            // Выполняем множественное обновление как одну транзакцию
            db.reference.updateChildren(updates).await()
            RealtimeDatabaseResult.Success
        } catch (e: Exception) {
            Log.e("addFriend", "Error adding friend: ${e.message}", e)
            RealtimeDatabaseResult.Error(e)
        }
    }

    suspend fun removeFriend(friend: UserData): RealtimeDatabaseResult {
        val currentUserId = Firebase.auth.currentUser?.uid
            ?: return RealtimeDatabaseResult.Error(Exception("Current user is null"))

        return try {
            // Создаем Map для удаления связей с обеих сторон одновременно
            val updates = hashMapOf<String, Any?>(
                "${DBRef.FRIEND}/$currentUserId/${friend.userId}" to null,
                "${DBRef.FRIEND}/${friend.userId}/$currentUserId" to null
            )

            // Выполняем множественное обновление как одну транзакцию
            db.reference.updateChildren(updates).await()
            RealtimeDatabaseResult.Success
        } catch (e: Exception) {
            Log.e("removeFriend", "Error removing friend: ${e.message}", e)
            RealtimeDatabaseResult.Error(e)
        }
    }


    fun getFriends(): Flow<RealtimeDatabaseValueResult<List<UserData>>> = callbackFlow {
        val userId = Firebase.auth.currentUser?.uid

        if (userId == null) {
            trySend(RealtimeDatabaseValueResult.Error(Exception("User is not authenticated")))
            close()
            return@callbackFlow
        }

        val ref = db
            .getReference(DBRef.FRIEND)
            .child(userId)

        trySend(RealtimeDatabaseValueResult.Loading())

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val pendingTasks = mutableListOf<Task<DataSnapshot>>()
                    val result = mutableListOf<UserData>()

                    // Если друзей нет, сразу возвращаем пустой список
                    if (!snapshot.exists() || snapshot.childrenCount == 0L) {
                        trySend(RealtimeDatabaseValueResult.Success(emptyList()))
                        return
                    }

                    // Создаем список задач для получения данных каждого друга
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
                    Log.e("getFriends", e.toString())
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
}