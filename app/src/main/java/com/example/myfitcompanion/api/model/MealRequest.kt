package com.example.myfitcompanion.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MealRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "calories")
    val calories: Int,
    @Json(name = "description")
    val description: String?
)
