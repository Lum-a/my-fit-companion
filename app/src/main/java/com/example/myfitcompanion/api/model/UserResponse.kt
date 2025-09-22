package com.example.myfitcompanion.api.model

import com.example.myfitcompanion.db.room.entities.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable


@Serializable
@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "firstName")
    val firstName: String?,
    @Json(name = "lastName")
    val lastName: String?,
    @Json(name = "email")
    val email: String,
    @Json(name = "role")
    val role: String,
    @Json(name = "height")
    val height: Float? = null,
    @Json(name = "weight")
    val weight: Float? = null,
    @Json(name = "bodyFat")
    val bodyFat: Float? = null,
    @Json(name = "goal")
    val goal: String? = null,
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "imageUrl")
    val imageUrl: String? = null
) {
    fun asUser(): User {
        return User(
            id = id,
            name = name,
            firstName = name,
            lastName = lastName,
            email = email,
            height = height,
            weight = weight,
            bodyFat = bodyFat,
            goal = goal,
            role = role,
            createdAt = createdAt,
            imageUrl = imageUrl
        )
    }
}
