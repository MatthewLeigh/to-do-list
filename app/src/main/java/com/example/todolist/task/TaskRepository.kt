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

    fun getAllUniqueCategories(): LiveData<List<String>> = taskDao.getAllUniqueCategories()

    suspend fun updateTaskCompletionStatus(taskId: Int, isComplete: Boolean) {
        taskDao.updateTaskCompletionStatus(taskId, isComplete)
    }

    suspend fun deleteById(taskId: Int) {
        taskDao.deleteById(taskId)
    }

}