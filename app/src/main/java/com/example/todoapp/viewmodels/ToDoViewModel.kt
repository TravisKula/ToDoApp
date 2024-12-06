package com.example.todoapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.datamodels.Task
import com.example.todoapp.datamodels.ToDoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ToDoViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDao = ToDoDatabase.getDatabase(application).taskDao()

    //Function to add a new task
    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.insert(task)
        }
    }

    // Expose tasks as a StateFlow
    private val _tasks = MutableStateFlow<List<Task>>(emptyList()) // Store the tasks in a MutableStateFlow
    val tasks: StateFlow<List<Task>> = _tasks //Expose it as a StateFlow to observe in UI

    init {
        getPendingTasks()
    }

    //Function to get pending tasks
    fun getPendingTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            val tasksFromDb = taskDao.getPendingTasks() // Fetch tasks from the DB
            _tasks.value = tasksFromDb // Update LiveData or State to display tasks in the UI
        }
    }

    //Function to mark a task as completed
    fun markTaskAsCompleted(taskId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.markAsCompleted(taskId)
        }
    }


}