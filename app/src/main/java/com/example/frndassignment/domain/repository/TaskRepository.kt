package com.example.frndassignment.domain.repository

import com.example.frndassignment.data.ApiResponse
import com.example.frndassignment.domain.servermodels.TaskModel
import com.example.frndassignment.presentation.viewmodel.TaskList
import retrofit2.Response

interface TaskRepository {

    suspend fun getCalendarTaskListById(userId: Int): Response<ApiResponse>

    suspend fun deleteTaskById(userId: Int, taskId: Int): Response<ApiResponse>

    suspend fun addTaskById(userId: Int, taskModel: TaskModel): Response<ApiResponse>
}