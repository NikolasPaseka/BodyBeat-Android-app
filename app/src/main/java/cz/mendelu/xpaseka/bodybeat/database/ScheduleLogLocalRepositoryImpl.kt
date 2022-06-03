package cz.mendelu.xpaseka.bodybeat.database

import cz.mendelu.xpaseka.bodybeat.model.ScheduleLog

class ScheduleLogLocalRepositoryImpl(private val scheduleLogDao: ScheduleLogDao) : IScheduleLogLocalRepository {
    override suspend fun getAll(): MutableList<ScheduleLog> {
        return scheduleLogDao.getAll()
    }

    override suspend fun insert(scheduleLog: ScheduleLog): Long {
        return scheduleLogDao.insert(scheduleLog)
    }

    override suspend fun update(scheduleLog: ScheduleLog) {
        scheduleLogDao.update(scheduleLog)
    }

    override suspend fun delete(scheduleLog: ScheduleLog) {
        scheduleLogDao.delete(scheduleLog)
    }
}