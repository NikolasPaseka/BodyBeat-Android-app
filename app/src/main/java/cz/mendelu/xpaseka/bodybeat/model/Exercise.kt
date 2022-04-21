package cz.mendelu.xpaseka.bodybeat.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "repeats")
    var repeats: Int,

    @ColumnInfo(name = "sets")
    var sets: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
}