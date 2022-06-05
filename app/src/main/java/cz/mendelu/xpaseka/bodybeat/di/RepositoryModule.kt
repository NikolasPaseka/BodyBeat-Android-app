package cz.mendelu.xpaseka.bodybeat.di

import cz.mendelu.xpaseka.bodybeat.database.Dao.ExerciseDao
import cz.mendelu.xpaseka.bodybeat.database.Dao.PlansDao
import cz.mendelu.xpaseka.bodybeat.database.Dao.ScheduleDao
import cz.mendelu.xpaseka.bodybeat.database.Dao.ScheduleLogDao
import cz.mendelu.xpaseka.bodybeat.database.irepository.IExerciseLocalRepository
import cz.mendelu.xpaseka.bodybeat.database.irepository.IPlansLocalRepository
import cz.mendelu.xpaseka.bodybeat.database.irepository.IScheduleLocalRepository
import cz.mendelu.xpaseka.bodybeat.database.irepository.IScheduleLogLocalRepository
import cz.mendelu.xpaseka.bodybeat.database.repository.ExerciseLocalRepositoryImpl
import cz.mendelu.xpaseka.bodybeat.database.repository.PlansLocalRepositoryImpl
import cz.mendelu.xpaseka.bodybeat.database.repository.ScheduleLocalRepositoryImpl
import cz.mendelu.xpaseka.bodybeat.database.repository.ScheduleLogLocalRepositoryImpl
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
    single {
        provideExerciseLocalRepository(get())
    }
}

fun providePlansLocalRepository(plansDao: PlansDao): IPlansLocalRepository = PlansLocalRepositoryImpl(plansDao)
fun provideScheduleLocalRepository(scheduleDao: ScheduleDao): IScheduleLocalRepository = ScheduleLocalRepositoryImpl(scheduleDao)
fun provideScheduleLogLocalRepository(scheduleLogDao: ScheduleLogDao): IScheduleLogLocalRepository = ScheduleLogLocalRepositoryImpl(scheduleLogDao)
fun provideExerciseLocalRepository(exerciseDao: ExerciseDao): IExerciseLocalRepository = ExerciseLocalRepositoryImpl(exerciseDao)