package com.example.todolist.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity :
    AppCompatActivity(),
    TaskAdapterMain.TaskClickDeleteInterface,
    TaskAdapterMain.TaskClickManageInterface,
    TaskAdapterMain.TaskClickToggleIsCheckedInterface
{

    lateinit var taskList : RecyclerView
    lateinit var addButton : FloatingActionButton
    lateinit var taskViewModel : TaskViewModel
    lateinit var mainBottomNav : BottomNavigationView

    private var currentFilter: String = "none"

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
        mainBottomNav = findViewById(R.id.MainBottomNav)

        val taskAdapterMain = TaskAdapterMain(this, this, this, this)
        taskList.adapter = taskAdapterMain
        taskViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[TaskViewModel::class.java]

        taskViewModel.allTasks.observe(this, { list ->
            list?.let {
                applyFilter(it, currentFilter, taskAdapterMain)
            }
        })

        addButton.setOnClickListener {
            val intent = Intent(this@MainActivity, ManageTaskActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        mainBottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_all_tasks -> {
                    currentFilter = "none"
                    applyFilter(taskViewModel.allTasks.value ?: emptyList(), currentFilter, taskAdapterMain)
                    true
                }
                R.id.nav_completed_tasks -> {
                    currentFilter = "isComplete"
                    applyFilter(taskViewModel.allTasks.value ?: emptyList(), currentFilter, taskAdapterMain)
                    true
                }
                R.id.nav_outsanding_tasks -> {
                    currentFilter = "outstanding"
                    applyFilter(taskViewModel.allTasks.value ?: emptyList(), currentFilter, taskAdapterMain)
                    true
                }
                R.id.nav_overdue_tasks -> {
                    currentFilter = "overdue"
                    applyFilter(taskViewModel.allTasks.value ?: emptyList(), currentFilter, taskAdapterMain)
                    true
                }
                else -> false
            }
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
        intent.putExtra("taskHexColor", taskTable.taskHexColor)
        intent.putExtra("taskCategory", taskTable.taskCategory)
        intent.putExtra("taskDueDateTime", taskTable.taskDueDateTime.toString())
        intent.putExtra("taskIsCompleted", taskTable.isComplete)
        startActivity(intent)
        this.finish()
    }

    override fun onTaskCheckBoxToggled(taskTable: TaskTable) {

        val isCompleteNew = !taskTable.isComplete
        taskViewModel.updateTaskCompletionStatus(taskTable.taskId, isCompleteNew)

        val status = if (taskTable.isComplete) "completed" else "incomplete"
        Toast.makeText(
            this,
            "${taskTable.taskTitle} marked as $status",
            Toast.LENGTH_SHORT
        ).show()

    }
}


private fun applyFilter(list: List<TaskTable>, filter: String, taskAdapterMain: TaskAdapterMain) {
    val filteredList = when (filter) {
        "none" -> list
        "isComplete" -> list.filter { task -> task.isComplete }
        "outstanding" -> list.filter { task -> !task.isComplete }
        "overdue" -> list.filter { task ->
            task.taskDueDateTime.isBefore(java.time.Instant.ofEpochSecond(System.currentTimeMillis() / 1000).atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()) && !task.isComplete
        }
        else -> list
    }
    Log.d("filter", "Filtered List: $filteredList")
    taskAdapterMain.updateList(filteredList)
}