package com.example.myfitcompanion.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myfitcompanion.db.room.dao.GymClassDao
import com.example.myfitcompanion.db.room.dao.MembershipDao
import com.example.myfitcompanion.db.room.dao.PlanDao
import com.example.myfitcompanion.db.room.dao.TrainerDao
import com.example.myfitcompanion.db.room.dao.UserDao
import com.example.myfitcompanion.model.entities.GymClass
import com.example.myfitcompanion.model.entities.Membership
import com.example.myfitcompanion.model.entities.Plan
import com.example.myfitcompanion.model.entities.Trainer
import com.example.myfitcompanion.model.entities.User

@Database(
    entities = [
        User::class,
        Membership::class,
        Trainer::class,
        GymClass::class,
        Plan::class
    ],
    version = 4,
    exportSchema = false
)
abstract class MyFitDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun membershipDao(): MembershipDao
    abstract fun trainerDao(): TrainerDao
    abstract fun gymClassDao(): GymClassDao
    abstract fun planDao(): PlanDao

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