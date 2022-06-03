package cz.mendelu.xpaseka.bodybeat.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule_log")
data class ScheduleLog(
    @ColumnInfo(name = "date")
    var date: Long,

) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
}