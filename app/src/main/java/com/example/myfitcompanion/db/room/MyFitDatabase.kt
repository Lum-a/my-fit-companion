package com.example.myfitcompanion.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myfitcompanion.model.User

@Database(entities = [(User::class)], version = 1, exportSchema = false)
abstract class MyFitDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: MyFitDatabase? = null

        fun getInstance(context: Context): MyFitDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyFitDatabase::class.java,
                        "myfit_database"
                    ).fallbackToDestructiveMigration(
                    ).build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}