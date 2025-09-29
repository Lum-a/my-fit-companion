package com.example.myfitcompanion.screen.trainer

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
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.myfitcompanion.db.room.entities.Trainer
import com.example.myfitcompanion.ui.theme.MyFitCompanionTheme
import com.example.myfitcompanion.ui.theme.myFitColors

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
    val userName by viewModel.userName.collectAsStateWithLifecycle()
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
                    text = "Our Trainers",
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
                    onClick = { onTrainerClick(currentUserId, "${userName.first} ${userName.second}", trainer.trainerId, "${trainer.firstName} ${trainer.lastName}")}
                )
            }
        }
    }
}

@Composable
fun TrainerCard(
    trainer: Trainer,
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
            // Trainer profile picture
            TrainerProfileImage(
                imageUrl = getTrainerImageUrl(trainer.firstName),
                trainerName = trainer.firstName,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Trainer info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "${trainer.firstName} ${trainer.lastName}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (trainer.specialization != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Specialization",
                            tint = myFitColors.current.gold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = trainer.specialization,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = myFitColors.current.gold,
                                fontWeight = FontWeight.Medium
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (trainer.contactInfo != null) {
                    Text(
                        text = "Contact: ${trainer.contactInfo}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun TrainerProfileImage(
    imageUrl: String,
    trainerName: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.clip(CircleShape)
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = "$trainerName profile picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(myFitColors.current.cardsGrey),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = myFitColors.current.gold,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                is AsyncImagePainter.State.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(myFitColors.current.cardsGrey),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Default trainer image",
                            tint = Color.Gray,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
                else -> {
                    SubcomposeAsyncImageContent()
                }
            }
        }
    }
}

// Helper function to get trainer image URL based on specialization
private fun getTrainerImageUrl(trainerName: String): String {
    // In a real app, this would come from the server
    // For demo purposes, using stock fitness trainer images
    val imageUrls = listOf(
        "https://images.pexels.com/photos/1552252/pexels-photo-1552252.jpeg",
        "https://images.pexels.com/photos/4754146/pexels-photo-4754146.jpeg",
        "https://images.pexels.com/photos/8401262/pexels-photo-8401262.jpeg",
        "https://images.pexels.com/photos/4148844/pexels-photo-4148844.jpeg",
        "https://images.pexels.com/photos/3768916/pexels-photo-3768916.jpeg"
    )
    return imageUrls[trainerName.hashCode().mod(imageUrls.size)]
}

// Sample data for preview
private fun getSampleTrainers(): List<Trainer> {
    return listOf(
        Trainer(
            trainerId = 1,
            firstName = "Sarah",
            lastName = "Johnson",
            specialization = "HIIT & Strength Training",
            contactInfo = "sarah.j@myfit.com"
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

@Preview(showBackground = true)
@Composable
fun TrainerScreenPreview() {
    MyFitCompanionTheme {
        TrainerScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun TrainerCardPreview() {
    MyFitCompanionTheme {
        Box(
            modifier = Modifier
                .background(myFitColors.current.background)
                .padding(16.dp)
        ) {
            TrainerCard(
                trainer = Trainer(
                    trainerId = 1,
                    firstName = "Sarah",
                    lastName = "Johnson",
                    specialization = "HIIT & Strength Training",
                    contactInfo = "sarah.j@myfit.com"
                ),
                onClick = {}
            )
        }
    }
}