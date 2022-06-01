package cz.mendelu.xpaseka.bodybeat

import androidx.lifecycle.ViewModel
import cz.mendelu.xpaseka.bodybeat.database.IPlansLocalRepository
import cz.mendelu.xpaseka.bodybeat.database.IScheduleLocalRepository
import cz.mendelu.xpaseka.bodybeat.model.Plan
import cz.mendelu.xpaseka.bodybeat.model.Schedule

class ScheduleViewModel(
    private val scheduleRepository: IScheduleLocalRepository,
    private val plansRepository: IPlansLocalRepository) : ViewModel() {

    data class PlanSchedule(var title: String, var time: Long)

    var planSchedules: MutableList<PlanSchedule> = mutableListOf()

    var schedule: MutableList<Schedule> = mutableListOf()

    suspend fun getSchedulesByDay(day: String) {
        this.schedule = scheduleRepository.getByDay(day)
        schedule.forEach { s ->
            val plan = plansRepository.findById(s.planId!!)
            planSchedules.add(PlanSchedule(plan.title, s.time))
        }
    }
}