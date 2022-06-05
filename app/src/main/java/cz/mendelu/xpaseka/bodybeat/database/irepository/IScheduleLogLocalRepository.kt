package cz.mendelu.xpaseka.bodybeat.database.irepository


import cz.mendelu.xpaseka.bodybeat.model.ScheduleLog

interface IScheduleLogLocalRepository {
    suspend fun getAll(): MutableList<ScheduleLog>
    suspend fun insert(scheduleLog: ScheduleLog): Long
    suspend fun update(scheduleLog: ScheduleLog)
    suspend fun delete(scheduleLog: ScheduleLog)
}