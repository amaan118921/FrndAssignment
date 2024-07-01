package com.example.frndassignment.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frndassignment.domain.servermodels.TaskModel
import com.example.frndassignment.domain.usecase.AddTaskByIdUseCase
import com.example.frndassignment.domain.usecase.DeleteTaskByIdUseCase
import com.example.frndassignment.domain.usecase.GetCalendarTaskUseCase
import com.example.frndassignment.domain.usecase.executeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias TaskList = List<TaskModel>

@HiltViewModel
class AppViewModel @Inject constructor(
    private val getCalendarTaskUseCase: GetCalendarTaskUseCase,
    private val addTaskByIdUseCase: AddTaskByIdUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase
) : ViewModel() {

    private var job: Job? = null

    private var selectedId: Int? = null

    fun getSelectedId() = selectedId

    fun setCurrentId(selectedId: Int?) {
        this.selectedId = selectedId
    }

    fun getCalendarTaskListById(
        userId: Int,
        onFailure: (msg: String?) -> Unit,
        onSuccess: (list: List<TaskModel>) -> Unit
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            executeUseCase(
                getCalendarTaskUseCase,
                GetCalendarTaskUseCase.GetCalendarTaskParams(userId),
                onFailure = onFailure
            ) {
                onSuccess(it?.tasks ?: emptyList())
            }
        }
    }

    fun addTaskToList(
        userId: Int? = selectedId, task: TaskModel, onFailure: (msg: String?) -> Unit,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            executeUseCase(
                addTaskByIdUseCase,
                AddTaskByIdUseCase.AddTaskParams(userId ?: return@launch, task),
                onFailure = onFailure
            ) {
                onSuccess()
            }
        }
    }

    fun deleteTaskById(
        userId: Int?, taskId: Int?, onFailure: (msg: String?) -> Unit,
        onSuccess: (id: Int?) -> Unit
    ) {
        viewModelScope.launch {
            executeUseCase(
                deleteTaskByIdUseCase,
                DeleteTaskByIdUseCase.DeleteTaskParams(
                    userId ?: return@launch,
                    taskId ?: return@launch
                ),
                onFailure = onFailure
            ) {
                onSuccess(selectedId)
            }
        }
    }
}