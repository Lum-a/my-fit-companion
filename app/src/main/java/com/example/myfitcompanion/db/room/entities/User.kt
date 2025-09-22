package com.example.myfitcompanion.db.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myfitcompanion.api.model.UserResponse

@Entity(tableName = "user")
data class User(
    @PrimaryKey val id: Int,
    val name: String,
    val firstName: String?,
    val lastName: String?,
    val email: String,
    val role: String,
    val height: Float?,
    val weight: Float?,
    val bodyFat: Float?,
    val goal: String?,
    val createdAt: String,
    val imageUrl: String? = null
)
