package com.example.myfitcompanion.api

import com.example.myfitcompanion.model.login.LoginRequest
import com.example.myfitcompanion.model.login.LoginResponse
import com.example.myfitcompanion.model.register.RegisterRequest
import com.example.myfitcompanion.model.register.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

}