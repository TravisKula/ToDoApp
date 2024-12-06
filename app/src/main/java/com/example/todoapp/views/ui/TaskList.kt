package com.example.todoapp.views.ui

import android.app.Application
import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.todoapp.viewmodels.ToDoViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.datamodels.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar
import java.util.Date
import java.util.Locale


//UI with scrollable list for tasks
@Composable
fun TaskList(viewModel: ToDoViewModel, modifier: Modifier = Modifier) {
    //states for task name and due date
    var taskName by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }

    //collect tasks from viewModel
    val tasks by viewModel.tasks.collectAsState() //Observe the StateFlow

    Column(modifier = Modifier.padding(16.dp)) {
        //  Input fields for task name and due date
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
               // .padding(8.dp)
                .fillMaxWidth()
        ) {

            // Task Name input
            TextField(
                value = taskName,
                onValueChange = { taskName = it },
                label = { Text("Task Name") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))


            DatePickerField(
                selectedDate = dueDate,
                onDateSelected = { dueDate = it }
            )

            // Add Task Button
            Button(
                onClick = {
                    if (taskName.isNotBlank() && dueDate.isNotBlank()) {
                        //Parse the due date into a Date Object
                        val parsedDate =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dueDate)

                        // Create a new Task and add it to the ViewModel
                        val task = Task(name = taskName, dueDate = parsedDate ?: Date())
                        viewModel.addTask(task)
                        // Clear the input fields after adding the task
                        taskName = ""
                        dueDate = ""
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Add Task")

            }


            // Display tasks in a scrollable list

            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)) {
                items(tasks) { task ->
                    Text(
                        text = "${task.name} - Due: ${
                            SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).format(task.dueDate)
                        }",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }

}
@Composable
fun DatePickerField(
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // Format the selected date
            val formattedDate = "${year}-${month + 1}-${dayOfMonth}"
            onDateSelected(formattedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    OutlinedTextField(
        value = selectedDate,
        onValueChange = {}, // no direct editing by user
        label = { Text("Due Date") },
        readOnly = true,
        modifier = Modifier.clickable { datePickerDialog.show() }
    )
}
/*
@Composable
fun previewToDoViewModel(): ToDoViewModel {
    // Mock ViewModel for preview
    val viewModel = ToDoViewModel(Application())
    val exampleTasks = listOf(
        Task(name = "Buy groceries", dueDate = Date()),
        Task(name = "Walk the dog", dueDate = Date())
    )
    // Add mock tasks to the ViewModel for preview purposes
    exampleTasks.forEach { viewModel.addTask(it) }
    return viewModel
}

@Preview(showBackground = true)
@Composable
fun TaskListPreview() {
    // Use the mock ViewModel for the preview
    TaskList(viewModel = previewToDoViewModel())
}

*/

/*
@Preview(showBackground = true)
@Composable
fun taskListPreview() {
    val dummyTasks = listOf(
        Task(name = "Buy groceries", dueDate = Date()),
        Task(name = "Prepare meeting notes", dueDate = Date())
    )
    TaskList(mockTasks = dummyTasks)
}

 */