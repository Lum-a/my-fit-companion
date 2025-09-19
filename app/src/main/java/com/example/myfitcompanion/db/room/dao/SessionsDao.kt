package com.example.myfitcompanion.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myfitcompanion.model.entities.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun attendSession(session: Session)

    @Query("SELECT * FROM classes WHERE classId = :id LIMIT 1")
    suspend fun getSessionById(id: Int): Session?

    @Query("SELECT * FROM classes ORDER BY startTime ASC")
    fun getSessions(): Flow<List<Session>>

}