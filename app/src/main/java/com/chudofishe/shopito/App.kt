package com.chudofishe.shopito

import android.app.Application
import com.chudofishe.shopito.data.db.repository.AuthRepository
import com.chudofishe.shopito.di.dbModule
import com.chudofishe.shopito.di.firebaseModule
import com.chudofishe.shopito.di.vmModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val authRepository: AuthRepository by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                vmModule,
                dbModule,
                firebaseModule
            )
        }

        applicationScope.launch {
            authRepository.syncAuthState()
        }
    }
}