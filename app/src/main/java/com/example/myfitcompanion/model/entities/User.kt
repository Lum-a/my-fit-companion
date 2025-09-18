package com.example.myfitcompanion.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myfitcompanion.api.model.UserResponse

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
    val createdAt: String,
    val photoUrl: String? = null // Added photoUrl for profile image
) {
    fun asResponse(): UserResponse {
        return UserResponse(
            id = id,
            name = name,
            email = email,
            role = role,
            height = height,
            weight = weight,
            bodyFat = bodyFat,
            goal = goal,
            createdAt = createdAt
            // photoUrl is not in UserResponse, so not mapped
        )
    }
}
