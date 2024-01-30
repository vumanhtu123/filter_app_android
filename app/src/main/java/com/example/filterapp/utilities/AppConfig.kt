package com.example.filterapp.utilities

import android.app.Application
import com.example.filterapp.dependencyinjection.repositoryModule
import com.example.filterapp.dependencyinjection.viewModeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppConfig : Application(){
    //application nó là một base class có vòng đời gắn vs app,
    // dễ thấy nhất là thường dùng nó để tạo chứa biến toàn cục tồn tại lâu dài trong app
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppConfig)
            modules(listOf(repositoryModule, viewModeModule))
        }
    }
}