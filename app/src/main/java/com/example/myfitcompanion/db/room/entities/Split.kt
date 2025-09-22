package com.example.myfitcompanion.db.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Edon Idrizi on 22/Sep/2025 :)
 **/
@Entity(tableName = "splits")
data class Split(
    @PrimaryKey(autoGenerate = true) val splitId: Int,
    val name: String,
    val description: String?,
    val imageUrl: String?
)