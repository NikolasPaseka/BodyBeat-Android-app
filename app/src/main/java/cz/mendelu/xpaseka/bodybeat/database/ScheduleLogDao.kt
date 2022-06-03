package cz.mendelu.xpaseka.bodybeat.database

import androidx.lifecycle.LiveData
import androidx.room.*
import cz.mendelu.xpaseka.bodybeat.model.Schedule
import cz.mendelu.xpaseka.bodybeat.model.ScheduleLog

@Dao
interface ScheduleLogDao {
    @Query("SELECT * FROM schedule_log")
    suspend fun getAll(): MutableList<ScheduleLog>

    @Insert
    suspend fun insert(scheduleLog: ScheduleLog): Long

    @Update
    suspend fun update(scheduleLog: ScheduleLog)

    @Delete
    suspend fun delete(scheduleLog: ScheduleLog)
}