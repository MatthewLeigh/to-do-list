package com.example.todolist.Task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todolist.Frequency
import com.example.todolist.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    val allTasks: LiveData<List<Task>>
    private val taskRepository: TaskRepository

    init {
        val taskDao = AppDatabase.getDatabase(application).getTasksDao()
        taskRepository = TaskRepository(taskDao)
        allTasks = taskRepository.getAllTasks()
    }

    fun insertTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.insert(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.update(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.delete(task)
    }

    fun getTaskById(taskId: Int) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.getTaskById(taskId)
    }

    fun getTasksByFrequency(frequency: Frequency) = viewModelScope.launch(Dispatchers.IO) {
        taskRepository.getTasksByFrequency(frequency)
    }
}