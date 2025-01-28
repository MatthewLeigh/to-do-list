package com.example.todolist

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import androidx.room.TypeConverters
import com.example.todolist.task.Task
import com.example.todolist.task.TaskDao
import com.example.todolist.taskInstance.TaskInstance
import com.example.todolist.taskInstance.TaskInstanceDao

@Database(entities = [Task::class, TaskInstance::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTasksDao(): TaskDao
    abstract fun getTaskInstanceDao(): TaskInstanceDao

    companion object{

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database"
                )
                .addTypeConverter(Converters())
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}