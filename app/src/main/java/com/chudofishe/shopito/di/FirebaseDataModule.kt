package com.chudofishe.shopito.di

import com.chudofishe.shopito.data.firebase.FirebaseUserData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

import org.koin.dsl.module

val firebaseModule = module {
    single { provideFirebaseAuth() }
    single { provideFirebaseUserData(get()) }
}

fun provideFirebaseAuth() = Firebase.auth
fun provideFirebaseUserData(auth: FirebaseAuth) = FirebaseUserData(auth)