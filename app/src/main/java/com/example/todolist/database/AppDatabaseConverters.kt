package com.example.todolist.database

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AppDatabaseConverters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromLocalDateTimeToString(dateTime: LocalDateTime): String {
        return dateTime.format(formatter)
    }

    @TypeConverter
    fun toStringFromLocalDateTime(dateTimeString: String): LocalDateTime {
        return dateTimeString.let { LocalDateTime.parse(it, formatter) }
    }

}
