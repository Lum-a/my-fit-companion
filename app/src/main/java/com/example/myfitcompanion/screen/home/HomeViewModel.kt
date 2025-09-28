package com.example.myfitcompanion.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.api.model.ExerciseResponse
import com.example.myfitcompanion.db.room.entities.User
import com.example.myfitcompanion.repository.UserRepository
import com.example.myfitcompanion.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {

   val user: StateFlow<User?> = repository.getUser()
       .stateIn(
           scope = viewModelScope,
           started = SharingStarted.Lazily,
           initialValue = null
       )

    private val _recentExercise = MutableStateFlow<ResultWrapper<List<ExerciseResponse>>>(ResultWrapper.Initial)
    val recentExercise = _recentExercise.asStateFlow()

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            repository.logout()
            onLoggedOut()
        }
    }

    fun getRecentExercise() {
        viewModelScope.launch {
            _recentExercise.value = ResultWrapper.Loading
            when (val result = repository.getRecentExercise()) {
                is ResultWrapper.Initial -> {
                    _recentExercise.value = ResultWrapper.Initial
                }
                is ResultWrapper.Success -> {
                    _recentExercise.value = ResultWrapper.Success(result.data)
                }
                is ResultWrapper.Error -> {
                    _recentExercise.value = ResultWrapper.Error(result.message ?: "Failed to get recent exercise")
                }
                is ResultWrapper.Loading -> {
                    // Already set to loading above
                }
            }
        }
    }

}