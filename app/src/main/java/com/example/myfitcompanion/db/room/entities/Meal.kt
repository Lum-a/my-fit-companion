package com.example.myfitcompanion.db.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Edon Idrizi on 19/Sep/2025 :)
 **/
@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey(autoGenerate = true) val mealId: Long = 0,
    val name: String,
    val calories: Int,
    val description: String?
)
