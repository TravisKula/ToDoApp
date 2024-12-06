package com.example.todoapp

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.ui.theme.ToDoAppTheme
import com.example.todoapp.viewmodels.ToDoViewModel
import com.example.todoapp.views.ui.TaskList

class MainActivity : ComponentActivity() {

    // Initialize the ViewModel
    private val viewModel: ToDoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                // Scaffold is the main layout for the app UI
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // TaskList composable that handles displaying and adding tasks
                    TaskList(viewModel = viewModel, modifier = Modifier.padding(innerPadding))

                }
            }
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ToDoAppTheme {
        // Preview the TaskList composable
        TaskList(viewModel = ToDoViewModel())
    }
}

 */