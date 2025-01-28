package com.example.todolist.TaskInstance

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todolist.Task.TaskInstance

@Dao
interface TaskInstanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskInstance: TaskInstance)

    @Update
    suspend fun update(taskInstance: TaskInstance)

    @Delete
    suspend fun delete(taskInstance: TaskInstance)

    @Query("SELECT * FROM task_instances WHERE taskId = :taskId ORDER BY date ASC")
    fun getInstancesForTask(taskId: Int): LiveData<List<TaskInstance>>

}