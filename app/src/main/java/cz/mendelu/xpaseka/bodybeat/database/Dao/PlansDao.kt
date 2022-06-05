package cz.mendelu.xpaseka.bodybeat.database.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import cz.mendelu.xpaseka.bodybeat.model.Plan


@Dao
interface PlansDao {
    @Query("SELECT * FROM plans")
    fun getAll(): LiveData<MutableList<Plan>>

    @Query("SELECT * FROM plans WHERE id = :id")
    suspend fun findById(id: Long): Plan

    @Query("SELECT * FROM exercises WHERE plan_id = :id")
    fun getExercisesFromPlan(id: Long): LiveData<MutableList<Exercise>>

    @Query("SELECT * FROM exercises WHERE plan_id = :id")
    suspend fun getExercisesTesting(id: Long): MutableList<Exercise>

    @Insert
    suspend fun insert(plan: Plan): Long

    @Insert
    suspend fun insertExercise(exercise: Exercise): Long

    @Update
    suspend fun update(plan: Plan)

    @Delete
    suspend fun delete(plan: Plan)
}