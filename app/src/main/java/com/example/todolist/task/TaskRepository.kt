package com.example.todolist.task

import androidx.lifecycle.LiveData
import com.example.todolist.Frequency

class TaskRepository(private val taskDao: TaskDao) {

    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }

    suspend fun update(task: Task) {
        taskDao.update(task)
    }

    suspend fun delete(task: Task) {
        taskDao.delete(task)
    }

    fun getAllTasks(): LiveData<List<Task>> = taskDao.getAllTasks()

    fun getTaskById(taskId: Int): LiveData<Task> = taskDao.getTaskById(taskId)

    fun getTasksByFrequency(frequency: Frequency): LiveData<List<Task>> = taskDao.getTasksByFrequency(frequency)

}