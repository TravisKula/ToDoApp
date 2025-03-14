package com.example.todoapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.datamodels.Task
import com.example.todoapp.datamodels.ToDoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


// Bridge between UI (TaskList) and TaskDao
// Contains business logic to fetch or update tasks and exposes these tasks as StateFlow or LiveData for the UI to observe.

class ToDoViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDao = ToDoDatabase.getDatabase(application).taskDao()

    // State to toggle sort order(ascending = true, descending = false)
    private val _isAscending = MutableStateFlow(true)
    val isAscending: StateFlow<Boolean> = _isAscending


    // Use Room's Flow to automatically observe pending tasks based on current sort order
    val pendingTasks: StateFlow<List<Task>> = _isAscending.flatMapLatest { ascending ->
        if (ascending) taskDao.getPendingTasksAsc() else taskDao.getPendingTasksDesc()
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    // Observing completed tasks based on the current sort order
    val completedTasks: StateFlow<List<Task>> = _isAscending.flatMapLatest { ascending ->
        if (ascending) taskDao.getCompletedTasksAsc() else taskDao.getCompletedTasksDesc()
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun toggleSortOrder() {
        _isAscending.value = !_isAscending.value // Flips between true (ascending) and false (descending)
    }

    //Function to add a new task
    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.insert(task) //Inserting a task automatically triggers Flow updates
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.delete(task) //delete task
        }
    }

    fun editTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.update(task) //edit task using 'update' function in DAO
        }
    }

    //Function to mark a task as completed
    fun markTaskAsCompleted(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.markAsCompleted(task.id) // Use the task ID directly to mark as completed



        }
    }


}