package cz.mendelu.xpaseka.bodybeat.di

import cz.mendelu.xpaseka.bodybeat.database.PlansDao
import cz.mendelu.xpaseka.bodybeat.database.ScheduleDao
import cz.mendelu.xpaseka.bodybeat.database.ScheduleLogDao
import cz.mendelu.xpaseka.bodybeat.database.WorkoutsDatabase
import org.koin.dsl.module

val daoModule = module {
    single {
        providePlansDao(get())
    }
    single {
        provideScheduleDao(get())
    }
    single {
        provideScheduleLogDao(get())
    }
}

fun providePlansDao(database: WorkoutsDatabase): PlansDao = database.plansDao()
fun provideScheduleDao(database: WorkoutsDatabase): ScheduleDao = database.scheduleDao()
fun provideScheduleLogDao(database: WorkoutsDatabase): ScheduleLogDao = database.scheduleLogDao()