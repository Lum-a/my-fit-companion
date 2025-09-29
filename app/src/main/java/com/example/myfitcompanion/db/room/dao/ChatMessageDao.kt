package com.example.myfitcompanion.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myfitcompanion.db.room.entities.ChatMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<ChatMessage>)

    @Update
    suspend fun updateMessage(message: ChatMessage)

    @Query("SELECT * FROM chat_messages WHERE room = :room ORDER BY createdAt ASC")
    fun getMessagesForRoom(room: String): Flow<List<ChatMessage>>

    @Query("SELECT * FROM chat_messages WHERE room = :room ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getMessagesForRoomPaginated(room: String, limit: Int, offset: Int): List<ChatMessage>

    @Query("UPDATE chat_messages SET isRead = 1, readAt = :readAt WHERE id IN (:messageIds)")
    suspend fun markMessagesAsRead(messageIds: List<String>, readAt: Long)

    @Query("DELETE FROM chat_messages WHERE id = :messageId")
    suspend fun deleteMessage(messageId: String)

    @Query("DELETE FROM chat_messages WHERE room = :room")
    suspend fun deleteAllMessagesInRoom(room: String)

    @Query("SELECT * FROM chat_messages WHERE room = :room ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLastMessageInRoom(room: String): ChatMessage?

    @Query("SELECT COUNT(*) FROM chat_messages WHERE room = :room AND isRead = 0 AND senderId != :currentUserId")
    suspend fun getUnreadMessageCount(room: String, currentUserId: Int): Int

    @Query("SELECT DISTINCT room FROM chat_messages WHERE senderId = :userId OR receiverId = :userId ORDER BY createdAt DESC")
    suspend fun getChatRooms(userId: Int): List<String>
}

