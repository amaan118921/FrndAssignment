package com.example.frndassignment.domain.usecase

import com.example.frndassignment.data.ApiResponse
import com.example.frndassignment.domain.repository.TaskRepository
import com.example.frndassignment.utils.ResultState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCalendarTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) : BaseUseCase<GetCalendarTaskUseCase.GetCalendarTaskParams, ApiResponse> {

    data class GetCalendarTaskParams(val userId: Int)

    override suspend fun process(params: GetCalendarTaskParams): ResultState<ApiResponse> {
        try {
            val response = taskRepository.getCalendarTaskListById(params.userId)
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