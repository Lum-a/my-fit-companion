package com.example.myfitcompanion.utils

sealed class ResultWrapper<out T> {
    data object Initial : ResultWrapper<Nothing>()
    data object Loading : ResultWrapper<Nothing>()
    data class Success<T>(val data: T? = null) : ResultWrapper<T>()
    data class Error(val message: String) : ResultWrapper<Nothing>()
}