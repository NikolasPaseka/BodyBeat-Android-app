package cz.mendelu.xpaseka.bodybeat.database

import androidx.lifecycle.LiveData

import cz.mendelu.xpaseka.bodybeat.model.Schedule

interface IScheduleLocalRepository {
    fun getAll(): LiveData<MutableList<Schedule>>
    fun getByPlanId(id: Long): LiveData<MutableList<Schedule>>
    suspend fun getByDay(day: String): MutableList<Schedule>
    suspend fun insert(schedule: Schedule): Long
    suspend fun update(schedule: Schedule)
    suspend fun delete(schedule: Schedule)
}