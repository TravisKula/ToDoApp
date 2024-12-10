package com.example.todoapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.datamodels.Task
import com.example.todoapp.datamodels.ToDoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

//ToDoViewModel
//Acts as a mediator between UI (TaskList) and TaskDao
//Contains business logic to fetch or update tasks and exposes these tasks as StateFlow or LiveData for the UI to observe.



class ToDoViewModel(application: Application) : AndroidViewModel(application) {
//    private val taskDao = ToDoDatabase.getDatabase(application).taskDao()
    private val taskDao = ToDoDatabase.getDatabase(application).taskDao() //changed Dec 8


    // Expose tasks as a StateFlow
//    private val _tasks = MutableStateFlow<List<Task>>(emptyList()) // Store the tasks in a MutableStateFlow
    //  val tasks: StateFlow<List<Task>> = _tasks //Expose it as a StateFlow to observe in UI

    // Use Room's Flow to automatically observe pending tasks
    val pendingtasks: StateFlow<List<Task>> = taskDao.getPendingTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    // Optional: Add a similar StateFlow for completed tasks
    val completedTasks: StateFlow<List<Task>> = taskDao.getCompletedTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    //stateIn converts the Flow into a StateFlow that the UI can easily observe.


    //Function to add a new task
    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.insert(task) //Inserting a task automatically triggers Flow updates
        }
    }

    //Function to mark a task as completed
    fun markTaskAsCompleted(taskId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.markAsCompleted(taskId) // Automatically triggers Flow updates
        }
    }
/*
    init {
        getPendingTasks()
    }


    //Function to get pending tasks
    fun getPendingTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            val tasksFromDb = taskDao.getPendingTasks() // Fetch tasks from the DB
       //     _tasks.value = tasksFromDb // Update LiveData or State to display tasks in the UI
        }
    }


*/




}