package com.example.frndassignment.data

import com.example.frndassignment.domain.servermodels.TaskModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    /* Fetch Task List by Id*/

    @POST("getCalendarTaskList")
    suspend fun getCalendarTaskListById(@Body apiParams: ApiParams): Response<ApiResponse>


    /* Delete Task By Task Id */

    @POST("deleteCalendarTask")
    suspend fun deleteTaskById(@Body apiParams: ApiParams): Response<ApiResponse>


    /* Add Task By Id */

    @POST("storeCalendarTask")
    suspend fun storeCalendarTaskById(@Body apiParams: ApiParams): Response<ApiResponse>

}