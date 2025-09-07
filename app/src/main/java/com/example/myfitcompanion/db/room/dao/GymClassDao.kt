package com.example.myfitcompanion.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myfitcompanion.model.entities.GymClass
import kotlinx.coroutines.flow.Flow

@Dao
interface GymClassDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(gymClass: GymClass)

    @Query("SELECT * FROM classes WHERE classId = :id LIMIT 1")
    suspend fun getClassById(id: Long): GymClass?

    @Query("SELECT * FROM classes ORDER BY startTime ASC")
    fun getAllClasses(): Flow<List<GymClass>>

    @Query("DELETE FROM classes WHERE classId = :id")
    suspend fun deleteClassById(id: Long)
}