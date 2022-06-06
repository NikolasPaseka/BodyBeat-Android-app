package cz.mendelu.xpaseka.bodybeat.fragments.plans

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import cz.mendelu.xpaseka.bodybeat.database.irepository.IExerciseLocalRepository
import cz.mendelu.xpaseka.bodybeat.database.irepository.IPlansLocalRepository
import cz.mendelu.xpaseka.bodybeat.model.Plan

class PlansViewModel(private val repository: IPlansLocalRepository, private val exerciseRepository: IExerciseLocalRepository) : ViewModel() {
    fun getAll(): LiveData<MutableList<Plan>> {
        return repository.getAll()
    }

    suspend fun getNumberOfExercisesByPlan(id: Long): Int {
        return exerciseRepository.getNumberOfExercisesByPlan(id)
    }
}