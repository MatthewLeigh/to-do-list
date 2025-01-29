package com.example.todolist.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.todolist.enums.Frequency
import java.time.LocalDate

// Task Table: Stores the meta data of each task.
@Entity(
    tableName = "tasks",
    indices = [Index(value = ["frequency"])]
)
data class TaskTable(
    @ColumnInfo(name = "title") val taskTitle: String,
    @ColumnInfo(name = "description") val taskDescription: String?,
    @ColumnInfo(name = "hexColor") val taskHexColor: String,
    @ColumnInfo(name = "category") val taskCategory: String?,
    @ColumnInfo(name = "frequency") val taskFrequency: Frequency,
    @ColumnInfo(name = "startDate") val taskStartDate: LocalDate,
    @ColumnInfo(name = "endDate") val taskEndDate: LocalDate?
) {
    @PrimaryKey(autoGenerate = true)
    var taskId = 0
}
