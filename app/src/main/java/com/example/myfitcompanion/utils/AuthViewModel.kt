package com.example.myfitcompanion.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.MyFitApplication
import com.example.myfitcompanion.model.UserRole
import com.example.myfitcompanion.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean> = userRepository.isLoggedIn()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val userRole: StateFlow<UserRole> = userRepository.userRole()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserRole.USER)

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            withContext(Dispatchers.IO){
                MyFitApplication.clearDB()
            }
        }
    }

}