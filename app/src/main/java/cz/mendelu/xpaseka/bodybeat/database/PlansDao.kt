package cz.mendelu.xpaseka.bodybeat.database

import androidx.lifecycle.LiveData
import androidx.room.*
import cz.mendelu.xpaseka.bodybeat.model.Plan


@Dao
interface PlansDao {
    @Query("SELECT * FROM plans")
    fun getAll(): LiveData<MutableList<Plan>>

    @Query("SELECT * FROM plans WHERE id = :id")
    fun findById(id: Long): Plan

    @Insert
    fun insert(plan: Plan): Long

    @Update
    fun update(plan: Plan)

    @Delete
    fun delete(plan: Plan)
}