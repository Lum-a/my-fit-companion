package com.example.myfitcompanion.di

import android.content.Context
import com.example.myfitcompanion.api.ApiService
import com.example.myfitcompanion.api.token.TokenManager
import com.example.myfitcompanion.api.token.TokenProvider
import com.example.myfitcompanion.db.room.MyFitDatabase
import com.example.myfitcompanion.db.room.UserDao
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
    fun provideTokenProvider(tokenManager: TokenManager): TokenProvider {
        return tokenManager
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): MyFitDatabase {
        return MyFitDatabase.getInstance(context)
    }

    @Provides
    fun provideUserDao(database: MyFitDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        apiService: ApiService,
        userDao: UserDao
    ): UserRepository {
        return UserRepositoryImpl(apiService, userDao)
    }
}