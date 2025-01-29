package com.example.todolist.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.R
import com.example.todolist.task.TaskTable
import com.example.todolist.task.TaskViewModel
import java.time.LocalDateTime
import java.util.Calendar

class ManageTaskActivity : AppCompatActivity() {

    // Declare View Model
    lateinit var taskViewModel: TaskViewModel

    // Declare TextViews
    lateinit var manageTaskActivityTitle : TextView
    lateinit var manageDueDateLabel : TextView
    lateinit var manageDueTimeLabel : TextView

    // Declare EditTexts
    lateinit var manageTaskTitle : EditText
    lateinit var manageTaskDescription : EditText

    // Declare AutoCompleteTextViews
    lateinit var manageTaskCategoryAutoComplete : AutoCompleteTextView

    // Declare Buttons
    lateinit var manageColorButton : Button
    lateinit var manageSaveTaskButton : Button

    // Task Id
    var manageTaskId = -1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manage_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.manageTask)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize ViewModel
        taskViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[TaskViewModel::class.java]

        // Initialize EditTexts for task details
        manageTaskTitle = findViewById(R.id.manageTaskTitle)
        manageTaskDescription = findViewById(R.id.manageTaskDescription)

        // Initialize TextViews
        manageTaskActivityTitle = findViewById(R.id.manageTaskActivityTitle)
        manageDueDateLabel = findViewById(R.id.manageDueDateLabel)
        manageDueTimeLabel = findViewById(R.id.manageDueTimeLabel)

        // Initialize AutoCompleteTextViews
        manageTaskCategoryAutoComplete = findViewById(R.id.manageTaskCategoryAutoComplete)

        // Initialize Buttons for various actions
        manageColorButton = findViewById(R.id.manageColorButton)
        manageSaveTaskButton = findViewById(R.id.manageSaveTaskButton)






        // Category Auto Complete Options
        val categoryList = mutableListOf<String>()
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categoryList)
        manageTaskCategoryAutoComplete.setAdapter(categoryAdapter)

        taskViewModel.allCategories.observe(this) { categories ->
            categoryList.clear()
            categoryList.addAll(categories)
            categoryAdapter.notifyDataSetChanged()
        }

        manageTaskCategoryAutoComplete.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                manageTaskCategoryAutoComplete.showDropDown()
            }
        }


        // Date Picker
        manageDueDateLabel.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    manageDueDateLabel.text = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }


        // Time Picker
        manageDueTimeLabel.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                this,
                { _, hour, minute ->
                    val formattedTime = String.format("%02d:%02d", hour, minute)
                    manageDueTimeLabel.text = formattedTime
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePicker.show()
        }


        // Get Intent Type
        val intentType = intent.getStringExtra("intentType")

        // If Update...
        if (intentType.equals("Update")) {

            manageTaskActivityTitle.text = "Update Task"
            manageTaskId = intent.getIntExtra("taskId", -1)
            manageTaskTitle.setText(intent.getStringExtra("taskTitle"))
            manageTaskDescription.setText(intent.getStringExtra("taskDescription"))
            // TODO: Compelte lists
        } else {
            manageTaskActivityTitle.text = "Create New Task"
        }




















        // On Save Button Click
        manageSaveTaskButton.setOnClickListener {

            val managedTaskTable = TaskTable(
                manageTaskTitle.text.toString(),
                manageTaskDescription.text.toString(),
                "#FF0000",
                "Fake",
                LocalDateTime.now().plusDays(1),
                false
            )

            if (intentType.equals("Update")) {
                managedTaskTable.taskId = manageTaskId
                taskViewModel.updateTask(managedTaskTable)
            } else {
                taskViewModel.insertTask(managedTaskTable)
            }

            Toast.makeText(
                this,
                "${manageTaskTitle.text} Task Updated",
                Toast.LENGTH_LONG
            ).show()

            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }
    }
}