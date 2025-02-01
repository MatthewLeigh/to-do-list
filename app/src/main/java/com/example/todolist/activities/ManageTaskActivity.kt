package com.example.todolist.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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

    // UI Components
    private lateinit var manageTaskActivityTitle: TextView
    private lateinit var manageTaskTitle: EditText
    private lateinit var manageTaskDescription: EditText
    private lateinit var manageTaskCategoryAutoComplete: AutoCompleteTextView
    private lateinit var manageDueDateLabel: TextView
    private lateinit var manageDueTimeLabel: TextView
    private lateinit var manageColorText: TextView
    private lateinit var manageColorButton: Button
    private lateinit var manageSaveTaskButton: Button
    private lateinit var manageCancelButton: Button

    // ViewModel
    private lateinit var taskViewModel: TaskViewModel

    // Task Properties
    private var manageTaskId = -1
    private var manageIsCompleted = false
    private var manageLocalDateTime = LocalDateTime.now()
    private var manageColor = Color.LTGRAY
    private var intentType = ""
    private var initialDueDateTime = LocalDateTime.now().plusDays(1)

    // Set up ViewModel
    private fun setupViewModel() {
        taskViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[TaskViewModel::class.java]
        Log.d("ManageTaskActivity", "ViewModel initialized")
    }

    // Initialize UI components
    private fun setupUI() {
        manageTaskActivityTitle = findViewById(R.id.manageTaskActivityTitle)
        manageTaskTitle = findViewById(R.id.manageTaskTitle)
        manageTaskDescription = findViewById(R.id.manageTaskDescription)
        manageTaskCategoryAutoComplete = findViewById(R.id.manageTaskCategoryAutoComplete)
        manageDueDateLabel = findViewById(R.id.manageDueDateLabel)
        manageDueTimeLabel = findViewById(R.id.manageDueTimeLabel)
        manageColorText = findViewById(R.id.manageColorText)
        manageColorButton = findViewById(R.id.manageColorButton)
        manageSaveTaskButton = findViewById(R.id.manageSaveTaskButton)
        manageCancelButton = findViewById(R.id.manageCancelButton)
        Log.d("ManageTaskActivity", "UI components initialized")
    }

    // Set up category dropdown
    private fun setupCategoryDropdown() {
        val categoryList = mutableListOf<String>()
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categoryList)
        manageTaskCategoryAutoComplete.setAdapter(categoryAdapter)

        taskViewModel.allCategories.observe(this) { categories ->
            categoryList.clear()
            categoryList.addAll(categories)
            categoryAdapter.notifyDataSetChanged()
            Log.d("ManageTaskActivity", "Categories loaded: ${categories.size} items")
        }

        manageTaskCategoryAutoComplete.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) manageTaskCategoryAutoComplete.showDropDown()
        }
        Log.d("ManageTaskActivity", "Category dropdown initialized")
    }

    // Handle Intent data
    private fun handleIntentData() {
        val args = intent.extras
        intentType = if (args != null) "Update" else "Create"
        Log.d("ManageTaskActivity", "Intent type: $intentType")

        if (intentType == "Update") {
            manageTaskId = args?.getInt("taskId", -1) ?: -1
            manageTaskTitle.setText(args?.getString("taskTitle"))
            manageTaskDescription.setText(args?.getString("taskDescription"))
            manageColor = args?.getInt("taskHexColor", Color.BLACK) ?: Color.BLACK
            manageTaskCategoryAutoComplete.setText(args?.getString("taskCategory"), false)

            val taskDateTimeString = args?.getString("taskDueDateTime")
            manageLocalDateTime = LocalDateTime.parse(taskDateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

            manageIsCompleted = args?.getBoolean("taskIsCompleted", false) ?: false
            manageTaskActivityTitle.text = getString(R.string.update_task)
            Log.d("ManageTaskActivity", "Loaded task data for update: ID = $manageTaskId")
        } else {
            manageLocalDateTime = initialDueDateTime
            manageTaskActivityTitle.text = getString(R.string.create_new_task)
            Log.d("ManageTaskActivity", "Initialized for new task creation")
        }

        // Update UI with task details
        manageDueDateLabel.text = manageLocalDateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        manageDueTimeLabel.text = manageLocalDateTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
        manageColorButton.setBackgroundColor(manageColor)
        manageColorText.text = String.format("#%06X", 0xFFFFFF and manageColor)
        Log.d("ManageTaskActivity", "UI updated with task details")
    }

    // Set up date and time pickers
    private fun setupDateTimePickers() {
        // Date Picker
        manageDueDateLabel.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                manageLocalDateTime = manageLocalDateTime.withYear(year).withMonth(month + 1).withDayOfMonth(dayOfMonth)
                manageDueDateLabel.text = manageLocalDateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                Log.d("ManageTaskActivity", "Date selected: ${manageLocalDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE)}")
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Time Picker
        manageDueTimeLabel.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(this, { _, hour, minute ->
                manageLocalDateTime = manageLocalDateTime.withHour(hour).withMinute(minute)
                manageDueTimeLabel.text = manageLocalDateTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
                Log.d("ManageTaskActivity", "Time selected: ${manageLocalDateTime.format(DateTimeFormatter.ISO_LOCAL_TIME)}")
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
        }
        Log.d("ManageTaskActivity", "Date and time pickers initialized")
    }

    // Set up color picker
    private fun setupColorPicker() {
        manageColorButton.setOnClickListener {
            AmbilWarnaDialog(this, manageColor, object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                    manageColor = color
                    manageColorButton.setBackgroundColor(color)
                    manageColorText.text = String.format("#%06X", 0xFFFFFF and color)
                    Log.d("ManageTaskActivity", "Color selected: ${String.format("#%06X", 0xFFFFFF and color)}")
                }

                override fun onCancel(dialog: AmbilWarnaDialog?) {
                    Log.d("ManageTaskActivity", "Color picker canceled")
                }
            }).show()
        }
        Log.d("ManageTaskActivity", "Color picker initialized")
    }

    // Set up save button
    private fun setupSaveButton() {
        manageSaveTaskButton.setOnClickListener {
            if (!validateInput()) {
                Log.d("ManageTaskActivity", "Validation failed: Inputs are invalid")
                return@setOnClickListener
            }

            val task = TaskTable(
                taskTitle = manageTaskTitle.text.toString(),
                taskDescription = manageTaskDescription.text.toString(),
                taskHexColor = manageColor,
                taskCategory = manageTaskCategoryAutoComplete.text.toString(),
                taskDueDateTime = manageLocalDateTime,
                isComplete = manageIsCompleted
            )

            var snackbarMessage = ""

            if (intentType == "Update") {
                task.taskId = manageTaskId
                taskViewModel.updateTask(task)
                snackbarMessage = "Task updated successfully!"
                Log.d("ManageTaskActivity", "Task updated: ID = $manageTaskId")
            } else {
                taskViewModel.insertTask(task)
                snackbarMessage = "Task created successfully!"
                Log.d("ManageTaskActivity", "Task created: Title = ${task.taskTitle}")
            }

            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                putExtra("snackbar_message", snackbarMessage)
            }
            startActivity(intent)
            finish()
        }

        Log.d("ManageTaskActivity", "Save button initialized")
    }

    // Set up cancel button
    private fun setupCancelButton() {
        manageCancelButton.setOnClickListener {
            if (hasUnsavedChanges()) {
                showDiscardChangesDialog()
                Log.d("ManageTaskActivity", "Unsaved changes detected, showing discard dialog")
            } else {
                finish()
                Log.d("ManageTaskActivity", "No unsaved changes, finishing activity")
            }
        }
        Log.d("ManageTaskActivity", "Cancel button initialized")
    }

    // Validate user input
    private fun validateInput(): Boolean {
        var isValid = true
        val title = manageTaskTitle.text.toString().trim()
        val description = manageTaskDescription.text.toString().trim()

        if (title.isEmpty()) {
            manageTaskTitle.error = "Title cannot be empty"
            isValid = false
        }

        if (description.isEmpty()) {
            manageTaskDescription.error = "Description cannot be empty"
            isValid = false
        }

        Log.d("ManageTaskActivity", "Input validation result: $isValid")
        return isValid
    }

    // Check for any unsaved changes
    private fun hasUnsavedChanges(): Boolean {
        val title = manageTaskTitle.text.toString().trim()
        val description = manageTaskDescription.text.toString().trim()
        val category = manageTaskCategoryAutoComplete.text.toString().trim()
        val color = manageColor
        val dueDateTime = manageLocalDateTime

        if (intentType == "Update") {
            val originalTask = TaskTable(
                taskTitle = intent.extras?.getString("taskTitle") ?: "",
                taskDescription = intent.extras?.getString("taskDescription") ?: "",
                taskHexColor = intent.extras?.getInt("taskHexColor", Color.BLACK) ?: Color.BLACK,
                taskCategory = intent.extras?.getString("taskCategory") ?: "",
                taskDueDateTime = LocalDateTime.parse(intent.extras?.getString("taskDueDateTime"), DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                isComplete = intent.extras?.getBoolean("taskIsCompleted", false) ?: false
            )

            return title != originalTask.taskTitle ||
                    description != originalTask.taskDescription ||
                    category != originalTask.taskCategory ||
                    color != originalTask.taskHexColor ||
                    dueDateTime != originalTask.taskDueDateTime
        } else {
            return title.isNotEmpty() ||
                    description.isNotEmpty() ||
                    category.isNotEmpty() ||
                    color != Color.LTGRAY ||
                    dueDateTime != initialDueDateTime
        }
    }

    // Open dialog to confirm discarding unsaved changes
    private fun showDiscardChangesDialog() {
        AlertDialog.Builder(this)
            .setTitle("Discard Changes?")
            .setMessage("Are you sure you want to discard your changes?")
            .setPositiveButton("Discard") { _, _ ->
                super.onBackPressed()
                Log.d("ManageTaskActivity", "Discard changes confirmed")
            }
            .setNegativeButton("Cancel", null)
            .show()
        Log.d("ManageTaskActivity", "Discard changes dialog shown")
    }

    // Handle create activity
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        setContentView(R.layout.activity_manage_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.manageTask)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        super.onCreate(savedInstanceState)

        setupUI()
        setupViewModel()
        setupCategoryDropdown()
        handleIntentData()
        setupDateTimePickers()
        setupColorPicker()
        setupSaveButton()
        setupCancelButton()
        Log.d("ManageTaskActivity", "Activity created and initialized")
    }

    // Handle back press
    override fun onBackPressed() {
        if (hasUnsavedChanges()) {
            showDiscardChangesDialog()
            Log.d("ManageTaskActivity", "Back pressed with unsaved changes")
        } else {
            super.onBackPressed()
            Log.d("ManageTaskActivity", "Back pressed, no unsaved changes")
        }
    }
}