package com.example.myfitcompanion.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String,
    val role: String,
    val height: Float?,
    val weight: Float?,
    val bodyFat: Float?,
    val goal: String?,
    val createdAt: String
)
