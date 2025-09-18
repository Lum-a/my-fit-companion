package com.example.myfitcompanion.admin.repository

import com.example.myfitcompanion.api.model.MealsResponse
import com.example.myfitcompanion.api.model.MealRequest
import com.example.myfitcompanion.api.model.UserResponse
import com.example.myfitcompanion.api.model.CreateUserRequest
import com.example.myfitcompanion.api.model.UpdateUserRequest
import com.example.myfitcompanion.api.model.SessionsResponse
import com.example.myfitcompanion.api.model.SessionRequest
import com.example.myfitcompanion.api.model.ExerciseResponse
import com.example.myfitcompanion.api.model.ExerciseRequest
import com.example.myfitcompanion.utils.ResultWrapper

interface AdminRepository {

    //users
    suspend fun getUsers(): ResultWrapper<List<UserResponse>>
    suspend fun addUser(user: CreateUserRequest): ResultWrapper<UserResponse>
    suspend fun updateUser(userId: Int, user: UpdateUserRequest): ResultWrapper<UserResponse>
    suspend fun deleteUser(userId: Int): ResultWrapper<Unit>

    //meals
    suspend fun getMeals(): ResultWrapper<List<MealsResponse>>
    suspend fun addMeal(meal: MealRequest): ResultWrapper<MealsResponse>
    suspend fun updateMeal(mealId: Int, meal: MealRequest): ResultWrapper<MealsResponse>
    suspend fun deleteMeal(mealId: Int): ResultWrapper<Unit>

    //sessions
    suspend fun getSessions(): ResultWrapper<List<SessionsResponse>>
    suspend fun addSession(session: SessionRequest): ResultWrapper<SessionsResponse>
    suspend fun updateSession(sessionId: Int, session: SessionRequest): ResultWrapper<SessionsResponse>
    suspend fun deleteSession(sessionId: Int): ResultWrapper<Unit>

    //exercises
    suspend fun getExercises(): ResultWrapper<List<ExerciseResponse>>
    suspend fun addExercise(exercise: ExerciseRequest): ResultWrapper<ExerciseResponse>
    suspend fun updateExercise(exerciseId: Int, exercise: ExerciseRequest): ResultWrapper<ExerciseResponse>
    suspend fun deleteExercise(exerciseId: Int): ResultWrapper<Unit>

}