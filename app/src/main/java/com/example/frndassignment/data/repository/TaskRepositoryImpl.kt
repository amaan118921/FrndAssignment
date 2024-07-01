package com.example.frndassignment.data.repository

import com.example.frndassignment.data.Api
import com.example.frndassignment.data.ApiParams
import com.example.frndassignment.data.ApiResponse
import com.example.frndassignment.domain.repository.TaskRepository
import com.example.frndassignment.domain.servermodels.TaskModel
import retrofit2.Response
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val api: Api) : TaskRepository {
    override suspend fun getCalendarTaskListById(userId: Int): Response<ApiResponse> {
        return api.getCalendarTaskListById(ApiParams(userId = userId))
    }

    override suspend fun deleteTaskById(userId: Int, taskId: Int): Response<ApiResponse> {
        return api.deleteTaskById(ApiParams(userId = userId, taskId = taskId))
    }

    override suspend fun addTaskById(userId: Int, taskModel: TaskModel): Response<ApiResponse> {
        return api.storeCalendarTaskById(
            ApiParams(
                userId = userId,
                taskModel = taskModel.taskDetail
            )
        )
    }
}