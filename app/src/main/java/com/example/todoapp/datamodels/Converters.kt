package com.example.todoapp.datamodels

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

// Class to convert dates to strings and strings to dates for storage in Room Database

class Converters {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @TypeConverter
    fun fromDate(date: Date?): String? {
        return date?.let { dateFormat.format(it) }
    }

    @TypeConverter
    fun toDate(dateString: String?): Date? {
        return dateString?.let { dateFormat.parse(it) }
    }

}


