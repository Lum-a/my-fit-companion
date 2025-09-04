package com.example.myfitcompanion

import android.app.Application

class MyFitApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private lateinit var instance: Application
        fun getInstance(): Application {
            return instance
        }
    }
}