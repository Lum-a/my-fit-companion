package com.example.myfitcompanion.api

import com.example.myfitcompanion.api.model.UpdateProfileRequest
import com.example.myfitcompanion.api.model.UpdateProfileResponse
import com.example.myfitcompanion.api.model.ExerciseResponse
import com.example.myfitcompanion.api.model.LoginRequest
import com.example.myfitcompanion.api.model.LoginResponse
import com.example.myfitcompanion.api.model.MealsResponse
import com.example.myfitcompanion.api.model.RegisterRequest
import com.example.myfitcompanion.api.model.RegisterResponse
import com.example.myfitcompanion.api.model.SessionResponse
import com.example.myfitcompanion.api.model.TrainerResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("trainers")
    suspend fun getTrainers(): List<TrainerResponse>

    @GET("sessions")
    suspend fun getSessions(): List<SessionResponse>

    @GET("meals")
    suspend fun getMeals(): List<MealsResponse>

    @GET("exercises")
    suspend fun getExercises(): List<ExerciseResponse>

    @PUT("users/{userId}")
    suspend fun updateProfile(
        @Path("userId") userId: Int?,
        @Body request: UpdateProfileRequest
    ): UpdateProfileResponse

    @GET("/sessions/recent/{userId}")
    suspend fun getRecentSession(@Path("userId") userId: Int?): SessionResponse

    @POST("/sessions/recent")
    suspend fun addRecentSession(@Body sessionId: Int, @Body userId: Int?)

}