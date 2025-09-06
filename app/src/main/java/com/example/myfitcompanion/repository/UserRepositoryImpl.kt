package com.example.myfitcompanion.repository

import com.example.myfitcompanion.api.ApiService
import com.example.myfitcompanion.db.room.UserDao
import com.example.myfitcompanion.model.login.LoginRequest
import com.example.myfitcompanion.model.login.LoginResponse
import com.example.myfitcompanion.model.register.RegisterRequest
import com.example.myfitcompanion.model.register.RegisterResponse
import com.example.myfitcompanion.model.User
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
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

    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    override suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

}