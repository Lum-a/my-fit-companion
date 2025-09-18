package com.example.myfitcompanion.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean> = userRepository.isLoggedIn()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val isAdmin: StateFlow<Boolean> = userRepository.isAdmin()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

}