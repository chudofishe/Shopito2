package com.chudofishe.shopito.di

import com.chudofishe.shopito.data.firebase.repo.FirebaseEmailToUIdRepository
import com.chudofishe.shopito.data.firebase.repo.FirebaseFriendRepository
import com.chudofishe.shopito.data.firebase.repo.FirebaseFriendRequestRepository
import com.chudofishe.shopito.data.firebase.repo.FirebaseUserDataRepository
import org.koin.androidx.compose.get

import org.koin.dsl.module

val firebaseModule = module {
    single { provideFirebaseUserData(get()) }
    single { provideFirebaseFriendRepository() }
    single {
        provideFirebaseFriendRequestRepository(
            firebaseUserDataRepository = get(),
            emailToUIdRepository = get()
        )
    }
    single { provideFirebaseEmailToUidRepository() }
}

fun provideFirebaseUserData(emailToUIdRepository: FirebaseEmailToUIdRepository) = FirebaseUserDataRepository(emailToUIdRepository)
fun provideFirebaseFriendRepository() = FirebaseFriendRepository()
fun provideFirebaseFriendRequestRepository(
    firebaseUserDataRepository: FirebaseUserDataRepository,
    emailToUIdRepository: FirebaseEmailToUIdRepository
) = FirebaseFriendRequestRepository(
    firebaseUserDataRepository, emailToUIdRepository
)
fun provideFirebaseEmailToUidRepository() = FirebaseEmailToUIdRepository()