package com.example.todolist.TaskInstance

import androidx.lifecycle.LiveData
import com.example.todolist.Task.TaskInstance

class TaskInstanceRepository(private val taskInstanceDao: TaskInstanceDao) {

    suspend fun insert(taskInstance: TaskInstance) {
        taskInstanceDao.insert(taskInstance)
    }

    suspend fun update(taskInstance: TaskInstance) {
        taskInstanceDao.update(taskInstance)
    }

    suspend fun delete(taskInstance: TaskInstance) {
        taskInstanceDao.delete(taskInstance)
    }

    fun getInstancesForTask(taskId: Int): LiveData<List<TaskInstance>> = taskInstanceDao.getInstancesForTask(taskId)

}