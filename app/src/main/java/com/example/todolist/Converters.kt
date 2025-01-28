package com.example.todolist

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromFrequencyToString(frequency: Frequency): String {
        return frequency.name
    }

    @TypeConverter
    fun fromStringToFrequency(value: String): Frequency {
        return Frequency.valueOf(value)
    }
}