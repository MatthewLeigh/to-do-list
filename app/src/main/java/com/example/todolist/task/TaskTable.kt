package com.example.todolist.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "tasks"
)
data class TaskTable(
    @ColumnInfo(name = "title") val taskTitle: String,
    @ColumnInfo(name = "description") val taskDescription: String?,
    @ColumnInfo(name = "hexColor") val taskHexColor: String,
    @ColumnInfo(name = "category") val taskCategory: String?,
    @ColumnInfo(name = "dueDateTime") val taskDueDateTime: LocalDateTime,
    @ColumnInfo(name = "isComplete") val isComplete: Boolean,
) {
    @PrimaryKey(autoGenerate = true)
    var taskId = 0
}
