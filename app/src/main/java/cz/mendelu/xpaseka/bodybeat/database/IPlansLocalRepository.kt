package cz.mendelu.xpaseka.bodybeat.database

import androidx.lifecycle.LiveData
import cz.mendelu.xpaseka.bodybeat.model.Plan

interface IPlansLocalRepository {
    fun getAll(): LiveData<MutableList<Plan>>
    fun findById(id: Long): Plan
    fun insert(plan: Plan): Long
    fun update(plan: Plan)
    fun delete(plan: Plan)
}