package com.example.myfitcompanion.screen.trainer

import android.util.Log
import com.example.myfitcompanion.api.ApiService
import com.example.myfitcompanion.api.model.TrainerResponse
import com.example.myfitcompanion.db.room.dao.TrainerDao
import com.example.myfitcompanion.db.room.entities.Trainer
import com.example.myfitcompanion.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainerRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val trainerDao: TrainerDao
): TrainerRepository {

    override suspend fun getTrainersFromApi(): ResultWrapper<List<TrainerResponse>> = try {
        val response = apiService.getTrainers()
        ResultWrapper.Success(response)
    } catch (e: Exception) {
        Log.e("TrainerRepositoryImpl", "Error getting trainers from API: ${e.message}")
        ResultWrapper.Error(e.message)
    }

    override suspend fun insertTrainer(trainer: Trainer) {
        trainerDao.insertTrainer(trainer)
    }

    override fun getAllTrainers(): Flow<List<Trainer>> {
        return trainerDao.getAllTrainers()
    }

    override suspend fun getTrainerById(trainerId: Long): Trainer? {
        return trainerDao.getTrainerById(trainerId)
    }

    override suspend fun syncTrainersFromApi(): ResultWrapper<Unit> = try {
        when (val result = getTrainersFromApi()) {
            is ResultWrapper.Success -> {
                Log.d("TrainerRepositoryImpl", "trainers: ${result.data}")
                // Convert TrainerResponse to Trainer entities and insert to database
                result.data.forEach { trainerResponse ->
                    val trainer = Trainer(
                        trainerId = trainerResponse.trainerId,
                        firstName = trainerResponse.firstName,
                        lastName = trainerResponse.lastName,
                        specialization = trainerResponse.specialization,
                        contactInfo = trainerResponse.contactInfo
                    )
                    insertTrainer(trainer)
                }
                ResultWrapper.Success(Unit)
            }
            is ResultWrapper.Error -> {
                Log.e("TrainerRepositoryImpl", "Error syncing trainers: ${result.message}")
                ResultWrapper.Error(result.message)
            }
            else -> ResultWrapper.Error("Unknown error occurred")
        }
    } catch (e: Exception) {
        Log.e("TrainerRepositoryImpl", "Error syncing trainers: ${e.message}")
        ResultWrapper.Error(e.message)
    }
}
