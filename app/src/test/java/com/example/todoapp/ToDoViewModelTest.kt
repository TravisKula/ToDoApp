package com.example.todoapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.todoapp.datamodels.Task
import com.example.todoapp.datamodels.TaskDao
import com.example.todoapp.viewmodels.ToDoViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class ToDoViewModelTest {

    // Executes LiveData and Flow Instantly
    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Mock the DAO and Application
    private val taskDao: TaskDao = mock()
    private val application: Application = mock()

    // Create ViewModel instance, passing the mock Application
    private val viewModel = ToDoViewModel(application)

    @Test
    fun `toggleSortOrder should change is Ascending value`() {
        // initial state should be true
        assertEquals(true, viewModel.isAscending.value)

        // Toggle the value
        viewModel.toggleSortOrder()

        // Check if it changed
        assertEquals(false, viewModel.isAscending.value)
    }

    @Test
    fun `addTask should call insert on TaskDao`() = runTest {
        val task: Task = mock() // Mock Task object

        viewModel.addTask(task)

        // Verify if insert() was called once
        verify(taskDao, times(1)).insert(task)
    }

}
