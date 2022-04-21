package cz.mendelu.xpaseka.bodybeat.di

import cz.mendelu.xpaseka.bodybeat.database.IPlansLocalRepository
import cz.mendelu.xpaseka.bodybeat.database.PlansDao
import cz.mendelu.xpaseka.bodybeat.database.PlansLocalRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single {
        providePlansLocalRepository(get())
    }
}

fun providePlansLocalRepository(plansDao: PlansDao): IPlansLocalRepository = PlansLocalRepositoryImpl(plansDao)