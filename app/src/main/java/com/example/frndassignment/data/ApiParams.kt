package com.example.frndassignment.data

import com.example.frndassignment.domain.servermodels.TaskDetail
import com.example.frndassignment.domain.servermodels.TaskModel
import com.example.frndassignment.presentation.viewmodel.TaskList
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class ApiParams(
    @Json(name = "user_id")
    val userId: Int? = null,
    @Json(name = "task_id")
    val taskId: Int? = null,
    @Json(name = "task")
    val taskModel: TaskDetail? = null
)

data class ApiResponse(
    @Json(name = "status")
    val status: String? = null,
    @Json(name = "tasks")
    val tasks: TaskList? = null
)