package cz.mendelu.xpaseka.bodybeat.database

import androidx.lifecycle.LiveData
import cz.mendelu.xpaseka.bodybeat.model.Schedule

class ScheduleLocalRepositoryImpl(private val scheduleDao: ScheduleDao) : IScheduleLocalRepository {
    override fun getAll(): LiveData<MutableList<Schedule>> {
        return scheduleDao.getAll()
    }

    override fun getByPlanId(id: Long): LiveData<MutableList<Schedule>> {
        return scheduleDao.getByPlanId(id)
    }

    override suspend fun getByDay(day: String): MutableList<Schedule> {
        return scheduleDao.getByDay(day)
    }

    override suspend fun insert(schedule: Schedule): Long {
        return scheduleDao.insert(schedule)
    }

    override suspend fun update(schedule: Schedule) {
        scheduleDao.update(schedule)
    }

    override suspend fun delete(schedule: Schedule) {
        scheduleDao.delete(schedule)
    }
}