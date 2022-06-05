package cz.mendelu.xpaseka.bodybeat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import cz.mendelu.xpaseka.bodybeat.database.irepository.IPlansLocalRepository
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import cz.mendelu.xpaseka.bodybeat.model.Plan

class PlanDetailViewModel(private val repository: IPlansLocalRepository) : ViewModel() {
    var id: Long? = null

    lateinit var plan: Plan
    var exerciseList: MutableList<Exercise> = mutableListOf()

    suspend fun getPlan(id: Long): Plan {
        return repository.findById(id)
    }

    fun getExercisesFromPlan(id: Long): LiveData<MutableList<Exercise>> {
        return repository.getExercisesFromPlan(id)
    }

    suspend fun deletePlan() {
        repository.delete(plan)
    }
}