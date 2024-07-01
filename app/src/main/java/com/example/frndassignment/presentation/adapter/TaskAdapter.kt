package com.example.frndassignment.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.frndassignment.R
import com.example.frndassignment.databinding.ListItemTasksBinding
import com.example.frndassignment.domain.servermodels.TaskModel
import com.example.frndassignment.utils.setVisible
import com.example.frndassignment.utils.throttleClick

class TaskAdapter : ListAdapter<TaskModel, TaskAdapter.TaskViewHolder>(diffCallBack) {

    private var onTaskDeleteClick: ((TaskModel) -> Unit)? = null

    fun setOnTaskDeleteClick(onTaskDeleteClick: (TaskModel) -> Unit) {
        this.onTaskDeleteClick = onTaskDeleteClick
    }

    class TaskViewHolder(private val binding: ListItemTasksBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(): ListItemTasksBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            ListItemTasksBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val taskModel = getItem(position)
        holder.bind().apply {
            ivDropDown.setImageResource(if (taskModel.isExpanded) R.drawable.baseline_arrow_drop_up_24 else R.drawable.baseline_arrow_drop_down_24)
            tvDesc.setVisible(taskModel.isExpanded)
            tvDelete.setVisible(taskModel.isExpanded)
            tvTitle.apply {
                ellipsize =
                    if (taskModel.isExpanded) null else android.text.TextUtils.TruncateAt.END
                maxLines = if (taskModel.isExpanded) Int.MAX_VALUE else 1
                text = taskModel.taskDetail?.title.orEmpty()
            }
            tvDesc.text = taskModel.taskDetail?.description ?: "--"
            tvDelete.throttleClick {
                onTaskDeleteClick?.invoke(taskModel)
            }
            holder.itemView.throttleClick {
                taskModel.isExpanded = !taskModel.isExpanded
                notifyItemChanged(holder.adapterPosition)
            }
        }
    }

    companion object {
        val diffCallBack = object : DiffUtil.ItemCallback<TaskModel>() {
            override fun areItemsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TaskModel, newItem: TaskModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}