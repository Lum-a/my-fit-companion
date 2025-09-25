package com.example.myfitcompanion.di

import com.example.myfitcompanion.db.room.MyFitDatabase
import com.example.myfitcompanion.db.room.dao.ChatMessageDao
import com.example.myfitcompanion.screen.chat.ChatRepository
import com.example.myfitcompanion.screen.chat.SocketManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {

    @Provides
    @Singleton
    fun provideChatMessageDao(database: MyFitDatabase): ChatMessageDao {
        return database.chatMessageDao()
    }

    @Provides
    @Singleton
    fun provideSocketManager(): SocketManager {
        return SocketManager()
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        chatMessageDao: ChatMessageDao,
        socketManager: SocketManager
    ): ChatRepository {
        return ChatRepository(chatMessageDao, socketManager)
    }
}
