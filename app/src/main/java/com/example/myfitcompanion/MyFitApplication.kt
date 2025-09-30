package com.example.myfitcompanion

import android.app.Application
import com.example.myfitcompanion.db.room.MyFitDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
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
        fun clearDB () {
            MyFitDatabase.getInstance(instance).clearAllTables()
        }
    }
}