package com.example.todoapp.views.ui

//import android.app.Application
import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
//import androidx.compose.material3.DatePickerDialog
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
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.datamodels.Task
import com.example.todoapp.ui.theme.ToDoAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
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
  //  val tasks by viewModel.tasks.collectAsState() //Observe the StateFlow

    // Expose the pending and completed tasks as StateFlows
    val pendingTasks by viewModel.pendingtasks.collectAsState()
    val completedTasks by viewModel.completedTasks.collectAsState()

    Column(modifier = modifier.padding(16.dp)) { //use the modifier parameter here
        //  Input fields for task name and due date
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {

            // Task Name input
            TextField(
                value = taskName,
                onValueChange = { taskName = it },
                label = { Text("Task Name") },
                modifier = Modifier
                    .weight(2f)
                    .padding(8.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))


            DatePickerField(
                selectedDate = dueDate,
                onDateSelected = { dueDate = it },
                modifier = Modifier
                    .weight(2f)
                    .padding(8.dp)
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
                       // viewModel.addTask(task)
                        viewModel.addTask(task)
                        // Clear the input fields after adding the task
                        taskName = ""
                        dueDate = ""
                    }
                },
                modifier = Modifier
                  //  .padding(top = 16.dp)
                   .weight(1f)
            ) {
                Text("Add Task")

            }
            }

            Spacer(modifier = Modifier.height(16.dp))

        // Display tasks in a scrollable list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Pending Tasks
            items(pendingTasks) { task ->
                Text(text = "Pending: ${task.name}")
            }
            items(completedTasks) { task ->
                Text(text = "Completed: ${task.name}")
            }

           /*
            items(tasks) { task ->
                Text(
                    text = "${task.name} - Due: ${
                        SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        ).format(task.dueDate)}",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            */
        }
        }

    //Spacer(modifier = Modifier.height(60.dp))



}
@Composable
fun DatePickerField(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
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

    Button(
        onClick = { datePickerDialog.show() },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(if (selectedDate.isBlank()) "Select Due Date" else selectedDate)
    }

    /*
    OutlinedTextField(
        value = selectedDate,
        onValueChange = {}, // no direct editing by user
        label = { Text("Due Date") },
        readOnly = true,
        modifier = modifier
            .clickable { datePickerDialog.show() })

     */

}
/*
@Preview
@Composable
fun TaskListPreview() {
    ToDoAppTheme{
        TaskList(
            viewModel = ToDoViewModel(),
            tasks = listOf(
                Task(name = "task 1 clothes", dueDate = Date()),
                Task(name = "task 2 laundry", dueDate = Date())
            ),
            modifier = Modifier

        )
    }
}


 */