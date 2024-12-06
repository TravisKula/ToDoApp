package com.example.todoapp.datamodels

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

//Dao class communicates with db
// created as an interface b/c Dao methods are implemented by room at runtime
//These are the methods for interacting with the DB


@Dao  //marks this Interface as a Data Access Object (DAO) for Room
interface TaskDao {

    @Insert
    suspend fun insert(task: Task)  //suspend allows functions to be called with coroutine for efficiency

    @Update
    suspend fun update(task: Task)

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY dueDate ASC")
    fun getPendingTasks(): List<Task>

    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY dueDate ASC")
    suspend fun getCompletedTasks(): List<Task>

    @Query("UPDATE tasks SET isCompleted = 1 WHERE id = :taskId")
    suspend fun markAsCompleted(taskId: Long)

}