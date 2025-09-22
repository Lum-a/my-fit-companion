package com.example.myfitcompanion.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myfitcompanion.db.room.entities.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun attendExercise(workout: Workout)

    @Query("SELECT * FROM workouts WHERE workoutId = :id LIMIT 1")
    suspend fun getWorkoutById(id: Int): Workout?

    @Query("SELECT * FROM workouts ORDER BY name ASC")
    fun getWorkouts(): Flow<List<Workout>>

}