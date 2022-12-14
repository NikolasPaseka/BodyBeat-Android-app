package cz.mendelu.xpaseka.bodybeat.database.irepository

import cz.mendelu.xpaseka.bodybeat.model.Exercise

interface IExerciseLocalRepository {
    suspend fun getAll(): MutableList<Exercise>
    suspend fun getNumberOfExercisesByPlan(id: Long): Int
    suspend fun insert(exercise: Exercise): Long
    suspend fun update(exercise: Exercise)
    suspend fun delete(exercise: Exercise)
}