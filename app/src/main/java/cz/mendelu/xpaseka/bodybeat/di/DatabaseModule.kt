package cz.mendelu.xpaseka.bodybeat.di

import android.content.Context
import cz.mendelu.xpaseka.bodybeat.database.WorkoutsDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        provideDatabase(androidContext())
    }
}

fun provideDatabase(context: Context): WorkoutsDatabase = WorkoutsDatabase.getDatabase(context)