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
    val pendingtasks: StateFlow<List<Task>> = taskDao.getPendingTasks()  //taskDao = provides the Flow to be converted
        .stateIn(                               //stateIn converts FLOW from room to StateFlow (LiveData)
            scope = viewModelScope,
         //   started = SharingStarted.WhileSubscribed(5000),
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    // Optional: Add a similar StateFlow for completed tasks
    val completedTasks: StateFlow<List<Task>> = taskDao.getCompletedTasks()
        .stateIn(
            scope = viewModelScope,
          //  started = SharingStarted.WhileSubscribed(5000),
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    //stateIn converts the Flow into a StateFlow that the UI can easily observe.


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

    //Function to mark a task as completed 1

    fun markTaskAsCompleted(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.markAsCompleted(task.id) // Use the task ID directly to mark as completed
            // markAsCompleted is a Room Query
           // delay(50) //Ensure room updates have time to propogate Dec 16

        }
    }


    /*
        //this one works also bas still random ui issue
    fun markTaskAsCompleted(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedTask = task.copy(isCompleted = true)
            taskDao.update(updatedTask)
            delay(100) // Ensure database update completes before UI recomposes
        }
    }
*/

    /*

    fun markTaskAsCompleted(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedTask = task.copy(isCompleted = true)
            taskDao.update(updatedTask)
            Log.d("ToDoViewModel", "Task marked completed: ${updatedTask.id}, ${updatedTask.dueDate}")
        }
    }

*/


    /*
        fun markTaskAsCompleted(task: Task) {
            viewModelScope.launch(Dispatchers.IO) {
                taskDao.update(task.copy(isCompleted = true)) // Update the task in the database
            }
        }

    */

/*
    fun markTaskAsCompleted(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            // Toggle isCompleted and update the task
            taskDao.update(task.copy(isCompleted = !task.isCompleted))
        }
    }


*/




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