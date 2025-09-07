package com.example.myfitcompanion.repository

import com.example.myfitcompanion.api.model.UpdateProfileRequest
import com.example.myfitcompanion.api.model.UpdateProfileResponse
import com.example.myfitcompanion.api.model.LoginResponse
import com.example.myfitcompanion.api.model.RegisterRequest
import com.example.myfitcompanion.model.entities.User
import com.example.myfitcompanion.api.model.RegisterResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface UserRepository {
    suspend fun login(email: String, password: String): Response<LoginResponse>
    suspend fun register(request: RegisterRequest): Response<RegisterResponse>
    suspend fun insertUser(user: User)
    suspend fun updateUser(request: UpdateProfileRequest): Response<UpdateProfileResponse>
    fun getUser(): Flow<User?>
    suspend fun getUserId(): Int?
    suspend fun deleteUser()
    suspend fun logout()
    fun isLoggedIn(): Flow<Boolean>
}