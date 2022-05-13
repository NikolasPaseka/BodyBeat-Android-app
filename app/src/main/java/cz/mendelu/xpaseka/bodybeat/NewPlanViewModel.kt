package cz.mendelu.xpaseka.bodybeat

import androidx.lifecycle.ViewModel
import cz.mendelu.xpaseka.bodybeat.database.IPlansLocalRepository
import cz.mendelu.xpaseka.bodybeat.model.Plan

class NewPlanViewModel(private val repository: IPlansLocalRepository) : ViewModel() {

    val plan = Plan("", 0, 0)

    suspend fun insertPlan() {
        repository.insert(plan)
    }

}