package com.example.frndassignment.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.frndassignment.Constants
import com.example.frndassignment.R
import com.example.frndassignment.databinding.ActivityMainBinding
import com.example.frndassignment.databinding.ActivityNumberPickerBinding
import com.example.frndassignment.domain.servermodels.DateModel
import com.example.frndassignment.domain.servermodels.TaskModel
import com.example.frndassignment.presentation.adapter.CalendarAdapter
import com.example.frndassignment.presentation.adapter.TaskAdapter
import com.example.frndassignment.presentation.bottomsheet.AddNewTaskBottomSheet
import com.example.frndassignment.presentation.viewmodel.AppViewModel
import com.example.frndassignment.utils.orZero
import com.example.frndassignment.utils.parseDate
import com.example.frndassignment.utils.setVisible
import com.example.frndassignment.utils.throttleClick
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: AppViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CalendarAdapter
    private var taskAdapter = TaskAdapter()
    private var calendar = Calendar.getInstance()
    private lateinit var currentDate: DateModel
    private val monthList = mutableListOf<DateModel>()
    private val dayList by lazy {
        getWeekDayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /* Init current Date */

        Calendar.getInstance().let {
            currentDate = DateModel(
                day = it.get(Calendar.DAY_OF_MONTH),
                month = it.get(Calendar.MONTH),
                year = it.get(Calendar.YEAR)
            ).apply {
                id = generateId(day.orZero(), month.orZero(), year.orZero())
            }
        }
        viewModel.setCurrentId(currentDate.id?.toIntOrNull())
        setTaskAdapter()
        setAdapter()
        setListeners()

        /* Get today's Tasks */
        fetchTasksById(currentDate.id?.toIntOrNull())
    }

    // Fetch Tasks By Id
    private fun fetchTasksById(id: Int?) {
        if (id == null) return
        showProgressBar()
        taskAdapter.submitList(emptyList())
        viewModel.getCalendarTaskListById(id, onFailure = {
            hideProgressBar()
            showToast(it)
        }) {
            taskAdapter.submitList(it)
            showEmptyView(it.isEmpty())
            hideProgressBar()
        }
    }

    // Add Tasks By Id
    private fun addTaskById(taskModel: TaskModel) {
        viewModel.addTaskToList(task = taskModel, onFailure = {
            showToast(getString(R.string.something_went_wrong) + it.orEmpty())
        }) {
            showToast(getString(R.string.task_added))
            fetchTasksById(viewModel.getSelectedId())
        }
    }

    // Delete Task By Id
    private fun deleteTaskById(taskModel: TaskModel) {
        viewModel.deleteTaskById(
            viewModel.getSelectedId(),
            taskId = taskModel.id,
            onFailure = { msg ->
                showToast(getString(R.string.something_went_wrong) + msg.orEmpty())
            }) {
            showToast(getString(R.string.task_deleted))
            fetchTasksById(viewModel.getSelectedId())
        }
    }

    private fun setTaskAdapter() {
        taskAdapter.setOnTaskDeleteClick {
            showConfirmationDialog {
                deleteTaskById(it)
            }

        }
        binding.rvTasks.adapter = taskAdapter
    }

    private fun showConfirmationDialog(onYesClick: () -> Unit) {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.delete_task))
            .setMessage(getString(R.string.task_deleted_msg))
            .setPositiveButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.yes)) { dialog, _ ->
                onYesClick()
                dialog.dismiss()
            }
            .show()
    }

    private fun setListeners() {
        binding.apply {
            prev.throttleClick {
                onPrevMonthClick()
            }
            next.throttleClick {
                onNextMonthClick()
            }
            tvYear.throttleClick {
                navigateToMonthYearPicker()
            }
            tvMonth.throttleClick {
                navigateToMonthYearPicker()
            }
            ivAddTask.throttleClick {
                openAddTaskBottomSheet()
            }
        }
    }

    private fun dialogYearPicker() {
        val dialog = MaterialAlertDialogBuilder(this).create()
        dialog.setView(ActivityNumberPickerBinding.inflate(LayoutInflater.from(this@MainActivity)).root)
        dialog.show()
        val monthNumberPicker = dialog.findViewById<NumberPicker>(R.id.monthNumberPicker)
        val numberPicker = dialog.findViewById<NumberPicker>(R.id.numberPicker)
        val btnSelect = dialog.findViewById<MaterialButton>(R.id.btnSelect)

        /* Set Month*/

        val currentMonth = calendar[Calendar.MONTH]
        val monthList = getListOfMonth()
        monthNumberPicker?.apply {
            maxValue = monthList.size - 1
            minValue = 0
            value = currentMonth
            displayedValues = monthList.toTypedArray()
        }
        /* Set Year */
        val currentYear = calendar[Calendar.YEAR]
        val list = (currentYear - 40..currentYear + 40).toList()
        numberPicker?.apply {
            maxValue = list.size - 1
            minValue = 0
            value = list.indexOf(currentYear)
            displayedValues = list.map { it.toString() }.toTypedArray()
        }
        btnSelect?.throttleClick {
            val selectedYear = list[numberPicker?.value ?: 0]
            val selectedMonth = monthNumberPicker?.value ?: 0
            getMonthList(month = selectedMonth, year = selectedYear).apply {
                setList(
                    this.first,
                    this.second
                )
            }
            dialog.dismiss()
        }
    }

    private fun onNextMonthClick() {
        val month = calendar[Calendar.MONTH]
        if (month < 11)
            getMonthList(month + 1).apply { setList(this.first, this.second) }
        else {
            getMonthList(0, year = calendar.get(Calendar.YEAR) + 1).apply {
                setList(
                    this.first,
                    this.second
                )
            }
        }
    }

    private fun onPrevMonthClick() {
        val month = calendar[Calendar.MONTH]
        if (month > 0)
            getMonthList(month - 1).apply { setList(this.first, this.second) }
        else {
            getMonthList(month = 11, year = calendar.get(Calendar.YEAR) - 1).apply {
                setList(
                    this.first,
                    this.second
                )
            }
        }
    }

    private fun setAdapter() {
        adapter = CalendarAdapter(
            currentDate
        )
        binding.rv.adapter = adapter
        adapter.setOnItemClick {
            viewModel.setCurrentId(it)
            fetchTasksById(it)
        }
        getMonthList(
            calendar[Calendar.MONTH],
        ).apply {
            setList(
                this.first, this.second
            )
        }
    }


    private fun setList(monthList: List<DateModel>, total: Int) {
        adapter.setList(dayList to monthList)
        adapter.setTotalDays(total)
    }


    private fun getMonthList(
        month: Int = calendar[Calendar.MONTH],
        year: Int = calendar[Calendar.YEAR]
    ): Pair<List<DateModel>, Int> {
        /* day of first lies between 1 - 7 */
        monthList.clear()
        calendar.set(year, month, 1)
        val dayOfFirst = calendar[Calendar.DAY_OF_WEEK]

        val result = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        binding.tvMonth.text = parseDate(calendar.timeInMillis)
        binding.tvYear.text = parseDate(calendar.timeInMillis, Constants.year)

        val prevMonthDays = getMonthDays(month - 1)
        for (i in 1..42) {
            monthList.add(
                if (i < dayOfFirst) {
                    DateModel(
                        day = prevMonthDays?.let { (it - dayOfFirst + i + 1) }
                    )
                } else if (i >= result + dayOfFirst) {
                    DateModel(
                        day = (i - result - dayOfFirst + 1)
                    )
                } else {
                    DateModel(
                        id = generateId(
                            i - dayOfFirst + 1,
                            month,
                            year
                        ),
                        month = month,
                        year = year,
                        day = (i - dayOfFirst + 1)
                    )
                }
            )
        }
        return monthList to result
    }

    private fun getMonthDays(month: Int): Int? {
        if (month < 0 || month > 11) return null
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    private fun getWeekDayList(): List<String> {
        return listOf(
            getString(R.string.sunday),
            getString(R.string.mon),
            getString(R.string.tuesday),
            getString(R.string.wednesday),
            getString(R.string.thursday),
            getString(R.string.friday),
            getString(R.string.saturday)
        )
    }


    private fun openAddTaskBottomSheet() {
        AddNewTaskBottomSheet().apply {
            setOnDoneClick {
                addTaskById(it)
            }
        }.showSafely()
    }

    private fun navigateToMonthYearPicker() {
        dialogYearPicker()
    }

    private fun BottomSheetDialogFragment.showSafely() {
        if (!isAdded) {
            show(this@MainActivity.supportFragmentManager, null)
        } else showToast(getString(R.string.fragment_already_added))
    }


    private fun showEmptyView(flag: Boolean) {
        binding.emptyView.setVisible(flag)
    }

    private fun hideProgressBar() {
        binding.pfTask.root.setVisible(false)
    }

    private fun showProgressBar() {
        binding.pfTask.root.setVisible(true)
    }

    private fun showToast(it: String?) {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }

    private fun generateId(day: Int, month: Int, year: Int) = "$day$month$year"

    private fun getListOfMonth(): List<String> {
        return listOf(
            getString(R.string.jan),
            getString(R.string.feb),
            getString(R.string.mar),
            getString(R.string.apr),
            getString(R.string.may),
            getString(R.string.jun),
            getString(R.string.july),
            getString(R.string.aug),
            getString(R.string.sept),
            getString(R.string.oct),
            getString(R.string.nov),
            getString(R.string.dec),
        )
    }


}
