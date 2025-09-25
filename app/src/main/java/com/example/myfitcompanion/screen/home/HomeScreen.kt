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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myfitcompanion.ui.theme.myFitColors
import com.example.myfitcompanion.utils.ResultWrapper

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onLogout: () -> Unit = {},
    onCheckInClick: () -> Unit = {},
    onWorkoutClick: () -> Unit = {},
    onTrainersClick: () -> Unit = {},
    onMealsClick: () -> Unit = {},
    onRecentExerciseClick: (videoId: String) -> Unit  = {}
) {
    val userData by viewModel.user.collectAsStateWithLifecycle()
    val recentExerciseState by viewModel.recentExercise.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(myFitColors.current.background)
            .padding(16.dp)
    ) {
        // Logout button positioned at top right
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            Icon(
                modifier = Modifier.clickable {
                    viewModel.logout { onLogout() }
                },
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "logout",
                tint = Color.White
            )
        }

        // Welcome message
        Text(
            text = "Welcome back, ${userData?.firstName ?: "User"} 👋",
            style = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
            modifier = Modifier.padding(bottom = 12.dp, top = 8.dp)
        )

        // Recent Exercises
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = myFitColors.current.cardsGrey),
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
                    text = "Recent Exercise",
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.White)
                )

                when (val state = recentExerciseState) {
                    is ResultWrapper.Initial -> {
                        LaunchedEffect(Unit) {
                            viewModel.getRecentExercise()
                        }
                    }
                    is ResultWrapper.Loading -> {
                        Text(
                            text = "Loading...",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = myFitColors.current.orange
                            )
                        )
                    }
                    is ResultWrapper.Success -> {
                        val exercise = state.data
                        Text(
                            modifier = modifier.clickable(
                                enabled = exercise.videoId.isNotEmpty(),
                                onClick = { onRecentExerciseClick(exercise.videoId) }
                            ),
                            text = "${exercise.name} - ${exercise.description}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = myFitColors.current.orange
                            )
                        )
                    }
                    is ResultWrapper.Error -> {
                        Text(
                            text = "No recent exercises found",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = myFitColors.current.orange
                            )
                        )
                    }
                }
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
                    label = "Workouts",
                    icon = Icons.Default.Home,
                    onClick = onWorkoutClick
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
                    label = "Meals",
                    icon = Icons.Default.Create,
                    onClick = onMealsClick
                )
            }
        }
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
            .background(myFitColors.current.background) // dark mode background
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
            colors = CardDefaults.cardColors(containerColor = myFitColors.current.cardsGrey),
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
                        color = if (membershipActive) myFitColors.current.orange else Color.Red
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
        colors = CardDefaults.cardColors(containerColor = myFitColors.current.cardsGrey),
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
                            listOf(myFitColors.current.orange, myFitColors.current.yellow)
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
