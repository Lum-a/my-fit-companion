package com.example.myfitcompanion.admin.repository

import com.example.myfitcompanion.api.model.UserResponse
import com.example.myfitcompanion.utils.ResultWrapper

interface AdminRepository {
    suspend fun getUsers(): ResultWrapper<List<UserResponse>>
    suspend fun addUser(user: UserResponse): ResultWrapper<UserResponse>
    suspend fun updateUser(user: UserResponse): ResultWrapper<UserResponse>
    suspend fun deleteUser(userId: Int): ResultWrapper<Unit>
}