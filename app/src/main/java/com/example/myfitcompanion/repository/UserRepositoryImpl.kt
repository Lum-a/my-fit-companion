package com.example.myfitcompanion.repository

import com.example.myfitcompanion.api.ApiService
import com.example.myfitcompanion.api.model.UpdateProfileRequest
import com.example.myfitcompanion.api.model.UpdateProfileResponse
import com.example.myfitcompanion.api.token.TokenManager
import com.example.myfitcompanion.db.room.dao.UserDao
import com.example.myfitcompanion.model.login.LoginRequest
import com.example.myfitcompanion.model.login.LoginResponse
import com.example.myfitcompanion.model.register.RegisterRequest
import com.example.myfitcompanion.model.entities.User
import com.example.myfitcompanion.model.register.RegisterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val tokenManager: TokenManager
): UserRepository {

    override suspend fun login(email: String, password: String): Response<LoginResponse> {
        return apiService.login(LoginRequest(email, password))
    }

    override suspend fun register(request: RegisterRequest): Response<RegisterResponse> {
        return apiService.register(request)
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

    override suspend fun updateUser(request: UpdateProfileRequest): Response<UpdateProfileResponse> {
        return apiService.updateProfile(getUserId(), request)
    }

    override suspend fun deleteUser() {
        userDao.deleteUser()
    }

    override suspend fun logout() {
        tokenManager.clearToken()
        userDao.deleteUser()
    }

}