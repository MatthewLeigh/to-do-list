package com.example.todolist.task

import androidx.lifecycle.LiveData
import com.example.todolist.enums.Frequency

class TaskRepository(private val taskDao: TaskDao) {

    suspend fun insert(taskTable: TaskTable) {
        taskDao.insert(taskTable)
    }

    suspend fun update(taskTable: TaskTable) {
        taskDao.update(taskTable)
    }

    suspend fun delete(taskTable: TaskTable) {
        taskDao.delete(taskTable)
    }

    fun getAllTasks(): LiveData<List<TaskTable>> = taskDao.getAllTasks()

    fun getTaskById(taskId: Int): LiveData<TaskTable> = taskDao.getTaskById(taskId)

    fun getTasksByFrequency(frequency: Frequency): LiveData<List<TaskTable>> = taskDao.getTasksByFrequency(frequency)

}