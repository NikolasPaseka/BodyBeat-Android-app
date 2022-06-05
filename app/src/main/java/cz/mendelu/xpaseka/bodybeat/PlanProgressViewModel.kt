package cz.mendelu.xpaseka.bodybeat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import cz.mendelu.xpaseka.bodybeat.database.irepository.IPlansLocalRepository
import cz.mendelu.xpaseka.bodybeat.database.irepository.IScheduleLogLocalRepository
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import cz.mendelu.xpaseka.bodybeat.model.Plan
import cz.mendelu.xpaseka.bodybeat.model.ScheduleLog

class PlanProgressViewModel(
    private val repository: IPlansLocalRepository,
    private val scheduleLogRepository: IScheduleLogLocalRepository
    ) : ViewModel() {

    lateinit var plan: Plan
    var exerciseList: MutableList<Exercise> = mutableListOf()
    var currentExercise: Exercise? = null
    var currentSet = 1

    suspend fun getPlan(id: Long): Plan {
        return repository.findById(id)
    }

    fun getExercisesFromPlan(id: Long): LiveData<MutableList<Exercise>> {
        return repository.getExercisesFromPlan(id)
    }

    suspend fun logFinishedWorkout() {
        scheduleLogRepository.insert(ScheduleLog(System.currentTimeMillis()))
    }

}