package com.example.todolist.task

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskTable: TaskTable)

    @Update
    suspend fun update(taskTable: TaskTable)

    @Delete
    suspend fun delete(taskTable: TaskTable)

    @Query("SELECT * FROM tasks ORDER BY taskId ASC")
    fun getAllTasks(): LiveData<List<TaskTable>>

    @Query("SELECT * FROM tasks WHERE taskId = :taskId LIMIT 1")
    fun getTaskById(taskId: Int): LiveData<TaskTable>

    @Query("SELECT DISTINCT category FROM tasks ORDER BY category ASC")
    fun getAllUniqueCategories(): LiveData<List<String>>

}