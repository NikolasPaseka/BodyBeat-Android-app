package cz.mendelu.xpaseka.bodybeat.database

import androidx.lifecycle.LiveData
import cz.mendelu.xpaseka.bodybeat.model.Plan

class PlansLocalRepositoryImpl(private val plansDao: PlansDao) : IPlansLocalRepository {
    override fun getAll(): LiveData<MutableList<Plan>> {
        return plansDao.getAll()
    }

    override fun findById(id: Long): Plan {
        return plansDao.findById(id)
    }

    override fun insert(plan: Plan): Long {
        return plansDao.insert(plan)
    }

    override fun update(plan: Plan) {
        plansDao.update(plan)
    }

    override fun delete(plan: Plan) {
        plansDao.delete(plan)
    }
}