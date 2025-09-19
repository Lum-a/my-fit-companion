package com.example.myfitcompanion.screen.trainer

import com.example.myfitcompanion.api.model.TrainerResponse
import com.example.myfitcompanion.model.entities.Trainer
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface TrainerRepository {
    suspend fun getTrainersFromApi(): Response<List<TrainerResponse>>
    suspend fun insertTrainer(trainer: Trainer)
    fun getAllTrainers(): Flow<List<Trainer>>
    suspend fun getTrainerById(trainerId: Long): Trainer?
    suspend fun syncTrainersFromApi()
}