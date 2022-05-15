package cz.mendelu.xpaseka.bodybeat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.mendelu.xpaseka.bodybeat.database.IPlansLocalRepository
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import cz.mendelu.xpaseka.bodybeat.model.Plan

class NewPlanViewModel(private val repository: IPlansLocalRepository) : ViewModel() {

    var plan = Plan("", 0, 0)
    var planId: Long? = null

    val exerciseList = MutableLiveData<MutableList<Exercise>>()
    val exercises: MutableList<Exercise> = mutableListOf()

    suspend fun insertPlan() {
        planId = repository.insert(plan)
    }

    suspend fun insertExercise(exercise: Exercise) {
        repository.insertExercise(exercise)
    }

}