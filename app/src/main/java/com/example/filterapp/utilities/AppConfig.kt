package com.example.filterapp.utilities

import android.app.Application
import com.example.filterapp.dependencyinjection.repositoryModule
import com.example.filterapp.dependencyinjection.viewModeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppConfig : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppConfig)
            modules(listOf(repositoryModule, viewModeModule))
        }
    }
}