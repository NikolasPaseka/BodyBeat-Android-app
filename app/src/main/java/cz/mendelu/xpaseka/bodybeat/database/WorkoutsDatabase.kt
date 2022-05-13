package cz.mendelu.xpaseka.bodybeat.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import cz.mendelu.xpaseka.bodybeat.model.Plan

@Database(entities = [Plan::class, Exercise::class], version = 1, exportSchema = true)
abstract class WorkoutsDatabase: RoomDatabase() {

    abstract fun plansDao(): PlansDao

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