package com.example.myfitcompanion.api.token

fun interface TokenProvider {
    fun getToken(): String?
}