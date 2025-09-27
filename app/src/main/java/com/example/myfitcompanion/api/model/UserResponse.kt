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
    @Json(name = "goalBodyFat")
    val goalBodyFat: Float? = null,
    @Json(name = "goalWeight")
    val goalWeight: Float? = null,
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "imageUrl")
    val imageUrl: String? = null
) {
    fun asUser(): User {
        return User(
            id = id,
            firstName = firstName,
            lastName = lastName,
            email = email,
            height = height,
            weight = weight,
            bodyFat = bodyFat,
            goalBodyFat = goalBodyFat,
            goalWeight = goalWeight,
            role = role,
            createdAt = createdAt,
            imageUrl = imageUrl
        )
    }
}
