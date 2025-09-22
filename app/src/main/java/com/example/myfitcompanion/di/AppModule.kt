package com.example.myfitcompanion.di

import android.content.Context
import com.example.myfitcompanion.admin.repository.AdminRepository
import com.example.myfitcompanion.admin.repository.AdminRepositoryImpl
import com.example.myfitcompanion.api.ApiService
import com.example.myfitcompanion.admin.api.AdminApiService
import com.example.myfitcompanion.api.token.TokenManager
import com.example.myfitcompanion.appwrite.AppWriteConfig
import com.example.myfitcompanion.db.room.MyFitDatabase
import com.example.myfitcompanion.db.room.dao.WorkoutsDao
import com.example.myfitcompanion.db.room.dao.TrainerDao
import com.example.myfitcompanion.db.room.dao.UserDao
import com.example.myfitcompanion.repository.UserRepository
import com.example.myfitcompanion.repository.UserRepositoryImpl
import com.example.myfitcompanion.screen.trainer.TrainerRepository
import com.example.myfitcompanion.screen.trainer.TrainerRepositoryImpl
import com.example.myfitcompanion.utils.AppWriteInteractor
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
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    fun provideUserDao(db: MyFitDatabase): UserDao = db.userDao()

    @Provides
    fun provideTrainerDao(db: MyFitDatabase): TrainerDao = db.trainerDao()

    @Provides
    fun provideWorkoutDao(db: MyFitDatabase): WorkoutsDao = db.workoutDao()

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
    fun provideTrainerRepository(
        apiService: ApiService,
        trainerDao: TrainerDao
    ): TrainerRepository {
        return TrainerRepositoryImpl(apiService, trainerDao)
    }

    @Provides
    @Singleton
    fun provideAdminRepository(
        adminApiService: AdminApiService,
        apiService: ApiService,
        userDao: UserDao
    ): AdminRepository {
        return AdminRepositoryImpl(adminApiService, apiService, userDao)
    }

    @Provides
    @Singleton
    fun provideAppWriteConfig(@ApplicationContext context: Context): AppWriteConfig {
        return AppWriteConfig(context)
    }

    @Provides
    @Singleton
    fun provideAppWriteInteractor(
        appWriteConfig: AppWriteConfig,
        @ApplicationContext context: Context
    ): AppWriteInteractor {
        return AppWriteInteractor(appWriteConfig, context)
    }
}