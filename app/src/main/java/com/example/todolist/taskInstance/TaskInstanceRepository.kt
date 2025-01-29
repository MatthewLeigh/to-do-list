package com.example.todolist.taskInstance

import androidx.lifecycle.LiveData

class TaskInstanceRepository(private val taskInstanceDao: TaskInstanceDao) {

    suspend fun insert(taskInstanceTable: TaskInstanceTable) {
        taskInstanceDao.insert(taskInstanceTable)
    }

    suspend fun update(taskInstanceTable: TaskInstanceTable) {
        taskInstanceDao.update(taskInstanceTable)
    }

    suspend fun delete(taskInstanceTable: TaskInstanceTable) {
        taskInstanceDao.delete(taskInstanceTable)
    }

    fun getInstancesForTask(taskId: Int): LiveData<List<TaskInstanceTable>> = taskInstanceDao.getInstancesForTask(taskId)

}