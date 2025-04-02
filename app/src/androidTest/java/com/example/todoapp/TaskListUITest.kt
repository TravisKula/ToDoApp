package com.example.todoapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class TaskListUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun addTask_displaysInList() {
        // Enter a task in the input field
        composeTestRule.onNodeWithText("Input Task").performTextInput("Buy groceries")

        // Click the Add Task button
        composeTestRule.onNodeWithText("Add Task").performClick()

        // Verify the new task appears in the list
        composeTestRule.onNodeWithText("Buy groceries").assertIsDisplayed()
    }

}