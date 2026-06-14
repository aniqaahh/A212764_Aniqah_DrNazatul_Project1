package com.example.a212764_aniqah_drnazatul_project1.data
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trails")
data class TrailEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val location: String,
    val difficulty: String,
    val rating: String,
    val imageRes: Int
)