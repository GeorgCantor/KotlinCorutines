package com.georgcantor.kotlincorutines

import android.app.Application
import com.georgcantor.kotlincorutines.di.apiModule
import com.georgcantor.kotlincorutines.di.repositoryModule
import com.georgcantor.kotlincorutines.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(apiModule, repositoryModule, viewModelModule))
        }
    }
}