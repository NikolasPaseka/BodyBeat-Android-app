package cz.mendelu.xpaseka.bodybeat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import cz.mendelu.xpaseka.bodybeat.database.IPlansLocalRepository
import cz.mendelu.xpaseka.bodybeat.model.Plan

class PlansViewModel(private val repository: IPlansLocalRepository) : ViewModel() {
    fun getAll(): LiveData<MutableList<Plan>> {
        return repository.getAll()
    }
}