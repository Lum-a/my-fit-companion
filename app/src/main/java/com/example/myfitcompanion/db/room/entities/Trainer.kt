package com.example.myfitcompanion.db.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

//personal trainer :P
@Entity(tableName = "trainers")
data class Trainer(
    @PrimaryKey(autoGenerate = true)
    val trainerId: Int,
    val firstName: String,
    val lastName: String,
    val specialization: String? = null,
    val contactInfo: String? = null
) {
    /**
     * Maps this Trainer instance to a User instance for use in the unified user system
     */
    fun toUser(): User {
        return User(
            id = this.trainerId,
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.contactInfo ?: "trainer${this.trainerId}@fitcompanion.com",
            role = "trainer",
            height = null,
            weight = null,
            bodyFat = null,
            goalBodyFat = null,
            goalWeight = null,
            createdAt = System.currentTimeMillis().toString(),
            imageUrl = null
        )
    }
}
