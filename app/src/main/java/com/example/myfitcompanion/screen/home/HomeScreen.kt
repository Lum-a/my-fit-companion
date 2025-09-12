package com.example.myfitcompanion.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onLogout: () -> Unit = {}
) {
    val userData by viewModel.user.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxWidth().padding(20.dp),
        horizontalAlignment = Alignment.End
    ) {

        Icon(modifier = modifier.clickable {
            viewModel.logout { onLogout() }
        },
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = "logout"
        )
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =  Arrangement.Center
    ) {
        Text( text = "Welcome ${userData?.name}", fontSize = 25.sp)
    }

}


@Preview
@Composable
fun HomeScreenTest(
    userName: String = "Laccug",
    membershipActive: Boolean = true,
    onCheckInClick: () -> Unit = {},
    onClassesClick: () -> Unit = {},
    onTrainersClick: () -> Unit = {},
    onPlansClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)) // dark mode background
            .padding(16.dp)
    ) {
        // Welcome message
        Text(
            text = "Welcome back, $userName 👋",
            style = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Membership card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1C)),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Membership",
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.White)
                )
                Text(
                    text = if (membershipActive) "Active" else "Expired",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (membershipActive) Color(0xFFFFA726) else Color.Red
                    )
                )
            }
        }

        // Quick actions grid
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                QuickActionCard(
                    label = "Check In",
                    icon = Icons.Default.Search,
                    onClick = onCheckInClick
                )
            }
            item {
                QuickActionCard(
                    label = "Classes",
                    icon = Icons.Default.Home,
                    onClick = onClassesClick
                )
            }
            item {
                QuickActionCard(
                    label = "Trainers",
                    icon = Icons.Default.AccountCircle,
                    onClick = onTrainersClick
                )
            }
            item {
                QuickActionCard(
                    label = "Plans",
                    icon = Icons.Default.Create,
                    onClick = onPlansClick
                )
            }
        }
    }
}

@Composable
fun QuickActionCard(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1C)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Gradient icon background
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(Color(0xFFFFA726), Color(0xFFFFEB3B))
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
        }
    }
}
