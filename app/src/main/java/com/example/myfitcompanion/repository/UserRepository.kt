package com.example.myfitcompanion.repository

import com.example.myfitcompanion.api.model.LoginRequest
import com.example.myfitcompanion.api.model.UpdateProfileRequest
import com.example.myfitcompanion.api.model.UpdateProfileResponse
import com.example.myfitcompanion.api.model.LoginResponse
import com.example.myfitcompanion.api.model.RegisterRequest
import com.example.myfitcompanion.model.entities.User
import com.example.myfitcompanion.api.model.RegisterResponse
import com.example.myfitcompanion.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface UserRepository {
    suspend fun login(request: LoginRequest): ResultWrapper<LoginResponse>
    suspend fun register(request: RegisterRequest): ResultWrapper<RegisterResponse>
    suspend fun insertUser(user: User)
    suspend fun updateUser(request: UpdateProfileRequest): ResultWrapper<UpdateProfileResponse>
    fun getUser(): Flow<User?>
    suspend fun getUserId(): Int?
    suspend fun deleteUser()
    suspend fun logout()
    fun isAdmin(): Flow<Boolean>
    fun isLoggedIn(): Flow<Boolean>
}