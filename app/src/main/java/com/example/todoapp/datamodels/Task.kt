package com.example.todoapp.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Represents a task entity stored in the Room database.
 */
@Entity(tableName = "tasks")
data class Task(

    /** Unique ID of each task (auto-generated). */
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val dueDate: Date,
    val isCompleted: Boolean = false
)
