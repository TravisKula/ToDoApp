package com.example.todoapp.views.ui

//import android.app.Application
import android.app.Application
import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Delete
import androidx.room.util.TableInfo
import com.example.todoapp.datamodels.Task
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.viewModelFactory
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
    var taskName by remember { mutableStateOf("") }  //variable (var-mutable) it's value can change. remember - store value across recompositions
    var dueDate by remember { mutableStateOf("") } //mutableStateOf - creates a state object that holds a value (makes variable observable and changeable)
    //initiallized with empty string
    

    // Expose the pending and completed tasks as StateFlows
    val isAscending by viewModel.isAscending.collectAsState()
    val pendingTasks by viewModel.pendingTasks.collectAsState(initial = emptyList())   //collectAsState listens to StateFlow and recomposes the UI whenever new data is emitted
    val completedTasks by viewModel.completedTasks.collectAsState(initial = emptyList())

    // Obtain the keyboard controller Dec21/24

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
                    //    .weight(3f)
                    .padding(2.dp)
                    .width(375.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

        }
          //  Spacer(modifier = Modifier.width(8.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()

        ) {
            DatePickerField(
                selectedDate = dueDate,
                onDateSelected = { dueDate = it },
                modifier = Modifier
                    //  .weight(1f)
                  //  .height(54.dp)
                    .padding(8.dp)
                    .width(150.dp)
            )

            // Toggle sort button (for ascending / descending list
            Button(onClick = { viewModel.toggleSortOrder() },
                modifier = Modifier
                    //  .fillMaxWidth()
                    .height(72.dp)
                    .width(150.dp)
                    .padding(8.dp)

            ) {
                Text(text = if (isAscending) "Descending" else "Ascending")
            }
        }

       // Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(                                     // button to Add TaskItem
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
                 //  .fillMaxWidth()
                    .height(72.dp)
                    .width(150.dp)
                    .padding(8.dp)

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

            item {                  //item function adds a single composable to LC (here a header)
                Text("Pending Tasks", style = MaterialTheme.typography.headlineMedium)
            }
            items(pendingTasks, key = { it.id }) { task ->    //renders a list of pendingTasks, provides tasks unique ID key
                TaskItem(                   //calling TaskItem function
                    task = task,
                    onDelete = { viewModel.deleteTask(task) },
                 //   onMarkComplete = { viewModel.markTaskAsCompleted(it) }, //Pass function to viewmodel
                    onMarkComplete = { viewModel.markTaskAsCompleted(task) },
                  //  onEdit = { viewModel.editTask(it) }
                    onEdit = { task -> viewModel.editTask(task)} ,

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
                    onEdit = { updatedTask -> viewModel.editTask(updatedTask) } ,


                )

            }

        }
    }
}



