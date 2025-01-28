package com.example.todolist

import androidx.lifecycle.LiveData

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

    suspend fun getAllTasks(): LiveData<List<Task>> = taskDao.getAllTasks()

    suspend fun getTaskById(taskId: Int): LiveData<Task> = taskDao.getTaskById(taskId)

    suspend fun getTasksByFrequency(frequency: Frequency): LiveData<List<Task>> = taskDao.getTasksByFrequency(frequency)

}