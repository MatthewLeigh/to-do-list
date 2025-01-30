package com.example.todolist.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.R
import com.example.todolist.task.TaskTable
import com.example.todolist.task.TaskViewModel
import yuku.ambilwarna.AmbilWarnaDialog
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class ManageTaskActivity : AppCompatActivity() {

    // Declare View Model
    private lateinit var taskViewModel: TaskViewModel

    // Declare UI Elements
    private lateinit var manageTaskActivityTitle: TextView
    private lateinit var manageTaskTitle: EditText
    private lateinit var manageTaskDescription: EditText
    private lateinit var manageTaskCategoryAutoComplete: AutoCompleteTextView
    private lateinit var manageDueDateLabel: TextView
    private lateinit var manageDueTimeLabel: TextView
    private lateinit var manageColorText: TextView
    private lateinit var manageColorButton: Button
    private lateinit var manageSaveTaskButton: Button

    // Task Properties
    private var manageTaskId = -1
    private var manageIsCompleted = false
    private var manageLocalDateTime = LocalDateTime.now()
    private var manageColor = Color.BLACK

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

        // Initialize UI Elements
        manageTaskActivityTitle = findViewById(R.id.manageTaskActivityTitle)
        manageTaskTitle = findViewById(R.id.manageTaskTitle)
        manageTaskDescription = findViewById(R.id.manageTaskDescription)
        manageTaskCategoryAutoComplete = findViewById(R.id.manageTaskCategoryAutoComplete)
        manageDueDateLabel = findViewById(R.id.manageDueDateLabel)
        manageDueTimeLabel = findViewById(R.id.manageDueTimeLabel)
        manageColorText = findViewById(R.id.manageColorText)
        manageColorButton = findViewById(R.id.manageColorButton)
        manageSaveTaskButton = findViewById(R.id.manageSaveTaskButton)

        // Setup Category Dropdown
        val categoryList = mutableListOf<String>()
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categoryList)
        manageTaskCategoryAutoComplete.setAdapter(categoryAdapter)

        taskViewModel.allCategories.observe(this) { categories ->
            categoryList.clear()
            categoryList.addAll(categories)
            categoryAdapter.notifyDataSetChanged()
        }

        manageTaskCategoryAutoComplete.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) manageTaskCategoryAutoComplete.showDropDown()
        }

        // Date Picker
        manageDueDateLabel.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                manageLocalDateTime = manageLocalDateTime.withYear(year).withMonth(month + 1).withDayOfMonth(dayOfMonth)
                val formattedDate = manageLocalDateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                manageDueDateLabel.text = formattedDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Time Picker
        manageDueTimeLabel.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(this, { _, hour, minute ->
                manageLocalDateTime = manageLocalDateTime.withHour(hour).withMinute(minute)
                val formattedTime = manageLocalDateTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
                manageDueTimeLabel.text = formattedTime
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
        }

        // Color Picker
        manageColorButton.setOnClickListener {
            AmbilWarnaDialog(this, manageColor, object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                    manageColor = color
                    manageColorButton.setBackgroundColor(color)
                    manageColorText.text = String.format("#%06X", 0xFFFFFF and color) // Convert to hex
                }

                override fun onCancel(dialog: AmbilWarnaDialog?) {}
            }).show()
        }

        // Get Intent Type
        val intentType = intent.getStringExtra("intentType")

        if (intentType == "Update") {

            val taskDateTimeString = intent.getStringExtra("taskDueDateTime")
            val taskDateTime = taskDateTimeString?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }

            manageColor = intent.getIntExtra("taskHexColor", Color.BLACK)

            manageTaskActivityTitle.text = "Update Task"
            manageTaskId = intent.getIntExtra("taskId", -1)
            manageTaskTitle.setText(intent.getStringExtra("taskTitle"))
            manageTaskDescription.setText(intent.getStringExtra("taskDescription"))
            manageColorText.text = String.format("#%06X", 0xFFFFFF and manageColor)
            manageTaskCategoryAutoComplete.setText(intent.getStringExtra("taskCategory"), false)
            manageDueDateLabel.text = taskDateTime?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
            manageDueTimeLabel.text = taskDateTime?.format(DateTimeFormatter.ofPattern("hh:mm a"))
            manageIsCompleted = intent.getBooleanExtra("taskIsCompleted", false)

            manageColorButton.setBackgroundColor(manageColor)

        } else {
            manageTaskActivityTitle.text = "Create New Task"
        }

        // On Save Button Click
        manageSaveTaskButton.setOnClickListener {

            val task = TaskTable(
                taskTitle = manageTaskTitle.text.toString(),
                taskDescription = manageTaskDescription.text.toString(),
                taskHexColor = manageColor,
                taskCategory = manageTaskCategoryAutoComplete.text.toString(),
                taskDueDateTime = manageLocalDateTime,
                isComplete = manageIsCompleted
            )

            if (intentType == "Update") {
                task.taskId = manageTaskId
                taskViewModel.updateTask(task)
                Toast.makeText(this, "Task Updated", Toast.LENGTH_LONG).show()

            } else {
                taskViewModel.insertTask(task)
                Toast.makeText(this, "Task Created", Toast.LENGTH_LONG).show()

            }

            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }
}
