package com.example.myfitcompanion.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResponse(
    @Json(name = "success")
    val success: Boolean? = null,

    @Json(name = "message")
    val message: String? = null,

    @Json(name = "error")
    val error: String? = null
) {
    /**
     * Helper function to get the actual message regardless of success/error format
     */
    fun getDisplayMessage(): String {
        return when {
            !error.isNullOrBlank() -> error
            !message.isNullOrBlank() -> message
            else -> "Unknown response"
        }
    }

    /**
     * Helper function to check if the response indicates success
     */
    fun isSuccess(): Boolean {
        return success == true && error.isNullOrBlank()
    }

    /**
     * Helper function to check if the response indicates an error
     */
    fun isError(): Boolean {
        return success == false || !error.isNullOrBlank()
    }
}
