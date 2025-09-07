package com.example.myfitcompanion.api

import com.example.myfitcompanion.model.gym_class.ClassBookingRequest
import com.example.myfitcompanion.model.gym_class.ClassBookingResponse
import com.example.myfitcompanion.model.gym_class.ClassResponse
import com.example.myfitcompanion.model.login.LoginRequest
import com.example.myfitcompanion.model.login.LoginResponse
import com.example.myfitcompanion.model.membership.MembershipResponse
import com.example.myfitcompanion.model.plan.PlanResponse
import com.example.myfitcompanion.model.register.RegisterRequest
import com.example.myfitcompanion.model.register.RegisterResponse
import com.example.myfitcompanion.model.trainer.TrainerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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
}