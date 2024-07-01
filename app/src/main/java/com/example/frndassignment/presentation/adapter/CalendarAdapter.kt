package com.example.frndassignment.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.frndassignment.R
import com.example.frndassignment.databinding.ListItemDateBinding
import com.example.frndassignment.databinding.ListItemDayBinding
import com.example.frndassignment.domain.servermodels.DateModel
import com.example.frndassignment.presentation.enums.ViewType
import com.example.frndassignment.utils.toStringOrEmpty

class CalendarAdapter(private val currentDate: DateModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var pair: Pair<List<String>, List<DateModel>> = Pair(emptyList(), emptyList())
    private var totalDays = 30
    private var selectedDate = DateModel()
    private var onItemClick: ((Int?) -> Unit)? = null

    fun setOnItemClick(onItemClick: (Int?) -> Unit) {
        this.onItemClick = onItemClick
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(pair: Pair<List<String>, List<DateModel>>) {
        this.pair = pair
        notifyDataSetChanged()
    }

    fun setTotalDays(totalDays: Int) {
        this.totalDays = totalDays
    }

    class DateViewHolder(private val binding: ListItemDateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            currentDate: DateModel,
            date: DateModel,
            pair: Pair<Int, Int>,
            position: Int,
            selectedDate: DateModel
        ) {
            binding.tvDate.text = date.day.toStringOrEmpty()
            if (position in (pair.first..pair.second)) {
                binding.tvDate.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        android.R.color.black
                    )
                )
                if (currentDate.day == date.day && currentDate.month == date.month && currentDate.year == date.year) {
                    binding.tvDate.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.iris
                        )
                    )
                    binding.cv
                        .strokeWidth = 2
                }
                if (selectedDate == date) {
                    binding.cv
                        .strokeWidth = 2
                    binding.cv.setCardBackgroundColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.iris
                        )
                    )
                    binding.tvDate.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.white
                        )
                    )
                }
            }
        }
    }

    class DayViewHolder(private val binding: ListItemDayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(day: String) {
            binding.tvDay.text = day
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DayViewHolder -> holder.bind(pair.first[position])
            is DateViewHolder -> {
                val dateModel = pair.second[position - 7]
                val idxOfOne = pair.second.indexOfFirst { it.day == 1 } + 7
                val idxOfLast = pair.second.indexOfLast { it.day == totalDays } + 7
                holder.itemView.setOnClickListener {
                    if (position in (idxOfOne..idxOfLast)) {
                        selectedDate = pair.second[position - 7]
                        onItemClick?.invoke(dateModel.id?.toIntOrNull())
                        notifyDataSetChanged()
                    }
                }
                holder.bind(
                    currentDate,
                    dateModel,
                    idxOfOne to idxOfLast,
                    position,
                    selectedDate
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return pair.first.size + pair.second.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.LAYOUT_DATE.ordinal -> {
                DateViewHolder(
                    ListItemDateBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                DayViewHolder(
                    ListItemDayBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position < 7) return ViewType.LAYOUT_DAYS.ordinal
        return ViewType.LAYOUT_DATE.ordinal
    }
}