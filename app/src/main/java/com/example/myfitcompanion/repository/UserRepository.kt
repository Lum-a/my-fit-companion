package com.example.myfitcompanion.repository

import com.example.myfitcompanion.api.model.ExerciseResponse
import com.example.myfitcompanion.api.model.LoginRequest
import com.example.myfitcompanion.api.model.UpdateProfileRequest
import com.example.myfitcompanion.api.model.UpdateProfileResponse
import com.example.myfitcompanion.api.model.LoginResponse
import com.example.myfitcompanion.api.model.RegisterRequest
import com.example.myfitcompanion.model.entities.User
import com.example.myfitcompanion.api.model.RegisterResponse
import com.example.myfitcompanion.api.model.SplitResponse
import com.example.myfitcompanion.api.model.WorkoutResponse
import com.example.myfitcompanion.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(request: LoginRequest): ResultWrapper<LoginResponse>
    suspend fun register(request: RegisterRequest): ResultWrapper<RegisterResponse>
    suspend fun insertUser(user: User)
    suspend fun updateUser(request: UpdateProfileRequest): ResultWrapper<UpdateProfileResponse>
    fun getUser(): Flow<User?>
    suspend fun getUserId(): Int?
    suspend fun deleteUser()
    suspend fun logout()
    fun isAdmin(): Flow<Boolean>
    fun isLoggedIn(): Flow<Boolean>

    suspend fun getRecentExercise(): ResultWrapper<ExerciseResponse>
    suspend fun addRecentExercise(splitId: Int)
    suspend fun getWorkouts(): ResultWrapper<List<WorkoutResponse>>
    suspend fun getSplits(workoutId: Int): ResultWrapper<List<SplitResponse>>
    suspend fun getExercises(splitId: Int): ResultWrapper<List<ExerciseResponse>>
}