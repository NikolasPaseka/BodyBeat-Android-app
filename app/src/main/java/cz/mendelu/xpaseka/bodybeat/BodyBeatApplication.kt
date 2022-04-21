package cz.mendelu.xpaseka.bodybeat

import android.app.Application
import cz.mendelu.xpaseka.bodybeat.di.daoModule
import cz.mendelu.xpaseka.bodybeat.di.databaseModule
import cz.mendelu.xpaseka.bodybeat.di.repositoryModule
import cz.mendelu.xpaseka.bodybeat.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BodyBeatApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(databaseModule, daoModule, repositoryModule, viewModelModule)
        }
    }
}