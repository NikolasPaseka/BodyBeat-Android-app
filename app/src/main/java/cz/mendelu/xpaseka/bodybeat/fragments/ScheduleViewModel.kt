package cz.mendelu.xpaseka.bodybeat.fragments

import androidx.lifecycle.ViewModel
import cz.mendelu.xpaseka.bodybeat.database.irepository.IPlansLocalRepository
import cz.mendelu.xpaseka.bodybeat.database.irepository.IScheduleLocalRepository
import cz.mendelu.xpaseka.bodybeat.database.irepository.IScheduleLogLocalRepository
import cz.mendelu.xpaseka.bodybeat.model.Schedule
import cz.mendelu.xpaseka.bodybeat.model.ScheduleLog

class ScheduleViewModel(
    private val scheduleRepository: IScheduleLocalRepository,
    private val plansRepository: IPlansLocalRepository,
    private val scheduleLogRepository: IScheduleLogLocalRepository
    ) : ViewModel() {

    data class PlanSchedule(var title: String, var time: Long)

    var planSchedules: MutableList<PlanSchedule> = mutableListOf()

    var schedule: MutableList<Schedule> = mutableListOf()

    suspend fun getSchedulesByDay(day: String) {
        schedule.clear()
        planSchedules.clear()
        schedule = scheduleRepository.getByDay(day)
        schedule.forEach { s ->
            val plan = plansRepository.findById(s.planId!!)
            planSchedules.add(PlanSchedule(plan.title, s.time))
        }
    }

    suspend fun getSchedules(): MutableList<Schedule> {
        return scheduleRepository.getArr()
    }

    suspend fun getSchedulesLog(): MutableList<ScheduleLog> {
        return scheduleLogRepository.getAll()
    }
}