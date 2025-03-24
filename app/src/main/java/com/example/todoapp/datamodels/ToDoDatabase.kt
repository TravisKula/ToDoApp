package com.example.todoapp.datamodels

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Room Database class for storing tasks.
 * Uses a singleton pattern to ensure only one instance of the database exists.
 */
@Database(entities = [Task::class], version = 1)
@TypeConverters(Converters::class) // Registers converters for Data Handling
abstract class ToDoDatabase : RoomDatabase() {

    /** Provides Access to DAO functions. */
    abstract fun taskDao(): TaskDao

    companion object {


        /**
         * Volatile ensures that writes to this field are immediately visible to other threads.
         * This prevents multiple instances from being created in a multi-threaded environment.
         */
        @Volatile
        private var INSTANCE: ToDoDatabase? = null
        /**
         * Returns the singleton instance of the database.
         * If an instance does not exist, it creates one using Room.
         */
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
