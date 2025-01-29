package com.example.todolist.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import androidx.room.TypeConverters
import com.example.todolist.task.TaskTable
import com.example.todolist.task.TaskDao

@Database(entities = [TaskTable::class], version = 2, exportSchema = false)
@TypeConverters(AppDatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTasksDao(): TaskDao

    companion object{

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database"
                )
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}