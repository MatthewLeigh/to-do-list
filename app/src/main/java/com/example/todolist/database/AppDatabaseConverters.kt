package com.example.todolist.database

import androidx.room.TypeConverter
import com.example.todolist.enums.Frequency
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AppDatabaseConverters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalDateToString(date: LocalDate?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun toStringFromLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it, formatter) }
    }

    @TypeConverter
    fun fromFrequencyToString(frequency: Frequency): String {
        return frequency.name
    }

    @TypeConverter
    fun toStringFromFrequency(value: String): Frequency {
        return Frequency.valueOf(value)
    }
}
