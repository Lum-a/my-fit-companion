package com.example.myfitcompanion.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

//personal trainer :P
@Entity(tableName = "trainers")
data class Trainer(
    @PrimaryKey(autoGenerate = true)
    val trainerId: Long = 0,
    val name: String,
    val specialization: String? = null,
    val contactInfo: String? = null
)
