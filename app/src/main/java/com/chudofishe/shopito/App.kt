package com.chudofishe.shopito

import android.app.Application
import com.chudofishe.shopito.di.dbModule
import com.chudofishe.shopito.di.vmModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                vmModule,
                dbModule
            )
        }
    }
}