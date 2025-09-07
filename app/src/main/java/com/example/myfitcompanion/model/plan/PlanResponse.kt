package com.example.myfitcompanion.model.plan

data class PlanResponse(
    val planId: Long,
    val name: String,
    val description: String?,
    val durationDays: Int,
    val price: Double,
    val discount: Double? = null
)