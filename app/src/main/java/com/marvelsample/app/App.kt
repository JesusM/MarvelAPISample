package com.marvelsample.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.marvelsample.app.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    companion object {
        init {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)

            modules(appModule, networkModule, repositoryModule, userCases, systemModule, viewModels)
        }
    }
}
