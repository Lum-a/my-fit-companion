package com.example.myfitcompanion.screen.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.api.token.TokenManager
import com.example.myfitcompanion.api.model.RegisterRequest
import com.example.myfitcompanion.api.model.UserResponse
import com.example.myfitcompanion.repository.UserRepository
import com.example.myfitcompanion.utils.ResultWrapper
import com.example.myfitcompanion.utils.isValidEmail
import com.example.myfitcompanion.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
): ViewModel() {

    private val TAG = "RegisterViewModel"
    private val _registerState = MutableStateFlow<ResultWrapper<UserResponse>>(ResultWrapper.Initial)
    val registerState: StateFlow<ResultWrapper<UserResponse>> = _registerState

    fun register(registerRequest: RegisterRequest) {
        if (!isValidEmail(registerRequest.email) ||
            !isValidPassword(registerRequest.password) ||
            registerRequest.firstName.isEmpty() ||
            registerRequest.lastName.isEmpty()
        ) {
            val message = "First name, last name, email or password is invalid!"
            _registerState.value = ResultWrapper.Error(message)
            Log.d(TAG, "Register aborted: $message")
            return
        }
        Log.d(TAG, "Register called with: $registerRequest")
        viewModelScope.launch {
            _registerState.value = ResultWrapper.Loading
            when (val result = userRepository.register(registerRequest)) {
                is ResultWrapper.Success -> {
                    val registerData = result.data
                    val user = registerData.user.asUser()
                    tokenManager.saveToken(registerData.token)
                    userRepository.insertUser(user)
                    _registerState.value = ResultWrapper.Success(registerData.user)
                    Log.d(TAG, "Registration success: ${registerData.user}")
                }
                is ResultWrapper.Error -> {
                    _registerState.value = ResultWrapper.Error("Registration failed: ${result.message}")
                    Log.d(TAG, "Registration error: ${result.message}")
                }
                is ResultWrapper.Loading -> {
                    // Already set to loading above
                }
                is ResultWrapper.Initial -> {
                    _registerState.value = ResultWrapper.Initial
                }
            }
        }
    }

}