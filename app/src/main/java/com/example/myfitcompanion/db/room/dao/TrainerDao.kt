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
    suspend fun insertTrainer(vararg trainer: Trainer)

    @Query("SELECT * FROM trainers ORDER BY firstName ASC")
    fun getAllTrainers(): Flow<List<Trainer>>

    @Query("SELECT * FROM trainers WHERE trainerId = :trainerId")
    suspend fun getTrainerById(trainerId: Long): Trainer?
}