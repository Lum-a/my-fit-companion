package com.example.myfitcompanion.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.api.model.SessionResponse
import com.example.myfitcompanion.model.entities.User
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

    private val _recentSessionState = MutableStateFlow<ResultWrapper<SessionResponse>>(ResultWrapper.Initial)
    val recentSessionState: StateFlow<ResultWrapper<SessionResponse>> = _recentSessionState.asStateFlow()

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            repository.logout()
            onLoggedOut()
        }
    }

    fun getRecentSession() {
        viewModelScope.launch {
            _recentSessionState.value = ResultWrapper.Loading
            when (val result = repository.getRecentSession()) {
                is ResultWrapper.Success -> {
                    _recentSessionState.value = ResultWrapper.Success(result.data)
                }
                is ResultWrapper.Error -> {
                    _recentSessionState.value = ResultWrapper.Error(result.message ?: "Failed to get recent session")
                }
                is ResultWrapper.Loading -> {
                    // Already set to loading above
                }
                is ResultWrapper.Initial -> {
                    _recentSessionState.value = ResultWrapper.Error("Unexpected initial state")
                }
            }
        }
    }

}