package com.example.myfitcompanion.repository

import android.util.Log
import com.example.myfitcompanion.api.ApiService
import com.example.myfitcompanion.api.model.ExerciseResponse
import com.example.myfitcompanion.api.model.UpdateProfileRequest
import com.example.myfitcompanion.api.model.UpdateProfileResponse
import com.example.myfitcompanion.api.model.BaseResponse
import com.example.myfitcompanion.api.token.TokenManager
import com.example.myfitcompanion.db.room.dao.UserDao
import com.example.myfitcompanion.api.model.LoginRequest
import com.example.myfitcompanion.api.model.LoginResponse
import com.example.myfitcompanion.api.model.PasswordUpdatedModel
import com.example.myfitcompanion.api.model.RegisterRequest
import com.example.myfitcompanion.db.room.entities.User
import com.example.myfitcompanion.api.model.RegisterResponse
import com.example.myfitcompanion.api.model.UpdateEmailRequest
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

    override suspend fun updateUser(request: UpdateProfileRequest): ResultWrapper<UpdateProfileResponse> {
        return try {
            // 1. Get current user
            val currentUser = userDao.getAllUsers().first().firstOrNull() ?: return ResultWrapper.Error("No user found")

            // 2. Call API
            val response = apiService.updateProfile(request).apply {
                //save changed user data to local db
                val updatedUser = currentUser.copy(
                    firstName = firstName ?: currentUser.firstName,
                    lastName = lastName ?: currentUser.lastName,
                    height = height ?: currentUser.height,
                    weight = weight ?: currentUser.weight,
                    bodyFat = bodyFat ?: currentUser.bodyFat,
                    goalBodyFat = goalBodyFat ?: currentUser.goalBodyFat,
                    goalWeight = goalWeight ?: currentUser.goalWeight,
                    imageUrl = imageUrl ?: currentUser.imageUrl
                )
                Log.d("UserRepositoryImpl", "updateUser: $updatedUser")
                userDao.updateUserDetails(updatedUser)
            }

            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Error(e.message)
        }
    }

    override suspend fun updatePassword(
        oldPassword: String,
        newPassword: String
    ): ResultWrapper<BaseResponse> = try {
        val updatePasswordModel = PasswordUpdatedModel(
            oldPassword,
            newPassword
        )
        val response = apiService.updatePassword(updatePasswordModel)
        ResultWrapper.Success(response)

    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override suspend fun updateEmail(newEmail: String): ResultWrapper<UpdateProfileResponse> = try {
        val updateEmailRequest = UpdateEmailRequest(newEmail)
        val response = apiService.updateEmail(updateEmailRequest)
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

    override suspend fun getRecentExercise(): ResultWrapper<List<ExerciseResponse>> = try {
            val response = apiService.getRecentExercises()
            ResultWrapper.Success(response)
        } catch (e: Exception) {
            ResultWrapper.Error(e.message)
        }

    override suspend fun saveRecentExercise(exerciseId: Int) {
        try {
            apiService.saveRecentExercise(exerciseId)
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