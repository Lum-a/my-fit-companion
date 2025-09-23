package com.example.myfitcompanion.api

import com.example.myfitcompanion.api.model.UpdateProfileRequest
import com.example.myfitcompanion.api.model.UpdateProfileResponse
import com.example.myfitcompanion.api.model.ExerciseResponse
import com.example.myfitcompanion.api.model.LoginRequest
import com.example.myfitcompanion.api.model.LoginResponse
import com.example.myfitcompanion.api.model.MealsResponse
import com.example.myfitcompanion.api.model.RegisterRequest
import com.example.myfitcompanion.api.model.RegisterResponse
import com.example.myfitcompanion.api.model.SplitResponse
import com.example.myfitcompanion.api.model.WorkoutResponse
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

    @GET("workouts") //to be changed to "workouts" in the backend
    suspend fun getWorkouts(): List<WorkoutResponse>

    @GET("workouts/{workoutId}/splits")
    suspend fun getWorkoutSplits(@Path("workoutId") workoutId: Int): List<SplitResponse>

    @GET("workouts/splits/{splitId}/exercises")
    suspend fun getExercises(@Path("splitId") splitId: Int): List<ExerciseResponse>

    @GET("meals")
    suspend fun getMeals(): List<MealsResponse>

    @PUT("profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): UpdateProfileResponse

    @GET("/exercises/recent/{userId}")
    suspend fun getRecentExercise(@Path("userId") userId: Int?): ExerciseResponse

    @POST("/exercises/recent")
    suspend fun addRecentExercise(@Body workoutId: Int, @Body userId: Int?)

}