package com.example.todoapp.datamodels

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

//Dao class communicates directly with db
// created as an interface b/c Dao methods are implemented by room at runtime
//These are the methods for interacting with the DB


@Dao  //marks this Interface as a Data Access Object (DAO) for Room
interface TaskDao {

    @Insert
    suspend fun insert(task: Task)  //suspend allows functions to be called with coroutine for efficiency

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

        // these are the room queries

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY dueDate ASC")
    fun getPendingTasks(): Flow<List<Task>> //Return a FLOW of information from DB for real-time updates

    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY dueDate ASC")
    fun getCompletedTasks(): Flow<List<Task>> //Make this is Flow too

    @Query("UPDATE tasks SET isCompleted = 1 WHERE id = :taskId")
    suspend fun markAsCompleted(taskId: Long)

}