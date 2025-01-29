package com.example.todolist

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
import com.example.todolist.task.Task
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
        taskViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[taskViewModel::class.java]
        taskViewModel.allTasks.observe(this, { list->
            list?.let {
                taskAdapterMain.updateList(it)
            }
        })

        addButton.setOnClickListener {
            val intent = Intent(this@MainActivity, CreateOrUpdateTaskActivity::class.java)
            startActivity(intent)
            this.finish()
        }

    }

    override fun onDeleteButtonClick(task: Task) {
        taskViewModel.deleteTask(task)
        Toast.makeText(
            this,
            "${task.taskTitle} Deleted",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onTaskItemClick(task: Task) {
        val intent = Intent(this@MainActivity, CreateOrUpdateTaskActivity::class.java)
        intent.putExtra("taskType", "Update")
        intent.putExtra("taskTitle", task.taskTitle)
        intent.putExtra("taskDescription", task.taskDescription)
        startActivity(intent)
        this.finish()
    }
}