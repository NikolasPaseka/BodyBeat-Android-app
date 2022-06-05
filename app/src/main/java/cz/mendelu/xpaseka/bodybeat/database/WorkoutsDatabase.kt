package cz.mendelu.xpaseka.bodybeat.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.mendelu.xpaseka.bodybeat.database.Dao.ExerciseDao
import cz.mendelu.xpaseka.bodybeat.database.Dao.PlansDao
import cz.mendelu.xpaseka.bodybeat.database.Dao.ScheduleDao
import cz.mendelu.xpaseka.bodybeat.database.Dao.ScheduleLogDao
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import cz.mendelu.xpaseka.bodybeat.model.Plan
import cz.mendelu.xpaseka.bodybeat.model.Schedule
import cz.mendelu.xpaseka.bodybeat.model.ScheduleLog

@Database(entities = [Plan::class, Exercise::class, Schedule::class, ScheduleLog::class],
    version = 2, exportSchema = true)
abstract class WorkoutsDatabase: RoomDatabase() {

    abstract fun plansDao(): PlansDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun scheduleLogDao(): ScheduleLogDao
    abstract fun ExerciseDao(): ExerciseDao

    companion object {
        private var INSTANCE: WorkoutsDatabase? = null
        fun getDatabase(context: Context): WorkoutsDatabase {
            if (INSTANCE == null) {
                synchronized(WorkoutsDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            WorkoutsDatabase::class.java, "workouts_database"
                        ).fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}