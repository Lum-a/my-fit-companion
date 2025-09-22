package com.example.myfitcompanion.db.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

//personal trainer :P
@Entity(tableName = "trainers")
data class Trainer(
    @PrimaryKey(autoGenerate = true)
    val trainerId: Int,
    val firstName: String,
    val lastName: String,
    val specialization: String? = null,
    val contactInfo: String? = null
)
