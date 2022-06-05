package cz.mendelu.xpaseka.bodybeat.database.irepository

import androidx.lifecycle.LiveData

import cz.mendelu.xpaseka.bodybeat.model.Schedule

interface IScheduleLocalRepository {
    fun getAll(): LiveData<MutableList<Schedule>>
    suspend fun getByPlanId(id: Long): MutableList<Schedule>
    suspend fun getByDay(day: String): MutableList<Schedule>
    suspend fun getArr(): MutableList<Schedule>
    suspend fun insert(schedule: Schedule): Long
    suspend fun update(schedule: Schedule)
    suspend fun delete(schedule: Schedule)
}