package com.example.todolist.dialogs

import android.content.Intent
import android.os.Bundle
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

    private lateinit var taskViewModel: TaskViewModel
    private var taskId: Int = -1
    private var taskTitle: String = ""

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet, container, false)

        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

        taskId = arguments?.getInt("taskId") ?: -1
        taskTitle = arguments?.getString("taskTitle") ?: "No Title"
        val taskCategory = arguments?.getString("taskCategory") ?: "No Category"
        val taskDueDateTime = arguments?.getString("taskDueDateTime") ?: "No Due Date"
        val taskDescription = arguments?.getString("taskDescription") ?: "No Description"

        val sheetTitle: TextView = view.findViewById(R.id.sheetTitle)
        val sheetCategory: TextView = view.findViewById(R.id.sheetCategory)
        val sheetDueDate: TextView = view.findViewById(R.id.sheetDueDate)
        val sheetDescription: TextView = view.findViewById(R.id.sheetDescription)
        val sheetDeleteText: LinearLayout = view.findViewById(R.id.sheetDeleteText)
        val sheetUpdateText: LinearLayout = view.findViewById(R.id.sheetUpdateText)

        sheetTitle.text = taskTitle
        sheetCategory.text = taskCategory
        sheetDueDate.text = taskDueDateTime
        sheetDescription.text = taskDescription

        sheetDeleteText.setOnClickListener {
            deleteTask()
        }

        sheetUpdateText.setOnClickListener {
            updateTask()
        }

        return view
    }

    private fun deleteTask() {
        if (taskId != -1) {
            taskViewModel.deleteById(taskId)
            Toast.makeText(requireContext(), "$taskTitle Deleted", Toast.LENGTH_LONG).show()
            dismiss()
        }
    }

    private fun updateTask() {
        val intent = Intent(requireContext(), ManageTaskActivity::class.java).apply {
            putExtras(arguments ?: Bundle())
        }
        dismiss()
        startActivity(intent)
    }
}
