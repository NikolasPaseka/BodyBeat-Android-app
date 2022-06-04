package cz.mendelu.xpaseka.bodybeat.database

import cz.mendelu.xpaseka.bodybeat.model.Exercise
import cz.mendelu.xpaseka.bodybeat.model.ScheduleLog

class ExerciseLocalRepositoryImpl(private val exerciseDao: ExerciseDao) : IExerciseLocalRepository {
    override suspend fun getAll(): MutableList<Exercise> {
        return exerciseDao.getAll()
    }

    override suspend fun insert(exercise: Exercise): Long {
        return exerciseDao.insert(exercise)
    }

    override suspend fun update(exercise: Exercise) {
        exerciseDao.update(exercise)
    }

    override suspend fun delete(exercise: Exercise) {
        exerciseDao.delete(exercise)
    }

}