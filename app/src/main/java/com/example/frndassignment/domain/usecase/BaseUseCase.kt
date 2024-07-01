package com.example.frndassignment.domain.usecase

import com.example.frndassignment.utils.ResultState
import com.example.frndassignment.utils.call
import kotlinx.coroutines.delay
import retrofit2.Response


interface BaseUseCase<I, O> {
    suspend fun process(params: I): ResultState<O>
}


// TODO: More Error Cases need to be handled

suspend fun <I, O> executeUseCase(
    useCase: BaseUseCase<I, O>,
    params: I,
    onFailure: ((msg: String?) -> Unit)? = null,
    onSuccess: (O?) -> Unit,
) {
    val result = useCase.process(params)
    delay(500)
    call(
        result,
        onFailure = onFailure
    ) {
        onSuccess(it)
    }

}


