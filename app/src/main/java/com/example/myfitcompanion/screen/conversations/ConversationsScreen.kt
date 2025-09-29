package com.example.myfitcompanion.screen.conversations

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myfitcompanion.ui.theme.myFitColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ConversationsScreen(
    modifier: Modifier = Modifier,
    viewModel: ConversationsViewModel = hiltViewModel(),
    onChatClick: (userId: Int, userName: String, peerId: Int, peerName: String) -> Unit = {_,_,_,_ ->}
) {
    val conversations by viewModel.conversations.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val currentUserId by viewModel.userID.collectAsStateWithLifecycle()
    val userName by viewModel.userName.collectAsStateWithLifecycle()
    Log.d("ConversationsScreen", "Rendering ConversationsScreen with $conversations conversations")
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(myFitColors.current.background)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = myFitColors.current.gold
            )
        } else if (conversations.isEmpty()) {
            // Center the "no conversations" message when list is empty
            Text(
                text = "No conversations yet",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Gray
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Recent Conversations",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                items(conversations) { conversation ->
                    ConversationCard(
                        conversation = conversation,
                        onClick = {
                            onChatClick(
                                currentUserId,
                                "${userName.first} ${userName.second}".trim(),
                                conversation.userId,
                                conversation.userName
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ConversationCard(
    conversation: ConversationItem,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = myFitColors.current.cardsGrey),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User profile picture with unread badge
            BadgedBox(
                badge = {
                    if (conversation.unreadCount > 0) {
                        Badge(
                            containerColor = myFitColors.current.gold,
                            contentColor = Color.Black
                        ) {
                            Text(
                                text = if (conversation.unreadCount > 99) "99+" else conversation.unreadCount.toString(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            ) {
                UserProfileImage(
                    userName = conversation.userName,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Conversation info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = conversation.userName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = conversation.lastMessage,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formatTime(conversation.lastMessageTime),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray
                    )
                )
            }
        }
    }
}

@Composable
fun UserProfileImage(
    userName: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(myFitColors.current.gold),
        contentAlignment = Alignment.Center
    ) {
        if (userName.isNotBlank()) {
            Text(
                text = userName.firstOrNull()?.toString()?.uppercase() ?: "?",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User profile",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

private fun formatTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60 * 1000 -> "Now" // Less than 1 minute
        diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)}m ago" // Less than 1 hour
        diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)}h ago" // Less than 1 day
        else -> {
            val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
            dateFormat.format(Date(timestamp))
        }
    }
}


