package com.chudofishe.shopito.model

data class UserData(
    val userId: String = "",
    val username: String? = "",
    val email: String? = "",
    val profilePictureUrl: String? = "",

    val friends: List<String> = emptyList(),
    val friendRequests: List<String> = emptyList()
)
