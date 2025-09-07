package com.example.myfitcompanion.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myfitcompanion.model.entities.Membership

@Dao
interface MembershipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembership(membership: Membership)

    @Query("SELECT * FROM memberships WHERE userId = :userId ORDER BY endDate DESC LIMIT 1")
    suspend fun getLatestMembership(userId: String): Membership?

    @Query("DELETE FROM memberships")
    suspend fun clearMemberships()
}