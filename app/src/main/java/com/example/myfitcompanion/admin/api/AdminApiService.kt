package com.example.myfitcompanion.admin.api

import com.example.myfitcompanion.api.model.CreateUserRequest
import com.example.myfitcompanion.api.model.ExerciseRequest
import com.example.myfitcompanion.api.model.ExerciseResponse
import com.example.myfitcompanion.api.model.MealRequest
import com.example.myfitcompanion.api.model.MealsResponse
import com.example.myfitcompanion.api.model.WorkoutRequest
import com.example.myfitcompanion.api.model.WorkoutResponse
import com.example.myfitcompanion.api.model.TrainerResponse
import com.example.myfitcompanion.api.model.UpdateTrainerRequest
import com.example.myfitcompanion.api.model.UpdateUserRequest
import com.example.myfitcompanion.api.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AdminApiService {

    @GET("admin/users")
    suspend fun getUsers(): List<UserResponse>

    @POST("admin/users")
    suspend fun addUser(@Body user: CreateUserRequest): UserResponse

    @PUT("admin/users/{id}")
    suspend fun updateUser(
        @Path("id") userId: Int,
        @Body user: UpdateUserRequest
    ): UserResponse

    @DELETE("admin/users/{id}")
    suspend fun deleteUser(@Path("id") userId: Int)

    // Meals
    @POST("admin/meals")
    suspend fun addMeal(@Body meal: MealRequest): MealsResponse

    @GET("admin/meals")
    suspend fun getMeals(): List<MealsResponse>

    @PUT("admin/meals/{id}")
    suspend fun updateMeal(@Path("id") mealId: Int, @Body meal: MealRequest): MealsResponse

    @DELETE("admin/meals/{id}")
    suspend fun deleteMeal(@Path("id") mealId: Int)

    // Workouts
    @POST("admin/workouts")
    suspend fun addWorkout(@Body workout: WorkoutRequest): WorkoutResponse

    @PUT("admin/workouts/{id}")
    suspend fun updateWorkout(@Path("id") workoutId: Int, @Body workout: WorkoutRequest): WorkoutResponse

    @DELETE("admin/workouts/{id}")
    suspend fun deleteWorkout(@Path("id") workoutId: Int)

    // Exercises
    @POST("admin/exercises")
    suspend fun addExercise(@Body exercise: ExerciseRequest): ExerciseResponse

    @PUT("admin/exercises/{id}")
    suspend fun updateExercise(@Path("id") exerciseId: Int, @Body exercise: ExerciseRequest): ExerciseResponse

    @DELETE("admin/exercises/{id}")
    suspend fun deleteExercise(@Path("id") exerciseId: Int)

    @PUT("admin/trainers/{id}")
    suspend fun updateTrainer(@Path("id") trainerId: Int, @Body trainer: UpdateTrainerRequest): TrainerResponse

    @DELETE("admin/trainers/{id}")
    suspend fun deleteTrainer(@Path("id") trainerId: Int)

}