package com.example.myfitcompanion.admin.repository

import com.example.myfitcompanion.api.admin.AdminApiService
import com.example.myfitcompanion.api.model.UserResponse
import com.example.myfitcompanion.utils.ResultWrapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepositoryImpl @Inject constructor(
    private val adminApiService: AdminApiService
): AdminRepository {

    override suspend fun getUsers(): ResultWrapper<List<UserResponse>> = try {
        val response = adminApiService.getUsers().map { it }
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override suspend fun addUser(user: UserResponse): ResultWrapper<UserResponse> = try {
        val response = adminApiService.addUser(user)
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        ResultWrapper.Error(e.message)
    }

    override suspend fun updateUser(user: UserResponse): ResultWrapper<UserResponse> = try {
        val response = adminApiService.updateUser(user.id, user)
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
}