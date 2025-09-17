package com.example.myfitcompanion.utils

fun isValidEmail(email: String): Boolean {
    val regex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}\$")
    return regex.matches(email.trim()) && email.isNotEmpty()
}

fun isValidPassword(password: String): Boolean {
    val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")
    return regex.matches(password.trim()) && password.isNotEmpty()
}