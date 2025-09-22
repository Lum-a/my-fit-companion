package com.example.myfitcompanion.repository

import android.util.Log
import com.example.myfitcompanion.api.ApiService
import com.example.myfitcompanion.api.model.ExerciseResponse
import com.example.myfitcompanion.api.model.UpdateProfileRequest
import com.example.myfitcompanion.api.model.UpdateProfileResponse
import com.example.myfitcompanion.api.token.TokenManager
import com.example.myfitcompanion.db.room.dao.UserDao
import com.example.myfitcompanion.api.model.LoginRequest
import com.example.myfitcompanion.api.model.LoginResponse
import com.example.myfitcompanion.api.model.RegisterRequest
import com.example.myfitcompanion.db.room.entities.User
import com.example.myfitcompanion.api.model.RegisterResponse
import com.example.myfitcompanion.api.model.WorkoutResponse
import com.example.myfitcompanion.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val tokenManager: TokenManager
): UserRepository {

    override suspend fun login(request: LoginRequest): ResultWrapper<LoginResponse> = try {
        val response = apiService.login(request)
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override suspend fun register(request: RegisterRequest): ResultWrapper<RegisterResponse>  = try {
        val response = apiService.register(request)
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override fun getUser(): Flow<User?> {
        return userDao.getUser()
    }

    override suspend fun getUserId(): Int? {
        return userDao.getUser().firstOrNull()?.id
    }

    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    override suspend fun updateUser(request: UpdateProfileRequest): ResultWrapper<UpdateProfileResponse> = try {
        // 1. Get current user
        val currentUser = userDao.getAllUsers().first().firstOrNull() ?: throw Exception("User not found")

        // 2. Call API
        val response = apiService.updateProfile(request)

        // 3. Update local user with new data
        val updatedUser = currentUser.copy(
            height = request.height ?: currentUser.height,
            weight = request.weight ?: currentUser.weight,
            bodyFat = request.bodyFat ?: currentUser.bodyFat,
            goal = request.goal ?: currentUser.goal,
            imageUrl = request.imageUrl ?: currentUser.imageUrl
        )

        userDao.updateUserDetails(updatedUser)
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override suspend fun deleteUser() {
        userDao.deleteUser()
    }

    override suspend fun logout() {
        tokenManager.clearToken()
        userDao.deleteUser()
    }

    override fun isAdmin(): Flow<Boolean> =
        userDao.getUser()
            .map { user ->
                user?.role == "admin".uppercase()
            }

    override fun isLoggedIn(): Flow<Boolean> = combine(
        userDao.getUser(),
        tokenManager.authToken
    ) { user, token ->
        user != null && !token.isNullOrEmpty()
    }

    override suspend fun getRecentExercise(): ResultWrapper<ExerciseResponse> = try {
            val response = apiService.getRecentExercise(getUserId())
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Error(e.message)
        }

    override suspend fun addRecentExercise(splitId: Int) {
        try {
            apiService.addRecentExercise(splitId, getUserId())
        } catch (e: Exception) {
            Log.d("UserRepositoryImpl", "addRecentExercise: ${e.message}")
        }
    }

    override suspend fun getWorkouts(): ResultWrapper<List<WorkoutResponse>> = try {
        val response = apiService.getWorkouts()
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override suspend fun getSplits(workoutId: Int): ResultWrapper<List<com.example.myfitcompanion.api.model.SplitResponse>> = try {
        val response = apiService.getWorkoutSplits(workoutId)
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override suspend fun getExercises(splitId: Int): ResultWrapper<List<ExerciseResponse>> = try {
        val response = apiService.getExercises(splitId)
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

}