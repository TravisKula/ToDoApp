package com.example.todoapp.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, // Unique ID of each task
    val name: String, // These variables are columns in the Database
    val dueDate: Date,
    val isCompleted: Boolean = false

)
