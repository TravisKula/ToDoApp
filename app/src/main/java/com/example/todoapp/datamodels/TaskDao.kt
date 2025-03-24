package com.example.todoapp.datamodels

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for performing CRUD operations on the Task entity.
 * Provides methods to insert, update, delete, and query tasks from the Room database.
 */
@Dao
interface TaskDao {

    @Insert
    suspend fun insert(task: Task)  // Suspend allows functions to be called within coroutines for efficiency

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    // These are the Room Queries

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY dueDate ASC")
    fun getPendingTasksAsc(): Flow<List<Task>> // Returns a FLOW of information from DB for real-time updates

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY dueDate DESC")
    fun getPendingTasksDesc(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY dueDate ASC")
    fun getCompletedTasksAsc(): Flow<List<Task>> // Makes this is Flow (real-time updates to the UI)

    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY dueDate DESC")
    fun getCompletedTasksDesc(): Flow<List<Task>>

    @Query("UPDATE tasks SET isCompleted = 1 WHERE id = :taskId")
    suspend fun markAsCompleted(taskId: Long)
}