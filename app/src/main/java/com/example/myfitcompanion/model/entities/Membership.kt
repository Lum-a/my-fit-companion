package com.example.myfitcompanion.model.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "memberships",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Membership(
    @PrimaryKey(autoGenerate = true)
    val membershipId: Long = 0,
    val userId: String,
    val type: String,       // e.g. "Monthly", "Yearly", "Student"
    val startDate: Long,    // store as timestamp
    val endDate: Long,
    val status: String      // "active", "expired"
)
