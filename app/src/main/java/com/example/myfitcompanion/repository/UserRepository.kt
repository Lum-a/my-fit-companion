package com.example.myfitcompanion.repository

import com.example.myfitcompanion.model.login.LoginResponse
import com.example.myfitcompanion.model.register.RegisterRequest
import com.example.myfitcompanion.model.entities.User
import com.example.myfitcompanion.model.register.RegisterResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface UserRepository {
    suspend fun login(email: String, password: String): Response<LoginResponse>
    suspend fun register(request: RegisterRequest): Response<RegisterResponse>
    suspend fun insertUser(user: User)
    fun getUser(): Flow<User?>
    suspend fun deleteUser(user: User)
}