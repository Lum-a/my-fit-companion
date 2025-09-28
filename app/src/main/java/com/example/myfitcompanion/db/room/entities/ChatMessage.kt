package com.example.myfitcompanion.db.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "chat_messages",
    indices = [
        Index(value = ["room", "createdAt"]),
        Index(value = ["senderId", "receiverId"]),
        Index(value = ["room"])
    ]
)
data class ChatMessage(
    @PrimaryKey
    val id: String,
    val room: String,
    val senderId: Int,
    val receiverId: Int,
    val content: String,
    val messageType: String = "text", // text, image, file, emoji
    val isRead: Boolean = false,
    val readAt: Long? = null,
    val editedAt: Long? = null,
    val isDeleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val senderName: String? = null
)
