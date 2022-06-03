package cz.mendelu.xpaseka.bodybeat.database

import androidx.lifecycle.LiveData
import cz.mendelu.xpaseka.bodybeat.model.Exercise
import cz.mendelu.xpaseka.bodybeat.model.Plan

class PlansLocalRepositoryImpl(private val plansDao: PlansDao) : IPlansLocalRepository {
    override fun getAll(): LiveData<MutableList<Plan>> {
        return plansDao.getAll()
    }

    override suspend fun findById(id: Long): Plan {
        return plansDao.findById(id)
    }

    override suspend fun insert(plan: Plan): Long {
        return plansDao.insert(plan)
    }

    override suspend fun insertExercise(exercise: Exercise): Long {
        return plansDao.insertExercise(exercise)
    }

    override fun getExercisesFromPlan(id: Long): LiveData<MutableList<Exercise>> {
        return plansDao.getExercisesFromPlan(id)
    }

    override suspend fun getExercisesTesting(id: Long): MutableList<Exercise> {
        return plansDao.getExercisesTesting(id)
    }

    override suspend fun update(plan: Plan) {
        plansDao.update(plan)
    }

    override suspend fun delete(plan: Plan) {
        plansDao.delete(plan)
    }
}