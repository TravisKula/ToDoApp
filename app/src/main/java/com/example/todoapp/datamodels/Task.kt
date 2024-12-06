package com.example.todoapp.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val dueDate: Date,
    val isCompleted: Boolean = false

)
