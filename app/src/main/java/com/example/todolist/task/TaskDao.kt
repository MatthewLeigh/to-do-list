package com.example.todolist.task

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todolist.enums.Frequency

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

    @Query("SELECT * FROM tasks WHERE frequency = :frequency ORDER BY taskId ASC")
    fun getTasksByFrequency(frequency: Frequency): LiveData<List<TaskTable>>

}