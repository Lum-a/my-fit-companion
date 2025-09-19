package com.example.myfitcompanion.api

import com.example.myfitcompanion.api.model.UpdateProfileRequest
import com.example.myfitcompanion.api.model.UpdateProfileResponse
import com.example.myfitcompanion.api.model.ClassBookingRequest
import com.example.myfitcompanion.api.model.ClassBookingResponse
import com.example.myfitcompanion.api.model.ClassResponse
import com.example.myfitcompanion.api.model.ExerciseResponse
import com.example.myfitcompanion.api.model.LoginRequest
import com.example.myfitcompanion.api.model.LoginResponse
import com.example.myfitcompanion.api.model.MealsResponse
import com.example.myfitcompanion.api.model.MembershipResponse
import com.example.myfitcompanion.api.model.PlanResponse
import com.example.myfitcompanion.api.model.RegisterRequest
import com.example.myfitcompanion.api.model.RegisterResponse
import com.example.myfitcompanion.api.model.SessionsResponse
import com.example.myfitcompanion.api.model.TrainerResponse
import retrofit2.Response
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

    @GET("plans")
    suspend fun getPlans(): List<PlanResponse>

    @GET("trainers")
    suspend fun getTrainers(): List<TrainerResponse>

    @GET("sessions")
    suspend fun getSessions(): List<SessionsResponse>

    @GET("meals")
    suspend fun getMeals(): List<MealsResponse>

    @GET("exercises")
    suspend fun getExercises(): List<ExerciseResponse>


    @PUT("users/{userId}")
    suspend fun updateProfile(
        @Path("userId") userId: Int?,
        @Body request: UpdateProfileRequest
    ): UpdateProfileResponse
}