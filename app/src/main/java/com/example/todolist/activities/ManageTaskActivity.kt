package com.example.todolist.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.enums.Frequency
import com.example.todolist.R
import com.example.todolist.task.TaskRepository
import com.example.todolist.task.TaskTable
import com.example.todolist.task.TaskViewModel
import java.time.LocalDate

class ManageTaskActivity : AppCompatActivity() {

    // Declare View Model
    lateinit var taskViewModel: TaskViewModel

    // Declare EditTexts
    lateinit var manageTaskTitle : EditText
    lateinit var manageTaskDescription : EditText
    lateinit var manageTaskCategoryLabel : EditText
    lateinit var manageFrequencyLabel : EditText

    // Declare Spinners
    lateinit var manageFrequencySpinner : Spinner

    // Declare AutoCompleteTextViews
    lateinit var manageTaskCategoryAutoComplete : AutoCompleteTextView

    // Declare Buttons
    lateinit var manageStartDateButton : Button
    lateinit var manageEndDateButton : Button
    lateinit var manageColorPickerButton : Button
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

        // Initialize Spinners for categories and frequency
        manageTaskCategoryAutoComplete = findViewById(R.id.manageTaskCategoryAutoComplete)
        manageFrequencySpinner = findViewById(R.id.manageFrequencySpinner)

        // Initialize Buttons for various actions
        manageStartDateButton = findViewById(R.id.manageStartDateButton)
        manageEndDateButton = findViewById(R.id.manageEndDateButton)
        manageColorPickerButton = findViewById(R.id.manageColorPickerButton)
        manageSaveTaskButton = findViewById(R.id.manageSaveTaskButton)

        // Independent of Intent Type

        // Frequency Spinner Options
        val frequencyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Frequency.entries.toTypedArray())
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        manageFrequencySpinner.adapter = frequencyAdapter


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



        // Get Intent Type
        val intentType = intent.getStringExtra("intentType")

        // If Update...
        if (intentType.equals("Update")) {

            manageTaskId = intent.getIntExtra("taskId", -1)
            manageTaskTitle.setText(intent.getStringExtra("taskTitle"))
            manageTaskDescription.setText(intent.getStringExtra("taskDescription"))
            // TODO: Compelte lists
        }




















        // On Save Button Click
        manageSaveTaskButton.setOnClickListener {

            val managedTaskTable = TaskTable(
                manageTaskTitle.text.toString(),
                manageTaskDescription.text.toString(),
                "#FF0000",
                "Fake",
                Frequency.DAILY,
                LocalDate.now(),
                LocalDate.now().plusWeeks(1)
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