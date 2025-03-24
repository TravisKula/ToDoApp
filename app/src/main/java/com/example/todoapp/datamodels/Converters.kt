package com.example.todoapp.datamodels

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Converts between Date and String for Room database storage.
 */

class Converters {

    // Date format used for storing and retrieving dates from the database
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


    /**
     * Converts a [Date] to a [String] format for database storage.
     * Returns `null` if the input date is `null`.
     */
    @TypeConverter
    fun fromDate(date: Date?): String? {
        return date?.let { dateFormat.format(it) }
    }

    /**
     * Converts a [String] date back into a [Date] object.
     * Returns `null` if the input string is `null` or invalid.
     */
    @TypeConverter
    fun toDate(dateString: String?): Date? {
        return dateString?.let { dateFormat.parse(it) }
    }

}


