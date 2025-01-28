package com.example.todolist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate


// Task Table: Stores the meta data of each task.
@Entity(
    tableName = "tasks",
    indices = [Index(value = ["frequency"])]
)
data class Task(
    @PrimaryKey(autoGenerate = true) var taskId: Int = 0,
    @ColumnInfo(name = "title") val taskTitle: String,
    @ColumnInfo(name = "description") val taskDescription: String?,
    @ColumnInfo(name = "hexColor") val taskHexColor: String,
    @ColumnInfo(name = "icon") val taskIcon: String,
    @ColumnInfo(name = "frequency") val taskFrequency: Frequency,
    @ColumnInfo(name = "startDate") val taskStartDate: LocalDate,
    @ColumnInfo(name = "endDate") val taskEndDate: LocalDate?
)


// Task Instances Table: Stores the instance record of each task.
@Entity(
    tableName = "task_instances",
    foreignKeys = [ForeignKey(
        entity = Task::class,
        parentColumns = ["taskId"],
        childColumns = ["taskId"],
        onDelete = CASCADE
    )],
    indices = [Index(value = ["taskId"])]
)
data class TaskInstance(
    @PrimaryKey(autoGenerate = true) var instanceId: Int = 0,
    @ColumnInfo(name = "taskId") val taskId: Int,
    @ColumnInfo(name = "date") val instanceDate: LocalDate,
    @ColumnInfo(name = "isCompleted") val instanceIsCompleted: Boolean
)