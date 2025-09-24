package com.example.myfitcompanion.screen.chat

import com.example.myfitcompanion.db.room.dao.ChatMessageDao
import com.example.myfitcompanion.db.room.entities.ChatMessage
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val chatMessageDao: ChatMessageDao,
    private val socketManager: SocketManager
) {

    fun getMessagesForRoom(room: String): Flow<List<ChatMessage>> {
        return chatMessageDao.getMessagesForRoom(room)
    }

    suspend fun insertMessage(message: ChatMessage) {
        chatMessageDao.insertMessage(message)
    }

    suspend fun insertMessages(messages: List<ChatMessage>) {
        chatMessageDao.insertMessages(messages)
    }

    suspend fun markMessagesAsRead(messageIds: List<String>, currentUserId: Int) {
        val readAt = System.currentTimeMillis()
        chatMessageDao.markMessagesAsRead(messageIds, readAt)

        // Also send read receipt via socket
        if (messageIds.isNotEmpty()) {
            val firstMessage = chatMessageDao.getMessagesForRoomPaginated(
                generateRoomId(currentUserId, getOtherUserId(messageIds.first(), currentUserId)),
                1,
                0
            ).firstOrNull()
            firstMessage?.let {
                socketManager.markMessagesAsRead(messageIds, it.senderId)
            }
        }
    }

    suspend fun getChatRooms(userId: Int): List<String> {
        return chatMessageDao.getChatRooms(userId)
    }

    suspend fun getUnreadMessageCount(room: String, currentUserId: Int): Int {
        return chatMessageDao.getUnreadMessageCount(room, currentUserId)
    }

    suspend fun getLastMessageInRoom(room: String): ChatMessage? {
        return chatMessageDao.getLastMessageInRoom(room)
    }

    suspend fun deleteMessage(messageId: String) {
        chatMessageDao.deleteMessage(messageId)
    }

    suspend fun deleteAllMessagesInRoom(room: String) {
        chatMessageDao.deleteAllMessagesInRoom(room)
    }

    // Socket operations
    fun connectSocket(serverUrl: String) {
        socketManager.connect(serverUrl)
    }

    fun authenticateUser(userId: Int, firstName: String, lastName: String) {
        socketManager.authenticate(userId, firstName, lastName)
    }

    fun joinChatRoom(userId: Int, peerId: Int, peerName: String) {
        socketManager.joinRoom(userId, peerId, peerName)
    }

    fun sendMessage(senderId: Int, receiverId: Int, content: String, messageType: String = "text") {
        socketManager.sendMessage(senderId, receiverId, content, messageType)
    }

    fun sendTypingIndicator(receiverId: Int, isTyping: Boolean) {
        socketManager.sendTypingIndicator(receiverId, isTyping)
    }

    fun getSocketEvents(): Flow<ChatEvent> {
        return socketManager.messageEvents
    }

    fun requestHistory(peerId: Int, limit: Int = 50, offset: Int = 0) {
        socketManager.getHistory(peerId, limit, offset)
    }

    fun disconnectSocket() {
        socketManager.disconnect()
    }

    fun isSocketConnected(): Boolean {
        return socketManager.isConnected()
    }

    // Helper functions
    fun generateRoomId(userId1: Int, userId2: Int): String {
        return listOf(userId1, userId2).sorted().joinToString(":")
    }

    private fun getOtherUserId(messageId: String, currentUserId: Int): Int {
        // This is a simplified implementation. In a real app, you'd need to
        // track the relationship between message IDs and user IDs properly
        return 0 // Placeholder - implement based on your needs
    }

    // Helper function to parse server messages into ChatMessage entities
    fun parseServerMessage(messageData: JSONObject): ChatMessage? {
        return try {
            val senderId = messageData.getInt("senderId")
            val receiverId = messageData.getInt("receiverId")

            ChatMessage(
                id = messageData.getString("_id"),
                room = generateRoomId(senderId, receiverId), // Normalize room ID
                senderId = senderId,
                receiverId = receiverId,
                content = messageData.getString("content"),
                messageType = messageData.optString("messageType", "text"),
                isRead = messageData.optBoolean("isRead", false),
                readAt = if (messageData.has("readAt")) messageData.getLong("readAt") else null,
                editedAt = if (messageData.has("editedAt")) messageData.getLong("editedAt") else null,
                isDeleted = messageData.optBoolean("isDeleted", false),
                createdAt = messageData.optLong("createdAt", System.currentTimeMillis()),
                senderName = messageData.optString("senderName", null)
            )
        } catch (e: Exception) {
            null
        }
    }

    // Helper function to parse message history from server
    fun parseMessageHistory(historyData: JSONObject): List<ChatMessage> {
        return try {
            val messagesArray = historyData.getJSONArray("messages")
            val messages = mutableListOf<ChatMessage>()

            for (i in 0 until messagesArray.length()) {
                val messageObj = messagesArray.getJSONObject(i)
                parseServerMessage(messageObj)?.let { message ->
                    messages.add(message)
                }
            }

            messages
        } catch (e: Exception) {
            emptyList()
        }
    }
}