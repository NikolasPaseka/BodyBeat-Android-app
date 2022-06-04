package cz.mendelu.xpaseka.bodybeat.di

import cz.mendelu.xpaseka.bodybeat.database.*
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
    single {
        provideExerciseDao(get())
    }
}

fun providePlansDao(database: WorkoutsDatabase): PlansDao = database.plansDao()
fun provideScheduleDao(database: WorkoutsDatabase): ScheduleDao = database.scheduleDao()
fun provideScheduleLogDao(database: WorkoutsDatabase): ScheduleLogDao = database.scheduleLogDao()
fun provideExerciseDao(database: WorkoutsDatabase): ExerciseDao = database.ExerciseDao()