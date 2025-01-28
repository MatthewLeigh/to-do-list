package com.example.todolist.TaskInstance

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todolist.Task.TaskInstance
import com.example.todolist.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskInstanceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskInstanceRepository

    init {
        val taskInstanceDao = AppDatabase.getDatabase(application).getTaskInstanceDao()
        repository = TaskInstanceRepository(taskInstanceDao)
    }

    fun insert(taskInstance: TaskInstance) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(taskInstance)
    }

    fun update(taskInstance: TaskInstance) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(taskInstance)
    }

    fun delete(taskInstance: TaskInstance) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(taskInstance)
    }

    fun getInstancesForTask(taskId: Int): LiveData<List<TaskInstance>> {
        return repository.getInstancesForTask(taskId)
    }
}
