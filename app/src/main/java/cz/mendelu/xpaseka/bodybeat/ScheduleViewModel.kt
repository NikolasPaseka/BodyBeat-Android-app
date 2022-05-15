package cz.mendelu.xpaseka.bodybeat

import androidx.lifecycle.ViewModel
import cz.mendelu.xpaseka.bodybeat.database.IPlansLocalRepository
import cz.mendelu.xpaseka.bodybeat.database.IScheduleLocalRepository
import cz.mendelu.xpaseka.bodybeat.model.Plan
import cz.mendelu.xpaseka.bodybeat.model.Schedule

class ScheduleViewModel(
    private val scheduleRepository: IScheduleLocalRepository,
    private val plansRepository: IPlansLocalRepository) : ViewModel() {

    var schedule: MutableList<Schedule> = mutableListOf()
    val plans: MutableList<Plan> = mutableListOf()

    suspend fun getScheduleByDay(day: String): MutableList<Schedule> {
        return scheduleRepository.getByDay(day)
    }

    suspend fun loadPlansBySchedule() {
        plans.clear()
        schedule.forEach { s ->
            plans.add(plansRepository.findById(s.planId!!))
        }
    }
}