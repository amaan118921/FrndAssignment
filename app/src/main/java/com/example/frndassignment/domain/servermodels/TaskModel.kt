package com.example.frndassignment.domain.servermodels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskModel(
    @Json(name = "task_id")
    val id: Int? = null,
    @Json(name = "task_detail")
    val taskDetail: TaskDetail? = null,

    @Transient
    var isExpanded: Boolean = false
) : Parcelable

@Parcelize
data class TaskDetail(
    @Json(name = "title")
    val title: String? = null,
    @Json(name = "description")
    val description: String? = null
) : Parcelable
