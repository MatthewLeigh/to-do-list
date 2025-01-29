package com.example.todolist.task

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R

class TaskAdapterMain(
    val context: Context,
    val taskClickDeleteInterface: TaskClickDeleteInterface,
    val taskClickUpdateInterface: TaskClickUpdateInterface
) : RecyclerView.Adapter<TaskAdapterMain.ViewHolder>() {

    private val allTaskTables = ArrayList<TaskTable>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val listTaskTitle: TextView = itemView.findViewById(R.id.listTaskTitle)
        val listTaskSubtitle: TextView = itemView.findViewById(R.id.listTaskSubtitle)
        val listTaskDeleteButton: ImageView = itemView.findViewById(R.id.listTaskDeleteButton)
        val listTaskHighlight: ConstraintLayout = itemView.findViewById(R.id.listTaskHighlight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.component_list_task_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.listTaskTitle.text = allTaskTables[position].taskTitle
        holder.listTaskSubtitle.text = allTaskTables[position].taskDueDateTime.toString()
        holder.listTaskHighlight.setBackgroundColor(allTaskTables[position].taskHexColor)

        holder.listTaskDeleteButton.setOnClickListener {
            taskClickDeleteInterface.onDeleteButtonClick(allTaskTables[position])
        }

        holder.itemView.setOnClickListener {
            taskClickUpdateInterface.onTaskItemClick(allTaskTables[position])
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

    interface TaskClickDeleteInterface {
        fun onDeleteButtonClick(taskTable: TaskTable)
    }

    interface TaskClickUpdateInterface {
        fun onTaskItemClick(taskTable: TaskTable)
    }
    
}