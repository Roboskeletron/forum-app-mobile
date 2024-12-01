package ru.vsu.forum

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import ru.vsu.forum.di.appModule
import ru.vsu.forum.di.dataModule
import ru.vsu.forum.di.domainModule
import ru.vsu.forum.di.networkModule

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(networkModule, dataModule, domainModule, appModule)
        }
    }
}