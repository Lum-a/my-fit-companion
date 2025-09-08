package com.example.myfitcompanion.screen.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfitcompanion.api.token.TokenManager
import com.example.myfitcompanion.model.entities.User
import com.example.myfitcompanion.api.model.RegisterRequest
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

    private val _registerState = MutableStateFlow<ResultWrapper<User>>(ResultWrapper.Initial)
    val registerState: StateFlow<ResultWrapper<User>> = _registerState

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isEmailValid = MutableStateFlow(true)
    val isEmailValid: StateFlow<Boolean> = _isEmailValid

    private val _isPasswordValid = MutableStateFlow(true)
    val isPasswordValid: StateFlow<Boolean> = _isPasswordValid

    fun onNameChanged(newName: String) {
        _name.value = newName
    }

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
        _isEmailValid.value = isValidEmail(newEmail)
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
        _isPasswordValid.value = isValidPassword(newPassword)
    }

    val canRegister: StateFlow<Boolean> = MutableStateFlow(false)
    init {
        viewModelScope.launch {
            combine(_name, _isEmailValid, _isPasswordValid) { name, emailValid, passwordValid ->
                name.isNotBlank() && emailValid && passwordValid
            }.collect {
                (canRegister as MutableStateFlow).value = it
            }
        }
    }

    fun register(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            _registerState.value = ResultWrapper.Loading
            try {

                val response = userRepository.register(registerRequest)
                if(response.isSuccessful) {
                    val body = response.body()
                    if(body != null) {
                        tokenManager.saveToken(body.token)
                        userRepository.insertUser(body.user)
                        _registerState.value = ResultWrapper.Success(body.user)
                    } else {
                        _registerState.value = ResultWrapper.Error("Empty response")
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Registration failed"
                    _registerState.value = ResultWrapper.Error(errorMsg)
                }

            } catch (e: Exception) {
                _registerState.value = ResultWrapper.Error(e.message ?: "Unknown error")
            }
        }
    }

}