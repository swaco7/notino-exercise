package com.example.notinotest.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notinotest.Config

@androidx.room.Database(entities = [ProductId::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: Database? = null

        fun getDatabase(context: Context): Database {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    Config.databaseName
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}