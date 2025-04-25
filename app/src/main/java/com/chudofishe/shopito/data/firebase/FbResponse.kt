package com.chudofishe.shopito.data.firebase

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
    object Success : FirebaseAuthResult()
    object Loading : FirebaseAuthResult()
    class Error(val error: Exception) : FirebaseAuthResult()
}
