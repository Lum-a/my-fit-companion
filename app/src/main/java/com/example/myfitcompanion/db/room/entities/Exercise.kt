package com.example.myfitcompanion.db.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true) val exerciseId: Int,
    val name: String,
    val type: String,
    val description: String,
    val videoId: String
)
