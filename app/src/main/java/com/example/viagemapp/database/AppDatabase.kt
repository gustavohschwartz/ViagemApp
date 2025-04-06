package com.example.viagemapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.viagemapp.dao.RegisterUserDao
import com.example.viagemapp.dao.TripDao
import com.example.viagemapp.entity.RegisterUser
import com.example.viagemapp.entity.Trip

@Database(
    entities = [RegisterUser::class, Trip::class],
    version = 2, // aumente se modificar o banco
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun registerUserDao(): RegisterUserDao
    abstract fun tripDao(): TripDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "RegisterUser_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
