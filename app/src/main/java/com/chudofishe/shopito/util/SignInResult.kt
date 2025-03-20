package com.chudofishe.shopito.util

import com.chudofishe.shopito.model.UserData

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)
