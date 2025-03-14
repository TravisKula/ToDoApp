package com.example.todoapp.views.ui

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.datamodels.Task
import com.example.todoapp.ui.theme.BackgroundGray
import com.example.todoapp.ui.theme.BigShoulders
import com.example.todoapp.ui.theme.BigShouldersBold
import com.example.todoapp.ui.theme.BigShouldersExtraBold
import com.example.todoapp.ui.theme.CharcoalGray
import com.example.todoapp.ui.theme.Cinnamon
import com.example.todoapp.ui.theme.Copper
import com.example.todoapp.ui.theme.CreamyWhite
import com.example.todoapp.ui.theme.DarkBrown
import com.example.todoapp.ui.theme.DarkCacoa
import com.example.todoapp.ui.theme.DeepCaramel
import com.example.todoapp.ui.theme.DeepCharcoal
import com.example.todoapp.ui.theme.DeepCoral
import com.example.todoapp.ui.theme.DeepNavy
import com.example.todoapp.ui.theme.DeepPlum
import com.example.todoapp.ui.theme.DeepRed
import com.example.todoapp.ui.theme.DullBrown
import com.example.todoapp.ui.theme.DustyCharcoal
import com.example.todoapp.ui.theme.DustyPurple
import com.example.todoapp.ui.theme.EarthyTeracotta
import com.example.todoapp.ui.theme.JadeGreen
import com.example.todoapp.ui.theme.Lavendar
import com.example.todoapp.ui.theme.LightBeige
import com.example.todoapp.ui.theme.LightBrown
import com.example.todoapp.ui.theme.LighterGray
import com.example.todoapp.ui.theme.MaroonButton
import com.example.todoapp.ui.theme.MediumBrown
import com.example.todoapp.ui.theme.MidnightBlue
import com.example.todoapp.ui.theme.MintGreen
import com.example.todoapp.ui.theme.MutedClay
import com.example.todoapp.ui.theme.MutedOrange
import com.example.todoapp.ui.theme.MutedTaupe
import com.example.todoapp.ui.theme.MutedTeal
import com.example.todoapp.ui.theme.NeutralPink
import com.example.todoapp.ui.theme.NotoSerif
import com.example.todoapp.ui.theme.NotoSerifItalic
import com.example.todoapp.ui.theme.PastelBlue
import com.example.todoapp.ui.theme.PastelPink
import com.example.todoapp.ui.theme.PastelYellow
import com.example.todoapp.ui.theme.Purple80
import com.example.todoapp.ui.theme.RustyCaramel
import com.example.todoapp.ui.theme.RustyOrange
import com.example.todoapp.ui.theme.SoftBeige
import com.example.todoapp.ui.theme.SoftCoral
import com.example.todoapp.ui.theme.StoneGray
import com.example.todoapp.ui.theme.VeryLightGray
import com.example.todoapp.ui.theme.WarmBeige
import com.example.todoapp.ui.theme.WarmCacao
import com.example.todoapp.ui.theme.WarmGray
import com.example.todoapp.ui.theme.WarmOrange
import com.example.todoapp.ui.theme.WarmTaupe
import com.example.todoapp.viewmodels.ToDoViewModel
import java.util.Calendar
import java.util.Date
import java.util.Locale


// UI with scrollable list for tasks

