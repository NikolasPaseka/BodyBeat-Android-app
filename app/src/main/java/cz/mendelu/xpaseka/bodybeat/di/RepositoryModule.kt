package cz.mendelu.xpaseka.bodybeat.di

import cz.mendelu.xpaseka.bodybeat.database.*
import org.koin.dsl.module

val repositoryModule = module {
    single {
        providePlansLocalRepository(get())
    }
    single {
        provideScheduleLocalRepository(get())
    }
    single {
        provideScheduleLogLocalRepository(get())
    }
}

fun providePlansLocalRepository(plansDao: PlansDao): IPlansLocalRepository = PlansLocalRepositoryImpl(plansDao)
fun provideScheduleLocalRepository(scheduleDao: ScheduleDao): IScheduleLocalRepository = ScheduleLocalRepositoryImpl(scheduleDao)
fun provideScheduleLogLocalRepository(scheduleLogDao: ScheduleLogDao): IScheduleLogLocalRepository = ScheduleLogLocalRepositoryImpl(scheduleLogDao)