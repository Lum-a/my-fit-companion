package com.example.myfitcompanion.admin.repository

import com.example.myfitcompanion.admin.api.AdminApiService
import com.example.myfitcompanion.api.model.UserResponse
import com.example.myfitcompanion.api.model.MealsResponse
import com.example.myfitcompanion.api.model.MealRequest
import com.example.myfitcompanion.api.model.SessionsResponse
import com.example.myfitcompanion.api.model.SessionRequest
import com.example.myfitcompanion.api.model.ExerciseResponse
import com.example.myfitcompanion.api.model.ExerciseRequest
import com.example.myfitcompanion.api.model.CreateUserRequest
import com.example.myfitcompanion.api.model.UpdateUserRequest
import com.example.myfitcompanion.utils.ResultWrapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepositoryImpl @Inject constructor(
    private val adminApiService: AdminApiService
): AdminRepository {

    override suspend fun getUsers(): ResultWrapper<List<UserResponse>> = try {
        val response = adminApiService.getUsers()
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override suspend fun addUser(user: CreateUserRequest): ResultWrapper<UserResponse> = try {
        val response = adminApiService.addUser(user)
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override suspend fun updateUser(userId: Int, user: UpdateUserRequest): ResultWrapper<UserResponse> = try {
        val response = adminApiService.updateUser(userId, user)
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override suspend fun deleteUser(userId: Int): ResultWrapper<Unit> = try {
        adminApiService.deleteUser(userId)
        ResultWrapper.Success(Unit)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    // Meals
    override suspend fun addMeal(meal: MealRequest): ResultWrapper<MealsResponse> = try {
        val response = adminApiService.addMeal(meal)
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override suspend fun getMeals(): ResultWrapper<List<MealsResponse>> = try {
        val response = adminApiService.getMeals()
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override suspend fun updateMeal(mealId: Int, meal: MealRequest): ResultWrapper<MealsResponse> = try {
        val response = adminApiService.updateMeal(mealId, meal)
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override suspend fun deleteMeal(mealId: Int): ResultWrapper<Unit> = try {
        adminApiService.deleteMeal(mealId)
        ResultWrapper.Success(Unit)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    // Sessions
    override suspend fun addSession(session: SessionRequest): ResultWrapper<SessionsResponse> = try {
        val response = adminApiService.addSession(session)
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override suspend fun getSessions(): ResultWrapper<List<SessionsResponse>> = try {
        val response = adminApiService.getSessions()
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override suspend fun updateSession(sessionId: Int, session: SessionRequest): ResultWrapper<SessionsResponse> = try {
        val response = adminApiService.updateSession(sessionId, session)
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override suspend fun deleteSession(sessionId: Int): ResultWrapper<Unit> = try {
        adminApiService.deleteSession(sessionId)
        ResultWrapper.Success(Unit)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    // Exercises
    override suspend fun getExercises(): ResultWrapper<List<ExerciseResponse>> = try {
        val response = adminApiService.getExercises()
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override suspend fun addExercise(exercise: ExerciseRequest): ResultWrapper<ExerciseResponse> = try {
        val response = adminApiService.addExercise(exercise)
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override suspend fun updateExercise(exerciseId: Int, exercise: ExerciseRequest): ResultWrapper<ExerciseResponse> = try {
        val response = adminApiService.updateExercise(exerciseId, exercise)
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override suspend fun deleteExercise(exerciseId: Int): ResultWrapper<Unit> = try {
        adminApiService.deleteExercise(exerciseId)
        ResultWrapper.Success(Unit)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }
}