package com.example.myfitcompanion.screen.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.api.model.SessionResponse
import com.example.myfitcompanion.repository.UserRepository
import com.example.myfitcompanion.utils.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _sessionsState = MutableStateFlow<ResultWrapper<List<SessionResponse>>>(ResultWrapper.Initial)
    val sessionsState: StateFlow<ResultWrapper<List<SessionResponse>>> = _sessionsState.asStateFlow()

    fun loadSessions() {
        viewModelScope.launch {
            _sessionsState.value = ResultWrapper.Loading
            when (val result = userRepository.getSessions()) {
                is ResultWrapper.Success -> {
                    _sessionsState.value = ResultWrapper.Success(result.data)
                }
                is ResultWrapper.Error -> {
                    _sessionsState.value = ResultWrapper.Error(result.message ?: "Failed to load sessions")
                }
                is ResultWrapper.Loading -> {
                    // Already set to loading above
                }
                is ResultWrapper.Initial -> {
                    _sessionsState.value = ResultWrapper.Error("Unexpected initial state")
                }
            }
        }
    }

    fun addRecentSession(sessionId: Int) {
        viewModelScope.launch {
            userRepository.addRecentSession(sessionId)
        }
    }
}