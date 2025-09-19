package com.example.myfitcompanion.repository

import com.example.myfitcompanion.api.ApiService
import com.example.myfitcompanion.api.model.UpdateProfileRequest
import com.example.myfitcompanion.api.model.UpdateProfileResponse
import com.example.myfitcompanion.api.token.TokenManager
import com.example.myfitcompanion.db.room.dao.UserDao
import com.example.myfitcompanion.api.model.LoginRequest
import com.example.myfitcompanion.api.model.LoginResponse
import com.example.myfitcompanion.api.model.RegisterRequest
import com.example.myfitcompanion.model.entities.User
import com.example.myfitcompanion.api.model.RegisterResponse
import com.example.myfitcompanion.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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
        val response = apiService.updateProfile(getUserId(), request)
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
                user?.role == "ADMIN"
            }

    override fun isLoggedIn(): Flow<Boolean> = combine(
        userDao.getUser(),
        tokenManager.authToken
    ) { user, token ->
        user != null && !token.isNullOrEmpty()
    }


}