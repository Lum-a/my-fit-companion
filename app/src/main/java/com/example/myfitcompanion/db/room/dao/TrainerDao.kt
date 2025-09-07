package com.example.myfitcompanion.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myfitcompanion.model.entities.Trainer
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrainer(trainer: Trainer)

    @Query("SELECT * FROM trainers ORDER BY name ASC")
    fun getAllTrainers(): Flow<List<Trainer>>
}