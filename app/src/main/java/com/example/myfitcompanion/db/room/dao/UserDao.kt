package com.example.myfitcompanion.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myfitcompanion.db.room.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM user LIMIT 1")
    fun getUser(): Flow<User?>

    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun findUserById(userId: Int): User?

    @Update
    suspend fun updateUserDetails(user: User)

    @Query("DELETE FROM user")
    suspend fun deleteUser()

}