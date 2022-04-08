package com.picpay.desafio.android

import android.app.Application
import com.picpay.desafio.android.di.remoteDataModule
import com.picpay.desafio.android.di.userCaseModule
import com.picpay.desafio.android.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(listOf(remoteDataModule, userCaseModule, viewModelModule))
        }
    }

    override fun onTerminate() {
        stopKoin()
        super.onTerminate()
    }
}