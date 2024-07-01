package com.example.frndassignment.domain.usecase

import com.example.frndassignment.data.ApiResponse
import com.example.frndassignment.domain.repository.TaskRepository
import com.example.frndassignment.domain.servermodels.TaskModel
import com.example.frndassignment.utils.ResultState
import retrofit2.Response
import javax.inject.Inject

class AddTaskByIdUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) : BaseUseCase<AddTaskByIdUseCase.AddTaskParams, ApiResponse> {

    data class AddTaskParams(val userId: Int, val taskModel: TaskModel)

    override suspend fun process(params: AddTaskParams): ResultState<ApiResponse> {
        try {
            val response = taskRepository.addTaskById(
                params.userId, params.taskModel
            )
            return if (response.isSuccessful) {
                ResultState.Success(response.body())
            } else {
                ResultState.Error(errorMsg = response.message())
            }
        } catch (e: Exception) {
            return ResultState.Error(errorMsg = e.localizedMessage ?: "")
        }
    }
}