package com.example.myfitcompanion.admin.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfitcompanion.ui.theme.myFitColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun AdminScreen(
    onNavigateToUsers: () -> Unit = {},
    onNavigateToWorkouts: () -> Unit = {},
    onNavigateToMeals: () -> Unit = {},
    onNavigateToTrainers: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard", color = myFitColors.current.gold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = myFitColors.current.background,
                    titleContentColor = myFitColors.current.gold,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout",
                        tint = myFitColors.current.gold,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable { onLogout() }
                    )
                }
            )
        },
        containerColor = myFitColors.current.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AdminBox(
                title = "Users",
                description = "Manage all registered users",
                icon = Icons.Default.Person,
                onClick = onNavigateToUsers
            )
            AdminBox(
                title = "Workouts",
                description = "Manage available fitness workouts",
                icon = Icons.Default.Menu,
                onClick = onNavigateToWorkouts
            )

            AdminBox(
                title = "Meals",
                description = "Manage Meals",
                icon = Icons.Default.Favorite,
                onClick = onNavigateToMeals
            )

            AdminBox(
                title = "Trainers",
                description = "Manage Trainers",
                icon = Icons.Default.Person,
                onClick = onNavigateToTrainers
            )
        }
    }
}

@Composable
fun AdminBox(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = myFitColors.current.cardsGrey),
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                icon,
                contentDescription = title,
                tint = myFitColors.current.gold,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(description, fontSize = 14.sp, color = myFitColors.current.lightOrange)
            }
        }
    }
}
