package com.example.todolist.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.task.TaskTable
import com.example.todolist.task.TaskAdapterMain
import com.example.todolist.task.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), TaskAdapterMain.TaskClickDeleteInterface, TaskAdapterMain.TaskClickUpdateInterface {

    lateinit var taskList : RecyclerView
    lateinit var addButton : FloatingActionButton
    lateinit var taskViewModel : TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        taskList = findViewById(R.id.MainTaskListRV)
        addButton = findViewById(R.id.MainAddButton)
        taskList.layoutManager = LinearLayoutManager(this)

        val taskAdapterMain = TaskAdapterMain(this, this, this)
        taskList.adapter = taskAdapterMain
        taskViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[TaskViewModel::class.java]
        taskViewModel.allTasks.observe(this, { list->
            list?.let {
                taskAdapterMain.updateList(it)
            }
        })

        addButton.setOnClickListener {
            val intent = Intent(this@MainActivity, ManageTaskActivity::class.java)
            startActivity(intent)
            this.finish()
        }

    }

    override fun onDeleteButtonClick(taskTable: TaskTable) {
        taskViewModel.deleteTask(taskTable)
        Toast.makeText(
            this,
            "${taskTable.taskTitle} Deleted",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onTaskItemClick(taskTable: TaskTable) {
        val intent = Intent(this@MainActivity, ManageTaskActivity::class.java)
        intent.putExtra("intentType", "Update")
        intent.putExtra("taskId", taskTable.taskId)
        intent.putExtra("taskTitle", taskTable.taskTitle)
        intent.putExtra("taskDescription", taskTable.taskDescription)
        // TODO: Complete List
        startActivity(intent)
        this.finish()
    }
}