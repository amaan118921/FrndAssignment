package com.example.frndassignment.utils

sealed class ResultState<T>(val result: T? = null, val msg: String? = null) {
    class Success<T>(data: T?) : ResultState<T>(data)
    class Loading<T>(data: T?) : ResultState<T>(data)
    class Error<T>(data: T? = null, errorMsg: String) : ResultState<T>(data, errorMsg)
}

fun <T> call(
    resultState: ResultState<T>,
    onFailure: ((msg: String?) -> Unit)? = null,
    onSuccess: (data: T?) -> Unit,
) {
    when (resultState) {
        is ResultState.Success -> {
            onSuccess(resultState.result)
        }

        is ResultState.Loading -> {}

        is ResultState.Error -> {
            onFailure?.invoke(resultState.msg)
        }
    }
}