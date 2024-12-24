package com.example.todoapp.datamodels

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale



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


/*
private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

@TypeConverter
fun fromDate(date: LocalDate?): String? {
    return date?.format(dateFormat)
}

@TypeConverter
fun toDate(dateString: String?): LocalDate? {
    return dateString?.let { LocalDate.parse(it, dateFormat) }
}
*/