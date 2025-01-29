package com.example.todolist.task

import androidx.lifecycle.LiveData

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

    fun getAllUniqueCategories(): LiveData<List<String>> = taskDao.getAllUniqueCategories()

    fun getCompletedTasks(): LiveData<List<TaskTable>> = taskDao.getCompletedTasks()

    fun getOutstandingTasks(): LiveData<List<TaskTable>> = taskDao.getOutstandingTasks()

    fun getOverdueTasks(): LiveData<List<TaskTable>> = taskDao.getOverdueTasks()




}