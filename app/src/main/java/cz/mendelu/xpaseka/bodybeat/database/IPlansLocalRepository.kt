package cz.mendelu.xpaseka.bodybeat.database

import androidx.lifecycle.LiveData
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import cz.mendelu.xpaseka.bodybeat.model.Plan

interface IPlansLocalRepository {
    fun getAll(): LiveData<MutableList<Plan>>
    suspend fun findById(id: Long): Plan
    suspend fun insert(plan: Plan): Long
    fun getExercisesFromPlan(id: Long): LiveData<MutableList<Exercise>>
    suspend fun update(plan: Plan)
    suspend fun delete(plan: Plan)
}