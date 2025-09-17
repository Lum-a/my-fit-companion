package com.example.myfitcompanion.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.api.model.UserResponse
import com.example.myfitcompanion.api.token.TokenManager
import com.example.myfitcompanion.repository.UserRepository
import com.example.myfitcompanion.utils.ResultWrapper
import com.example.myfitcompanion.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<ResultWrapper<UserResponse>>(ResultWrapper.Initial)
    val loginState: StateFlow<ResultWrapper<UserResponse>> = _loginState

    fun login(email: String, password: String) {
        Log.d("LoginViewModel", "Login called with email: $email")
        if (!isValidEmail(email)) {
            val message = "email or password is invalid!"
            _loginState.value = ResultWrapper.Error(message)
            Log.d("LoginViewModel", message)
            return
        }
        viewModelScope.launch {
            _loginState.value = ResultWrapper.Loading
            try {
                val response  = userRepository.login(email, password)
                Log.d("LoginViewModel", "Login API response: $response")
                if(response.isSuccessful) {
                    val loginData = response.body()
                    if(loginData != null) {
                        val user = loginData.user.asUser()
                        tokenManager.saveToken(loginData.token)
                        userRepository.insertUser(user)
                        _loginState.value = ResultWrapper.Success(loginData.user)
                        Log.d("LoginViewModel", "Login success: ${loginData.user}")
                    } else {
                        _loginState.value = ResultWrapper.Error("Empty response body")
                        Log.d("LoginViewModel", "Login error: Empty response body")
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Login failed"
                    _loginState.value = ResultWrapper.Error(errorMsg)
                    Log.d("LoginViewModel", "Login error: $errorMsg")
                }
            } catch (e: Exception) {
                _loginState.value = ResultWrapper.Error(e.message ?: "Unknown error")
                Log.d("LoginViewModel", "Login exception: ${e.message}")
            }
        }
    }
}