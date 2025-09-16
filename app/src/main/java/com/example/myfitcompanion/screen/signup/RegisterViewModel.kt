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
            registerRequest.name.isEmpty()
        ) {
            Log.d(TAG, "Register aborted: email or password is invalid!")
            return
        }
        Log.d(TAG, "Register called with: $registerRequest")
        viewModelScope.launch {
            _registerState.value = ResultWrapper.Loading
            try {
                val response = userRepository.register(registerRequest)
                Log.d(TAG, "Register API response: $response")
                if(response.isSuccessful) {
                    val body = response.body()
                    if(body != null) {
                        tokenManager.saveToken(body.token)
                        userRepository.insertUser(body.user.asUser())
                        _registerState.value = ResultWrapper.Success(body.user)
                        Log.d(TAG, "Registration success: ${body.user}")
                    } else {
                        _registerState.value = ResultWrapper.Error("Empty response")
                        Log.d(TAG, "Registration error: Empty response")
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Registration failed"
                    _registerState.value = ResultWrapper.Error(errorMsg)
                    Log.d(TAG, "Registration error: $errorMsg")
                }
            } catch (e: Exception) {
                _registerState.value = ResultWrapper.Error(e.message ?: "Unknown error")
                Log.d(TAG, "Registration exception: ${e.message}")
            }
        }
    }

}