package com.example.myfitcompanion.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "classes")
data class GymClass(
    @PrimaryKey(autoGenerate = true) val classId: Long = 0,
    val name: String,
    val description: String? = null,
    val trainerId: Long,   // FK -> Trainer
    val startTime: Long,   // store as timestamp (millis)
    val endTime: Long,
    val capacity: Int
)
