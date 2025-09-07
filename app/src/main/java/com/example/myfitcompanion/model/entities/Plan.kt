package com.example.myfitcompanion.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plans")
data class Plan(
    @PrimaryKey(autoGenerate = true) val planId: Long = 0,
    val name: String,          // e.g. "Monthly Plan", "Premium Plan"
    val description: String?,  // short info about the plan
    val durationDays: Int,     // number of days this plan is valid
    val price: Double          // price in your currency
)
