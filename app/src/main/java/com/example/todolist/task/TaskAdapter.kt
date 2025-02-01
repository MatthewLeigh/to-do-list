package com.example.todolist.task

import android.content.Context
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TaskAdapter(
    private val context: Context,
    private val taskClickManageInterface: TaskClickManageInterface,
    private val taskClickToggleIsCheckedInterface: TaskClickToggleIsCheckedInterface
)
    : RecyclerView.Adapter<TaskAdapter.ViewHolder>()
{

    // List to hold all task items
    private val allTaskTables = ArrayList<TaskTable>()

    // Toggle Due Date / Countdown State
    private var showCountdown: Boolean = false

    // Handler for active countdown updates
    private val handler = Handler(Looper.getMainLooper())
    private var updateInterval = 1000L

    // Runnable to update countdowns for all visible ViewHolders
    private val updateCountdownRunnable = object : Runnable {
        override fun run() {
            if (showCountdown) {
                visibleViewHolders.forEach { holder ->
                    val task = allTaskTables[holder.adapterPosition]
                    updateCountdownText(holder, task)
                }
            }
            handler.postDelayed(this, updateInterval)
        }
    }

    // Track visible ViewHolders
    private val visibleViewHolders = mutableListOf<ViewHolder>()

    // Start the countdown updates
    private fun startCountdownUpdates() {
        handler.post(updateCountdownRunnable)
    }

    // Stop the countdown updates
    private fun stopCountdownUpdates() {
        handler.removeCallbacks(updateCountdownRunnable)
    }

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

    // Update the showCountdown flag
    fun updateShowCountdown(showCountdown: Boolean) {
        this.showCountdown = showCountdown
        if (showCountdown) {
            startCountdownUpdates()
        } else {
            stopCountdownUpdates()
        }
        notifyDataSetChanged()
        Log.d("TaskAdapter", "Show countdown updated: $showCountdown")
    }

    // Apply UI changes to completed tasks
    private fun applyTaskComplete(holder: ViewHolder, isComplete: Boolean) {

        if (isComplete) {
            holder.listTaskTitle.paintFlags = holder.listTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.listTaskSubtitle.paintFlags = holder.listTaskSubtitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.listTaskFooter.setText(R.string.task_complete)

            
        } else {
            holder.listTaskTitle.paintFlags = holder.listTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.listTaskSubtitle.paintFlags = holder.listTaskSubtitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

        }
        Log.d("TaskAdapter", "Applied strikethrough for task completion: $isComplete")
    }

    // Apply overdue UI changes for overdue tasks
    private fun applyOverdueUI(holder: ViewHolder, task: TaskTable) {
        val overdue = task.taskDueDateTime.isBefore(LocalDateTime.now()) && !task.isComplete

        if (overdue) {
            holder.listTaskCheckBox.setButtonDrawable(R.drawable.cusomt_checkbox_overdue)
            holder.listTaskFooter.setTextColor(context.resources.getColor(R.color.overdue, null))

        } else {
            holder.listTaskCheckBox.setButtonDrawable(R.drawable.custom_checkbox)
            holder.listTaskFooter.setTextColor(context.resources.getColor(R.color.text, null))
        }
        Log.d("TaskAdapter", "Applied overdue UI for task: ${task.taskTitle}, isOverdue: $overdue")
    }

    // Update the countdown text for a specific ViewHolder
    private fun updateCountdownText(holder: ViewHolder, task: TaskTable) {

        if (task.isComplete) {
            return
        }

        val overdue = task.taskDueDateTime.isBefore(LocalDateTime.now())

        val duration = if (overdue) {
            Duration.between(task.taskDueDateTime, LocalDateTime.now())
        } else {
            Duration.between(LocalDateTime.now(), task.taskDueDateTime)
        }

        val days = duration.toDays()
        val hours = duration.toHours() % 24
        val minutes = duration.toMinutes() % 60

        val countdownText = buildString {
            if (task.taskDueDateTime.isBefore(LocalDateTime.now())) append("- ")
            if (days > 0) append("$days days, ")
            if (hours > 0) append("$hours hours, ")
            if (minutes > 0) append("$minutes minutes")
        }

        holder.listTaskFooter.text = countdownText

        // Update every second until new minute ticks over to ensure alignment with exact minute,
        // then update every minute.
        val seconds = duration.seconds % 60
        updateInterval = if (seconds.toInt() == 59) 60000L else 1000L
    }

    // Set up the UI elements for a task
    private fun setupUI(holder: ViewHolder, task: TaskTable) {
        // Set the text
        holder.listTaskTitle.text = task.taskTitle
        holder.listTaskSubtitle.text = task.taskDescription

        // Update the footer text based on the toggle state (Time until due date, or due date)
        if (showCountdown) {
            updateCountdownText(holder, task)
        } else {
            holder.listTaskFooter.text = task.taskDueDateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a"))
        }

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

        Log.d("TaskAdapter", "Set up click listeners for task: ${task.taskTitle}")
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
        applyOverdueUI(holder, task)
        applyTaskComplete(holder, task.isComplete)
        setupOnClickListeners(holder, task)

        Log.d("TaskAdapter", "Bound data to ViewHolder at position: $position")
    }

    // Track visible ViewHolders
    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        visibleViewHolders.add(holder)
    }

    // Remove ViewHolders that are no longer visible
    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        visibleViewHolders.remove(holder)
    }

    // Get the number of items in the list
    override fun getItemCount(): Int {
        return allTaskTables.size
    }

    // Interface for deleting a task
    interface TaskClickDeleteInterface {
        fun onDeleteButtonClick(taskTable: TaskTable)
    }

    // Interface for managing a task
    interface TaskClickManageInterface {
        fun onTaskItemClick(taskTable: TaskTable)
    }

    // Interface for toggling the checkbox
    interface TaskClickToggleIsCheckedInterface {
        fun onTaskCheckBoxToggled(taskTable: TaskTable)
    }
}