@Composable
fun TaskList(viewModel: ToDoViewModel, modifier: Modifier = Modifier) {

    // State variables for task name and due date
    var taskName by remember { mutableStateOf("") }  // variable (var-mutable) it's value can change. remember - store value across recompositions
    var dueDate by remember { mutableStateOf("") } // mutableStateOf - creates a state object that holds a value (makes variable observable and changeable)
    // initiallized with empty string


    // Expose the pending and completed tasks as StateFlows
    val isAscending by viewModel.isAscending.collectAsState()
    val pendingTasks by viewModel.pendingTasks.collectAsState(initial = emptyList())   // collectAsState listens to StateFlow and recomposes the UI whenever new data is emitted
    val completedTasks by viewModel.completedTasks.collectAsState(initial = emptyList())

    // Obtain the keyboard controller Dec 21/24

    Column(modifier = modifier
        .fillMaxSize() // Ensures it covers full screen
        .border(6.dp, color = Color.Black,
            shape = RoundedCornerShape(24.dp) // Rounds the interior corners
    )
        .clip(RoundedCornerShape(24.dp)) // Ensures content inside follows rounded shape

            .background(
            brush = Brush.linearGradient(
            colors = listOf(Color(0xFFFFF59D), Color(0xFFD1C4E9)), // Pastel Yellow → Lavender
        start = Offset(0f, 0f), // Top-left
        end = Offset(1000f, 1000f) // Bottom-right (adjust based on screen size)
    )
            )
        .padding(16.dp)

    ) { //use the modifier parameter here
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
                label = { Text("Input Task") },
                modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth()
                 //   .width(375.dp)
                    .height(65.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(width = 1.dp, color = Color.Black),

              //  colors = TextFieldDefaults.colors(PastelBlue)

                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black, // Text color when focused
                    unfocusedTextColor = Color.Black, // Text color when unfocused
                    focusedContainerColor = PastelBlue, // Background when focused
                    unfocusedContainerColor = PastelBlue, // Background when unfocused
                    disabledContainerColor = PastelBlue.copy(alpha = 0.5f), // Background when disabled
                    focusedIndicatorColor = Color.Transparent, // Remove underline
                    unfocusedIndicatorColor = Color.Transparent, // Remove underline
                    disabledIndicatorColor = Color.Transparent // Remove underline
                )
            )
        }
          Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp) // Add padding around the card
                .shadow(12.dp, RoundedCornerShape(12.dp)), // Soft shadow effect
            shape = RoundedCornerShape(12.dp), // Rounded corners
            border = BorderStroke(2.dp, Color.Gray), // Border around the card
            elevation = CardDefaults.cardElevation(defaultElevation = 22.dp) // Elevation effect
        ) {

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()

            ) {
                DatePickerField(
                    selectedDate = dueDate,
                    onDateSelected = { dueDate = it },
                    modifier = Modifier
                )

                // Toggle sort button (for ascending / descending list
                Button(
                    onClick = { viewModel.toggleSortOrder() },
                    modifier = Modifier
                        //  .fillMaxWidth()
                        .height(100.dp)
                        .width(80.dp)
                        .padding(2.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DustyCharcoal)
                ) {
                    Text(
                        text = if (isAscending) "Sort" else "Sort",
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }

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
                    modifier = Modifier

                        .height(100.dp)
                        .width(150.dp)
                        .padding(4.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkCacoa)

                ) {
                    Text(
                        "Add Task",
                        textAlign = TextAlign.Center,

                        )
                }

            }


            }  // End of Button Row

        Spacer(modifier = Modifier.height(12.dp))

        // Display tasks in a scrollable list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // Pending Tasks
            item { //item function adds a single composable to LC (here a header)
                Text(
                    "Pending Tasks",
                    style = TextStyle(
                        fontFamily = NotoSerif,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = DarkCacoa,
                    modifier = Modifier.padding(bottom = 4.dp) // Adds space between text & line
                )

                Divider(
                    color = DarkCacoa, // Matches text color
                    thickness = 2.dp,
                    modifier = Modifier.padding(horizontal = 160.dp) // Adds left/right spacing
                )


            }
            items(
                pendingTasks,
                key = { it.id }) { task -> //renders a list of pendingTasks, provides tasks unique ID key
                TaskItem(
                    //calling TaskItem function
                    task = task,
                    onDelete = { viewModel.deleteTask(task) },
                    onMarkComplete = { viewModel.markTaskAsCompleted(task) },
                    onEdit = { task -> viewModel.editTask(task) },
                //    cardColor = WarmOrange // Pending task color
                    cardColor = PastelYellow
                    )
            }


            // Completed Tasks
            item {
                Text(
                    "Completed Tasks",
                    modifier = Modifier
                        .padding(top = 28.dp, bottom = 4.dp),
                    style = TextStyle(
                        fontFamily = NotoSerif,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold),
                    color = DarkCacoa,
                )
                Divider(
                    color = DarkCacoa, // Matches text color
                    thickness = 2.dp,
                    modifier = Modifier.padding(horizontal = 160.dp) // Adds left/right spacing
                )
            }

            items(completedTasks, key = { it.id }) { task ->
                TaskItem(
                    task = task,
                    onDelete = { viewModel.deleteTask(task) },
                    onMarkComplete = { viewModel.markTaskAsCompleted(task) },
                    onEdit = { updatedTask -> viewModel.editTask(updatedTask) },
                    cardColor = PastelPink

                    )

            }

        }
    } // End of Column
}


