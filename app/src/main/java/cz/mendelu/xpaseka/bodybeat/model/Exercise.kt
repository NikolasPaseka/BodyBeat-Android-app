package cz.mendelu.xpaseka.bodybeat.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "exercises",
    foreignKeys = [
        ForeignKey(
        entity = Plan::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("plan_id"),
        onDelete = CASCADE
        )
    ]
)
data class Exercise(
    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "repeats")
    var repeats: Int,

    @ColumnInfo(name = "sets")
    var sets: Int,

    @ColumnInfo(name = "plan_id")
    var planId: Long
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
}