package com.example.myfitcompanion.db.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val id: Int,
    val username: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String,
    val role: String,
    val height: Float?,
    val weight: Float?,
    val bodyFat: Float?,
    val goalBodyFat: Float?,
    val goalWeight: Float?,
    val createdAt: String,
    val imageUrl: String? = null
)
