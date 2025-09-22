package com.example.myfitcompanion.db.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.myfitcompanion.db.room.entities.Split
import kotlinx.coroutines.flow.Flow

/**
 * Created by Edon Idrizi on 22/Sep/2025 :)
 **/
@Dao
interface SplitDao {

    @Query("SELECT * FROM splits WHERE splitId = :id LIMIT 1")
    suspend fun getSplitById(id: Int): Split?

    @Query("SELECT * FROM splits ORDER BY name ASC")
    fun getSplits(): Flow<List<Split>>
}