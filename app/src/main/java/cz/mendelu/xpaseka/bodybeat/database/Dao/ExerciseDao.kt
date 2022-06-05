package cz.mendelu.xpaseka.bodybeat.database.Dao

import androidx.room.*
import cz.mendelu.xpaseka.bodybeat.model.Exercise

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises")
    suspend fun getAll(): MutableList<Exercise>

    @Query("SELECT COUNT(*) FROM exercises WHERE plan_id = :id")
    suspend fun getNumberOfExercisesByPlan(id: Long): Int

    @Insert
    suspend fun insert(exercise: Exercise): Long

    @Update
    suspend fun update(exercise: Exercise)

    @Delete
    suspend fun delete(exercise: Exercise)
}