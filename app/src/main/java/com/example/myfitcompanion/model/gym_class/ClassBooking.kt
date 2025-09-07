package com.example.myfitcompanion.model.gym_class

data class ClassBookingRequest(
    val userId: String
)

data class ClassBookingResponse(
    val bookingId: Long,
    val classId: Long,
    val userId: String,
    val status: String
)


