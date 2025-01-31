package com.example.todolist.dialogs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.R
import com.example.todolist.activities.ManageTaskActivity
import com.example.todolist.task.TaskTable
import com.example.todolist.task.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog : BottomSheetDialogFragment() {

    // UI Components
    private lateinit var sheetTitle: TextView
    private lateinit var sheetCategory: TextView
    private lateinit var sheetDueDate: TextView
    private lateinit var sheetDescription: TextView
    private lateinit var sheetDeleteText: LinearLayout
    private lateinit var sheetUpdateText: LinearLayout

    // ViewModel
    private lateinit var taskViewModel: TaskViewModel

    // Task Properties
    private var taskId: Int = -1
    private var taskTitle: String = ""
    private var taskCategory: String = ""
    private var taskDueDateTime: String = ""
    private var taskDescription: String = ""

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
        taskDueDateTime = arguments?.getString("taskDueDateTime") ?: ""
        taskDescription = arguments?.getString("taskDescription") ?: ""
        Log.d("BottomSheetDialog", "Arguments assigned to properties")
    }

    // Set text values where required
    private fun setText() {
        sheetTitle.text = taskTitle
        sheetCategory.text = taskCategory
        sheetDueDate.text = taskDueDateTime
        sheetDescription.text = taskDescription
        Log.d("BottomSheetDialog", "Text set for UI components")
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
        setupOnClickListeners()

        Log.d("BottomSheetDialog", "Dialog created and initialized")

        return view
    }
}
