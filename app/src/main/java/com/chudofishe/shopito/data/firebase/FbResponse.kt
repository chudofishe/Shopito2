package com.chudofishe.shopito.data.firebase

import com.google.firebase.auth.FirebaseUser

sealed class RealtimeDatabaseValueResult<T> {
    class Success<T> (val data: T): RealtimeDatabaseValueResult<T>()
    class Error<T> (val error: Exception): RealtimeDatabaseValueResult<T>()
    class Loading<T> : RealtimeDatabaseValueResult<T>()
}

sealed class RealtimeDatabaseResult {
    object Success : RealtimeDatabaseResult()
    object Loading : RealtimeDatabaseResult()
    class Error(val error: Exception) : RealtimeDatabaseResult()
}

sealed class FirebaseAuthResult {
    class Success(val user: FirebaseUser) : FirebaseAuthResult()
    object Loading : FirebaseAuthResult()
    class Error(val error: Exception) : FirebaseAuthResult()
}
