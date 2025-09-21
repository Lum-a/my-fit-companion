package com.example.myfitcompanion.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myfitcompanion.db.room.dao.ExerciseDao
import com.example.myfitcompanion.db.room.dao.MealDao
import com.example.myfitcompanion.db.room.dao.TrainerDao
import com.example.myfitcompanion.db.room.dao.UserDao
import com.example.myfitcompanion.db.room.dao.WorkoutsDao
import com.example.myfitcompanion.model.entities.Exercise
import com.example.myfitcompanion.model.entities.Meal
import com.example.myfitcompanion.model.entities.Workout
import com.example.myfitcompanion.model.entities.Trainer
import com.example.myfitcompanion.model.entities.User

@Database(
    entities = [
        User::class,
        Trainer::class,
        Workout::class,
        Exercise::class,
        Meal::class
    ],
    version = 7,
    exportSchema = false
)
abstract class MyFitDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun mealDao(): MealDao
    abstract fun trainerDao(): TrainerDao
    abstract fun workoutDao(): WorkoutsDao
    abstract fun exerciseDao(): ExerciseDao

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
                        false
                    ).build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}