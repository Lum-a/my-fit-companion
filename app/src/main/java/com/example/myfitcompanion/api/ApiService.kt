package com.example.myfitcompanion.api

import com.example.myfitcompanion.api.model.UpdateProfileRequest
import com.example.myfitcompanion.api.model.UpdateProfileResponse
import com.example.myfitcompanion.api.model.ClassBookingRequest
import com.example.myfitcompanion.api.model.ClassBookingResponse
import com.example.myfitcompanion.api.model.ClassResponse
import com.example.myfitcompanion.api.model.LoginRequest
import com.example.myfitcompanion.api.model.LoginResponse
import com.example.myfitcompanion.api.model.MembershipResponse
import com.example.myfitcompanion.api.model.PlanResponse
import com.example.myfitcompanion.api.model.RegisterRequest
import com.example.myfitcompanion.api.model.RegisterResponse
import com.example.myfitcompanion.api.model.TrainerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("memberships/{userId}")
    suspend fun getMembership(@Path("userId") userId: String): Response<MembershipResponse>

    @GET("plans")
    suspend fun getPlans(): Response<List<PlanResponse>>

    @GET("trainers")
    suspend fun getTrainers(): Response<List<TrainerResponse>>

    @GET("classes")
    suspend fun getClasses(): Response<List<ClassResponse>>

    @POST("classes/{classId}/book")
    suspend fun bookClass(
        @Path("classId") classId: Long,
        @Body bookingRequest: ClassBookingRequest
    ): Response<ClassBookingResponse>

    @PUT("users/{userId}")
    suspend fun updateProfile(
        @Path("userId") userId: Int?,
        @Body request: UpdateProfileRequest
    ): Response<UpdateProfileResponse>
}