package com.example.todolist.task

import android.content.Context
import android.graphics.Paint
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
    val context: Context,
    val taskClickManageInterface: TaskClickManageInterface,
    val taskClickToggleIsCheckedInterface: TaskClickToggleIsCheckedInterface
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private val allTaskTables = ArrayList<TaskTable>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val listTaskTitle: TextView = itemView.findViewById(R.id.listTaskTitle)
        val listTaskSubtitle: TextView = itemView.findViewById(R.id.listTaskSubtitle)
        val listTaskFooter: TextView = itemView.findViewById(R.id.listTaskFooter)
        val listTaskHighlight: ConstraintLayout = itemView.findViewById(R.id.listTaskHighlight)
        val listTaskCheckBox: CheckBox = itemView.findViewById(R.id.listTaskCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_list_task_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = allTaskTables[position]

        val title = task.taskTitle
        val subtitle = if (task.taskCategory?.isNotEmpty() == true) {
            "${task.taskCategory} | ${task.taskDescription}"
        } else {
            task.taskDescription
        }

        val footer = "Due: ${task.taskDueDateTime?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a"))}"

        // Set the text
        holder.listTaskTitle.text = title
        holder.listTaskSubtitle.text = subtitle
        holder.listTaskFooter.text = footer

        // Apply strikethrough if task is complete
        if (task.isComplete) {
            holder.listTaskTitle.paintFlags = holder.listTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.listTaskSubtitle.paintFlags = holder.listTaskSubtitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.listTaskFooter.paintFlags = holder.listTaskFooter.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.listTaskTitle.paintFlags = holder.listTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.listTaskSubtitle.paintFlags = holder.listTaskSubtitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.listTaskFooter.paintFlags = holder.listTaskFooter.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        // Set footer to overdue color if overdue.
        if (
            task.taskDueDateTime.isBefore(java.time.Instant.ofEpochSecond(System.currentTimeMillis() / 1000).atZone(java.time.ZoneId.systemDefault()).toLocalDateTime())
            && !task.isComplete
        ) {
            holder.listTaskFooter.setTextColor(context.resources.getColor(R.color.overdue, null))
        } else {
            holder.listTaskFooter.setTextColor(context.resources.getColor(R.color.text, null))
        }

        // Set the background color for the task
        holder.listTaskHighlight.setBackgroundColor(task.taskHexColor)

        // Set up checkbox state
        holder.listTaskCheckBox.isChecked = task.isComplete

        // Update the task completion status when the checkbox is toggled
        holder.listTaskCheckBox.setOnClickListener {
            taskClickToggleIsCheckedInterface.onTaskCheckBoxToggled(task)
        }

        // Open task for update when clicked
        holder.itemView.setOnClickListener {
            taskClickManageInterface.onTaskItemClick(task)
        }
    }

    override fun getItemCount(): Int {
        return allTaskTables.size
    }

    fun updateList(newList: List<TaskTable>) {
        allTaskTables.clear()
        allTaskTables.addAll(newList)
        notifyDataSetChanged()
    }

    // Interface for deleting a task
    interface TaskClickDeleteInterface {
        fun onDeleteButtonClick(taskTable: TaskTable)
    }

    // Interface for managing a task (e.g., viewing or editing)
    interface TaskClickManageInterface {
        fun onTaskItemClick(taskTable: TaskTable)
    }

    // New Interface for toggling the checkbox (task completion status)
    interface TaskClickToggleIsCheckedInterface {
        fun onTaskCheckBoxToggled(taskTable: TaskTable)
    }
}
