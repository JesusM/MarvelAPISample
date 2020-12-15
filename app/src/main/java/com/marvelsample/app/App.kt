package com.marvelsample.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.marvelsample.app.di.*
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule

class App : Application(), DIAware {

    companion object {
        init {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    override val di by DI.lazy {
        import(androidXModule(this@App))
        import(appModule)
        import(networkModule)
        import(repositoryModule)
        import(systemModule)
        import(userCases)
    }
}
