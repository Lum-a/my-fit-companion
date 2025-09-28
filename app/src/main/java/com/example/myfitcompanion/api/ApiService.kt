package com.example.myfitcompanion.api

import com.example.myfitcompanion.api.model.UpdateProfileRequest
import com.example.myfitcompanion.api.model.UpdateProfileResponse
import com.example.myfitcompanion.api.model.ExerciseResponse
import com.example.myfitcompanion.api.model.LoginRequest
import com.example.myfitcompanion.api.model.LoginResponse
import com.example.myfitcompanion.api.model.MealsResponse
import com.example.myfitcompanion.api.model.PasswordUpdatedModel
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
import retrofit2.http.Query

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("trainers")
    suspend fun getTrainers(): List<TrainerResponse>

    @GET("workouts")
    suspend fun getWorkouts(): List<WorkoutResponse>

    @GET("splits/{workoutId}")
    suspend fun getWorkoutSplits(@Path("workoutId") workoutId: Int): List<SplitResponse>

    @GET("exercises/{splitId}")
    suspend fun getExercises(@Path("splitId") splitId: Int): List<ExerciseResponse>

    @GET("meals")
    suspend fun getMeals(): List<MealsResponse>

    @PUT("profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): UpdateProfileResponse

    @POST("exercises/{exerciseId}/recent")
    suspend fun saveRecentExercise(@Path("exerciseId") exerciseId: Int)

    @GET("exercises/recent")
    suspend fun getRecentExercises(@Query("limit") limit: Int = 5): List<ExerciseResponse>


    @PUT("profile/change-email")
    suspend fun updateEmail(@Body newEmail: String): UpdateProfileResponse


    @PUT("profile/change-password")
    suspend fun updatePassword(
        @Body passwordModel: PasswordUpdatedModel
    ): UpdateProfileResponse

}