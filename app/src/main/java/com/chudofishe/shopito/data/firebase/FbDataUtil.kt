package com.chudofishe.shopito.data.firebase

import android.util.Base64
import com.chudofishe.shopito.model.UserData
object FbDataUtil {

    fun currentUserToFriendRequest(user: UserData): UserData {
        return UserData(
            userId = user.userId,
            email = user.email.toString(),
            photoUrl = user.photoUrl.toString(),
            name = user.name.toString()
        )
    }

    fun encodeEmail(email: String): String {
        return Base64.encodeToString(email.toByteArray(), Base64.URL_SAFE or Base64.NO_WRAP)
    }

    fun decodeEmail(encoded: String): String {
        return String(Base64.decode(encoded, Base64.URL_SAFE or Base64.NO_WRAP))
    }

}