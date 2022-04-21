package cz.mendelu.xpaseka.bodybeat.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plans")
data class Plan(
    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "timer_exercises")
    var timerExercises: Double,

    @ColumnInfo(name = "timer_series")
    var timerSeries: Double

    ) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

    @ColumnInfo(name = "description")
    var description: String? = null
}