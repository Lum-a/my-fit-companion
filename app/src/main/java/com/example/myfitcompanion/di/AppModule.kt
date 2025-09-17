package com.example.myfitcompanion.di

import android.content.Context
import com.example.myfitcompanion.admin.repository.AdminRepository
import com.example.myfitcompanion.admin.repository.AdminRepositoryImpl
import com.example.myfitcompanion.api.ApiService
import com.example.myfitcompanion.api.admin.AdminApiService
import com.example.myfitcompanion.api.token.TokenManager
import com.example.myfitcompanion.db.room.MyFitDatabase
import com.example.myfitcompanion.db.room.dao.GymClassDao
import com.example.myfitcompanion.db.room.dao.MembershipDao
import com.example.myfitcompanion.db.room.dao.PlanDao
import com.example.myfitcompanion.db.room.dao.TrainerDao
import com.example.myfitcompanion.db.room.dao.UserDao
import com.example.myfitcompanion.repository.UserRepository
import com.example.myfitcompanion.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): MyFitDatabase {
        return MyFitDatabase.getInstance(context)
    }

    @Provides
    fun provideUserDao(db: MyFitDatabase): UserDao = db.userDao()

    @Provides
    fun provideMembershipDao(db: MyFitDatabase): MembershipDao = db.membershipDao()

    @Provides
    fun provideTrainerDao(db: MyFitDatabase): TrainerDao = db.trainerDao()

    @Provides
    fun provideGymClassDao(db: MyFitDatabase): GymClassDao = db.gymClassDao()

    @Provides
    fun providePlanDao(db: MyFitDatabase): PlanDao = db.planDao()

    @Provides
    @Singleton
    fun provideUserRepository(
        apiService: ApiService,
        userDao: UserDao,
        tokenManager: TokenManager
    ): UserRepository {
        return UserRepositoryImpl(apiService, userDao, tokenManager)
    }

    @Provides
    @Singleton
    fun provideAdminRepository(
        adminApiService: AdminApiService
    ): AdminRepository {
        return AdminRepositoryImpl(adminApiService)
    }
}