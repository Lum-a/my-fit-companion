package com.example.myfitcompanion.screen.trainer

import android.util.Log
import com.example.myfitcompanion.api.ApiService
import com.example.myfitcompanion.api.model.TrainerResponse
import com.example.myfitcompanion.db.room.dao.TrainerDao
import com.example.myfitcompanion.model.entities.Trainer
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainerRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val trainerDao: TrainerDao
): TrainerRepository {

    override suspend fun getTrainersFromApi(): Response<List<TrainerResponse>> {
        return apiService.getTrainers()
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

    override suspend fun syncTrainersFromApi() {
        try {
            val response = getTrainersFromApi()
            Log.d("TrainerRepositoryImpl", "response:${response}")
            if (response.isSuccessful) {
                response.body()?.let { trainerResponses ->
                    // Convert TrainerResponse to Trainer entities and insert to database
                    trainerResponses.forEach { trainerResponse ->
                        val trainer = Trainer(
                            trainerId = trainerResponse.trainerId,
                            firstName = trainerResponse.firstName,
                            lastName = trainerResponse.lastName,
                            specialization = trainerResponse.specialization,
                            contactInfo = trainerResponse.contactInfo
                        )
                        insertTrainer(trainer)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("TrainerRepositoryImpl", "Error syncing trainers: ${e.message}" )
        }
    }
}
