package cz.mendelu.xpaseka.bodybeat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.mendelu.xpaseka.bodybeat.database.IPlansLocalRepository
import cz.mendelu.xpaseka.bodybeat.database.IScheduleLocalRepository
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import cz.mendelu.xpaseka.bodybeat.model.Plan
import cz.mendelu.xpaseka.bodybeat.model.Schedule

class NewPlanViewModel(private val repository: IPlansLocalRepository, private val scheduleRepository: IScheduleLocalRepository) : ViewModel() {

    var plan = Plan("", 0, 0)
    var planId: Long? = null

    val exerciseList = MutableLiveData<MutableList<Exercise>>()
    val exercises: MutableList<Exercise> = mutableListOf()

    val scheduleList = MutableLiveData<MutableList<Schedule>>()
    val schedules: MutableList<Schedule> = mutableListOf()

    suspend fun insertPlan() {
        planId = repository.insert(plan)
    }

    suspend fun insertExercise(exercise: Exercise) {
        repository.insertExercise(exercise)
    }

    suspend fun insertSchedule(schedule: Schedule) {
        scheduleRepository.insert(schedule)
    }

    suspend fun getSchedules(id: Long): LiveData<MutableList<Schedule>> {
        return scheduleRepository.getByPlanId(id)
    }

}