@Composable
fun TaskItem( // A Task which can be deleted, edited or marked as complete
    task: Task, // task (parameter) an OBJECT of type Task (data class - name, dueDate, isCompleted)
    onDelete: () -> Unit,       // a callbackfunction triggered when task is deleted
    onMarkComplete: (Task) -> Unit,  // Takes task as a parameter b/c requires info (isCompleted) from Task
    onEdit: (Task) -> Unit,
    cardColor: Color // Allows color customization

) {

    // State variables
    var isEditing by remember { mutableStateOf(false) }  // Boolean that tracks where task is in Edit Mode - default to non-editing mode
    // var editedText by remember { mutableStateOf((task.name)) } //state variable to hold the current text of the task. Initialized with the name of the task
    var editedText by remember { mutableStateOf(TextFieldValue(text = task.name)) }  // TextField will initially display the name of the task b/c text property is initialized to task.name
    //   val updatedTask = remember(task) { task.copy(name = editedText) }

    var editedDueDate by remember {
        mutableStateOf(
            TextFieldValue(
                text = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(task.dueDate)
            )
        )
    }

    val focusRequester = remember { FocusRequester() }  //FocusRequester for the TextField

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .height(180.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor), // Use the passed color
        shape = RoundedCornerShape(28.dp)
    ) {
        Column {  //TOP COLUMN for Task and date and Task converted to textfield for Editing

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically, // Align children to center vertically
                horizontalArrangement = Arrangement.Center
            ) {


                Text(
                    text = "Due Date:",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = BigShouldersBold,
                        fontSize = 16.sp,
                     //   fontWeight = FontWeight.Thin,
                    color = DeepCharcoal,
                    textAlign = TextAlign.Center
                        //Add a small space between Text and TextField
                )
                )

                TextField(
                    value = editedDueDate,
                    onValueChange = { editedDueDate = it },
                    enabled = isEditing,
                    singleLine = true,
                    modifier = Modifier
                        .width(IntrinsicSize.Min) // Shrink to fit the content
                        .offset(x = (-12).dp) // Shift TextField 12.dp to the left
                        //   .fillMaxWidth()
                        //  .padding(2.dp)
                        .focusRequester(focusRequester),
                    textStyle =  TextStyle(
                        fontFamily = BigShouldersBold,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Thin,

                  //  textStyle = MaterialTheme.typography.bodyMedium.copy(
                    //    color = MaterialTheme.colorScheme.onSurface
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
                )
            }

            // Box where text is displayed // use box to allow for scrolling long messages
            Box(
                modifier = Modifier
                    .height(70.dp) //fixed height for the task text
                    .padding(bottom = 6.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()) // Allows scrolling if text overflows the height

            )
            {
                TextField(
                    value = editedText, // Shows the current task name / TextFieldlValue
                    onValueChange = {
                        editedText = it
                    }, //Updates the editedText state when the user types
                    enabled = isEditing, // Enables editing only when in editing mode
                    singleLine = false, // Allows multiline text
                    modifier = Modifier
                        .fillMaxWidth() // Fills the card's width
                        .focusRequester(focusRequester), // Attaches the FocusRequester

                    textStyle =  TextStyle(
                        fontFamily = NotoSerifItalic,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Thin,
                        color = DeepCharcoal
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


            Spacer(modifier = Modifier.width(16.dp))

            Row {    // Bottom row for Icons
                //Checkbox to mark task as completed
                // Edit Button
                IconButton(onClick = {
                    //IconButton to toggle between Edit(pencil) and Save(check)

                    if (isEditing) {   //isEditing becomes True after clicking the checkmark
                        // Save the edited task

                        val newDueDate = SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        ).parse(editedDueDate.text)

                        //   onEdit(task.copy(name = editedText))
                        onEdit(task.copy(name = editedText.text, dueDate = newDueDate)) //Dec 21

                    } else {   //clicking pencil executes else block


                        // Request focus and set cursor to end when entering editing mode
                        focusRequester.requestFocus() // places cursor in TextField
                        editedText =
                            editedText.copy(selection = TextRange(editedText.text.length))  //Dec 21

                        editedDueDate =
                            editedDueDate.copy(selection = TextRange(editedDueDate.text.length)) //Dec 22/24 make due date editable
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

                // Checkbox to mark task as complete or incomplete

                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onMarkComplete(task) },
                    modifier = Modifier.padding(4.dp)
                )


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
            val formattedDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            onDateSelected(formattedDate)
        },

        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Button(
        onClick = { datePickerDialog.show() },
        modifier = modifier
           // .fillMaxWidth()
            .height(100.dp)
            .width(150.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(22.dp),
        colors = ButtonDefaults.buttonColors(containerColor = DarkCacoa)



    ) {
        Text(
            if (selectedDate.isBlank()) "Due Date" else selectedDate

        )
    }
}


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

