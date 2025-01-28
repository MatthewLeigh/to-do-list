package com.example.todolist

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalDateToString(date: LocalDate?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun toStringToLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it, formatter) }
    }

    @TypeConverter
    fun fromFrequencyToString(frequency: Frequency): String {
        return frequency.name
    }

    @TypeConverter
    fun fromStringToFrequency(value: String): Frequency {
        return Frequency.valueOf(value)
    }
}
