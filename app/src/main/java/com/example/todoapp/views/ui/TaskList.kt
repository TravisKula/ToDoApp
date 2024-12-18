package com.example.todoapp.views.ui

//import android.app.Application
import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.runtime.key
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Delete
import androidx.room.util.TableInfo
import com.example.todoapp.datamodels.Task
import androidx.compose.ui.platform.LocalFocusManager
import com.example.todoapp.ui.theme.ToDoAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import java.time.format.TextStyle
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
    val pendingTasks by viewModel.pendingtasks.collectAsState()   //collectAsState listens to StateFlow and recomposes the UI whenever new data is emitted
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
                    .weight(3f)
                    .padding(2.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))


            DatePickerField(
                selectedDate = dueDate,
                onDateSelected = { dueDate = it },
                modifier = Modifier.weight(1f)
                    .padding(2.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
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
                    .fillMaxWidth()
                    .height(54.dp)
                //    .padding(24.dp)

            ) {
                Text("Add")

            }

        }

        //end column
        // Add Task Button

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

            item {
                Text("Pending Tasks", style = MaterialTheme.typography.headlineMedium)
            }
            items(pendingTasks, key = { it.id }) { task ->
                TaskItem(
                    task = task,
                    onDelete = { viewModel.deleteTask(task) },
                 //   onMarkComplete = { viewModel.markTaskAsCompleted(it) }, //Pass function to viewmodel
                    onMarkComplete = { viewModel.markTaskAsCompleted(task) },
                  //  onEdit = { viewModel.editTask(it) }
                    onEdit = { viewModel.editTask(task)}
                )
            }

            // Completed Tasks

            item {
                Text("Completed Tasks", style = MaterialTheme.typography.headlineMedium)
            }

            items(completedTasks, key = { it.id  }) { task ->
                TaskItem(
                    task = task,
                    onDelete = { viewModel.deleteTask(task) },
                    onMarkComplete = { viewModel.markTaskAsCompleted(task) },
                    onEdit = { viewModel.editTask(task) }

                )

            }

        }
    }
}



@Composable
fun TaskItem(                       //A Task which can be deleted, edited or marked as complete
    task: Task,                 // task (parameter) an OBJECT of type Task (data class - name, dueDate, isCompleted)
    onDelete: () -> Unit,       // a callbackfunction triggered when task is deleted -> Unit means it returns nothing
    onMarkComplete: (Task) -> Unit,  // Tasks task as a parameter b/c requires info (isCompleted) from Task
    onEdit: (Task) -> Unit,  //Takes Task as a parameter because info (taskName, dueDate) are required to delete
    // these are callback functions because the are functions passed as arguments to another function
    //modifier: Modifier = Modifier

    ) {

    // state variables
    var isEditing by remember { mutableStateOf(false) }  //boolean that tracks where task is in Edit Mode - default to non-editing mode
    var editedText by remember { mutableStateOf((task.name)) } //state variable to hold the current text of the task. Initialized with the name of the task
 //   val updatedTask = remember(task) { task.copy(name = editedText) }

   // val focusRequester = remember { FocusRequester() }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .height(180.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)

    ) {
        Column {  //TOP COLUMN for Task and date and Task converted to textfield for Editing

            Text(
                "Due: ${
                    SimpleDateFormat(
                        "EEE, MMM dd, YYYY",
                        Locale.getDefault()
                    ).format(task.dueDate)
                }",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Box where text is displayed
            Box(modifier = Modifier
                .height(60.dp) //fixed height for the task text
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()) //Allows scrolling if text overflows the height
            )
            {
                TextField(
                    value = editedText, // shows the current task name (editText)
                    onValueChange = { editedText = it }, //Updates the editedText state when the user types
              //     enabled = isEditing, //Disable editing unless in editing mode
                    singleLine = false, //Allows multiline text
                    modifier = Modifier
                        .fillMaxWidth(), //fills the card's width
                      //  .focusRequester(focusRequester), // Attach the FocusRequester
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),

                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
            }


            Spacer(modifier = Modifier.width(8.dp))

            Row {    // Bottom row for Icons
                //Checkbox to mark task as completed
            // Edit Button
            IconButton(onClick = {
                //IconButton to toggle between Edit(pencil) and Save(check)

                if (isEditing) {
                    // Save the edited task
                    onEdit(task.copy(name = editedText))
                    // if isEditing = true, it triggers onEdit with the updated task name

                }

                isEditing = !isEditing  //Toggles isEditing state

            }) {
                Icon(
                    imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit, //if in Editing mode, click check to save. pencil when not editing
                    contentDescription = if (isEditing) "Save" else "Edit",
                    modifier = Modifier.weight(1f)
                )
            }
                    // Checkbox to mark task as complete or incomplete

                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onMarkComplete(task) },
                    modifier = Modifier.padding(4.dp)
                )




                /*

                Checkbox(
                    checked = task.isCompleted,  //reflects task's isCompleted status
                    onCheckedChange = { onMarkComplete(task.copy(isCompleted = it)) }, // Copy and update task
                    //onCheckedChange defines what happens when user checks/unchecks box
                    // it (boolean value)  represents state of box
                    //true if user checks and false if user unchecks (it = true)
                    //task = current task being represented (data class)
                    //.copy creates a new Task object with updated value for isCompleted
                    //
                    modifier = Modifier.padding(4.dp)
                )

            */
                        //Delete Button Icon
                        IconButton(onClick = onDelete) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Task",
                                tint = Color.Red
                            )
                        }
                }
        }

    }
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
            .padding(2.dp)
            .height(54.dp)
    ) {
        Text(if (selectedDate.isBlank()) "Due Date" else selectedDate)
    }}





/*
@Composable
@Preview
fun PreviewTaskItem() {
    val task = Task("Sample Task", Date())
    TaskItem(
        task = task,
        onDelete = {},
        onMarkComplete = {},
        onEdit = {}
    )
}
*/

