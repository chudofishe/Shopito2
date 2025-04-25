package com.chudofishe.shopito.data.firebase.repo

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

abstract class FirebaseDataRepository {
    protected val DB_URL = "https://shopito-d61cb-default-rtdb.europe-west1.firebasedatabase.app/"
    protected val db = Firebase.database(DB_URL)
}