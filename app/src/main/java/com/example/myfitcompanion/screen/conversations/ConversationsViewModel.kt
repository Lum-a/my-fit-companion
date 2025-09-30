package com.example.myfitcompanion.screen.conversations

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.db.room.dao.ChatMessageDao
import com.example.myfitcompanion.db.room.dao.UserDao
import com.example.myfitcompanion.db.room.entities.User
import com.example.myfitcompanion.repository.UserRepository
import com.example.myfitcompanion.screen.trainer.TrainerRepository
import com.example.myfitcompanion.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ConversationItem(
    val userId: Int,
    val userName: String,
    val lastMessage: String,
    val room: String,
    val unreadCount: Int = 0,
    val lastMessageTime: Long = 0L
)

@HiltViewModel
class ConversationsViewModel @Inject constructor(
    private val chatMessageDao: ChatMessageDao,
    private val userDao: UserDao,
    private val trainerRepository: TrainerRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _conversations = MutableStateFlow<List<ConversationItem>>(emptyList())
    val conversations: StateFlow<List<ConversationItem>> = _conversations.asStateFlow()

    private val _userID = MutableStateFlow(0)
    val userID: StateFlow<Int> = _userID.asStateFlow()

    private val _userName = MutableStateFlow(Pair("", ""))
    var userName: StateFlow<Pair<String, String>> = _userName.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadCurrentUser()
        loadConversations()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                userDao.getUser().collect {
                    Log.d("Conversations", "Current user from DB: $it")
                    it?.let {
                        _userID.value = it.id
                        _userName.value = Pair(it.firstName ?: "", it.lastName ?: "")
                    }
                }

            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun loadConversations() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val currentUserId = userDao.getUser().firstOrNull()?.id ?: 0
                if (currentUserId == 0) return@launch
                val rooms = chatMessageDao.getChatRooms(currentUserId)
                Log.d("Conversations", "Found rooms: $rooms")
                val conversationItems = mutableListOf<ConversationItem>()

                rooms.forEach { room ->
                    val lastMessage = chatMessageDao.getLastMessageInRoom(room)
                    val unreadCount = chatMessageDao.getUnreadMessageCount(room, currentUserId)
                    Log.d(
                        "Conversations",
                        "Room: $room, Last Message: $lastMessage, Unread Count: $unreadCount"
                    )
                    lastMessage?.let { msg ->
                        // Determine the other user ID from the room or message
                        val otherUserId =
                            if (msg.senderId == currentUserId) msg.receiverId else msg.senderId

                        // User not found locally, fetch from API and save to DB
                        Log.d(
                            "Conversations",
                            "User $otherUserId not found locally, fetching from API"
                        )
                        var otherUser: User? = null
                        when (val result = userRepository.fetchAndSaveUserById(otherUserId)) {
                            is ResultWrapper.Success -> {
                                otherUser = result.data
                                _userName.value =
                                    Pair(otherUser.firstName ?: "", otherUser.lastName ?: "")
                                Log.d(
                                    "Conversations",
                                    "Successfully fetched and saved user: $userName"
                                )
                            }

                            is ResultWrapper.Error -> {
                                Log.e(
                                    "Conversations",
                                    "Failed to fetch user $otherUserId: ${result.message}"
                                )
                                // Fallback to senderName from message or default
                                otherUser =
                                    trainerRepository.getTrainerById(otherUserId.toLong())?.toUser()
                            }

                            ResultWrapper.Initial -> {
                                //do nothing
                            }

                            ResultWrapper.Loading -> {
                                //do nothing
                            }
                        }

                        // Trim message to 30 characters
                        val trimmedMessage = if (msg.content.length > 30) {
                            "${msg.content.take(30)}..."
                        } else {
                            msg.content
                        }

                        conversationItems.add(
                            ConversationItem(
                                userId = otherUserId,
                                userName = (otherUser?.firstName + " " + otherUser?.lastName)
                                    ?: "User Unknown",
                                lastMessage = trimmedMessage,
                                room = room,
                                unreadCount = unreadCount,
                                lastMessageTime = msg.createdAt
                            )
                        )
                    }
                }

                // Sort by last message time (most recent first)
                _conversations.value = conversationItems.sortedByDescending { it.lastMessageTime }
                _conversations.tryEmit(conversationItems.sortedByDescending { it.lastMessageTime })
            } catch (e: Exception) {
                // Handle error
                Log.e("Conversations", "Error loading conversations", e)
                _conversations.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshConversations() {
        loadConversations()
    }
}
