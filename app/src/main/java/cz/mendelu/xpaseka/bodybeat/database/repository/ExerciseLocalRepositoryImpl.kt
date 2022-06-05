package cz.mendelu.xpaseka.bodybeat.database.repository

import cz.mendelu.xpaseka.bodybeat.database.Dao.ExerciseDao
import cz.mendelu.xpaseka.bodybeat.database.irepository.IExerciseLocalRepository
import cz.mendelu.xpaseka.bodybeat.model.Exercise

class ExerciseLocalRepositoryImpl(private val exerciseDao: ExerciseDao) : IExerciseLocalRepository {
    override suspend fun getAll(): MutableList<Exercise> {
        return exerciseDao.getAll()
    }

    override suspend fun getNumberOfExercisesByPlan(id: Long): Int {
        return exerciseDao.getNumberOfExercisesByPlan(id)
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