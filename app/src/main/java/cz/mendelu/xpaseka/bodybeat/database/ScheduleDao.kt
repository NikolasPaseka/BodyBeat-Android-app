package cz.mendelu.xpaseka.bodybeat.database

import androidx.lifecycle.LiveData
import androidx.room.*
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import cz.mendelu.xpaseka.bodybeat.model.Plan
import cz.mendelu.xpaseka.bodybeat.model.Schedule


@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule")
    fun getAll(): LiveData<MutableList<Schedule>>

    @Query("SELECT * FROM schedule WHERE plan_id = :id")
    fun getByPlanId(id: Long): LiveData<MutableList<Schedule>>

    @Query("SELECT * FROM schedule WHERE day = :day")
    suspend fun getByDay(day: String): MutableList<Schedule>

    @Insert
    suspend fun insert(schedule: Schedule): Long

    @Update
    suspend fun update(schedule: Schedule)

    @Delete
    suspend fun delete(schedule: Schedule)
}