package com.example.todolist.task

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import java.time.format.DateTimeFormatter

class TaskAdapter(
    private val context: Context,
    private val taskClickManageInterface: TaskClickManageInterface,
    private val taskClickToggleIsCheckedInterface: TaskClickToggleIsCheckedInterface
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    // List to hold all task items
    private val allTaskTables = ArrayList<TaskTable>()

    // ViewHolder Inner Class
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listTaskTitle: TextView = itemView.findViewById(R.id.listTaskTitle)
        val listTaskSubtitle: TextView = itemView.findViewById(R.id.listTaskSubtitle)
        val listTaskFooter: TextView = itemView.findViewById(R.id.listTaskFooter)
        val listTaskHighlight: ConstraintLayout = itemView.findViewById(R.id.listTaskHighlight)
        val listTaskCheckBox: CheckBox = itemView.findViewById(R.id.listTaskCheckBox)
    }

    // Update the list of tasks and notify the adapter of data changes
    fun updateList(newList: List<TaskTable>) {
        allTaskTables.clear()
        allTaskTables.addAll(newList)
        notifyDataSetChanged()

        Log.d("TaskAdapter", "Updated task list with ${newList.size} items")
    }

    // Apply or remove strikethrough effect based on task completion status
    private fun applyStrikethrough(holder: ViewHolder, isComplete: Boolean) {

        if (isComplete) {
            holder.listTaskTitle.paintFlags = holder.listTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.listTaskSubtitle.paintFlags = holder.listTaskSubtitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.listTaskFooter.paintFlags = holder.listTaskFooter.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        } else {
            holder.listTaskTitle.paintFlags = holder.listTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.listTaskSubtitle.paintFlags = holder.listTaskSubtitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.listTaskFooter.paintFlags = holder.listTaskFooter.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        Log.d("TaskAdapter", "Applied strikethrough for task completion: $isComplete")
    }

    // Apply overdue UI changes for overdue tasks
    private fun applyOverdueUI(holder: ViewHolder, task: TaskTable) {

        // Check if dueDateTime is before the current time.
        val isOverdue = task.taskDueDateTime.isBefore(
            java.time.Instant.ofEpochSecond(System.currentTimeMillis() / 1000)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime()
        ) && !task.isComplete

        if (isOverdue) {
            holder.listTaskCheckBox.setButtonDrawable(R.drawable.cusomt_checkbox_overdue)
            holder.listTaskFooter.setTextColor(context.resources.getColor(R.color.overdue, null))
        } else {
            holder.listTaskCheckBox.setButtonDrawable(R.drawable.custom_checkbox)
            holder.listTaskFooter.setTextColor(context.resources.getColor(R.color.text, null))
        }

        Log.d("TaskAdapter", "Applied overdue UI for task: ${task.taskTitle}, isOverdue: $isOverdue")
    }

    // Set up the UI elements for a task
    private fun setupUI(holder: ViewHolder, task: TaskTable) {

        // Set the text
        holder.listTaskTitle.text = task.taskTitle
        holder.listTaskSubtitle.text = task.taskDescription
        holder.listTaskFooter.text = "${task.taskDueDateTime?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a"))}"

        // Set the background color for the task
        holder.listTaskHighlight.setBackgroundColor(task.taskHexColor)

        // Set up checkbox state
        holder.listTaskCheckBox.isChecked = task.isComplete

        Log.d("TaskAdapter", "Set up UI for task: ${task.taskTitle}")
    }

    // Set up click listeners for the task item and checkbox
    private fun setupOnClickListeners(holder: ViewHolder, task: TaskTable) {

        // Update the task completion status when the checkbox is toggled
        holder.listTaskCheckBox.setOnClickListener {
            Log.d("TaskAdapter", "Checkbox toggled for task: ${task.taskTitle}")
            taskClickToggleIsCheckedInterface.onTaskCheckBoxToggled(task)
        }

        // Open task for update when clicked
        holder.itemView.setOnClickListener {
            Log.d("TaskAdapter", "Task item clicked: ${task.taskTitle}")
            taskClickManageInterface.onTaskItemClick(task)
        }

        Log.d("TaskAdapter", "Sett up click listeners for task: ${task.taskTitle}")
    }

    // Create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.component_list_task_item, parent, false)
        Log.d("TaskAdapter", "ViewHolder created")
        return ViewHolder(itemView)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = allTaskTables[position]

        setupUI(holder, task)
        applyStrikethrough(holder, task.isComplete)
        applyOverdueUI(holder, task)
        setupOnClickListeners(holder, task)

        Log.d("TaskAdapter", "Bound data to ViewHolder at position: $position")
    }

    // Get the number of items in the list
    override fun getItemCount(): Int {
        return allTaskTables.size
    }

    // Interface for deleting a task
    interface TaskClickDeleteInterface {
        fun onDeleteButtonClick(taskTable: TaskTable)
    }

    // Interface for managing a task (e.g., viewing or editing)
    interface TaskClickManageInterface {
        fun onTaskItemClick(taskTable: TaskTable)
    }

    // Interface for toggling the checkbox (task completion status)
    interface TaskClickToggleIsCheckedInterface {
        fun onTaskCheckBoxToggled(taskTable: TaskTable)
    }
}