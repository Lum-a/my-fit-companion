package com.example.myfitcompanion.screen.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myfitcompanion.db.room.entities.ChatMessage
import com.example.myfitcompanion.ui.theme.myFitColors
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    userId: Int=0,
    peerId: Int=0,
    peerName: String,
    onNavigateBack: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val messageText by viewModel.messageText.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()

    // Initialize chat when the screen loads
    LaunchedEffect(userId, peerId) {
        val serverUrl = "https://my-fit-companion-production.up.railway.app"
        viewModel.initializeChat(userId, peerId, peerName, serverUrl)
    }

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(myFitColors.current.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = peerName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Text(
                        text = uiState.peerOnlineStatus.ifEmpty { uiState.connectionStatus },
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = myFitColors.current.cardsGrey
            )
        )

        // Error message
        uiState.errorMessage?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = error,
                        color = Color.Red,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = { viewModel.retryConnection() }) {
                        Text("Retry", color = Color.Red)
                    }
                    TextButton(onClick = { viewModel.clearError() }) {
                        Text("Dismiss", color = Color.Gray)
                    }
                }
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.messages) { message ->
                MessageBubble(
                    message = message,
                    isOwnMessage = message.senderId == userId,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Typing indicator
            if (uiState.peerTyping.isNotEmpty()) {
                item {
                    Text(
                        text = uiState.peerTyping,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                    )
                }
            }
        }

        // Message Input
        MessageInput(
            messageText = messageText,
            onMessageChange = viewModel::updateMessageText,
            onSendMessage = {
                viewModel.sendMessage()
                keyboardController?.hide()
            },
            isConnected = uiState.isConnected
        )
    }
}

@Composable
fun MessageBubble(
    message: ChatMessage,
    isOwnMessage: Boolean,
    modifier: Modifier = Modifier
) {
    val alignment = if (isOwnMessage) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor = if (isOwnMessage) myFitColors.current.orange else myFitColors.current.cardsGrey
    val textColor = Color.White

    Box(
        modifier = modifier,
        contentAlignment = alignment
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isOwnMessage) 16.dp else 4.dp,
                bottomEnd = if (isOwnMessage) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(containerColor = bubbleColor)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = message.content,
                    color = textColor,
                    fontSize = 14.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = formatTime(message.createdAt),
                        fontSize = 10.sp,
                        color = textColor.copy(alpha = 0.7f)
                    )

                    if (isOwnMessage) {
                        Text(
                            text = if (message.isRead) "✓✓" else "✓",
                            fontSize = 10.sp,
                            color = if (message.isRead) myFitColors.current.yellow else textColor.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    isConnected: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = myFitColors.current.cardsGrey)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = onMessageChange,
                placeholder = {
                    Text(
                        text = if (isConnected) "Type a message..." else "Connecting...",
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = myFitColors.current.gold,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = myFitColors.current.gold,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = { if (isConnected && messageText.isNotBlank()) onSendMessage() }
                ),
                enabled = isConnected,
                maxLines = 3
            )

            FloatingActionButton(
                onClick = onSendMessage,
                containerColor = myFitColors.current.orange,
                contentColor = Color.White,
                modifier = Modifier.size(56.dp),
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send message"
                )
            }
        }
    }
}

fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    val fakeMessages = listOf(
        ChatMessage(
            id = "1",
            room = "1:2",
            senderId = 2,
            receiverId = 1,
            content = "Hey! How's your workout going?",
            createdAt = System.currentTimeMillis() - 600000, // 10 minutes ago
            senderName = "John"
        ),
        ChatMessage(
            id = "2",
            room = "1:2",
            senderId = 1,
            receiverId = 2,
            content = "Great! Just finished my leg day. The squats were intense 💪",
            createdAt = System.currentTimeMillis() - 480000, // 8 minutes ago
            senderName = "You"
        ),
        ChatMessage(
            id = "3",
            room = "1:2",
            senderId = 2,
            receiverId = 1,
            content = "Nice! I'm planning to hit the gym later today. Any tips for leg day?",
            createdAt = System.currentTimeMillis() - 360000, // 6 minutes ago
            senderName = "John"
        ),
        ChatMessage(
            id = "4",
            room = "1:2",
            senderId = 1,
            receiverId = 2,
            content = "Sure! Make sure to warm up properly and focus on form over weight. Start with bodyweight squats if you're a beginner.",
            createdAt = System.currentTimeMillis() - 240000, // 4 minutes ago
            senderName = "You",
            isRead = true
        ),
        ChatMessage(
            id = "5",
            room = "1:2",
            senderId = 2,
            receiverId = 1,
            content = "Thanks for the advice! I'll definitely keep that in mind 👍",
            createdAt = System.currentTimeMillis() - 120000, // 2 minutes ago
            senderName = "John"
        ),
        ChatMessage(
            id = "6",
            room = "1:2",
            senderId = 1,
            receiverId = 2,
            content = "No problem! Let me know how it goes. Good luck! 🔥",
            createdAt = System.currentTimeMillis() - 60000, // 1 minute ago
            senderName = "You"
        )
    )

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Top App Bar
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "John Doe",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                        Text(
                            text = "Online",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
                    .background(Color.DarkGray),
            )

            // Messages List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(fakeMessages) { message ->
                    MessageBubblePreview(
                        message = message,
                        isOwnMessage = message.senderId == 1,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Typing indicator
                item {
                    Text(
                        text = "John is typing...",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                    )
                }
            }

            // Message Input
            MessageInputPreview()
        }
    }
}

@Composable
fun MessageBubblePreview(
    message: ChatMessage,
    isOwnMessage: Boolean,
    modifier: Modifier = Modifier
) {
    val alignment = if (isOwnMessage) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor = if (isOwnMessage) Color(0xFFFF6B35) else Color.DarkGray
    val textColor = Color.White

    Box(
        modifier = modifier,
        contentAlignment = alignment
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isOwnMessage) 16.dp else 4.dp,
                bottomEnd = if (isOwnMessage) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(containerColor = bubbleColor)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = message.content,
                    color = textColor,
                    fontSize = 14.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = formatTime(message.createdAt),
                        fontSize = 10.sp,
                        color = textColor.copy(alpha = 0.7f)
                    )

                    if (isOwnMessage) {
                        Text(
                            text = if (message.isRead) "✓✓" else "✓",
                            fontSize = 10.sp,
                            color = if (message.isRead) Color.Yellow else textColor.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInputPreview() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = { },
                placeholder = {
                    Text(
                        text = "Type a message...",
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFFD700),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFFFFD700),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                maxLines = 3
            )

            FloatingActionButton(
                onClick = { },
                containerColor = Color(0xFFFF6B35),
                contentColor = Color.White,
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send message"
                )
            }
        }
    }
}
