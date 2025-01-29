package com.example.todolist.taskInstance

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskInstanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskInstanceTable: TaskInstanceTable)

    @Update
    suspend fun update(taskInstanceTable: TaskInstanceTable)

    @Delete
    suspend fun delete(taskInstanceTable: TaskInstanceTable)

    @Query("SELECT * FROM task_instances WHERE taskId = :taskId ORDER BY date ASC")
    fun getInstancesForTask(taskId: Int): LiveData<List<TaskInstanceTable>>

}