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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel acts as a bridge between UI (TaskList) and TaskDao.
 * It handles business logic, fetches/updates tasks, and exposes them as StateFlow.
 */
class ToDoViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDao = ToDoDatabase.getDatabase(application).taskDao()

    // StateFlow to toggle sort order (true = ascending, false = descending)
    private val _isAscending = MutableStateFlow(true)
    val isAscending: StateFlow<Boolean> = _isAscending

    // Uses Room's Flow to automatically observe pending tasks based on current sort order
    val pendingTasks: StateFlow<List<Task>> = _isAscending.flatMapLatest { ascending ->
        if (ascending) taskDao.getPendingTasksAsc() else taskDao.getPendingTasksDesc()
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Fetches completed tasks as StateFlow, observing changes in sort order
    val completedTasks: StateFlow<List<Task>> = _isAscending.flatMapLatest { ascending ->
        if (ascending) taskDao.getCompletedTasksAsc() else taskDao.getCompletedTasksDesc()
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun toggleSortOrder() {
        _isAscending.value =
            !_isAscending.value
    }

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.insert(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.delete(task)
        }
    }

    fun editTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.update(task)
        }
    }

    fun markTaskAsCompleted(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.markAsCompleted(task.id)
        }
    }
}