@Composable
fun TaskItem(                       //A Task which can be deleted, edited or marked as complete
    task: Task,                 // task (parameter) an OBJECT of type Task (data class - name, dueDate, isCompleted)
    onDelete: () -> Unit,       // a callbackfunction triggered when task is deleted -> Unit means it returns nothing
    onMarkComplete: (Task) -> Unit,  // Takes task as a parameter b/c requires info (isCompleted) from Task
    onEdit: (Task) -> Unit


    //Takes Task as a parameter because info (taskName, dueDate) are required to delete
    // these are callback functions because the are functions passed as arguments to another function
    //modifier: Modifier = Modifier

    ) {

    // state variables
    var isEditing by remember { mutableStateOf(false) }  //boolean that tracks where task is in Edit Mode - default to non-editing mode
   // var editedText by remember { mutableStateOf((task.name)) } //state variable to hold the current text of the task. Initialized with the name of the task
    var editedText by remember { mutableStateOf(TextFieldValue(text = task.name)) }  //TextField will initially display the name of the task b/c text property is initialized to task.name
    //   val updatedTask = remember(task) { task.copy(name = editedText) }

    var editedDueDate by remember { mutableStateOf(TextFieldValue(text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(task.dueDate))) }  // Dec 22/24

    val focusRequester = remember { FocusRequester() }  //FocusRequester for the TextField


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .height(180.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)

    ) {
        Column {  //TOP COLUMN for Task and date and Task converted to textfield for Editing

        Row(
            verticalAlignment = Alignment.CenterVertically // Align children to center vertically
        ) {
            Text(
                text = "Due Date:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(end = 0.dp) //Add a small space between Text and TextField
            )

            TextField(
                value = editedDueDate,
                onValueChange = { editedDueDate = it},
                enabled = isEditing,
                singleLine = true,
                modifier = Modifier
                    .width(IntrinsicSize.Min) // Shrink to fit the content
                    .offset(x = (-12).dp) // Shift TextField 12.dp to the left
                    //   .fillMaxWidth()
                  //  .padding(2.dp)
                    .focusRequester(focusRequester),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
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
                ),

            //    contentPadding = PaddingValues(0.dp)

            )
        }


            /*  // Original Due Date display in TaskItem - not editable (Text)
            Text(
                "Due: ${
                    SimpleDateFormat(   //includes leading zeros (shows in double digits)
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).format(task.dueDate)
                }",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

             */

            // Box where text is displayed // use box to allow for scrolling long messages
            Box(modifier = Modifier
                .height(60.dp) //fixed height for the task text
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()) //Allows scrolling if text overflows the height
            )
            {
                TextField(
                    value = editedText, // shows the current task name / TextFieldlValue
                    onValueChange = { editedText = it }, //Updates the editedText state when the user types
                   enabled = isEditing, //enable editing only when in editing mode
                    singleLine = false, //Allows multiline text
                    modifier = Modifier
                        .fillMaxWidth() //fills the card's width
                        .focusRequester(focusRequester), // Attach the FocusRequester
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

                if (isEditing) {   //isEditing becomes True after clicking the checkmark
                    // Save the edited task

                    val newDueDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(editedDueDate.text)

                 //   onEdit(task.copy(name = editedText))
                    onEdit(task.copy(name = editedText.text, dueDate = newDueDate)) //Dec 21
               //     viewModel.editTask(task.copy(name = editedText))


                    // if isEditing = true, it triggers onEdit with the updated task name


              //      onEdit(task.copy(name = editedDueDate.text))  //Dec 22/24

                    // when click the Pencil Icon, isEditing is still false, else block executes

                } else {   //clicking pencil executes else block


                    // Request focus and set cursor to end when entering editing mode
                    focusRequester.requestFocus() // places cursor in TextField
                    editedText = editedText.copy(selection = TextRange(editedText.text.length))  //Dec 21

                    editedDueDate = editedDueDate.copy(selection = TextRange(editedDueDate.text.length)) //Dec 22/24 make due date editable
                }
                // after else block (which put cursor in TextField),
                // isEditing = false changes to isEditing = True (which changes from pencil to checkmark)
                // then TextField becomes editable
                isEditing = !isEditing  //Toggles isEditing state

            }) {
                Icon(
                    imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit, //if in Editing mode, click check to save. pencil when not editing
                    contentDescription = if (isEditing) "Save" else "Edit",
                    modifier = Modifier.weight(1f)
                )
            }
                // Request focus automatically and set cursor to the end when entering editing mode
                LaunchedEffect(isEditing) {
                    if (isEditing) {
                        focusRequester.requestFocus()
                        editedText = editedText.copy(selection = TextRange(editedText.text.length))
                        editedDueDate = editedDueDate.copy(selection = TextRange(editedText.text.length))  //Dec 22/24
                    }
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
            // Use SimpleDateFormat for consistency
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            onDateSelected(formattedDate)
        },


    /*
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // Format the selected date

           val formattedDate = "${year}-${month + 1}-${dayOfMonth}"
            // this is the format: yyyy-MM-dd (not padded to 2 digits)
            // so is 2025-1-1


     //       val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            onDateSelected(formattedDate)  //better to be SimpleDateFormat??


        },

     */
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

