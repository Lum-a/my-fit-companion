package com.example.myfitcompanion.screen.trainer

import com.example.myfitcompanion.api.model.TrainerResponse
import com.example.myfitcompanion.model.entities.Trainer
import com.example.myfitcompanion.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface TrainerRepository {
    suspend fun getTrainersFromApi(): ResultWrapper<List<TrainerResponse>>
    suspend fun insertTrainer(trainer: Trainer)
    fun getAllTrainers(): Flow<List<Trainer>>
    suspend fun getTrainerById(trainerId: Long): Trainer?
    suspend fun syncTrainersFromApi(): ResultWrapper<Unit>
}