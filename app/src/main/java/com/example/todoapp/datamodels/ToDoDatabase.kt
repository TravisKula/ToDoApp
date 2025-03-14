package com.example.todoapp.datamodels

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Defines Room Database

@Database(entities = [Task::class], version = 1)
@TypeConverters(Converters::class) // Registers converters
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao // Accesses DAO functions

    companion object {
        @Volatile
        private var INSTANCE: ToDoDatabase? = null
        // Ensures only 1 instance (singleton) of the Database is created

        fun getDatabase(context: Context): ToDoDatabase { // Creates the Database when needed
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDatabase::class.java,
                    "todo_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}