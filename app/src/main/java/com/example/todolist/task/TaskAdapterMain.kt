package com.example.todolist.task

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R

class TaskAdapterMain(
    val context: Context,
    val taskClickDeleteInterface: TaskClickDeleteInterface,
    val taskClickUpdateInterface: TaskClickUpdateInterface
) : RecyclerView.Adapter<TaskAdapterMain.ViewHolder>() {

    private val allTasks = ArrayList<Task>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listTaskTitle = itemView.findViewById<TextView>(R.id.listTaskTitle)
        val listTaskSubtitle = itemView.findViewById<TextView>(R.id.listTaskSubtitle)
        val listTaskDeleteButton = itemView.findViewById<TextView>(R.id.listTaskDeleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.component_list_task_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.listTaskTitle.text = allTasks[position].taskTitle
        holder.listTaskSubtitle.text = allTasks[position].taskCategory

        holder.listTaskDeleteButton.setOnClickListener {
            taskClickDeleteInterface.onDeleteButtonClick(allTasks[position])
        }

        holder.itemView.setOnClickListener {
            taskClickUpdateInterface.onTaskItemClick(allTasks[position])
        }
    }

    override fun getItemCount(): Int {
        return allTasks.size
    }

    fun updateList(newList : List<Task>) {
        allTasks.clear()
        allTasks.addAll(newList)
        notifyDataSetChanged()
}

interface TaskClickDeleteInterface {
    fun onDeleteButtonClick(task: Task)
}

interface TaskClickUpdateInterface {
    fun onTaskItemClick(task: Task)
}