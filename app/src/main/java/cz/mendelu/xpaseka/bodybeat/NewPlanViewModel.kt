package cz.mendelu.xpaseka.bodybeat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.mendelu.xpaseka.bodybeat.database.IPlansLocalRepository
import cz.mendelu.xpaseka.bodybeat.database.IScheduleLocalRepository
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import cz.mendelu.xpaseka.bodybeat.model.Plan
import cz.mendelu.xpaseka.bodybeat.model.Schedule
import kotlin.system.exitProcess

class NewPlanViewModel(private val repository: IPlansLocalRepository, private val scheduleRepository: IScheduleLocalRepository) : ViewModel() {

    var plan = Plan("", 0, 0)
    var planId: Long? = null

    var exercises: MutableList<Exercise> = mutableListOf()
    var schedules: MutableList<Schedule> = mutableListOf()

    suspend fun insertPlan() {
        planId = repository.insert(plan)
    }

    suspend fun insertExercise(exercise: Exercise) {
        repository.insertExercise(exercise)
    }

    suspend fun insertSchedule(schedule: Schedule) {
        scheduleRepository.insert(schedule)
    }

//    suspend fun getSchedules(id: Long): LiveData<MutableList<Schedule>> {
//        return scheduleRepository.getByPlanId(id)
//    }

    suspend fun getData(id: Long) {
        planId = id
        plan = repository.findById(id)
        if (exercises.isEmpty()) {
            exercises = repository.getExercisesTesting(id)
        }
        if (schedules.isEmpty()) {
            schedules = scheduleRepository.getByPlanId(id)
        }
    }

    suspend fun updatePlan() {
        repository.update(plan)
    }
}