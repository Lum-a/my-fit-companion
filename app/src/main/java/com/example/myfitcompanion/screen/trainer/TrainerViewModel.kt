package com.example.myfitcompanion.screen.trainer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.db.room.entities.Trainer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainerViewModel @Inject constructor(
    private val trainerRepository: TrainerRepository
): ViewModel() {

    val trainers: StateFlow<List<Trainer>> = trainerRepository.getAllTrainers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    val userID: StateFlow<Int> = trainerRepository.getUser()
        .map { it?.id ?: 0}
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = 0
        )

    val userName: StateFlow<String> = trainerRepository.getUser()
        .map { it?.username ?: "" }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ""
        )

    init {
        // Auto-sync trainers when ViewModel is created
        syncTrainers()
    }

    fun syncTrainers() {
        viewModelScope.launch {
            try {
                trainerRepository.syncTrainersFromApi()
            } catch (e: Exception) {
                Log.e("TrainerViewModel", "Error syncing trainers: ${e.message}" )
            }
        }
    }
}