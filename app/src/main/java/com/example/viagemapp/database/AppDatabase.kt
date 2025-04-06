package com.example.viagemapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.viagemapp.dao.RegisterUserDao
import com.example.viagemapp.entity.RegisterUser


@Database(
entities =[RegisterUser::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun registerUserDao(): RegisterUserDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java,
                    "RegisterUser_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }

}
