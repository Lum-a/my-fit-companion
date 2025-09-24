package com.example.myfitcompanion.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.db.room.entities.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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

    // Track pending messages to avoid duplicates
    private val pendingMessages = mutableMapOf<String, String>() // tempId -> content

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
                chatRepository.getMessagesForRoom(room).distinctUntilChanged().collect { messages ->
                    _uiState.update { it.copy(messages = messages) }
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
        pendingMessages[tempId] = content

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
                                // Check if this is a confirmation of our own sent message
                                val matchingTempId = pendingMessages.entries.find { (_, content) ->
                                    content == serverMessage.content &&
                                    serverMessage.senderId == currentUserId
                                }?.key

                                if (matchingTempId != null) {
                                    // Remove the temporary message and clear from pending
                                    chatRepository.deleteMessage(matchingTempId)
                                    pendingMessages.remove(matchingTempId)
                                }

                                // Insert the server message
                                chatRepository.insertMessage(serverMessage)
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
    }

    fun retryConnection() {
        val userId = currentUserId ?: return
        val peerId = currentPeerId ?: return
        val peerName = _uiState.value.peerName
        val serverUrl = "wss://my-fit-companion-production.up.railway.app" // Use your server URL

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
