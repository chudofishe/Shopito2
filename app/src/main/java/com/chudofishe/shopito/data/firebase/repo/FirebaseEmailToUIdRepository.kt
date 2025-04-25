package com.chudofishe.shopito.data.firebase.repo

import com.chudofishe.shopito.data.firebase.DBRef
import com.chudofishe.shopito.data.firebase.FbDataUtil
import com.google.firebase.database.getValue
import kotlinx.coroutines.tasks.await

class FirebaseEmailToUIdRepository : FirebaseDataRepository() {

    fun setEmailToUid(email: String, uid: String) {
        val encoded = FbDataUtil.encodeEmail(email)
        db.getReference(DBRef.EMAIL_TO_UID).child(encoded).setValue(uid)
    }

    suspend fun getUidByEmail(email: String): String? {
        val encoded = FbDataUtil.encodeEmail(email)
        return db.getReference(DBRef.EMAIL_TO_UID).child(encoded).get().await().getValue<String>()
    }
}