package com.example.myfitcompanion.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true) val exerciseId: Long = 0,
    val name: String,
    val type: String,
    val duration: Int, // in minutes
    val caloriesBurned: Int
)
