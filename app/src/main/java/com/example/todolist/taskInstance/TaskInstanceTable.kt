package com.example.todolist.taskInstance

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.todolist.task.TaskTable
import java.time.LocalDate


// Task Instances Table: Stores the instance record of each task.
@Entity(
    tableName = "task_instances",
    foreignKeys = [ForeignKey(
        entity = TaskTable::class,
        parentColumns = ["taskId"],
        childColumns = ["taskId"],
        onDelete = CASCADE
    )],
    indices = [Index(value = ["taskId"])]
)
data class TaskInstanceTable(
    @PrimaryKey(autoGenerate = true) var instanceId: Int = 0,
    @ColumnInfo(name = "taskId") val taskId: Int,
    @ColumnInfo(name = "date") val instanceDate: LocalDate,
    @ColumnInfo(name = "isCompleted") val instanceIsCompleted: Boolean
)