package com.example.todolist.task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todolist.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    val allTasks: LiveData<List<TaskTable>>
    val allCategories: LiveData<List<String>>
    private val taskRepository: TaskRepository

    init {
        val taskDao = AppDatabase.getDatabase(application).getTasksDao()
        taskRepository = TaskRepository(taskDao)
        allTasks = taskRepository.getAllTasks()
        allCategories = taskRepository.getAllUniqueCategories()
    }

    fun insertTask(taskTable: TaskTable) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.insert(taskTable)
    }

    fun updateTask(taskTable: TaskTable) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.update(taskTable)
    }

    fun deleteTask(taskTable: TaskTable) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.delete(taskTable)
    }

    fun getTaskById(taskId: Int) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.getTaskById(taskId)
    }

    fun getAllUniqueCategories() = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.getAllUniqueCategories()
    }

    fun getCompletedTasks() = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.getCompletedTasks()
    }

    fun getOutstandingTasks() = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.getOutstandingTasks()
    }

    fun getOverdueTasks() = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.getOverdueTasks()
    }

    fun updateTaskCompletionStatus(taskId: Int, isComplete: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.updateTaskCompletionStatus(taskId, isComplete)
    }

    fun deleteById(taskId: Int) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.deleteById(taskId)
    }

}