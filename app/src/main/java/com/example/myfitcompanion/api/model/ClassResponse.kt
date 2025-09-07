package com.example.myfitcompanion.api.model

data class ClassResponse(
    val classId: Long,
    val name: String,
    val description: String?,
    val trainerId: Long,
    val startTime: Long,
    val endTime: Long,
    val capacity: Int
)