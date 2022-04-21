package cz.mendelu.xpaseka.bodybeat.di

import cz.mendelu.xpaseka.bodybeat.database.PlansDao
import cz.mendelu.xpaseka.bodybeat.database.WorkoutsDatabase
import org.koin.dsl.module

val daoModule = module {
    single {
        providePlansDao(get())
    }
}

fun providePlansDao(database: WorkoutsDatabase): PlansDao = database.plansDao()