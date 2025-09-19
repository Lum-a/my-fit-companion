package com.example.myfitcompanion.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myfitcompanion.model.entities.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExercise(plan: Exercise)

    @Query("SELECT * FROM exercises ORDER BY name ASC")
    fun getExercises(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE exerciseId = :id LIMIT 1")
    suspend fun getExerciseById(id: Long): Exercise?
}