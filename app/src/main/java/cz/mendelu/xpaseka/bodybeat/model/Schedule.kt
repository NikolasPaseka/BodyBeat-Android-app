package cz.mendelu.xpaseka.bodybeat.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "schedule",
    foreignKeys = [
        ForeignKey(
            entity = Plan::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("plan_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Schedule(
    @ColumnInfo(name = "day")
    var day: String,

    @ColumnInfo(name = "time")
    var time: Long
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

    @ColumnInfo(name = "plan_id")
    var planId: Long? = null
}