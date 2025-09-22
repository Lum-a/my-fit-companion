package com.example.myfitcompanion.db.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true) val workoutId: Int,
    val name: String,
    val description: String?,
    val imageUrl: String?
)
