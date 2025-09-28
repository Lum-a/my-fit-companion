package com.example.myfitcompanion.screen.conversations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myfitcompanion.db.room.entities.Trainer
import com.example.myfitcompanion.screen.trainer.TrainerCard
import com.example.myfitcompanion.screen.trainer.TrainerViewModel
import com.example.myfitcompanion.ui.theme.myFitColors
import kotlin.collections.ifEmpty

@Composable
fun ConversationsScreen() {

}

@Composable
fun TrainerScreen(
    modifier: Modifier = Modifier,
    viewModel: TrainerViewModel = hiltViewModel(),
    onTrainerClick: (userId: Int, userName: String, peerId: Int, peerName: String) -> Unit = {_,_,_,_ ->}
) {
    // Sample data for preview - in real app this would come from ViewModel
    val trainers = getSampleTrainers()
    val realTrainers by viewModel.trainers.collectAsStateWithLifecycle()
    val trainerList = realTrainers.ifEmpty { trainers }
    val currentUserId by viewModel.userID.collectAsStateWithLifecycle()
    val currentUserName by viewModel.userName.collectAsStateWithLifecycle()
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(myFitColors.current.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Recent conversations",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(trainerList) { trainer ->
                TrainerCard(
                    trainer = trainer,
                    onClick = { onTrainerClick(currentUserId, currentUserName.first ?: "", trainer.trainerId, "${trainer.firstName} ${trainer.lastName}")}
                )
            }
        }
    }
}

// Sample data for preview
private fun getSampleTrainers(): List<Trainer> {
    return listOf(
        Trainer(
            trainerId = 1,
            firstName = "Sarah",
            lastName = "Johnson",
            specialization = "HIIT & Strength Training",
            contactInfo = ""
        ),
        Trainer(
            trainerId = 2,
            firstName = "Mike",
            lastName = "Chen",
            specialization = "Yoga & Flexibility",
            contactInfo = "mike.c@myfit.com"
        ),
        Trainer(
            trainerId = 3,
            firstName = "Jessica",
            lastName = "Rodriguez",
            specialization = "CrossFit & Conditioning",
            contactInfo = "jessica.r@myfit.com"
        ),
        Trainer(
            trainerId = 4,
            firstName = "David",
            lastName = "Thompson",
            specialization = "Powerlifting & Bodybuilding",
            contactInfo = "david.t@myfit.com"
        ),
        Trainer(
            trainerId = 5,
            firstName = "Emma",
            lastName = "Wilson",
            specialization = "Dance & Cardio",
            contactInfo = "emma.w@myfit.com"
        ),
        Trainer(
            trainerId = 6,
            firstName = "Alex",
            lastName = "Martinez",
            specialization = "Boxing & Martial Arts",
            contactInfo = "alex.m@myfit.com"
        ),
        Trainer(
            trainerId = 7,
            firstName = "Lisa",
            lastName = "Park",
            specialization = "Pilates & Core Strength",
            contactInfo = "lisa.p@myfit.com"
        )
    )
}