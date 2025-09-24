package com.example.myfitcompanion.screen.chat

import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import org.json.JSONObject
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketManager @Inject constructor() {

    private var socket: Socket? = null
    private val _messageEvents = Channel<ChatEvent>(Channel.UNLIMITED)
    val messageEvents: Flow<ChatEvent> = _messageEvents.receiveAsFlow()

    fun connect(serverUrl: String) {
        try {
            val options = IO.Options().apply {
                transports = arrayOf("websocket")
                timeout = 20000
                reconnection = true
                reconnectionAttempts = 5
                reconnectionDelay = 1000
            }

            socket = IO.socket(URI.create(serverUrl), options)

            socket?.apply {
                on(Socket.EVENT_CONNECT) {
                    _messageEvents.trySend(ChatEvent.Connected)
                }

                on(Socket.EVENT_DISCONNECT) { args ->
                    val reason = args.getOrNull(0)?.toString() ?: "Unknown"
                    _messageEvents.trySend(ChatEvent.Disconnected(reason))
                }

                on(Socket.EVENT_CONNECT_ERROR) { args ->
                    val error = args.getOrNull(0)?.toString() ?: "Connection error"
                    _messageEvents.trySend(ChatEvent.Error(error))
                }

                on("chat:authenticated") { args ->
                    val data = args.getOrNull(0) as? JSONObject
                    _messageEvents.trySend(ChatEvent.Authenticated(data))
                }

                on("chat:joined") { args ->
                    val data = args.getOrNull(0) as? JSONObject
                    _messageEvents.trySend(ChatEvent.JoinedRoom(data))
                }

                on("chat:message") { args ->
                    val data = args.getOrNull(0) as? JSONObject
                    data?.let {
                        _messageEvents.trySend(ChatEvent.MessageReceived(it))
                    }
                }

                on("chat:message_sent") { args ->
                    val data = args.getOrNull(0) as? JSONObject
                    _messageEvents.trySend(ChatEvent.MessageSent(data))
                }

                on("chat:history") { args ->
                    val data = args.getOrNull(0) as? JSONObject
                    _messageEvents.trySend(ChatEvent.HistoryReceived(data))
                }

                on("chat:typing") { args ->
                    val data = args.getOrNull(0) as? JSONObject
                    _messageEvents.trySend(ChatEvent.TypingIndicator(data))
                }

                on("chat:user_online") { args ->
                    val data = args.getOrNull(0) as? JSONObject
                    _messageEvents.trySend(ChatEvent.UserOnline(data))
                }

                on("chat:user_offline") { args ->
                    val data = args.getOrNull(0) as? JSONObject
                    _messageEvents.trySend(ChatEvent.UserOffline(data))
                }

                on("chat:error") { args ->
                    val data = args.getOrNull(0) as? JSONObject
                    val errorMessage = data?.getString("message") ?: "Unknown error"
                    _messageEvents.trySend(ChatEvent.Error(errorMessage))
                }

                connect()
            }
        } catch (e: Exception) {
            _messageEvents.trySend(ChatEvent.Error("Failed to connect: ${e.message}"))
        }
    }

    fun authenticate(userId: Int, username: String) {
        socket?.emit("chat:authenticate", JSONObject().apply {
            put("userId", userId)
            put("username", username)
        })
    }

    fun joinRoom(userId: Int, peerId: Int, peerName: String) {
        socket?.emit("chat:join", JSONObject().apply {
            put("userId", userId)
            put("peerId", peerId)
            put("peerName", peerName)
        })
    }

    fun sendMessage(senderId: Int, receiverId: Int, content: String, messageType: String = "text") {
        socket?.emit("chat:send", JSONObject().apply {
            put("senderId", senderId)
            put("receiverId", receiverId)
            put("content", content)
            put("messageType", messageType)
        })
    }

    fun sendTypingIndicator(receiverId: Int, isTyping: Boolean) {
        socket?.emit("chat:typing", JSONObject().apply {
            put("receiverId", receiverId)
            put("isTyping", isTyping)
        })
    }

    fun markMessagesAsRead(messageIds: List<String>, senderId: Int) {
        socket?.emit("chat:mark_read", JSONObject().apply {
            put("messageIds", messageIds)
            put("senderId", senderId)
        })
    }

    fun getHistory(peerId: Int, limit: Int = 50, offset: Int = 0) {
        socket?.emit("chat:get_history", JSONObject().apply {
            put("peerId", peerId)
            put("limit", limit)
            put("offset", offset)
        })
    }

    fun disconnect() {
        socket?.disconnect()
        socket = null
    }

    fun isConnected(): Boolean = socket?.connected() == true
}

sealed class ChatEvent {
    object Connected : ChatEvent()
    data class Disconnected(val reason: String) : ChatEvent()
    data class Error(val message: String) : ChatEvent()
    data class Authenticated(val data: JSONObject?) : ChatEvent()
    data class JoinedRoom(val data: JSONObject?) : ChatEvent()
    data class MessageReceived(val data: JSONObject) : ChatEvent()
    data class MessageSent(val data: JSONObject?) : ChatEvent()
    data class HistoryReceived(val data: JSONObject?) : ChatEvent()
    data class TypingIndicator(val data: JSONObject?) : ChatEvent()
    data class UserOnline(val data: JSONObject?) : ChatEvent()
    data class UserOffline(val data: JSONObject?) : ChatEvent()
}
