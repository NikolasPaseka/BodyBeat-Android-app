package cz.mendelu.xpaseka.bodybeat.database.irepository

import androidx.lifecycle.LiveData
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import cz.mendelu.xpaseka.bodybeat.model.Plan

interface IPlansLocalRepository {
    fun getAll(): LiveData<MutableList<Plan>>
    suspend fun findById(id: Long): Plan
    suspend fun insert(plan: Plan): Long
    suspend fun insertExercise(exercise: Exercise): Long
    fun getExercisesFromPlan(id: Long): LiveData<MutableList<Exercise>>
    suspend fun getExercisesTesting(id: Long): MutableList<Exercise>
    suspend fun update(plan: Plan)
    suspend fun delete(plan: Plan)
}