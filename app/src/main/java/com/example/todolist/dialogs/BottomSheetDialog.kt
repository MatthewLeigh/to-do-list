package com.example.todolist.dialogs

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.R
import com.example.todolist.activities.ManageTaskActivity
import com.example.todolist.task.TaskTable
import com.example.todolist.task.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BottomSheetDialog : BottomSheetDialogFragment() {

    // UI Components
    private lateinit var sheetTitle: TextView
    private lateinit var sheetCategory: TextView
    private lateinit var sheetDueDate: TextView
    private lateinit var sheetCountdown: TextView
    private lateinit var countdownIcon: ImageView
    private lateinit var sheetDescription: TextView
    private lateinit var sheetDeleteText: LinearLayout
    private lateinit var sheetUpdateText: LinearLayout

    // ViewModel
    private lateinit var taskViewModel: TaskViewModel

    // Task Properties
    private var taskId: Int = -1
    private var taskTitle: String = ""
    private var taskCategory: String = ""
    private var taskDueDateTime: LocalDateTime? = null
    private var taskDescription: String = ""
    private var taskIsCompleted: Boolean = false

    // Countdown Timer
    private lateinit var countDownTimer: CountDownTimer
    private val updateInterval = 1000L // Update every second

    // Companion Object
    companion object {
        fun newInstance(task: TaskTable): BottomSheetDialog {
            val args = Bundle().apply {
                putInt("taskId", task.taskId)
                putString("taskTitle", task.taskTitle)
                putString("taskDescription", task.taskDescription)
                putInt("taskHexColor", task.taskHexColor)
                putString("taskCategory", task.taskCategory)
                putString("taskDueDateTime", task.taskDueDateTime.toString())
                putBoolean("taskIsCompleted", task.isComplete)
            }

            return BottomSheetDialog().apply {
                arguments = args
            }
        }
    }

    // Initialize UI components
    private fun setupUI(view: View) {
        sheetTitle = view.findViewById(R.id.sheetTitle)
        sheetCategory = view.findViewById(R.id.sheetCategory)
        sheetDueDate = view.findViewById(R.id.sheetDueDate)
        sheetCountdown = view.findViewById(R.id.sheetCountdown)
        countdownIcon = view.findViewById(R.id.countdownIcon)
        sheetDescription = view.findViewById(R.id.sheetDescription)
        sheetDeleteText = view.findViewById(R.id.sheetDeleteText)
        sheetUpdateText = view.findViewById(R.id.sheetUpdateText)
        Log.d("BottomSheetDialog", "UI components initialized")
    }

    // Read arguments and assign to properties
    private fun readArguments() {
        taskId = arguments?.getInt("taskId") ?: -1
        taskTitle = arguments?.getString("taskTitle") ?: ""
        taskCategory = arguments?.getString("taskCategory") ?: ""
        taskDescription = arguments?.getString("taskDescription") ?: ""
        taskIsCompleted = arguments?.getBoolean("taskIsCompleted") ?: false

        val dateTimeString = arguments?.getString("taskDueDateTime") ?: ""
        taskDueDateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        Log.d("BottomSheetDialog", "Arguments assigned to properties")
    }

    // Set text values where required
    private fun setText() {
        sheetTitle.text = taskTitle
        sheetCategory.text = taskCategory
        sheetDescription.text = taskDescription
        sheetDueDate.text = "${DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a").format(taskDueDateTime)}"

        Log.d("BottomSheetDialog", "Text set for UI components")
    }

    // Handle completed task
    private fun handleCompletedTask() {
        sheetCountdown.text = getString(R.string.task_complete)
        sheetCountdown.setTextColor(ContextCompat.getColor(requireContext(), R.color.text))
        countdownIcon.setImageResource(R.drawable.ic_check_circle)
        Log.d("BottomSheetDialog", "UI set for completed task")
    }

    // Start the countdown timer if task is not completed.
    private fun startCountdown() {

        if (taskIsCompleted) {
            handleCompletedTask()
            return
        }

        Log.d("BottomSheetDialog", "Starting countdown timer")

        val dueDateTime = taskDueDateTime!!
        val currentTime = LocalDateTime.now()

        // Calculate the time difference in milliseconds
        val duration = Duration.between(currentTime, dueDateTime)
        val millisInFuture = duration.toMillis()

        // Start the countdown timer
        countDownTimer = object : CountDownTimer(Long.MAX_VALUE, updateInterval) {
            override fun onTick(millisUntilFinished: Long) {

                val overdue = dueDateTime.isBefore(currentTime)

                val duration = if (overdue) {
                    Duration.between(dueDateTime, LocalDateTime.now())
                } else {
                    Duration.between(LocalDateTime.now(), dueDateTime)
                }

                val days = duration.toDays()
                val hours = duration.toHours() % 24
                val minutes = duration.toMinutes() % 60
                val seconds = duration.seconds % 60

                val countdownText = buildString {
                    if (overdue) append("- ")
                    if (days > 0) append("$days days, ")
                    if (hours > 0) append("$hours hours, ")
                    if (minutes > 0) append("$minutes minutes, ")
                    append("$seconds seconds")
                }

                // Update the countdown TextView
                sheetCountdown.text = countdownText

                // Update colors and icon based on overdue status
                if (overdue) {
                    sheetCountdown.setTextColor(ContextCompat.getColor(requireContext(), R.color.overdue))
                    sheetDueDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.overdue))
                    countdownIcon.setImageResource(R.drawable.ic_circle_error_overdue)

                } else {
                    sheetCountdown.setTextColor(ContextCompat.getColor(requireContext(), R.color.text))
                    sheetDueDate.setTextColor(ContextCompat.getColor(requireContext(), R.color.text))
                    countdownIcon.setImageResource(R.drawable.ic_alarm)
                }
            }

            // Needed for CountDownTimer object, but never used since the timer runs into negatives.
            override fun onFinish() { }
        }.start()
    }

    // Delete task from database
    private fun deleteTask() {
        if (taskId != -1) {
            taskViewModel.deleteById(taskId)
            Toast.makeText(requireContext(), "$taskTitle Deleted", Toast.LENGTH_LONG).show()
            dismiss()
            Log.d("BottomSheetDialog", "$taskTitle Deleted")
        }
    }

    // Update task in database
    private fun updateTask() {
        val intent = Intent(requireContext(), ManageTaskActivity::class.java).apply {
            putExtras(arguments ?: Bundle())
        }
        dismiss()
        startActivity(intent)
        Log.d("BottomSheetDialog", "$taskTitle Update Selected")
    }

    // Set up onClick listeners
    private fun setupOnClickListeners() {
        sheetDeleteText.setOnClickListener { deleteTask() }
        sheetUpdateText.setOnClickListener { updateTask() }
        Log.d("BottomSheetDialog", "onClick listeners set up.")
    }

    // Handle create activity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.bottom_sheet, container, false)
        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

        setupUI(view)
        readArguments()
        setText()
        startCountdown()
        setupOnClickListeners()

        Log.d("BottomSheetDialog", "Dialog created and initialized")

        return view
    }

    // Clean up the countdown timer when the dialog is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }
}