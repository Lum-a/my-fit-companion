package com.example.myfitcompanion.api.admin

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
    suspend fun addUser(@Body user: UserResponse): UserResponse

    @PUT("admin/users/{id}")
    suspend fun updateUser(
        @Path("id") userId: Int,
        @Body user: UserResponse
    ): UserResponse

    @DELETE("admin/users/{id}")
    suspend fun deleteUser(@Path("id") userId: Int)

}