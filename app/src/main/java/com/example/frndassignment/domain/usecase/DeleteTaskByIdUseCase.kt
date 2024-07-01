package com.example.frndassignment.domain.usecase

import com.example.frndassignment.data.ApiResponse
import com.example.frndassignment.domain.repository.TaskRepository
import com.example.frndassignment.domain.servermodels.TaskModel
import com.example.frndassignment.utils.ResultState
import retrofit2.Response
import javax.inject.Inject

class DeleteTaskByIdUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) : BaseUseCase<DeleteTaskByIdUseCase.DeleteTaskParams, ApiResponse> {

    data class DeleteTaskParams(val userId: Int, val taskId: Int)

    override suspend fun process(params: DeleteTaskParams): ResultState<ApiResponse> {
        try {
            val response = taskRepository.deleteTaskById(
                params.userId, params.taskId
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