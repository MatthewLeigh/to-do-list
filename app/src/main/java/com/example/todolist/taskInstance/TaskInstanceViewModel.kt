package com.example.todolist.taskInstance

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todolist.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskInstanceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskInstanceRepository

    init {
        val taskInstanceDao = AppDatabase.getDatabase(application).getTaskInstanceDao()
        repository = TaskInstanceRepository(taskInstanceDao)
    }

    fun insert(taskInstanceTable: TaskInstanceTable) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(taskInstanceTable)
    }

    fun update(taskInstanceTable: TaskInstanceTable) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(taskInstanceTable)
    }

    fun delete(taskInstanceTable: TaskInstanceTable) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(taskInstanceTable)
    }

    fun getInstancesForTask(taskId: Int): LiveData<List<TaskInstanceTable>> {
        return repository.getInstancesForTask(taskId)
    }
}
