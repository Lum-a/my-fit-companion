package com.example.myfitcompanion.screen.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.db.room.entities.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _messageText = MutableStateFlow("")
    val messageText: StateFlow<String> = _messageText.asStateFlow()

    private var currentRoom: String? = null
    private var currentUserId: Int? = null
    private var currentPeerId: Int? = null

    // Track pending messages with more detailed info to avoid duplicates
    private val pendingMessages = mutableMapOf<String, PendingMessage>()

    init {
        observeSocketEvents()
    }

    fun initializeChat(userId: Int, peerId: Int, peerName: String, serverUrl: String) {
        currentUserId = userId
        currentPeerId = peerId
        currentRoom = chatRepository.generateRoomId(userId, peerId)

        _uiState.update {
            it.copy(
                peerName = peerName,
                isConnecting = true,
                connectionStatus = "Connecting..."
            )
        }

        // Connect to socket and authenticate
        chatRepository.connectSocket(serverUrl)
        chatRepository.authenticateUser(userId, "User$userId") // You can get username from user session

        // Load local messages first
        currentRoom?.let { room ->
            viewModelScope.launch {
                chatRepository.getMessagesForRoom(room).collect { messages ->
                    _uiState.update {
                        it.copy(messages = messages) }
                }
            }
        }
    }

    fun joinRoom() {
        val userId = currentUserId ?: return
        val peerId = currentPeerId ?: return
        val peerName = _uiState.value.peerName

        chatRepository.joinChatRoom(userId, peerId, peerName)

        // Request message history from server
        chatRepository.requestHistory(peerId)
    }

    fun sendMessage() {
        val content = _messageText.value.trim()
        if (content.isEmpty()) return

        val userId = currentUserId ?: return
        val peerId = currentPeerId ?: return

        // Generate a unique temporary ID
        val tempId = "temp_${System.currentTimeMillis()}_${userId}"

        // Track this pending message
        pendingMessages[tempId] = PendingMessage(content, System.currentTimeMillis(), userId, peerId)

        // Send via socket
        chatRepository.sendMessage(userId, peerId, content)

        // Create local message for immediate UI update
        val localMessage = ChatMessage(
            id = tempId,
            room = currentRoom ?: "",
            senderId = userId,
            receiverId = peerId,
            content = content,
            createdAt = System.currentTimeMillis(),
            senderName = "You"
        )

        viewModelScope.launch {
            chatRepository.insertMessage(localMessage)
        }

        _messageText.value = ""
        stopTyping()
    }

    fun updateMessageText(text: String) {
        _messageText.value = text

        // Send typing indicator
        val peerId = currentPeerId
        if (peerId != null && text.isNotEmpty() && !_uiState.value.isTyping) {
            startTyping()
        } else if (peerId != null && text.isEmpty() && _uiState.value.isTyping) {
            stopTyping()
        }
    }

    private fun startTyping() {
        val peerId = currentPeerId ?: return
        _uiState.update { it.copy(isTyping = true) }
        chatRepository.sendTypingIndicator(peerId, true)
    }

    private fun stopTyping() {
        val peerId = currentPeerId ?: return
        _uiState.update { it.copy(isTyping = false) }
        chatRepository.sendTypingIndicator(peerId, false)
    }

    fun markMessagesAsRead(messageIds: List<String>) {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            chatRepository.markMessagesAsRead(messageIds, userId)
        }
    }

    private fun observeSocketEvents() {
        viewModelScope.launch {
            chatRepository.getSocketEvents().collect { event ->
                when (event) {
                    is ChatEvent.Connected -> {
                        _uiState.update {
                            it.copy(
                                isConnected = true,
                                isConnecting = false,
                                connectionStatus = "Connected"
                            )
                        }
                    }

                    is ChatEvent.Disconnected -> {
                        _uiState.update {
                            it.copy(
                                isConnected = false,
                                isConnecting = false,
                                connectionStatus = "Disconnected: ${event.reason}"
                            )
                        }
                    }

                    is ChatEvent.Error -> {
                        _uiState.update {
                            it.copy(
                                isConnecting = false,
                                connectionStatus = "Error: ${event.message}",
                                errorMessage = event.message
                            )
                        }
                    }

                    is ChatEvent.Authenticated -> {
                        _uiState.update {
                            it.copy(connectionStatus = "Authenticated")
                        }
                        joinRoom()
                    }

                    is ChatEvent.JoinedRoom -> {
                        _uiState.update {
                            it.copy(connectionStatus = "Joined chat room")
                        }
                    }

                    is ChatEvent.MessageReceived -> {
                        val message = chatRepository.parseServerMessage(event.data)
                        message?.let { serverMessage ->
                            viewModelScope.launch {
                                // Check if this message belongs to the current chat
                                val isRelevantMessage = isMessageForCurrentChat(serverMessage)

                                if (isRelevantMessage && serverMessage.senderId == currentUserId) {
                                    // This is our own message - try to match with pending messages
                                    val matchingEntry = pendingMessages.entries.find { (tempId, pendingMessage) ->
                                        pendingMessage.content == serverMessage.content &&
                                        pendingMessage.senderId == serverMessage.senderId &&
                                        pendingMessage.receiverId == serverMessage.receiverId &&
                                        // Only match messages sent within the last 30 seconds to avoid stale matches
                                        (System.currentTimeMillis() - pendingMessage.timestamp) < 30000
                                    }

                                    matchingEntry?.let { (tempId, _) ->
                                        // Remove the temporary message and clear from pending
                                        chatRepository.deleteMessage(tempId)
                                        pendingMessages.remove(tempId)

                                        // Insert server message with normalized room ID
                                        val normalizedMessage = serverMessage.copy(
                                            room = currentRoom ?: ""
                                        )
                                        chatRepository.insertMessage(normalizedMessage)
                                        return@launch
                                    }
                                }
                                // If it's a relevant message (either not our own or no match found)
                                if (isRelevantMessage) {
                                    // Insert server message with normalized room ID
                                    val normalizedMessage = serverMessage.copy(
                                        room = currentRoom ?: ""
                                    )
                                    chatRepository.insertMessage(normalizedMessage)
                                }
                            }
                        }
                    }

                    is ChatEvent.MessageSent -> {
                        // Handle message sent confirmation if needed
                        _uiState.update {
                            it.copy(connectionStatus = "Message sent")
                        }
                    }

                    is ChatEvent.HistoryReceived -> {
                        event.data?.let { data ->
                            val messages = chatRepository.parseMessageHistory(data)
                            viewModelScope.launch {
                                chatRepository.insertMessages(messages)
                            }
                        }
                    }

                    is ChatEvent.TypingIndicator -> {
                        event.data?.let { data ->
                            val isTyping = data.optBoolean("isTyping", false)
                            val username = data.optString("username", "Someone")
                            _uiState.update {
                                it.copy(
                                    peerTyping = if (isTyping) "$username is typing..." else ""
                                )
                            }
                        }
                    }

                    is ChatEvent.UserOnline -> {
                        _uiState.update {
                            it.copy(peerOnlineStatus = "Online")
                        }
                    }

                    is ChatEvent.UserOffline -> {
                        _uiState.update {
                            it.copy(peerOnlineStatus = "Offline")
                        }
                    }
                }
            }
        }

        // Clean up old pending messages periodically to prevent memory leaks
        viewModelScope.launch {
            while (true) {
                delay(60000) // Check every minute
                val currentTime = System.currentTimeMillis()
                val expiredMessages = pendingMessages.filter { (_, pendingMessage) ->
                    (currentTime - pendingMessage.timestamp) > 120000 // 2 minutes timeout
                }
                expiredMessages.keys.forEach { tempId ->
                    pendingMessages.remove(tempId)
                    // Convert expired temp messages to permanent ones by updating their ID
                    // This ensures messages don't disappear even if server confirmation is lost
                    viewModelScope.launch {
                        val tempMessage = chatRepository.getMessagesForRoom(currentRoom ?: "")
                            .first()
                            .find { it.id == tempId }
                        tempMessage?.let { message ->
                            // Create a new message with a permanent ID
                            val permanentMessage = message.copy(
                                id = "local_${System.currentTimeMillis()}_${message.senderId}"
                            )
                            chatRepository.deleteMessage(tempId)
                            chatRepository.insertMessage(permanentMessage)
                        }
                    }
                }
            }
        }
    }

    // Helper function to check if a message belongs to the current chat
    private fun isMessageForCurrentChat(message: ChatMessage): Boolean {
        val userId = currentUserId ?: return false
        val peerId = currentPeerId ?: return false

        // Check if the message involves the current user and peer
        return (message.senderId == userId && message.receiverId == peerId) ||
               (message.senderId == peerId && message.receiverId == userId)
    }

    fun retryConnection() {
        val userId = currentUserId ?: return
        val peerId = currentPeerId ?: return
        val peerName = _uiState.value.peerName
        val serverUrl = "https://my-fit-companion-production.up.railway.app" // Use your server URL

        initializeChat(userId, peerId, peerName, serverUrl)
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    override fun onCleared() {
        super.onCleared()
        chatRepository.disconnectSocket()
    }
}

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val connectionStatus: String = "Not connected",
    val peerName: String = "",
    val peerOnlineStatus: String = "",
    val peerTyping: String = "",
    val isTyping: Boolean = false,
    val errorMessage: String? = null
)

data class PendingMessage(
    val content: String,
    val timestamp: Long,
    val senderId: Int,
    val receiverId: Int
)
